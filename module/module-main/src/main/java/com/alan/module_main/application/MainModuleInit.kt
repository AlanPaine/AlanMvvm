package com.alan.module_main.application

import com.alan.arms.BaseApp
import com.alan.arms.other.LaunchUtil
import com.alan.commonlib.application.IModuleInit
import com.alan.umenglib.AifbdUmengClient
import com.orhanobut.logger.Logger

class MainModuleInit : IModuleInit {
    override fun onInitAhead(application: BaseApp?): Boolean {
        Logger.i("main组件初始化完成 -- onInitAhead")
        application?.let { AifbdUmengClient.init(it) }
//        LaunchUtil.init({
//            LoginActivity.isLogin
//        }, { ac, login ->
        //未登录条件触发   ac为发起的acitivity的回调 可用来启动LoginActivity,login 调用 login.isLogin(boolean//传入是否登录成功)//        })
        return false
    }

    override fun onInitLow(application: BaseApp?): Boolean {
        return false
    }
}