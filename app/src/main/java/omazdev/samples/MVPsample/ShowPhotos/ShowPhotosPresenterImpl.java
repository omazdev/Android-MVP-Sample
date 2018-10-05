package omazdev.samples.MVPsample.ShowPhotos;

import android.os.Handler;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import omazdev.samples.MVPsample.R;
import omazdev.samples.MVPsample.ShowPhotos.events.ShowPhotosEvent;
import omazdev.samples.MVPsample.ShowPhotos.ui.ShowPhotosView;
import omazdev.samples.MVPsample.api.response.Photo;
import omazdev.samples.MVPsample.entities.MyPhoto;
import omazdev.samples.MVPsample.libs.base.EventBus;

public class ShowPhotosPresenterImpl implements ShowPhotosPresenter {

    private EventBus eventBus;
    private ShowPhotosView view;
    private SearchPhotosInteractor searchPhotosInteractor;
    private SavePhotoInteractor savePhotoInteractor;

    private int photosIndex;
    private String showMode;
    private List<Photo> myPhotosList;
    private List<String> photosUrlList;
    private List<String> photosTiltleList;
    private MyPhoto photoToSave;//This object is added for testing purpose.
    private Handler handler;

    public ShowPhotosPresenterImpl(EventBus eventBus, ShowPhotosView view
            , SearchPhotosInteractor searchInteractor, SavePhotoInteractor savePhotoInteractor) {

        this.eventBus = eventBus;
        this.view = view;
        this.searchPhotosInteractor = searchInteractor;
        this.savePhotoInteractor = savePhotoInteractor;
        initVariables();
    }

    private void initVariables() {
        myPhotosList = Collections.emptyList();
        photosIndex = 0;
        photosUrlList = Collections.emptyList();
        photosTiltleList =Collections.emptyList();
        photoToSave = new MyPhoto();
        handler = new Handler();
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
        photosIndex = 0;
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
        view = null;
    }

    @Override
    public void checkIntent(String tags, ArrayList<String> photosUrlList
            , ArrayList<String> photosTiltleList, int photoToShowIndex) {
        if (this.view != null) {
            if (tags != null) {
                showMode = SEARCH_MODE;
                searchPhotos(tags);
            } else if (photosUrlList != null && photosTiltleList != null && photoToShowIndex != -1) {
                showMode = SAVE_MODE;
                photosIndex = photoToShowIndex;
                this.photosUrlList = photosUrlList;
                this.photosTiltleList = photosTiltleList;
                view.changeButtonsSourceImg();
                loadSavedPhoto( photosUrlList.get(photoToShowIndex), photosTiltleList.get(photoToShowIndex));
            }
        }
    }

