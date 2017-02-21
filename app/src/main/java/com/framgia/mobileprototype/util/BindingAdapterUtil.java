package com.framgia.mobileprototype.util;

import android.databinding.BindingAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

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
}
