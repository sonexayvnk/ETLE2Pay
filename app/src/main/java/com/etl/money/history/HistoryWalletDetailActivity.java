package com.etl.money.history;

import androidx.appcompat.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.etl.money.LoginActivity;
import com.etl.money.R;
import com.etl.money.config.AutoLogout;
import com.etl.money.global.GlabaleParameter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HistoryWalletDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_wallet_detail);

        try {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(GlabaleParameter.PREFS_HISTORY_DETAIL, MODE_PRIVATE);
        String strDetail     = pref.getString(GlabaleParameter.PREFS_ETL_HISTORY_DETAIL, "");
        Log.e("strDetail", strDetail);



        String[] arry = strDetail.split("~");
        SharedPreferences msn = getSharedPreferences(GlabaleParameter.PREFS_MSISDN, Context.MODE_PRIVATE);
        TextView txtWalleteAccount = (TextView) findViewById(R.id.txtWalleteAccount) ;
        txtWalleteAccount.setText( msn.getString("msisdn", ""));


        TextView txtCirclesTitle = (TextView) findViewById(R.id.txtCirclesTitle) ;
        txtCirclesTitle.setText(arry[6]);
        ShapeDrawable shapeDrawable= new ShapeDrawable();
        shapeDrawable.setShape(new OvalShape());
        shapeDrawable.getPaint().setColor(Color.parseColor(arry[7]));
        txtCirclesTitle.setBackgroundDrawable(shapeDrawable);
        TextView  txtSubject = (TextView) findViewById(R.id.txtSubject) ;
        txtSubject.setText(arry[1]);
      //  txtSubject.setTextColor(Color.parseColor(arry[7]));

        TextView txtDescription = (TextView) findViewById(R.id.txtDescription) ;
        txtDescription.setText(arry[2]);

        TextView txtDatetine = (TextView) findViewById(R.id.txtDatetine) ;
        txtDatetine.setText(arry[4]);

        TextView txtAmount = (TextView) findViewById(R.id.txtAmount) ;
       // txtAmount.setText(arry[6]);
        txtAmount.setText(new DecimalFormat("#,###,###").format(Integer.parseInt(arry[5])) + " " + getString(R.string.str_kip));
        txtAmount.setTextColor(Color.parseColor(arry[7]));

        TextView txtDetail = (TextView) findViewById(R.id.txtDetail) ;
            try {
        txtDetail.setText(arry[9]);
            }     catch (Exception e) {
                txtDetail.setText(getResources().getString(R.string.str_unknown));
            }
        }     catch (Exception e) {
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_icon_close, menu);
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.actionbar_layout_title_center);
            TextView mytext = (TextView) findViewById(R.id.txtcenter);
            mytext.setText(R.string.str_history_detail);
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