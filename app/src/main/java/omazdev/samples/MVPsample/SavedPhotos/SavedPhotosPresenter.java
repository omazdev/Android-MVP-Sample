package omazdev.samples.MVPsample.SavedPhotos;

import java.util.List;

import omazdev.samples.MVPsample.SavedPhotos.events.SavedPhotosEvent;
import omazdev.samples.MVPsample.SavedPhotos.ui.SavedPhotosView;

import omazdev.samples.MVPsample.entities.MyPhoto;


public interface SavedPhotosPresenter {
    void onCreate();
    void onDestroy();

    void getPhotos();
    void onGetPhotosSuccess(List<MyPhoto> photos);
   //void onGetPhotosFailure();

    void deletePhoto(MyPhoto photo);
    void onDeletePhotoSuccess(MyPhoto photo);
    void onDeletePhotoFailure();

    void onPhotoRemoved(Boolean photosListIsEmpty);

    void showClickedPhoto(MyPhoto photo, List<MyPhoto> photosList);

    void onEventMainThread(SavedPhotosEvent event);

    SavedPhotosView getView();

}
