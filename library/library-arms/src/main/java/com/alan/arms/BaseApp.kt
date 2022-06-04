package com.alan.arms

import android.app.Activity
import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.alan.arms.aop.Log
import com.alan.arms.ext.isDebug
import com.alan.arms.manager.ActivityManager
import com.alan.arms.other.ToastStyle
import com.alan.arms.other.ToastLogInterceptor
import com.hjq.toast.ToastUtils
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

open class BaseApp : Application() , ViewModelStoreOwner {
    private lateinit var mAppViewModelStore: ViewModelStore

    private var mFactory: ViewModelProvider.Factory? = null

    override fun getViewModelStore(): ViewModelStore {
        return mAppViewModelStore
    }


    override fun onCreate() {
        super.onCreate()
        initSdk(this)
        mAppViewModelStore = ViewModelStore()
    }

    /**
     * 获取一个全局的ViewModel
     */
    fun getAppViewModelProvider(): ViewModelProvider {
        return ViewModelProvider(this, this.getAppFactory())
    }

    private fun getAppFactory(): ViewModelProvider.Factory {
        if (mFactory == null) {
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(this)
        }
        return mFactory as ViewModelProvider.Factory
    }

    companion object {
            /**
             * 初始化一些第三方框架
             */
            fun initSdk(application: Application) {
                // Activity 栈管理初始化
                ActivityManager.getInstance().init(application)

                // 初始化日志
                Logger.addLogAdapter( object : AndroidLogAdapter(){
                    override fun isLoggable(priority: Int, tag: String?): Boolean {
                        return isDebug()
                    }
                })
                // 初始化吐司
                ToastUtils.init(application, ToastStyle())
                // 设置调试模式
                ToastUtils.setDebugMode(isDebug())
                // 设置 Toast 拦截器
                ToastUtils.setInterceptor(ToastLogInterceptor())

                // 注册网络状态变化监听
                val connectivityManager: ConnectivityManager? = ContextCompat.getSystemService(application, ConnectivityManager::class.java)
                if (connectivityManager != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                        override fun onLost(network: Network) {
                            val topActivity: Activity? = ActivityManager.getInstance().getTopActivity()
                            if (topActivity !is LifecycleOwner) {
                                return
                            }
                            val lifecycleOwner: LifecycleOwner = topActivity
                            if (lifecycleOwner.lifecycle.currentState != Lifecycle.State.RESUMED) {
                                return
                            }
                            ToastUtils.show(R.string.common_network_error)
                        }
                    })
                }
            }
    }
}