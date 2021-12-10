package com.etl.money.setting;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.etl.money.LoginActivity;
import com.etl.money.R;
import com.etl.money.config.AutoLogout;
import com.etl.money.global.GlabaleParameter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SelectAccountTypeActivity  extends AppCompatActivity {
    TextView mytext;
    String paymentType = "1";
    String paymentTypeOrg = "1";
    private RadioButton rdMainBalance,rdSubBalance;
    TextView txtMainBalance,txtSubBalance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_account_type);
        try {
           // getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.actionbar_layout_title_center);
            mytext = (TextView) findViewById(R.id.txtcenter);
            mytext.setText(R.string.str_select_account_type_for_payment_wallet);
        } catch (Exception e) {
        }
        rdMainBalance = (RadioButton) findViewById(R.id.rdMainBalance);
        rdSubBalance = (RadioButton) findViewById(R.id.rdSubBalance);
        txtMainBalance  = (TextView) findViewById(R.id.txtMainBalance);
        txtSubBalance  = (TextView) findViewById(R.id.txtSubBalance);
        SharedPreferences pref = getApplicationContext().getSharedPreferences(GlabaleParameter.PAYMENT_TPYE, MODE_PRIVATE);
        paymentTypeOrg =  pref.getString(GlabaleParameter.PREFS_PAYMENT_TPYE, "1");
        paymentType =paymentTypeOrg;

        LoadAccount(paymentType);
        rdMainBalance.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        paymentType = "1";
                        LoadAccount(paymentType);
                    }
                });
        rdSubBalance.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                paymentType = "2";
                LoadAccount(paymentType);
            }
        });

        txtMainBalance.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        paymentType = "1";
                        LoadAccount(paymentType);
                    }
                });
        txtSubBalance.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                paymentType = "2";
                LoadAccount(paymentType);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_icon_close, menu);
        try {
            //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.actionbar_layout_title_center);
            TextView mytext = (TextView) findViewById(R.id.txtcenter);
            mytext.setText(R.string.str_chose_account);
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
    private void  LoadAccount(String str){

        paymentType = str ;
        Button btnSave = (Button)findViewById(R.id.btnSave);
        if (paymentType.equals(paymentTypeOrg)){
            btnSave.setEnabled(false);
        } else {
            btnSave.setEnabled(true);
        }



        if (paymentType.equals("1")){
            rdMainBalance.setChecked(true);
            rdSubBalance.setChecked(false);
            txtMainBalance.setTextColor(Color.parseColor("#0064ff"));
            txtSubBalance.setTextColor(Color.parseColor("#858585"));
        }else   if (paymentType.equals("2")){

            rdMainBalance.setChecked(false);
            rdSubBalance.setChecked(true);
            txtMainBalance.setTextColor(Color.parseColor("#858585"));
            txtSubBalance.setTextColor(Color.parseColor("#0064ff"));
        }
    }
    public void onClickSaveAccType(View v)
    {

String ms;
        ms = ""+getResources().getString(R.string.str_would_you_like_to_save);
        //  txtTranAmt.setText(new DecimalFormat("#,###,###").format(Integer.parseInt(StrTranAmt+"")));
        final AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle(R.string.str_confirm);
        alert.setMessage(ms);
        alert.setIcon(R.drawable.ic_question);
        alert.setButton(getResources().getString(R.string.str_yes_to_exit), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                SharedPreferences pref = getApplicationContext().getSharedPreferences(GlabaleParameter.PAYMENT_TPYE, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(GlabaleParameter.PREFS_PAYMENT_TPYE, paymentType);  // Saving string
                editor.commit(); // commit changes
                paymentTypeOrg = paymentType ;
                LoadAccount(paymentType);
            }
        });
        alert.setButton2(getResources().getString(R.string.str_no_to_exit), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.show();


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
