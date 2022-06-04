package com.alan.commonlib.application

import com.alan.arms.BaseApp


class CommonModuleInit : IModuleInit {
    override fun onInitAhead(application: BaseApp?): Boolean {

        return false
    }

    override fun onInitLow(application: BaseApp?): Boolean {
        return false
    }
}