package omazdev.samples.MVPsample.SavedPhotos.ui.adapters;

import java.util.List;

import omazdev.samples.MVPsample.entities.MyPhoto;

public interface OnItemClickListener {
    void onItemclick(MyPhoto photo, List<MyPhoto> photoList);
    void onDeleteClick(MyPhoto photo);
    void onShareClick(MyPhoto currentPhoto);
}
