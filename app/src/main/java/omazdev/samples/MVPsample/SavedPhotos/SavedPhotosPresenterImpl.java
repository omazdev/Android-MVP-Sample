package omazdev.samples.MVPsample.SavedPhotos;


import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import omazdev.samples.MVPsample.R;
import omazdev.samples.MVPsample.SavedPhotos.events.SavedPhotosEvent;
import omazdev.samples.MVPsample.SavedPhotos.ui.SavedPhotosView;
import omazdev.samples.MVPsample.entities.MyPhoto;
import omazdev.samples.MVPsample.libs.base.EventBus;

public class SavedPhotosPresenterImpl implements SavedPhotosPresenter {

    private EventBus eventBus;
    private SavedPhotosView view;
    private GetPhotosInteractor getPhotosInteractor;
    private ManipulatePhotosInteractor manipulatePhotosInteractor;

    private ArrayList<String> photosUrlList;
    private ArrayList<String> photosTiltleList;

    public SavedPhotosPresenterImpl(EventBus eventBus, SavedPhotosView view
                                  , GetPhotosInteractor getPhotosInteractor
                                  , ManipulatePhotosInteractor manipulatePhotosInteractor) {
        this.eventBus = eventBus;
        this.view = view;
        this.getPhotosInteractor = getPhotosInteractor;
        this.manipulatePhotosInteractor = manipulatePhotosInteractor;
        photosUrlList = new ArrayList<>();
        photosTiltleList = new ArrayList<>();
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
        view = null;
    }

    @Override
    public void getPhotos() {
        if (this.view != null) {
            view.showProgress();
            view.hideUIElements();
            getPhotosInteractor.execute();
        }
    }

    @Override
    public void deletePhoto(MyPhoto photo) {
        manipulatePhotosInteractor.execute(photo);
    }

    @Override
    @Subscribe
    public void onEventMainThread(SavedPhotosEvent event) {
        int type = event.getType();
        if (type == SavedPhotosEvent.READ_EVENT){
            onGetPhotosSuccess(event.getphotosList());
        }else if (type == SavedPhotosEvent.FAILURE_DELETE_EVENT){
            onDeletePhotoFailure();
        }else if (type == SavedPhotosEvent.SUCCESS_DELETE_EVENT){
            onDeletePhotoSuccess(event.getphotosList().get(0));
        }
    }

    @Override
    public void onGetPhotosSuccess(List<MyPhoto> photos) {
        if (this.view != null){
            view.hideProgress();
            view.showUIElements();
            if (photos.isEmpty()){
                view.showNoElementsTv();
            }else {
                view.hideNoElementsTv();
                view.setPhotos(photos);
            }

        }
    }

    @Override
    public void onDeletePhotoSuccess(MyPhoto photo ) {
        if (view != null){
            view.onPhotoDeleted(photo);
            view.showSuccess(R.string.delete_photo_success);
        }
    }

    @Override
    public void onDeletePhotoFailure() {
        if (view != null){
            view.showError(R.string.delete_photo_failure);
        }
    }

    @Override
    public void onPhotoRemoved(Boolean isPhotosListEmpty) {
        if (view != null && isPhotosListEmpty){
            view.hideProgress();
            view.hideUIElements();
            view.showNoElementsTv();
        }
    }

    @Override
    public void showClickedPhoto(MyPhoto photo, List<MyPhoto> photosList) {
        photosUrlList = new ArrayList<>();
        photosTiltleList= new ArrayList<>();
        for (MyPhoto thisPhoto:
             photosList) {
            photosUrlList.add(thisPhoto.getFlickrUrl());
            photosTiltleList.add(thisPhoto.getTitle());
        }

        int myPhotoIndex = photosUrlList.indexOf(photo.getFlickrUrl());

        if (view != null){
            view.openShowPhotosActivity(photosUrlList, photosTiltleList, myPhotoIndex);
        }

    }

    @Override
    public SavedPhotosView getView() {
        return this.view;
    }
}
