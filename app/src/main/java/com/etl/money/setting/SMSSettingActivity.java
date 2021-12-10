package com.etl.money.setting;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.etl.money.LoginActivity;
import com.etl.money.R;
import com.etl.money.config.AutoLogout;
import com.etl.money.devise_info.IMEI;
import com.etl.money.global.GlabaleParameter;
import com.etl.money.security.CSSLSocketFactory;
import com.etl.money.security.CryptoHelper;
import com.etl.money.security.GetLocation;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SMSSettingActivity extends AppCompatActivity {
    Switch switchSMSNotification ;
    String StrMobileNumber ;
    String strSMSSettingOrg ;
   String strSMSSetting ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_setting);
        switchSMSNotification = (Switch) findViewById(R.id.switchSMSNotification);
        SharedPreferences msn = getSharedPreferences(GlabaleParameter.PREFS_MSISDN, Context.MODE_PRIVATE);
        StrMobileNumber = msn.getString("msisdn", "");
        SharedPreferences pref = getApplicationContext().getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, MODE_PRIVATE);
        strSMSSettingOrg     = pref.getString(GlabaleParameter.PREFS_SMS_SETTING, "1");
        strSMSSetting = strSMSSettingOrg ;
       // Log.e("strSMSSettingOrg:", strSMSSetting);
        LoadSMSSetting(strSMSSetting);
        switchSMSNotification.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        if (strSMSSetting.equals("1")){
                            SaveSMSSetting("0");
                        }else if (strSMSSetting.equals("0")){
                            SaveSMSSetting("1");
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_icon_close, menu);
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.actionbar_layout_title_center);
            TextView mytext = (TextView) findViewById(R.id.txtcenter);
            mytext.setText(R.string.str_sms_setting);
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

    private void  LoadSMSSetting(String str){
        strSMSSetting = str ;
        if (strSMSSetting.equals("1")){
            switchSMSNotification.setChecked(true);
        }else if (strSMSSetting.equals("0")){
            switchSMSNotification.setChecked(false);
        }
    }

    private void SaveSMSSetting(final String str){
        final String  strGetLocation =    GetLocation.get(SMSSettingActivity.this);
        final Activity act = SMSSettingActivity.this ;
        final ProgressDialog progress_sub = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        progress_sub.setMessage(getString(R.string.str_pleasewait_inprocess));
        progress_sub.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress_sub.show();
        String url = getString(R.string.str_url_https) + "WalletSMSSeting.php";
        //   Log.e("url_info:", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObject = new JSONObject(response);
                            if (jObject.getString("status").equals("OK")) {
                                CryptoHelper cryptoHelper = new CryptoHelper();
                                String result = "";
                                try {
                                    result =  cryptoHelper.decrypt(jObject.getString("result"));
                                } catch (Exception e) {
                                }
                                String[] arry = result.split("\\|");
                                if (arry[0].equals("405000000")){
                                    if (StrMobileNumber.equals(arry[1])){
                                        strSMSSetting = str ;
                                        SharedPreferences pref = getApplicationContext().getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString(GlabaleParameter.PREFS_SMS_SETTING, strSMSSetting);  // Saving string
                                        editor.commit(); // commit changes
                                        strSMSSettingOrg = strSMSSetting ;
                                        // LoadSMSSetting(strSMSSetting);
                                        Toast.makeText(getApplication(), getString(R.string.str_successful), Toast.LENGTH_LONG).show();
                                       // dialog(getString(R.string.str_topup_successful),1) ;  // 1:Success, 2:Fail, 3:Error
                                        // finish();
                                    }
                                }else{
                                    Toast.makeText(getApplication(),""+arry[1], Toast.LENGTH_LONG).show();
                                  //  dialog(""+arry[2],2) ;  // 1:Success, 2:Fail, 3:Error

                                }
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplication(), getString(R.string.str_can_not_connect_to_server), Toast.LENGTH_LONG).show();
                        }
                        progress_sub.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplication(), getString(R.string.str_can_not_connect_to_server), Toast.LENGTH_LONG).show();
                        progress_sub.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() { Map<String, String> params = new HashMap<String, String>();
                // ===== StrBasic_info info
                IMEI imei = new IMEI();
                int verCode = imei.get_versionCode(getApplication());
                String currentDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
                SharedPreferences pref = getApplicationContext().getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, MODE_PRIVATE);
                String Strlanguage     = pref.getString(GlabaleParameter.PREFS_ETL_LANGGAUGE, "en");
                String StrdeviceInfo   = pref.getString(GlabaleParameter.PREFS_ETL_DEVICEINFO, null);
                String Strtoken        = pref.getString(GlabaleParameter.PREFS_ETL_TOKEN_WALLET_API, null);
                String StrjwtToken     = pref.getString(GlabaleParameter.PREFS_JWT_TOKEN, null);
                StrdeviceInfo    = strGetLocation+"~"+StrdeviceInfo;
                String strBasic_info   = StrMobileNumber+"|"+verCode+"|"+currentDate+"|"+Strlanguage+"|"+StrdeviceInfo+"|"+Strtoken+"|"+StrjwtToken;
                // ===== End StrBasic info

                strBasic_info +=  "|"+str;
              //  Log.e("strSMSSetting11:",  strBasic_info);
                String encryptString = "";
                CryptoHelper cryptoHelper = new CryptoHelper();
                try {
                    encryptString =  cryptoHelper.encrypt(strBasic_info);
                } catch (Exception e) {
                }
                params.put("publickey","SMSSetting");
                params.put("Active_values", encryptString);
              // Log.e("Active_valuesTransfer:",  encryptString);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this, new HurlStack(null, CSSLSocketFactory.getSocketFactory(this)));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            Toast.makeText(getApplication(), getString(R.string.str_can_not_connect_to_server), Toast.LENGTH_LONG).show();
            //Log.e("", e.toString());
            progress_sub.dismiss();
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