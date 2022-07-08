package com.alan.arms.ext

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import kotlin.math.roundToInt
import android.renderscript.Element
/**
 * 模糊效果
 * @param context
 * @param radius
 * @param scale
 */
fun Bitmap.blur(context: Context, radius: Float, scale: Float): Bitmap {
    val width = (width * scale).roundToInt()
    val height = (height * scale).roundToInt()
    return Bitmap.createScaledBitmap(this, width, height, false).apply {
        val renderScript = RenderScript.create(context)
        val input = Allocation.createFromBitmap(renderScript, this)
        val output = Allocation.createTyped(renderScript, input.type)
        ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript)).apply {
            setInput(input)
            setRadius(radius)
            forEach(output)
        }
        output.copyTo(this)
        renderScript.destroy()
    }
}

/**
 * 合成更新的Icon
 *
 * @param drawable
 * @return
 */
fun Drawable.toBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(
        intrinsicWidth,
        intrinsicHeight,
        if (opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
    )
    val canvas = Canvas(bitmap)
    setBounds(0, 0, intrinsicWidth, intrinsicHeight)
    draw(canvas)
    return bitmap
}