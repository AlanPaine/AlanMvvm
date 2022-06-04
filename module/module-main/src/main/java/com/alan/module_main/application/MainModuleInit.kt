package com.alan.module_main.application

import com.alan.arms.BaseApp
import com.alan.commonlib.application.IModuleInit
import com.orhanobut.logger.Logger

class MainModuleInit : IModuleInit {
    override fun onInitAhead(application: BaseApp?): Boolean {
        Logger.i("main组件初始化完成 -- onInitAhead")

        return false
    }

    override fun onInitLow(application: BaseApp?): Boolean {
        return false
    }
}