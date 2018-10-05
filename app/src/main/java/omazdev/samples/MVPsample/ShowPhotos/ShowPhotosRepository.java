package omazdev.samples.MVPsample.ShowPhotos;


import omazdev.samples.MVPsample.entities.MyPhoto;

public interface ShowPhotosRepository {

    int PER_PAGE = 15;

    void searchPhotos(String tags);

    Boolean savePhoto(MyPhoto photo);
}
