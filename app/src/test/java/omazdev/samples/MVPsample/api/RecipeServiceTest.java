package omazdev.samples.MVPsample.api;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import omazdev.samples.MVPsample.BaseTest;
import omazdev.samples.MVPsample.api.response.PhotosSearchResponse;
import retrofit2.Call;
import retrofit2.Response;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 27)
public class RecipeServiceTest extends BaseTest {
    private FlickrService service;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        FlickrClient client = new FlickrClient();
        service = client.getFlickrService();
    }

    @Test
    public void doSearch_getPhotoFromBackend() throws Exception {
        String tags = "sky";
        int perPage = 15;

        Call<PhotosSearchResponse> call = service.search(tags,perPage);
        assertNotNull(call);

        Response<PhotosSearchResponse> response = call.execute();
        assertNotNull(response);
        assertTrue(response.isSuccessful());

        PhotosSearchResponse photosSearchResponse = response.body();
        assertNotNull(photosSearchResponse);
        assertEquals("ok",photosSearchResponse.stat);
        assertEquals(15,photosSearchResponse.photos.photo.size());

    }
}
