package com.lewis.gradualflash

import android.animation.ValueAnimator
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuff.Mode
import androidx.annotation.ColorInt
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

class GradualFlash private constructor() {
    companion object {
        //普通颜色模式,直接使用设置的颜色
        const val COLOR_TYPE_NORMAL = 1

        //闪光,会自动为前后的low颜色加上透明度
        const val COLOR_TYPE_FLASH = 2

        //透明,不显示效果
        const val COLOR_TYPE_ALPHA = 3

        const val DIRECTION_LEFT_TO_RIGHT = 1;
        const val DIRECTION_TOP_BOTTOM = 2;
        const val DIRECTION_RIGHT_LEFT = 3;
        const val DIRECTION_BOTTOM_TOP = 4;

        const val SHAPE_MODE_LINE = 0
        const val SHAPE_MODE_RADIAL = 1

    }

    //首个及最后一个需要有透明度才有闪光
    @ColorInt
    private val baseColor = 0x4c000000

    @ColorInt
    private var highLightColor = Color.WHITE

    @ColorInt
    private var lowLightColor = Color.WHITE

    //颜色模式
    var colorType = COLOR_TYPE_FLASH

    //效果运动方向
    var direction = DIRECTION_LEFT_TO_RIGHT

    //重复次数
    var repeatCount = ValueAnimator.INFINITE

    //重复类型
    var repeatMode = ValueAnimator.RESTART

    //一次效果的时间
    var animationDuration = 1000L

    //旋转角度
    var rotate = 20f

    //直接设置效果的宽高
    private var realFlashWidth = 0
    private var realFlashHeight = 0

    //设置效果宽高为整体的百分比
    private var flashWidthRatio = 1f
    private var flashHeightRatio = 1f

    //特效模式
    var shapeMode = SHAPE_MODE_LINE

    //渐变的增强速率
    var intensity = 0f

    //渐变衰弱速率
    var dropoff = 0.5f

    //自动开始,开启后再恢复的时候回自动播放
    var autoStart = true

    //效果是否透明(透明会与原控件颜色融合)
    var isAlpha = true

    //颜色混溶模式
    var mXfermode: Mode = Mode.SRC_IN

    //渐变动画开始的坐标,Float.MIN_VALUE为不使用此值
    var mAnimStartX = Float.MIN_VALUE
    var mAnimStartY = Float.MIN_VALUE

    //渐变动画开始的坐标,占渐变体的百分比
    var mAnimStartXRatio = -1f
    var mAnimStartYRatio = -1f

    val colorsArray = IntArray(4)
    val positionArray = FloatArray(4)

    fun getFlashWidth(width: Int): Int {
        return if (realFlashWidth > 0) realFlashWidth else round(width * flashWidthRatio).toInt()
    }

    fun getFlashHeight(height: Int): Int {
        return if (realFlashHeight > 0) realFlashHeight else round(height * flashHeightRatio).toInt()
    }

    fun getAnimStartX(width: Int): Float {
        if (mAnimStartX != Float.MIN_VALUE) {
            return mAnimStartX
        }
        return mAnimStartXRatio * getFlashWidth(width)
    }

    fun getAnimStartY(height: Int): Float {
        if (mAnimStartY != Float.MIN_VALUE) {
            return mAnimStartY
        }
        return mAnimStartYRatio * getFlashHeight(height)
    }

    fun getXfermode() = mXfermode

    fun initColor() {
        val lowColor = if (colorType == COLOR_TYPE_FLASH) {
            baseColor or (lowLightColor and 0x00FFFFFF)
        } else {
            lowLightColor
        }

        colorsArray[1] = highLightColor
        colorsArray[3] = lowColor
        when (shapeMode) {
            SHAPE_MODE_LINE -> {
                colorsArray[0] = lowColor
                colorsArray[2] = highLightColor
            }
            SHAPE_MODE_RADIAL -> {
                colorsArray[0] = highLightColor
                colorsArray[2] = lowColor
            }
        }
    }

    fun initPosition() {
        when (shapeMode) {
            SHAPE_MODE_LINE -> {
                positionArray[0] = max((1f - intensity - dropoff) / 2f, 0f)
                positionArray[1] = max((1f - intensity - 0.001f) / 2f, 0f)
                positionArray[2] = min((1f + intensity + 0.001f) / 2f, 1f)
                positionArray[3] = min((1f + intensity + dropoff) / 2f, 1f)
            }
            SHAPE_MODE_RADIAL -> {
                positionArray[0] = 0f
                positionArray[1] = min(intensity, 1f)
                positionArray[2] = min(intensity + dropoff, 1f)
                positionArray[3] = 1f
            }
        }
    }

