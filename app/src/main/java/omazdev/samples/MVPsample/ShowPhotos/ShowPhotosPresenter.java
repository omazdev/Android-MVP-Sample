package omazdev.samples.MVPsample.ShowPhotos;

import java.util.ArrayList;

import omazdev.samples.MVPsample.ShowPhotos.events.ShowPhotosEvent;
import omazdev.samples.MVPsample.ShowPhotos.ui.ShowPhotosView;

public interface ShowPhotosPresenter {

    String SEARCH_MODE = "showSearched";
    String SAVE_MODE = "showSaved";

    void onCreate();
    void onDestroy();

    String getShowMode();

    void checkIntent(String tags,ArrayList<String> photosUrlList, ArrayList<String> photosTiltleList, int photoIndex);

    void showNextSavedPhoto();
    void showPreviousSavedPhoto();

    void onKeepPhoto();
    void onDismissPhoto();

    void searchPhotos(String tags);
    void onSearchPhotosSuccess(ShowPhotosEvent showPhotosEvent);
    void onSearchPhotosFailure(int errorId , String errorSupp);

    void onPhotoReady();
    void onPhotoFailed();

    void onSavePhotoSuccess();
    void onSavePhotoFailure();

    void loadNextPhoto();
    void savePhoto();
    void dismissPhoto();

    void onEventMainThread(ShowPhotosEvent event);

    ShowPhotosView getView();

}