    @Override
    public void showNextSavedPhoto() {
        photosIndex++;
        if (this.view != null) {
            if (photosIndex < photosUrlList.size()){
                view.saveAnimation();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.showProgress();
                        view.hideUIElements();
                        view.loadPhoto(photosUrlList.get(photosIndex), photosTiltleList.get(photosIndex));
                    }
                }, 500);

            }else{
                view.showError(R.string.search_photos_max_limit,null);
                view.finishShowActivity();
            }
        }
    }

    @Override
    public void showPreviousSavedPhoto() {
        photosIndex--;
        if (this.view != null) {
            if (photosIndex >= 0 && photosIndex < photosUrlList.size()){
                view.dismissAnimation();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.showProgress();
                        view.hideUIElements();
                        view.loadPhoto(photosUrlList.get(photosIndex), photosTiltleList.get(photosIndex));
                    }
                }, 500);

            }else{
                view.showError(R.string.search_photos_max_limit,null);
                view.finishShowActivity();
            }
        }
    }

    @Override
    public void onKeepPhoto() {
        if (showMode.equals(SEARCH_MODE) ){
            savePhoto();
        }else if (showMode.equals(SAVE_MODE)){
            showNextSavedPhoto();
        }
    }

    @Override
    public void onDismissPhoto() {
        if (showMode.equals(SEARCH_MODE) ){
            dismissPhoto();
        }else if (showMode.equals(SAVE_MODE)){
            showPreviousSavedPhoto();
        }
    }

    @Override
    public void searchPhotos(String tags) {
        view.showProgress();
        view.hideUIElements();
        searchPhotosInteractor.execute(tags);
    }

    private void loadSavedPhoto(String photoUrl, String photoTitle){
        view.showProgress();
        view.makeUIElementsInvisible();
        view.loadPhoto(photoUrl, photoTitle);
    }

    @Override
    @Subscribe
    public void onEventMainThread(ShowPhotosEvent event) {
        if (this.view != null){
            int errorId = event.getErrorId();
            String errorSupp = event.getErrorSupplement();
            if (errorId != -1){
                onSearchPhotosFailure( errorId ,  errorSupp);
            }else {
                if (event.getType() == ShowPhotosEvent.SEARCH_EVENT){
                    onSearchPhotosSuccess(event);
                }else if (event.getType() == ShowPhotosEvent.SAVE_FAILURE_EVENT){
                    onSavePhotoFailure();
                }else if (event.getType() == ShowPhotosEvent.SAVE_SUCCESS_EVENT){
                    onSavePhotoSuccess();
                }
            }
        }
    }

    @Override
    public void onSearchPhotosSuccess(ShowPhotosEvent successEvent) {
        myPhotosList = successEvent.getphotosList();
        if (this.view != null) {
            view.hideProgress();
            view.showUIElements();
            view.loadPhoto(myPhotosList.get(photosIndex));
        }
    }

    @Override
    public void onSearchPhotosFailure(int errorId , String errorSupp) {
        if (this.view != null) {
            view.hideProgress();
            view.showUIElements();
            view.showError( errorId ,  errorSupp);
            view.finishShowActivity();
        }

    }

    @Override
    public void onPhotoReady() {
        if (this.view != null) {
            view.hideProgress();
            view.showUIElements();
        }
    }

    @Override
    public void onPhotoFailed() {
        if (showMode.equals(SEARCH_MODE) ){
            loadNextPhoto();
        }else if (showMode.equals(SAVE_MODE)){
            showNextSavedPhoto();
        }
    }

    @Override
    public void onSavePhotoSuccess() {
        view.showSuccess(R.string.save_photo_success);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadNextPhoto();
            }
        }, 1000);
    }

    @Override
    public void onSavePhotoFailure() {
        view.showError(R.string.save_photo_failure,null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadNextPhoto();
            }
        }, 500);
    }

    @Override
    public void savePhoto() {
        if (this.view != null) {
            view.saveAnimation();
            photoToSave = new MyPhoto(myPhotosList.get(photosIndex));
            savePhotoInteractor.execute(photoToSave);
        }
    }

    @Override
    public void dismissPhoto() {
        if (this.view != null) {
            view.dismissAnimation();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadNextPhoto();
                }
            }, 500);
        }
    }

    @Override
    public void loadNextPhoto() {
        photosIndex++;
        if (this.view != null) {
            if (photosIndex < myPhotosList.size()){
                view.showProgress();
                view.hideUIElements();
                view.loadPhoto(myPhotosList.get(photosIndex));
            }else{
                view.showError(R.string.search_photos_max_limit,null);
                view.finishShowActivity();
                }
        }

    }

/*---------------------------GETTERS & SETTERS FOR TESTING---------------------------------------*/
/*----------------------------TODO remove for production-----------------------------------------*/

    @Override
    public ShowPhotosView getView() {
        return this.view;
    }

    public String getShowMode() {
        return showMode;
    }

    public int getPhotosIndex() {
        return photosIndex;
    }

    public void setPhotosIndex(int photosIndex) {
        this.photosIndex = photosIndex;
    }

    public List<String> getPhotosUrlList() {
        return photosUrlList;
    }

    public void setPhotosUrlList(List<String> photosUrlList) {
        this.photosUrlList = photosUrlList;
    }

    public List<String> getPhotosTiltleList() {
        return photosTiltleList;
    }

    public void setPhotosTiltleList(List<String> photosTiltleList) {
        this.photosTiltleList = photosTiltleList;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void setShowMode(String showMode) {
        this.showMode = showMode;
    }

    public void setMyPhotosList(List<Photo> myPhotosList) {
        this.myPhotosList = myPhotosList;
    }

    public MyPhoto getPhotoToSave() {
        return photoToSave;
    }

}
