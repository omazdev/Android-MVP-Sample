package omazdev.samples.MVPsample.showPhotos.ui;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowToast;

import omazdev.samples.MVPsample.BaseTest;
import omazdev.samples.MVPsample.R;
import omazdev.samples.MVPsample.ShowPhotos.ShowPhotosPresenter;
import omazdev.samples.MVPsample.ShowPhotos.ui.ShowPhotosActivity;
import omazdev.samples.MVPsample.ShowPhotos.ui.ShowPhotosView;
import omazdev.samples.MVPsample.api.response.Photo;
import omazdev.samples.MVPsample.libs.base.ImageLoader;
import omazdev.samples.MVPsample.support.ShadowImageView;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static omazdev.samples.MVPsample.ShowPhotos.ui.ShowPhotosView.TAGS;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 27,shadows = {ShadowImageView.class})
public class ShowPhotosActivityTest extends BaseTest{

    @Mock private ShowPhotosPresenter presenter;
    @Mock private ImageLoader imageLoader;
    @Mock private Photo photo;

    private ShowPhotosView view;
    private ShowPhotosActivity activity;
    private ShadowActivity shadowActivity;
    private ActivityController<ShowPhotosActivity> controller;


    @Override
    public void setUp() throws Exception {
        super.setUp();
        ShowPhotosActivity showPhotosActivity = new ShowPhotosActivity(){

            public void setupInjection() {
                this.setPresenter(presenter);
                this.setImageLoader(imageLoader);
            }

        };

        Intent intent = new Intent().putExtra(TAGS,"someTag");
        controller = ActivityController.of(showPhotosActivity,intent).create().visible();
        activity = controller.get();
        view = activity;
        shadowActivity = shadowOf(activity);
    }

    @Test
    public void testOnActivityCreated_checkIntent() throws Exception {
        verify(presenter).onCreate();
        verify(presenter).checkIntent("someTag",null,null,-1);

        assertNotNull(activity.getSupportActionBar());
        assertEquals("SHOW PHOTOS",activity.getSupportActionBar().getTitle());
    }

    @Test
    public void testOnActivityDestroyed_presenterDestroyed() throws Exception {
        controller.destroy();

        assertTrue(activity.isDestroyed());

        verify(presenter).onDestroy();
    }

    @Test
    public void testKeepButtonClicked_shouldKeepPhoto() throws Exception {
        ImageButton saveButton = activity.findViewById(R.id.saveButton);
        saveButton.performClick();

        verify(presenter).onKeepPhoto();
    }

    @Test
    public void testDismissButtonClicked_shouldDismissPhoto() throws Exception {
        ImageButton saveButton = activity.findViewById(R.id.dismissButton);
        saveButton.performClick();

        verify(presenter).onDismissPhoto();
    }

    @Test
    public void testSwipeRight_shouldKeepPhoto() throws Exception {
        ImageView mainImg = activity.findViewById(R.id.mainImg);
        ShadowImageView shadowImage = Shadow.extract(mainImg);
        shadowImage.performSwipe(200,200,500,250,50);
        verify(presenter).onKeepPhoto();
    }

    @Test
    public void testSwipeLeft_shouldCallOnDismiss() throws Exception {
        ImageView mainImg = activity.findViewById(R.id.mainImg);
        ShadowImageView shadowImage = Shadow.extract(mainImg);
        shadowImage.performSwipe(200,200,-500,250,50);
        verify(presenter).onDismissPhoto();
    }

    @Test
    public void testSwipeDown_shouldCallOnKeep() throws Exception {
        ImageView mainImg = activity.findViewById(R.id.mainImg);
        ShadowImageView shadowImage = Shadow.extract(mainImg);
        shadowImage.performSwipe(200,200,250,500,50);
        verify(presenter).onKeepPhoto();
    }

    @Test
    public void testSwipeUp_shouldCallOnDismiss() throws Exception {
        ImageView mainImg = activity.findViewById(R.id.mainImg);
        ShadowImageView shadowImage = Shadow.extract(mainImg);
        shadowImage.performSwipe(200,200,250,-500,50);
        verify(presenter).onDismissPhoto();
    }

    @Test
    public void testLoadPhoto_singleArgument_loadPhoto() throws Exception {
        when(photo.getFlickrUrl()).thenReturn("someURL");
        when(photo.getTitle()).thenReturn("someTitle");

        ImageView mainImg = activity.findViewById(R.id.mainImg);
        TextView titleTv = activity.findViewById(R.id.titleTv);

        view.loadPhoto(photo);
        verify(imageLoader).load(mainImg,"someURL");
        assertEquals("someTitle",titleTv.getText().toString());
    }

    @Test
    public void testLoadPhoto_singleArgument_loadNextPhoto() throws Exception {
        when(photo.getFlickrUrl()).thenReturn("");

        view.loadPhoto(photo);
        verify(presenter).loadNextPhoto();
    }

