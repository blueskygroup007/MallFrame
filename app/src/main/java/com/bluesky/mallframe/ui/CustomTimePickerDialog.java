package com.bluesky.mallframe.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

/**
 * @author BlueSky
 * @date 2020/6/1
 * Description:
 */
public class CustomTimePickerDialog extends AlertDialog {

    protected CustomTimePickerDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CustomTimePickerDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
