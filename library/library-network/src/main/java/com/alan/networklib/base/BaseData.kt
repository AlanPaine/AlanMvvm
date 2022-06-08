package com.alan.networklib.base
/**
 * 作者　: AlanPaine
 * 时间　: 2022/06/08
 * 描述　: BaseData 接口状态泛型构造方法 可根据自己实际场景修改
 */
class BaseData<T> (
    var code:Int,
    var message: String,
    var data: T?
)