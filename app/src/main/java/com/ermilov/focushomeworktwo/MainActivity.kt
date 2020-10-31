package com.ermilov.focushomeworktwo

import android.graphics.Paint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onStart() {
        super.onStart()
        button_increase.setOnClickListener {
            speedometrview.onSpeedChanged(speedometrview.getCurrentSpeed()+10)
            speedometrview.setAnimation()
        }
        button_decrease.setOnClickListener {
            speedometrview.onSpeedChanged(speedometrview.getCurrentSpeed()-10)
        }
    }
}