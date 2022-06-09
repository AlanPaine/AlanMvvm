package com.alan.module_main.config

enum class AppConstants(var tag:String) {
    /**
     * 关闭网页
     */
    BACK("back"),
    /**
     * toast提示
     */
    TOAST("toast"),
    /**
     * 打电话
     */
    CALL_PHONE("call"),

    /**
     * 获取经纬度
     */
    GET_LOCATION("getLocation"),
    /**
     * 跳转新的Web页面
     */
    GO_TO_NEW_PAGE("gotoNewPage")
}