package com.alan.arms.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.alan.arms.base.viewmodel.BaseViewModel
import com.alan.arms.ext.inflateBindingWithGeneric
/**
 * 作者　: AlanPaine
 * 时间　: 2022/06/02
 * 描述　: ViewModelFragment基类，自动把ViewModel注入Fragment和Databind注入进来了
 * 需要使用Databind的清继承它
 */
abstract class BaseVmDbFragment<VM : BaseViewModel, DB : ViewDataBinding> : BaseFragment<VM>() {

    override fun getLayoutId(): Int  = 0

    //该类绑定的ViewDataBinding
    private var _binding: DB? = null
    open val mDatabind: DB get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding  = inflateBindingWithGeneric(inflater,container,false)
        return mDatabind.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}