package com.test.youtubeappproject

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout

class ExoPlayerMotionLayout
@JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : MotionLayout(context, attributeSet, defStyleAttr) {

    var targetView: View? = null

    //제스쳐 이벤트
    private val gestureDetector by lazy {
        GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(
                e1: MotionEvent,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float,
            ): Boolean {
                Log.d("testt", "현재 스크롤 작동중")
                return targetView?.containTouchArea(e1.x.toInt(), e1.y.toInt()) ?: false
            }

        })
    }

    //타치 이벤트를 가로채기 위한 메서드를 오버라이드해서 스크롤 할 때만 true값을 얻음
    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            Log.d("testt", "현재 스크롤 작동중")
            return gestureDetector.onTouchEvent(event)
        } ?: return false
    }

    private fun View.containTouchArea(x: Int, y: Int): Boolean {
        return (x in this.left..this.right && y in this.top..this.bottom)
    }
}