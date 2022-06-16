package com.alan.umenglib

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import com.umeng.socialize.PlatformConfig
import com.umeng.socialize.ShareAction
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.media.UMImage

/**
 * 作者　: AlanPaine
 * 时间　: 2022/06/08
 * desc   : 友盟客户端
 */
object AifbdUmengClient {
    /**
     * 初始化友盟相关 SDK
     */
    fun init(application: Application) {
        try {
            val metaData = application.packageManager.getApplicationInfo(
                application.packageName,
                PackageManager.GET_META_DATA
            ).metaData
            UMConfigure.preInit(application, metaData["UMENG_APPKEY"].toString(),"umeng")
            UMConfigure.submitPolicyGrantResult(application, true)
            // 友盟统计，API 说明：https://developer.umeng.com/docs/66632/detail/101814#h1-u521Du59CBu5316u53CAu901Au7528u63A5u53E32
            UMConfigure.init(
                application,
                metaData["UMENG_APPKEY"].toString(),
                "umeng",
                UMConfigure.DEVICE_TYPE_PHONE,
                ""
            )
            // 选用自动采集模式：https://developer.umeng.com/docs/119267/detail/118588#h1-u9875u9762u91C7u96C63
            MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)
            // 初始化各个平台的 Key
            PlatformConfig.setWeixin(
                metaData["WX_APPID"].toString(),
                metaData["WX_APPKEY"].toString()
            )
            // Andriod 11必须要调用PlatformConfig.setWXFileProvider、PlatformConfig.setQQFileProvider和PlatformConfig.setDingFileProvider等等，否则分享本地图片到微信、QQ、微博和钉钉等平台会失败
            PlatformConfig.setWXFileProvider("${application.packageName}.fileprovider");
            PlatformConfig.setQQZone(
                metaData["QQ_APPID"].toString(),
                metaData["QQ_APPKEY"].toString()
            )
            PlatformConfig.setQQFileProvider("${application.packageName}.fileprovider");
            PlatformConfig.setSinaWeibo(
                metaData.get("SN_APPID").toString(),
                metaData.get("SN_APPKEY").toString(),
                "http://sns.whalecloud.com")
            PlatformConfig.setSinaFileProvider("${application.packageName}.fileprovider");
            // 豆瓣RENREN平台目前只能在服务器端配置
            //PlatformConfig.setYixin("yxc0614e80c9304c11b0391514d09f13bf");
            //PlatformConfig.setTwitter("3aIN7fuF685MuZ7jtXkQxalyi", "MK6FEYG63eWcpDFgRYw4w9puJhzDl0tyuqWjZ3M7XJuuG7mMbO");
            //PlatformConfig.setAlipay("2015111700822536");
            //PlatformConfig.setLaiwang("laiwangd497e70d4", "d497e70d4c3e4efeab1381476bac4c5e");
            //PlatformConfig.setPinterest("1439206");
            //PlatformConfig.setKakao("e4f60e065048eb031e235c806b31c70f");
            //PlatformConfig.setDing("dingoalmlnohc0wggfedpk");
            //PlatformConfig.setVKontakte("5764965","5My6SNliAaLxEm3Lyd9J");
            //PlatformConfig.setDropbox("oz8v5apet3arcdy","h7p2pjbzkkxt02a");
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    /**
     * 分享
     *
     * @param activity              Activity对象
     * @param platform              分享平台
     * @param data                  分享内容
     * @param listener              分享监听
     */
    fun share(
        activity: Activity,
        platform: AifbdPlatform,
        data: AifbdUmengShare.ShareData,
        listener: AifbdUmengShare.OnShareListener?
    ) {
        if (isAppInstalled(activity, platform.packageName)) {
                ShareAction(activity)
                    .setPlatform(platform.thirdParty)
                    .withMedia(data.create())
                    .setCallback(
                        if (listener != null) AifbdUmengShare.ShareListenerWrapper(
                            platform.thirdParty,
                            listener
                        ) else null
                    )
                    .share()
        } else {
            // 当分享的平台软件可能没有被安装的时候
            listener?.onError(platform, PackageManager.NameNotFoundException("Is not installed"))
        }
    }

    fun shareImage(
        activity: Activity,
        platform: AifbdPlatform,
        data: AifbdUmengShare.ShareData,
        listener: AifbdUmengShare.OnShareListener?
    ) {
        if (isAppInstalled(activity, platform.packageName)) {
                ShareAction(activity)
                    .setPlatform(platform.thirdParty)
                    .withMedia(data.createImage())
                    .setCallback(
                        if (listener != null) AifbdUmengShare.ShareListenerWrapper(
                            platform.thirdParty,
                            listener
                        ) else null
                    )
                    .share()
        } else {
            // 当分享的平台软件可能没有被安装的时候
            listener?.onError(platform, PackageManager.NameNotFoundException("Is not installed"))
        }
    }

    /**
     * 登录
     *
     * @param activity              Activity对象
     * @param platform              登录平台
     * @param listener              登录监听
     */
    fun login(
        activity: Activity,
        platform: AifbdPlatform,
        listener: AifbdUmengLogin.OnLoginListener?
    ) {
        if (isAppInstalled(activity, platform)) {
            try {
                // 删除旧的第三方登录授权
                UMShareAPI.get(activity).deleteOauth(activity, platform.thirdParty, null)
                // 要先等上面的代码执行完毕之后
                Thread.sleep(200)
                // 开启新的第三方登录授权
                UMShareAPI.get(activity).getPlatformInfo(
                    activity,
                    platform.thirdParty,
                    if (listener != null) AifbdUmengLogin.LoginListenerWrapper(
                        platform.thirdParty,
                        listener
                    ) else null
                )
            } catch (ignored: InterruptedException) {
            }
        } else {
            // 当登录的平台软件可能没有被安装的时候
            listener?.onError(platform, PackageManager.NameNotFoundException("Is not installed"))
        }
    }
    /**
     * 设置回调
     */
    fun onActivityResult(
        activity: Activity?,
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        UMShareAPI.get(activity).onActivityResult(requestCode, resultCode, data)
    }

    /**
     * 判断 App 是否安装
     */
    fun isAppInstalled(
        context: Context,
        platform: AifbdPlatform
    ): Boolean {
        return isAppInstalled(context, platform.packageName)
    }

    private fun isAppInstalled(
        context: Context,
        packageName: String
    ): Boolean {
        return try {
            context.packageManager.getApplicationInfo(packageName, 0)
            true
        } catch (ignored: PackageManager.NameNotFoundException) {
            false
        }
    }
}