package omazdev.samples.MVPsample.SavedPhotos;

public class GetPhotosInteractorImpl implements GetPhotosInteractor {

    private SavedPhotosRepository repository;

    public GetPhotosInteractorImpl(SavedPhotosRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute() {
        repository.getPhotos();
    }
}
