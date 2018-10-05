package omazdev.samples.MVPsample;

import android.app.Application;
import android.content.Intent;

import com.facebook.login.LoginManager;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;


import omazdev.samples.MVPsample.SavedPhotos.di.DaggerSavedPhotosComponent;
import omazdev.samples.MVPsample.SavedPhotos.di.SavedPhotosComponent;
import omazdev.samples.MVPsample.SavedPhotos.di.SavedPhotosModule;
import omazdev.samples.MVPsample.SavedPhotos.ui.SavedPhotosActivity;
import omazdev.samples.MVPsample.SavedPhotos.ui.SavedPhotosView;
import omazdev.samples.MVPsample.SavedPhotos.ui.adapters.OnItemClickListener;


import omazdev.samples.MVPsample.ShowPhotos.di.DaggerShowPhotosComponent;
import omazdev.samples.MVPsample.ShowPhotos.di.ShowPhotosComponent;
import omazdev.samples.MVPsample.ShowPhotos.di.ShowPhotosModule;
import omazdev.samples.MVPsample.ShowPhotos.ui.ShowPhotosActivity;
import omazdev.samples.MVPsample.ShowPhotos.ui.ShowPhotosView;
import omazdev.samples.MVPsample.libs.di.LibsModule;
import omazdev.samples.MVPsample.login.ui.LoginActivity;


public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        initFacebook();
        initDB();
    }

//    private void initFacebook() {
//        FacebookSdk.sdkInitialize(this);
//    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        DBTearDown();
    }

    private void initDB() {
        //FlowManager.init(this);
        FlowManager.init(new FlowConfig.Builder(this).build());
        // verbose logging
         FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);
    }

    private void DBTearDown() {
        FlowManager.destroy();
    }

    public void logout() {
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public ShowPhotosComponent getSearchPhotosComponent(ShowPhotosActivity activity, ShowPhotosView view){
        return  DaggerShowPhotosComponent
                .builder()
                .libsModule(new LibsModule(activity))
                .showPhotosModule(new ShowPhotosModule(view))
                .build();
    }

    public SavedPhotosComponent getSavedPhotosComponent(SavedPhotosActivity activity, SavedPhotosView view
            , OnItemClickListener onItemClickListener){

        return DaggerSavedPhotosComponent
                .builder()
                .libsModule(new LibsModule(activity))
                .savedPhotosModule(new SavedPhotosModule(view,onItemClickListener))
                .build();

    }
    
}
