package com.alan.arms.base.fragment

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.alan.arms.action.HandlerAction
import com.alan.arms.action.IFragmentAction
import com.alan.arms.action.KeyboardAction
import com.alan.arms.action.ToastAction
import com.alan.arms.base.viewmodel.BaseViewModel
import com.alan.arms.ext.getVmClazz

abstract class BaseFragment<VM : BaseViewModel> : Fragment(),IFragmentAction,HandlerAction,
    ToastAction, KeyboardAction {
    //是否第一次加载
    private var isFirst: Boolean = true

    lateinit var mViewModel: VM

    lateinit var mActivity: AppCompatActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFirst = true
        mViewModel = createViewModel()
        onViewCreated(savedInstanceState)
        onIntentData()
        createObserver()
        initData()
    }

    /**
     * 创建viewModel
     */
    private fun createViewModel(): VM {
        return ViewModelProvider(this).get(getVmClazz(this))
    }

    /**
     * Fragment 按键事件派发
     */
    open fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        val fragments: MutableList<Fragment?> = childFragmentManager.fragments
        for (fragment: Fragment? in fragments) {
            // 这个子 Fragment 必须是 BaseFragment 的子类，并且处于可见状态
            if (fragment !is BaseFragment<*> || fragment.getLifecycle().currentState != Lifecycle.State.RESUMED) {
                continue
            }
            // 将按键事件派发给子 Fragment 进行处理
            if (fragment.dispatchKeyEvent(event)) {
                // 如果子 Fragment 拦截了这个事件，那么就不交给父 Fragment 处理
                return true
            }
        }
        return when (event?.action) {
            KeyEvent.ACTION_DOWN -> onKeyDown(event.keyCode, event)
            KeyEvent.ACTION_UP -> onKeyUp(event.keyCode, event)
            else -> false
        }
    }

    /**
     * 按键按下事件回调
     */
    open fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // 默认不拦截按键事件
        return false
    }

    /**
     * 按键抬起事件回调
     */
    open fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        // 默认不拦截按键事件
        return false
    }


    override fun onResume() {
        super.onResume()
        onVisible()
    }
    /**
     * 是否需要懒加载
     */
    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirst) {
            // 延迟加载 防止 切换动画还没执行完毕时数据就已经加载好了，这时页面会有渲染卡顿
            getHandler().postDelayed( {
                lazyLoadData()
                isFirst = false
            },lazyLoadTime())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        removeCallbacks()
    }
}