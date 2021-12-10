package com.etl.money.notification;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.etl.money.R;
import com.etl.money.global.GlabaleParameter;

public class NotificationDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);

        SharedPreferences pref = getApplicationContext().getSharedPreferences(GlabaleParameter.PREFS_NOTIFICATION, MODE_PRIVATE);
        String strTitle    = pref.getString(GlabaleParameter.PREFS_NOTIFICATION_TITLE, "");
        String strDescription    = pref.getString(GlabaleParameter.PREFS_NOTIFICATION_DESCRIPTION, "");
        String strDateTime    = pref.getString(GlabaleParameter.PREFS_NOTIFICATION_DATETIME, "");

        TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
        TextView txtDescription = (TextView) findViewById(R.id.txtDescription);
        TextView txtDateTime = (TextView) findViewById(R.id.txtDateTime);

        txtTitle.setText(strTitle);
        txtDescription.setText(strDescription);
        txtDateTime.setText(getString(R.string.str_date) + ": " + strDateTime);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_icon_close, menu);
        try {
            getSupportActionBar().setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.actionbar_layout_title_center);
            TextView mytext = (TextView) findViewById(R.id.txtcenter);
            mytext.setText(R.string.str_notification_detail);
        } catch (Exception e) {
        }
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_exit:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}