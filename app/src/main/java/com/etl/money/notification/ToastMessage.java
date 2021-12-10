package com.etl.money.notification;

import android.content.Context;
import android.widget.Toast;

public class ToastMessage {
    public static void notificationMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}