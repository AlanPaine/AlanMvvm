package com.alan.commonlib.application

import com.alan.arms.BaseApp


/**
 * 项目名称：
 * 类名称：IModuleInit.kt
 * 类描述：动态配置组件Application,有需要初始化的组件实现该接口,统一在宿主app 的Application进行初始化
 * 作者：AlanPaine
 * 创建时间： 2022/3/2-10:37
 * 邮箱：AlanPaine@163.COM
 * 修改简介：
 */
interface  IModuleInit {
    /** 需要优先初始化的  */
    fun onInitAhead(application: BaseApp?): Boolean

    /** 可以后初始化的  */
    fun onInitLow(application: BaseApp?): Boolean
}