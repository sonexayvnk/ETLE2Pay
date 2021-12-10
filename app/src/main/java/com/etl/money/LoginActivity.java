package com.etl.money;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.etl.money.config.ResetPasswordActivity;
import com.etl.money.devise_info.IMEI;
import com.etl.money.fingerprint.fingerw;
import com.etl.money.global.GlabaleParameter;
import com.etl.money.permission.MarshMallowPermission;
import com.etl.money.register.PolicyActivity;
import com.etl.money.register.RegisterActivity;
import com.etl.money.register.RegisterViaKycActivity;
import com.etl.money.security.CSSLSocketFactory;
import com.etl.money.security.CryptoHelper;
import com.etl.money.security.GetLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

//import android.support.v4.content.ContextCompat;

public class LoginActivity extends AppCompatActivity {
    private CancellationSignal cancellationSignal;
    LinearLayout footerLayout;
    String StrShowOrHidePassword = "0";
    String strRemember_me = "0";
    EditText txtUserName, textPassword;
    Button btnUseFinger,changeLangauge,btn_login;
    Locale myLocale;
    Context context = this;
    LinearLayout constraintLayout;
    LinearLayout logoLinear,TextInputLayoutOTP;
    TextView bt_register,guestUser,aboutApp;
    EditText textOTP;
    String strOTP = "0" ;
    private Context mContext;
    private Activity mActivity;

    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;
    FusedLocationProviderClient fusedLocationProviderClient;

    public static final int MULTIPLE_PERMISSIONS = 10; // code you want.
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bt_register = findViewById(R.id.register);
        guestUser = findViewById(R.id.guestUser);
        aboutApp = findViewById(R.id.aboutApp);
        textOTP = findViewById(R.id.textOTP);
        btn_login = findViewById(R.id.btn_login);
        btnUseFinger = findViewById(R.id.btnUseFinger);

        TextInputLayoutOTP = findViewById(R.id.TextInputLayoutOTP);
        textOTP.setVisibility(View.GONE);
        TextInputLayoutOTP.setVisibility(View.GONE);
        textOTP.setText("");
        strOTP = "0";
        getInfo();
//        String strplayerid = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();
//        Log.e("strplayerid", strplayerid);
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        SharedPreferences pref2 = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref2.edit();
//        String ss=    pref2.getString("key_name5", null);          // getting String
        //    Log.d("debugaa", "User:" );
//        OneSignal.startInit(this)
//                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
//                .unsubscribeWhenNotificationsAreDisabled(true)
//                .init();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermissions()) {
                // permissions granted.
                Log.e("permissions granted", "permissions granted 01");
            } else {
                // show dialog informing them that we lack certain permissions

                Log.e("permissions granted", "show dialog informing them that we lack certain permissions 01");
            }
        }


        // Get the application context
        mContext = getApplicationContext();
        mActivity = LoginActivity.this;

//        mContext = getApplicationContext();
//        // Get the activity
//        mActivity = LoginActivity.this;
//  checkAllowPhoneStatePermission();
        //      checkAllowPhoneStatePermission();


        // The request code used in ActivityCompat.requestPermissions()
// and returned in the Activity's onRequestPermissionsResult()
        //  if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){


        //  }
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//            checkPermission();
        //  android.Manifest.permission.USE_BIOMETRIC,
