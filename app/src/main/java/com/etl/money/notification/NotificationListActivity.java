package com.etl.money.notification;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.etl.money.LoginActivity;
import com.etl.money.R;
import com.etl.money.config.AutoLogout;
import com.etl.money.global.GlabaleParameter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NotificationListActivity extends AppCompatActivity {
    Context ctx;
    NotificationSQLiteAdapter helper;
    ListView listView;
    private Application application;

    EditText txtTitle,txtDescription,txtDatetime, txtStatus;


   //ArrayList<DataModel> dataModelArrayList;
    ArrayList<NotificationListData> dataModelArrayList;
    private NotificationListAdapter dataAdapter;
 //   ArrayList<DataModel> dataModelArrayList;
 //   private DataAdapter dataAdapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        recyclerView = findViewById(R.id.recycler);
        helper = new NotificationSQLiteAdapter(this);
       // GetNotification();
        viewdata();

//
//        dataModelArrayList = new ArrayList<>();
//
//
//        DataModel playerModel = new DataModel();
//
//
//        playerModel.setData_price("strprice");
//        playerModel.setPackage_more_detail("strDial");
//        dataModelArrayList.add(playerModel);
//
//
//        DataModel playerModel2 = new DataModel();
//
//
//        playerModel2.setData_price("strprice");
//        playerModel2.setPackage_more_detail("strDial");
//        dataModelArrayList.add(playerModel2);
//    setupRecycler();
    }
    private void GetNotification(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences(GlabaleParameter.PREFS_NOTIFICATION, MODE_PRIVATE);
        String strTitle    = pref.getString(GlabaleParameter.PREFS_NOTIFICATION_TITLE, "");
        String strDescription    = pref.getString(GlabaleParameter.PREFS_NOTIFICATION_DESCRIPTION, "");
      //  add(strTitle,strDescription);
    }

    private void LoadRecyclerView(String strData) {

        try {

            dataModelArrayList = new ArrayList<>();
            String[] arry = strData.split("\\|");
            for (String str : arry) {

              String[] arryB = str.split("~");
                Log.e("strData:", ""+arryB[0]);
              //  NotificationListData ListData = new NotificationListData();
                NotificationListData ListData = new NotificationListData();
//                ArrayList<DataModel> dataModelArrayList;
//                private DataAdapter dataAdapter;
             //   ListData.setDescription(arryB[0]);

                ListData.setIds(arryB[0]);
                ListData.setTitle((arryB[1]));
                ListData.setDescription((arryB[2]));
                ListData.setDatetime((arryB[3]));
                ListData.setStatus((arryB[4]));
                dataModelArrayList.add(ListData);
            }

            setupRecycler();

        } catch (Exception e) {
        }

    }
    public void ShowDetail(final String strData) {
        String[] arry = strData.split("~");

//        SharedPreferences pref = getApplicationContext().getSharedPreferences(GlabaleParameter.PREFS_NOTIFICATION, MODE_PRIVATE);
//        String strTitle    = pref.getString(GlabaleParameter.PREFS_NOTIFICATION_TITLE, "");
//        String strDescription    = pref.getString(GlabaleParameter.PREFS_NOTIFICATION_DESCRIPTION, "");




        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(GlabaleParameter.PREFS_NOTIFICATION, Context.MODE_PRIVATE).edit();
        editor.putString(GlabaleParameter.PREFS_NOTIFICATION_TITLE, arry[1]);
        editor.putString(GlabaleParameter.PREFS_NOTIFICATION_DESCRIPTION, arry[2]);
        editor.putString(GlabaleParameter.PREFS_NOTIFICATION_DATETIME, arry[3]);
        editor.commit();

        updateStatus(arry[0] , "1");
        Intent refresh1 = new Intent(getApplicationContext(), NotificationDetailActivity.class);
        startActivity(refresh1);
    }

    private void setupRecycler() {
        dataAdapter = new NotificationListAdapter(this, dataModelArrayList);
        recyclerView.setAdapter(dataAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
    }
    private void viewdata()
    {
        String data = helper.getData();
     Log.e("Decrptdata:", ""+data);
      //  LoadRecyclerView(data);
       // NotificationMessage.notificationMessage(this,data);
    }
    private void updateStatus(String strIds , String strStatus)
    {
        if(strIds.isEmpty() || strStatus.isEmpty())
        {
            ToastMessage.notificationMessage(getApplicationContext(),"Enter Data");
        }
        else
        {
            int a= helper.updateStatus( strIds, strStatus);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_icon_close, menu);
        try {
            getSupportActionBar().setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.actionbar_layout_title_center);
            TextView mytext = (TextView) findViewById(R.id.txtcenter);
            mytext.setText(R.string.str_notification);
        } catch (Exception e) {
        }
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_exit:

                //  ((WalletActivity)ctx).BuyPackaget("strData");
                Intent mIntent = new Intent();
                mIntent.putExtra("keyName", "notification");
                setResult(RESULT_OK, mIntent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //  ((WalletActivity)ctx).BuyPackaget("strData");
            Intent mIntent = new Intent();
            mIntent.putExtra("keyName", "notification");
            setResult(RESULT_OK, mIntent);
            finish();
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onResume() {
        super.onResume();
       /* SharedPreferences.Editor editor = getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, Context.MODE_PRIVATE).edit();
        editor.putString(GlabaleParameter.PREFS_RESUM_CHECK_BALANCE_AFTER_CHARGE, "1" );
        editor.apply();*/
        //===Auto Logout
        SharedPreferences pref = getApplicationContext().getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, MODE_PRIVATE);
        String strFromDate = pref.getString(GlabaleParameter.PREFS_RESUM_LAST_DATE, "");
        int Countdow  = Integer.parseInt(pref.getString(GlabaleParameter.PREFS_ETL_AUT_LOGOOT, ""));
        String strCheckBalanceAfterCharge = pref.getString(GlabaleParameter.PREFS_RESUM_CHECK_BALANCE_AFTER_CHARGE, "");
        String strToDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
        SharedPreferences.Editor editor = getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, Context.MODE_PRIVATE).edit();
        editor.putString(GlabaleParameter.PREFS_RESUM_LAST_DATE, strToDate);

        if (strFromDate.equals("")){strFromDate=strToDate;}
        Integer DateDateDiff = AutoLogout.Difference(strFromDate, strToDate);
        if (DateDateDiff>Countdow){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        editor.apply();
    }

}