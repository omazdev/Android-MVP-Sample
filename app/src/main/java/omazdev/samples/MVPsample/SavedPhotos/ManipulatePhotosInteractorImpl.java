package omazdev.samples.MVPsample.SavedPhotos;


import omazdev.samples.MVPsample.entities.MyPhoto;

public class ManipulatePhotosInteractorImpl implements ManipulatePhotosInteractor {

    private SavedPhotosRepository repository;

    public ManipulatePhotosInteractorImpl(SavedPhotosRepository repository) {
        this.repository = repository;
    }


    @Override
    public void execute(MyPhoto photo) {
        repository.deletePhoto(photo);
    }
}
