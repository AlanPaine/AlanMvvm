package com.alan.arms.ext

import android.os.Build

fun isAndroidS() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

fun isAndroidR() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R

fun isAndroidQ() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

fun isAndroidP() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

fun isAndroidO() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O