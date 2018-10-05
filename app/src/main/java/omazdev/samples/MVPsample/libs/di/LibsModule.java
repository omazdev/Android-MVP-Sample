package omazdev.samples.MVPsample.libs.di;


import android.app.Activity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import omazdev.samples.MVPsample.libs.GlideImageLoader;
import omazdev.samples.MVPsample.libs.GreenRobotEventBus;
import omazdev.samples.MVPsample.libs.base.EventBus;
import omazdev.samples.MVPsample.libs.base.ImageLoader;

@Module
public class LibsModule {
    private Activity activity;

    public LibsModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @Singleton
    ImageLoader providesImageLoader(RequestManager requestManager) {
        return new GlideImageLoader(requestManager);
    }

    @Provides
    @Singleton
    RequestManager providesRequestManager(Activity activity) {
        return Glide.with(activity);
    }

    @Provides
    @Singleton
    Activity providesActivity() {
        return this.activity;
    }

    @Provides
    @Singleton
    EventBus providesEventBus(org.greenrobot.eventbus.EventBus eventBus) {
        return new GreenRobotEventBus(eventBus);
    }

    @Provides
    @Singleton
    org.greenrobot.eventbus.EventBus providesLibraryEventBus() {
        return org.greenrobot.eventbus.EventBus.getDefault();
    }
}
