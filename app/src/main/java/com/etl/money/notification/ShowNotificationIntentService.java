package com.etl.money.notification;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.etl.money.R;

public class ShowNotificationIntentService extends IntentService {
    private static final String ACTION_SHOW_NOTIFICATION = "my.app.service.action.show";
    private static final String ACTION_HIDE_NOTIFICATION = "my.app.service.action.hide";


    public ShowNotificationIntentService() {
        super("ShowNotificationIntentService");
    }

    public static void startActionShow(Context context) {
        Intent intent = new Intent(context, ShowNotificationIntentService.class);
        intent.setAction(ACTION_SHOW_NOTIFICATION);
        context.startService(intent);
    }

    public static void startActionHide(Context context) {
        Intent intent = new Intent(context, ShowNotificationIntentService.class);
        intent.setAction(ACTION_HIDE_NOTIFICATION);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SHOW_NOTIFICATION.equals(action)) {
                handleActionShow();
            } else if (ACTION_HIDE_NOTIFICATION.equals(action)) {
                handleActionHide();
            }
        }
    }

    private void handleActionShow() {
        showStatusBarIcon(ShowNotificationIntentService.this);
    }

    private void handleActionHide() {
       // hideStatusBarIcon(ShowNotificationIntentService.this);
    }

    public static void showStatusBarIcon(Context ctx) {
        Context context = ctx;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx)
                .setContentTitle(ctx.getString(R.string.str_date))
                .setSmallIcon(R.drawable.ic_notification)
                .setOngoing(true);
//        Intent intent = new Intent(context, MainActivity.class);
//        PendingIntent pIntent = PendingIntent.getActivity(context, STATUS_ICON_REQUEST_CODE, intent, 0);
//        builder.setContentIntent(pIntent);
//        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification notif = builder.build();
//        notif.flags |= Notification.FLAG_ONGOING_EVENT;
//        mNotificationManager.notify(STATUS_ICON_REQUEST_CODE, notif);
    }
}