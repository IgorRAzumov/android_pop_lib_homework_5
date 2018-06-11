package ru.geekbrains.android3_5.model.image;

public interface IImageSaver<T> {
   String saveImage(T image, String url);
}
