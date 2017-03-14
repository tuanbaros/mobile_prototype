package com.framgia.mobileprototype.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.framgia.mobileprototype.R;

import java.util.ArrayList;

public class DrawView extends View {
    private Paint mPaint;
    private ArrayList<Point> mPoints;
    private ArrayList<ArrayList<Point>> mStrokes;

    public DrawView(Context context) {
        super(context);
        mPoints = new ArrayList<>();
        mStrokes = new ArrayList<>();
        createPaint(Color.BLACK, context.getResources().getDimension(R.dimen.dp_4));
    }

    public DrawView(Context context, int color) {
        super(context);
        mPoints = new ArrayList<>();
        mStrokes = new ArrayList<>();
        createPaint(color, context.getResources().getDimension(R.dimen.dp_4));
    }

    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
        this.setBackgroundColor(Color.TRANSPARENT);
        for (Object obj : mStrokes) {
            drawStroke((ArrayList) obj, c);
        }
        drawStroke(mPoints, c);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
            this.setTag(true);
            mPoints.add(new Point((int) event.getX(), (int) event.getY()));
            invalidate();
        }
        if (event.getActionMasked() == MotionEvent.ACTION_UP) {
            this.mStrokes.add(mPoints);
            mPoints = new ArrayList<>();
            RelativeLayout parent = (RelativeLayout) this.getParent();
            parent.addView(new DrawView(parent.getContext(), (int) parent.getTag()));
        }
        return true;
    }

    private void drawStroke(ArrayList stroke, Canvas c) {
        if (stroke.size() <= 0) return;
        Point p0 = (Point) stroke.get(0);
        for (int i = 1; i < stroke.size(); i++) {
            Point p1 = (Point) stroke.get(i);
            c.drawLine(p0.x, p0.y, p1.x, p1.y, mPaint);
            p0 = p1;
        }
    }

    private void createPaint(int color, float width) {
        Paint temp = new Paint();
        temp.setStyle(Paint.Style.STROKE);
        temp.setAntiAlias(true);
        temp.setColor(color);
        temp.setStrokeWidth(width);
        temp.setStrokeCap(Paint.Cap.ROUND);
        mPaint = temp;
    }
}

