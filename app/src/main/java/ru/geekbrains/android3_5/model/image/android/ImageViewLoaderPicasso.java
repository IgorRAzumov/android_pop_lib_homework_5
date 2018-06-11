package ru.geekbrains.android3_5.model.image.android;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import io.reactivex.Single;
import ru.geekbrains.android3_5.model.cashe.ICache;
import ru.geekbrains.android3_5.model.image.ImageLoader;
import ru.geekbrains.android3_5.model.storage.IImageSaver;


public class ImageViewLoaderPicasso implements ImageLoader<ImageView> {
    private Target target;
    private IImageSaver imageSaver;
    private ICache cache;

    public ImageViewLoaderPicasso(IImageSaver imageSaver, ICache cache) {
        this.imageSaver = imageSaver;
        this.cache = cache;
    }

    @Override
    public void loadInto(@Nullable String url, ImageView container) {
        if (target != null) {
            target = null;
        }
        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                saveImage(bitmap, url);
                container.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.get().load(url).into(container);
    }


    Single<Boolean> saveImage(Bitmap bitmap, String url) {
        return Single.fromCallable(() -> {
            boolean isSaved = imageSaver.saveImage(bitmap, url);
            if (isSaved) {
                cache.saveImage(url);
            }
            return true;
        });
    }


}
