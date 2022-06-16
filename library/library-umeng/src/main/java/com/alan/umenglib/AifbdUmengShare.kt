package com.alan.umenglib

import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.media.UMImage
import com.umeng.socialize.media.UMWeb
import java.lang.ref.SoftReference

/**
 * 作者　: AlanPaine
 * 时间　: 2022/06/08
 * desc   : 友盟第三方分享
 */
class AifbdUmengShare {
    class ShareData(
        /** 上下文对象  */
        private val mContext: Context
    ) {

        /** 分享标题  */
        private var mShareTitle: String? = null

        /** 分享 URL  */
        var shareUrl: String? = null

        /** 分享描述  */
        private var mShareDescription: String? = null

        /** 分享缩略图  */
        private var mShareLogo: UMImage? = null


        fun setShareTitle(title: String?) {
            mShareTitle = title
        }

        fun setShareDescription(description: String?) {
            mShareDescription = description
        }

        fun setShareLogo(logo: String?) {
            mShareLogo = UMImage(mContext, logo)
        }

        fun setShareLogo(@DrawableRes id: Int) {
            mShareLogo = UMImage(mContext, id)
        }
        fun setShareImage(@DrawableRes id: Int) {
            mShareLogo = UMImage(mContext, id)

        }
        fun setShareImage(img: String?) {
            mShareLogo = UMImage(mContext, img)
        }
        fun setShareImage(bitmap: Bitmap?) {
            mShareLogo = UMImage(mContext,bitmap)
        }
        fun create(): UMWeb {
            val content = UMWeb(shareUrl)
            content.title = mShareTitle
            if (mShareLogo != null) {
                content.setThumb(mShareLogo)
            }
            content.description = mShareDescription
            return content
        }

        fun createImage(): UMImage? {
            mShareLogo?.isLoadImgByCompress = true
            mShareLogo?.setThumb(mShareLogo)
            mShareLogo?.compressStyle =UMImage.CompressStyle.SCALE//大小压缩，默认为大小压缩，适合普通很大的图
            mShareLogo?.compressFormat = Bitmap.CompressFormat.PNG//用户分享透明背景的图片可以设置这种方式，但是qq好友，微信朋友圈，不支持透明背景图片，会变成黑色
            return mShareLogo
        }

        fun getShareTitle(): String? {
            return mShareTitle
        }
        fun getShareDescription(): String? {
            return mShareDescription
        }

    }

    /**
     * 为什么要用软引用，因为友盟会将监听回调（UMShareListener）持有成静态的
     */
    class ShareListenerWrapper internal constructor(
        platform: SHARE_MEDIA?,
        listener: OnShareListener?
    ) : SoftReference<OnShareListener?>(listener), UMShareListener {
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
         */
        override fun onResult(platform: SHARE_MEDIA) {
            if (get() != null) {
                get()?.onSucceed(mPlatform)
            }
        }

        /**
         * 授权失败的回调
         *
         * @param platform      平台名称
         * @param t             错误原因
         */
        override fun onError(platform: SHARE_MEDIA, t: Throwable) {
            if (get() != null) {
                get()?.onError(mPlatform, t)
            }
        }

        /**
         * 授权取消的回调
         *
         * @param platform      平台名称
         */
        override fun onCancel(platform: SHARE_MEDIA) {
            if (get() != null) {
                get()?.onCancel(mPlatform)
            }
        }

        init {
            mPlatform = when (platform) {
                SHARE_MEDIA.QQ -> AifbdPlatform.QQ
                SHARE_MEDIA.QZONE -> AifbdPlatform.QZONE
                SHARE_MEDIA.WEIXIN -> AifbdPlatform.WECHAT
                SHARE_MEDIA.WEIXIN_CIRCLE -> AifbdPlatform.CIRCLE
                else -> throw IllegalStateException("are you ok?")
            }
        }
    }

    interface OnShareListener {
        /**
         * 分享成功的回调
         *
         * @param platform      平台名称
         */
       open fun onSucceed(platform: AifbdPlatform?)

        /**
         * 分享失败的回调
         *
         * @param platform      平台名称
         * @param t             错误原因
         */
        open fun onError(
            platform: AifbdPlatform?,
            t: Throwable?
        ) {
        }

        /**
         * 分享取消的回调
         *
         * @param platform      平台名称
         */
        open  fun onCancel(platform: AifbdPlatform?) {}
    }
}