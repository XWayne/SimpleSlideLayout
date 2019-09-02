package com.android.john.x622.simpleslidelayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class NetChildRecycleView extends RecyclerView {

    private final int mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

    private float mLastY = 0;// 记录上次Y位置

    private float mLastX = 0;

    public NetChildRecycleView(Context context) {
        super(context);
    }

    public NetChildRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NetChildRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                if ( checkParentConsume(ev) ){
                    getParent().requestDisallowInterceptTouchEvent(false);
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;

        }
        mLastY = ev.getY();
        mLastX = ev.getX();

        return super.dispatchTouchEvent(ev);
    }


    /**
     * @param motionEvent 本次点击事件
     * @return 是否应该由parent消费，默认为false
     */
    private boolean checkParentConsume(MotionEvent motionEvent){

        float nowY = motionEvent.getY();
        float nowX = motionEvent.getX();

        if (Math.abs(nowY - mLastY) > Math.abs(nowX - mLastX)){
            if (mLastY > nowY){ //即手势向上
                return !canScrollVertically(1);
//                return false;
            }else {
                return !canScrollVertically(-1);
            }
        }

        return false;
    }
}
