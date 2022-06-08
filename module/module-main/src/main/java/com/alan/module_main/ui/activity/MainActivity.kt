package com.alan.module_main.ui.activity

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.alan.arms.aop.Log
import com.alan.arms.base.activity.BaseVmVbActivity
import com.alan.arms.base.viewmodel.BaseViewModel
import com.alan.arms.ext.isOnDoubleClick
import com.alan.module_main.databinding.ActivityMainBinding


class MainActivity : BaseVmVbActivity<BaseViewModel,ActivityMainBinding>() {

    @Log("Text")
    override fun onViewCreated(savedInstanceState: Bundle?) {
        mViewBind.spWebView.loadUrl("https://www.baidu.com")
        mViewBind.spWebView.webViewClient = object :WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                showToast("加载完成")
            }
        }

    }

    override fun onBackPressed() {
        if (isOnDoubleClick()){
            super.onBackPressed()
        }else{
            showToast("在点一次退出")
        }
    }
}