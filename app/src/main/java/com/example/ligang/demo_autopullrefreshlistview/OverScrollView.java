package com.example.ligang.demo_autopullrefreshlistview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;


public class OverScrollView extends ScrollView {

    private static final float OVER_SCROLL_RATIO = 0.5f;
    private static final int REBOUND_DURATION = 200;

    private View innerView;
    private float lastY = -1;
    private Rect originalRect = new Rect();

    public OverScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            innerView = getChildAt(0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (innerView != null) {
            handleTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }

    public void handleTouchEvent(MotionEvent ev) {
        final float currY = ev.getY();
        if (lastY == -1) {
            lastY = currY;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = currY;
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = (currY - lastY) * OVER_SCROLL_RATIO;
                lastY = currY;
                if (needOverScroll(deltaY)) {
                    if (originalRect.isEmpty()) {
                        originalRect.set(innerView.getLeft(), innerView.getTop(), innerView.getRight(), innerView.getBottom());
                    }
                    if (deltaY > 0) {
                        innerView.layout(innerView.getLeft(), innerView.getTop() + (int) (Math.ceil(deltaY)), innerView.getRight(), innerView.getBottom() + (int) (Math.ceil(deltaY)));
                    } else {
                        innerView.layout(innerView.getLeft(), innerView.getTop() + (int) (Math.floor(deltaY)), innerView.getRight(), innerView.getBottom() + (int) (Math.floor(deltaY)));
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (needRebound()) {
                    rebound();
                }
                reset();
                break;
            default:
                reset();
                break;
        }
    }

    private void reset() {
        lastY = -1;
    }

    public void rebound() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, innerView.getTop(), originalRect.top);
        translateAnimation.setDuration(REBOUND_DURATION);
        innerView.startAnimation(translateAnimation);
        innerView.layout(originalRect.left, originalRect.top, originalRect.right, originalRect.bottom);
        originalRect.setEmpty();
    }

    public boolean needRebound() {
        return getInnerViewActualTop() > 0 || getInnerViewActualBottom() < getHeight();
    }

    public int getInnerViewActualTop(){
        return  innerView.getTop() - getScrollY();
    }

    public int getInnerViewActualBottom(){
        return  innerView.getBottom() - getScrollY();
    }

    public boolean needOverScroll(float deltaY) {
        final int offset = innerView.getMeasuredHeight() - getHeight();
        final float scrollY = getScrollY();
        return (scrollY == 0 && deltaY > 0)|| (scrollY == offset && deltaY < 0);
    }
}
