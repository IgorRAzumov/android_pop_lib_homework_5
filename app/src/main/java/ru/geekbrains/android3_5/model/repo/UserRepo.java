package ru.geekbrains.android3_5.model.repo;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import ru.geekbrains.android3_5.model.api.ApiHolder;
import ru.geekbrains.android3_5.model.cashe.ICache;
import ru.geekbrains.android3_5.model.common.NetworkStatus;
import ru.geekbrains.android3_5.model.entity.Repository;
import ru.geekbrains.android3_5.model.entity.User;


public class UserRepo {
    private ICache cache;

    public UserRepo(ICache cache) {
        this.cache = cache;
    }

    public Observable<User> getUser(String username) {
        if (NetworkStatus.isOffline()) {
            return Observable.create(emitter -> {
                User user = cache.getUser(username);
                if (user == null) {
                    emitter.onError(new RuntimeException("No such user in cache: " + username));
                } else {
                    emitter.onNext(user);
                    emitter.onComplete();
                }
            });
        } else {
            return ApiHolder
                    .getApi()
                    .getUser(username)
                    .subscribeOn(Schedulers.io())
                    .map(user -> {
                        cache.putUser(user);
                        return user;
                    });
        }
    }

    public Observable<List<Repository>> getUserRepos(User user) {
        if (NetworkStatus.isOffline()) {
            return Observable.create(emitter -> {
                List<Repository> repositories = cache.getUserRepos(user);
                if (repositories == null) {
                    emitter.onError(new RuntimeException("No such user in cache: " + user.getLogin()));
                } else {
                    emitter.onNext(repositories);
                    emitter.onComplete();
                }
            });
        } else {
            return ApiHolder
                    .getApi()
                    .getUserRepos(user.getReposUrl())
                    .subscribeOn(Schedulers.io())
                    .map(repositories -> {
                        cache.putUserRepos(user, repositories);
                        return repositories;
                    });
        }
    }
}
