package com.example.ligang.demo_autopullrefreshlistview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;

public class EditTextWithClearFinal extends EditText {

    private Drawable clearIconDrawable;
    private Rect clearIconRect;
    private boolean drawClearIcon;

    /**
     * considered disabled if android:drawableRight is not set
     */
    private boolean clearIconEnabled = true;

    public EditTextWithClearFinal(Context context) {
        super(context);
    }

    public EditTextWithClearFinal(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextWithClearFinal(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Drawable[] drawables = getCompoundDrawables();

        // pass in the clear drawable via android:drawableRight, so we don't need to define custom attrs
        clearIconDrawable = drawables[2];
        if (clearIconDrawable == null) {
            clearIconEnabled = false;
            return;
        }
        setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1],
                null, drawables[3]);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        clearIconRect = new Rect(w - clearIconDrawable.getIntrinsicWidth() - getPaddingRight(),
                h - clearIconDrawable.getIntrinsicHeight() - getPaddingBottom(),
                w,
                h);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start,
                                 int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        // drawClearIcon = !TextUtils.isEmpty(text); not good !
        // Here, editText is focused or not need to be considered, because the fist time the editText is shown,
        // this method will also get called, if it has content, the clear icon will show(event without focus) which is not we want.
        drawClearIcon = isFocused() && !TextUtils.isEmpty(text);

        // There's no need to call invalidate() here, because every time you change the text,
        // onTextChanged will be called before onDraw()
        // invalidate();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP
                && clearIconEnabled
                && drawClearIcon
                && clearIconRect.contains((int)(event.getX()), (int)(event.getY()))) {
            setText("");

            // stop paste menu from showing
            int cacheInputType = getInputType();
            setInputType(InputType.TYPE_NULL);
            super.onTouchEvent(event);
            setInputType(cacheInputType);
			return true;
        }
        return super.onTouchEvent(event);
    }

    /**
     * The most important thing is calculate dx and dy.The ordinary solution forgets to plus getScrollX/getScrollY.
     * It will cause problems when the EditText starts to scroll,
     * eg. when your characters is very long using single-line EditText or your lines it too many using multiline EditText.
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if (clearIconEnabled && drawClearIcon) {
            float dx = getWidth() + getScrollX() - clearIconDrawable.getIntrinsicWidth()
                    - getPaddingRight();
            float dy = getHeight() + getScrollY() - clearIconDrawable.getIntrinsicHeight()
                    - getPaddingBottom();
            canvas.save();
            canvas.translate(dx, dy);
            clearIconDrawable.draw(canvas);
            canvas.restore();
        }
        super.onDraw(canvas);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);

        drawClearIcon = focused && !TextUtils.isEmpty(getText());
        invalidate();
    }
}
