package ru.geekbrains.android3_5.model.cashe;

import android.support.annotation.Nullable;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import ru.geekbrains.android3_5.model.entity.Repository;
import ru.geekbrains.android3_5.model.entity.User;
import ru.geekbrains.android3_5.model.entity.activeandroid.AARepository;
import ru.geekbrains.android3_5.model.entity.activeandroid.AAUser;

public class AACache implements ICache {

    @Override
    public User getUser(String userName) {
        User user = null;
        AAUser aaUser = new Select()
                .from(AAUser.class)
                .where("login = ?", userName)
                .executeSingle();
        if (aaUser != null) {
            user = new User(aaUser.login, aaUser.avatarUrl, aaUser.reposUrl);
        }
        return user;
    }

    @Override
    public List<Repository> getUserRepos(User user) {
        List<Repository> repositories = null;
        AAUser aaUser = getUserFromCache(user.getLogin());
        if (aaUser != null) {
            repositories = new ArrayList<>();
            for (AARepository aaRepository : aaUser.repositories()) {
                repositories.add(new Repository(aaRepository.id, aaRepository.name));
            }
        }
        return repositories;
    }

    @Override
    public void putUser(User user) {
        AAUser aaUser = getUserFromCache(user.getLogin());
        if (aaUser == null) {
            aaUser = new AAUser();
            aaUser.login = user.getLogin();
        }

        aaUser.avatarUrl = user.getAvatarUrl();
        aaUser.save();
    }

    @Override
    public void putUserRepos(User user, List<Repository> repositories) {
        AAUser aaUser = getUserFromCache(user.getLogin());
        if (aaUser == null) {
            aaUser = new AAUser();
            aaUser.login = user.getLogin();
            aaUser.avatarUrl = user.getAvatarUrl();
            aaUser.save();
        }

        new Delete().from(AARepository.class).where("user = ?", aaUser.getId()).execute();

        ActiveAndroid.beginTransaction();
        try {
            for (Repository repository : repositories) {
                AARepository aaRepository = new AARepository();
                aaRepository.id = repository.getId();
                aaRepository.name = repository.getName();
                aaRepository.user = aaUser;
                aaRepository.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

    private AAUser getUserFromCache(String login) {
        return new Select()
                .from(AAUser.class)
                .where("login = ?", login)
                .executeSingle();

    }
}
