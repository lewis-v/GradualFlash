package com.lewis.gradualflash

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

class GradualFlashLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val gfDrawable = GradualFlashDrawable()
    private val paint = Paint()

    private var mShowFG = true

    init {
        gfDrawable.callback = this
        setWillNotDraw(false)
        setLayerType(View.LAYER_TYPE_HARDWARE, paint)
        attrs?.let {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.GradualFlashLayout, 0, 0);
            try {
                setGradualFlash(GradualFlash.GFBuilder().applyWithAttr(ta).build())
            } finally {
                ta.recycle()
            }

        }
        if (gfDrawable.getGradualFlash() == null) {
            setGradualFlash(GradualFlash.GFBuilder().build())
        }
    }

    fun setGradualFlash(gf: GradualFlash) {
        gfDrawable.setGradualFlash(gf)
    }

    fun isStartGFAnim(): Boolean {
        return gfDrawable.isStarted()
    }

    fun startGFAnim() {
        gfDrawable.startGF()
    }

    fun stopGFAnim() {
        gfDrawable.stopGF()
    }

    fun isShowGF(): Boolean {
        return mShowFG
    }

    fun showGF() {
        mShowFG = true
        invalidate()
    }

    fun hideGF() {
        mShowFG = false
        invalidate()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        gfDrawable.setBounds(0, 0, width, height)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        if (mShowFG) {
            canvas?.let {
                gfDrawable.draw(canvas)
            }
        }
    }

    override fun verifyDrawable(who: Drawable): Boolean {
        return super.verifyDrawable(who) || who == gfDrawable
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        gfDrawable.startIfNeedOnResume()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        gfDrawable.stopGF()
    }
}