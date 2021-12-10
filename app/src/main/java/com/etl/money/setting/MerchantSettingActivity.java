package com.etl.money.setting;



import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.etl.money.LoginActivity;
import com.etl.money.R;
import com.etl.money.change_passwor.ChangePasswordWalletActivity;
import com.etl.money.config.AutoLogout;
import com.etl.money.fingerprint.FingerprintActivity;
import com.etl.money.global.GlabaleParameter;
import com.etl.money.promotion_and_advertising.CreateAdvertisingActivity;
import com.etl.money.register.RegisterNewMerchantActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MerchantSettingActivity extends AppCompatActivity {
    GridView gridView;
    ArrayList<ItemSetting> gridArray = new ArrayList<ItemSetting>();

    MerchantAdapterSetting customGridAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_setting);


        // ===== QR Code

        LoadItem();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_exit:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressLint("WrongConstant")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_icon_close, menu);
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.actionbar_layout_title_center);
            TextView mytext = (TextView) findViewById(R.id.txtcenter);
            mytext.setText(R.string.str_setting);
        } catch (Exception e) {
        }
        return true;
    }
    private void LoadItem(){
        //set grid view item
  gridArray.add(new ItemSetting("editphoto", BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_edit_profile),getResources().getString(R.string.str_edit_profile_info),getResources().getString(R.string.str_edit_profile_photo)));
        gridArray.add(new ItemSetting("advertising", BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_advertising),getResources().getString(R.string.str_create_advertising),getResources().getString(R.string.str_create_advertising)));
        gridView = (GridView) findViewById(R.id.gridView1);
        customGridAdapter = new MerchantAdapterSetting(this, R.layout.setting_row, gridArray);
        gridView.setAdapter(customGridAdapter);
    }
    public void AdapterClick(final String strText) {
     if (strText.equals("editphoto")) {
            startActivity(new Intent(getApplicationContext(), RegisterNewMerchantActivity.class));
        } else  if (strText.equals("advertising")) {
         startActivity(new Intent(getApplicationContext(), CreateAdvertisingActivity.class));
     }
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