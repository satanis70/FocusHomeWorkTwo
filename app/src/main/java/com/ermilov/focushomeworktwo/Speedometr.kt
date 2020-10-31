package com.ermilov.focushomeworktwo

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.RequiresApi
import kotlin.math.cos
import kotlin.math.round
import kotlin.math.sin


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class Speedometr @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyle, defStyleRes), SpeedChangeListener {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mCurrentSpeed = 0f
    private var text = "km/h"
    private var markRange = 10
    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.speedometr_attributies,
            defStyle,
            defStyleRes
        )

        val char = typedArray.getText(R.styleable.speedometr_attributies_android_text)
        text = char?.toString() ?: "km/h"

        markRange = typedArray.getInt(R.styleable.speedometr_attributies_markRange, 10)
        typedArray.recycle()
    }


    fun getCurrentSpeed(): Float {

        return mCurrentSpeed
    }

    fun setCurrentSpeed(mCurrentSpeed: Float) {
        when {
            mCurrentSpeed < 0 -> {
                this.mCurrentSpeed = 0f

            }
            mCurrentSpeed > 120 -> {
                this.mCurrentSpeed = 120f
            }
            else -> this.mCurrentSpeed = mCurrentSpeed
        }
    }


    override fun onSpeedChanged(newSpeedValue: Float) {
        this.setCurrentSpeed(newSpeedValue)

        this.invalidate()
    }





    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var width = width
        var height = height
        val aspect = width/height
        val normalAspect = 2f/1f
        val textPadding = 0.85f
        val longScale = 0.9f
        if (aspect>normalAspect){
            width = (normalAspect * height).toInt()
        } else if (aspect<normalAspect) {
            height = (width/normalAspect).toInt()
        }

        canvas?.save()

        canvas?.scale(.5f * width, -1f * height)
        canvas?.translate(1f, -1f)
        paint.color = Color.parseColor("#808080")
        paint.style = Paint.Style.FILL
        canvas?.drawCircle(0f, 0f, 1f, paint)

        paint.color = Color.WHITE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 0.005f

        val maxValue = 120
        val scale = 0.9f
        val step = Math.PI / maxValue
        for (i in 0..maxValue){
            val x1 = cos(Math.PI - step * i)
            val y1 = sin(Math.PI - step * i)
            val x2 : Float
            val y2 : Float
            if (i % markRange == 0){
                x2 = (x1*scale*0.9f).toFloat()
                y2 = (y1*scale*0.9f).toFloat()
            } else {
                 x2 = (x1*scale).toFloat()
                 y2 = (y1*scale).toFloat()
            }

            canvas?.drawLine(x1.toFloat(), y1.toFloat(), x2, y2, paint)
        }

        canvas?.restore()
        canvas?.save()

        canvas?.translate((width / 2).toFloat(), 0f)

        paint.textSize = (height/10).toFloat()
        paint.color = Color.LTGRAY
        paint.style = Paint.Style.FILL

        val factor: Float = height * scale * longScale * textPadding

        var i = 0
        while (i <= maxValue) {
            paint.color = Color.YELLOW
            val x = cos(Math.PI - step * i).toFloat() * factor
            val y = sin(Math.PI - step * i).toFloat() * factor
            val text = i.toString()
            val textLen = round(paint.measureText(text))
            canvas!!.drawText(i.toString(), x - textLen / 2, height - y, paint)
            i += markRange
        }


        canvas!!.drawText(text, -paint.measureText(text) / 2, height - height * 0.15f, paint)

        canvas.restore()
        canvas.save()

        canvas.translate((width / 2).toFloat(), height.toFloat())
        canvas.scale(.5f * width, -1f * height)
        canvas.rotate(90 - 180f * (getCurrentSpeed() / maxValue.toFloat()))


        paint.color = Color.RED
        drawArrow(canvas)

        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#ff8080")
        canvas.drawCircle(0f, 0f, .05f, paint)
        canvas.restore()

    }


    fun setAnimation() {
        val colorTo = Color.RED
        val colorFrom = Color.YELLOW
        val objectAnimator = ObjectAnimator.ofObject(paint, "color", ArgbEvaluator(), colorFrom, colorTo)
        objectAnimator.interpolator = LinearInterpolator()
        objectAnimator.duration = getCurrentSpeed().toLong()
        objectAnimator.addUpdateListener {
            //speedometrview.setBackgroundColor(objectAnimator.animatedValue as Int)
            paint.color = objectAnimator.animatedValue as Int
            invalidate()
        }


    }
    fun drawArrow(canvas: Canvas?){
        paint.style = Paint.Style.FILL_AND_STROKE
        setAnimation()
        paint.strokeWidth = 0.02f
        canvas?.drawLine(0.01f, 0f, 0f, 1f, paint)
        canvas?.drawLine(-0.01f, 0f, 0f, 1f, paint)

    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var width = MeasureSpec.getSize(widthMeasureSpec)

        val heigthMode = MeasureSpec.getMode(widthMeasureSpec)
        var height = MeasureSpec.getSize(widthMeasureSpec)

        val aspect = width/height
        val normalAspect = 2f/1f

        if (aspect>normalAspect){
            if (widthMode != MeasureSpec.EXACTLY){
                width = round(normalAspect * height).toInt()
            }
        } else {
            if (heigthMode != MeasureSpec.EXACTLY){
                height = round(width / normalAspect).toInt()
            }
        }

        setMeasuredDimension(width, height)
    }



}