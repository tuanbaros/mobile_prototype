package com.framgia.mobileprototype.util;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.framgia.mobileprototype.Constant;
import com.framgia.mobileprototype.R;

import java.io.File;
import java.io.InputStream;

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
}
