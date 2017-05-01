package com.framgia.mobileprototype.util;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.framgia.mobileprototype.Constant;
import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.mockdetail.MockDetailContract;
import com.framgia.mobileprototype.ui.widget.CustomRelativeLayout;

import java.io.File;

/**
 * Created by tuannt on 21/02/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.util
 */
public class BindingAdapterUtil {
    @BindingAdapter({"adapter", "listener"})
    public static void setPagerAdapter(ViewPager view, PagerAdapter adapter,
                                       ViewPager.OnPageChangeListener onPageChangeListener) {
        view.setAdapter(adapter);
        view.setCurrentItem(0);
        view.addOnPageChangeListener(onPageChangeListener);
    }

    @BindingAdapter({"src"})
    public static void setImageResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }

    @BindingAdapter({"avatar"})
    public static void setAvatar(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
            .load(url)
            .error(R.drawable.ic_avatar_default)
            .into(imageView);
    }

    @BindingAdapter({"text"})
    public static void setTextResource(TextView textView, int resource) {
        if (resource != 0) textView.setText(resource);
    }

    @BindingAdapter({"actionBar"})
    public static void setSupportActionBar(Toolbar toolbar, AppCompatActivity appCompatActivity) {
        appCompatActivity.setSupportActionBar(toolbar);
    }

    @BindingAdapter({"isOpenDrawer"})
    public static void setOpenDrawer(DrawerLayout drawer, boolean isOpenDrawer) {
        if (isOpenDrawer) drawer.openDrawer(GravityCompat.START);
        else drawer.closeDrawer(GravityCompat.START);
    }

    @BindingAdapter({"adapter"})
    public static void setAdapter(RecyclerView view, RecyclerView.Adapter adapter) {
        view.setAdapter(adapter);
        view.setHasFixedSize(true);
    }

    @BindingAdapter({"layoutManager"})
    public static void setLayoutManager(RecyclerView view,
                                        LayoutManagerUtil.LayoutManagerFactory layoutManagerUtil) {
        view.setLayoutManager(layoutManagerUtil.create(view));
    }

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String path) {
        String filePath = Constant.FILE_PATH + path;
        File imgFile = new File(filePath);
        if (imgFile.exists()) {
            Glide.with(view.getContext())
                .load(imgFile)
                .signature(new StringSignature(imgFile.getName() + imgFile.lastModified()))
                .into(view);
            return;
        }
        Glide.with(view.getContext())
            .load(Uri.parse(Constant.ASSET_PATH + path))
            .error(R.mipmap.ic_launcher)
            .into(view);
    }

    @BindingAdapter({"navListener"})
    public static void setListener(final NavigationView view,
                                   NavigationView.OnNavigationItemSelectedListener listener) {
        view.setCheckedItem(R.id.nav_project);
        view.setNavigationItemSelectedListener(listener);
    }

    @BindingAdapter({"touchHandler"})
    public static void setTouchHandler(CustomRelativeLayout view,
                                       MockDetailContract.Presenter listener) {
        view.setPresenter(listener);
    }

    @BindingAdapter({"hidden", "animation"})
    public static void setAnimation(TextView chosen, TextView normal, String animation) {
        Context context = chosen.getContext();
        Resources resources = chosen.getResources();
        if (animation.equals(resources.getString(R.string.title_transition_default))) {
            chosen.setVisibility(View.VISIBLE);
            return;
        }
        if (animation.equals(resources.getString(R.string.title_transition_fade))) {
            chosen.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in));
            chosen.setVisibility(View.VISIBLE);
            return;
        }
        if (animation.equals(resources.getString(R.string.title_transition_slide_left))) {
            chosen.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_left));
            chosen.setVisibility(View.VISIBLE);
            return;
        }
        if (animation.equals(resources.getString(R.string.title_transition_slide_right))) {
            chosen.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_right));
            chosen.setVisibility(View.VISIBLE);
            return;
        }
    }

    @BindingAdapter({"behavier"})
    public static void setBehavier(RecyclerView recyclerView, final FloatingActionButton fab) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) fab.hide();
                else fab.show();
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @BindingAdapter({"mockImage", "rotate"})
    public static void loadMockImage(ImageView view, String path, boolean rotate) {
        String filePath = Constant.FILE_PATH + path;
        File imgFile = new File(filePath);
        if (imgFile.exists()) {
            Glide.with(view.getContext())
                .load(imgFile)
                .transform(rotate ? new RotateTransformation(view.getContext(), 90f) : null)
                .signature(new StringSignature(imgFile.getName() + imgFile.lastModified()))
                .into(view);
            return;
        }
        Glide.with(view.getContext())
            .load(Uri.parse(Constant.ASSET_PATH + path))
            .error(R.mipmap.ic_launcher)
            .into(view);
    }
}
