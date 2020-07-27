package com.lewis.gradualflash

import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.Drawable
import com.lewis.gradualflash.GradualFlash.Companion.DIRECTION_BOTTOM_TOP
import com.lewis.gradualflash.GradualFlash.Companion.DIRECTION_LEFT_TO_RIGHT
import com.lewis.gradualflash.GradualFlash.Companion.DIRECTION_RIGHT_LEFT
import com.lewis.gradualflash.GradualFlash.Companion.DIRECTION_TOP_BOTTOM
import com.lewis.gradualflash.GradualFlash.Companion.SHAPE_MODE_LINE
import com.lewis.gradualflash.GradualFlash.Companion.SHAPE_MODE_RADIAL
import kotlin.math.max
import kotlin.math.sqrt


class GradualFlashDrawable : Drawable() {

    private var gradualFlash: GradualFlash? = null

    private val paint = Paint().apply {
        isAntiAlias = true
    }
    private val rect = Rect()
    private val shaderMatrix = Matrix()

    private var valueAnimator: ValueAnimator? = null

    private val animUpdateListener by lazy {
        ValueAnimator.AnimatorUpdateListener {
            invalidateSelf()
        }
    }

    fun setGradualFlash(gradualFlash: GradualFlash) {
        this.gradualFlash = gradualFlash
        updatePaint()
        updateAnim()
        invalidateSelf()
    }


    fun getGradualFlash(): GradualFlash? {
        return gradualFlash
    }

    private fun updatePaint() {
        paint.xfermode = PorterDuffXfermode(
            gradualFlash?.getXfermode()?:PorterDuff.Mode.SRC_IN
        )
        gradualFlash?.let {
            val boundsWidth = bounds.width()
            val boundsHeight = bounds.height()
            if (boundsHeight == 0 || boundsWidth == 0) return
            val width = it.getFlashWidth(boundsWidth)
            val height = it.getFlashHeight(boundsHeight)
            when (it.shapeMode) {
                SHAPE_MODE_LINE -> {
                    val isVertical =
                        it.direction == DIRECTION_BOTTOM_TOP || it.direction == DIRECTION_TOP_BOTTOM
                    paint.shader = LinearGradient(
                        0f,
                        0f,
                        if (isVertical) 0f else width.toFloat(),
                        if (isVertical) height.toFloat() else 0f,
                        it.colorsArray,
                        it.positionArray,
                        Shader.TileMode.CLAMP
                    )
                }
                SHAPE_MODE_RADIAL -> {
                    paint.shader = RadialGradient(
                        width / 2f,
                        height / 2f,
                        (max(width, height) / sqrt(2.0)).toFloat(),
                        it.colorsArray,
                        it.positionArray,
                        Shader.TileMode.CLAMP
                    )
                }
            }
        }
    }

    private fun updateAnim() {
        var oldIsStart = false
        valueAnimator?.let {
            oldIsStart = it.isStarted
            it.cancel()
            it.removeAllUpdateListeners()
        }
        gradualFlash?.let {
            valueAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
                repeatMode = it.repeatMode
                repeatCount = it.repeatCount
                duration = it.animationDuration
                addUpdateListener(animUpdateListener)
                if (oldIsStart) {
                    start()
                }
            }
        }
    }

    fun startGF() {
        valueAnimator?.let {
            if (!it.isStarted && callback != null) {
                it.start()
            }
        }
    }

    fun stopGF() {
        valueAnimator?.let {
            it.cancel()
            it.removeAllUpdateListeners()
        }
    }

    override fun draw(canvas: Canvas) {
        gradualFlash?.let {
            var translateX = 0f
            var translateY = 0f
            val maxWidth = rect.width()
            val maxHeight = rect.height()
            val animValue = valueAnimator?.animatedFraction ?: 0f
            when (it.direction) {
                DIRECTION_LEFT_TO_RIGHT -> {
                    translateX =
                        it.getAnimStartX(maxWidth) + (maxWidth + it.getFlashWidth(maxWidth)) * animValue
                    translateY = 0f
                }
                DIRECTION_RIGHT_LEFT -> {
                    translateX = maxWidth + it.getAnimStartX(maxWidth) - (maxWidth + it.getFlashWidth(maxWidth)) * animValue
                    translateY = 0f
                }
                DIRECTION_TOP_BOTTOM -> {
                    translateY =
                        it.getAnimStartY(maxHeight) + (maxHeight + it.getFlashHeight(maxHeight)) * animValue
                    translateX = 0f
                }
                DIRECTION_BOTTOM_TOP -> {
                    translateY = maxHeight + it.getAnimStartY(maxHeight) - (maxHeight + it.getFlashHeight(maxHeight)) * animValue
                    translateX = 0f
                }
            }

            shaderMatrix.reset()
            shaderMatrix.setRotate(it.rotate, rect.width() / 2f, rect.height() / 2f)
            shaderMatrix.postTranslate(translateX, translateY);
            paint.shader?.setLocalMatrix(shaderMatrix)
            canvas.drawRect(rect, paint)
        }
    }

    override fun setAlpha(alpha: Int) {
    }

    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }

    override fun onBoundsChange(bounds: Rect?) {
        super.onBoundsChange(bounds)
        bounds?.let {
            rect.set(0, 0, it.width(), it.height())
            updatePaint()
            startIfNeedOnResume()
        }
    }

    /**
     * 恢复的时候使用,如果恢复的时候需要开始动画就会开始
     * 开始条件为相关参数不为空且设置了自动播放
     */
    fun startIfNeedOnResume() {
        if (valueAnimator != null && valueAnimator?.isStarted == false
            && gradualFlash != null && gradualFlash?.autoStart == true
            && callback != null
        ) {
            valueAnimator?.start()
        }
    }

    fun isStarted(): Boolean {
        return valueAnimator?.isStarted?:false
    }
}
