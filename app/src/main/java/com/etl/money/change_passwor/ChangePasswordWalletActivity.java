package com.etl.money.change_passwor;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.etl.money.config.InternetCheck;
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

public class ChangePasswordWalletActivity extends AppCompatActivity {
    String StrShowOrHidePasswordOldPW ="0";
    String StrShowOrHidePasswordNewPW ="0";
    String StrShowOrHidePasswordConfirmPW ="0";

    TextView str, str_sms,txt_old_password,txt_new_password,txt_connfirm_password;
    Button btnRefresh;
    String strPwold;
    String successful ="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_wallet);
        String successful ="0";
        SharedPreferences msn = getSharedPreferences(GlabaleParameter.PREFS_MSISDN, Context.MODE_PRIVATE);
        final String msdn = msn.getString("msisdn", "");

        Intent myIntent = getIntent(); // gets the previously created intent
        String serviceName = myIntent.getStringExtra("serviceName"); // will return "FirstKeyValue"
        //String secondKeyName= myIntent.getStringExtra("secondKeyName");


        str = new EditText(this);

        str = (EditText) findViewById(R.id.txt_old_password);
        str.setHint(R.string.str_enter_old_password);

        str = new EditText(this);
        str = (EditText) findViewById(R.id.txt_new_password);
        str.setHint(R.string.str_enter_new_password);

        str = new EditText(this);
        str = (EditText) findViewById(R.id.txt_connfirm_password);
        str.setHint(R.string.str_enter_confirm_password);

        str = new Button(this);
        str = (Button) findViewById(R.id.button_change);
        str.setText(R.string.str_change_assword);


        txt_old_password =  (EditText) findViewById(R.id.txt_old_password);
        txt_new_password = (EditText) findViewById(R.id.txt_new_password);
        txt_connfirm_password =  (EditText) findViewById(R.id.txt_connfirm_password);

