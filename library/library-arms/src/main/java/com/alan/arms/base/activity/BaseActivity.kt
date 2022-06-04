package com.alan.arms.base.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.alan.arms.action.*
import com.alan.arms.base.fragment.BaseFragment
import com.alan.arms.base.viewmodel.BaseViewModel
import com.alan.arms.ext.getVmClazz

abstract class BaseActivity <VM : BaseViewModel>  : AppCompatActivity(),IActivityAction,ActivityAction,ToastAction,
    HandlerAction,KeyboardAction {

    lateinit var mViewModel: VM
    /**
     * 是否需要使用DataBinding 供子类BaseVmDbActivity修改，用户请慎动
     */
    private var isUserDb = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLayout()
        onIntentData()//获取数据
        onViewCreated(savedInstanceState)//界面创建完成
        createObserver()//创建观察者
        initData()//初始化数据
    }
    /**
     * 初始化布局
     */
    protected open fun initLayout() {
        mViewModel = createViewModel()
        if (!isUserDb) {
            setContentView(getLayoutId())
        } else {
            initDataBind()
        }
        initSoftKeyboard()
    }
    /**
     * 创建viewModel
     */
    private fun createViewModel(): VM {
        return ViewModelProvider(this)[getVmClazz(this)]
    }
    /**
     * 和 setContentView 对应的方法
     */
    private fun getContentView(): ViewGroup? {
        return findViewById(Window.ID_ANDROID_CONTENT)
    }
    /**
     * 初始化软键盘
     */
    private fun initSoftKeyboard() {
        // 点击外部隐藏软键盘，提升用户体验
        getContentView()?.setOnClickListener {
            // 隐藏软键，避免内存泄漏
            hideKeyboard(currentFocus)
        }
    }

    /**
     * 处理事件分发机制
     */
    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        val fragments: MutableList<Fragment?> = supportFragmentManager.fragments
        for (fragment: Fragment? in fragments) {
            // 这个 Fragment 必须是 BaseFragment 的子类，并且处于可见状态
            if (fragment !is BaseFragment<*> || fragment.getLifecycle().currentState != Lifecycle.State.RESUMED) {
                continue
            }
            // 将按键事件派发给 Fragment 进行处理
            if (fragment.dispatchKeyEvent(event)) {
                // 如果 Fragment 拦截了这个事件，那么就不交给 Activity 处理
                return true
            }
        }
        return super.dispatchKeyEvent(event)
    }

    /**
     * 获取 Context 对象
     */
    override fun getContext(): Context {
        return this
    }

    override fun startActivity(intent: Intent) {
        return super<AppCompatActivity>.startActivity(intent)
    }

    /**
     * 如果当前的 Activity（singleTop 启动模式） 被复用时会回调
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        // 设置为当前的 Intent，避免 Activity 被杀死后重启 Intent 还是最原先的那个
        setIntent(intent)
    }

    override fun finish() {
        super.finish()
        // 隐藏软键，避免内存泄漏
        hideKeyboard(currentFocus)
    }

    override fun onDestroy() {
        super.onDestroy()
        removeCallbacks()
    }
}