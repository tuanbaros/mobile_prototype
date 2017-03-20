package com.framgia.mobileprototype.linkto;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.data.model.Element;
import com.framgia.mobileprototype.databinding.ItemTransitionBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by tuannt on 01/03/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.projectdetail
 */
public class TransitionAdapter extends RecyclerView.Adapter<TransitionAdapter.ViewHolder> {
    private List<String> mTransitions = new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    private LinkToContract.Presenter mListener;
    private Element mElement;
    private Context mContext;

    public TransitionAdapter(Context context, Element element, LinkToContract.Presenter listener) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mListener = listener;
        mElement = element;
        setUpListTransition();
    }

    private void setUpListTransition() {
        String[] listTransitionNames =
            mContext.getResources().getStringArray(R.array.title_transition);
        Collections.addAll(mTransitions, listTransitionNames);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ItemTransitionBinding mItemTransitionBinding;
        private TransitionItemActionHandler mTransitionItemActionHandler;

        ViewHolder(ItemTransitionBinding itemTransitionBinding) {
            super(itemTransitionBinding.getRoot());
            mItemTransitionBinding = itemTransitionBinding;
            mTransitionItemActionHandler = new TransitionItemActionHandler(mListener);
            mItemTransitionBinding.setElement(mElement);
            mItemTransitionBinding.setHandler(mTransitionItemActionHandler);
        }

        void bindData(String transition) {
            if (transition == null) return;
            mItemTransitionBinding.setTransition(transition);
            mItemTransitionBinding.executePendingBindings();
        }
    }

    @Override
    public TransitionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemTransitionBinding itemTransitionBinding =
            DataBindingUtil.inflate(mLayoutInflater, R.layout.item_transition, parent, false);
        return new TransitionAdapter.ViewHolder(itemTransitionBinding);
    }

    @Override
    public void onBindViewHolder(final TransitionAdapter.ViewHolder holder, final int position) {
        holder.bindData(mTransitions.get(position));
    }

    @Override
    public int getItemCount() {
        return null != mTransitions ? mTransitions.size() : 0;
    }
}
