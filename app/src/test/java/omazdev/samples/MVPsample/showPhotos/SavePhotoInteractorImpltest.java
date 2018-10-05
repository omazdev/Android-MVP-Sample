package omazdev.samples.MVPsample.showPhotos;

import org.junit.Test;
import org.mockito.Mock;

import omazdev.samples.MVPsample.BaseTest;
import omazdev.samples.MVPsample.ShowPhotos.SavePhotoInteractor;
import omazdev.samples.MVPsample.ShowPhotos.SavePhotoInteractorImpl;
import omazdev.samples.MVPsample.ShowPhotos.ShowPhotosRepository;
import omazdev.samples.MVPsample.entities.MyPhoto;

import static org.mockito.Mockito.verify;

public class SavePhotoInteractorImpltest extends BaseTest {

    @Mock private ShowPhotosRepository repository;
    @Mock private MyPhoto photoToSave;

    private SavePhotoInteractor interactor;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        interactor = new SavePhotoInteractorImpl(repository);
    }

    @Test
    public void testExecute() throws Exception {
        interactor.execute(photoToSave);
        verify(repository).savePhoto(photoToSave);
    }
}
