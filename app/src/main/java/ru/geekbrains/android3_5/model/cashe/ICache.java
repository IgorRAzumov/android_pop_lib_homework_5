package ru.geekbrains.android3_5.model.cashe;

import android.support.annotation.Nullable;

import java.util.List;

import ru.geekbrains.android3_5.model.entity.Repository;
import ru.geekbrains.android3_5.model.entity.User;

public interface ICache {
    @Nullable
    User getUser(String login);

    @Nullable
    List<Repository> getUserRepos(User user);

    void putUser(User user);

    void putUserRepos(User user, List<Repository> repositories);
}
