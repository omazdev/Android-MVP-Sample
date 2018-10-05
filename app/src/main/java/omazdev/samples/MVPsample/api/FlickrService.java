package omazdev.samples.MVPsample.api;

import omazdev.samples.MVPsample.api.response.PhotosSearchResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FlickrService {

@GET("/services/rest/?method=flickr.photos.search")
Call<PhotosSearchResponse> search(@Query("tags") String tags, @Query("per_page") int perPage);
}
