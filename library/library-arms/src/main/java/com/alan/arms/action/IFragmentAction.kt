package com.alan.arms.action

import android.os.Bundle

interface IFragmentAction {
    /**
     * 获取getIntent();数据
     */
    fun onIntentData() {

    }
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
     * 懒加载
     */
    fun lazyLoadData(){}

    /**
     * 延迟加载 防止 切换动画还没执行完毕时数据就已经加载好了，这时页面会有渲染卡顿  bug
     * 这里传入你想要延迟的时间，延迟时间可以设置比转场动画时间长一点 单位： 毫秒
     * 不传默认 300毫秒
     * @return Long
     */
    fun lazyLoadTime(): Long {
        return 300
    }

}