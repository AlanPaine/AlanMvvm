package com.axber.art.ext

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.LruCache
import android.view.View
import android.widget.ScrollView
import androidx.recyclerview.widget.RecyclerView

/** 普通view 截图*/
fun View.getBitmap(): Bitmap? {
    //开启绘图缓存
    isDrawingCacheEnabled = true
    buildDrawingCache(true)
    val backache = drawingCache
    return if (backache != null) {
        val bitmap = Bitmap.createBitmap(backache)
        isDrawingCacheEnabled = false
        bitmap
    } else {
        null
    }
}


/** scrollview 截取全部的内容*/
fun ScrollView.getBitmap(): Bitmap? {
    var h = 0
    for (i in 0..childCount) {
        h += getChildAt(i)?.height ?: 0
    }
    val bitmap = Bitmap.createBitmap(width, h, Bitmap.Config.ARGB_8888)
    bitmap?.setHasAlpha(true)
    val canvas = Canvas(bitmap)
    canvas.drawColor(Color.TRANSPARENT)
    val bg = background
    bg.draw(canvas)
    bg.setBounds(0, 0, width, h)
    draw(canvas)
    return bitmap
}

/** RecyclerView获取截图*/
fun RecyclerView.getBitmap(): Bitmap? {
    if (adapter == null) {
        return null
    }
    var h = 0
    val cacheSize = Runtime.getRuntime().maxMemory() / 1024 / 8  //取1/8的最大内存作为缓冲区
    val cache = LruCache<String, Bitmap>(cacheSize.toInt())
    for (i in 0..childCount) {
        //手动调用创建和绑定ViewHolder方法，
        val holder = adapter!!.createViewHolder(this, adapter!!.getItemViewType(i))
        adapter!!.onBindViewHolder(holder, i)
        //测量
        holder.itemView.measure(
            View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        //布局
        holder.itemView.layout(0, 0, holder.itemView.measuredWidth,
            holder.itemView.measuredHeight)
        //开启绘图缓存
        holder.itemView.isDrawingCacheEnabled = true
        holder.itemView.buildDrawingCache()
        val drawingCache = holder.itemView?.drawingCache
        if (drawingCache != null) {
            cache.put(i.toString(), drawingCache)
        }
        h += holder.itemView.measuredHeight
    }
    val bitmap = Bitmap.createBitmap(width, h, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val bg = background
    bg.draw(canvas)
    bg.setBounds(0, 0, width, h)

    var currentTop = 0f
    val bitmapPaint = Paint()
    for (i in 0..cache.size()) {
        val b = cache[i.toString()]
        canvas.drawBitmap(b, 0f, currentTop, bitmapPaint)
        currentTop += b.height
    }
    return bitmap
}
