package com.rhythm.animals.night.app.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

public class ScrollableWebView extends WebView {
    private boolean isScrollable = true;

    public ScrollableWebView(Context context) {
        super(context);
    }

    public ScrollableWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollableWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return isScrollable && super.onTouchEvent(event);
            default:
                return super.onTouchEvent(event);
        }
    }
}
