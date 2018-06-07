package ru.geekbrains.android3_5.model.cashe;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import ru.geekbrains.android3_5.model.entity.Repository;
import ru.geekbrains.android3_5.model.entity.User;
import ru.geekbrains.android3_5.model.entity.realm.RealmRepository;
import ru.geekbrains.android3_5.model.entity.realm.RealmUser;

public class RealmCache implements ICache {

    @Override
    public User getUser(String login) {
        User user = null;
        Realm realm = Realm.getDefaultInstance();
        RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", login).findFirst();
        if (realmUser != null) {
            user = new User(realmUser.getLogin(), realmUser.getAvatarUrl(), realmUser.getReposUrl());
        }
        return user;
    }

    @Override
    public List<Repository> getUserRepos(User user) {
        List<Repository> repositories = null;
        Realm realm = Realm.getDefaultInstance();
        RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", user.getLogin()).findFirst();
        if (realmUser != null) {
            repositories = new ArrayList<>();
            for (RealmRepository realmRepository : realmUser.getRepos()) {
                repositories.add(new Repository(realmRepository.getId(), realmRepository.getName()));
            }
        }
        return repositories;
    }

    @Override
    public void putUser(User user) {
        Realm realm = Realm.getDefaultInstance();
        RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", user.getLogin()).findFirst();
        if (realmUser == null) {
            realm.executeTransaction(innerRealm -> {
                RealmUser newRealmUser = realm.createObject(RealmUser.class, user.getLogin());
                newRealmUser.setAvatarUrl(user.getAvatarUrl());
                newRealmUser.setReposUrl(user.getReposUrl());
            });
        }
        realm.close();
    }

    @Override
    public void putUserRepos(User user, List<Repository> repositories) {
        Realm realm = Realm.getDefaultInstance();
        RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", user.getLogin()).findFirst();
        if (realmUser == null) {
            realm.executeTransaction(innerRealm -> {
                RealmUser newRealmUser = realm.createObject(RealmUser.class, user.getLogin());
                newRealmUser.setAvatarUrl(user.getAvatarUrl());
                newRealmUser.setReposUrl(user.getReposUrl());
            });
        }

        final RealmUser finalRealmUser = realm.where(RealmUser.class)
                .equalTo("login", user.getLogin()).findFirst();
        realm.executeTransaction(innerRealm -> {
            assert finalRealmUser != null;
            finalRealmUser.getRepos().deleteAllFromRealm();
            for (Repository repository : repositories) {
                RealmRepository realmRepository = innerRealm.createObject(RealmRepository.class,
                        repository.getId());
                realmRepository.setName(repository.getName());
                finalRealmUser.getRepos().add(realmRepository);
            }
        });
        realm.close();
    }
}
