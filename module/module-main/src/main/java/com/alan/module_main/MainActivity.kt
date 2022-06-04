package com.alan.module_main

import android.os.Bundle
import com.alan.arms.aop.Log
import com.alan.arms.base.activity.BaseActivity
import com.alan.arms.base.viewmodel.BaseViewModel
import com.alan.arms.ext.isOnDoubleClick


class MainActivity : BaseActivity<BaseViewModel>() {
    /**
     * 获取布局id
     */
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }
    @Log("pcz=================")
    override fun onViewCreated(savedInstanceState: Bundle?) {
        showToast("测试")
    }

    override fun onBackPressed() {
        if (isOnDoubleClick()){
            super.onBackPressed()
        }else{
            showToast("在点一次退出")
            // Toast.makeText(this,"在点一次退出",Toast.LENGTH_LONG).show()
        }
    }
}