//


        loadLocale();

        txtUserName = (EditText) findViewById(R.id.txtUserName);
        textPassword = (EditText) findViewById(R.id.textPassword);
        footerLayout = (LinearLayout) findViewById(R.id.footerLayout);

        txtUserName.setText("");
        textPassword.setText("");

        logoLinear = (LinearLayout) findViewById(R.id.logo);
        //=== Remember Wallet number
        CheckBox chRemember_me = (CheckBox) findViewById(R.id.chRemember_me);


        SharedPreferences pref = getApplicationContext().getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, MODE_PRIVATE);
        strRemember_me = pref.getString(GlabaleParameter.PREFS_ETL_REMEMBER_WALLET_NUMBER, "0");
        if (strRemember_me.equals("1")) {
            chRemember_me.setChecked(true);
            SharedPreferences msne = getSharedPreferences(GlabaleParameter.PREFS_MSISDN, Context.MODE_PRIVATE);
            txtUserName.setText(msne.getString(GlabaleParameter.PREFS_MSISDN, ""));
        } else {
            chRemember_me.setChecked(false);
        }
        chRemember_me.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final SharedPreferences shared = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                if (isChecked) {
                    strRemember_me = "1";
                } else {
                    strRemember_me = "0";
                }
            }
        });
        //=== End Remember Wallet number

        constraintLayout = findViewById(R.id.rootview);
        constraintLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                //  ImageView imgView = (ImageView)findViewById(R.id.logo);
                Rect r = new Rect();
                constraintLayout.getWindowVisibleDisplayFrame(r);
                int screenHeight = constraintLayout.getRootView().getHeight();
                //  Log.e("screenHeight", String.valueOf(screenHeight));
                int keypadHeight = screenHeight - r.bottom;
                //  Log.e("keypadHeight", String.valueOf(keypadHeight));
                if (keypadHeight > screenHeight * 0.15) {
                    //    Toast.makeText(LoginActivity.this,"Keyboard is showing",Toast.LENGTH_LONG).show();
                    logoLinear.setVisibility(View.GONE);
                    footerLayout.setVisibility(View.GONE);
                    //  swipeRefresh.setPadding(0, 0, 0, keypadHeight);
                } else {

                    logoLinear.setVisibility(View.VISIBLE);
                    footerLayout.setVisibility(View.VISIBLE);
                    // swipeRefresh.setPadding(0, 0, 0, 0);
                    //   Toast.makeText(LoginActivity.this,"keyboard closed",Toast.LENGTH_LONG).show();
                }
            }
        });


        changeLangauge = findViewById(R.id.lg_changed);
        changeLangauge.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ShowsPopupLanguage();

            }


        });
        textPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (textPassword.getRight() - textPassword.getCompoundDrawables()[2].getBounds().width())) {
                        ShowOrHidePassword();
                        return true;
                    }
                }
                return false;
            }
        });
        ShowOrHidePassword();


