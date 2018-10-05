package omazdev.samples.MVPsample.ShowPhotos.di;

import javax.inject.Singleton;

import dagger.Component;
import omazdev.samples.MVPsample.ShowPhotos.ui.ShowPhotosActivity;
import omazdev.samples.MVPsample.libs.di.LibsModule;

@Singleton
@Component(modules = {LibsModule.class,ShowPhotosModule.class})
public interface ShowPhotosComponent {

    void inject(ShowPhotosActivity activity);

}
