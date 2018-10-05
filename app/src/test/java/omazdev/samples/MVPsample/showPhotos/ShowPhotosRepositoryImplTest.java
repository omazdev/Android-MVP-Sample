package omazdev.samples.MVPsample.showPhotos;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.io.IOException;
import java.util.Arrays;

import omazdev.samples.MVPsample.BaseTest;
import omazdev.samples.MVPsample.R;
import omazdev.samples.MVPsample.ShowPhotos.ShowPhotosRepository;
import omazdev.samples.MVPsample.ShowPhotos.ShowPhotosRepositoryImpl;
import omazdev.samples.MVPsample.ShowPhotos.events.ShowPhotosEvent;
import omazdev.samples.MVPsample.api.FlickrService;
import omazdev.samples.MVPsample.api.response.Photo;
import omazdev.samples.MVPsample.api.response.Photos;
import omazdev.samples.MVPsample.api.response.PhotosSearchResponse;
import omazdev.samples.MVPsample.entities.MyPhoto;
import omazdev.samples.MVPsample.libs.base.EventBus;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ShowPhotosRepositoryImplTest extends BaseTest {

    @Mock private EventBus eventBus;
    @Mock private FlickrService service;
    @Mock private Photo photo;
    @Mock private MyPhoto mySavedPhoto;

    private ArgumentCaptor<ShowPhotosEvent> showPhotosEventArgumentCaptor;

    private ShowPhotosRepositoryImpl repository;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        repository = new ShowPhotosRepositoryImpl( eventBus,  service);
        showPhotosEventArgumentCaptor = ArgumentCaptor.forClass(ShowPhotosEvent.class);
    }

    @Test
    public void testSearchPhotos_APIServiceCallSuccess_EventPosted() throws Exception  {
        String tags = "someTag";
        when(service.search(tags,ShowPhotosRepository.PER_PAGE)).thenReturn(buildCall(true,null));

        repository.searchPhotos(tags);
        verify(service).search(tags,ShowPhotosRepository.PER_PAGE);
        verify(eventBus).post(showPhotosEventArgumentCaptor.capture());
        ShowPhotosEvent event = showPhotosEventArgumentCaptor.getValue();
        assertEquals(ShowPhotosEvent.SEARCH_EVENT,event.getType());
        assertNotNull(event.getphotosList());
        assertNull(event.getErrorSupplement());
        assertEquals(-1,event.getErrorId());
    }

    @Test
    public void testSearchPhotos_APIServiceCallFailure_EventPosted() throws Exception  {
        String tags = "someTag";
        String errorMsg = "error";

        when(service.search(tags,ShowPhotosRepository.PER_PAGE)).thenReturn(buildCall(false,errorMsg));

        repository.searchPhotos(tags);

        verify(service).search(tags,ShowPhotosRepository.PER_PAGE);
        verify(eventBus).post(showPhotosEventArgumentCaptor.capture());
        ShowPhotosEvent event = showPhotosEventArgumentCaptor.getValue();
        assertEquals(ShowPhotosEvent.SEARCH_EVENT,event.getType());
        assertNull(event.getphotosList());
        assertEquals(errorMsg,event.getErrorSupplement());
        assertEquals(R.string.search_api_endpoint_error,event.getErrorId());
    }

    @Test
    public void savePhoto_saveSuccess_EventPosted() throws Exception  {
        when(mySavedPhoto.save()).thenReturn(true);
        repository.savePhoto(mySavedPhoto);

        verify(eventBus).post(showPhotosEventArgumentCaptor.capture());
        ShowPhotosEvent event = showPhotosEventArgumentCaptor.getValue();
        assertEquals(ShowPhotosEvent.SAVE_SUCCESS_EVENT,event.getType());
        assertNull(event.getphotosList());
        assertNull(event.getErrorSupplement());
        assertEquals(-1,event.getErrorId());
    }

    @Test
    public void savePhoto_saveFailure_EventPosted() throws Exception  {
        when(mySavedPhoto.save()).thenReturn(false);
        repository.savePhoto(mySavedPhoto);

        verify(eventBus).post(showPhotosEventArgumentCaptor.capture());
        ShowPhotosEvent event = showPhotosEventArgumentCaptor.getValue();
        assertEquals(ShowPhotosEvent.SAVE_FAILURE_EVENT,event.getType());
        assertNull(event.getphotosList());
        assertNull(event.getErrorSupplement());
        assertEquals(-1,event.getErrorId());
    }

    private Call<PhotosSearchResponse> buildCall(final Boolean success, final String errorMsg){

        return new Call<PhotosSearchResponse>() {
            @Override
            public Response<PhotosSearchResponse> execute() throws IOException {
                Response<PhotosSearchResponse> result = null;
                if (success){
                    PhotosSearchResponse response1 = new PhotosSearchResponse();

                    Photos photos = new Photos();
                    photos.setTotal(15);
                    photos.setPhoto(Arrays.asList(photo));

                    response1.setPhotos(photos);
                    result = Response.success(response1);

                }else {
                    result = Response.error(null, null);
                }

                return result;
            }

            @Override
            public void enqueue(Callback<PhotosSearchResponse> callback) {
                if (success) {
                    try {
                        callback.onResponse(this, execute());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    callback.onFailure(this, new Throwable(errorMsg));
                }
            }

            @Override
            public boolean isExecuted() {
                return false;
            }

            @Override
            public void cancel() {

            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public Call<PhotosSearchResponse> clone() {
                return null;
            }

            @Override
            public Request request() {
                return null;
            }
        };
    }
}
