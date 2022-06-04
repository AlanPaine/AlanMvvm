package com.alan.arms.other

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import com.alan.arms.R
import com.hjq.toast.style.BlackToastStyle


/**
 * 作者　: AlanPaine
 * 时间　: 2022/06/02
 * 描述　: Toast 样式配置
 */
class ToastStyle : BlackToastStyle() {

    override fun getBackgroundDrawable(context: Context): Drawable {
        val drawable = GradientDrawable()
        // 设置颜色
        drawable.setColor(-0x78000000)
        // 设置圆角
        drawable.cornerRadius = context.resources.getDimension(R.dimen.button_circle_size)
        return drawable
    }

    override fun getTextSize(context: Context): Float {
        return context.resources.getDimension(R.dimen.sp_14)
    }

    override fun getHorizontalPadding(context: Context): Int {
        return context.resources.getDimension(R.dimen.sp_24).toInt()
    }

    override fun getVerticalPadding(context: Context): Int {
        return context.resources.getDimension(R.dimen.sp_16).toInt()
    }
}