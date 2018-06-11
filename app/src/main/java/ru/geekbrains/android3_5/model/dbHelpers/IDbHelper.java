package ru.geekbrains.android3_5.model.dbHelpers;

public interface IDbHelper {
    void saveImage(String imageUrl, String imageFilePath);

    String getImage(String url);
}
