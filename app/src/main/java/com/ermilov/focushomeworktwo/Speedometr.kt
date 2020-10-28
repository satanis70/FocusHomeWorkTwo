package com.ermilov.focushomeworktwo

import android.content.Context
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
    init {
            LayoutInflater.from(context).inflate(R.layout.speedometr, this, true)
            attrs?.let {
                val typeArray = context.obtainStyledAttributes(
                    it, R.styleable.speedometr_attributies, 0, 0
                )
                val speedTextView = resources.getText(
                    typeArray.getResourceId(
                        R.styleable.speedometr_attributies_android_textSize,
                        R.string.app_name
                    )
                )
                textSize = typeArray.getDimensionPixelSize(
                    R.styleable.speedometr_attributies_android_textSize,
                    textSize.toInt()
                ).toFloat()
                speed_tex_view.text = speedTextView
                speed_tex_view.textSize = textSize
                speed_tex_view.gravity = Gravity.CENTER
                typeArray.recycle()

            }
        }


    fun getCurrentSpeed(): Float {
        return mCurrentSpeed
    }

    fun setCurrentSpeed(mCurrentSpeed){
        this.mCurrentSpeed = mCurrentSpeed;
    }


    override fun onSpeedChanged(newSpeedValue: Float) {

    }




}