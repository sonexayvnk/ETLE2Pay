package com.etl.money.setting;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import com.etl.money.devise_info.IMEI;
import com.etl.money.global.GlabaleParameter;
import com.etl.money.security.CSSLSocketFactory;
import com.etl.money.security.CryptoHelper;
import com.etl.money.security.GetLocation;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MerchantBalanceInfoActivity extends AppCompatActivity {
String strMsisdn ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_info);
        SharedPreferences msn = getSharedPreferences(GlabaleParameter.PREFS_MSISDN, Context.MODE_PRIVATE);
        strMsisdn = msn.getString("msisdn", "");
        refresh( );
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
            String ActivityKey ;
            Intent intent = getIntent();
            ActivityKey = intent.getStringExtra("ActivityKey");
            getSupportActionBar().setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.actionbar_layout_title_center);
            TextView mytext = (TextView) findViewById(R.id.txtcenter);
            mytext.setText( getString(this, "str_"+ActivityKey) );

        } catch (Exception e) {
        }
        return true;
    }

    public static String getString(Context context, String idName) {
        Resources res = context.getResources();
        String str =  idName;
        try {
            str =  res.getString(res.getIdentifier(idName, "string", context.getPackageName()));
        } catch (Exception e) {
        }
        return str;
    }
    public void refresh( ){
        final String  strGetLocation =    GetLocation.get(MerchantBalanceInfoActivity.this);
        final Activity act = MerchantBalanceInfoActivity.this ;
        final ProgressDialog progress_sub = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        progress_sub.setMessage(getString(R.string.str_pleasewait_inprocess));
        progress_sub.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress_sub.show();
        String url = getString(R.string.str_url_https) + "WalletMerchantQueryBalance.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Log.e("response refresh:", response);
                        try {
                            JSONObject jObject = new JSONObject(response);
                            if (jObject.getString("status").equals("OK")) {
                           //     Log.e("Decrpt23:", jObject.getString("result"));
                                CryptoHelper cryptoHelper = new CryptoHelper();
                                String result = "";
                                try {
                                    result =  cryptoHelper.decrypt(jObject.getString("result"));
                                } catch (Exception e) {
                                }
                              //  Log.e("Decrpt5:",result);
                                String[] arry = result.split("\\|");

                                if (arry[0].equals("405000000")){

                                    if (strMsisdn.equals(arry[1])){
                                        LoadBalance(result);
                                    }
                                }

                                else {
                                    //  dialog(result,2);  // 1:Success, 2:Fail, 3:Error
                                    //dialog(getString(R.string.str_error_operation_fail93),2);  // 1:Success, 2:Fail, 3:Error
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
                        Toast.makeText(getApplication(), getString(R.string.str_can_not_connect_to_server)+"=="+error, Toast.LENGTH_LONG).show();
                        Log.e("error:", ""+error);
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
                String strBasic_info   = strMsisdn+"|"+verCode+"|"+currentDate+"|"+Strlanguage+"|"+StrdeviceInfo+"|"+Strtoken+"|"+StrjwtToken;
                // ===== End StrBasic info

                CryptoHelper cryptoHelper = new CryptoHelper();
                String encryptString = "";
                try {
                    encryptString =  cryptoHelper.encrypt(strBasic_info);
                } catch (Exception e) {
                }
                params.put("publickey", "CheckActive");
                params.put("Active_values", encryptString);
                Log.e("Active_valuesQ22:", ""+encryptString);
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
    private void LoadBalance(String Str){
       String[] arry = Str.split("\\|");



        //String[] arryData = Str.split("\\|");
        // listView1
        final ListView lisView1 = (ListView)findViewById(R.id.listView1);
        ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;
       /* for (String str : arryData) {
            String[] ArrB = str.split( ";" );
            map = new HashMap<String, String>();
            map.put("Title", ArrB[2]+":");
            map.put("Description", ArrB[3]);
            MyArrList.add(map);
        }
        */
        String strMainBalance = " "+ new DecimalFormat("#,###,###").format(Integer.parseInt(arry[2] + "")) + " " + getResources().getString(R.string.str_kip);
        String strSubBalance = " "+ new DecimalFormat("#,###,###").format(Integer.parseInt(arry[3] + "")) + " " + getResources().getString(R.string.str_kip);
        //=== 1
        map = new HashMap<String, String>();
        map.put("Title",getResources().getString(R.string.str_wallet_number)+":");
        map.put("Description", strMsisdn);
        MyArrList.add(map);
      //=== 2
        map = new HashMap<String, String>();
        map.put("Title",getResources().getString(R.string.str_balance)+":");
        map.put("Description", strMainBalance);
        MyArrList.add(map);

        // ===
        SimpleAdapter sAdap;
        sAdap = new SimpleAdapter(MerchantBalanceInfoActivity.this, MyArrList, R.layout.wallet_info_column,
                new String[] {"Title", "Description"}, new int[] {R.id.txtTitle, R.id.txtDescription});
        lisView1.setAdapter(sAdap);








        Log.e("StrVerifyTypeaa22:", Str);


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