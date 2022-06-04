package com.alan.arms.aop


/**
 * 作者　: AlanPaine
 * 时间　: 2022/06/02
 * 描述　: 防重复点击注解
 */
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER)
annotation class SingleClick constructor(
    /**
     * 快速点击的间隔
     */
    val value: Long = 1000
)