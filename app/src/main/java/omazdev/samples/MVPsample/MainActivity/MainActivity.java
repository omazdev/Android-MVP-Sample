package omazdev.samples.MVPsample.MainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import omazdev.samples.MVPsample.MainApplication;
import omazdev.samples.MVPsample.R;
import omazdev.samples.MVPsample.SavedPhotos.ui.SavedPhotosActivity;
import omazdev.samples.MVPsample.ShowPhotos.ui.ShowPhotosActivity;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.searchEditText)
    EditText searchEditText;
    @BindView(R.id.searchButton)
    Button searchButton;
    @BindView(R.id.viewLikedButton)
    Button viewLikedButton;

    static final String TAGS = "tags";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.searchButton, R.id.viewLikedButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.searchButton:
                String tags = getTags();
                if (tags != null){
                    openShowPhotosActivity(tags);
                }
                else {
                    tagsError(getString(R.string.search_tags_error));
                }
                break;
            case R.id.viewLikedButton:
                openSavedPhotosActivity();
                break;
        }
    }

    private void openShowPhotosActivity(String tags) {
        Intent intent = new Intent(this, ShowPhotosActivity.class);
        intent.putExtra(TAGS,tags);
        startActivity(intent);
    }

    private void openSavedPhotosActivity() {
        Intent intent = new Intent(this, SavedPhotosActivity.class);
        startActivity(intent);
    }

    private String getTags() {
        if (searchEditText.getText() == null){
            return null;
        }else {
            String tags = searchEditText.getText().toString();
            if (tags.isEmpty()){
                tagsError(getString(R.string.search_tags_error));
                return null;
            }else {
                return tags;
            }
        }
    }

    private void tagsError(String error) {
        Toast.makeText(this,error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemid = item.getItemId();
        switch (itemid) {
            case R.id.logout:
                logout();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        MainApplication mainApp = (MainApplication) getApplication();
        mainApp.logout();
    }
}
