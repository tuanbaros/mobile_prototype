package com.framgia.mobileprototype.linkto;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.data.model.Element;
import com.framgia.mobileprototype.databinding.ItemTransitionBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tuannt on 01/03/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.projectdetail
 */
public class TransitionAdapter extends RecyclerView.Adapter<TransitionAdapter.ViewHolder> {
    private List<Transition> mTransitions = new ArrayList<>();
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
        Resources resources = mContext.getResources();
        mTransitions.add(new Transition(resources.getString(R.string.title_transition_default), 0));
        mTransitions.add(new Transition(resources.getString(
            R.string.title_transition_fade), R.anim.fade_in));
        mTransitions.add(new Transition(resources.getString(
            R.string.title_transition_slide_right), R.anim.slide_right));
        mTransitions.add(new Transition(resources.getString(
            R.string.title_transition_slide_left), R.anim.slide_left));
        mTransitions.add(new Transition(resources.getString(
            R.string.title_transition_slide_bottom), R.anim.slide_top));
        mTransitions.add(new Transition(resources.getString(
            R.string.title_transition_slide_top), R.anim.slide_bottom));
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ItemTransitionBinding mItemTransitionBinding;
        private TransitionItemActionHandler mTransitionItemActionHandler;

        ViewHolder(ItemTransitionBinding itemTransitionBinding) {
            super(itemTransitionBinding.getRoot());
            mItemTransitionBinding = itemTransitionBinding;
            mTransitionItemActionHandler = new TransitionItemActionHandler(mListener);
            mItemTransitionBinding.setElement(mElement);
            mItemTransitionBinding.setHandler(mTransitionItemActionHandler);
            mItemTransitionBinding.getRoot().setOnClickListener(this);
        }

        void bindData(Transition transition) {
            if (transition == null) return;
            mItemTransitionBinding.setTransition(transition);
            mItemTransitionBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            if (mItemTransitionBinding.getTransition().mAnim != 0) {
                Animation animation = AnimationUtils.loadAnimation(
                    mContext, mItemTransitionBinding.getTransition().mAnim);
                mItemTransitionBinding.textTransitionChosen.startAnimation(animation);
            }
            mListener.chooseTransition(mItemTransitionBinding.getTransition().mName, mElement);
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

    public class Transition {
        private String mName;
        private int mAnim;

        public Transition(String name, int anim) {
            this.mName = name;
            this.mAnim = anim;
        }

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            this.mName = name;
        }

        public int getAnim() {
            return mAnim;
        }

        public void setAnim(int anim) {
            this.mAnim = anim;
        }
    }
}
