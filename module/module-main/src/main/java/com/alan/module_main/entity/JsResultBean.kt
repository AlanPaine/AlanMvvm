package com.alan.module_main.entity

data class JsResultBean(
    //类型
    val type:String,
    //回调，不需要传空字符串
    val callback:String,
    //详细数据
    val data: JsResultData?
)

data class JsResultData(
    val url:String,
    //toast
    // 1:成功,2:提示,3:错误
    val alertType: String,
    //提示文案
    val message: String,
    //电话列表
    val phoneList:ArrayList<String>
)