package com.etl.money.notification;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.etl.money.global.GlabaleParameter;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class NotificationOpened implements OneSignal.NotificationOpenedHandler {
    NotificationSQLiteAdapter helper;
    private Application application;

    public NotificationOpened(Application application) {
        this.application = application;
    }

    @Override
    public void notificationOpened(OSNotificationOpenResult result) {



        String message=result.notification.payload.body!=null?result.notification.payload.body:"";
        String title=result.notification.payload.title!=null?result.notification.payload.title:"";

        helper = new NotificationSQLiteAdapter(application);

        String currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
        add(title,message,currentDate);
//
        SharedPreferences.Editor editor = application.getSharedPreferences(GlabaleParameter.PREFS_NOTIFICATION, Context.MODE_PRIVATE).edit();
        editor.putString(GlabaleParameter.PREFS_NOTIFICATION_TITLE, title);
        editor.putString(GlabaleParameter.PREFS_NOTIFICATION_DESCRIPTION, message);
        editor.putString(GlabaleParameter.PREFS_NOTIFICATION_DATETIME, currentDate);
        editor.commit();

        // Get custom datas from notification
         JSONObject data = result.notification.payload.additionalData;
        if (data != null) {
            String myCustomData = data.optString("key", null);
        }

        // React to button pressed
        OSNotificationAction.ActionType actionType = result.action.type;
        if (actionType == OSNotificationAction.ActionType.ActionTaken)
            Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);
        // Launch new activity using Application object
        startApp();
    }
    private void add(String strTitle , String strDescription,String currentDate )
    {

        if(strTitle.isEmpty() || strDescription.isEmpty())
        {
         //   NotificationMessage.notificationMessage(getApplicationContext(),"Enter Both Name and Password");
        }
        else
        {
            long id = helper.insertData(strTitle,strDescription,currentDate);
            if(id<=0)
            {
            //    NotificationMessage.notificationMessage(getApplicationContext(),"Insertion Unsuccessful");

            } else
            {
            //    NotificationMessage.notificationMessage(getApplicationContext(),"Insertion Successful");

            }
        }
    }

    private void startApp() {
      //  Log.i("OneSignalExample", "Button pressed with id:555 " );
        Intent intent = new Intent(application, NotificationDetailActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        application.startActivity(intent);
    }
}