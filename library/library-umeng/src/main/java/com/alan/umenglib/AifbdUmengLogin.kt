package com.alan.umenglib

import com.umeng.socialize.UMAuthListener
import com.umeng.socialize.bean.SHARE_MEDIA
import java.lang.ref.SoftReference

/**
 * 作者　: AlanPaine
 * 时间　: 2022/06/08
 * desc   : 友盟第三方登录
 */
class AifbdUmengLogin {
    class LoginData internal constructor(data: Map<String, String>) {
        /** 用户 id  */
        val id: String?

        /** 昵称  */
        val name: String?

        /** 性别  */
        val sex: String?

        /** 头像  */
        val avatar: String?

        /** Token  */
        val token: String?

        /**
         * 判断当前的性别是否为男性
         */
        val isMan: Boolean
            get() = "男" == sex

        init {
            // 第三方登录获取用户资料：https://developer.umeng.com/docs/66632/detail/66639#h3-u83B7u53D6u7528u6237u8D44u6599
            id = data["uid"]
            name = data["name"]
            sex = data["gender"]
            avatar = data["iconurl"]
            token = data["accessToken"]
        }
    }

    /**
     * 为什么要用软引用，因为友盟会将监听回调（UMAuthListener）持有成静态的
     */
    class LoginListenerWrapper internal constructor(
        platform: SHARE_MEDIA?,
        listener: OnLoginListener?
    ) : SoftReference<OnLoginListener?>(listener), UMAuthListener {
        private var mPlatform: AifbdPlatform? = null

        /**
         * 授权开始的回调
         *
         * @param platform      平台名称
         */
        override fun onStart(platform: SHARE_MEDIA) {}

        /**
         * 授权成功的回调
         *
         * @param platform      平台名称
         * @param action        行为序号，开发者用不上
         * @param data          用户资料返回
         */
        override fun onComplete(
            platform: SHARE_MEDIA,
            action: Int,
            data: Map<String, String>
        ) {
            if (get() != null) {
                get()!!.onSucceed(mPlatform, LoginData(data))
            }
        }

        /**
         * 授权失败的回调
         *
         * @param platform      平台名称
         * @param action        行为序号，开发者用不上
         * @param t             错误原因
         */
        override fun onError(
            platform: SHARE_MEDIA,
            action: Int,
            t: Throwable
        ) {
            if (get() != null) {
                get()!!.onError(mPlatform, t)
            }
        }

        /**
         * 授权取消的回调
         *
         * @param platform      平台名称
         * @param action        行为序号，开发者用不上
         */
        override fun onCancel(platform: SHARE_MEDIA, action: Int) {
            if (get() != null) {
                get()!!.onCancel(mPlatform)
            }
        }

        init {
            mPlatform = when (platform) {
                SHARE_MEDIA.QQ -> AifbdPlatform.QQ
                SHARE_MEDIA.WEIXIN -> AifbdPlatform.WECHAT
                else -> throw IllegalStateException("are you ok?")
            }
        }
    }

    interface OnLoginListener {
        /**
         * 授权成功的回调
         *
         * @param platform      平台名称
         * @param data          用户资料返回
         */
        fun onSucceed(platform: AifbdPlatform?, data: LoginData?)

        /**
         * 授权失败的回调
         *
         * @param platform      平台名称
         * @param t             错误原因
         */
        fun onError(
            platform: AifbdPlatform?,
            t: Throwable?
        ) {
        }

        /**
         * 授权取消的回调
         *
         * @param platform      平台名称
         */
        fun onCancel(platform: AifbdPlatform?) {}
    }
}