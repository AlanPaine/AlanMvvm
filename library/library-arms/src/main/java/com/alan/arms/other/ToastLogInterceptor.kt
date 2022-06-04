package com.alan.arms.other

import com.alan.arms.action.ToastAction
import com.alan.arms.ext.isDebug
import com.hjq.toast.ToastUtils
import com.hjq.toast.config.IToastInterceptor
import com.orhanobut.logger.Logger

/**
 * 作者　: AlanPaine
 * 时间　: 2022/06/02
 * 描述　: 自定义 Toast 拦截器（用于追踪 Toast 调用的位置）
 */
class ToastLogInterceptor : IToastInterceptor {

    override fun intercept(text: CharSequence): Boolean {
        if (isDebug()) {
            // 获取调用的堆栈信息
            val stackTrace: Array<StackTraceElement> = Throwable().stackTrace
            // 跳过最前面两个堆栈
            var i = 2
            while (stackTrace.size > 2 && i < stackTrace.size) {

                // 获取代码行数
                val lineNumber: Int = stackTrace[i].lineNumber
                // 获取类的全路径
                val className: String = stackTrace[i].className
                if (((lineNumber <= 0) || className.startsWith(ToastUtils::class.java.name) ||
                            className.startsWith(ToastAction::class.java.name))) {
                    i++
                    continue
                }
                Logger.i("(%s:%s) %s", stackTrace[i].fileName, lineNumber, text.toString())
                break
                i++
            }
        }
        return false
    }
}