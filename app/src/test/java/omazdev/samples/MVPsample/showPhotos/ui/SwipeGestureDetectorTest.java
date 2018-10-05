package omazdev.samples.MVPsample.showPhotos.ui;

import android.view.MotionEvent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import omazdev.samples.MVPsample.BaseTest;
import omazdev.samples.MVPsample.ShowPhotos.ui.SwipeGestureDetector;
import omazdev.samples.MVPsample.ShowPhotos.ui.SwipeGestureListener;

import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 27)
public class SwipeGestureDetectorTest extends BaseTest {

    @Mock private SwipeGestureListener listener;

    private SwipeGestureDetector detector;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        detector = new SwipeGestureDetector(listener);
    }

    @Test
    public void testSwipeRight_shouldCallOnKeep() throws Exception {
        long downTime = 0;
        long moveTime = downTime + 500;
        long upTime = downTime + 1000;
        float xStart = 200;
        float yStart = 200;
        float xEnd = 500;
        float yEnd = 250;

        MotionEvent e1 = MotionEvent.obtain(downTime, moveTime, MotionEvent.ACTION_MOVE
                                            , xStart, yStart, 0);
        MotionEvent e2 = MotionEvent.obtain(downTime, upTime, MotionEvent.ACTION_UP
                                            , xEnd, yEnd, 0);
        float velocityX = 120;

        detector.onFling(e1, e2, velocityX, 0);
        verify(listener).onKeep();
    }

    @Test
    public void testSwipeLeft_shouldCallOnDismiss() throws Exception {
        long downTime = 0;
        long moveTime = downTime + 500;
        long upTime = downTime + 1000;
        float xStart = 200;
        float yStart = 200;
        float xEnd = -500;
        float yEnd = 250;

        MotionEvent e1 = MotionEvent.obtain(downTime, moveTime, MotionEvent.ACTION_MOVE
                                            , xStart, yStart, 0);
        MotionEvent e2 = MotionEvent.obtain(downTime, upTime, MotionEvent.ACTION_UP
                                            , xEnd, yEnd, 0);
        float velocityX = 120;

        detector.onFling(e1, e2, velocityX, 0);
        verify(listener).onDismiss();
    }

    @Test
    public void testSwipeDown_shouldCallOnKeep() throws Exception {
        long downTime = 0;
        long moveTime = downTime + 500;
        long upTime = downTime + 1000;
        float xStart = 200;
        float yStart = 200;
        float xEnd = 250;
        float yEnd = 500;

        MotionEvent e1 = MotionEvent.obtain(downTime, moveTime, MotionEvent.ACTION_MOVE
                                            , xStart, yStart, 0);
        MotionEvent e2 = MotionEvent.obtain(downTime, upTime, MotionEvent.ACTION_UP
                                            , xEnd, yEnd, 0);
        float velocityY = 120;

        detector.onFling(e1, e2, 0, velocityY);
        verify(listener).onKeep();
    }

    @Test
    public void testSwipeUp_shouldCallOnDismiss() throws Exception {
        long downTime = 0;
        long moveTime = downTime + 500;
        long upTime = downTime + 1000;
        float xStart = 200;
        float yStart = 200;
        float xEnd = 250;
        float yEnd = -500;

        MotionEvent e1 = MotionEvent.obtain(downTime, moveTime, MotionEvent.ACTION_MOVE
                                            , xStart, yStart, 0);
        MotionEvent e2 = MotionEvent.obtain(downTime, upTime, MotionEvent.ACTION_UP
                                            , xEnd, yEnd, 0);
        float velocityY = 120;

        detector.onFling(e1, e2, 0, velocityY);
        verify(listener).onDismiss();
    }


}
