package ru.geekbrains.android3_5.model.image.android;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.Single;
import ru.geekbrains.android3_5.model.image.ImageLoader;
import timber.log.Timber;

/**
 * Created by stanislav on 3/12/2018.
 */

public class ImageViewLoaderPicasso implements ImageLoader<ImageView> {
    private Target target;


    @Override
    public void loadInto(@Nullable String url, ImageView container) {
        if (target != null) {
            target = null;
        }
        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                saveImage(bitmap,url);
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


    public Single<Boolean> saveImage(Bitmap bitmap, String url) {
        return Single.fromCallable(() -> {
            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + url);
            try {
                file.createNewFile();
                FileOutputStream ostream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                ostream.flush();
                ostream.close();
            } catch (IOException e) {
                Timber.e("IOException" + e.getLocalizedMessage());
            }
            return true;
        });
    }


}
