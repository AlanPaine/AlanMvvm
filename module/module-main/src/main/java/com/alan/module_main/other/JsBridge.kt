package com.alan.module_main.other

import android.webkit.JavascriptInterface
import com.alan.commonlib.other.GsonUtils
import com.alan.module_main.entity.JsResultBean

open class JsBridge(listener: OnListener) {
        private var mListener:OnListener?= listener
        interface OnListener{
            fun goToAction(data: JsResultBean?)
        }

        @JavascriptInterface
        fun action(result:String){
            val data = GsonUtils.fromJson(result, JsResultBean::class.java)
            mListener?.goToAction(data)
        }
    }