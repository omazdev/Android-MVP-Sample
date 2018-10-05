package omazdev.samples.MVPsample.SavedPhotos.ui;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowToast;

import java.util.ArrayList;
import java.util.List;

import omazdev.samples.MVPsample.BaseTest;
import omazdev.samples.MVPsample.R;
import omazdev.samples.MVPsample.SavedPhotos.SavedPhotosPresenter;
import omazdev.samples.MVPsample.SavedPhotos.ui.adapters.OnItemClickListener;
import omazdev.samples.MVPsample.SavedPhotos.ui.adapters.SavedPhotosAdapter;
import omazdev.samples.MVPsample.ShowPhotos.ui.ShowPhotosView;
import omazdev.samples.MVPsample.entities.MyPhoto;
import omazdev.samples.MVPsample.libs.base.ImageLoader;
import omazdev.samples.MVPsample.support.ShadowRecyclerViewAdapter;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 27,shadows = {ShadowRecyclerViewAdapter.class})
public class SavedPhotosActivityTest extends BaseTest {

    @Mock private List<MyPhoto> photoList;
    @Mock private ImageLoader imageLoader;
    @Mock private MyPhoto photo;
    @Mock private SavedPhotosPresenter presenter;
    @Mock private SavedPhotosAdapter adapter;

    private SavedPhotosView view;
    private SavedPhotosActivity activity;
    private ShadowActivity shadowActivity;
    private ActivityController<SavedPhotosActivity> controller;
    private OnItemClickListener onItemClickListener;
    private ShadowRecyclerViewAdapter shadowRecyclerViewAdapter;


    @Override
    public void setUp() throws Exception {
        super.setUp();
        final SavedPhotosPresenter mPresenter = this.presenter;
        final SavedPhotosAdapter mAdapter = this.adapter;

        SavedPhotosActivity savedPhotosActivity = new SavedPhotosActivity(){

            public void setupInjection() {
                this.setPresenter(mPresenter);
                this.setAdapter(mAdapter);
            }
        };

        controller = ActivityController.of(savedPhotosActivity).create().visible();
        activity = controller.get();
        view = activity;
        onItemClickListener = activity;
        shadowActivity = shadowOf(activity);
    }

    @Test
    public void testOnActivityCreated_loadActionBar_CallPresenterOnCreateGetPhotos() throws Exception {
        verify(presenter).onCreate();
        verify(presenter).getPhotos();

        assertNotNull(activity.getSupportActionBar());
        assertEquals("FAVORITE PHOTOS",activity.getSupportActionBar().getTitle());

        RecyclerView recyclerView = activity.findViewById(R.id.recyclerView);

        assertNotNull(recyclerView.getAdapter());
        assertNotNull(recyclerView.getLayoutManager());
    }

    @Test
    public void testOnActivityDestroyed_presenterDestroyed() throws Exception {
        controller.destroy();

        assertTrue(activity.isDestroyed());

        verify(presenter).onDestroy();
    }

    private void setUpShadowAdapter(int positionToClick){
        String URL = "someURL";

        when(photoList.get(positionToClick)).thenReturn(photo);
        when(photoList.get(positionToClick).getFlickrUrl()).thenReturn(URL);

        SavedPhotosAdapter savedPhotosAdapter = new SavedPhotosAdapter(photoList,imageLoader,onItemClickListener);
        RecyclerView recyclerView = activity.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(savedPhotosAdapter);
        shadowRecyclerViewAdapter = Shadow.extract(recyclerView.getAdapter());
    }

    @Test
    public void testRecyclerViewOnItemClick_shouldCallPresenterShowClickedPhoto() throws Exception {
        int positionToClick = 2;
        setUpShadowAdapter(positionToClick);

        shadowRecyclerViewAdapter.itemVisible(positionToClick);
        shadowRecyclerViewAdapter.performClick(positionToClick);

        verify(presenter).showClickedPhoto(photo,photoList);
    }

    @Test
    public void testRecyclerViewOnDeleteClick_shouldCallPresenterDeletePhoto() throws Exception {
        int positionToClick = 2;
        setUpShadowAdapter(positionToClick);

        shadowRecyclerViewAdapter.itemVisible(positionToClick);
        shadowRecyclerViewAdapter.performClickOverViewHolder(positionToClick, R.id.deleteBtn);

        verify(presenter).deletePhoto(photo);
    }

