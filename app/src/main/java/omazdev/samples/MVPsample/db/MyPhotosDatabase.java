package omazdev.samples.MVPsample.db;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = MyPhotosDatabase.NAME ,version = MyPhotosDatabase.VERSION)
public class MyPhotosDatabase {
    public static final int VERSION = 1;
    public static final String NAME = "MyPhotos";
}
