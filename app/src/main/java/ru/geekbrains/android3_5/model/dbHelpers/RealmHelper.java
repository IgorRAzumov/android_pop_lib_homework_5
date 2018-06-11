package ru.geekbrains.android3_5.model.dbHelpers;

import io.realm.Realm;
import ru.geekbrains.android3_5.model.entity.realm.RealmImage;

public class RealmHelper implements IDbHelper {

    @Override
    public void saveImage(String imageUrl, String imageFilePath) {
        Realm realm = Realm.getDefaultInstance();
        RealmImage realmImage = realm.where(RealmImage.class).equalTo("url", imageUrl).findFirst();
        if (realmImage == null) {
            realm.executeTransaction(innerRealm -> {
                RealmImage newRealmImage = realm.createObject(RealmImage.class, imageUrl);
                newRealmImage.setImageFilePath(imageFilePath);
            });
        }
        realm.close();
    }


    @Override
    public String getImage(String url) {
        String imagePath = null;
        Realm realm = Realm.getDefaultInstance();
        RealmImage realmImage = realm.where(RealmImage.class).equalTo("url", url).findFirst();
        if (realmImage != null) {
            imagePath = realmImage.getImageFilePath();
        }
        return imagePath;
    }
}
