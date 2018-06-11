package ru.geekbrains.android3_5.model.image.android;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import ru.geekbrains.android3_5.model.dbHelpers.IDbHelper;
import ru.geekbrains.android3_5.model.image.IImageSaver;
import ru.geekbrains.android3_5.model.image.ImageLoader;


public class ImageViewLoaderPicasso implements ImageLoader<ImageView> {
    private Target target;
    private IImageSaver<Bitmap> imageSaver;
    private IDbHelper dbHelper;

    public ImageViewLoaderPicasso(IImageSaver imageSaver, IDbHelper dbHelper) {
        this.imageSaver = imageSaver;
        this.dbHelper = dbHelper;
    }

    @Override
    public void loadInto(@Nullable String url, ImageView container) {
        if (target != null) {
            target = null;
        }
        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                saveImage(bitmap, url)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
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
            boolean isSaved = false;
            String filePath = imageSaver.saveImage(bitmap, url);
            if (filePath != null && TextUtils.isEmpty(filePath)) {
                dbHelper.saveImage(url, filePath);
                isSaved = true;
            }
            return isSaved;
        });
    }


}
