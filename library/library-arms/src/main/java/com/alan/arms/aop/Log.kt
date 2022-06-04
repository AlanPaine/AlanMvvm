package com.alan.arms.aop

/**
 * 作者　: AlanPaine
 * 时间　: 2022/06/02
 * 描述　: Debug 日志注解
 */
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.CONSTRUCTOR)
annotation class Log constructor(val value: String = "AppLog")