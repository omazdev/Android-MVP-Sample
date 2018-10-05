package omazdev.samples.MVPsample.MainActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.FacebookSdk;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowToast;

import omazdev.samples.MVPsample.BaseTest;
import omazdev.samples.MVPsample.R;
import omazdev.samples.MVPsample.SavedPhotos.ui.SavedPhotosActivity;
import omazdev.samples.MVPsample.ShowPhotos.ui.ShowPhotosActivity;
import omazdev.samples.MVPsample.login.ui.LoginActivity;

import static junit.framework.Assert.assertEquals;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 27)
public class MainActivityTest extends BaseTest {

    private MainActivity activity;
    private ShadowActivity shadowActivity;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        activity = Robolectric.buildActivity(MainActivity.class).create().visible().get();
        shadowActivity = shadowOf(activity);
    }

    @Test
    public void testSearchButtonClick_launchShowPhotosActivity() throws Exception {
        String TAGS = "someTags";
        EditText searchEditText = activity.findViewById(R.id.searchEditText);
        searchEditText.setText(TAGS);

        Button searchButton = activity.findViewById(R.id.searchButton);
        searchButton.performClick();

        Intent startedIntent = shadowActivity.getNextStartedActivity();

        assertEquals(new ComponentName(activity,ShowPhotosActivity.class)
                     ,startedIntent.getComponent());
        assertEquals(TAGS,startedIntent.getStringExtra("tags"));

    }

    @Test
    public void testSearchButtonClick_nullTagsShowError() throws Exception {
        String TAGS = null;
        EditText searchEditText = activity.findViewById(R.id.searchEditText);
        searchEditText.setText(TAGS);

        Button searchButton = activity.findViewById(R.id.searchButton);
        searchButton.performClick();

        assertEquals(RuntimeEnvironment.application.getString(R.string.search_tags_error)
                    , ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void testSearchButtonClick_emptyTagsShowError() throws Exception {
        String TAGS = "";
        EditText searchEditText = activity.findViewById(R.id.searchEditText);
        searchEditText.setText(TAGS);

        Button searchButton = activity.findViewById(R.id.searchButton);
        searchButton.performClick();

        assertEquals(RuntimeEnvironment.application.getString(R.string.search_tags_error)
                , ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void testViewLikedButtonClick_launchSavedPhotosActivity() throws Exception {
        Button viewLikedButton = activity.findViewById(R.id.viewLikedButton);
        viewLikedButton.performClick();

        Intent startedIntent = shadowActivity.getNextStartedActivity();

        assertEquals(new ComponentName(activity,SavedPhotosActivity.class)
                ,startedIntent.getComponent());
    }

    @Test
    public void testLogoutMenuItemClick_launchLoginActivity() throws Exception {

        FacebookSdk.sdkInitialize(activity);
        shadowActivity.clickMenuItem(R.id.logout);

        Intent intent = shadowActivity.peekNextStartedActivity();

        assertEquals(new ComponentName(activity,LoginActivity.class)
                ,intent.getComponent());
        assertEquals(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
                ,intent.getFlags());

    }

}
