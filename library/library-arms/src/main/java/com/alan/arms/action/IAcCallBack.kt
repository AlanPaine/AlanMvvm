package com.alan.arms.action

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.LinkedBlockingDeque

interface IAcCallBack {
    fun getResultDeque(): LinkedBlockingDeque<StartForResult>

    fun getResultLauncher(): ActivityResultLauncher<Intent>

    fun <Host : LifecycleOwner> Host.initAcCallBackHelper()

    fun getAcCallContext(): FragmentActivity?
}