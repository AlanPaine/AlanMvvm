package com.alan.arms.action;

import android.content.Intent;
import androidx.annotation.Nullable;

public interface StartForResult {

    void result(int code, @Nullable Intent data);
}