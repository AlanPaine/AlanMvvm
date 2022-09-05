package com.alan.arms.other

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.savedstate.SavedStateRegistryOwner
import com.alan.arms.action.IAcCallBack
import com.alan.arms.action.StartForResult
import java.util.*
import java.util.concurrent.LinkedBlockingDeque
import kotlin.collections.LinkedHashMap

class AcCallBackHelper : IAcCallBack {

    private lateinit var startActivityResultDeque: LinkedBlockingDeque<StartForResult>
    private lateinit var activityForResult: ActivityResultLauncher<Intent>

    private var msaContext: FragmentActivity? = null

    companion object {
        private const val SAVE_STATE_KEY = "mas_save_state"
        private const val SAVE_STATE_BUNDLE_KEY = "mas_bundle_tag"
        private val msaResultSaveState =
            LinkedHashMap<String, LinkedBlockingDeque<StartForResult>>()

    }

    private val activityResultCallback = object : ActivityResultCallback<ActivityResult> {
        override fun onActivityResult(result: ActivityResult?) {
            result ?: return
            startActivityResultDeque.pollFirst()?.result(result.resultCode, result.data)
        }
    }


    /**
     * {@inheritDoc}
     */
    override fun <Host : LifecycleOwner> Host.initAcCallBackHelper() {
        when (this) {
            is FragmentActivity -> {
                msaContext = this
                startActivityResultDeque = bindHostSaveState() ?: LinkedBlockingDeque()
                activityForResult = registerForActivityResult(
                    ActivityResultContracts.StartActivityForResult(),
                    activityResultCallback
                )
            }
            is Fragment -> {
                msaContext = activity
                startActivityResultDeque = bindHostSaveState() ?: LinkedBlockingDeque()
                activityForResult = registerForActivityResult(
                    ActivityResultContracts.StartActivityForResult(),
                    activityResultCallback
                )
            }
        }
    }

    override fun getAcCallContext() = msaContext

    private fun SavedStateRegistryOwner.bindHostSaveState(): LinkedBlockingDeque<StartForResult>? {
        val saveStateKey = savedStateRegistry.consumeRestoredStateForKey(SAVE_STATE_KEY)
            ?.getString(SAVE_STATE_BUNDLE_KEY) ?: UUID.randomUUID().toString()

        savedStateRegistry.registerSavedStateProvider(SAVE_STATE_KEY) {
            Bundle().apply {
                if (::startActivityResultDeque.isInitialized) {
                    msaResultSaveState[saveStateKey] = startActivityResultDeque
                    putString(SAVE_STATE_BUNDLE_KEY, saveStateKey)
                }
            }
        }

        return msaResultSaveState.remove(saveStateKey)
    }

    override fun getResultDeque() = startActivityResultDeque

    override fun getResultLauncher() = activityForResult

}