    class GFBuilder() {
        private val gradualFlash = GradualFlash()

        fun setColorType(type: Int): GFBuilder {
            gradualFlash.colorType = type
            return this
        }

        fun setDirection(direction: Int): GFBuilder {
            gradualFlash.direction = direction
            return this
        }

        fun setRepeatCount(count: Int): GFBuilder {
            gradualFlash.repeatCount = count
            return this
        }

        fun setRepeatMode(mode: Int): GFBuilder {
            gradualFlash.repeatMode = mode
            return this
        }

        fun setAnimDuration(duration: Long): GFBuilder {
            gradualFlash.animationDuration = duration
            return this
        }

        fun setRotate(rotate: Float): GFBuilder {
            gradualFlash.rotate = rotate
            return this
        }

        fun setHeightColor(color: Int): GFBuilder {
            gradualFlash.highLightColor = color
            return this
        }

        fun setLowColor(color: Int): GFBuilder {
            gradualFlash.lowLightColor = color
            return this
        }

        fun setShapeMode(model: Int): GFBuilder {
            gradualFlash.shapeMode = model
            return this
        }

        fun setIntensity(intensity: Float): GFBuilder {
            gradualFlash.intensity = intensity
            return this
        }

        fun setDropoff(dropoff: Float): GFBuilder {
            gradualFlash.dropoff = dropoff
            return this
        }

        fun setAutoStart(autoStart: Boolean): GFBuilder {
            gradualFlash.autoStart = autoStart
            return this
        }

        fun setIsAlpha(isAlpha: Boolean): GFBuilder {
            gradualFlash.isAlpha = isAlpha
            return this
        }

        fun setXfermode(xfermode: PorterDuff.Mode): GFBuilder {
            gradualFlash.mXfermode = xfermode
            return this
        }

        fun setFlashWidthRatio(ratio: Float): GFBuilder {
            gradualFlash.flashWidthRatio = ratio
            return this
        }

        fun setFlashHeightRatio(ratio: Float): GFBuilder {
            gradualFlash.flashHeightRatio = ratio
            return this
        }

        fun setRealFlashWidth(width: Int): GFBuilder {
            gradualFlash.realFlashWidth = width
            return this
        }

        fun setRealFlashHeight(height: Int): GFBuilder {
            gradualFlash.realFlashHeight = height
            return this
        }

        fun setAnimStartX(x: Float): GFBuilder {
            gradualFlash.mAnimStartX = x
            return this
        }

        fun setAnimStartY(y: Float): GFBuilder {
            gradualFlash.mAnimStartY = y
            return this
        }

        fun setAnimStartXRatio(ratio: Float): GFBuilder {
            gradualFlash.mAnimStartXRatio = ratio
            return this
        }

        fun setAnimStartYRatio(ratio: Float): GFBuilder {
            gradualFlash.mAnimStartYRatio = ratio
            return this
        }

        fun applyWithAttr(ta: TypedArray): GFBuilder {
            gradualFlash.lowLightColor =
                ta.getColor(R.styleable.GradualFlashLayout_gf_low_color, gradualFlash.lowLightColor)
            gradualFlash.highLightColor = ta.getColor(
                R.styleable.GradualFlashLayout_gf_height_color,
                gradualFlash.highLightColor
            )
            gradualFlash.colorType =
                ta.getInt(R.styleable.GradualFlashLayout_gf_color_type, gradualFlash.colorType)
            gradualFlash.direction =
                ta.getInt(R.styleable.GradualFlashLayout_gf_direction, gradualFlash.direction)
            gradualFlash.repeatCount =
                ta.getInt(R.styleable.GradualFlashLayout_gf_repeat_count, gradualFlash.repeatCount)
            gradualFlash.repeatMode =
                ta.getInt(R.styleable.GradualFlashLayout_gf_repeat_mode, gradualFlash.repeatMode)
            gradualFlash.animationDuration = ta.getInt(
                R.styleable.GradualFlashLayout_gf_duration,
                gradualFlash.animationDuration.toInt()
            ).toLong()
            gradualFlash.rotate =
                ta.getFloat(R.styleable.GradualFlashLayout_gf_rotate, gradualFlash.rotate)
            gradualFlash.autoStart =
                ta.getBoolean(R.styleable.GradualFlashLayout_gf_auto_start, gradualFlash.autoStart)
            gradualFlash.shapeMode =
                ta.getInt(R.styleable.GradualFlashLayout_gf_shape_mode, gradualFlash.shapeMode)
            return this
        }

        fun build(): GradualFlash {
            gradualFlash.initColor()
            gradualFlash.initPosition()
            return gradualFlash
        }
    }
}