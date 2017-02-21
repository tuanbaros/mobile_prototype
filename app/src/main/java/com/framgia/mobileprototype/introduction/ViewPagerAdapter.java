package com.framgia.mobileprototype.introduction;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.databinding.ItemViewPagerIntroductionBinding;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<Introduction> mIntroductions = new ArrayList<>();

    public ViewPagerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        getIntroductions();
    }

    private void getIntroductions() {
        mIntroductions.add(new Introduction(
            R.string.title_idea, R.string.msg_description, R.drawable.image_tutorial_1));
        mIntroductions.add(new Introduction(
            R.string.title_take_picture, 0, R.drawable.image_tutorial_2));
        mIntroductions.add(new Introduction(
            R.string.title_link, 0, R.drawable.image_tutorial_3));
        mIntroductions.add(new Introduction(
            R.string.title_play, 0, R.drawable.image_tutorial_4));
    }

    @Override
    public int getCount() {
        return mIntroductions.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Introduction introduction = mIntroductions.get(position);
        ItemViewPagerIntroductionBinding binding =
            DataBindingUtil
                .inflate(mLayoutInflater, R.layout.item_view_pager_introduction, container, false);
        binding.setIntroduction(introduction);
        container.addView(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    public class Introduction {
        private int mTitle;
        private int mDescription;
        private int mImage;

        Introduction(int title, int description, int image) {
            mDescription = description;
            mImage = image;
            mTitle = title;
        }

        public int getDescription() {
            return mDescription;
        }

        public int getImage() {
            return mImage;
        }

        public int getTitle() {
            return mTitle;
        }
    }
}
