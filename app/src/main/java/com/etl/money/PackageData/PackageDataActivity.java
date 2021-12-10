package com.etl.money.PackageData;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PackageDataActivity extends AppCompatActivity {
    TextView mytext;
    String StrVerifyType = "0";
    final Context context = this;
    private static ProgressDialog mProgressDialog;
    ArrayList<DataModel> dataModelArrayList;
    private DataAdapter dataAdapter;
    private RecyclerView recyclerView;
    TextView not_available;
    TextView txtDestination_number, txtSubscriberType, txtNumberStatus, txtNumberType ;
    String mobileEn = "";
    String StrDestination_number = "";
    String StrUseVerifyCode = "0";
    String MobileNumber = "" ;
    String StrVerifyCodePopupShow = "0" ;
    String StrPackageName = "" ;
    TextView txtPackageName ;
    String stusNumber = "" ;
    String strMsisdn ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_data);
        stusNumber = "";
        StrDestination_number = "";
        StrUseVerifyCode = "0";
        txtDestination_number = (TextView)findViewById(R.id.txtDestination_number);
        txtSubscriberType = (TextView)findViewById(R.id.txtSubscriberType);
        txtNumberStatus = (TextView)findViewById(R.id.txtNumberStatus);
        txtNumberType = (TextView)findViewById(R.id.txtNumberType);
        recyclerView = findViewById(R.id.recycler);
        SharedPreferences msn = getSharedPreferences(GlabaleParameter.PREFS_MSISDN, Context.MODE_PRIVATE);
        txtDestination_number.setText(msn.getString("msisdn", ""));
        strMsisdn = msn.getString("msisdn", "") ;

        StrVerifyCodePopupShow = "0" ;
        StrPackageName = "";
        not_available = findViewById(R.id.tv_cannotuseService);
        SharedPreferences pref2 = getApplicationContext().getSharedPreferences("SubTypePreOrPos", MODE_PRIVATE);
        String PreOrPos =  pref2.getString("PreOrPos", "0");          // getting String
        StrUseVerifyCode = PreOrPos;

        not_available.setText("");
        if (not_available.getVisibility() == View.VISIBLE)
            not_available.setVisibility(View.INVISIBLE);
        else{
            not_available.setVisibility(View.VISIBLE);
        }


        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.actionbar_layout_title_center);
            mytext = (TextView) findViewById(R.id.txtcenter);

            mytext.setGravity(Gravity.CENTER | Gravity.BOTTOM);
            mytext.setText(R.string.str_buy_data_for_other_number_title);

            // mytext.setTextSize(R.dimen.text_size_normall);
        } catch (Exception e) {
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_icon_close, menu);
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.actionbar_layout_title_center);
            TextView mytext = (TextView) findViewById(R.id.txtcenter);
            mytext.setText(R.string.str_buy_package_data);
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


    private void setupRecycler() {
        dataAdapter = new DataAdapter(this, dataModelArrayList);
        recyclerView.setAdapter(dataAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
    }
    public static void removeSimpleProgressDialog() {
        try {
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }
        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();

        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




    public void LoginigFail(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void dialogSysError(String msg) {
        //String ms ="Sorry, System error, pls contact call center 135, thank you.";
        new AlertDialog.Builder(this)
                //  .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.app_name)
                .setMessage(msg)
                .setPositiveButton(R.string.str_ok_only, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }

                })
                // .setNegativeButton(R.string.str_no_to_exit, null)
                .show();
    }


    private void ForceLogout() {
        String displayPuop = getString(R.string.str_wifi_reach_amt);
        new AlertDialog.Builder(this)
                // .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.app_name)
//                .setTitle(R.string.title_confirm_to_exit)
                // .setMessage((this.getString(R.string.str_confirm_to_Topup)) + "" + msdn + " ?")
                .setMessage((displayPuop))
                .setPositiveButton(R.string.str_ok_only, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // SharedPreferences msne = getSharedPreferences(GlabaleParameter.PREFS_MSISDN, Context.MODE_PRIVATE);
                        //final String msdnd = msne.getString("msisdn", "");
                        SharedPreferences setting_logout_session = getSharedPreferences(GlabaleParameter.PREFS_SESSION, MODE_PRIVATE);
                        SharedPreferences.Editor edt_session = setting_logout_session.edit();
                        edt_session.putString(GlabaleParameter.PREFS_SESSION, "0");
                        edt_session.commit();
                        //Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        //startActivity(intent);
                        // finish();
                        // System.exit(0);
                        // -------------
                        return;
                    }
                })
                // .setNegativeButton(R.string.str_no_to_topup, null)
                .show();
        return;
    }

    public void sendSMS(String phoneNo, String msg) {
//        try {
//            SmsManager smsManager = SmsManager.getDefault();
//            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
//            Toast.makeText(getApplicationContext(), "Message Sent",
//                    Toast.LENGTH_LONG).show();
//        } catch (Exception ex) {
//            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
//                    Toast.LENGTH_LONG).show();
//            ex.printStackTrace();
//        }
    }


    private void eroorCheckPackage() {
        String displayPuop = getString(R.string.str_error_check_package);
        new AlertDialog.Builder(this)
                //  .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.app_name))
                // .setMessage((this.getString(R.string.str_confirm_to_Topup)) + "" + msdn + " ?")
                .setMessage((displayPuop))
                .setPositiveButton(R.string.str_ok_only, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // SharedPreferences msne = getSharedPreferences(GlabaleParameter.PREFS_MSISDN, Context.MODE_PRIVATE);
                        //final String msdnd = msne.getString("msisdn", "");
                        SharedPreferences setting_logout_session = getSharedPreferences(GlabaleParameter.PREFS_SESSION, MODE_PRIVATE);
                        SharedPreferences.Editor edt_session = setting_logout_session.edit();
                        edt_session.putString(GlabaleParameter.PREFS_SESSION, "0");
                        edt_session.commit();
                        //Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        //startActivity(intent);
                        // finish();
                        // System.exit(0);

                        // -------------
                        return;
                    }
                })
                // .setNegativeButton(R.string.str_no_to_topup, null)
                .show();
        return;
    }


    // connected internet

    private void PopUpVerifyCode2(String packageName) {

        final   PopupWindow popup;
        View popupContent = getLayoutInflater().inflate(R.layout.verify_code_popup, null);
        popup = new PopupWindow();

        //popup should wrap content view
        popup.setWindowLayoutMode(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setHeight(250);
        popup.setWidth(350);
        //set content and background
        popup.setContentView(popupContent);
        ///popup.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_background));
        popupContent.findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                popup.dismiss();
            }
        });
        // popup.setTouchInterceptor(this);
        popup.setFocusable(true);
        popup.setOutsideTouchable(true);
    }


    private void dialog(String Str ,int ico) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Str);
        builder.setCancelable(false)
                .setPositiveButton(R.string.str_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
        AlertDialog alert = builder.create();
        alert.setTitle(R.string.str_etlL_Services);
        // alert.setIcon(R.drawable.ic_success);
        /*
        if (ico==1){ // Success
            alert.setTitle(R.string.str_successful);
            alert.setIcon(R.drawable.ic_success);
        }else if  (ico==2){ //Fail
            alert.setTitle(R.string.str_failed);
            alert.setIcon(R.drawable.ic_warning);
        }else if  (ico==3){ // Error
            alert.setTitle(R.string.str_error);
            alert.setIcon(R.drawable.ic_error);
        }
        */
        alert.show();
    }
    public void onClickPackageDataOtherNumber(View v) {
        // Log.e("mobileEn=", mobileEn);
        // customer_list_package_request(mobileEn);
        String strDestination_number = txtDestination_number.getText().toString();
        //if ( strMobileNumber.length()<8) {txtDestNum.requestFocus();txtDestNum.setError(getString(R.string.str_phone_number_is_invalide));return;}
        if (strDestination_number.trim().startsWith("202") || strDestination_number.trim().startsWith("302")){} else { txtDestination_number.requestFocus() ;txtDestination_number.setError(getString(R.string.str_buy_data_for_other_not_start_in_correct_fomat));return;}
        if (strDestination_number.length()<9) { txtDestination_number.requestFocus() ;txtDestination_number.setError(getString(R.string.str_phone_number_is_invalide));return;}
        SharedPreferences sharedJwtTokenKey = getSharedPreferences(GlabaleParameter.PREFS_JWT_TOKEN, Context.MODE_PRIVATE);
        String strjwtToken = sharedJwtTokenKey.getString(GlabaleParameter.PREFS_JWT_TOKEN, null);
        String msdn = null;



        fetchingJSON(strDestination_number);
    }
    private void customer_buy_package_request(final String strPackageNameApply, final String strDestination_numberApply, final String strVerifyCodeApply) {
        final String  strGetLocation =    GetLocation.get(PackageDataActivity.this);
        //  String msisdnAndAdslEncrypt = source_msdn+"|"+amtPay+"|"+adsl_id;
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle(getString(R.string.app_name));
        progress.setMessage(getString(R.string.str_pleasewait_inprocess));
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        String url = getString(R.string.str_url_https) + "WalletPackageData.php";
        //new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log.e("response", response);
                        try {
                            //แปลงผลลัพธ์ที่ได้มาเป็น JSON Object
                            JSONObject jObject = new JSONObject(response);
                            // Log.e("onPostExecute",jObject.getString("status"));
                            if (jObject.getString("status").equals("OK")) {
                                String result = jObject.getString("result");
                                //  Log.e("result2:------",result);

                                CryptoHelper cryptoHelper = new CryptoHelper();
                                try {
                                    result =  cryptoHelper.decrypt(result);
                                } catch (Exception e) {
                                }


                                //Log.e("decry Res:-------------", decrypted);
                                String[] parts = result.split("\\|");
                                String resultCode = parts[0];
                                // 200= success, 250= Can not deudct balance, 300= have pacakge,
                                if (resultCode.equals("405000000")) {
                                    // dialogPaySuccess();
                                    StrVerifyCodePopupShow = "0" ;

                                    StrVerifyCodePopupShow = "1" ;

                                    String msg = getString(R.string.str_package_success);
                                    dialog(msg,1);
                                } else {
                                    String msg = getString(R.string.str_package_success);
                                    dialog( parts[1],2);
                                }
                                //1001ถ้าดึงข้อมูลจาก database มีปัญหาจะแสดง error
                            } else {
                                // dialogFail();

                                // ((ActivityHotspotUser)context).cannotConnectToServer_wifi();
                            }
                            // dialogConnect_user_info.dismiss();
                            // ((MainActivity)context).setList(list);
                            //ถ้าขณะแปลงข้อมูล JSON มีปัญหาจะมาทำงานส่วนนี้
                        } catch (JSONException e) {
                            //Log.e("ConnectServer3", "Error parsing data " + e.toString());
                            // ((ActivityHotspotUser)context).errorConnectToServer_wifi();
                            // Toast.makeText(UserInfoActivity.this, "", Toast.LENGTH_LONG).show();
                            //  Toast.makeText(InternetAdslPaymentActivity.this, getString(R.string.str_can_not_connect_to_server), Toast.LENGTH_LONG).show();
                        }
                        progress.dismiss();

                        // Log.e("response",response);
                        //Toast.makeText(UserInfoActivity.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  Toast.makeText(InternetAdslPaymentActivity.this, getString(R.string.str_can_not_connect_to_server), Toast.LENGTH_LONG).show();
                        progress.dismiss();
                        // Log.e("", error.toString());
                        //Toast.makeText(UserInfoActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {


                IMEI imei = new IMEI();
                int verCode = imei.get_versionCode(getApplication());
                String currentDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
                SharedPreferences pref = getApplicationContext().getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, MODE_PRIVATE);
                String Strlanguage     = pref.getString(GlabaleParameter.PREFS_ETL_LANGGAUGE, "en");
                String StrdeviceInfo   = pref.getString(GlabaleParameter.PREFS_ETL_DEVICEINFO, null);
                String Strtoken        = pref.getString(GlabaleParameter.PREFS_ETL_TOKEN_WALLET_API, null);
                String StrjwtToken     = pref.getString(GlabaleParameter.PREFS_JWT_TOKEN, null);
                StrdeviceInfo  +="~"+ strGetLocation;
                String strBasic_info   = strMsisdn+"|"+verCode+"|"+currentDate+"|"+Strlanguage+"|"+StrdeviceInfo+"|"+Strtoken+"|"+StrjwtToken;
                // ===== End StrBasic info

                SharedPreferences prefpaymentType = getApplicationContext().getSharedPreferences(GlabaleParameter.PAYMENT_TPYE, MODE_PRIVATE);
                String  paymentType =  prefpaymentType.getString(GlabaleParameter.PREFS_PAYMENT_TPYE, "1");
                strBasic_info +=  "|"+paymentType+"|"+strPackageNameApply+"|"+strDestination_numberApply+"|"+strVerifyCodeApply;
                CryptoHelper cryptoHelper = new CryptoHelper();
                String encryptString = "";
                try {
                    encryptString =  cryptoHelper.encrypt(strBasic_info);
                } catch (Exception e) {
                }

                //Log.e("finalPackage_numEn", finalPackage_numEn);
                //   Log.e("number", finalMobileEn);
                //   Log.e("verCode", String.valueOf(verCode));
                Map<String, String> params = new HashMap<String, String>();
                params.put("publickey","Apply");
                params.put("Active_values", encryptString);
                Log.e("Active_valuesApply1", strBasic_info);
                Log.e("Active_valuesApply2", encryptString);
                //params.put("versionCRequestQueue requestQueue = Volley.newRequestQueue(this, new HurlStack(null, getSocketFactory()));ode", String.valueOf(verCode));
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));  // set DefaultRetryPolicy.DEFAULT_MAX_RETRIES =0 no retry
        //= 0 mean retry connect 1 times only
        // stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this, new HurlStack(null, CSSLSocketFactory.getSocketFactory(this)));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            //  Toast.makeText(InternetAdslPaymentActivity.this, getString(R.string.str_can_not_connect_to_server), Toast.LENGTH_LONG).show();
            //Log.e("", e.toString());
        }

    }

    private void fetchingJSON(final String Destination) {
        final String  strGetLocation =    GetLocation.get(PackageDataActivity.this);
        // package_date_loading.php
        // StrDestination_number = loadvalues ;
        String msdn = null;

        String url = getString(R.string.str_url_https) + "WalletPackageData.php";
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle(getString(R.string.app_name));
        progress.setMessage(getString(R.string.str_pleasewait_inprocess));
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
//        showSimpleProgressDialog(this, "Loading...", "Fetching Json", false);
        //     Log.e("URLstring", ">>" + url);
        final String finalMsdn = msdn;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response=", ">>" + response);
                        try {

                            removeSimpleProgressDialog();
                            JSONObject obj = new JSONObject(response);
                            if (obj.optString("status").equals("OK")) {

                                LinearLayout remark ;
                                remark = (LinearLayout)findViewById(R.id.remark);

                                remark.setVisibility(View.GONE);

                                dataModelArrayList = new ArrayList<>();
                                String resultStr = obj.getString("result");
                                CryptoHelper cryptoHelper = new CryptoHelper();
                                String result = "";
                                try {
                                    result =  cryptoHelper.decrypt(resultStr);
                                } catch (Exception e) {
                                }
                                Log.e("decryptedaa", result);
                                String[] partsOfStr = result.split("\\|");
                                String mobile_snd;
                                String resultCode = "";

                                StrVerifyCodePopupShow = "0" ;
                                StrPackageName = "";
                                resultCode = partsOfStr[0];
                                mobile_snd = partsOfStr[1];

                                // StrUseVerifyCode = partsOfStr[6] ;

                                if(resultCode.equals("405000000")){

                                    StrDestination_number = partsOfStr[2];
                                    String  StrSubscriberType = partsOfStr[3];
                                    stusNumber = partsOfStr[4] ;
                                    String StrNumberType = partsOfStr[5] ;

                                    txtDestination_number.setText(StrDestination_number);
                                    if(stusNumber.equals("1001")){
                                        txtNumberStatus.setText(R.string.str_mobile_status_os_active);
                                    }else if ((stusNumber.equals("1011")) || (stusNumber.equals("1003"))){
                                        txtNumberStatus.setText(R.string.str_mobile_status_os_suspend);
                                    }else if (stusNumber.equals("1000" )){ // Idle
                                        txtNumberStatus.setText(R.string.str_mobile_status_1000_idle);
                                    }else if (stusNumber.equals("1002" )){ // Idle
                                        txtNumberStatus.setText(R.string.str_mobile_status_1002_suspend_requestrequest);
                                    }else if (stusNumber.equals("1004" )){ // Idle
                                        txtNumberStatus.setText(R.string.str_mobile_status_1004_Suspend_fraud);
                                    }else if (stusNumber.equals("1005" )){ // Idle
                                        txtNumberStatus.setText(R.string.str_mobile_status_1005_SuspendCreditLimit);
                                    }else if ((stusNumber.equals("1007")) || (stusNumber.equals("1006"))){ // Idle
                                        txtNumberStatus.setText(R.string.str_mobile_status_1006_1007_Disable);
                                    }else if (stusNumber.equals("1008" )){ // Idle
                                        txtNumberStatus.setText(R.string.str_mobile_status_1008_closed);
                                    }else if ((stusNumber.equals("1008")) || (stusNumber.equals("1010"))){ // Idle
                                        txtNumberStatus.setText(R.string.str_mobile_status_1008_closed);
                                    }else if (stusNumber.equals("1012" )){ // Idle
                                        txtNumberStatus.setText(R.string.str_mobile_status_1012_Recharge_Lock);
                                    }

                                    if (StrNumberType.equals("1")){
                                        txtNumberType.setText(R.string.str_postpaid);

                                    }else  {
                                        txtNumberType.setText(R.string.str_prepaid);
                                    }
                                    txtSubscriberType.setText(StrSubscriberType);

                                    // CryptoHelper cryptoHelper = new CryptoHelper();
                                    String strData =  obj.getString("packageDetail");

                                    String decryptData =  "";

                                    try {

                                        decryptData =  cryptoHelper.decrypt(strData);
                                    } catch (Exception e) {
                                    }

                                    String[] arry = decryptData.split("\\|");

                                    String  str_buy_via_ussd = getString(R.string.str_buy_via_ussd) ;
                                    String  str_day = getString(R.string.str_day) ;
                                    String  str_price = getString(R.string.str_price) ;
                                    String  str_kip = getString(R.string.str_kip) ;

                                    String  str_dia = getString(R.string.str_dia) ;


                                    for (String str : arry) {
                                        String[] arryB = str.split(";");
                                        DataModel playerModel = new DataModel();

                                        String strprice = arryB[2]+"/"+arryB[1]+ " " + str_day + ", " + new DecimalFormat("#,###,###").format(Integer.parseInt(arryB[3]+"")) + " " + str_kip ;
                                        // String strDial = arryB[2]+"/"+arryB[1]+ " " + str_day + "," + new DecimalFormat("#,###,###").format(Integer.parseInt(arry[3]+"")) + " " + str_kip ;
                                        String strDial = str_buy_via_ussd + " " +  arryB[4]  + " " + str_dia ;

                                        playerModel.setPackage_name(arryB[0]);
                                        playerModel.setData_price(strprice);
                                        playerModel.setPackage_more_detail(strDial);

                                        // playerModel.setImgURL(dataobj.getString("imgURL"));
                                        // playerModel.setImghit(dataobj.getString("imgURLHit"));
                                        //   playerModel.setImghit("newlogo");
                                        dataModelArrayList.add(playerModel);
                                    }

                                    setupRecycler();


                                    progress.dismiss();
                                }else {
                                    Toast.makeText(PackageDataActivity.this, getString(R.string.str_package_not_aviable), Toast.LENGTH_LONG).show();
                                    progress.dismiss();
                                    return;
                                }
                            } else if (obj.optString("status").equals("NOTHING")) {
                                Toast.makeText(PackageDataActivity.this, getString(R.string.str_package_not_aviable), Toast.LENGTH_LONG).show();
                                progress.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PackageDataActivity.this, getString(R.string.str_can_not_connect_to_server), Toast.LENGTH_LONG).show();
                        progress.dismiss();
                        // Log.e("", error.toString());
                        //Toast.makeText(UserInfoActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
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
                String strBasic_info   = strMsisdn+"|"+verCode+"|"+currentDate+"|"+Strlanguage+"|"+StrdeviceInfo+"|"+Strtoken+"|"+StrjwtToken;
                // ===== End StrBasic info
                strBasic_info   += "|"+Destination;
                //          Log.e("finalMobileEn=",finalMobileEn);

                CryptoHelper cryptoHelper = new CryptoHelper();
                String encryptString = "";
                try {
                    encryptString =  cryptoHelper.encrypt(strBasic_info);
                } catch (Exception e) {
                }

                Map<String, String> params = new HashMap<String, String>();
                params.put("publickey","CheckActive");
                params.put("Active_values", encryptString);
                Log.e("Active_valuesDataList1=",strBasic_info);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));  // set DefaultRetryPolicy.DEFAULT_MAX_RETRIES =0 no retry
        try {
            //   RequestQueue requestQueue = Volley.newRequestQueue(this);
            RequestQueue requestQueue = Volley.newRequestQueue(this, new HurlStack(null, CSSLSocketFactory.getSocketFactory(this)));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            Toast.makeText(PackageDataActivity.this, getString(R.string.str_can_not_connect_to_server), Toast.LENGTH_LONG).show();
            //Log.e("", e.toString());
        }
    }

    public void BuyPackageAlert(final String packageName) {
        // Log.e("TOKEN=:", stusNumber);
        if (!stusNumber.equals("1001")){
            dialog(getString(R.string.sory_can_not_subscribe_for_number_status)+""+txtNumberStatus.getText().toString(),1);
            return;
        }
        SharedPreferences pref = getApplicationContext().getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, MODE_PRIVATE);
        StrVerifyType     = pref.getString(GlabaleParameter.PREFS_ETL_VERIFY_TYPE, "0");
        if (StrVerifyType.equals("1")){ // OTP
            if (StrVerifyCodePopupShow.equals("1")) {
                PopUpVerifyCode(StrPackageName);
            } else {
                String str_ms = getString(R.string.str_you_would_like_to_buy) + " " + packageName + " " + getString(R.string.str_to_number) + " " + StrDestination_number + getString(R.string.str_really);
                new AlertDialog.Builder(this)
//                .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.app_name)
                        .setMessage(str_ms)
                        .setPositiveButton(R.string.str_yes_to_exit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SentToSMS(StrDestination_number, packageName);
                            }
                        })
                        .setNegativeButton(R.string.str_no_to_exit, null)
                        .show();
            }
        } else {  // Password

            PopUpVerifyCode(packageName);
        }
    }
    private void SentToSMS(final String txtDestNum,final String packageName){
        final String  strGetLocation =    GetLocation.get(PackageDataActivity.this);
        final ProgressDialog progress_sub = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        progress_sub.setMessage(getString(R.string.str_pleasewait_inprocess));
        progress_sub.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress_sub.show();
        String url = getString(R.string.str_url_https) + "WalletPackageData.php";
        // Log.e("url_info:", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("onResponse:", response);
                        try {
                            JSONObject jObject = new JSONObject(response);
                            if (jObject.getString("status").equals("OK")) {
                                /// Log.e("Decrpt:", EnDecry.de(jObject.getString("result"),act));
                                CryptoHelper cryptoHelper = new CryptoHelper();
                                String result = "";
                                try {
                                    result =  cryptoHelper.decrypt(jObject.getString("result"));
                                } catch (Exception e) {
                                }
                                Log.e("result2:", result);
                                String[] arry = result.split("\\|");
                                if (strMsisdn.equals(arry[1])){
                                    if (arry[0].equals("405000000")){


                                        StrVerifyCodePopupShow = "1" ;
                                        StrPackageName = arry[2];

                                        PopUpVerifyCode(StrPackageName);

                                    } else{
                                        dialog(arry[1],2);  // 1:Success, 2:Fail, 3:Error
                                    }
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
                StrdeviceInfo   +="~"+ strGetLocation;
                String strBasic_info   = strMsisdn+"|"+verCode+"|"+currentDate+"|"+Strlanguage+"|"+StrdeviceInfo+"|"+Strtoken+"|"+StrjwtToken;
                // ===== End StrBasic info

                SharedPreferences prefpaymentType = getApplicationContext().getSharedPreferences(GlabaleParameter.PAYMENT_TPYE, MODE_PRIVATE);
                String  paymentType =  prefpaymentType.getString(GlabaleParameter.PREFS_PAYMENT_TPYE, "1");
                strBasic_info +=  "|"+packageName+"|"+txtDestNum;

                //          Log.e("finalMobileEn=",finalMobileEn);

                CryptoHelper cryptoHelper = new CryptoHelper();
                String encryptString = "";
                try {
                    encryptString =  cryptoHelper.encrypt(strBasic_info);
                } catch (Exception e) {
                }


                params.put("publickey","SentSMS");
                params.put("Active_values", encryptString);
                Log.e("Active_valuesSentSMS1=",encryptString);
                Log.e("Active_valuesSentSMS2=",strBasic_info);
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
    private void PopUpVerifyCode(String packageName) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.verify_code_popup);

        txtPackageName = (TextView) dialog.findViewById(R.id.txtPackageName);
        // TextView txtID = (TextView) dialog.findViewById(R.id.txtVerifyCode);
        final   TextView txtVerifyCode = (EditText) dialog.findViewById(R.id.txtVerifyCode);
        Button btnClose = (Button) dialog.findViewById(R.id.btnClose);
        txtPackageName.setText(packageName);
        if (StrVerifyType.equals("1")) { // OTP
            txtVerifyCode.setHint(getString(R.string.str_otp));
        }else{
            txtVerifyCode.setHint(getString(R.string.pass_hint));
        }
        //==============
        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // BuyPackage( txtDestination_number.getText().toString(),  txtPackageName.getText().toString());
                customer_buy_package_request(StrPackageName,StrDestination_number,txtVerifyCode.getText().toString());
                dialog.dismiss();
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrVerifyCodePopupShow = "0";
                StrPackageName="";

                dialog.dismiss();
            }
        });


        dialog.show();
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
