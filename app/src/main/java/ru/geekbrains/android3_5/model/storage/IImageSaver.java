package ru.geekbrains.android3_5.model.storage;

public interface IImageSaver<T> {
   boolean saveImage(T image, String url);
}
