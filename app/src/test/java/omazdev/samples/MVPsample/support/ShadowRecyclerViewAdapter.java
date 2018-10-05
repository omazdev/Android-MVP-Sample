package omazdev.samples.MVPsample.support;


import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;

@Implements(RecyclerView.Adapter.class)
public class ShadowRecyclerViewAdapter {

    @RealObject
    private RecyclerView.Adapter realObject;

    private RecyclerView recyclerView;
    private SparseArray<RecyclerView.ViewHolder> holders = new SparseArray<>();;


    @Implementation
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Implementation
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        realObject.onBindViewHolder(holder,position);
        holders.put(position,holder);
    }

    public int getItemCount(){
        return realObject.getItemCount();
    }

    public void itemVisible(int positon){
        RecyclerView.ViewHolder holder =
                realObject.createViewHolder(recyclerView,
                                            realObject.getItemViewType(positon));
        onBindViewHolder(holder,positon);
    }

    public Boolean performClick(int position){
        View holderView = holders.get(position).itemView;
        return holderView.performClick();
    }

    public Boolean performClickOverViewHolder(int position,int viewId){
        Boolean isViewClicked = false;
        View holderView = holders.get(position).itemView;
        View viewToClick = holderView.findViewById(viewId);
        if (viewToClick != null){
            isViewClicked = viewToClick.performClick();
        }
        return isViewClicked;
    }

    public View getViewForHolderPosition(int position) {
        return holders.get(position).itemView;
    }

}
