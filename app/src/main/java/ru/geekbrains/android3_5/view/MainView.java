package ru.geekbrains.android3_5.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Created by stanislav on 3/12/2018.
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface MainView extends MvpView
{
    void showAvatar(String avatarUrl);
    void showError(String message);
    void setUsername(String username);
    void showLoading();
    void hideLoading();
    void updateRepoList();
    void init();
}
