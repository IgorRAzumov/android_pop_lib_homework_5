package ru.geekbrains.android3_5.model.cashe;

import android.support.annotation.Nullable;

import java.util.List;

import io.paperdb.Paper;
import ru.geekbrains.android3_5.model.common.Utils;
import ru.geekbrains.android3_5.model.entity.Repository;
import ru.geekbrains.android3_5.model.entity.User;

public class PaperCache implements ICache {

    @Override
    public User getUser(String login) {
        User user = null;
        if (Paper.book("users").contains(login)) {
            return Paper.book("users").read(login);
        }
        return user;
    }

    @Override
    public List<Repository> getUserRepos(User user) {
        String md5 = Utils.MD5(user.getReposUrl());
        List<Repository> repositories = null;
        if (Paper.book("repos").contains(md5)) {
            repositories = Paper.book("repos").read(md5);
        }
        return repositories;
    }

    @Override
    public void putUser(User user) {
        Paper.book("users").write(user.getLogin(), user);
    }

    @Override
    public void putUserRepos(User user, List<Repository> repositories) {
        String md5 = Utils.MD5(user.getReposUrl());
        Paper.book("repos").write(md5, repositories);
    }
}
