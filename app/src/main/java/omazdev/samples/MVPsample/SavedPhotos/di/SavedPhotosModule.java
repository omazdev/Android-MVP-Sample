package omazdev.samples.MVPsample.SavedPhotos.di;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import omazdev.samples.MVPsample.SavedPhotos.GetPhotosInteractor;
import omazdev.samples.MVPsample.SavedPhotos.GetPhotosInteractorImpl;
import omazdev.samples.MVPsample.SavedPhotos.ManipulatePhotosInteractor;
import omazdev.samples.MVPsample.SavedPhotos.ManipulatePhotosInteractorImpl;
import omazdev.samples.MVPsample.SavedPhotos.SavedPhotosPresenter;
import omazdev.samples.MVPsample.SavedPhotos.SavedPhotosPresenterImpl;
import omazdev.samples.MVPsample.SavedPhotos.SavedPhotosRepository;
import omazdev.samples.MVPsample.SavedPhotos.SavedPhotosRepositoryImpl;
import omazdev.samples.MVPsample.SavedPhotos.ui.SavedPhotosView;
import omazdev.samples.MVPsample.SavedPhotos.ui.adapters.OnItemClickListener;
import omazdev.samples.MVPsample.SavedPhotos.ui.adapters.SavedPhotosAdapter;
import omazdev.samples.MVPsample.entities.MyPhoto;
import omazdev.samples.MVPsample.libs.base.EventBus;
import omazdev.samples.MVPsample.libs.base.ImageLoader;

@Module
public class SavedPhotosModule {

    private SavedPhotosView view;
    private OnItemClickListener onItemClickListener;

    public SavedPhotosModule(SavedPhotosView view, OnItemClickListener onItemClickListener) {
        this.view = view;
        this.onItemClickListener = onItemClickListener;
    }

    @Provides
    @Singleton
    SavedPhotosView providesSavedPhotosView(){
        return this.view;
    }

    @Provides
    @Singleton
    OnItemClickListener providesOnItemClickListener(){
        return this.onItemClickListener;
    }

    @Provides @Singleton
    SavedPhotosPresenter providesSavedPhotosPresenter(EventBus eventBus, SavedPhotosView view
                                                    , GetPhotosInteractor getPhotosInteractor
                                                    , ManipulatePhotosInteractor manipulatePhotosInteractor){
        return new SavedPhotosPresenterImpl( eventBus,  view,  getPhotosInteractor, manipulatePhotosInteractor);

    }

    @Provides @Singleton
    GetPhotosInteractor providesGetPhotosInteractor(SavedPhotosRepository repository){
        return new GetPhotosInteractorImpl(repository);
    }

    @Provides @Singleton
    ManipulatePhotosInteractor providesManipulatePhotosInteractor(SavedPhotosRepository repository){
        return new ManipulatePhotosInteractorImpl(repository);
    }

    @Provides @Singleton
    SavedPhotosRepository providesShowPhotosRepository(EventBus eventBus){
        return new SavedPhotosRepositoryImpl(eventBus);
    }

    @Provides @Singleton
    SavedPhotosAdapter providesSavedPhotosAdapter (List<MyPhoto> photoList, ImageLoader imageLoader, OnItemClickListener onItemClickListener){
        return new SavedPhotosAdapter(photoList, imageLoader, onItemClickListener);
    }

    @Provides @Singleton
    List<MyPhoto> providesEmptyList(){
//        return Collections.emptyList();
        return new ArrayList<MyPhoto>();
    }

}
