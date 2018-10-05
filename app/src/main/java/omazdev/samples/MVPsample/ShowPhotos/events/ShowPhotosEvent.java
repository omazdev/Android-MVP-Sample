package omazdev.samples.MVPsample.ShowPhotos.events;

import java.util.Collections;
import java.util.List;

import omazdev.samples.MVPsample.api.response.Photo;

public class ShowPhotosEvent {
    private int type;
    private int errorId;
    private String errorSupplement;
    private List<Photo> photosList = Collections.emptyList();

    public final static int SEARCH_EVENT = 0;
    public final static int SAVE_SUCCESS_EVENT = 1;
    public final static int SAVE_FAILURE_EVENT = 2;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getErrorSupplement() {
        return errorSupplement;
    }

    public void setErrorSupplement(String errorSupplement) {
        this.errorSupplement = errorSupplement;
    }

    public List<Photo> getphotosList() {
        return photosList;
    }

    public void setphotosList(List<Photo> photosList) {
        this.photosList = photosList;
    }

    public int getErrorId() {
        return errorId;
    }

    public void setErrorId(int errorId) {
        this.errorId = errorId;
    }
}
