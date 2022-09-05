package com.alan.arms.other

import androidx.fragment.app.FragmentActivity
import com.alan.arms.action.IOnLoginNext
import com.alan.arms.config.LaunchAcConfig


/**
 * @Author      :zwz
 * @Date        :  2022/6/7 9:40
 * @Description : 描述
 */
object LaunchUtil {

    private var config: LaunchAcConfig? = null
    private var isLogin: (() -> Boolean)? = null
    private var startLogin: ((FragmentActivity, IOnLoginNext) -> Unit)? = null

    @JvmStatic
    fun init(
        isLogin: () -> Boolean,
        startLogin: (FragmentActivity, IOnLoginNext) -> Unit
    ) {
        init(null, isLogin, startLogin)
    }

    @JvmStatic
    fun init(
        config: LaunchAcConfig?,
        isLogin: () -> Boolean,
        startLogin: (FragmentActivity, IOnLoginNext) -> Unit
    ) {
        this.config = config
        this.isLogin = isLogin
        this.startLogin = startLogin

    }

    fun getConfig() = config

    fun getIsLogin(): Boolean {
        if (isLogin == null) return true
        return isLogin?.invoke()?:false
    }

    fun getStartLogin(ac: FragmentActivity, next: IOnLoginNext) {
        if (startLogin == null) RuntimeException("请调用 LaunchUtil.isLogin ")
        startLogin?.invoke(ac, next)
    }
}