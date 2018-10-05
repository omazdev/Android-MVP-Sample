package omazdev.samples.MVPsample.SavedPhotos.ui;

import java.util.ArrayList;
import java.util.List;

import omazdev.samples.MVPsample.entities.MyPhoto;

public interface SavedPhotosView {

    String PHOTOS_URL_LIST = "photosUrlList";
    String PHOTOS_TITLE_LIST = "photosTitleList";
    String PHOTO_INDEX = "photoIndex";

    void setPhotos(List<MyPhoto> photos);

    void onPhotoDeleted(MyPhoto photo);

    void openShowPhotosActivity(ArrayList<String> photosUrlList, ArrayList<String> photosTiltleList, int photoIndex);

    void showError(int errorId);
    void showSuccess(int successId);

    void showProgress();
    void hideProgress();
    void showNoElementsTv();
    void hideNoElementsTv();
    void showUIElements();
    void hideUIElements();

}
