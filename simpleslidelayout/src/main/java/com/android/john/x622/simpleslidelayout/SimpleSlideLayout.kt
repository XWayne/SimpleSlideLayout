package com.android.john.x622.simpleslidelayout

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout
import androidx.customview.widget.ViewDragHelper
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class SimpleSlideLayout :FrameLayout{

    //可见“手柄”高度
    private var handlerHeight = context.dp2px(50f)

    //内容view
    private val contentView:View by lazy{
        getChildAt(0)
    }

    //认为是“fling”的最小速度的值
    private val flingVelocity by lazy{
        ViewConfiguration.get(context).scaledMinimumFlingVelocity
    }

    private val viewDragHelper:ViewDragHelper by lazy {
        ViewDragHelper.create(this,1f,object :ViewDragHelper.Callback(){
            override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                return child == contentView
            }

            override fun onEdgeDragStarted(edgeFlags: Int, pointerId: Int) {
                super.onEdgeDragStarted(edgeFlags, pointerId)
                viewDragHelper.captureChildView(contentView,pointerId)
            }

            override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
                return max(min(top,height - handlerHeight.toInt()),height - contentView.height)
            }

            override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
                super.onViewReleased(releasedChild, xvel, yvel)

                if (abs(yvel)>=flingVelocity){
                    if (yvel>0) toBottom() else toTop()
                }else if (releasedChild.y.toInt() !in height - releasedChild.height .. (releasedChild.height- handlerHeight.toInt())/2){
                    toBottom()
                }else toTop()

            }
        })
    }

    constructor(context:Context):this(context,null)
    constructor(context:Context,attrs:AttributeSet?):super(context, attrs)

    init {
        //TODO 添加自定义属性

        setBackgroundColor( Color.TRANSPARENT )
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount > 1 )
            throw IllegalArgumentException("子view最多只能一个")
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        contentView.layout(0
            ,height - min(handlerHeight.toInt(),contentView.height)
            ,contentView.width
            ,height+contentView.height - min(handlerHeight.toInt(),contentView.height)
        )

    }

    override fun computeScroll() {
        super.computeScroll()
        if (viewDragHelper.continueSettling(true))
            invalidate()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (event!!.action == MotionEvent.ACTION_DOWN){
            if (event.y.toInt() in 0..contentView.y.toInt()){
                return false  //不消耗其他的点击事件
            }
        }

        viewDragHelper.processTouchEvent(event)
        return true
    }

    //解决recyclerView的滑动冲突
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        val event = ev!!
        val inBound = event.x.toInt() in 0..contentView.width
                && event.y.toInt() in contentView.y.toInt()..contentView.y.toInt()+handlerHeight.toInt()

        val intercept = if (inBound){
//            viewDragHelper.processTouchEvent(event)
            true
        }else{
            viewDragHelper.shouldInterceptTouchEvent(event)
        }

        viewDragHelper.processTouchEvent(event) //当子view不再消耗事件时，接管剩余的触摸事件
        return intercept
    }

    private fun scrollToExact(finalTop:Int){
        viewDragHelper.settleCapturedViewAt(0,finalTop)
        invalidate()
    }
    private fun toTop() = scrollToExact( height - contentView.height)

    private fun toBottom() = scrollToExact( height - handlerHeight.toInt())
}