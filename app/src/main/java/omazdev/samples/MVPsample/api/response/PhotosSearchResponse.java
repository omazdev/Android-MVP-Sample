package omazdev.samples.MVPsample.api.response;

public class PhotosSearchResponse {
     public Photos photos;
     public String stat;

     public Photos getPhotos() {
          return photos;
     }

     public void setPhotos(Photos photos) {
          this.photos = photos;
     }

     public String getStat() {
          return stat;
     }

     public void setStat(String stat) {
          this.stat = stat;
     }


}
