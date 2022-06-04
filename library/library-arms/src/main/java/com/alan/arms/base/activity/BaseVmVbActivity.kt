package com.alan.arms.base.activity

import android.view.View
import androidx.viewbinding.ViewBinding
import com.alan.arms.base.viewmodel.BaseViewModel
import com.alan.arms.ext.inflateBindingWithGeneric
/**
 * 作者　: AlanPaine
 * 时间　: 2022/06/02
 * 描述　: ViewModelFragment基类，自动把ViewModel注入Activity和 ViewBinding 注入进来了
 * 需要使用 ViewBinding 的清继承它
 */
abstract class BaseVmVbActivity<VM : BaseViewModel, VB : ViewBinding> : BaseActivity<VM>() {

    override fun getLayoutId(): Int  = 0

    lateinit var mViewBind: VB

    /**
     * 创建DataBinding
     */
    override fun initDataBind(): View? {
        mViewBind = inflateBindingWithGeneric(layoutInflater)
        return mViewBind.root

    }
}