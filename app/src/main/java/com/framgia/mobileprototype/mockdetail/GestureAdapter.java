package com.framgia.mobileprototype.mockdetail;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.databinding.ItemGestureBinding;

import java.util.ArrayList;

public class GestureAdapter extends RecyclerView.Adapter<GestureAdapter.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private MockDetailContract.Presenter mListener;
    private String mGesture;
    private ArrayList<GestureAdapter.Gesture> mGestures = new ArrayList<>();

    public GestureAdapter(Context context, String gesture,
                          MockDetailContract.Presenter listener) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mListener = listener;
        mGesture = gesture;
        setUpListGestures();
    }

    private void setUpListGestures() {
        mGestures.clear();
        String[] gestures = mContext.getResources().getStringArray(R.array.title_gesture_list);
        TypedArray icons = mContext.getResources().obtainTypedArray(R.array.icon_gestures);
        for (int i = 0; i < gestures.length; i++) {
            mGestures.add(new Gesture(gestures[i], icons.getResourceId(i, -1)));
        }
        icons.recycle();
    }

    private boolean checkAction(String nameGesture) {
        return nameGesture.equals(mGesture);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ItemGestureBinding mItemGestureBinding;

        ViewHolder(ItemGestureBinding itemGestureBinding) {
            super(itemGestureBinding.getRoot());
            mItemGestureBinding = itemGestureBinding;
            mItemGestureBinding.getRoot().setOnClickListener(this);
        }

        void bindData(Gesture gesture) {
            if (gesture == null) return;
            mItemGestureBinding.setGesture(gesture);
            mItemGestureBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            mListener.setElementGesture(mItemGestureBinding.getGesture().getName());
        }
    }

    @Override
    public GestureAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemGestureBinding itemGestureBinding =
            DataBindingUtil.inflate(mLayoutInflater, R.layout.item_gesture, parent, false);
        return new GestureAdapter.ViewHolder(itemGestureBinding);
    }

    @Override
    public void onBindViewHolder(GestureAdapter.ViewHolder holder, int position) {
        holder.bindData(mGestures.get(position));
    }

    @Override
    public int getItemCount() {
        return null != mGestures ? mGestures.size() : 0;
    }

    public class Gesture {
        private String mName;
        private int mIcon;

        public Gesture(String name, int icon) {
            mName = name;
            mIcon = icon;
        }

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }

        public int getIcon() {
            return mIcon;
        }

        public void setIcon(int icon) {
            mIcon = icon;
        }

        public boolean isAction() {
            return checkAction(mName);
        }
    }
}
