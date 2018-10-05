package omazdev.samples.MVPsample.libs;

import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;

import omazdev.samples.MVPsample.libs.base.ImageLoader;

public class GlideImageLoader implements ImageLoader {
    private RequestManager glideRequestManager;
    private RequestListener onFinishedLoadingListener;

    public GlideImageLoader(RequestManager glideRequestManager) {
        this.glideRequestManager = glideRequestManager;
    }

    @Override
    public void load(ImageView imageView, String URL) {

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        if (onFinishedLoadingListener != null) {

            glideRequestManager
                    .load(URL)
                    .apply(options)
                    .listener(onFinishedLoadingListener)
                    .into(imageView);
        } else {
            glideRequestManager
                    .load(URL)
                    .apply(options)
                    .into(imageView);
        }
    }

    @Override
    public void setOnFinishedImageLoadingListener(Object listener) {
        if (listener instanceof  RequestListener) {
            this.onFinishedLoadingListener = (RequestListener) listener;
        }
    }
}
