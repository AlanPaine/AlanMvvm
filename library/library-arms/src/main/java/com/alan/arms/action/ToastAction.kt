package com.alan.arms.action

import androidx.annotation.StringRes
import com.hjq.toast.ToastUtils


/**
 * 作者　: AlanPaine
 * 时间　: 2022/06/02
 * 描述　: 吐司意图
 */
interface ToastAction {

    fun showToast(text: CharSequence?) {
        ToastUtils.show(text)
    }

    fun showToast(@StringRes id: Int) {
        ToastUtils.show(id)
    }

    fun showToast(`object`: Any?) {
        ToastUtils.show(`object`)
    }
}