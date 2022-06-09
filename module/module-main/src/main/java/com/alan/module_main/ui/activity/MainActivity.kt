package com.alan.module_main.ui.activity

import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import com.alan.arms.aop.Log
import com.alan.arms.base.activity.BaseVmVbActivity
import com.alan.arms.base.viewmodel.BaseViewModel
import com.alan.arms.ext.isOnDoubleClick
import com.alan.commonlib.other.GsonUtils
import com.alan.module_main.config.AppConstants
import com.alan.module_main.databinding.ActivityMainBinding
import com.alan.module_main.entity.JsResultBean


class MainActivity : BaseVmVbActivity<BaseViewModel,ActivityMainBinding>() {

    @Log("Text")
    override fun onViewCreated(savedInstanceState: Bundle?) {
        mViewBind.spWebView.loadUrl("file:///android_asset/index.html")
        mViewBind.spWebView.addJavascriptInterface(JsBridge(object : JsBridge.OnListener {
            override fun goToAction(data: JsResultBean?) {
                runOnUiThread {
                    when (data?.type) {
                        AppConstants.BACK.tag -> {
                            //返回
                            this@MainActivity.finish()
                        }
                        AppConstants.TOAST.tag -> {
                            //toast
                            showToast("${data.data?.message}")
                        }
                        AppConstants.CALL_PHONE.tag -> {
                            //打电话
                            showToast("${data.data?.phoneList?.toString()}")
                        }
                        AppConstants.GET_LOCATION.tag -> {
                            //获取定位
                            showToast("获取定位")
                        }
                        AppConstants.GO_TO_NEW_PAGE.tag -> {
                            //打开新界面
                            showToast("打开新界面")
                        }
                        else -> {
                            showToast(data.toString())
                        }
                    }
                    //showToast("${data?.data}")
                   // mViewBind.spWebView.quickCallJs(data?.callback+"", arrayOf("大家还记得和"))
                }
            }
        }),"android")

        mViewBind.spWebView.webViewClient = object :WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                showToast("加载完成")
            }
        }

//        lifecycleScope.launch {
//            RxHttp.get("")
//                .add("test","qw")
//                .toFlow<String>()
//                .catch {
//
//                }.collect {
//
//                }
//        }
    }

    override fun onBackPressed() {
        if (isOnDoubleClick()){
            super.onBackPressed()
        }else{
            showToast("在点一次退出")
        }
    }


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
}