    @Test
    public void testRecyclerViewOnShareClick_shouldCallPresenterDeletePhoto() throws Exception {
        int positionToClick = 2;
        setUpShadowAdapter(positionToClick);

        shadowRecyclerViewAdapter.itemVisible(positionToClick);
        shadowRecyclerViewAdapter.performClickOverViewHolder(positionToClick, R.id.shareBtn);

        Intent startedIntent = shadowActivity.getNextStartedActivity();

        assertEquals(Intent.ACTION_SEND,startedIntent.getAction());
        assertEquals(RuntimeEnvironment.application.getString(R.string.share_msg)
                        +" "+"someURL"
                      ,startedIntent.getStringExtra(Intent.EXTRA_TEXT));
    }

    @Test
    public void testSetPhotos_shouldCallAdaptersSetPhotos() throws Exception {
        view.setPhotos(photoList);
        verify(adapter).setPhotos(photoList);
    }

    @Test
    public void testOnPhotoDeleted_CallAdapterRomevePhoto_CallpresenterOnPhotoRemoved() throws Exception {
        when(adapter.removePhoto(photo)).thenReturn(true);
        when(photoList.contains(photo)).thenReturn(true);
        when(photoList.indexOf(photo)).thenReturn(0);

        view.onPhotoDeleted(photo);
        verify(adapter).removePhoto(photo);
        verify(presenter).onPhotoRemoved(true);
    }

    @Test
    public void testOpenShowPhotosActivity_shouldStartShowPhotosActivity() throws Exception {
        ArrayList<String> photosUrlList = mock(ArrayList.class);
        ArrayList<String> photosTiltleList = mock(ArrayList.class);
        when(photosUrlList.size()).thenReturn(3);
        when(photosTiltleList.size()).thenReturn(4);
        int photoIndex = 0;

        view.openShowPhotosActivity(photosUrlList,photosTiltleList,photoIndex);

        Intent startedIntent = shadowActivity.getNextStartedActivity();

        assertEquals(3,startedIntent.getStringArrayListExtra(ShowPhotosView.PHOTOS_URL_LIST).size());
        assertEquals(4,startedIntent.getStringArrayListExtra(ShowPhotosView.PHOTOS_TITLE_LIST).size());
        assertEquals(0,startedIntent.getIntExtra(ShowPhotosView.PHOTO_INDEX,-1));
    }

    @Test
    public void testShowError_shouldShowToast() throws Exception {
        int errorId = R.string.search_tags_error;
        view.showError(errorId);
        assertEquals(RuntimeEnvironment.application.getString(R.string.search_tags_error)
                   , ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void testShowSuccess_shouldShowToast() throws Exception {
        int errorId = R.string.delete_photo_success;
        view.showError(errorId);
        assertEquals(RuntimeEnvironment.application.getString(R.string.delete_photo_success)
                , ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void testShowProgress_progressBarShouldBeVisible() throws Exception {
        view.showProgress();

        ProgressBar progressBar = activity.findViewById(R.id.progressBar);
        assertEquals(View.VISIBLE,progressBar.getVisibility());
    }

    @Test
    public void testHideProgress_progressBarShouldBeGone() throws Exception {
        view.hideProgress();

        ProgressBar progressBar = activity.findViewById(R.id.progressBar);
        assertEquals(View.GONE,progressBar.getVisibility());
    }

    @Test
    public void testShowNoElementsTv_noElementsTvShouldBeVisible() throws Exception {
        view.showNoElementsTv();

        TextView noElementsTv = activity.findViewById(R.id.noElementsTv);
        assertEquals(View.VISIBLE,noElementsTv.getVisibility());
    }

    @Test
    public void testShowNoElementsTv_noElementsTvShouldBeGone() throws Exception {
        view.hideNoElementsTv();

        TextView noElementsTv = activity.findViewById(R.id.noElementsTv);
        assertEquals(View.GONE,noElementsTv.getVisibility());
    }

    @Test
    public void testshowUIElements_showUIElementsShouldBeVisible() throws Exception {
        view.showUIElements();

        RecyclerView recyclerView = activity.findViewById(R.id.recyclerView);
        assertEquals(View.VISIBLE,recyclerView.getVisibility());
    }

    @Test
    public void testshowUIElements_showUIElementsShouldBeGone() throws Exception {
        view.hideUIElements();

        RecyclerView recyclerView = activity.findViewById(R.id.recyclerView);
        assertEquals(View.GONE,recyclerView.getVisibility());
    }

    @Test
    public void testHomeMenuClicked_shouldCallOnBackPressed() throws Exception {
        shadowActivity.clickMenuItem(android.R.id.home);
        assertTrue(activity.isFinishing());
    }

}
