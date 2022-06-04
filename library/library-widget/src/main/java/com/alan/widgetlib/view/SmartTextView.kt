package com.alan.widgetlib.view

import android.content.*
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView


/**
 * 作者　: AlanPaine
 * 时间　: 2022/06/02
 * 描述　: 自动显示和隐藏的 TextView
 */
class SmartTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle) :
    AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        refreshVisibilityStatus()
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
        refreshVisibilityStatus()
    }

    override fun setCompoundDrawables(left: Drawable?, top: Drawable?, right: Drawable?, bottom: Drawable?) {
        super.setCompoundDrawables(left, top, right, bottom)
        refreshVisibilityStatus()
    }

    override fun setCompoundDrawablesRelative(start: Drawable?, top: Drawable?, end: Drawable?, bottom: Drawable?) {
        super.setCompoundDrawablesRelative(start, top, end, bottom)
        refreshVisibilityStatus()
    }

    /**
     * 刷新当前可见状态
     */
    private fun refreshVisibilityStatus() {
        // 判断当前有没有设置文本达到自动隐藏和显示的效果
        if (isEmptyContent() && visibility != GONE) {
            visibility = GONE
            return
        }
        if (visibility != VISIBLE) {
            visibility = VISIBLE
        }
    }

    /**
     * TextView 内容是否为空
     */
    private fun isEmptyContent(): Boolean {
        if (!TextUtils.isEmpty(text)) {
            return false
        }
        val compoundDrawables: Array<Drawable?> = compoundDrawables
        val compoundDrawablesRelative: Array<Drawable?> = compoundDrawablesRelative
        for (drawable: Drawable? in compoundDrawables) {
            if (drawable != null) {
                return false
            }
        }
        for (drawable: Drawable? in compoundDrawablesRelative) {
            if (drawable != null) {
                return false
            }
        }
        return true
    }
}