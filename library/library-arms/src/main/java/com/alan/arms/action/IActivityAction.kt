package com.alan.arms.action

import android.os.Bundle
import android.view.View

interface IActivityAction {
    /**
     * 获取getIntent();数据
     */
    fun onIntentData() {}

    /**
     * 获取布局id
     */
    fun getLayoutId(): Int

    /**
     * 初始化view
     */
    fun onViewCreated(savedInstanceState: Bundle?)

    /**
     * 创建观察者
     */
    fun createObserver(){}

    /**
     * 初始化数据
     */
    fun initData() {}

    /**
     * 供子类BaseVmDbActivity 初始化Databinding操作
     */
     fun initDataBind(): View? {
        return null
    }

}