package omazdev.samples.MVPsample.api.response;

import java.util.ArrayList;
import java.util.List;

public class Photos {
    public int page;
    public int pages;
    public int perpage;
    public long total;
    public List<Photo> photo = new ArrayList<>();

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<Photo> getPhoto() {
        return photo;
    }

    public void setPhoto(List<Photo> photo) {
        this.photo = photo;
    }


}
