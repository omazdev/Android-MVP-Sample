package omazdev.samples.MVPsample.ShowPhotos.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import omazdev.samples.MVPsample.ShowPhotos.SavePhotoInteractor;
import omazdev.samples.MVPsample.ShowPhotos.SavePhotoInteractorImpl;
import omazdev.samples.MVPsample.ShowPhotos.SearchPhotosInteractor;
import omazdev.samples.MVPsample.ShowPhotos.SearchPhotosInteractorImpl;
import omazdev.samples.MVPsample.ShowPhotos.ShowPhotosPresenter;
import omazdev.samples.MVPsample.ShowPhotos.ShowPhotosPresenterImpl;
import omazdev.samples.MVPsample.ShowPhotos.ShowPhotosRepository;
import omazdev.samples.MVPsample.ShowPhotos.ShowPhotosRepositoryImpl;
import omazdev.samples.MVPsample.ShowPhotos.ui.ShowPhotosView;
import omazdev.samples.MVPsample.api.FlickrClient;
import omazdev.samples.MVPsample.api.FlickrService;
import omazdev.samples.MVPsample.libs.base.EventBus;

@Module
public class ShowPhotosModule {
    private ShowPhotosView view;

    public ShowPhotosModule(ShowPhotosView view) {
        this.view = view;
    }

    @Provides
    @Singleton
    ShowPhotosView providesShowPhotosView(){
        return this.view;
    }

    @Provides @Singleton
    ShowPhotosPresenter providesShowPhotosPresenter(EventBus eventBus, ShowPhotosView view
                                                    , SearchPhotosInteractor searchInteractor
                                                    ,SavePhotoInteractor savePhotoInteractor){

        return new ShowPhotosPresenterImpl( eventBus,  view,  searchInteractor, savePhotoInteractor);

    }

    @Provides @Singleton
    SearchPhotosInteractor providesSearchPhotosInteractor(ShowPhotosRepository repository){
        return new SearchPhotosInteractorImpl(repository);
    }

    @Provides @Singleton
    SavePhotoInteractor providesSavePhotoInteractor(ShowPhotosRepository repository){
        return new SavePhotoInteractorImpl(repository);
    }

    @Provides @Singleton
    ShowPhotosRepository providesShowPhotosRepository(EventBus eventBus, FlickrService service){
        return new ShowPhotosRepositoryImpl( eventBus,  service);
    }

    @Provides @Singleton
    FlickrService providesFlickrService(){
        return new FlickrClient().getFlickrService();
    }
}
