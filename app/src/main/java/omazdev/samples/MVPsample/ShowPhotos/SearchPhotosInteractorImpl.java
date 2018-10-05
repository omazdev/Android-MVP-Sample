package omazdev.samples.MVPsample.ShowPhotos;

public class SearchPhotosInteractorImpl implements SearchPhotosInteractor {

     private ShowPhotosRepository repository;

    public SearchPhotosInteractorImpl(ShowPhotosRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(String tags) {
        repository.searchPhotos(tags);
    }
}
