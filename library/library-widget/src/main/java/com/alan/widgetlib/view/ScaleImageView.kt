package com.alan.widgetlib.view

import android.content.*
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.alan.widgetlib.R

/**
 * 作者　: AlanPaine
 * 时间　: 2022/06/02
 * 描述　: 长按缩放松手恢复的 ImageView
 */
class ScaleImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    AppCompatImageView(context, attrs, defStyleAttr) {

    private var scaleSize: Float = 1.2f

    init {
        val array: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.ScaleImageView)
        setScaleSize(array.getFloat(R.styleable.ScaleImageView_scaleRatio, scaleSize))
        array.recycle()
    }

    override fun dispatchSetPressed(pressed: Boolean) {
        // 判断当前手指是否按下了
        if (pressed) {
            scaleX = scaleSize
            scaleY = scaleSize
        } else {
            scaleX = 1f
            scaleY = 1f
        }
    }

    fun setScaleSize(size: Float) {
        scaleSize = size
    }
}