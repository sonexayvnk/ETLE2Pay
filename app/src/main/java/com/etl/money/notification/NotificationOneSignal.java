package com.etl.money.notification;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.etl.money.global.GlabaleParameter;
import com.onesignal.OneSignal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
public class NotificationOneSignal extends Application {
    private Object Deprecated;
    @Override
    public void onCreate() {
        super.onCreate();
     //   ======== EX I
        // Logging set to help debug issues, remove before releasing your app.
 //       OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
//        // OneSignal Initialization
//        OneSignal.startInit(this)
//                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
//                .unsubscribeWhenNotificationsAreDisabled(true)
//                .init();
        //   ======== EX II
       // OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
//        OneSignal.startInit(this)
//                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
//                .setNotificationOpenedHandler(new NotificationOpened(this))
//                .unsubscribeWhenNotificationsAreDisabled(true)
//                .init();

        //   ======== EX III

        // OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
     //   OneSignal.setInFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification);
        OneSignal.startInit(this)

                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .setNotificationOpenedHandler(new NotificationOpened(this))
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

//                 OneSignal.startInit(this)
//                .setColor(getResources().getColor(R.color.color_white))
//                .setContentTitle(getString(R.string.str_confirm_password))
//                .setContentText(String.format(getString(R.string.str_confirm), "viewObject.getTitle()"))
//                .setDefaults(NotificationCompat.DEFAULT_ALL)
//                .setStyle("bigText")
//                .setPriority(NotificationCompat.PRIORITY_HIGH) // or NotificationCompat.PRIORITY_MAX



        String currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
        SharedPreferences.Editor editor = getSharedPreferences(GlabaleParameter.PREFS_NOTIFICATION, Context.MODE_PRIVATE).edit();
        editor.putString(GlabaleParameter.PREFS_NOTIFICATION_TITLE, currentDate);
        editor.commit();

        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {

                if (userId != null){
                    //  session=new PlayerIdsession(context);
                    //  session.savePlayerId(userId);
                    Log.d("debug", "PlayerId----:" + userId);
                }

           /* if (registrationId != null){
                Log.d("debug", "registrationId:" + registrationId);
        }*/

            }
        });
    }





}