        btnRefresh = new Button(this);
        btnRefresh = (Button) findViewById(R.id.button_change);
        btnRefresh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplication(), "btnRefresh .", Toast.LENGTH_LONG).show();
                try {
                    EditText text1 = (EditText) findViewById(R.id.txt_old_password);
                    String str_old_password = text1.getText().toString().trim();

                    EditText text2 = (EditText) findViewById(R.id.txt_new_password);
                    String str_new_password = text2.getText().toString().trim();


                    if (str_old_password.trim().length() <4 ) {
                        text1.requestFocus();
                        Toast.makeText(getApplication(), getString(R.string.str_The_Password_must_be_6_characters_long), Toast.LENGTH_SHORT).show();
                        text1.setError(getString(R.string.str_The_Password_must_be_6_characters_long));
                        return;
                    }


                    if (str_new_password.trim().length() < 5) {
                        text2.requestFocus();
                        Toast.makeText(getApplication(), getString(R.string.str_The_Password_must_be_6_characters_long), Toast.LENGTH_SHORT).show();
                        text2.setError(getString(R.string.str_The_Password_must_be_6_characters_long));
                        return;
                    }


                    EditText text3 = (EditText) findViewById(R.id.txt_connfirm_password);
                    String str_firm_password = text3.getText().toString().trim();
                    str_sms = (TextView) findViewById(R.id.txt_smsChangePass);
                    str_sms.setTextColor(Color.parseColor("#FF0000"));
                    //  if  (str_old_password.trim().length() < 5) {str_sms.setText(R.string.str_The_Password_must_be_6_characters_long);return;}
                    //  if  (str_new_password.trim().length() < 5) {str_sms.setText(R.string.str_The_Password_must_be_6_characters_long);return;}
                    if (str_old_password.equals("")) {
                        str_sms.setText(R.string.str_please_enter_Old_password);
                        return;
                    }
                    if (str_new_password.equals("")) {
                        str_sms.setText(R.string.str_please_enter_new_password);
                        return;
                    }
                    if (str_firm_password.equals("")) {
                        str_sms.setText(R.string.str_please_enter_confirm_password);
                        return;
                    }
                    if (str_new_password.equals("")) {
                    } else {
                        if (str_new_password.equals(str_firm_password)) {
                            ConfirmCangPassword(msdn);
                        } else {
                            str_sms = (TextView) findViewById(R.id.txt_smsChangePass);
                            str_sms.setText(R.string.str_confirm_password_mismatch);
                        }
                    }
                } catch (Exception ex) {
                    //    Log.e("ConfirmCangPassword", ex.getMessage());
                }
            }
        });


        txt_old_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (txt_old_password.getRight() - txt_old_password.getCompoundDrawables()[2].getBounds().width())) {
                        ShowOrHidePasswordOldPW();
                        return true;
                    }
                }
                return false;
            }
        });
        txt_new_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (txt_new_password.getRight() - txt_new_password.getCompoundDrawables()[2].getBounds().width())) {
                        ShowOrHidePasswordNewPW();
                        return true;
                    }
                }
                return false;
            }
        });
        txt_connfirm_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (txt_connfirm_password.getRight() - txt_connfirm_password.getCompoundDrawables()[2].getBounds().width())) {
                        ShowOrHidePasswordConfirmPW();
                        return true;
                    }
                }
                return false;
            }
        });
        ShowOrHidePasswordOldPW();
        ShowOrHidePasswordNewPW();
        ShowOrHidePasswordConfirmPW();
    }

    public void ConfirmCangPassword(String msdn) {
        new AlertDialog.Builder(this)
                // .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.app_name)
                .setMessage(R.string.str_do_you_want_to_change_password)
                .setPositiveButton(R.string.str_yes_to_exit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences msne = getSharedPreferences(GlabaleParameter.PREFS_MSISDN, Context.MODE_PRIVATE);
                        final String msdnd = msne.getString("msisdn", "");

                        ChangePassword(msdnd);
                    }
                })
                .setNegativeButton(R.string.str_no_to_exit, null)
                .show();
    }







    @SuppressLint("WrongConstant")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_icon_close, menu);
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.actionbar_layout_title_center);
            TextView mytext = (TextView) findViewById(R.id.txtcenter);
            mytext.setText(R.string.str_change_assword);
        } catch (Exception e) {
        }
        return true;
    }




    // connected internet
    private void ChangePassword(final String finalMobileEn) {
     final String  strGetLocation =    GetLocation.get(ChangePasswordWalletActivity.this);

        InternetCheck internetCheck = new InternetCheck();
        if (internetCheck.isInternetOn(getApplication())) {
        } else {
            Toast.makeText(this, getString(R.string.str_conect_internet_fail), Toast.LENGTH_LONG).show();
            return;
        }
        final ProgressDialog progress_sub = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        progress_sub.setMessage(getString(R.string.str_pleasewait_inprocess));
        progress_sub.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress_sub.show();
        final String url = getString(R.string.str_url_https) + "WalletChangePassword.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jObject = new JSONObject(response);

                            if (jObject.getString("status").equals("OK")) {
                                //   String res = jObject.getString("result");
                                //  String decrypted = null;
                                try {
                                    CryptoHelper cryptoHelper = new CryptoHelper();
                                    String result = "";
                                    try {
                                        result =  cryptoHelper.decrypt(jObject.getString("result"));
                                    } catch (Exception e) {
                                    }


                                    String[] arry = result.split("\\|");
                                    if (arry[0].equals("405000000")){
                                        successful = "1";
                                        dialog(getString(R.string.str_change_password_complete));
                                        str_sms.setText("");
                                        txt_old_password.setText("");
                                        txt_new_password.setText("");
                                        txt_connfirm_password.setText("");

                                    }else if (arry[0].equals("233")){
                                        str_sms.setText(R.string.str_old_password_incorrect);
                                        dialog(getString(R.string.str_old_password_incorrect));
                                    }else{
                                        dialog(arry[1]);
                                    }

                                } catch (Exception e) {
                                }
                                // Log.e("Decrypted:", decrypted);
                                //  setList_Refresh(decrypted);
                                // dialog(String Str)
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplication(), getString(R.string.str_can_not_connect_to_server)+" a", Toast.LENGTH_LONG).show();
                        }
                        progress_sub.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplication(), getString(R.string.str_can_not_connect_to_server)+" b", Toast.LENGTH_LONG).show();
                        progress_sub.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
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
                String strBasic_info   = finalMobileEn+"|"+verCode+"|"+currentDate+"|"+Strlanguage+"|"+StrdeviceInfo+"|"+Strtoken+"|"+StrjwtToken;
                // ===== End StrBasic info

                EditText txtOldPass = (EditText) findViewById(R.id.txt_old_password);
                String str_old_password = txtOldPass.getText().toString().trim();
                EditText txtNewpass = (EditText) findViewById(R.id.txt_new_password);
                String str_new_password = txtNewpass.getText().toString().trim();

                strBasic_info +="|"+str_old_password;
                strBasic_info +="|"+str_new_password;

               /*
                String enCrypNewpass = null;
                String enCrypOldpass = null;
                // EnDecryption enDecryption = new EnDecryption(ChangePasswordActivity.this);
                EditText txtOldPass = (EditText) findViewById(R.id.txt_old_password);
                String str_old_password = txtOldPass.getText().toString().trim();
                EditText txtNewpass = (EditText) findViewById(R.id.txt_new_password);
                String str_new_password = txtNewpass.getText().toString().trim();
                IMEI imei = new IMEI();
                String deviceInfo = "";
                int verCode = 0;
                try {
                    deviceInfo = imei.get_dev_id02(getApplication());
                    verCode = imei.get_versionCode(getApplication());
                } catch (Exception e) {
                }
                SharedPreferences prefs = getSharedPreferences("CommonPrefs", MODE_PRIVATE);
                String language  = prefs.getString("langPref", "en");
                String mStr = finalMobileEn + "|" + str_old_password + "|" + str_new_password + "|" + deviceInfo + "|" + language + "|" + strjwtToken;
                //  Log.e("mStr1", mStr);
*/
                CryptoHelper cryptoHelper = new CryptoHelper();
                String encryptString = "";
                try {
                    encryptString =  cryptoHelper.encrypt(strBasic_info);
                } catch (Exception e) {
                }
                //  Log.e("Before Encrypted:", mStr);
                //  Log.e("Before URL:", url);
                params.put("publickey", "ChangePassword");
                params.put("Active_values", encryptString);


               // params.put("versionCode", String.valueOf(verCode));
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this, new HurlStack(null, CSSLSocketFactory.getSocketFactory(this)));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            Toast.makeText(getApplication(), getString(R.string.str_can_not_connect_to_server), Toast.LENGTH_LONG).show();
            progress_sub.dismiss();
        }
    }

    private void dialog(String Str) {
        new AlertDialog.Builder(this)
                //.setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.app_name)
                .setMessage(Str)
                .setPositiveButton(R.string.str_yes_to_exit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (successful.equals("1"))
                            //Stop the activity
                            finish();
                        //  edtAmt.setText("");

                        return;
                    }

                })
                // .setNegativeButton(R.string.str_no_to_exit, null)
                .show();

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
    private  void ShowOrHidePasswordOldPW(){

        if (StrShowOrHidePasswordOldPW.equals("0")){
            txt_old_password.setCompoundDrawablesWithIntrinsicBounds(null,null, getResources().getDrawable(R.drawable.ic_pw_hide),null);
            txt_old_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            StrShowOrHidePasswordOldPW="1";
        }else if (StrShowOrHidePasswordOldPW.equals("1")){
            txt_old_password.setCompoundDrawablesWithIntrinsicBounds(null,null, getResources().getDrawable(R.drawable.ic_pw_show),null);
            txt_old_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
            StrShowOrHidePasswordOldPW="0";
        }else if (StrShowOrHidePasswordOldPW.equals("2")){
            txt_old_password.setCompoundDrawablesWithIntrinsicBounds(null,null, getResources().getDrawable(R.drawable.ic_blank),null);
            txt_old_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
            StrShowOrHidePasswordOldPW="2";
        }
    }
    private  void ShowOrHidePasswordNewPW(){

        if (StrShowOrHidePasswordNewPW.equals("0")){
            txt_new_password.setCompoundDrawablesWithIntrinsicBounds(null,null, getResources().getDrawable(R.drawable.ic_pw_hide),null);
            txt_new_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            StrShowOrHidePasswordNewPW="1";
        }else if (StrShowOrHidePasswordNewPW.equals("1")){
            txt_new_password.setCompoundDrawablesWithIntrinsicBounds(null,null, getResources().getDrawable(R.drawable.ic_pw_show),null);
            txt_new_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
            StrShowOrHidePasswordNewPW="0";
        }else if (StrShowOrHidePasswordNewPW.equals("2")){
            txt_new_password.setCompoundDrawablesWithIntrinsicBounds(null,null, getResources().getDrawable(R.drawable.ic_blank),null);
            txt_new_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
            StrShowOrHidePasswordNewPW="2";
        }
    }
    private  void ShowOrHidePasswordConfirmPW(){

        if (StrShowOrHidePasswordConfirmPW.equals("0")){
            txt_connfirm_password.setCompoundDrawablesWithIntrinsicBounds(null,null, getResources().getDrawable(R.drawable.ic_pw_hide),null);
            txt_connfirm_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            StrShowOrHidePasswordConfirmPW="1";
        }else if (StrShowOrHidePasswordConfirmPW.equals("1")){
            txt_connfirm_password.setCompoundDrawablesWithIntrinsicBounds(null,null, getResources().getDrawable(R.drawable.ic_pw_show),null);
            txt_connfirm_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
            StrShowOrHidePasswordConfirmPW="0";
        }else if (StrShowOrHidePasswordConfirmPW.equals("2")){
            txt_connfirm_password.setCompoundDrawablesWithIntrinsicBounds(null,null, getResources().getDrawable(R.drawable.ic_blank),null);
            txt_connfirm_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
            StrShowOrHidePasswordConfirmPW="2";
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

