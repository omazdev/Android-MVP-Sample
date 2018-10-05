package omazdev.samples.MVPsample.ShowPhotos;


import omazdev.samples.MVPsample.entities.MyPhoto;

public class SavePhotoInteractorImpl implements SavePhotoInteractor {

    private ShowPhotosRepository repository;

    public SavePhotoInteractorImpl(ShowPhotosRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(MyPhoto photo) {
//        MyPhoto photoToSave = new MyPhoto(photo);
        repository.savePhoto(photo);
    }
}
