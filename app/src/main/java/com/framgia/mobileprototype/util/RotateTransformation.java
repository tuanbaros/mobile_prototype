package com.framgia.mobileprototype.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by tuannt on 30/03/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.util
 */
public class RotateTransformation extends BitmapTransformation {
    private float mRotateRotationAngle = 0f;

    public RotateTransformation(Context context, float rotateRotationAngle) {
        super(context);
        this.mRotateRotationAngle = rotateRotationAngle;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        Matrix matrix = new Matrix();
        matrix.postRotate(mRotateRotationAngle);
        return Bitmap.createBitmap(
            toTransform, 0, 0, toTransform.getWidth(), toTransform.getHeight(), matrix, true);
    }

    @Override
    public String getId() {
        return "rotate" + mRotateRotationAngle;
    }
}
