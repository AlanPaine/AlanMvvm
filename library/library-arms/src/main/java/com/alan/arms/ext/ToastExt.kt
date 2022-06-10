package com.alan.arms.ext

import android.app.Activity
import android.content.Context
import androidx.annotation.StringRes
import com.hjq.toast.ToastUtils


//////////////////////////////消息提示//////////////////////////////////////////////////
fun showToast(msg: String) {
    ToastUtils.show(msg)
}

fun showToast(@StringRes id: Int) {
    ToastUtils.show(id)
}

fun showToast(`object`: Any?) {
    ToastUtils.show(`object`)
}

fun Context.showToast(msg: String) {
    ToastUtils.show(msg)
}

fun Context.showToast(@StringRes id: Int) {
    ToastUtils.show(id)
}

fun Context.showToast(`object`: Any?) {
    ToastUtils.show(`object`)
}

fun Activity.showToastFinish(`object`: Any?) {
    ToastUtils.show(`object`)
    finish()
}
fun Activity.showToastFinish(msg: String){
    ToastUtils.show(msg)
    finish()
}

fun Activity.showToastFinish(@StringRes id: Int){
    ToastUtils.show(id)
    finish()
}

