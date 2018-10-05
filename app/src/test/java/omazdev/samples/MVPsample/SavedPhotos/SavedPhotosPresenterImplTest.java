package omazdev.samples.MVPsample.SavedPhotos;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import omazdev.samples.MVPsample.BaseTest;
import omazdev.samples.MVPsample.R;
import omazdev.samples.MVPsample.SavedPhotos.events.SavedPhotosEvent;
import omazdev.samples.MVPsample.SavedPhotos.ui.SavedPhotosView;
import omazdev.samples.MVPsample.entities.MyPhoto;
import omazdev.samples.MVPsample.libs.base.EventBus;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SavedPhotosPresenterImplTest extends BaseTest {

    @Mock private EventBus eventBus;
    @Mock private SavedPhotosView view;
    @Mock private GetPhotosInteractor getPhotosInteractor;
    @Mock private ManipulatePhotosInteractor manipulatePhotosInteractor;
    @Mock private MyPhoto photo;
    @Mock private SavedPhotosEvent event;
    @Mock private List<MyPhoto> photosList;

    private SavedPhotosPresenterImpl presenter;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        presenter = new SavedPhotosPresenterImpl(eventBus,view,getPhotosInteractor,manipulatePhotosInteractor);
    }

    @Test
    public void testOnCreate_subscribeToEventBus() throws Exception {
        presenter.onCreate();
        verify(eventBus,times(1)).register(presenter);
    }

    @Test
    public void testOnDestroy_unSubscribeToEventBus() throws Exception {
        presenter.onDestroy();
        verify(eventBus,times(1)).unregister(presenter);
        assertNull(presenter.getView());
    }

    @Test
    public void testGetPhotos_callInteractor() throws Exception {
        presenter.getPhotos();
        assertNotNull(presenter.getView());
        verify(view,times(1)).showProgress();
        verify(view,times(1)).hideUIElements();
        verify(getPhotosInteractor,times(1)).execute();
    }

    @Test
    public void testDeletePhoto_callInteractor() throws Exception {
        presenter.deletePhoto(photo);
        verify(manipulatePhotosInteractor,times(1)).execute(photo);
    }

    @Test
    public void testOnEventMainThread_READ_EVENT_photoListIsEmpty() throws Exception {
        int type = SavedPhotosEvent.READ_EVENT;
        when(event.getType()).thenReturn(type);
        when(event.getphotosList()).thenReturn(photosList);
        when(photosList.isEmpty()).thenReturn(true);

        presenter.onEventMainThread(event);

        assertNotNull(presenter.getView());
        verify(view,times(1)).hideProgress();
        verify(view,times(1)).showUIElements();
        verify(view,times(1)).showNoElementsTv();
    }

    @Test
    public void testOnEventMainThread_READ_EVENT_photoListIsNotEmpty() throws Exception {
        int type = SavedPhotosEvent.READ_EVENT;
        when(event.getType()).thenReturn(type);
        when(event.getphotosList()).thenReturn(photosList);
        when(photosList.isEmpty()).thenReturn(false);

        presenter.onEventMainThread(event);

        assertNotNull(presenter.getView());
        verify(view,times(1)).hideNoElementsTv();
        verify(view,times(1)).setPhotos(photosList);
    }

    @Test
    public void testOnEventMainThread_FAILURE_DELETE_EVENT_photoListIsNotEmpty() throws Exception {
        int type = SavedPhotosEvent.FAILURE_DELETE_EVENT;
        when(event.getType()).thenReturn(type);

        presenter.onEventMainThread(event);
        assertNotNull(presenter.getView());
        verify(view,times(1)).showError(R.string.delete_photo_failure);
    }

    @Test
    public void testOnEventMainThread_SUCCESS_DELETE_EVENT_photoListIsNotEmpty() throws Exception {
        int type = SavedPhotosEvent.SUCCESS_DELETE_EVENT;
        when(event.getType()).thenReturn(type);
        when(event.getphotosList()).thenReturn(photosList);
        when(event.getphotosList().get(0)).thenReturn(photo);

        presenter.onEventMainThread(event);
        assertNotNull(presenter.getView());
        verify(view,times(1)).onPhotoDeleted(photo);
        verify(view,times(1)).showSuccess(R.string.delete_photo_success);
    }

    @Test
    public void testOnPhotoRemoved_shouldShowNoElementsTv() throws Exception {
        presenter.onPhotoRemoved(true);

        assertNotNull(presenter.getView());
        verify(view).hideProgress();
        verify(view).hideUIElements();
        verify(view).showNoElementsTv();
    }

    @Test
    public void testShowClickedPhoto_photoListIsNotEmpty() throws Exception {
        String URL = "someURL";
        String TITLE = "someTITLE";

        Iterator<MyPhoto> iterator = mock(Iterator.class);
        ArrayList<String> photosUrlList = mock(ArrayList.class);
        ArrayList<String> photosTiltleList = mock(ArrayList.class);

        ArrayList<String> finalPhotosUrlList = new ArrayList<>();
        ArrayList<String> finalPhotosTiltleList = new ArrayList<>();

        finalPhotosUrlList.add(URL);
        finalPhotosUrlList.add(URL);
        finalPhotosTiltleList.add(TITLE);
        finalPhotosTiltleList.add(TITLE);

        when(photosList.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true,true, false);
        when(iterator.next()).thenReturn(photo);
        when(photo.getFlickrUrl()).thenReturn(URL);
        when(photo.getTitle()).thenReturn(TITLE);
        when(photosUrlList.add(URL)).thenReturn(true);
        when(photosTiltleList.add(URL)).thenReturn(true);
        when(photosUrlList.indexOf(URL)).thenReturn(0);

        presenter.showClickedPhoto(photo,photosList);

        assertNotNull(presenter.getView());
        verify(view,times(1))
                .openShowPhotosActivity(finalPhotosUrlList,finalPhotosTiltleList,0);
    }

}
