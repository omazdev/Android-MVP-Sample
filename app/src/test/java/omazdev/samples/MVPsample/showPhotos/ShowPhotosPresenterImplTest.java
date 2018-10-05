package omazdev.samples.MVPsample.showPhotos;

import android.os.Handler;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import omazdev.samples.MVPsample.BaseTest;
import omazdev.samples.MVPsample.R;
import omazdev.samples.MVPsample.ShowPhotos.SavePhotoInteractor;
import omazdev.samples.MVPsample.ShowPhotos.SearchPhotosInteractor;
import omazdev.samples.MVPsample.ShowPhotos.ShowPhotosPresenter;
import omazdev.samples.MVPsample.ShowPhotos.ShowPhotosPresenterImpl;
import omazdev.samples.MVPsample.ShowPhotos.events.ShowPhotosEvent;
import omazdev.samples.MVPsample.ShowPhotos.ui.ShowPhotosView;
import omazdev.samples.MVPsample.api.response.Photo;
import omazdev.samples.MVPsample.libs.base.EventBus;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ShowPhotosPresenterImplTest extends BaseTest {

    @Mock private EventBus eventBus;
    @Mock private ShowPhotosView view;
    @Mock private SearchPhotosInteractor searchPhotosInteractor;
    @Mock private SavePhotoInteractor savePhotoInteractor;
    @Mock private List<Photo> myPhotosList;
    @Mock private Photo photo;
    @Mock private ShowPhotosEvent event;

    private ShowPhotosPresenterImpl presenter;
    private boolean fromOtherTest;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        presenter = new ShowPhotosPresenterImpl(eventBus,view,searchPhotosInteractor,savePhotoInteractor);
        fromOtherTest = false;
    }

    @Test
    public void testOncreate_subscribedToEventBus() throws Exception  {
        presenter.onCreate();
        verify(eventBus,times(1)).register(presenter);
        assertEquals(0,presenter.getPhotosIndex());
    }

    @Test
    public void testOnDestroy_unSubscribedToEventBus() throws Exception  {
        presenter.onDestroy();
        verify(eventBus,times(1)).unregister(presenter);
        assertNull(presenter.getView());
    }

    @Test
    public void testCheckIntent_searchPhotos() throws Exception  {
        String tags = "sky";
        presenter.checkIntent(tags,null,null,-1);
        assertNotNull(presenter.getView());
        assertEquals(ShowPhotosPresenter.SEARCH_MODE,presenter.getShowMode());

        verify(view,times(1)).showProgress();
        verify(view,times(1)).hideUIElements();
        verify(searchPhotosInteractor,times(1)).execute(tags);

    }

    @Test
    public void testCheckIntent_loadSavedPhotos() throws Exception  {

        ArrayList<String> photosTiltleList = new ArrayList<>();
        ArrayList<String> photosUrlList= new ArrayList<>();
        /*_______________Checking with real Data(not the mock)_____________*/
        photosTiltleList.add("AIDAaura ( Best Viewed Large)");
        photosTiltleList.add("at the close of the dayhttps:");
        photosTiltleList.add("a8d7bf5e-d0e9-4d37-969e-930ee379851f");
        photosTiltleList.add("Road Closed");
        photosTiltleList.add("Moving Skies.");
        photosTiltleList.add("Others Take Over");
        photosTiltleList.add("Photo");
        photosTiltleList.add("Botanical Garden");
        photosTiltleList.add("Fishing Pier at Sunset");
        photosTiltleList.add("A glimpse of the sun after daybreak");
        photosTiltleList.add("A glimpse of the sun after daybreak");
        photosTiltleList.add("A glimpse of the sun after daybreak");
        photosTiltleList.add("Driver Goodman");
        photosTiltleList.add("Streaking along");
        photosTiltleList.add("Sunshine in Sunshine");

        photosUrlList.add("https://farm2.staticflickr.com/1888/29511814247_a98f52973f_b.jpg");
        photosUrlList.add("https://farm2.staticflickr.com/1888/29511814247_a98f52973f_b.jpg");
        photosUrlList.add("https://farm2.staticflickr.com/1847/43538686765_15d9a99c57_b.jpg");
        photosUrlList.add("https://farm2.staticflickr.com/1843/43538208315_44e11c5d6c_b.jpg");
        photosUrlList.add("https://farm2.staticflickr.com/1853/43537355065_af67675a27_b.jpg");
        photosUrlList.add("https://farm2.staticflickr.com/1884/42635901760_6d50e5c342_b.jpg");
        photosUrlList.add("https://farm2.staticflickr.com/1860/30576019428_b9e1cd60fc_b.jpg");
        photosUrlList.add("https://farm2.staticflickr.com/1878/44444085201_4cfbdb018d_b.jpg");
        photosUrlList.add("https://farm2.staticflickr.com/1894/43533108945_60aabe35e6_b.jpg");
        photosUrlList.add("https://farm2.staticflickr.com/1858/43723418134_49814ae2de_b.jpg");
        photosUrlList.add("https://farm2.staticflickr.com/1849/43532040935_d7e488ac53_b.jpg");
        photosUrlList.add("https://farm2.staticflickr.com/1856/44441056011_ec1cdea71e_b.jpg");
        photosUrlList.add("https://farm2.staticflickr.com/1858/43531917865_580bf189da_b.jpg");
        photosUrlList.add("https://farm2.staticflickr.com/1869/43531898715_4e1bd1101c_b.jpg");
        photosUrlList.add("https://farm2.staticflickr.com/1889/43531856825_9d89d499b2_b.jpg");

        presenter.checkIntent(null,photosUrlList,photosTiltleList,2);

        assertNotNull(presenter.getView());
        assertEquals(ShowPhotosPresenter.SAVE_MODE , presenter.getShowMode());
        assertEquals(2 , presenter.getPhotosIndex());
        assertEquals(photosUrlList , presenter.getPhotosUrlList());
        assertEquals(photosTiltleList , presenter.getPhotosTiltleList());

        verify(view,times(1)).changeButtonsSourceImg();
        verify(view,times(1)).showProgress();
        verify(view,times(1)).makeUIElementsInvisible();
        verify(view,times(1)).loadPhoto(photosUrlList.get(2),photosTiltleList.get(2));

    }

    private void showNextSavedPhoto_verifyPhotoLoaded(Boolean isFromOtherTest,Boolean isFromPhotoFailed) {
        int listSize = 15;
        List<String> photosUrlList = mock(List.class);
        List<String> photosTiltleList = mock(List.class);

        doReturn(listSize).when(photosUrlList).size();
        doReturn("random string").when(photosUrlList).get(1);
        doReturn("another random string").when(photosTiltleList).get(1);

        Handler mockHandler = mock(Handler.class);
        when(mockHandler.postDelayed(any(Runnable.class), anyLong())).thenAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                invocation.getArgumentAt(0, Runnable.class).run();
                return null;
            }

        });

        presenter.setPhotosUrlList(photosUrlList);
        presenter.setPhotosTiltleList(photosTiltleList);
        presenter.setHandler(mockHandler);

        if (isFromPhotoFailed){
            presenter.onPhotoFailed();
        }else if (isFromOtherTest){
            presenter.onKeepPhoto();
        }else {
            presenter.showNextSavedPhoto();
        }

        assertEquals(listSize,photosUrlList.size());
        assertEquals(1,presenter.getPhotosIndex());
        assertNotNull(presenter.getView());
        assertTrue(presenter.getPhotosIndex()< listSize);

        verify(view,times(1)).saveAnimation();
        verify(view,times(1)).showProgress();
        verify(view,times(1)).hideUIElements();
        verify(view,times(1)).loadPhoto(photosUrlList.get(1),photosTiltleList.get(1));

    }

    @Test
    public void testShowNextSavedPhoto_verifyPhotoLoaded() throws Exception  {
        presenter.setPhotosIndex(0);
        showNextSavedPhoto_verifyPhotoLoaded(false,false);
    }

    @Test
    public void testShowNextSavedPhoto_shouldShowError() throws Exception  {
        int listSize = 15;
        List<String> photosUrlList = mock(List.class);
        doReturn(listSize).when(photosUrlList).size();

        presenter.setPhotosIndex(17);
        presenter.setPhotosUrlList(photosUrlList);
        if (!fromOtherTest){
            presenter.showNextSavedPhoto();
        }

        assertNotNull(presenter.getView());
        assertTrue(presenter.getPhotosIndex() > listSize);
        verify(view,times(1)).showError(R.string.search_photos_max_limit,null);
        verify(view,times(1)).finishShowActivity();

    }

    @Test
    public void testShowPreviousSavedPhoto_verifyPhotoLoaded() throws Exception  {
        showPreviousSavedPhoto_success(false);
    }

    @Test
    public void testShowPreviousSavedPhoto_shouldShowError() throws Exception  {
        showPreviousSavedPhoto_shouldShowError(false);

    }

    @Test
    public void testOnKeepPhoto_shouldSavePhoto() throws Exception  {
        when(myPhotosList.get(0)).thenReturn(photo);

        presenter.setShowMode(ShowPhotosPresenter.SEARCH_MODE);
        presenter.setMyPhotosList(myPhotosList);
        presenter.onKeepPhoto();

        assertNotNull(presenter.getView());
        verify(view,times(1)).saveAnimation();
        verify(savePhotoInteractor,times(1)).execute(presenter.getPhotoToSave());
    }

    @Test
    public void testOnKeepPhoto_shouldShowNextSavedPhoto_withSuccess() throws Exception  {
        presenter.setPhotosIndex(0);
        presenter.setShowMode(ShowPhotosPresenter.SAVE_MODE);
        showNextSavedPhoto_verifyPhotoLoaded(true,false);
    }

    @Test
    public void testOnKeepPhoto_shouldShowNextSavedPhoto_withError() throws Exception  {
        presenter.setShowMode(ShowPhotosPresenter.SAVE_MODE);
        presenter.onKeepPhoto();

        fromOtherTest = true;
        testShowNextSavedPhoto_shouldShowError();
    }

    @Test
    public void testOnDismissPhoto_shouldDismissPhoto_verifyNextPhotoLoaded() throws Exception  {
        int listSize = 15;
        doReturn(listSize).when(myPhotosList).size();
        Handler mockHandler = mock(Handler.class);
        when(mockHandler.postDelayed(any(Runnable.class), anyLong())).thenAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                invocation.getArgumentAt(0, Runnable.class).run();
                return null;
            }

        });

        presenter.setMyPhotosList(myPhotosList);
        presenter.setPhotosIndex(0);
        presenter.setHandler(mockHandler);
        presenter.setShowMode(ShowPhotosPresenter.SEARCH_MODE);
        fromOtherTest = true;
        presenter.onDismissPhoto();

        assertNotNull(presenter.getView());
        verify(view,times(1)).dismissAnimation();
        testLoadNextPhoto_verifyPhotoLoaded();
    }

    @Test
    public void testOnDismissPhoto_showPreviousSavedPhoto_success() throws Exception  {
        presenter.setShowMode(ShowPhotosPresenter.SAVE_MODE);
        showPreviousSavedPhoto_success(true);
    }

    @Test
    public void testOnDismissPhoto_showPreviousSavedPhoto_error() throws Exception  {
        presenter.setShowMode(ShowPhotosPresenter.SAVE_MODE);
        showPreviousSavedPhoto_shouldShowError(true);
    }

    private void showPreviousSavedPhoto_success(Boolean isFromDismissPhoto) {
        int listSize = 15;
        List<String> photosUrlList = mock(List.class);
        List<String> photosTiltleList = mock(List.class);

        doReturn(listSize).when(photosUrlList).size();
        doReturn("random string").when(photosUrlList).get(1);
        doReturn("another random string").when(photosTiltleList).get(1);

        Handler mockHandler = mock(Handler.class);
        when(mockHandler.postDelayed(any(Runnable.class), anyLong())).thenAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                invocation.getArgumentAt(0, Runnable.class).run();
                return null;
            }

        });

        presenter.setPhotosIndex(5);
        presenter.setPhotosUrlList(photosUrlList);
        presenter.setPhotosTiltleList(photosTiltleList);
        presenter.setHandler(mockHandler);

        if (isFromDismissPhoto){
            presenter.onDismissPhoto();
        }else {
            presenter.showPreviousSavedPhoto();
        }

        assertEquals(listSize,photosUrlList.size());
        assertEquals(4,presenter.getPhotosIndex());
        assertNotNull(presenter.getView());
        assertTrue(presenter.getPhotosIndex() >= 0);
        assertTrue(presenter.getPhotosIndex()< listSize);

        verify(view,times(1)).dismissAnimation();
        verify(view,times(1)).showProgress();
        verify(view,times(1)).hideUIElements();
        verify(view,times(1)).loadPhoto(photosUrlList.get(4),photosTiltleList.get(4));
    }

    private void showPreviousSavedPhoto_shouldShowError(Boolean isFromDismissPhoto) {
        int listSize = 15;
        List<String> photosUrlList = mock(List.class);
        doReturn(listSize).when(photosUrlList).size();

        presenter.setPhotosIndex(0);
        presenter.setPhotosUrlList(photosUrlList);
        if (isFromDismissPhoto){
            presenter.onDismissPhoto();
        }else {
            presenter.showPreviousSavedPhoto();
        }

        assertNotNull(presenter.getView());
        assertTrue(presenter.getPhotosIndex() < 0);
        verify(view,times(1)).showError(R.string.search_photos_max_limit,null);
        verify(view,times(1)).finishShowActivity();

    }

    @Test
    public void testLoadNextPhoto_verifyPhotoLoaded() throws Exception  {
        int listSize = 15;

        doReturn(listSize).when(myPhotosList).size();

//        presenter.setPhotosIndex(0);
        presenter.setMyPhotosList(myPhotosList);
        if (!fromOtherTest) {
            presenter.loadNextPhoto();
        }
        assertEquals(1,presenter.getPhotosIndex());
        assertNotNull(presenter.getView());
        assertTrue(presenter.getPhotosIndex()< listSize);

        verify(view,times(1)).showProgress();
        verify(view,times(1)).hideUIElements();
        verify(view,times(1)).loadPhoto(myPhotosList.get(1));

    }

    @Test
    public void testLoadNextPhoto_shouldShowError() throws Exception  {
        int listSize = 15;
        doReturn(listSize).when(myPhotosList).size();

        presenter.setPhotosIndex(17);
        presenter.setMyPhotosList(myPhotosList);
        presenter.loadNextPhoto();

        assertNotNull(presenter.getView());
        assertTrue(presenter.getPhotosIndex() > listSize);
        verify(view,times(1)).showError(R.string.search_photos_max_limit,null);
        verify(view,times(1)).finishShowActivity();

    }

    @Test
    public void testOnEventMainThread_hasError() throws Exception  {
        int errorId = 123456;
        String errorSupp = "supp";

        when(event.getErrorId()).thenReturn(errorId);
        when(event.getErrorSupplement()).thenReturn(errorSupp);

        presenter.onEventMainThread(event);

        assertNotNull(presenter.getView());
        verify(view,times(1)).hideProgress();
        verify(view,times(1)).showUIElements();
        verify(view,times(1)).showError(errorId,errorSupp);
        verify(view,times(1)).finishShowActivity();

    }

    @Test
    public void testOnEventMainThread_searchEvent() throws Exception  {
        int errorId = -1;
        String errorSupp = "supp";

        when(event.getErrorId()).thenReturn(errorId);
        when(event.getErrorSupplement()).thenReturn(errorSupp);
        when(event.getType()).thenReturn(ShowPhotosEvent.SEARCH_EVENT);
        when(event.getphotosList()).thenReturn(myPhotosList);
        when(myPhotosList.get(0)).thenReturn(photo);

        presenter.setMyPhotosList(myPhotosList);
        presenter.onEventMainThread(event);

        assertNotNull(presenter.getView());
        verify(view,times(1)).hideProgress();
        verify(view,times(1)).showUIElements();
        verify(view,times(1)).loadPhoto(photo);

    }

    @Test
    public void testOnEventMainThread_savePhotoFailure() throws Exception  {
        int listSize = 15;
        int errorId = -1;
        String errorSupp = "supp";

        when(event.getErrorId()).thenReturn(errorId);
        when(event.getErrorSupplement()).thenReturn(errorSupp);
        when(event.getType()).thenReturn(ShowPhotosEvent.SAVE_FAILURE_EVENT);
        when(event.getphotosList()).thenReturn(myPhotosList);
        doReturn(listSize).when(myPhotosList).size();
        when(myPhotosList.get(0)).thenReturn(photo);
        Handler mockHandler = mock(Handler.class);
        when(mockHandler.postDelayed(any(Runnable.class), anyLong())).thenAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                invocation.getArgumentAt(0, Runnable.class).run();
                return null;
            }

        });

        fromOtherTest = true;
        presenter.setPhotosIndex(0);
        presenter.setHandler(mockHandler);
        presenter.setMyPhotosList(myPhotosList);
        presenter.onEventMainThread(event);

        assertNotNull(presenter.getView());
        verify(view,times(1)).showError(R.string.save_photo_failure,null);
        testLoadNextPhoto_verifyPhotoLoaded();

    }

    @Test
    public void testOnEventMainThread_savePhotoSuccess() throws Exception  {
        int listSize = 15;
        int errorId = -1;
        String errorSupp = "supp";

        when(event.getErrorId()).thenReturn(errorId);
        when(event.getErrorSupplement()).thenReturn(errorSupp);
        when(event.getType()).thenReturn(ShowPhotosEvent.SAVE_SUCCESS_EVENT);
        when(event.getphotosList()).thenReturn(myPhotosList);
        doReturn(listSize).when(myPhotosList).size();
        when(myPhotosList.get(0)).thenReturn(photo);
        Handler mockHandler = mock(Handler.class);
        when(mockHandler.postDelayed(any(Runnable.class), anyLong())).thenAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                invocation.getArgumentAt(0, Runnable.class).run();
                return null;
            }

        });

        fromOtherTest = true;
        presenter.setPhotosIndex(0);
        presenter.setHandler(mockHandler);
        presenter.setMyPhotosList(myPhotosList);
        presenter.onEventMainThread(event);

        assertNotNull(presenter.getView());
        verify(view,times(1)).showSuccess(R.string.save_photo_success);
        testLoadNextPhoto_verifyPhotoLoaded();

    }

    @Test
    public void testOnPhotoReady() throws Exception  {
        presenter.onPhotoReady();
        assertNotNull(presenter.getView());
        verify(view,times(1)).hideProgress();
        verify(view,times(1)).showUIElements();
    }

    @Test
    public void testOnPhotoFailed_shouldLoadNextPhoto() throws Exception  {
        int listSize = 15;
        doReturn(listSize).when(myPhotosList).size();
        when(myPhotosList.get(0)).thenReturn(photo);

        fromOtherTest = true;
        presenter.setPhotosIndex(0);
        presenter.setShowMode(ShowPhotosPresenter.SEARCH_MODE);
        presenter.setMyPhotosList(myPhotosList);
        presenter.onPhotoFailed();
        testLoadNextPhoto_verifyPhotoLoaded();
    }

    @Test
    public void testOnPhotoFailed_shouldShowNextSavedPhoto() throws Exception  {
        presenter.setPhotosIndex(0);
        presenter.setShowMode(ShowPhotosPresenter.SAVE_MODE);
        showNextSavedPhoto_verifyPhotoLoaded(false,true);
    }

}
