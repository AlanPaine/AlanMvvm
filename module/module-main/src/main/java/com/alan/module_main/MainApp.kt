package com.alan.module_main

import com.alan.arms.BaseApp
import com.alan.commonlib.application.CommonModuleInit
import com.alan.commonlib.config.ModuleLifecycleConfig
import com.alan.module_main.application.MainModuleInit


class MainApp : BaseApp() {
    private val applicationList = arrayListOf(
        CommonModuleInit::class.java.name,
        MainModuleInit::class.java.name)
    override fun onCreate() {
        super.onCreate()
        ModuleLifecycleConfig.instance.initModuleAhead(this,applicationList)

    }
}