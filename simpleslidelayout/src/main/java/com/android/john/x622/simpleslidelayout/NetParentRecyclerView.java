package com.android.john.x622.simpleslidelayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class NetParentRecyclerView  extends RecyclerView {

    private final int mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

    private float mLastY = 0;// 记录上次Y位置

    private float mLastX = 0;

    public NetParentRecyclerView(Context context) {
        super(context);
    }

    public NetParentRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NetParentRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {

        boolean intercept = super.onInterceptTouchEvent(e);

        if (e.getAction() == MotionEvent.ACTION_DOWN){

            return false;
        }

        return intercept;
    }
}

