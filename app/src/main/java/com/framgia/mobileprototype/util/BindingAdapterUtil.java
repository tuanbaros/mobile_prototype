package com.framgia.mobileprototype.util;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.framgia.mobileprototype.Constant;
import com.framgia.mobileprototype.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
        if (path == null) {
            view.setImageResource(R.mipmap.ic_launcher);
            return;
        }
        try {
            InputStream inputStream = view.getContext().getAssets().open(path);
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            view.setImageDrawable(drawable);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            String filePath =
                Environment.getExternalStorageDirectory() + Constant.FILE_PATH + path;
            File imgFile = new File(filePath);
            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                view.setImageBitmap(bitmap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
