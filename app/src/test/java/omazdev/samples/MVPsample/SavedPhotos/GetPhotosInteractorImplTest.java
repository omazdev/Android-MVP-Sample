package omazdev.samples.MVPsample.SavedPhotos;

import org.junit.Test;
import org.mockito.Mock;

import omazdev.samples.MVPsample.BaseTest;

import static org.mockito.Mockito.verify;

public class GetPhotosInteractorImplTest extends BaseTest {

    @Mock private SavedPhotosRepository repository;

    private GetPhotosInteractorImpl interactor;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        interactor = new GetPhotosInteractorImpl(repository);
    }

    @Test
    public void testExecute() throws Exception {
        interactor.execute();
        verify(repository).getPhotos();
    }
}
