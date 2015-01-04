package com.example.ligang.demo_autopullrefreshlistview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * ScrollView反弹效果的实现
 */
public class BounceScrollView extends ScrollView {
    private static final float OVER_SCROLL_RATIO = 0.5f;
    private View inner;// 孩子View

    private float lastScrollY = -1;

    /**
     * y of the last event
     */
    private float lastY = -1;

    private float y;// 点击时y坐标

    private Rect normal = new Rect();// 矩形(这里只是个形式，只是用于判断是否需要动画.)

    private boolean isCount = false;// 是否开始计算

    public BounceScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 根据 XML 生成视图工作完成.该函数在生成视图的最后调用，在所有子视图添加完之后. 即使子类覆盖了 onFinishInflate
     * 方法，也应该调用父类的方法，使该方法得以执行.
     */
    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            inner = getChildAt(0);
        }
    }

    /**
     * 监听touch
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // scroll as default first
//        final boolean result = super.onTouchEvent(ev);
        if (inner != null) {
            handleTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 触摸事件
     *
     * @param ev
     */
    public void handleTouchEvent(MotionEvent ev) {


        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("", "MotionEvent.ACTION_DOWN");
//                lastY = nowY;
                break;

            /***
             * 排除出第一次移动计算，因为第一次无法得知y坐标， 在MotionEvent.ACTION_DOWN中获取不到，
             * 因为此时是MyScrollView的touch事件传递到到了LIstView的孩子item上面.所以从第二次计算开始.
             * 然而我们也要进行初始化，就是第一次移动的时候让滑动距离归0. 之后记录准确了就正常执行.
             */
            case MotionEvent.ACTION_MOVE:
                /*
                final float preY = y;// 按下时的y坐标
                float nowY = ev.getY();// 时时y坐标
                int deltaY = (int) (preY - nowY);// 滑动距离
                if (!isCount) {
                    deltaY = 0; // 在这里要归0.
                }

                y = nowY;
                */
                final float nowY = ev.getY();
                if (lastY == -1) {
                    lastY = nowY;
                }
                final float deltaY = (nowY - lastY) * OVER_SCROLL_RATIO;
                lastY = nowY;
//                Log.e("", "MotionEvent.ACTION_MOVE" + " "+deltaY);
                // 当滚动到最上或者最下时就不会再滚动，这时移动布局
                if (needOverScroll()) {
                    Log.e("", "moving inner view");
                    // 初始化头部矩形
                    if (normal.isEmpty()) {
                        // 保存正常的布局位置
                        normal.set(inner.getLeft(), inner.getTop(), inner.getRight(), inner.getBottom());
                    }
                    // 移动布局
                    if (deltaY > 0) {
                        inner.layout(inner.getLeft(), inner.getTop() + (int)(Math.ceil(deltaY)), inner.getRight(), inner.getBottom() + (int)(Math.ceil(deltaY )));
                    } else {
                        inner.layout(inner.getLeft(), inner.getTop() + (int)(Math.floor(deltaY)), inner.getRight(), inner.getBottom() + (int)(Math.floor(deltaY)));
                    }
//                    Log.e("move", "move"+lastY+" "+deltaY);
                }
                Log.e("", "getScrollY() = " + getScrollY() + " inner.getTop() = " + inner.getTop() + " getInnerViewActualTop() = " + getInnerViewVisualTop());
//                isCount = true;
                break;
            case MotionEvent.ACTION_UP:

                if (needRebound()) {
                    rebound();
//                    isCount = false;
                }
                lastY = -1;
//                lastScrollY = -1;
//                Log.e("", "MotionEvent.ACTION_UP"+" "+getScrollY());
                break;
            default:
                lastY = -1;
//                lastScrollY = -1;
                break;
        }
    }

    public void rebound() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, inner.getTop(), normal.top);
        translateAnimation.setDuration(200);
        inner.startAnimation(translateAnimation);
        inner.layout(normal.left, normal.top, normal.right, normal.bottom);
        normal.setEmpty();
    }

    public boolean needRebound() {
        Log.e("", "getInnerViewActualTop() = "+getInnerViewVisualTop()+"; getInnerViewActualBottom() = "+getInnerViewVisualBottom()+"; getHeight() = "+getHeight());
        return !normal.isEmpty() && (getInnerViewVisualTop() > 0 || getInnerViewVisualBottom() < getHeight());
    }

    /**
     * get inner view's top according to the scrollView and take scroll into consideration
     * @return
     */
    public int getInnerViewVisualTop(){
        return  inner.getTop() - getScrollY();
    }

    public int getInnerViewVisualBottom(){
        return  inner.getBottom() - getScrollY();
    }

    public boolean needOverScroll() {

        final int offset = inner.getMeasuredHeight() - getHeight();
        final float scrollY = getScrollY();
        return scrollY == 0 || scrollY == offset;
        /*
        if (lastScrollY == -1){
            lastScrollY = getScrollY();
        }
//        Log.e("", "needOverScroll"+" "+offset + " "+ lastScrollY);
//        final boolean result = (lastScrollY == 0 && deltaY > 0)|| (lastScrollY == offset && deltaY < 0);
        final boolean result = (lastScrollY == 0 && deltaY > 0)|| (lastScrollY == offset && deltaY < 0) ;

        lastScrollY = getScrollY();

        return  result;
        */
    }
/*

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        // do what you need to with the event, and then...
		if (inner != null) {
			handleTouchEvent(e);
		}
		
        return super.dispatchTouchEvent(e);
    }
*/

}
