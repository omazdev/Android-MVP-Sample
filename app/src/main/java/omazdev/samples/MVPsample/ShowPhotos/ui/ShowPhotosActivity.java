package omazdev.samples.MVPsample.ShowPhotos.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import omazdev.samples.MVPsample.MainApplication;
import omazdev.samples.MVPsample.R;
import omazdev.samples.MVPsample.ShowPhotos.ShowPhotosPresenter;

import omazdev.samples.MVPsample.api.response.Photo;
import omazdev.samples.MVPsample.libs.base.ImageLoader;

public class ShowPhotosActivity extends AppCompatActivity implements ShowPhotosView, SwipeGestureListener {

    @BindView(R.id.titleTv)
    TextView titleTv;
    @BindView(R.id.mainImg)
    ImageView mainImg;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.saveButton)
    ImageButton saveButton;
    @BindView(R.id.dismissButton)
    ImageButton dismissButton;

    @Inject ShowPhotosPresenter presenter;
    @Inject ImageLoader imageLoader;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        ButterKnife.bind(this);
        setupInjection();
        setupImageLoader();
        setupGestureDetection();
        handler = new Handler();
        presenter.onCreate();
        setupActionBar();
        checkIntent();
    }

    private void checkIntent() {
        String tags = getIntent().getStringExtra(TAGS);
        ArrayList<String> photosUrlList =getIntent().getStringArrayListExtra(PHOTOS_URL_LIST);
        ArrayList<String> photosTiltleList =getIntent().getStringArrayListExtra(PHOTOS_TITLE_LIST);
        int photoIndex = getIntent().getIntExtra(PHOTO_INDEX,-1);
        presenter.checkIntent(tags,photosUrlList,photosTiltleList,photoIndex);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    public void setupInjection() {
        ((MainApplication) getApplication())
                .getSearchPhotosComponent(this, this)
                .inject(this);
    }

    public void setupImageLoader() {

        RequestListener glideRequestListener = new RequestListener() {

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target
                    , DataSource dataSource, boolean isFirstResource) {
                presenter.onPhotoReady();
                return false;
            }

            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target
                    , boolean isFirstResource) {
                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        presenter.onPhotoFailed();
                    }
                });
                return false;
            }

        };
        imageLoader.setOnFinishedImageLoadingListener(glideRequestListener);
    }

    private void setupGestureDetection() {
        final GestureDetector gestureDetector = new GestureDetector(this
                                              , new SwipeGestureDetector(this));
        View.OnTouchListener gestureOnTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        };

        mainImg.setOnTouchListener(gestureOnTouchListener);

    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("SHOW PHOTOS");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @OnClick({R.id.saveButton, R.id.dismissButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.saveButton:
                onKeep();
                break;
            case R.id.dismissButton:
                onDismiss();
                break;
        }
    }

    @Override
    public void loadPhoto(Photo photo) {
        if (!photo.getFlickrUrl().isEmpty()) {
            imageLoader.load(mainImg, photo.getFlickrUrl());
            titleTv.setText(photo.getTitle());
        } else {
            presenter.loadNextPhoto();
        }
    }

    @Override
    public void loadPhoto(String photoUrl, String photoTitle) {
        imageLoader.load(mainImg, photoUrl);
        titleTv.setText(photoTitle);
    }

    @Override
    public void onKeep() {
        presenter.onKeepPhoto();
    }

    @Override
    public void onDismiss() {
        presenter.onDismissPhoto();
    }

    @Override
    public void showError(int errorId , String errorSupp) {
        String errorMsg = "";

        switch (errorId){
            case R.string.save_photo_failure:
                errorMsg = getString(R.string.save_photo_failure);
                break;
            case R.string.search_photos_max_limit:
                errorMsg = getString(R.string.search_photos_max_limit);
                break;
            case R.string.search_api_response_error :
                errorMsg = getString(R.string.search_api_response_error);
                break;
            case R.string.search_api_endpoint_error :
                errorMsg = getString(R.string.search_api_endpoint_error);
                if (errorSupp != null){
                    errorMsg += " : " + errorSupp;
                }
                break;
        }

        Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showSuccess(int successMsg) {
        String successMessage = "";
        switch (successMsg){
            case R.string.save_photo_success:
                successMessage = getString(R.string.save_photo_success);
                break;
        }

        Toast.makeText(getBaseContext(), successMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finishShowActivity() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1000);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showUIElements() {
        titleTv.setVisibility(View.VISIBLE);
        mainImg.setVisibility(View.VISIBLE);
        saveButton.setVisibility(View.VISIBLE);
        dismissButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideUIElements() {
        titleTv.setVisibility(View.GONE);
        mainImg.setVisibility(View.GONE);
        saveButton.setVisibility(View.GONE);
        dismissButton.setVisibility(View.GONE);
    }

    @Override
    public void makeUIElementsInvisible() {
        titleTv.setVisibility(View.INVISIBLE);
        mainImg.setVisibility(View.INVISIBLE);
        saveButton.setVisibility(View.INVISIBLE);
        dismissButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void saveAnimation() {
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.save_animation);
        anim.setAnimationListener(getAnimationListener());
        mainImg.startAnimation(anim);
    }

    @Override
    public void dismissAnimation() {
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.dismiss_animation);
        anim.setAnimationListener(getAnimationListener());
        mainImg.startAnimation(anim);
    }

    @Override
    public void changeButtonsSourceImg() {
        saveButton.setImageResource(R.drawable.ic_skip_next_black_48dp);
        saveButton.setTag(R.drawable.ic_skip_next_black_48dp);

        dismissButton.setImageResource(R.drawable.ic_skip_previous_black_48dp);
        dismissButton.setTag(R.drawable.ic_skip_previous_black_48dp);
    }

    private Animation.AnimationListener getAnimationListener() {
        return new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                clearImage();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
    }

    private void clearImage() {
        mainImg.setImageResource(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setPresenter(ShowPhotosPresenter presenter) {
        this.presenter = presenter;
    }

    public void setImageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }
}