//        // Init OneSignal
//        OneSignal.startInit(this).setNotificationOpenedHandler(new NotificationOpenHandler2()).init();
//    }
//
//    class NotificationOpenHandler2 implements OneSignal.NotificationOpenedHandler {
//        // This fires when a notification is opened by tapping on it.
//        @Override
//        public void notificationOpened(OSNotificationOpenResult result) {
//            OSNotificationAction.ActionType actionType = result.action.type;
//            JSONObject data = result.notification.payload.additionalData;
//    }
        checkFinger();
        checkPermission();
    }
    public void onClickUseFinger(View v) {
        checkFinger();
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermissions()) {
                // permissions granted.
                Log.e("permissions granted", "permissions granted 01");
            } else {
                // show dialog informing them that we lack certain permissions

                Log.e("permissions granted", "show dialog informing them that we lack certain permissions 01");
            }
        }
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getApplication(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permissions granted.
                    Log.e("permissions granted", "permissions granted 02");
                } else {
                    // no permissions granted.
                    Log.e("no permissions granted", "no permissions granted 02");
                    // checkPermissions();
                }
                return;
            }
        }
    }




    private void ShowOrHidePassword() {
        if (StrShowOrHidePassword.equals("1")) {
            textPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_pw_show), null);
            textPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
            StrShowOrHidePassword = "0";
        } else if (StrShowOrHidePassword.equals("0")) {
            textPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_pw_hide), null);
            textPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            StrShowOrHidePassword = "1";
        }
    }


    private void dialog(String Str, int ico) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Str);
        builder.setCancelable(false)
                .setPositiveButton(R.string.str_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                checkFinger();
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

    public void onClickCancel(View v) {
        finish();
    }

    private void Loginsuccess() {
        Intent intent = new Intent(this, WalletActivity.class);
        startActivity(intent);
    }

    public void Login(final String strLoninType) {
        final String strGetLocation = GetLocation.get(LoginActivity.this);
        final Activity act = LoginActivity.this;
        final ProgressDialog progress_sub = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        progress_sub.setMessage(getString(R.string.str_pleasewait_inprocess));
        progress_sub.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress_sub.show();
        String url = getString(R.string.str_url_https) + "WalletLogin.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      //  Log.e("response11:", response);
                        try {
                            JSONObject jObject = new JSONObject(response);
                            if (jObject.getString("status").equals("OK")) {

                              //   Log.e("response:", response);

                                CryptoHelper cryptoHelper = new CryptoHelper();
                                String decryptString = "";


                                try {
                                    decryptString = cryptoHelper.decrypt(jObject.getString("result"));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }



                                  //Log.e("Decrpt:", decryptString);
                                String[] arry = decryptString.split("\\|");

                                // Log.e("Decrpta:", arry[0]);

                                if (arry[0].equals("100000123")) {
                                    textOTP.setVisibility(View.VISIBLE);
                                    TextInputLayoutOTP.setVisibility(View.VISIBLE);
                                    strOTP = "1";
                                    textOTP.setText("");
                                    //btn_login.setText(getString(R.string.str_get_otp));
                                    progress_sub.dismiss();
                                    return;
                                }


                                if (arry[0].equals("405000000")) {
                                    SharedPreferences msne = getSharedPreferences(GlabaleParameter.PREFS_MSISDN, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = msne.edit();
                                    editor.clear();
                                    editor.putString(GlabaleParameter.PREFS_MSISDN, arry[1]);
                                    editor.apply();
                                    String confirmMethod = arry[4];
                                    //     Log.e("decryptString2 Login=",""+ decryptString);
                                    //  SharedPreferences editorMyPrefAfertLogin = getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor1 = getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, Context.MODE_PRIVATE).edit();

                                    editor1.putString(GlabaleParameter.PREFS_ETL_DEVICEINFO, "Test");

                                    SharedPreferences prefs = getSharedPreferences("CommonPrefs", MODE_PRIVATE);
                                    String language = prefs.getString("Language", "en");

                                    editor1.putString(GlabaleParameter.PREFS_ETL_LANGGAUGE, ""+language);
                                    editor1.putString(GlabaleParameter.PREFS_ETL_MAIN_BALANCE, "" + arry[2]);
                                    editor1.putString(GlabaleParameter.PREFS_ETL_SUB1_BALANCE, "" + arry[3]);
                                    editor1.putString(GlabaleParameter.PREFS_ETL_VERIFY_TYPE, arry[4]);
                                    editor1.putString(GlabaleParameter.PREFS_ETL_TOKEN_WALLET_API, "" + arry[5]);
                                    editor1.putString(GlabaleParameter.PREFS_JWT_TOKEN, "" + arry[6]);
                                    editor1.putString(GlabaleParameter.PREFS_SMS_SETTING, "" + arry[7]);
                                    // editor1.putString(GlabaleParameter.PREFS_ETL_DASHBOARD, "" + arry[8]);
                                    // editor1.putString(GlabaleParameter.PREFS_ETL_PROFILE_ID, "" + arry[9]);
                                    String strProfileIdAfterLogin = arry[8];
                                    String str_profile_photos_after_login = arry[9];
                                    // Log.e("decryptString20210305=",arry[10]+"********");
                                    // editor1.putString(GlabaleParameter.PREFS_ETL_PROFILE_PHOTOS, "" + arry[10]);
                                    editor1.putString(GlabaleParameter.PREFS_ETL_CUSTOMER_NAME, "" + arry[10]);
                                    editor1.putString(GlabaleParameter.PREFS_ETL_CUSTOMER_LAST_NAME, "" + arry[11]);
                                    //===20210223
                                    // Log.e("PREFS_SMS_SETTING:", arry[12]);
                                    editor1.putString(GlabaleParameter.PREFS_ETL_IS_END_USER_ENABLE, "" + arry[12]);
                                    // editor1.putString(GlabaleParameter.PREFS_ETL_IS_END_USER_ENABLE, ""+ arry[12]);
                                    editor1.putString(GlabaleParameter.PREFS_ETL_IS_AGENT_ENABLE, "" + arry[13]);
                                    editor1.putString(GlabaleParameter.PREFS_ETL_IS_MERCHANT_ENABLE, "" + arry[14]);
                                    if (arry[12].equals("1")) {
                                        editor1.putString(GlabaleParameter.PREFS_ETL_END_USER_MENU_LIST_SHOWMORE, "" + arry[15]);
                                        editor1.putString(GlabaleParameter.PREFS_ETL_END_USER_MENU_LIST_ฺCOUNT, "" + arry[18]);
                                    }
                                    if (arry[13].equals("1")) {
                                        editor1.putString(GlabaleParameter.PREFS_ETL_AGENT_MENU_LIST_SHOWMORE, "" + arry[16]);
                                        editor1.putString(GlabaleParameter.PREFS_ETL_AGENT_MENU_LIST_COUNT, "" + arry[19]);
                                    }
                                    if (arry[14].equals("1")) {
                                        editor1.putString(GlabaleParameter.PREFS_ETL_MERCHANT_MENU_LIST_SHOWMORE, "" + arry[17]);
                                        editor1.putString(GlabaleParameter.PREFS_ETL_MERCHANT_MENU_LIST_COUNT, "" + arry[20]);
                                    }
                                    editor1.putString(GlabaleParameter.PREFS_ETL_AUT_LOGOOT, "" + arry[21]);
                                    editor1.putString(GlabaleParameter.PREFS_RESUM_CHECK_BALANCE_AFTER_CHARGE, "0" );

                                    String strToDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
                                    editor1.putString(GlabaleParameter.PREFS_RESUM_LAST_DATE, strToDate);
                                    editor1.putString(GlabaleParameter.PREFS_ETL_REMEMBER_WALLET_NUMBER, strRemember_me);

                                    SharedPreferences pref_p1 = getApplicationContext().getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_PROFILE_STORE, MODE_PRIVATE);
                                    String strProfileIdStore = pref_p1.getString(GlabaleParameter.PREFS_ETL_PROFILE_ID_STORE, "");

                                    //  Log.e("strProfileIdAfterLogin:", arry[8]);
                                    //   Log.e("ProfileIdAfterLoginA:", jObject.getString("profilePhotos"));


                                    if (!strProfileIdStore.equals(strProfileIdAfterLogin)) {
                                        //   Log.e("Pro file AfterLogin:", strProfileIdStore);
                                        try {


                                            //  Log.e("Profile20210305A:", arry[10]+"***");
                                            //   Log.e("Profile20210305B:", decryptPhotoBase64+"***");

                                            SharedPreferences.Editor pref_p2 = getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_PROFILE_STORE, Context.MODE_PRIVATE).edit();
                                            // str_profile_photos_after_login = getResources().getString(R.string.img_take_photos);
                                            pref_p2.putString(GlabaleParameter.PREFS_ETL_PROFILE_PHOTOS_STORE, str_profile_photos_after_login);
                                            pref_p2.putString(GlabaleParameter.PREFS_ETL_PROFILE_ID_STORE, strProfileIdAfterLogin);
                                            pref_p2.apply();
                                            // str_profile_photos_after_login = cryptoHelper.decrypt( arry[10]);
                                        } catch (Exception e) {
                                        }


                                    } else {
                                        // Log.e("Pro file Store:", strProfileIdAfterLogin);
                                    }


                                    editor1.apply();


                                    Loginsuccess();
                                    textOTP.setText("");
                                    strOTP = "0";
                                    textOTP.setVisibility(View.GONE);
                                    TextInputLayoutOTP.setVisibility(View.GONE);
                                }    else {
                                    textOTP.setText("");
                                    strOTP = "0";
                                    textOTP.setVisibility(View.GONE);
                                    TextInputLayoutOTP.setVisibility(View.GONE);
                                    dialog(getString(R.string.str_login_fail_please_check_pass_ornumber), 2);  // 1:Success, 2:Fail, 3:Error

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


                String deviceModel = getInfo();
                // ===== standard info
                IMEI imei = new IMEI();
                String deviceInfo = "";
                int verCode = 0;
                String strjwtToken = null;
                try {
                    deviceInfo = imei.get_dev_id_andSim_only(getApplication());
                    verCode = imei.get_versionCode(getApplication());
                    SharedPreferences sharedJwtTokenKey = getSharedPreferences(GlabaleParameter.PREFS_JWT_TOKEN, Context.MODE_PRIVATE);
                    strjwtToken = sharedJwtTokenKey.getString(GlabaleParameter.PREFS_JWT_TOKEN, null);
                } catch (Exception e) {
                }
                 Log.e("GetLocationabzz:", strGetLocation);
                String strOneSignalPlayerIds = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();
                int osFlage = 1;
                deviceInfo += "~" + strGetLocation + "~" + strOneSignalPlayerIds + "~" + osFlage + "~" + deviceModel;
                //  Log.e("deviceInfo:", deviceInfo);

                String currentDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
                SharedPreferences prefs = getSharedPreferences("CommonPrefs", MODE_PRIVATE);
                String language = prefs.getString("Language", "en");



                SharedPreferences pref_p1 = getApplicationContext().getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_PROFILE_STORE, MODE_PRIVATE);
                String strProfileIdStore = pref_p1.getString(GlabaleParameter.PREFS_ETL_PROFILE_ID_STORE, "");

                SharedPreferences pref = getApplicationContext().getSharedPreferences("SHARED_FINGER", MODE_PRIVATE);
                // String    keyFinger = pref.getString("keyFinger", "");
                // String    sdnFinger = pref.getString("sdnFinger", "");
                //  String    PswFinger = pref.getString("PswFinger", "");
                String strFingerOrPassword = "";
                String loginAppMethod = "";
              String  strmsisdn = txtUserName.getText().toString();
                if (strLoninType.equals("FSW")) {
                    strFingerOrPassword = pref.getString("PswFinger", "");
                    strmsisdn = pref.getString("sdnFinger", "");

                    loginAppMethod = "2";
                }
                if (strLoninType.equals("PSW")) {
                    loginAppMethod = "1";
                    strFingerOrPassword = textPassword.getText().toString();
                }
                String loginType = "1"; // 1 = App , 2 = USSD
                String Standard_info = strmsisdn + "|" + verCode + "|" + currentDate + "|" + deviceInfo + "|" + language + "|" + strFingerOrPassword + "|" + loginAppMethod + "|" + loginType+ "|" + strProfileIdStore+ "|" + textOTP.getText().toString();
                // ===== End standard info

                CryptoHelper cryptoHelper = new CryptoHelper();
                String encryptString = "";
                try {
                    encryptString = cryptoHelper.encrypt(Standard_info);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                params.put("publickey", "CheckActive");
                // params.put("Lo", encryptString);
                params.put("Active_values", encryptString);
                //  Log.e("Active_values", encryptString);
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

//    public void checkAllowPhoneStatePermission() {
//
//
//        // public void checkAllowPhoneStatePermission(View view) {
//        //Check if permission is granted or not
//        if (ContextCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            MarshMallowPermission.requestReadPhoneStatePermission(LoginActivity.this);
//            MarshMallowPermission.requestStoragePermission(LoginActivity.this);
//            //      MarshMallowPermission.requestLocationPermission(LoginActivity.this);
//
////            Get_imei_info get_imei_info = new Get_imei_info();
////            String ime = get_imei_info.get_dev_id_andSim_only(this);
////            Toast.makeText(MainActivity.this, "Permission is already granted 00 Data HERE." + ime, Toast.LENGTH_SHORT).show();
//        } else {
////            Get_imei_info get_imei_info = new Get_imei_info();
////            String ime = get_imei_info.get_dev_id_andSim_only(this);
//            //  Toast.makeText(LoginActivity.this, "Permission is already granted.", Toast.LENGTH_SHORT).show();
//            finish();
//        }
//
//    }

    public void loadLocale() {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", MODE_PRIVATE);
        String language = prefs.getString(langPref, "");
        if (language.equals("")) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(langPref, "en");
            editor.apply();
            language = prefs.getString(langPref, "");
        }
        //  Toast.makeText(this, "You selected country code: " + language, Toast.LENGTH_LONG).show();
        changeLang(language);
    }

    public void changeLang(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        saveLocale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;

        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        //  updateTexts();

        SetTextOnChooseLanguageOnclick();

    }

    public void saveLocale(String lang) {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        //.makeText(this, "You selected country code: " + lang, Toast.LENGTH_LONG).show();

        editor.apply();
       // SetTextOnChooseLanguageOnclick();
        // Change language on click choose

    }

    private void SetTextOnChooseLanguageOnclick() {



//        txtUserName = findViewById(R.id.txtUserName);
//        txtUserName.setHint(R.string.str_user_name);
//
//        textPassword = findViewById(R.id.textPassword);
//        textPassword.setHint(R.string.pass_hint);

        CheckBox chRemember_me =  findViewById(R.id.chRemember_me);
        chRemember_me.setText(R.string.remember_me);


        bt_register = findViewById(R.id.register);
        bt_register.setText(R.string.str_register);

        guestUser.setText(R.string.str_guest_user);
        aboutApp.setText(R.string.str_about_app);

        btn_login = findViewById(R.id.btn_login);

        btn_login.setText(R.string.login_title);


    }

    private void ShowsPopupLanguage() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.cumstom_popup_language);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dialog.setBackgroundDrawable(null);
        // popUp.setBackgroundDrawable(new ColorDrawable(
        //  android.graphics.Color.TRANSPARENT));

        RelativeLayout ReClose = (RelativeLayout) dialog.findViewById(R.id.ReClose);
        LinearLayout Lilo = (LinearLayout) dialog.findViewById(R.id.Lilo);
        LinearLayout Lien = (LinearLayout) dialog.findViewById(R.id.Lien);
        LinearLayout Lizh = (LinearLayout) dialog.findViewById(R.id.Lizh);
        LinearLayout Livi = (LinearLayout) dialog.findViewById(R.id.Livi);

        ReClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Lilo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLocale("lo");
                loadLocale();
                dialog.dismiss();
            }
        });
        Lien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLocale("en");
                loadLocale();
                dialog.dismiss();
            }
        });
        Lizh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLocale("zh");
                loadLocale();
                dialog.dismiss();
            }
        });
        Livi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLocale("vi");
                loadLocale();
                dialog.dismiss();
            }
        });

        dialog.show();
    }






    protected void checkPermission() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.ACCESS_WIFI_STATE,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.CAMERA
        };
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }


    }

    public static boolean hasPermissions(Context context, String... permissions) {

        // Log.e("permissions", "permissions");

        if (context != null && permissions != null) {
            for (String permission : permissions) {

                //    Log.e("permissions loop:", "permissions" + permission);
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


//    protected void checkPermission(){
//        if(ContextCompat.checkSelfPermission(mActivity,Manifest.permission.CAMERA)
//                + ContextCompat.checkSelfPermission(
//                mActivity,Manifest.permission.READ_CONTACTS)
//                + ContextCompat.checkSelfPermission(
//                mActivity,Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED){
//
//            // Do something, when permissions not granted
//            if(ActivityCompat.shouldShowRequestPermissionRationale(
//                    mActivity,Manifest.permission.CAMERA)
//                    || ActivityCompat.shouldShowRequestPermissionRationale(
//                    mActivity,Manifest.permission.READ_CONTACTS)
//                    || ActivityCompat.shouldShowRequestPermissionRationale(
//                    mActivity,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
//                // If we should give explanation of requested permissions
//
//                // Show an alert dialog here with request explanation
//                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
//                builder.setMessage("Camera, Read Contacts and Write External" +
//                        " Storage permissions are required to do the task.");
//                builder.setTitle("Please grant those permissions");
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        ActivityCompat.requestPermissions(
//                                mActivity,
//                                new String[]{
//                                        Manifest.permission.CAMERA,
//                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                                        Manifest.permission.READ_PHONE_STATE
//                                },
//                                MY_PERMISSIONS_REQUEST_CODE
//                        );
//                    }
//                });
//                builder.setNeutralButton("Cancel",null);
//                AlertDialog dialog = builder.create();
//                dialog.show();
//            }else{
//                // Directly request for required permissions, without explanation
//                ActivityCompat.requestPermissions(
//                        mActivity,
//                        new String[]{
//                                Manifest.permission.CAMERA,
//                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                                Manifest.permission.READ_PHONE_STATE
//                        },
//                        MY_PERMISSIONS_REQUEST_CODE
//                );
//            }
//        }else {
//            // Do something, when permissions are already granted
//            Toast.makeText(mContext,"Permissions already granted",Toast.LENGTH_SHORT).show();
//        }
//    }
// //   Manifest.permission.READ_CONTACTS,
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
//        switch (requestCode){
//            case MY_PERMISSIONS_REQUEST_CODE:{
//                // When request is cancelled, the results array are empty
//                if(
//                        (grantResults.length >0) &&
//                                (grantResults[0]
//                                        + grantResults[1]
//                                        + grantResults[2]
//                                        == PackageManager.PERMISSION_GRANTED
//                                )
//                ){
//                    // Permissions are granted
//                    Toast.makeText(mContext,"Permissions granted.",Toast.LENGTH_SHORT).show();
//                }else {
//                    // Permissions are denied
//                    Toast.makeText(mContext,"Permissions denied.",Toast.LENGTH_SHORT).show();
//                }
//                return;
//            }
//        }
//    }
//
//
//


    public void onClickLogin(View v) {
        final String strGetLocation = GetLocation.get(LoginActivity.this);

        // LoginActivity.this.getLocation();


        if (txtUserName.getText().toString().equals("")) {
            txtUserName.setError(getString(R.string.str_please_enter_phone_number));
            return;
        }
        if (txtUserName.getText().toString().trim().startsWith("202") || txtUserName.getText().toString().trim().startsWith("302")) {
        } else {
            txtUserName.requestFocus();
            txtUserName.setError(getString(R.string.str_phone_number_is_invalide));
            return;
        }
        if (txtUserName.getText().toString().length() < 9) {
            txtUserName.requestFocus();
            txtUserName.setError(getString(R.string.str_phone_number_is_invalide));
            return;
        }
        if (textPassword.getText().toString().equals("")) {
            textPassword.setError(getString(R.string.str_The_Password_must_be_6_characters_long));
            return;
        }
        if (strOTP=="1"){
            if (textOTP.getText().toString().equals("")) {
                textOTP.setError(getString(R.string.str_enter_opt));
                return;
            }
        }



        //if (textPassword.getText().toString().length()<9) { txtUserName.requestFocus() ;txtUserName.setError(getString(R.string.str_phone_number_is_invalide));return;}
        Login("PSW");


    }

    private String getInfo() {
        try {
            // int detailsInfo = Build.VERSION.SDK_INT;

            String details = Build.BRAND + "," + Build.MANUFACTURER + "," + Build.MODEL + "," + Build.VERSION.RELEASE;


            return details;
        } catch (Exception e) {
            return "";
        }

//                +"\nCPU_ABI : "+Build.CPU_ABI
//                +"\nCPU_ABI2 : "+Build.CPU_ABI2
//                +"\nDISPLAY : "+Build.DISPLAY
//                +"\nFINGERPRINT : "+Build.FINGERPRINT
//                +"\nHARDWARE : "+Build.HARDWARE
//                +"\nHOST : "+Build.HOST
//                +"\nID : "+Build.ID
//                +"\nMANUFACTURER : "+Build.MANUFACTURER
//                +"\nMODEL : "+Build.MODEL
//                +"\nPRODUCT : "+Build.PRODUCT
//                +"\nSERIAL : "+Build.SERIAL
//                +"\nTAGS : "+Build.TAGS
//                +"\nTIME : "+Build.TIME
//                +"\nTYPE : "+Build.TYPE
//                +"\nUNKNOWN : "+Build.UNKNOWN
//                        +"\nHARDWARE : "+Build.HARDWARE
//                +"\nUSER : "+Build.USER;


//                String  details =  "VERSION.RELEASE : "+Build.VERSION.RELEASE
//                +"\nVERSION.INCREMENTAL : "+Build.VERSION.INCREMENTAL
//                +"\nVERSION.SDK.NUMBER : "+Build.VERSION.SDK_INT
//                +"\nBOARD : "+Build.BOARD
//                +"\nBOOTLOADER : "+Build.BOOTLOADER
//                +"\nBRAND : "+Build.BRAND
//                +"\nCPU_ABI : "+Build.CPU_ABI
//                +"\nCPU_ABI2 : "+Build.CPU_ABI2
//                +"\nDISPLAY : "+Build.DISPLAY
//                +"\nFINGERPRINT : "+Build.FINGERPRINT
//                +"\nHARDWARE : "+Build.HARDWARE
//                +"\nHOST : "+Build.HOST
//                +"\nID : "+Build.ID
//                +"\nMANUFACTURER : "+Build.MANUFACTURER
//                +"\nMODEL : "+Build.MODEL
//                +"\nPRODUCT : "+Build.PRODUCT
//                +"\nSERIAL : "+Build.SERIAL
//                +"\nTAGS : "+Build.TAGS
//                +"\nTIME : "+Build.TIME
//                +"\nTYPE : "+Build.TYPE
//                +"\nUNKNOWN : "+Build.UNKNOWN
//                +"\nUSER : "+Build.USER;

        //   Log.e("Device Details",details);

    }


    private void checkFinger() {
        btnUseFinger.setVisibility(View.GONE);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SHARED_FINGER", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String keyFinger = pref.getString("keyFinger", "");
        String sdnFinger = pref.getString("sdnFinger", "");
        //  String    PswFinger = pref.getString("PswFinger", "");

        if (sdnFinger.length() > 8) {
            if (keyFinger.length() > 1) {
                authenticateUser(keyFinger, sdnFinger);
                btnUseFinger.setVisibility(View.VISIBLE );
            }
        }
    }

    private void authenticateUser(String keyFinger, String sdnFinger) {
        BiometricPrompt biometricPrompt = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            biometricPrompt = new BiometricPrompt.Builder(this)
                    .setTitle(getString(R.string.str_login_with_fingerprint))
                    .setSubtitle(getString(R.string.str_your_account_number_is) + " " + sdnFinger)
                    .setDescription(getString(R.string.str_please_verify_your_fingerprint_to_login))
                    .setNegativeButton(getString(R.string.str_cancel), this.getMainExecutor(),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    notifyUser("Authentication cancelled");
                                }
                            })
                    .build();

            biometricPrompt.authenticate(getCancellationSignal(), getMainExecutor(),
                    getAuthenticationCallback(keyFinger));
        }
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

    @RequiresApi(api = Build.VERSION_CODES.P)
    private BiometricPrompt.AuthenticationCallback getAuthenticationCallback(final String keyFinger) {

        return new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              CharSequence errString) {
                notifyUser("Authentication error: " + errString);
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                super.onAuthenticationHelp(helpCode, helpString);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }

            @Override
            public void onAuthenticationSucceeded(
                BiometricPrompt.AuthenticationResult result) {
                String FKLastGet = FKLastGet = fingerw.g();
                FKLastGet = fingerw.g();
                FKLastGet = fingerw.g();
                if (keyFinger.equals(FKLastGet)) {
                    Login("FSW");
                } else {
                    notifyUser(getString(R.string.str_Cannot_recognize_your_finger));
                }
                //  notifyUser("Authentication Succeeded");
                super.onAuthenticationSucceeded(result);
            }
        };
    }

    private void notifyUser(String message) {
        Toast.makeText(this,
                message,
                Toast.LENGTH_LONG).show();
    }




    public void onClickRegister(View view) {

        Intent intent = new Intent(this, PolicyActivity.class);
        startActivity(intent);
    }
    public void onClickResetPassword(View v) {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        startActivity(intent);
    }

}