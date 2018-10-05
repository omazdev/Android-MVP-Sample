package omazdev.samples.MVPsample.ShowPhotos.ui;


import omazdev.samples.MVPsample.api.response.Photo;

public interface ShowPhotosView {

    String TAGS = "tags";

    String PHOTOS_URL_LIST = "photosUrlList";
    String PHOTOS_TITLE_LIST = "photosTitleList";
    String PHOTO_INDEX = "photoIndex";

    void showError(int errorId , String errorSupp);
    void showSuccess(int success);

    void finishShowActivity();

    void loadPhoto(Photo photo);
    void loadPhoto(String photoUrl, String photoTitle);

    void showProgress();
    void hideProgress();
    void showUIElements();
    void hideUIElements();
    void makeUIElementsInvisible();

    void saveAnimation();
    void dismissAnimation();

    void changeButtonsSourceImg();

}
