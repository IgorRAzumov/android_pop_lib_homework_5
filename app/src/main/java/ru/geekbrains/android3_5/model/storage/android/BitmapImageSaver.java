package ru.geekbrains.android3_5.model.storage.android;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import ru.geekbrains.android3_5.model.storage.IImageSaver;
import timber.log.Timber;

public class BitmapImageSaver implements IImageSaver<Bitmap> {

    @Override
    public boolean saveImage(Bitmap image, String url) {
        boolean isCompleted = false;
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + url);
        try {
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            image.compress(android.graphics.Bitmap.CompressFormat.JPEG, 80, outputStream);
            outputStream.flush();
            outputStream.close();
            isCompleted = true;
        } catch (IOException e) {
            Timber.e("IOException%s", e.getLocalizedMessage());
        }
        return isCompleted;
    }
}
