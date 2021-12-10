package com.etl.money.fingerprint;

import android.Manifest;
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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

public class FingerprintActivity extends AppCompatActivity {
    private TextView mAuthMsgTv;
    private ViewSwitcher mSwitcher;
    private Button mGoToSettingsBtn;

    String StrMobileNumber;
    String Str_Use_Status = "0";
    CheckBox ch_pringerprint;
    Button btn_save;
    private CancellationSignal cancellationSignal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint);
        checkBiometricSupport();


        ch_pringerprint = (CheckBox) findViewById(R.id.ch_pringerprint);
        mAuthMsgTv = (TextView) findViewById(R.id.auth_message_tv);
        btn_save = (Button) findViewById(R.id.btn_save);
        SharedPreferences msn = getSharedPreferences(GlabaleParameter.PREFS_MSISDN, Context.MODE_PRIVATE);
        StrMobileNumber = msn.getString("msisdn", "");
        // Toast.makeText(this, ""+fingerw.g(), Toast.LENGTH_LONG).show();


        if (fingerw.g().length()<2){
            mAuthMsgTv.setText(R.string.str_device_does_not_have_finger);
            ch_pringerprint.setVisibility(View.GONE);
            btn_save.setVisibility(View.GONE);
        } else{
            SharedPreferences pref = getApplicationContext().getSharedPreferences("SHARED_FK1", MODE_PRIVATE);
            String FK1 = pref.getString("FK1", "");
            if (FK1.length()<2){
                SharedPreferences.Editor editorfk = pref.edit();
                String b = fingerw.g();
                editorfk.putString("FK1", "" + b);
                editorfk.commit();
            }

        }

        mGoToSettingsBtn = (Button) findViewById(R.id.go_to_settings_btn);
        mGoToSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                startActivity(new Intent(Settings.ACTION_SETTINGS));
            }
        });


        SharedPreferences pref = getApplicationContext().getSharedPreferences("SHARED_FINGER", MODE_PRIVATE);

        if (pref.getString("sdnFinger", "").length() > 1) {
            ch_pringerprint.setChecked(true);
            Str_Use_Status = "1";
        }
        //  Check();
        //  btn_save.setEnabled(false);
        ch_pringerprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check();
            }
        });


        // Toast.makeText(this, "Authentication succeeded2.", Toast.LENGTH_SHORT).show();
        SharedPreferences pref1 = getApplicationContext().getSharedPreferences("SHARED_FK1", MODE_PRIVATE);
        String FK1 = pref1.getString("FK1", "");
        SharedPreferences pref2 = getApplicationContext().getSharedPreferences("SHARED_FK2", MODE_PRIVATE);
        String FK2 = pref2.getString("FK2", "");
        if (FK2.equals(FK1)) {
        } else {
            // btn_save.setEnabled(true);
        }
    }

    public void Check() {
        String Str_Use = "";
        if (ch_pringerprint.isChecked()) {
            Str_Use = "1";
        } else {
            Str_Use = "0";
            ;
        }
        if (Str_Use_Status.equals(Str_Use)) {
            // btn_save.setEnabled(false);
        } else {
            //btn_save.setEnabled(true);
        }
    }

    public void btn_save(View v) {

        authenticateUser();

    }

    public void Save(String msdn) {
        //  btn_save.setEnabled(false);
        // ch_pringerprint.setEnabled(false);


        String StrPswFinger = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date()) + StrMobileNumber;
        //  String StrPswFinger = msdn+deviceInfo;
