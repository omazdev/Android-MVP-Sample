package omazdev.samples.MVPsample.api.response;

public class Photo {

    public String id;
    public String secret;
    public String server;
    public int farm;
    public String title;

    public String getTitle() {
        return title;
    }

    //https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}_[mstzb].jpg
    public String getFlickrUrl(){
        return "https://farm"+farm+".staticflickr.com/"+server+"/"+id+"_"+secret+"_b.jpg";
    }

}
