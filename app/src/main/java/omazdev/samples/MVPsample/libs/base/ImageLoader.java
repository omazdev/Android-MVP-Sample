package omazdev.samples.MVPsample.libs.base;

import android.widget.ImageView;

public interface ImageLoader {
    void load(ImageView imageView, String URL);
    void setOnFinishedImageLoadingListener(Object listener);
}
