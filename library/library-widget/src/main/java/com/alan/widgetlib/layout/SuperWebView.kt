package com.alan.widgetlib.layout

import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.webkit.ValueCallback
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.annotation.RequiresApi
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class SuperWebView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) :
    WebView(context, attrs, defStyleAttr, defStyleRes), QuickCallJs {

    init {
        val settings = settings
        // 允许文件访问
        settings.allowFileAccess = true
        // 允许网页定位
        settings.setGeolocationEnabled(true)
        // 允许保存密码
        //settings.setSavePassword(true);
        // 开启 JavaScript
        settings.javaScriptEnabled = true
        // 允许网页弹对话框
        settings.javaScriptCanOpenWindowsAutomatically = true
        // 加快网页加载完成的速度，等页面完成再加载图片
        settings.loadsImagesAutomatically = true
        // 本地 DOM 存储（解决加载某些网页出现白板现象）
        settings.domStorageEnabled = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 解决 Android 5.0 上 WebView 默认不允许加载 Http 与 Https 混合内容
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        // 不显示滚动条
        isVerticalScrollBarEnabled = false
        isHorizontalScrollBarEnabled = false
    }


    /**
     * 获取当前的 url
     *
     * @return      返回原始的 url，因为有些url是被WebView解码过的
     */
    override fun getUrl(): String? {
        val originalUrl = super.getOriginalUrl()

        // 避免开始时同时加载两个地址而导致的崩溃
        return originalUrl ?: super.getUrl()
    }

    /**
     * 有参调用并返回调用结果
     */
    override fun quickCallJs(
        method: String?,
        callback: ValueCallback<String?>?,
        params: Array<out String?>
    ) {
        val sb = java.lang.StringBuilder()
        sb.append("javascript:$method")
        if (params.isEmpty()) {
            sb.append("()")
        } else {
            sb.append("(").append(concat(*params)).append(")")
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            evaluateJavascript(sb.toString()) { value -> callback?.onReceiveValue(value) }
        }else{
            loadUrl(sb.toString())
        }
    }

    /**
     * 有参调用
     */
    override fun quickCallJs(method: String?, params: Array<out String?>) {
        this.quickCallJs(method,null,params)
    }

    /**
     * 无参数调用
     */
    override fun quickCallJs(method: String?) {
        this.quickCallJs(method, arrayOf())
    }

    /**
     * 合并多个字符串
     */
    private fun concat(vararg params: String?): String {
        val mStringBuilder = StringBuilder()
        for (i in params.indices) {
            val param = params[i]
            if (!isJson(param)) {
                mStringBuilder.append("\"").append(param).append("\"")
            } else {
                mStringBuilder.append(param)
            }
            if (i != params.size - 1) {
                mStringBuilder.append(" , ")
            }
        }
        return mStringBuilder.toString()
    }

    /**
     * 判断是不是json
     */
    private fun isJson(target: String?): Boolean {
        if (TextUtils.isEmpty(target)) {
            return false
        }
        val tag: Boolean = try {
            if (target?.startsWith("[") == true) {
                JSONArray(target)
            } else {
                JSONObject(target)
            }
            true
        } catch (ignore: JSONException) {
            //            ignore.printStackTrace();
            false
        }
        return tag
    }

}
interface QuickCallJs{
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun quickCallJs(method: String?, callback: ValueCallback<String?>?, params: Array<out String?>)

    fun quickCallJs(method: String?,  params: Array<out String?>)
    fun quickCallJs(method: String?)
}