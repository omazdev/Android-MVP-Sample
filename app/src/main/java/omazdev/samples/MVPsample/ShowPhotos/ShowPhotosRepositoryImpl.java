package omazdev.samples.MVPsample.ShowPhotos;

import java.util.List;

import omazdev.samples.MVPsample.R;
import omazdev.samples.MVPsample.ShowPhotos.events.ShowPhotosEvent;
import omazdev.samples.MVPsample.api.FlickrService;

import omazdev.samples.MVPsample.api.response.Photo;
import omazdev.samples.MVPsample.api.response.PhotosSearchResponse;

import omazdev.samples.MVPsample.entities.MyPhoto;
import omazdev.samples.MVPsample.libs.base.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowPhotosRepositoryImpl implements ShowPhotosRepository {

    private EventBus eventBus;
    private FlickrService service;

    public ShowPhotosRepositoryImpl(EventBus eventBus, FlickrService service) {
        this.eventBus = eventBus;
        this.service = service;
    }

    @Override
    public void searchPhotos(String tags) {
        Call<PhotosSearchResponse> call = service.search( tags, PER_PAGE);
        Callback<PhotosSearchResponse> callback = new Callback<PhotosSearchResponse>() {
            @Override
            public void onResponse(Call<PhotosSearchResponse> call, Response<PhotosSearchResponse> response) {
                if (response.isSuccessful()) {
                    PhotosSearchResponse photosSearchResponse = response.body();

                    if (photosSearchResponse != null && photosSearchResponse.getPhotos().getTotal() != 0
                            && photosSearchResponse.getPhotos().getPhoto() != null){

                            post(photosSearchResponse.getPhotos().getPhoto());

                    } else {
                        post(R.string.search_api_response_error,null);
                    }
                } else {
                    post(R.string.search_api_response_error, null);
                }
            }

            @Override
            public void onFailure(Call<PhotosSearchResponse> call, Throwable t) {
                post(R.string.search_api_endpoint_error,t.getLocalizedMessage());
            }
        };

        call.enqueue(callback);
    }

    @Override
    public Boolean savePhoto(MyPhoto photo) {
        Boolean saveSuccess = photo.save();
        if (saveSuccess){
            post(ShowPhotosEvent.SAVE_SUCCESS_EVENT);
        }else {
            post(ShowPhotosEvent.SAVE_FAILURE_EVENT);
        }
        return saveSuccess;
    }

    private void post(int errorId, String errorSupp, int type, List<Photo> photosList){
        ShowPhotosEvent event = new ShowPhotosEvent();
        event.setErrorId(errorId);
        event.setErrorSupplement(errorSupp);
        event.setType(type);
        event.setphotosList(photosList);
        eventBus.post(event);
    }

    private void post(List<Photo> photosList){
        post(-1,null, ShowPhotosEvent.SEARCH_EVENT, photosList);
    }

    private void post(int errorId,String errorSupp){
        post(errorId , errorSupp, ShowPhotosEvent.SEARCH_EVENT, null);
    }

    private void post(int saveEventType){
        post(-1,null, saveEventType, null);
    }

}
