package com.alan.arms.base.activity

import android.view.View
import androidx.databinding.ViewDataBinding
import com.alan.arms.base.viewmodel.BaseViewModel
import com.alan.arms.ext.inflateBindingWithGeneric
/**
 * 作者　: AlanPaine
 * 时间　: 2022/06/02
 * 描述　: ViewModelFragment基类，自动把ViewModel注入Activity和Databind注入进来了
 * 需要使用Databind的清继承它
 */
abstract class BaseVmDbActivity <VM : BaseViewModel, DB : ViewDataBinding> : BaseVmActivity<VM>() {

    override fun getLayoutId(): Int  = 0

    lateinit var mDatabind: DB

    override fun initDataBind(): View? {
        mDatabind = inflateBindingWithGeneric(layoutInflater)
        return mDatabind.root
    }
}