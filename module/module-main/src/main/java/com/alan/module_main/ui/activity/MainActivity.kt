package com.alan.module_main.ui.activity

import android.content.Context
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.alan.arms.aop.Log
import com.alan.arms.base.activity.BaseVmVbActivity
import com.alan.arms.base.viewmodel.BaseViewModel
import com.alan.arms.ext.isOnDoubleClick
import com.alan.module_main.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import rxhttp.asFlow
import rxhttp.toClass
import rxhttp.toFlow
import rxhttp.wrapper.param.RxHttp


class MainActivity : BaseVmVbActivity<BaseViewModel,ActivityMainBinding>() {

    @Log("Text")
    override fun onViewCreated(savedInstanceState: Bundle?) {
        mViewBind.spWebView.loadUrl("file:///android_asset/index.html")
        mViewBind.spWebView.addJavascriptInterface(JsInteraction(this),"android")
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


    class JsInteraction(context: Context) {
        private var mContext: Context?= context

        @JavascriptInterface
        fun action(result:String){
//            showToast(result)
            Toast.makeText(mContext,result+"",Toast.LENGTH_LONG).show()
        }
    }
}