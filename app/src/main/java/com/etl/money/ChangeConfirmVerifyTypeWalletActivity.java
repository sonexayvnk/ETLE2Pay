package com.etl.money;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
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
import com.etl.money.devise_info.IMEI;
import com.etl.money.global.GlabaleParameter;
import com.etl.money.security.CSSLSocketFactory;
import com.etl.money.security.CryptoHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ChangeConfirmVerifyTypeWalletActivity extends AppCompatActivity {
    String StrShowOrHidePassword ="0";
    TextView mytext;
    private Context context;
    TextView txtVerifyTitle, txtMsisdn, txt_old_password, txtVerifyCode , txtnew_password,txtconfirm_password ;
    Button btnChangeConfirmVerifyType;
    String StrVerifyType ;
    String StrStepOPT = "1";
    String strMsisdn ="";
    String successful ="0";
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_confirm_verify_type_wallet);

        successful = "0";
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.actionbar_layout_title_center);
            mytext = (TextView) findViewById(R.id.txtcenter);
            mytext.setText(R.string.str_change_confirm_verify_code);

        } catch (Exception e) {
        }
        txtMsisdn = (TextView) findViewById(R.id.txtmsisdn);
        txtVerifyTitle = (TextView) findViewById(R.id.txtVerifyTitle);
        txt_old_password = (TextView) findViewById(R.id.txt_old_password);
        txtVerifyCode  = (TextView) findViewById(R.id.txtVerifyCode);
        txtnew_password = (TextView) findViewById(R.id.txtnew_password);
        txtconfirm_password = (TextView) findViewById(R.id.txtconfirm_password);

        btnChangeConfirmVerifyType = (Button) findViewById(R.id.btnChangeConfirmVerifyType);

        SharedPreferences msn = getSharedPreferences(GlabaleParameter.PREFS_MSISDN, Context.MODE_PRIVATE);
        txtMsisdn.setText(msn.getString("msisdn", ""));


        SharedPreferences pref = getApplicationContext().getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, MODE_PRIVATE);
        StrVerifyType     = pref.getString(GlabaleParameter.PREFS_ETL_VERIFY_TYPE, "");

        // getting String
        LoadVerifyType();

        txt_old_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (txt_old_password.getRight() - txt_old_password.getCompoundDrawables()[2].getBounds().width())) {
                        ShowOrHidePassword();
                        return true;
                    }
                }
                return false;
            }
        });
        txtnew_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (txtnew_password.getRight() - txtnew_password.getCompoundDrawables()[2].getBounds().width())) {
                        ShowOrHidePassword();
                        return true;
                    }
                }
                return false;
            }
        });
        txtconfirm_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (txtconfirm_password.getRight() - txtconfirm_password.getCompoundDrawables()[2].getBounds().width())) {
                        ShowOrHidePassword();
                        return true;
                    }
                }
                return false;
            }
        });
        ShowOrHidePassword();
    }
    private void  CleaText(){
        txt_old_password.setText("");
        txtVerifyCode.setText("");
        txtnew_password.setText("");
        txtconfirm_password.setText("");
        txtVerifyCode.setText("");
    }
    private void  LoadVerifyType(){




        if (StrVerifyType.equals("1")){


            btnChangeConfirmVerifyType.setText(getResources().getString(R.string.str_sent_to_sms));

            txtVerifyTitle.setText(getResources().getString(R.string.str_change_opt_to_password));
            //  txtVerifyTitle.setText("Change To Password");

            txt_old_password.setVisibility(View.GONE);
            txtVerifyCode.setVisibility(View.GONE);
            txtnew_password.setVisibility(View.GONE);
            txtconfirm_password.setVisibility(View.GONE);



        } else if (StrVerifyType.equals("2")){
            txtVerifyTitle.setText(getResources().getString(R.string.str_change_password_to_opt));
            btnChangeConfirmVerifyType.setText(getResources().getString(R.string.str_change_to_opt));
            //btnChangeConfirmVerifyType.setText("Chane to OTP");

            txtVerifyCode.setVisibility(View.GONE);
            txtnew_password.setVisibility(View.GONE);
            txtconfirm_password.setVisibility(View.GONE);



        }
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_icon_close, menu);
        return true;
    }

    public void onClickChangeConfirmVerifyType(View v)
    {

        String ms = "";
        if (StrVerifyType.equals("1")){

            if (StrStepOPT.equals("1")){
                ms =  getString(R.string.str_do_you_want_sent_verify_code_to_sms) ;
            }else if (StrStepOPT.equals("2")){
                final    TextView  txtVerifyCode = (TextView) findViewById(R.id.txtVerifyCode);
                if (txtVerifyCode.getText().toString().length()<1){ txtVerifyCode.setError(getString(R.string.str_modify_code));return;}
                if (txtnew_password.getText().toString().length()<1){ txtnew_password.setError(getString(R.string.str_enter_new_password));return;}
                if (txtnew_password.getText().toString().length()<6){ txtnew_password.setError(getString(R.string.str_The_Password_must_be_6_characters_long));return;}
                if (txtconfirm_password.getText().toString().length()<1){ txtconfirm_password.setError(getString(R.string.str_enter_confirm_password));return;}
                if (txtnew_password.getText().toString().equals(txtconfirm_password.getText().toString())){}else{ txtconfirm_password.setError(getString(R.string.str_confirm_password_mismatch));return;}

                ms = getResources().getString(R.string.str_do_you_want_change_confirm_verify_type_otp_to_password);
            }

        }else  if (StrVerifyType.equals("2")){

            if (txt_old_password.getText().toString().length()<1){ txt_old_password.setError(getString(R.string.str_please_enter_Old_password));return;}
            if (txt_old_password.getText().toString().length()<6){ txt_old_password.setError(getString(R.string.str_The_Password_must_be_6_characters_long));return;}

            ms = getResources().getString(R.string.str_do_you_want_change_confirm_verify_type_password_to_otp);
        }

        new AlertDialog.Builder(this)
//                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.app_name)
                .setMessage(ms)
                .setPositiveButton(R.string.str_yes_to_exit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ChangeConfirmVerifyType(txtMsisdn.getText().toString()) ;
                    }
                })
                .setNegativeButton(R.string.str_no_to_exit, null)
                .show();

    }

    private void ChangeConfirmVerifyType(String MobileNumber){
        strMsisdn = MobileNumber;
        final Activity act = ChangeConfirmVerifyTypeWalletActivity.this ;
        final ProgressDialog progress_sub = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        progress_sub.setMessage(getString(R.string.str_pleasewait_inprocess));
        progress_sub.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress_sub.show();
        String url = getString(R.string.str_url_https) + "WalletChangeConfirmVerifyType.php";
        //   Log.e("url_info:", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                   //     Log.e("responseChangeVer:", response);
                        try {
                            JSONObject jObject = new JSONObject(response);
                            if (jObject.getString("status").equals("OK")) {
                                CryptoHelper cryptoHelper = new CryptoHelper();
                                String result = "";
                                try {
                                    result =  cryptoHelper.decrypt(jObject.getString("result"));
                                } catch (Exception e) {
                                }
                            //     Log.e("resultChangeVer:", result) ;

                              //  Log.e("resultChangeVer:", result);

                                String[] arry = result.split("\\|");
                           //     Log.e("resultChangeVer:", arry[1]) ;

                                if (arry[0].equals("405000000")){
                                    if (strMsisdn.equals(arry[1])){
                                        if (StrVerifyType.equals("1")){
                                            if (StrStepOPT.equals("1")){
                                                txtVerifyCode.setVisibility(View.VISIBLE);
                                                txtnew_password.setVisibility(View.VISIBLE);
                                                txtconfirm_password.setVisibility(View.VISIBLE);
                                                StrStepOPT = "2";
                                                btnChangeConfirmVerifyType.setText(getResources().getString(R.string.str_change_to_password));
                                            }else if (StrStepOPT.equals("2")){
                                                successful = "1";
                                                StrStepOPT = "1";
                                                StrVerifyType="2";
                                                dialog(getResources().getString(R.string.str_change_confirm_verify_type_successful_your_verify_type_is_Password));
                                                LoadVerifyType();
                                                CleaText();
                                            }
                                        }else  if (StrVerifyType.equals("2")){
                                            StrStepOPT = "1";
                                            StrVerifyType="1";
                                            successful = "1";
                                            dialog(getResources().getString(R.string.str_change_confirm_verify_type_successful_your_verify_type_is_OPT));
                                            LoadVerifyType();
                                            CleaText();
                                        }
                                    }
                                }else{

                                    dialog(arry[2]);

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
                String strBasic_info   = strMsisdn+"|"+verCode+"|"+currentDate+"|"+Strlanguage+"|"+StrdeviceInfo+"|"+Strtoken+"|"+StrjwtToken;
                // ===== End StrBasic info
                if (StrVerifyType.equals("1")){
                    if (StrStepOPT.equals("1")){
                        params.put("publickey","SentSMS");
                    }else if (StrStepOPT.equals("2")){
                        params.put("publickey","ChangeToPassword");
                        final    TextView  txtVerifyCode = (TextView) findViewById(R.id.txtVerifyCode);
                        final    TextView  txtnew_password = (TextView) findViewById(R.id.txtnew_password);
                        strBasic_info +=  "|"+txtVerifyCode.getText().toString()+"|"+txtnew_password.getText().toString();
                    }
                }else  if (StrVerifyType.equals("2")){
                    params.put("publickey","ChangeToOTP");
                    final    TextView  txt_old_password = (TextView) findViewById(R.id.txt_old_password);
                    strBasic_info += "|"+txt_old_password.getText().toString();
                }
                CryptoHelper cryptoHelper = new CryptoHelper();
                String encryptString = "";
                try {
                    encryptString =  cryptoHelper.encrypt(strBasic_info);
                } catch (Exception e) {
                }
                params.put("Active_values", encryptString);

              //  Log.e("Active_valuesChangeVer:", encryptString);
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
                            ((WalletActivity)context).refresh();

                        finish();
                        //  edtAmt.setText("");
                        return;
                    }

                })
                // .setNegativeButton(R.string.str_no_to_exit, null)
                .show();

    }
    private  void ShowOrHidePassword(){

        if (StrShowOrHidePassword.equals("0")){
            txt_old_password.setCompoundDrawablesWithIntrinsicBounds(null,null, getResources().getDrawable(R.drawable.ic_pw_hide),null);
            txtnew_password.setCompoundDrawablesWithIntrinsicBounds(null,null, getResources().getDrawable(R.drawable.ic_pw_hide),null);
            txtconfirm_password.setCompoundDrawablesWithIntrinsicBounds(null,null, getResources().getDrawable(R.drawable.ic_pw_hide),null);
            txt_old_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            txtnew_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            txtconfirm_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            StrShowOrHidePassword="1";
        }else if (StrShowOrHidePassword.equals("1")){
            txt_old_password.setCompoundDrawablesWithIntrinsicBounds(null,null, getResources().getDrawable(R.drawable.ic_pw_show),null);
            txtnew_password.setCompoundDrawablesWithIntrinsicBounds(null,null, getResources().getDrawable(R.drawable.ic_pw_show),null);
            txtconfirm_password.setCompoundDrawablesWithIntrinsicBounds(null,null, getResources().getDrawable(R.drawable.ic_pw_show),null);
            txt_old_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
            txtnew_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
            txtconfirm_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
            StrShowOrHidePassword="0";
        }else if (StrShowOrHidePassword.equals("2")){
            txt_old_password.setCompoundDrawablesWithIntrinsicBounds(null,null, getResources().getDrawable(R.drawable.ic_blank),null);
            txtnew_password.setCompoundDrawablesWithIntrinsicBounds(null,null, getResources().getDrawable(R.drawable.ic_blank),null);
            txtconfirm_password.setCompoundDrawablesWithIntrinsicBounds(null,null, getResources().getDrawable(R.drawable.ic_blank),null);
            txt_old_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
            txtnew_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
            txtconfirm_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
            StrShowOrHidePassword="2";
        }
    }
}