//  username="2029822167";
        // password="234567";
        CryptoHelper cryptoHelper = new CryptoHelper();
        String encryptPswFinger = "";
        try {
            encryptPswFinger = cryptoHelper.encrypt(StrPswFinger);
        } catch (Exception e) {
        }
        StrPswFinger = encryptPswFinger;


        SharedPreferences pref = getApplicationContext().getSharedPreferences("SHARED_FINGER", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        // StrPswFinger = msdn + "|" +StrPswFinger;
        if (ch_pringerprint.isChecked()) {
            //  Toast.makeText(this, ""+StrPswFinger, Toast.LENGTH_LONG).show();

            try {
                String FKL = fingerw.g();
                FKL = fingerw.g();
                FKL = fingerw.g();
                editor.putString("keyFinger", "" + FKL);
                editor.putString("sdnFinger", msdn);  // Saving string
                editor.putString("PswFinger", StrPswFinger);  // Saving string
                //  EnDecryption enDecryption = new EnDecryption(LoginWithFingerprintActivity.this);
                // StrPswFinger = EnDecryption.bytesToHex(enDecryption.encrypt(StrPswFinger));
            } catch (Exception e) {
                ;
                return;
            }

        } else {
            StrPswFinger = "0";
            editor.putString("keyFinger", "");  // Saving string
            editor.putString("sdnFinger", "");  // Saving string
            editor.putString("PswFinger", "0");  // Saving string
        }

        editor.commit(); // commit changes

        SharedPreferences pref1 = getApplicationContext().getSharedPreferences("SHARED_FK1", MODE_PRIVATE);
        // String FK1x =pref1.getString("FK1", "");
        String FK1 = pref1.getString("FK1", "");

        SharedPreferences pref2 = getApplicationContext().getSharedPreferences("SHARED_FK2", MODE_PRIVATE);
        SharedPreferences.Editor editor2 = pref2.edit();
        editor2.putString("FK2", "" + FK1);  // Saving string
        editor2.commit(); // commit changes

/*
        Log.e("Active_FK1:", pref1.getString("FK1", ""));
        Log.e("Active_FK2:", pref2.getString("FK2", ""));
        Log.e("Active_sdnFinger:", pref.getString("sdnFinger", ""));
        Log.e("Active_PswFinger:", pref.getString("PswFinger", ""));
*/

        ActServices(StrPswFinger);

    }

    public void get_result(String result) {
        Str_Use_Status = "" + result;
        SharedPreferences pref1 = getApplicationContext().getSharedPreferences("SHARED_FK1", MODE_PRIVATE);
        // String FK1x =pref1.getString("FK1", "");
        String FK1 = pref1.getString("FK1", "");
        if (FK1.length() < 8) {
            Toast.makeText(this, R.string.str_error_please_try_again, Toast.LENGTH_LONG).show();
            return;
        }
        if (result.equals("1")) {
            Toast.makeText(this, R.string.str_Login_with_finger_used, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, R.string.str_Login_with_finger_not_used, Toast.LENGTH_LONG).show();
        }
        //  btn_save.setEnabled(true);
        // ch_pringerprint.setEnabled(true);

    }

    private void ActServices(final String StrPswFinger) {
        final String strGetLocation = GetLocation.get(FingerprintActivity.this);
        final Activity act = FingerprintActivity.this;
        final ProgressDialog progress_sub = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        progress_sub.setMessage(getString(R.string.str_pleasewait_inprocess));
        progress_sub.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress_sub.show();
        String url = getString(R.string.str_url_https) + "WalletFingerprint.php";
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
                                    result = cryptoHelper.decrypt(jObject.getString("result"));
                                } catch (Exception e) {
                                }
                                String[] arry = result.split("\\|");
                                if (arry[0].equals("405000000")) {
                                    if (StrMobileNumber.equals(arry[1])) {
                                        String Str_Use = "";
                                        if (ch_pringerprint.isChecked()) {
                                            Str_Use = "1";
                                        } else {
                                            Str_Use = "0";
                                            ;
                                        }
                                        get_result(Str_Use);
                                    }
                                } else {
                                    Toast.makeText(getApplication(), "" + arry[1], Toast.LENGTH_LONG).show();
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
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
                String strBasic_info = StrMobileNumber + "|" + verCode + "|" + currentDate + "|" + Strlanguage + "|" + StrdeviceInfo + "|" + Strtoken + "|" + StrjwtToken;
                // ===== End StrBasic info

                //  Log.e("strSMSSetting11:",  strBasic_info);
                String encryptString = "";
                CryptoHelper cryptoHelper = new CryptoHelper();

                strBasic_info += "|" + StrPswFinger;
                try {
                    encryptString = cryptoHelper.encrypt(strBasic_info);
                } catch (Exception e) {
                }
                params.put("publickey", "CheckActive");
                params.put("Active_values", encryptString);
                // Log.e("Active_values:",  encryptString);
                // Log.e("Active_valuesD:",  strBasic_info);
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

    private Boolean checkBiometricSupport() {

        KeyguardManager keyguardManager =
                (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

        PackageManager packageManager = this.getPackageManager();

        if (!keyguardManager.isKeyguardSecure()) {
            notifyUser("Lock screen security not enabled in Settings");
            return false;
        }

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.USE_BIOMETRIC) !=
                PackageManager.PERMISSION_GRANTED) {

            notifyUser("Fingerprint authentication permission not enabled");
            return false;
        }

        if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            return true;
        }

        return true;
    }


    private void notifyUser(String message) {
        Toast.makeText(this,
                message,
                Toast.LENGTH_LONG).show();
    }

    private CancellationSignal getCancellationSignal() {
        cancellationSignal = new CancellationSignal();
        cancellationSignal.setOnCancelListener(new
          CancellationSignal.OnCancelListener() {
           @Override
                                                           public void onCancel() {
                                                               notifyUser("Cancelled via signal");
                                                           }
                                                       });
        return cancellationSignal;
    }

    private BiometricPrompt.AuthenticationCallback getAuthenticationCallback() {


        return new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              CharSequence errString) {
                notifyUser("Authentication error: " + errString);
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationHelp(int helpCode,
                                             CharSequence helpString) {
                super.onAuthenticationHelp(helpCode, helpString);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }

            @Override
            public void onAuthenticationSucceeded(
                    BiometricPrompt.AuthenticationResult result) {
                notifyUser("Authentication Succeeded");
                Save(StrMobileNumber);
                super.onAuthenticationSucceeded(result);
            }
        };
    }


    private void authenticateUser() {
        BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(this)
                .setTitle("Biometric Demo")
                .setSubtitle("Authentication is required to continue")
                .setDescription("This app uses biometric authentication to protect your data.")
                .setNegativeButton("Cancel", this.getMainExecutor(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notifyUser("Authentication cancelled: ");
                            }
                        })
                .build();

        biometricPrompt.authenticate(getCancellationSignal(), getMainExecutor(),
                getAuthenticationCallback());

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