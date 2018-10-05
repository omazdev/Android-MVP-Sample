package omazdev.samples.MVPsample.SavedPhotos;

import org.junit.Test;
import org.mockito.Mock;

import omazdev.samples.MVPsample.BaseTest;
import omazdev.samples.MVPsample.entities.MyPhoto;

import static org.mockito.Mockito.verify;

public class ManipulatePhotosInteractorImplTest extends BaseTest {

    @Mock private SavedPhotosRepository repository;
    @Mock private MyPhoto photo;

    private ManipulatePhotosInteractorImpl interactor;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        interactor = new ManipulatePhotosInteractorImpl(repository);
    }

    @Test
    public void testExecute() throws Exception {
        interactor.execute(photo);
        verify(repository).deletePhoto(photo);
    }
}
