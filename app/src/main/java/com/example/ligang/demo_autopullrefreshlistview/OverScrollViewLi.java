package com.example.ligang.demo_autopullrefreshlistview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;


public class OverScrollViewLi extends ScrollView {

    private static final float OVER_SCROLL_RATIO = 0.5f;
    private static final int REBOUND_DURATION = 200;

    private View innerView;

    /**
     * y coordinate of the last event
     */
    private float lastY = -1;

    /**
     * record y coordinate when beg overScrolling, used in changing the position of the innerView
     */
    private float overScrollStartY = -1;


    private Rect originalRect = new Rect();// 矩形(这里只是个形式，只是用于判断是否需要动画.)

    private boolean isCount = false;// 是否开始计算

    public OverScrollViewLi(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 根据 XML 生成视图工作完成.该函数在生成视图的最后调用，在所有子视图添加完之后. 即使子类覆盖了 onFinishInflate
     * 方法，也应该调用父类的方法，使该方法得以执行.
     */
    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            innerView = getChildAt(0);
        }
    }

    /**
     * 监听touch
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // scroll as default first
//        final boolean result = super.onTouchEvent(ev);
        if (innerView != null) {
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

        final float currY = ev.getY();
        if (lastY == -1) {
            lastY = currY;
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("", "MotionEvent.ACTION_DOWN");
                lastY = currY;
                break;

            /***
             * 排除出第一次移动计算，因为第一次无法得知y坐标， 在MotionEvent.ACTION_DOWN中获取不到，
             * 因为此时是MyScrollView的touch事件传递到到了LIstView的孩子item上面.所以从第二次计算开始.
             * 然而我们也要进行初始化，就是第一次移动的时候让滑动距离归0. 之后记录准确了就正常执行.
             */
            case MotionEvent.ACTION_MOVE:
                /*
                final float preY = y;// 按下时的y坐标
                float currY = ev.getY();// 时时y坐标
                int deltaY = (int) (preY - currY);// 滑动距离
                if (!isCount) {
                    deltaY = 0; // 在这里要归0.
                }

                y = currY;
                */

                final float deltaY = (currY - lastY) * OVER_SCROLL_RATIO;
                lastY = currY;
//                Log.e("", "MotionEvent.ACTION_MOVE" + " "+deltaY);
                // 当滚动到最上或者最下时就不会再滚动，这时移动布局
                if (needOverScroll(deltaY)) {
                    Log.e("", "moving innerView view");
                    if (originalRect.isEmpty()) {
                        // 保存正常的布局位置
                        originalRect.set(innerView.getLeft(), innerView.getTop(), innerView.getRight(), innerView.getBottom());
                        overScrollStartY = currY;
                    }
                    // 移动布局

                    if (deltaY > 0) {
                        innerView.layout(innerView.getLeft(), innerView.getTop() + (int) (Math.ceil(deltaY)), innerView.getRight(), innerView.getBottom() + (int) (Math.ceil(deltaY)));
                    } else {
                        innerView.layout(innerView.getLeft(), innerView.getTop() + (int) (Math.floor(deltaY)), innerView.getRight(), innerView.getBottom() + (int) (Math.floor(deltaY)));
                    }

                    /*
                    final float offsetY = (currY - overScrollStartY) * OVER_SCROLL_RATIO;
                    innerView.layout(originalRect.left, (int)(originalRect.top+offsetY), originalRect.right, (int)(originalRect.bottom+offsetY));
                    */
                }
                Log.e("", "getScrollY() = " + getScrollY() + " innerView.getTop() = " + innerView.getTop() + " getInnerViewActualTop() = " + getInnerViewActualTop());
                break;
            case MotionEvent.ACTION_UP:

                if (needRebound()) {
                    rebound();
//                    isCount = false;
                }
                reset();
//                lastScrollY = -1;
//                Log.e("", "MotionEvent.ACTION_UP"+" "+getScrollY());
                break;
            default:
                reset();
//                lastScrollY = -1;
                break;
        }
    }

    private void reset() {
        lastY = -1;
        // innerView.getTop > 0 && scrollY > 0
//        setScrollY(getScrollY() - innerView.getTop());
//        innerView.layout(getLeft(), 0, getRight(), getHeight());
        Log.e("reset", "getScrollY() = " + getScrollY() + " innerView.getTop() = " + innerView.getTop() + " getInnerViewActualTop() = " + getInnerViewActualTop());

    }

    public void rebound() {
        /*
        TranslateAnimation translateAnimation = null;
        float dy = 0;
        if (getInnerViewActualTop() > 0){
            // rebound up to top
            dy = 0 - getInnerViewActualTop();
        } else if (getInnerViewActualBottom() < getHeight()){
            // rebound down to bottom
            dy = getHeight() - getInnerViewActualBottom();
        }
        translateAnimation = new TranslateAnimation(0, 0, getTop(), getTop()+dy);
        */
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, innerView.getTop(), originalRect.top);
        translateAnimation.setDuration(REBOUND_DURATION);
        innerView.startAnimation(translateAnimation);

        innerView.layout(originalRect.left, originalRect.top, originalRect.right, originalRect.bottom);

        originalRect.setEmpty();
    }

    public boolean needRebound() {
//        Log.e("", "getInnerViewActualTop() = "+ getInnerViewActualTop()+"; getInnerViewActualBottom() = "+ getInnerViewActualBottom()+"; getHeight() = "+getHeight());
        return getInnerViewActualTop() > 0 || getInnerViewActualBottom() < getHeight();
    }

    /**
     * get innerView's actual top according to the scrollView coordinate and take the invisible part into consideration
     * @return
     */
    public int getInnerViewActualTop(){
        return  innerView.getTop() - getScrollY();
    }

    public int getInnerViewActualBottom(){
        return  innerView.getBottom() - getScrollY();
    }

    public boolean needOverScroll(float deltaY) {
        final int offset = innerView.getMeasuredHeight() - getHeight();
        final float scrollY = getScrollY();
//        return (scrollY == 0 )|| (scrollY == offset);
        return (scrollY == 0 && deltaY > 0)|| (scrollY == offset && deltaY < 0);
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
		if (innerView != null) {
			handleTouchEvent(e);
		}
		
        return super.dispatchTouchEvent(e);
    }
*/

}
