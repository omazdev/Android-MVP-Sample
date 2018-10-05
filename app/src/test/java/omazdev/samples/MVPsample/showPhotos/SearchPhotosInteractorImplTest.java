package omazdev.samples.MVPsample.showPhotos;

import org.junit.Test;
import org.mockito.Mock;

import omazdev.samples.MVPsample.BaseTest;
import omazdev.samples.MVPsample.ShowPhotos.SearchPhotosInteractor;
import omazdev.samples.MVPsample.ShowPhotos.SearchPhotosInteractorImpl;
import omazdev.samples.MVPsample.ShowPhotos.ShowPhotosRepository;

import static org.mockito.Mockito.verify;

public class SearchPhotosInteractorImplTest extends BaseTest {

    @Mock private ShowPhotosRepository repository;

    private SearchPhotosInteractor interactor;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        interactor = new SearchPhotosInteractorImpl(repository);
    }

    @Test
    public void testExecute() throws Exception {
        String tags = "someTags";
        interactor.execute(tags);

        verify(repository).searchPhotos(tags);
    }
}
