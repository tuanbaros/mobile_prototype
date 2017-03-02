package com.framgia.mobileprototype.projectdetail;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.data.model.Mock;
import com.framgia.mobileprototype.databinding.ItemMockBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tuannt on 01/03/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.projectdetail
 */
public class MockAdapter extends RecyclerView.Adapter<MockAdapter.ViewHolder> {
    private List<Mock> mMocks = new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    private ProjectDetailContract.Presenter mListener;

    public MockAdapter(Context context, List<Mock> mocks,
                       ProjectDetailContract.Presenter listener) {
        if (mocks != null) mMocks.addAll(mocks);
        mLayoutInflater = LayoutInflater.from(context);
        mListener = listener;
    }

    public void updateData(Mock mock) {
        if (mock != null) {
            mMocks.add(mock);
            notifyDataSetChanged();
        }
    }

    public void removeItem(int position) {
        mMocks.remove(position);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ItemMockBinding mItemMockBinding;
        private MockItemActionHandler mMockItemActionHandler;

        ViewHolder(ItemMockBinding itemMockBinding) {
            super(itemMockBinding.getRoot());
            mItemMockBinding = itemMockBinding;
            mMockItemActionHandler = new MockItemActionHandler(mListener);
            mItemMockBinding.setHandler(mMockItemActionHandler);
        }

        void bindData(Mock mock, int position) {
            if (mock == null) return;
            mItemMockBinding.setMock(mock);
            mItemMockBinding.setPosition(position);
            mItemMockBinding.executePendingBindings();
        }
    }

    @Override
    public MockAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemMockBinding itemMockBinding =
            DataBindingUtil.inflate(mLayoutInflater, R.layout.item_mock, parent, false);
        return new MockAdapter.ViewHolder(itemMockBinding);
    }

    @Override
    public void onBindViewHolder(MockAdapter.ViewHolder holder, int position) {
        holder.bindData(mMocks.get(position), position);
    }

    @Override
    public int getItemCount() {
        return null != mMocks ? mMocks.size() : 0;
    }
}
