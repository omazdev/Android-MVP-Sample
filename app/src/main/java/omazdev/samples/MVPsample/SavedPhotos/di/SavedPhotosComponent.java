package omazdev.samples.MVPsample.SavedPhotos.di;

import javax.inject.Singleton;

import dagger.Component;
import omazdev.samples.MVPsample.SavedPhotos.ui.SavedPhotosActivity;
import omazdev.samples.MVPsample.libs.di.LibsModule;

@Singleton
@Component(modules = {LibsModule.class,SavedPhotosModule.class})
public interface SavedPhotosComponent {

    void inject(SavedPhotosActivity activity);
}
