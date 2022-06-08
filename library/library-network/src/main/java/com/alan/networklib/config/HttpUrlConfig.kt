package com.alan.networklib.config

import rxhttp.wrapper.annotation.DefaultDomain
import rxhttp.wrapper.annotation.Domain

/**
 * 作者　: AlanPaine
 * 时间　: 2022/06/08
 * 描述　: URL配置地址
 */
object HttpUrlConfig {

    @JvmField
    @Domain(name = "Update", className = "Simple")
    var update = "http://183.230.190.228:10082/"

    @JvmField
    @DefaultDomain //设置为默认域名
    var baseUrl = "http://183.230.190.228:10081/"


}