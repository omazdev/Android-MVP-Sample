package omazdev.samples.MVPsample.SavedPhotos.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import omazdev.samples.MVPsample.MainApplication;
import omazdev.samples.MVPsample.R;
import omazdev.samples.MVPsample.SavedPhotos.SavedPhotosPresenter;
import omazdev.samples.MVPsample.SavedPhotos.ui.adapters.OnItemClickListener;
import omazdev.samples.MVPsample.SavedPhotos.ui.adapters.SavedPhotosAdapter;
import omazdev.samples.MVPsample.ShowPhotos.ui.ShowPhotosActivity;
import omazdev.samples.MVPsample.entities.MyPhoto;

public class SavedPhotosActivity extends AppCompatActivity implements SavedPhotosView, OnItemClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.noElementsTv)
    TextView noElementsTv;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;


    @Inject SavedPhotosPresenter presenter;
    @Inject SavedPhotosAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);
        ButterKnife.bind(this);
        setupActionBar();
        setupInjection();
        setupRecyclerView();
        presenter.onCreate();
        getSavedPhotos();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("FAVORITE PHOTOS");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void setupInjection() {
        ((MainApplication) getApplication())
                .getSavedPhotosComponent(this, this,this)
                .inject(this);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);
    }

    private void getSavedPhotos() {
        presenter.getPhotos();
    }

    @Override
    public void onItemclick(MyPhoto photo, List<MyPhoto> photosList) {
        presenter.showClickedPhoto(photo, photosList);
    }

    @Override
    public void onDeleteClick(MyPhoto photo) {
        presenter.deletePhoto(photo);
    }

    @Override
    public void onShareClick(MyPhoto currentPhoto) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                         getString(R.string.share_msg)
                           +" "+currentPhoto.getFlickrUrl());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @Override
    public void setPhotos(List<MyPhoto> photos) {
        adapter.setPhotos(photos);
    }

    @Override
    public void onPhotoDeleted(MyPhoto photo) {
        Boolean isPhotosListEmpty = adapter.removePhoto(photo);
        presenter.onPhotoRemoved(isPhotosListEmpty);
    }

    @Override
    public void openShowPhotosActivity(ArrayList<String> photosUrlList
                                     , ArrayList<String> photosTiltleList
                                     , int photoIndex) {
        Intent intent = new Intent(this, ShowPhotosActivity.class);
        intent.putStringArrayListExtra(PHOTOS_URL_LIST,photosUrlList);
        intent.putStringArrayListExtra(PHOTOS_TITLE_LIST,photosTiltleList);
        intent.putExtra(PHOTO_INDEX,photoIndex);
        startActivity(intent);
    }

    @Override
    public void showError(int errorId) {
        Toast.makeText(getBaseContext(),getString(errorId),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccess(int successId) {
        Toast.makeText(getBaseContext(),getString(successId),Toast.LENGTH_SHORT).show();
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
    public void showNoElementsTv() {
        noElementsTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoElementsTv() {
        noElementsTv.setVisibility(View.GONE);
    }

    @Override
    public void showUIElements() {
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideUIElements() {
        recyclerView.setVisibility(View.GONE);
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

    public void setPresenter(SavedPhotosPresenter presenter) {
        this.presenter = presenter;
    }

    public void setAdapter(SavedPhotosAdapter adapter) {
        this.adapter = adapter;
    }

}
