package omazdev.samples.MVPsample.SavedPhotos.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import omazdev.samples.MVPsample.R;
import omazdev.samples.MVPsample.entities.MyPhoto;
import omazdev.samples.MVPsample.libs.base.ImageLoader;

public class SavedPhotosAdapter extends RecyclerView.Adapter<SavedPhotosAdapter.ViewHolder> {

    private List<MyPhoto> photoList;
    private ImageLoader imageLoader;
    private OnItemClickListener onItemClickListener;

    public SavedPhotosAdapter(List<MyPhoto> photoList, ImageLoader imageLoader
                            , OnItemClickListener onItemClickListener) {
        this.photoList = photoList;
        this.imageLoader = imageLoader;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.element_photo_saved, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        imageLoader.load(holder.imageView, photoList.get(position).getFlickrUrl());
        holder.setOnItemClickListener(photoList,photoList.get(position), position, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.deleteBtn)
        ImageButton deleteBtn;
        @BindView(R.id.shareBtn)
        ImageButton shareBtn;

        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            ButterKnife.bind(this, view);

        }

        public void setOnItemClickListener(final List<MyPhoto> photoList
                                         , final MyPhoto currentPhoto
                                         , final int position
                                         , final OnItemClickListener onItemClickListener){

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemclick(currentPhoto, photoList);
                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onDeleteClick(currentPhoto);
                }
            });

            shareBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onShareClick(currentPhoto);
                }
            });

        }

    }

    public void setPhotos(List<MyPhoto> photos) {
        photoList.clear();
        this.photoList = photos;
        this.notifyDataSetChanged();
    }

    public Boolean removePhoto(MyPhoto photo) {
        Boolean containsPhoto = photoList.contains(photo);
        if (containsPhoto){
            int pos = photoList.indexOf(photo);
            photoList.remove(photo);
            notifyItemRemoved(pos);
        }
        return photoList.isEmpty();
    }

}
