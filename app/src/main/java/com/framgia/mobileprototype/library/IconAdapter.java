package com.framgia.mobileprototype.library;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.framgia.mobileprototype.R;
import com.mikepenz.iconics.view.IconicsImageView;
import java.util.ArrayList;

/**
 * Created by FRAMGIA\nguyen.thanh.tuan on 15/05/2017.
 */

class IconAdapter extends RecyclerView.Adapter<IconAdapter.ViewHolder> {

    private ArrayList<String> mIcons = new ArrayList<>();
    private IconItemHandler mIconItemHandler;

    IconAdapter(IconItemHandler iconItemHandler) {
        mIconItemHandler = iconItemHandler;
    }

    void setUpData(ArrayList<String> icons) {
        if (icons == null) return;
        mIcons = icons;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_icon, parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(mIcons.get(position));
    }

    @Override
    public int getItemCount() {
        return mIcons.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private IconicsImageView mIconicsImageView;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mIconicsImageView = (IconicsImageView) itemView.findViewById(R.id.image_icon);
        }

        void bindData(String icon) {
            mIconicsImageView.setIcon(icon);
        }

        @Override
        public void onClick(View v) {
            if (mIconItemHandler == null) return;
            mIconItemHandler.itemClicked(mIcons.get(getAdapterPosition()));
        }
    }
}
