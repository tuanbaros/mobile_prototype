package com.framgia.mobileprototype.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.request.target.SquaringDrawable;

public class RoundImage extends AppCompatImageView {
    public RoundImage(Context context) {
        super(context);
    }

    public RoundImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null) return;
        if (getWidth() == 0 || getHeight() == 0) return;
        Bitmap b;
        if (drawable instanceof SquaringDrawable) {
            b = ((GlideBitmapDrawable) this.getDrawable().getCurrent()).getBitmap();
        } else {
            b = ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap bitmap = b.copy(Config.ARGB_8888, true);
        int w = getWidth();
        Bitmap roundBitmap = getCroppedBitmap(bitmap, w);
        canvas.drawBitmap(roundBitmap, 0, 0, null);
    }

    private Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap bitmap;
        if (bmp.getWidth() != radius || bmp.getHeight() != radius) {
            float smallest = Math.min(bmp.getWidth(), bmp.getHeight());
            float factor = smallest / radius;
            bitmap = Bitmap.createScaledBitmap(bmp,
                (int) (bmp.getWidth() / factor),
                (int) (bmp.getHeight() / factor), false);
        } else {
            bitmap = bmp;
        }
        Bitmap output = Bitmap.createBitmap(radius, radius, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, radius, radius);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(radius / 2 + 0.7f, radius / 2 + 0.7f, radius / 2 + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}
