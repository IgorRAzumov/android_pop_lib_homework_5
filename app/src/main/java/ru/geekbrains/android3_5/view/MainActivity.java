package ru.geekbrains.android3_5.view;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import ru.geekbrains.android3_5.R;
import ru.geekbrains.android3_5.model.dbHelpers.RealmHelper;
import ru.geekbrains.android3_5.model.image.ImageLoader;
import ru.geekbrains.android3_5.model.image.android.BitmapImageSaver;
import ru.geekbrains.android3_5.model.image.android.ImageLoaderGlide;
import ru.geekbrains.android3_5.model.image.android.ImageViewLoaderPicasso;
import ru.geekbrains.android3_5.presenter.MainPresenter;

public class MainActivity extends MvpAppCompatActivity implements MainView {
    @BindView(R.id.iv_avatar)
    ImageView avatarImageView;
    @BindView(R.id.tv_error)
    TextView errorTextView;
    @BindView(R.id.tv_username)
    TextView usernameTextView;
    @BindView(R.id.pb_loading)
    ProgressBar loadingProgressBar;
    @BindView(R.id.rv_repos)
    RecyclerView reposRecyclerView;

    RepoRVAdapter adapter;

    @InjectPresenter
    MainPresenter presenter;

    ImageLoader<ImageView> imageLoader;


    Target target;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        target = initTarget();
        imageLoader = new ImageViewLoaderPicasso(new BitmapImageSaver(),new RealmHelper());

        reposRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reposRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private Target initTarget() {
        return new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
    }

    @Override
    public void init() {
        adapter = new RepoRVAdapter(presenter);
        reposRecyclerView.setAdapter(adapter);
    }

    @ProvidePresenter
    public MainPresenter provideMainPresenter() {
        return new MainPresenter(AndroidSchedulers.mainThread());
    }


    @Override
    public void showAvatar(String avatarUrl) {
        imageLoader.loadInto(avatarUrl, avatarImageView);

    }

    @Override
    public void showError(String message) {
        errorTextView.setText(message);
    }

    @Override
    public void showLoading() {
        loadingProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void updateRepoList() {
        adapter.notifyDataSetChanged();
    }


    @Override
    public void setUsername(String username) {
        usernameTextView.setText(username);
    }
}
