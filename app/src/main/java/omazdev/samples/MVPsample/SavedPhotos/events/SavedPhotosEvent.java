package omazdev.samples.MVPsample.SavedPhotos.events;

import java.util.Collections;
import java.util.List;

import omazdev.samples.MVPsample.entities.MyPhoto;


public class SavedPhotosEvent {
    private int type;
    private int errorId;
    private List<MyPhoto> photosList = Collections.emptyList();

    public final static int READ_EVENT = 0;
    public final static int SUCCESS_DELETE_EVENT = 1;
    public final static int FAILURE_DELETE_EVENT = 2;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getErrorId() {
        return errorId;
    }

    public void setErrorId(int errorId) {
        this.errorId = errorId;
    }

    public List<MyPhoto> getphotosList() {
        return photosList;
    }

    public void setphotosList(List<MyPhoto> photosList) {
        this.photosList = photosList;
    }

}
