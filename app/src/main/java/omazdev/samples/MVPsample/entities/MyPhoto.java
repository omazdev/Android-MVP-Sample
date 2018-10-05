package omazdev.samples.MVPsample.entities;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import omazdev.samples.MVPsample.api.response.Photo;
import omazdev.samples.MVPsample.db.MyPhotosDatabase;

@Table(database = MyPhotosDatabase.class)
public class MyPhoto extends BaseModel{

    @Column @PrimaryKey private String id;

    @Column private String secret;

    @Column private String server;

    @Column private int farm;

    @Column private String title;

    public MyPhoto() { }

    public MyPhoto(Photo photo) {
        this.id = photo.id;
        this.secret = photo.secret;
        this.server = photo.server;
        this.farm = photo.farm;
        this.title = photo.title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getFarm() {
        return farm;
    }

    public void setFarm(int farm) {
        this.farm = farm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFlickrUrl() {
        return "https://farm" + farm + ".staticflickr.com/" + server + "/" + id + "_" + secret + "_m.jpg";
    }

    public String getShortFlickrUrl() {
        return "https://farm" + farm + ".staticflickr.com/" + server + "/" + id + "_" + secret + "_s.jpg";
    }

}
