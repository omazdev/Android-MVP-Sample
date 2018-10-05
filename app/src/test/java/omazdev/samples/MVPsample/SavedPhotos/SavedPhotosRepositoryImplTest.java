package omazdev.samples.MVPsample.SavedPhotos;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import omazdev.samples.MVPsample.BaseTest;
import omazdev.samples.MVPsample.MainApplication;
import omazdev.samples.MVPsample.SavedPhotos.events.SavedPhotosEvent;
import omazdev.samples.MVPsample.entities.MyPhoto;
import omazdev.samples.MVPsample.libs.base.EventBus;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 27)
public class SavedPhotosRepositoryImplTest extends BaseTest {

    @Mock private EventBus eventBus;

    private MainApplication app;
    private SavedPhotosRepositoryImpl repository;
    private ArgumentCaptor<SavedPhotosEvent> argumentCaptor;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        app = (MainApplication) RuntimeEnvironment.application;
        repository = new SavedPhotosRepositoryImpl(eventBus);
        argumentCaptor = ArgumentCaptor.forClass(SavedPhotosEvent.class);
        app.onCreate();
    }

    @After
    public void tearDown() throws Exception {
        app.onTerminate();
    }

    @Test
    public void getPhotos_EventPosted() throws Exception {
//        int photosToStore = 2;
        List<MyPhoto> storedPhotos = new ArrayList<>();

        MyPhoto photo1 = new MyPhoto();
        photo1.setId("1");
        photo1.save();

        MyPhoto photo2 = new MyPhoto();
        photo2.setId("2");
        photo2.save();

        storedPhotos.add(photo1);
        storedPhotos.add(photo2);

//        List<MyPhoto> photosFromDB = new Select().from(MyPhoto.class).queryList();

        repository.getPhotos();
        verify(eventBus).post(argumentCaptor.capture());
        SavedPhotosEvent event = argumentCaptor.getValue();
        assertEquals(SavedPhotosEvent.READ_EVENT,event.getType());
        assertNotNull(event.getphotosList());
        assertEquals("1",event.getphotosList().get(0).getId());
        assertEquals("2",event.getphotosList().get(1).getId());
        assertEquals(-1,event.getErrorId());

        for (MyPhoto photo : storedPhotos) {
            photo.delete();
        }

    }

    @Test
    public void testDeletePhoto_deleteSuccess_EventPosted() throws Exception {

        MyPhoto photo1 = new MyPhoto();
        photo1.setId("1");
        photo1.save();

        repository.deletePhoto(photo1);

        assertFalse(photo1.exists());
        verify(eventBus).post(argumentCaptor.capture());
        SavedPhotosEvent event = argumentCaptor.getValue();
        assertEquals(SavedPhotosEvent.SUCCESS_DELETE_EVENT,event.getType());
        assertNotNull(event.getphotosList());
        assertEquals("1",event.getphotosList().get(0).getId());
        assertEquals(-1,event.getErrorId());

        photo1.delete();

    }

    @Test
    public void testDeletePhoto_deleteFailure_EventPosted() throws Exception {
        MyPhoto photo1 = mock(MyPhoto.class);
        when(photo1.delete()).thenReturn(false);

        repository.deletePhoto(photo1);

        verify(eventBus).post(argumentCaptor.capture());
        SavedPhotosEvent event = argumentCaptor.getValue();
        assertEquals(SavedPhotosEvent.FAILURE_DELETE_EVENT,event.getType());
        assertNotNull(event.getphotosList());
        assertEquals(-1,event.getErrorId());
    }

}
