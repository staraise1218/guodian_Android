package com.smile.guodian.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;



class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;

    RecyclerViewHolder(View itemView) {
        super(itemView);
        this.mViews = new SparseArray<>();
    }


    View getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return view;
    }
}
