package com.alan.module_main.ui.activity

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.lifecycleScope
import com.alan.arms.action.IAcCallBack
import com.alan.arms.base.activity.BaseVmVbActivity
import com.alan.arms.base.viewmodel.BaseViewModel
import com.alan.arms.ext.isOnDoubleClick
import com.alan.arms.ext.launchAc
import com.alan.arms.other.AcCallBackHelper
import com.alan.arms.other.LaunchUtil
import com.alan.module_main.R
import com.alan.module_main.config.AppConstants
import com.alan.module_main.databinding.ActivityMainBinding
import com.alan.module_main.entity.JsResultBean
import com.alan.module_main.other.JsBridge
import com.alan.umenglib.AifbdPlatform
import com.alan.umenglib.AifbdUmengClient
import com.alan.umenglib.AifbdUmengShare
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import rxhttp.toFlow
import rxhttp.wrapper.param.RxHttp


class MainActivity : BaseVmVbActivity<BaseViewModel,ActivityMainBinding>() {


    override fun onViewCreated(savedInstanceState: Bundle?) {

        mViewBind.spWebView.run {

            webViewClient = object :WebViewClient(){
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    //showToast("加载完成")
                    runOnUiThread {
                        mViewBind.spWebView.quickCallJs("appData", arrayOf("大家还记得和"))
                    }
                }
            }
            addJavascriptInterface(JsBridge(object : JsBridge.OnListener {
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
                                //  showToast("获取定位")

                                lifecycleScope.launch {
                                    RxHttp.postJson("http://jsonplaceholder.typicode.com/posts")
                                        .add("test","qw")
                                        .add("tests","qwdddddd")
                                        .toFlow<String>()
                                        .catch {
                                            showToast("${it}")
                                        }.collect {
                                            showToast("${it}")
                                        }
                                }
                            }
                            AppConstants.GO_TO_NEW_PAGE.tag -> {
                                //打开新界面
                                showToast("打开新界面")
                                launchAc<BrowserActivity>("param1" to "data","param2" to 123 ){
                                    result { code, result ->
                                        showToast("BrowserActivity页面传值${result?.getStringExtra("data")}")
                                    }
                                }
//                                launch<BrowserActivity>(this@MainActivity){
//                                    putExtra("param1", "data")
//                                    putExtra("param2", 123)
//                                }
                            }

                            AppConstants.SHARE.tag-> {
                                //打开新界面
                                //this@MainActivity.runOnUiThread {
                                    val shareData = AifbdUmengShare.ShareData(this@MainActivity)
                                    //shareData.setShareImage(mViewBind.spWebView.getBitmap())
                                    shareData.setShareImage(R.mipmap.banner3)
//                                    shareData.setShareLogo(R.mipmap.ic_launcher)
//                                    shareData.setShareDescription("垃圾分享")
//                                    shareData.setShareTitle("垃圾分享垃圾分享")
//                                    shareData.shareUrl = "https://www.aifbd.com"
                                    AifbdUmengClient.shareImage(this@MainActivity, AifbdPlatform.WECHAT,shareData, object :
                                        AifbdUmengShare.OnShareListener{
                                        /**
                                         * 分享成功的回调
                                         *
                                         * @param platform      平台名称
                                         */
                                        override fun onSucceed(platform: AifbdPlatform?) {

                                        }

                                    })
                                //}

                                showToast("打开分享")
                            }
                            else -> {
                                showToast(data.toString())
                            }
                        }
                        //showToast("${data?.data}")

                    }
                }
            }),"android")

            loadUrl("file:///android_asset/index.html")
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