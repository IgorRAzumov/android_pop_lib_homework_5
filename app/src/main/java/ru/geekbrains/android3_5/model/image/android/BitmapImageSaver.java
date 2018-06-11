package ru.geekbrains.android3_5.model.image.android;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import ru.geekbrains.android3_5.model.image.IImageSaver;
import timber.log.Timber;

public class BitmapImageSaver implements IImageSaver<Bitmap> {

    @Override
    public String saveImage(Bitmap image, String url) {
        String imageFilePath = null;
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + url);
        try {
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            image.compress(android.graphics.Bitmap.CompressFormat.JPEG, 80, outputStream);
            outputStream.flush();
            outputStream.close();
            imageFilePath = file.getAbsolutePath();
        } catch (IOException e) {
            Timber.e("IOException%s", e.getLocalizedMessage());
        }
        return imageFilePath;
    }
}
