package com.alan.arms.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.alan.arms.action.IAcCallBack
import com.alan.arms.action.IOnLoginNext
import com.alan.arms.config.LaunchAcConfig
import com.alan.arms.other.LaunchUtil
import com.alan.arms.other.set


inline fun <reified AC : FragmentActivity> FragmentActivity.launchAc(
    vararg parameter: Pair<String, Any?>,
    noinline builder: (LaunchAcConfig.Builder.() -> Unit)? = null
) {

    val javaClass: Class<*> = AC::class.java

    var create: LaunchAcConfig? = null

    if (builder != null) {
        create = LaunchAcConfig.create(builder)
    }

    insideStartUpActivityWithLoginForResult(javaClass, this, create, parameter = parameter)
}


inline fun <reified AC : FragmentActivity> Fragment.launchAc(
    vararg parameter: Pair<String, Any?>,
    noinline builder: (LaunchAcConfig.Builder.() -> Unit)? = null
) {

    val javaClass: Class<*> = AC::class.java

    var create: LaunchAcConfig? = null

    if (builder != null) {
        create = LaunchAcConfig.create(builder)
    }

    insideStartUpActivityWithLoginForResult(javaClass, this, create, parameter = parameter)

}

inline fun <reified AC : FragmentActivity> Context.launchAc(
    vararg parameter: Pair<String, Any?>,
    noinline builder: (LaunchAcConfig.Builder.() -> Unit)? = null
) {

    val javaClass: Class<*> = AC::class.java

    var create: LaunchAcConfig? = null

    if (builder != null) {
        create = LaunchAcConfig.create(builder)
    }

    insideStartUpActivityWithLoginForResult(javaClass, this, create, parameter = parameter)

}


//外部就不要调佣这个了,除非没有符合条件的
fun insideStartUpActivityWithLoginForResult(
    javaClass: Class<*>,
    any: Any,
    config: LaunchAcConfig?,
    vararg parameter: Pair<String, Any?>,
) {

    val mConfig: LaunchAcConfig? = config ?: LaunchUtil.getConfig()

    val withLogin = mConfig?.withLogin

    val isLogin = LaunchUtil.getIsLogin()

    val stop = mConfig?.condition?.jump(isLogin) == false

    if (stop) {
        return
    }

    if (withLogin == true) {
        if (isLogin) {
            _jump(any, javaClass, config, parameter = parameter)
        } else {
            val ac: FragmentActivity =
                when (any) {
                    is FragmentActivity -> {
                        any
                    }
                    is Fragment -> {
                        any.requireActivity()
                    }
                    is IAcCallBack -> {
                        any.getAcCallContext()!!
                    }
                    else -> throw RuntimeException("")
                }

            LaunchUtil.getStartLogin(ac, object : IOnLoginNext {
                override fun isLogin(login: Boolean) {
                    _jump(any, javaClass, config, parameter = parameter)
                }
            })
        }

    } else {
        _jump(any, javaClass, config, parameter = parameter)
    }


}

private fun _jump(
    any: Any,
    javaClass: Class<*>,
    config: LaunchAcConfig?,
    vararg parameter: Pair<String, Any?>
) {
    when (any) {
        is IAcCallBack -> {
            any._jump(javaClass, config, parameter = parameter)
        }
        is Fragment -> {
            any._jump(javaClass, config, parameter = parameter)
        }
        is FragmentActivity -> {
            any._jump(javaClass, config, parameter = parameter)
        }
    }

}

private fun Fragment._jump(
    javaClass: Class<*>,
    config: LaunchAcConfig?,
    vararg parameter: Pair<String, Any?>,
) {

    requireActivity()._jump(javaClass, config, parameter = parameter)

}

private fun FragmentActivity._jump(
    javaClass: Class<*>,
    config: LaunchAcConfig?,
    vararg parameter: Pair<String, Any?>,
) {

    startActivity(Intent(this, javaClass).also { intent ->
        intent.add(*parameter)
        config?.intent?.invoke(intent)
    })
}

private fun IAcCallBack._jump(
    javaClass: Class<*>,
    config: LaunchAcConfig?,
    vararg parameter: Pair<String, Any?>,
) {

    if (config?.result != null) {
        getResultDeque().offerFirst(config.result)
        getAcCallContext()?.let {
            getResultLauncher().launch(Intent(it, javaClass).also { intent ->
                intent.add(*parameter)
                config.intent?.invoke(intent)
            })
        }
    } else {
        getAcCallContext()?.let {
            it._jump(javaClass, config, parameter = parameter)
        }
    }
}


private fun Intent.add(vararg parameter: Pair<String, Any?>) = apply {
    parameter.forEach {
        this[it.first] = it.second
    }
}

fun Fragment.setBack(
    vararg parameter: Pair<String, Any?>,
    resultCode: Int = Activity.RESULT_OK,
    finish: Boolean = true,
) {
    requireActivity().setBack(*parameter, resultCode = resultCode, finish = finish)
}

fun Activity.setBack(
    vararg parameter: Pair<String, Any?>,
    resultCode: Int = Activity.RESULT_OK,
    finish: Boolean = true,
) {
    setResult(resultCode, Intent().also {
        it.add(*parameter)
    })

    if (finish) finish()
}

fun Context?.setBack(
    vararg parameter: Pair<String, Any?>,
    resultCode: Int = Activity.RESULT_OK,
    finish: Boolean = true,
) {
    (this as? FragmentActivity)?.setBack(*parameter, resultCode = resultCode, finish = finish)
}