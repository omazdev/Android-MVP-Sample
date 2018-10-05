package omazdev.samples.MVPsample.SavedPhotos;

import omazdev.samples.MVPsample.entities.MyPhoto;

public interface SavedPhotosRepository {
    void getPhotos();
    void deletePhoto(MyPhoto photo);
}
