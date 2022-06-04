package com.alan.arms.aop

import com.orhanobut.logger.Logger
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.CodeSignature


/**
 * 作者　: AlanPaine
 * 时间　: 2022/06/02
 * 描述　: 防重复点击切面
 */
@Suppress("unused")
@Aspect
class SingleClickAspect {

    /** 最近一次点击的时间 */
    private var lastTime: Long = 0

    /** 最近一次点击的标记 */
    private var lastTag: String? = null

    /**
     * 方法切入点
     */
    @Pointcut("execution(@com.alan.arms.aop.SingleClick * *(..))")
    fun method() {}

    /**
     * 在连接点进行方法替换
     */
    @Around("method() && @annotation(singleClick)")
    @Throws(Throwable::class)
    fun aroundJoinPoint(joinPoint: ProceedingJoinPoint, singleClick: SingleClick) {
        val codeSignature: CodeSignature = joinPoint.signature as CodeSignature
        // 方法所在类
        val className: String = codeSignature.declaringType.name
        // 方法名
        val methodName: String = codeSignature.name
        // 构建方法 TAG
        val builder: StringBuilder = StringBuilder("$className.$methodName")
        builder.append("(")
        val parameterValues: Array<Any?> = joinPoint.args
        for (i in parameterValues.indices) {
            val arg: Any? = parameterValues[i]
            if (i == 0) {
                builder.append(arg)
            } else {
                builder.append(", ")
                    .append(arg)
            }
        }
        builder.append(")")
        val tag: String = builder.toString()
        val currentTimeMillis: Long = System.currentTimeMillis()
        if (currentTimeMillis - lastTime < singleClick.value && (tag == lastTag)) {
            Logger.i("%s 毫秒内发生快速点击：%s", singleClick.value, tag)
            return
        }
        lastTime = currentTimeMillis
        lastTag = tag
        // 执行原方法
        joinPoint.proceed()
    }
}