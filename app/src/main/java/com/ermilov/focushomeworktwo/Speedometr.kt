package com.ermilov.focushomeworktwo

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.speedometr.view.*


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class Speedometr @JvmOverloads constructor(

    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes), SpeedChangeListener {

    private var textSize: Float = 30f
    private var mCurrentSpeed = 0f
    private var onMarkPaint: Paint? = null
    private var offMarkPaint: Paint? = null
    private var scalePaint: Paint? = null
    private var readingPaint: Paint? = null
    private var onPath: Path? = null
    private var offPath: Path? = null
    val oval = RectF()

    private var ON_COLOR: Int = android.graphics.Color.argb(255, 0xff, 0xA5, 0x00)
    private var OFF_COLOR: Int = android.graphics.Color.argb(255, 0x3e, 0x3e, 0x3e)
    private var SCALE_COLOR: Int = android.graphics.Color.argb(255, 255, 255, 255)
    private val SCALE_SIZE = 14f
    private val READING_SIZE = 60f

    private var centerX = 0f
    private var centerY = 0f
    private val radius = 0f

    init {
            LayoutInflater.from(context).inflate(R.layout.speedometr, this, true)
            attrs?.let {
                val typeArray = context.obtainStyledAttributes(
                    it, R.styleable.speedometr_attributies, 0, 0
                )

                ON_COLOR = typeArray.getColor(
                    R.styleable.speedometr_attributies_onColor, ON_COLOR)
                OFF_COLOR = typeArray.getColor(
                    R.styleable.speedometr_attributies_offColor,
                    OFF_COLOR
                )
                SCALE_COLOR = typeArray.getColor(
                    R.styleable.speedometr_attributies_scaleColor,
                    SCALE_COLOR
                )




                textSize = typeArray.getDimensionPixelSize(
                    R.styleable.speedometr_attributies_android_textSize,
                    textSize.toInt()
                ).toFloat()

                speed_tex_view.textSize = textSize
                speed_tex_view.gravity = Gravity.CENTER
                typeArray.recycle()
                initDrawingTools()

            }
        }

    private fun initDrawingTools() {
        onMarkPaint = Paint()
        onMarkPaint?.setStyle(Paint.Style.STROKE)
        onMarkPaint?.setColor(ON_COLOR)
        onMarkPaint?.setStrokeWidth(35f)
        onMarkPaint?.setShadowLayer(5f, 0f, 0f, ON_COLOR)
        onMarkPaint?.setAntiAlias(true)
        offMarkPaint = Paint(onMarkPaint)
        offMarkPaint?.setColor(OFF_COLOR)
        offMarkPaint?.setStyle(Paint.Style.FILL_AND_STROKE)
        offMarkPaint?.setShadowLayer(0f, 0f, 0f, OFF_COLOR)
        scalePaint = Paint(offMarkPaint)
        scalePaint?.setStrokeWidth(2f)
        scalePaint?.setTextSize(SCALE_SIZE)
        scalePaint?.setShadowLayer(5f, 0f, 0f, Color.RED)
        scalePaint?.setColor(SCALE_COLOR)
        readingPaint = Paint(scalePaint)
        readingPaint?.setStyle(Paint.Style.FILL_AND_STROKE)
        offMarkPaint?.setShadowLayer(3f, 0f, 0f, Color.WHITE)
        readingPaint?.setTextSize(65f)
        readingPaint?.setTypeface(Typeface.SANS_SERIF)
        readingPaint?.setColor(Color.WHITE)
        onPath = Path()
        offPath = Path()
    }



    fun getCurrentSpeed(): Float {
        return mCurrentSpeed
    }

    fun setCurrentSpeed(mCurrentSpeed: Float) {
        when {
            mCurrentSpeed < 0 -> {
                this.mCurrentSpeed = 0f
            }
            mCurrentSpeed > 280 -> {
                this.mCurrentSpeed = 280f
            }
            else -> this.mCurrentSpeed = mCurrentSpeed
        }
    }


    override fun onSpeedChanged(newSpeedValue: Float) {
        this.setCurrentSpeed(newSpeedValue)
        speed_tex_view.text = getCurrentSpeed().toString()
    }

    override fun onDraw(canvas: Canvas?) {
        drawScaleBackGround(canvas)
        drawScale(canvas)
    }

    fun drawScaleBackGround(canvas: Canvas?){
        offPath?.reset();
            var i = -180
            while (i < 0) {
                offPath?.addArc(oval, i.toFloat(), 2f)
                i += 4
            }
            canvas?.drawPath(offPath!!, offMarkPaint!!)
    }

    private fun drawScale(canvas: Canvas?) {
        onPath?.reset()
        var i = -180
        while (i < mCurrentSpeed / 280 * 180 - 180) {
            onPath!!.addArc(oval, i.toFloat(), 2f)
            i += 4
        }
        canvas?.drawPath(onPath!!, onMarkPaint!!)
    }
}