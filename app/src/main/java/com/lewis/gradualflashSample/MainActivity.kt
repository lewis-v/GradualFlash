package com.lewis.gradualflashSample

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lewis.gradualflash.GradualFlash
import com.lewis.gradualflash.GradualFlash.Companion.COLOR_TYPE_FLASH
import com.lewis.gradualflash.GradualFlash.Companion.COLOR_TYPE_NORMAL
import com.lewis.gradualflash.GradualFlash.Companion.SHAPE_MODE_LINE
import com.lewis.gradualflash.GradualFlash.Companion.SHAPE_MODE_RADIAL
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //普通-线性渐变
        gf.run {
            setGradualFlash(
                GradualFlash.GFBuilder()
                    .setLowColor(Color.RED)
                    .setShapeMode(SHAPE_MODE_LINE)
                    .setHeightColor(Color.BLACK)
                    .setAnimDuration(1000)
                    .setAutoStart(true)
                    .build()
            )
        }
        //普通-半径渐变
        gf1.run {
            setGradualFlash(
                GradualFlash.GFBuilder()
                    .setLowColor(Color.RED)
                    .setShapeMode(SHAPE_MODE_RADIAL)
                    .setHeightColor(Color.BLACK)
                    .setAnimDuration(1000)
                    .setAutoStart(true)
                    .build()
            )
        }

        //颜色模式-COLOR_TYPE_NORMAL
        gf2.run {
            setGradualFlash(
                GradualFlash.GFBuilder()
                    .setLowColor(Color.RED)
                    .setShapeMode(SHAPE_MODE_LINE)
                    .setColorType(COLOR_TYPE_NORMAL)
                    .setHeightColor(Color.BLACK)
                    .setAnimDuration(1000)
                    .setAutoStart(true)
                    .build()
            )
        }

        //颜色模式-COLOR_TYPE_FLASH
        gf3.run {
            setGradualFlash(
                GradualFlash.GFBuilder()
                    .setLowColor(Color.RED)
                    .setShapeMode(SHAPE_MODE_LINE)
                    .setColorType(COLOR_TYPE_FLASH)
                    .setHeightColor(Color.BLACK)
                    .setAnimDuration(1000)
                    .setAutoStart(true)
                    .build()
            )
        }

        //无动画,实现普通的渐变效果
        gf4.run {
            setGradualFlash(
                GradualFlash.GFBuilder()
                    .setLowColor(Color.RED)
                    .setShapeMode(SHAPE_MODE_LINE)
                    .setColorType(COLOR_TYPE_FLASH)
                    .setHeightColor(Color.BLACK)
                    .setAnimDuration(1000)
                    .setAutoStart(false)
                    .build()
            )
        }

        //无动画-一半渐变
        gf5.run {
            setGradualFlash(
                GradualFlash.GFBuilder()
                    .setLowColor(Color.RED)
                    .setShapeMode(SHAPE_MODE_LINE)
                    .setColorType(COLOR_TYPE_NORMAL)
                    .setHeightColor(Color.BLACK)
                    .setAnimDuration(1000)
                    .setFlashWidthRatio(0.5f)
                    .setAnimStartXRatio(0.5f)
                    .setAutoStart(false)
                    .build()
            )
        }
        //加个边框
        gf6.run {
            setGradualFlash(
                GradualFlash.GFBuilder()
                    .setLowColor(Color.RED)
                    .setShapeMode(SHAPE_MODE_LINE)
                    .setColorType(COLOR_TYPE_NORMAL)
                    .setHeightColor(Color.BLACK)
                    .setAnimDuration(1000)
                    .setAutoStart(true)
                    .build()
            )
        }
        //边框放在容器里
        gf7.run {
            setGradualFlash(
                GradualFlash.GFBuilder()
                    .setLowColor(Color.RED)
                    .setShapeMode(SHAPE_MODE_LINE)
                    .setColorType(COLOR_TYPE_NORMAL)
                    .setHeightColor(Color.BLACK)
                    .setAnimDuration(1000)
                    .setAutoStart(true)
                    .build()
            )
        }
        //加个边框-COLOR_TYPE_FLASH
        gf8.run {
            setGradualFlash(
                GradualFlash.GFBuilder()
                    .setLowColor(Color.RED)
                    .setShapeMode(SHAPE_MODE_LINE)
                    .setColorType(COLOR_TYPE_FLASH)
                    .setHeightColor(Color.BLACK)
                    .setAnimDuration(1000)
                    .setAutoStart(true)
                    .build()
            )
        }
        //加个边框-Xfermode使用MULTIPLY
        gf9.run {
            setGradualFlash(
                GradualFlash.GFBuilder()
                    .setLowColor(Color.parseColor("#11FF0000"))
                    .setShapeMode(SHAPE_MODE_LINE)
                    .setColorType(COLOR_TYPE_NORMAL)
                    .setHeightColor(Color.BLACK)
                    .setAnimDuration(1000)
                    .setXfermode(PorterDuff.Mode.MULTIPLY)
                    .setAutoStart(true)
                    .build()
            )
        }
    }
}
