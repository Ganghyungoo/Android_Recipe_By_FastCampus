package com.test.recorderproject

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.time.Duration

class WaveFormView @JvmOverloads constructor(
    context:Context,
    attrs:AttributeSet? = null,
    defStyleAttr: Int = 0
): View(context,attrs,defStyleAttr) {

    val ampList = mutableListOf<Float>()
    val rectList = mutableListOf<RectF>()

    val rectWidth = 10f
    var tick = 0

    //절대 좌표 네모
    val rectF = RectF(20f, 30f ,20f + 30f, 30f + 60f)
    val redPaint = Paint().apply {
        color = Color.RED
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (rectF in rectList){
            canvas?.drawRect(rectF,redPaint)
        }
    }

    fun addAmplitude(maxAmplitude:Float) {

       val amplitude = (maxAmplitude / Short.MAX_VALUE) * this.height * 0.8f
        ampList.add(maxAmplitude)
        rectList.clear()

        val maxRect = this.width / rectWidth

        val amps = ampList.takeLast(maxRect.toInt())

        for((i,amp) in amps.withIndex()){
            val rectF = RectF()
            rectF.top = (this.height / 2) - amp / 2 -3f
            rectF.bottom = rectF.top + amp + 3f
            rectF.left = i * rectWidth
            rectF.right = rectF.left + rectWidth - 5f

            rectList.add(rectF)
        }

        invalidate()
    }

    fun replayAmplitude(duration: Int){
        rectList.clear()

        val maxRect = (this.width / rectWidth)
        val amps = ampList.take(tick).takeLast(maxRect.toInt())

        for((i,amp) in amps.withIndex()){
            val rectF = RectF()
            rectF.top = (this.height / 2) - amp / 2 -3f
            rectF.bottom = rectF.top + amp + 3f
            rectF.left = i * rectWidth
            rectF.right = rectF.left + rectWidth - 5f

            rectList.add(rectF)
        }

        tick++
        invalidate()
    }

    fun clearWave(){
        rectList.clear()
        tick = 0
        invalidate()
    }

    fun clearData(){
        ampList.clear()
    }
}