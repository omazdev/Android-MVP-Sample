package omazdev.samples.MVPsample.SavedPhotos.ui;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadow.api.Shadow;

import java.util.List;

import omazdev.samples.MVPsample.BaseTest;
import omazdev.samples.MVPsample.R;
import omazdev.samples.MVPsample.SavedPhotos.ui.adapters.OnItemClickListener;
import omazdev.samples.MVPsample.SavedPhotos.ui.adapters.SavedPhotosAdapter;
import omazdev.samples.MVPsample.entities.MyPhoto;
import omazdev.samples.MVPsample.libs.base.ImageLoader;
import omazdev.samples.MVPsample.support.ShadowRecyclerViewAdapter;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 27,shadows = {ShadowRecyclerViewAdapter.class})
public class SavedPhotosAdapterTest extends BaseTest {

    @Mock private List<MyPhoto> photoList;
    @Mock private ImageLoader imageLoader;
    @Mock private OnItemClickListener onItemClickListener;
    @Mock private MyPhoto photo;

    private SavedPhotosAdapter adapter;
    private ShadowRecyclerViewAdapter shadowAdapter;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        adapter = new SavedPhotosAdapter(photoList,imageLoader,onItemClickListener);
        shadowAdapter = Shadow.extract(adapter);
        
        Activity activity = Robolectric.setupActivity(Activity.class);
        RecyclerView recyclerView = new RecyclerView(activity);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(adapter);
    }

    @Test
    public void testSetPhotos_itemCountMatches() throws Exception {
        int itemCount = 15;
        when(photoList.size()).thenReturn(itemCount);

        adapter.setPhotos(photoList);

        assertEquals(itemCount,adapter.getItemCount());
    }

    @Test
    public void testremovePhoto_shouldRemoveItem() throws Exception {
        when(photoList.contains(photo)).thenReturn(true);
        when(photoList.indexOf(photo)).thenReturn(0);

        adapter.removePhoto(photo);

        verify(photoList).remove(photo);
    }

    @Test
    public void testOnItemClick_shouldCallListener() throws Exception {
        int itemPosition = 4;
        String URL = "someURL";

        when(photoList.get(itemPosition)).thenReturn(photo);
        when(photoList.get(itemPosition).getFlickrUrl()).thenReturn(URL);

        shadowAdapter.itemVisible(itemPosition);
        shadowAdapter.performClick(itemPosition);

        verify(onItemClickListener).onItemclick(photo,photoList);
    }

    @Test
    public void testonDeleteClick_shouldCallListener() throws Exception {
        int itemPosition = 4;
        String URL = "someURL";

        when(photoList.get(itemPosition)).thenReturn(photo);
        when(photoList.get(itemPosition).getFlickrUrl()).thenReturn(URL);

        shadowAdapter.itemVisible(itemPosition);
        shadowAdapter.performClickOverViewHolder(itemPosition, R.id.deleteBtn);

        verify(onItemClickListener).onDeleteClick(photo);
    }

    @Test
    public void testonShareClick_shouldCallListener() throws Exception {
        int itemPosition = 4;
        String URL = "someURL";

        when(photoList.get(itemPosition)).thenReturn(photo);
        when(photoList.get(itemPosition).getFlickrUrl()).thenReturn(URL);

        shadowAdapter.itemVisible(itemPosition);
        shadowAdapter.performClickOverViewHolder(itemPosition, R.id.shareBtn);

        verify(onItemClickListener).onShareClick(photo);
    }

}