    @Test
    public void testLoadPhoto_doubleArgument_loadPhoto() throws Exception {
        String photoUrl = "someURL";
        String photoTitle = "someTitle";

        ImageView mainImg = activity.findViewById(R.id.mainImg);
        TextView titleTv = activity.findViewById(R.id.titleTv);

        view.loadPhoto(photoUrl,photoTitle);
        verify(imageLoader).load(mainImg,"someURL");
        assertEquals("someTitle",titleTv.getText().toString());
        }

    @Test
    public void testShowError_shouldShowToast() throws Exception {
        int successMessage = R.string.search_api_endpoint_error;
        String errorSupp = "error supp";

        view.showError(successMessage,errorSupp);
        assertEquals(RuntimeEnvironment.application.getString(R.string.search_api_endpoint_error)
                        +" : "+errorSupp
                , ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void testShowSuccess_shouldShowToast() throws Exception {
        int successMessage = R.string.save_photo_success;
        view.showSuccess(successMessage);
        assertEquals(RuntimeEnvironment.application.getString(R.string.save_photo_success), ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void testFinishShowActivity_shouldFinishActivity() throws Exception {
        Handler mockHandler = mock(Handler.class);
        when(mockHandler.postDelayed(any(Runnable.class), anyLong())).thenAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                invocation.getArgumentAt(0, Runnable.class).run();
                return null;
            }

        });

        activity.setHandler(mockHandler);
        view.finishShowActivity();
        assertTrue(activity.isFinishing());
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
    public void testShowUIElements_UiElementsShouldBeVisible() throws Exception {
        TextView titleTv = activity.findViewById(R.id.titleTv);
        ImageView mainImg = activity.findViewById(R.id.mainImg);
        ImageButton saveButton= activity.findViewById(R.id.saveButton);
        ImageButton dismissButton= activity.findViewById(R.id.dismissButton);

        view.showUIElements();

        assertEquals(View.VISIBLE,titleTv.getVisibility());
        assertEquals(View.VISIBLE,mainImg.getVisibility());
        assertEquals(View.VISIBLE,saveButton.getVisibility());
        assertEquals(View.VISIBLE,dismissButton.getVisibility());
    }

    @Test
    public void testHideUIElements_UiElementsShouldBeGone() throws Exception {
        TextView titleTv = activity.findViewById(R.id.titleTv);
        ImageView mainImg = activity.findViewById(R.id.mainImg);
        ImageButton saveButton= activity.findViewById(R.id.saveButton);
        ImageButton dismissButton= activity.findViewById(R.id.dismissButton);

        view.hideUIElements();

        assertEquals(View.GONE,titleTv.getVisibility());
        assertEquals(View.GONE,mainImg.getVisibility());
        assertEquals(View.GONE,saveButton.getVisibility());
        assertEquals(View.GONE,dismissButton.getVisibility());
    }

    @Test
    public void testMakeUIElementsInvisible_UiElementsShouldBeInvisible() throws Exception {
        TextView titleTv = activity.findViewById(R.id.titleTv);
        ImageView mainImg = activity.findViewById(R.id.mainImg);
        ImageButton saveButton= activity.findViewById(R.id.saveButton);
        ImageButton dismissButton= activity.findViewById(R.id.dismissButton);

        view.makeUIElementsInvisible();

        assertEquals(View.INVISIBLE,titleTv.getVisibility());
        assertEquals(View.INVISIBLE,mainImg.getVisibility());
        assertEquals(View.INVISIBLE,saveButton.getVisibility());
        assertEquals(View.INVISIBLE,dismissButton.getVisibility());
    }

    @Test
    public void testSaveAnimation_animationShouldBeStarted() throws Exception {
        ImageView mainImg = activity.findViewById(R.id.mainImg);

        view.saveAnimation();

        assertNotNull(mainImg.getAnimation());
        assertTrue(mainImg.getAnimation().hasStarted());
    }

    @Test
    public void testDismissAnimation_animationShouldBeStarted() throws Exception {
        ImageView mainImg = activity.findViewById(R.id.mainImg);

        view.dismissAnimation();

        assertNotNull(mainImg.getAnimation());
        assertTrue(mainImg.getAnimation().hasStarted());
    }

    @Test
    public void changeButtonsSourceImg() throws Exception {
        view.changeButtonsSourceImg();

        ImageButton saveButton = activity.findViewById(R.id.saveButton);
        ImageButton dismissButton = activity.findViewById(R.id.dismissButton);

        assertEquals(R.drawable.ic_skip_next_black_48dp,saveButton.getTag());
        assertEquals(R.drawable.ic_skip_previous_black_48dp,dismissButton.getTag());
    }

    @Test
    public void testHomeMenuClicked_shouldCallOnBackPressed() throws Exception {
        shadowActivity.clickMenuItem(android.R.id.home);
        assertTrue(activity.isFinishing());
    }

}
