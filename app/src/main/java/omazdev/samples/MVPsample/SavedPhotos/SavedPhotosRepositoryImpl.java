package omazdev.samples.MVPsample.SavedPhotos;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

import omazdev.samples.MVPsample.SavedPhotos.events.SavedPhotosEvent;
import omazdev.samples.MVPsample.entities.MyPhoto;
import omazdev.samples.MVPsample.libs.base.EventBus;

public class SavedPhotosRepositoryImpl implements SavedPhotosRepository{

    private EventBus eventBus;

    public SavedPhotosRepositoryImpl(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void getPhotos() {
        List<MyPhoto> storedPhotos = SQLite.select().from(MyPhoto.class).queryList();
        post(storedPhotos);
    }

    @Override
    public void deletePhoto(MyPhoto photo) {
        Boolean deleteSuccess = photo.delete();
        List<MyPhoto> myPhoto = new ArrayList<>();
        myPhoto.add(0,photo);
        if (deleteSuccess){
            post(myPhoto,SavedPhotosEvent.SUCCESS_DELETE_EVENT);
        }else {
            post(myPhoto,SavedPhotosEvent.FAILURE_DELETE_EVENT);
        }
    }

    private void post( int errorId, int type, List<MyPhoto> photosList){
        SavedPhotosEvent event = new SavedPhotosEvent();
        event.setErrorId(errorId);
        event.setType(type);
        event.setphotosList(photosList);
        eventBus.post(event);
    }

    private void post(List<MyPhoto> photosList, int type){
        post(-1, type, photosList);
    }

    private void post(List<MyPhoto> photosList){
        post(-1, SavedPhotosEvent.READ_EVENT, photosList);
    }


}
