package com.etl.money.register;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.etl.money.R;
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

public class RegisterViaKycActivity extends AppCompatActivity {
   EditText txtMobileNumber ;
   TextView txtStatusView , txtStatus, txtRequestIDView , txtRequestID ;
   Button btnRegister;
   String strCheckKYC = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_via_kyc);
      //  txtMobileNumber =  findViewById(R.id.txtMobileNumber);
        txtMobileNumber = findViewById(R.id.txtMobileNumber);
        txtStatusView =  findViewById(R.id.txtStatusView);
        txtStatus = findViewById(R.id.txtStatus);
        txtRequestIDView =  findViewById(R.id.txtRequestIDView);
        txtRequestID = findViewById(R.id.txtRequestID);
        btnRegister =  findViewById(R.id.btnRegister);
        txtStatus.setVisibility(View.GONE );
        txtStatusView.setVisibility(View.GONE );
        txtRequestID.setVisibility(View.GONE );
        txtRequestIDView.setVisibility(View.GONE );
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String strMobileNumber = txtMobileNumber.getText().toString();
                String strRequestID = txtRequestID.getText().toString();
                // check Mobile number
                if (strMobileNumber.trim().startsWith("202") || strMobileNumber.trim().startsWith("302")) {

                } else {
                    Toast.makeText(getApplication(), getString(R.string.str_phone_number_is_invalide), Toast.LENGTH_SHORT).show();
                    txtMobileNumber.requestFocus();
                    txtMobileNumber.setError(getString(R.string.str_phone_number_is_invalide));
                    return;

                }

                if (strMobileNumber.trim().length() < 9 || strMobileNumber.trim().length() > 10) {
                    txtMobileNumber.requestFocus();
                    Toast.makeText(getApplication(), getString(R.string.str_phone_number_is_invalide), Toast.LENGTH_SHORT).show();
                    txtMobileNumber.setError(getString(R.string.str_phone_number_is_invalide));
                    return;
                } else {

                }
                if (strCheckKYC.equals("1")){
                    Register(strMobileNumber, strRequestID );
                }else{
                    LoadInfo(strMobileNumber);
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
            mytext.setText(getString(R.string.str_register_via_kyc));
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void dialog(String Str, int ico) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Str);
        builder.setCancelable(false)
                .setPositiveButton(R.string.str_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (ico == 1){finish();}
                            }
                        });
        AlertDialog alert = builder.create();
        if (ico == 1) { // Success
            alert.setTitle(R.string.str_successful);
            alert.setIcon(R.drawable.ic_success);
        } else if (ico == 2) { //Fail
            alert.setTitle(R.string.str_failed);
            alert.setIcon(R.drawable.ic_warning);
        } else if (ico == 3) { // Error
            alert.setTitle(R.string.str_error);
            alert.setIcon(R.drawable.ic_error);
        }
        alert.show();
    }

    private void Register(String Msisdn, String requestID) {
        //  Toast.makeText(getApplication(),""+str, Toast.LENGTH_LONG).show();
        final String strMsisdn = Msisdn;
        final String strrequestID = requestID;
        final String strGetLocation = GetLocation.get(this);


        final Activity act = com.etl.money.register.RegisterViaKycActivity.this ;
        final ProgressDialog progress_sub = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        progress_sub.setMessage(getString(R.string.str_pleasewait_inprocess));
        progress_sub.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress_sub.show();

        String url = getString(R.string.str_url_https) + "WalletRegisterViaKYC.php";
        //   Log.e("url_info:", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Log.e("result2:------",response);
                        try {
                            //แปลงผลลัพธ์ที่ได้มาเป็น JSON Object
                            JSONObject jObject = new JSONObject(response);
                            if (jObject.getString("status").equals("OK")) {
                                String result = jObject.getString("result");
                                //    Log.e("result2:------",result);
                                CryptoHelper cryptoHelper = new CryptoHelper();
                                try {
                                    result =  cryptoHelper.decrypt(jObject.getString("result"));
                                } catch (Exception e) {
                                }
                                String[] parts = result.split("\\|");
                                String resultCode = parts[0].trim();
                                if(resultCode.equals("405000000")) {

                                    dialog(getString(R.string.str_register_successful) ,  1);
//finish();
                                }else {
                                    dialog(parts[1] ,  2);
                                  //  Toast.makeText(getApplication(),""+parts[1], Toast.LENGTH_LONG).show();
                             }
                            } else {
                                Toast.makeText(com.etl.money.register.RegisterViaKycActivity.this, getString(R.string.str_can_not_connect_to_server), Toast.LENGTH_LONG).show();

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
                String Strlanguage = pref.getString(GlabaleParameter.PREFS_ETL_LANGGAUGE, "en");
                String StrdeviceInfo = pref.getString(GlabaleParameter.PREFS_ETL_DEVICEINFO, null);
                String Strtoken = pref.getString(GlabaleParameter.PREFS_ETL_TOKEN_WALLET_API, null);
                String StrjwtToken = pref.getString(GlabaleParameter.PREFS_JWT_TOKEN, null);
                StrdeviceInfo = strGetLocation + "~" + StrdeviceInfo;
                String strBasic_info   = strMsisdn+"|"+verCode+"|"+currentDate+"|"+Strlanguage+"|"+StrdeviceInfo+"|"+Strtoken+"|"+StrjwtToken+"|"+requestID;
                // ===== End StrBasic info
                String encryptString = "";
                CryptoHelper cryptoHelper = new CryptoHelper();
                try {
                    encryptString = cryptoHelper.encrypt(strBasic_info);
                } catch (Exception e) {
                }
                params.put("publickey", "Register");
                params.put("Active_values", encryptString);
                //  Log.e("strTakePhotosBase64:", ""+strTakePhotosBase64);
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
    private void LoadInfo(String Msisdn) {
        //  Toast.makeText(getApplication(),""+str, Toast.LENGTH_LONG).show();
        final String strMsisdn = Msisdn;
        final String strGetLocation = GetLocation.get(this);


        final Activity act = com.etl.money.register.RegisterViaKycActivity.this ;
        final ProgressDialog progress_sub = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        progress_sub.setMessage(getString(R.string.str_pleasewait_inprocess));
        progress_sub.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress_sub.show();

        String url = getString(R.string.str_url_https) + "WalletRegisterViaKYC.php";
        //   Log.e("url_info:", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Log.e("result2:------",response);
                        try {
                            //แปลงผลลัพธ์ที่ได้มาเป็น JSON Object
                            JSONObject jObject = new JSONObject(response);
                            if (jObject.getString("status").equals("OK")) {
                                String result = jObject.getString("result");
                                //    Log.e("result2:------",result);
                                CryptoHelper cryptoHelper = new CryptoHelper();
                                try {
                                    result =  cryptoHelper.decrypt(jObject.getString("result"));
                                } catch (Exception e) {
                                }
                                String[] parts = result.split("\\|");
                                String resultCode = parts[0].trim();
                                if(resultCode.equals("405000000")) {
                                    if(parts[2].equals("1")) {
                                        strCheckKYC = "1";
                                        btnRegister.setText(getString(R.string.str_register_via_kyc));
                                        txtStatus.setText(getString(R.string.str_active));

                                        txtMobileNumber.setEnabled(false);
                                    }else{

                                        txtStatus.setText(getString(R.string.str_no_subscriber));
                                        txtMobileNumber.setEnabled(true);
                                        strCheckKYC = "0";
                                        btnRegister.setText(getString(R.string.str_query_from_kyc));
                                    }

                                    txtRequestID.setText(parts[3]);


                                    txtStatus.setVisibility(View.VISIBLE );
                                    txtStatusView.setVisibility(View.VISIBLE );
                                    txtRequestID.setVisibility(View.VISIBLE );
                                    txtRequestIDView.setVisibility(View.VISIBLE );


                                }else {
                                    txtStatus.setText(getString(R.string.str_no_subscriber));
                                    txtMobileNumber.setEnabled(true);
                                    txtStatus.setVisibility(View.GONE );
                                    txtStatusView.setVisibility(View.GONE );
                                    txtRequestID.setVisibility(View.GONE );
                                    txtRequestIDView.setVisibility(View.GONE );
                                    btnRegister.setText(getString(R.string.str_query_from_kyc));
                                   // Toast.makeText(getApplication(),""+parts[1], Toast.LENGTH_LONG).show();
                                    strCheckKYC = "0";
                                }
                            } else {
                                Toast.makeText(com.etl.money.register.RegisterViaKycActivity.this, getString(R.string.str_can_not_connect_to_server), Toast.LENGTH_LONG).show();

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
                String Strlanguage = pref.getString(GlabaleParameter.PREFS_ETL_LANGGAUGE, "en");
                String StrdeviceInfo = pref.getString(GlabaleParameter.PREFS_ETL_DEVICEINFO, null);
                String Strtoken = pref.getString(GlabaleParameter.PREFS_ETL_TOKEN_WALLET_API, null);
                String StrjwtToken = pref.getString(GlabaleParameter.PREFS_JWT_TOKEN, null);
                StrdeviceInfo = strGetLocation + "~" + StrdeviceInfo;
                String strBasic_info   = strMsisdn+"|"+verCode+"|"+currentDate+"|"+Strlanguage+"|"+StrdeviceInfo+"|"+Strtoken+"|"+StrjwtToken+"|";
                // ===== End StrBasic info
                String encryptString = "";
                CryptoHelper cryptoHelper = new CryptoHelper();
                try {
                    encryptString = cryptoHelper.encrypt(strBasic_info);
                } catch (Exception e) {
                }
                params.put("publickey", "Loadinfo");
                params.put("Active_values", encryptString);
                //  Log.e("strTakePhotosBase64:", ""+strTakePhotosBase64);
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
}