package com.etl.money.promotion_and_advertising;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
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
import com.etl.money.LoginActivity;
import com.etl.money.R;
import com.etl.money.config.AutoLogout;
import com.etl.money.devise_info.IMEI;
import com.etl.money.global.GlabaleParameter;
import com.etl.money.register.RegisterNewAgentActivity;
import com.etl.money.security.CryptoHelper;
import com.etl.money.security.GetLocation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class PromotionActivity extends AppCompatActivity {
    String StrMobileNumber ;
    private PromotionAdapter HistoryAdapter;
    Context context;
    LayoutInflater layoutinflater;
    TextView txtloading;
    android.widget.ProgressBar ProgressBar;
    String language;
    public static String loadingstatus = "";
    public boolean loadingMore = false;
    String StrLastMonthEnd = "" ;
    public static Integer loadingUp = 0;
    String strLatitude ="0";
    String strLongitude = "0" ;
    Integer More = 1;
    int RowCount = 0;
    TextView mytext;
    TelephonyManager tManager;
    //boolean loadingMore = false;
    Integer a = 0;
    ListView list;
    TextView judul, tgl, isi;
    // SwipeRefreshLayout swipe;
    List<PromotionData> newsList = new ArrayList<PromotionData>();
    PromotionAdapter adapter;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);
        StrLastMonthEnd = "" ;
        tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        SharedPreferences msn = getSharedPreferences(GlabaleParameter.PREFS_MSISDN, Context.MODE_PRIVATE);
        StrMobileNumber = msn.getString("msisdn", "");

        try {
            final String strGetLocation = GetLocation.get(PromotionActivity.this);
             strLatitude =strGetLocation.split("~")[0];
             strLongitude = strGetLocation.split("~")[1] ;
           //  strLatitude ="111";
           //  strLongitude = "222" ;
        } catch (Exception e) {
        }


        //  MyArrList = new ArrayList<HashMap<String, String>>();
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.actionbar_layout_title_center);
            mytext = (TextView) findViewById(R.id.txtcenter);
            mytext.setText(R.string.str_promotion);
        } catch (Exception e) {
        }

        loadingUp = 0;
        list = (ListView) findViewById(R.id.listView1);
        newsList.clear();
        adapter = new PromotionAdapter(PromotionActivity.this, newsList);
        list.setAdapter(adapter);
        View footerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.promotion_loading_view, null, false);
        this.list.addFooterView(footerView);
        txtloading = (TextView) list.findViewById(R.id.txtloading);
        ProgressBar = (android.widget.ProgressBar) list.findViewById(R.id.progressBar);
        ProgressBar.setVisibility(View.GONE);
        ProgressBar.setVisibility(View.VISIBLE);
        //  ViewGroup footer = (ViewGroup)layoutinflater.inflate(R.layout.loading_view,list,false);
        //  list.addFooterView(footer);
        newsList.clear();
        adapter.notifyDataSetChanged();

        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //what is the bottom item that is visible
                int lastInScreen = firstVisibleItem + visibleItemCount;
                //is the bottom item visible & not loading more already? Load more!
                // Log.e("More=",String.valueOf( More));
                if (More == 1) {
                    if ((lastInScreen == totalItemCount) && !(loadingMore)) {

                        loadingUp++;
                        if (loadingUp == 1) {
                            customer_history_request(""+RowCount);
                        }
                    }
                }
            }
        });
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_icon_close, menu);
        return true;
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

    public void set_Refresh(String resultStatus) {
        if (resultStatus.equals("")) {
            if (RowCount == 0) {
                txtloading.setText(R.string.str_no_record);
            } else {
                txtloading.setText(""); // "R.string.str_end_more"
            }
            More = 0;
            ProgressBar.setVisibility(View.GONE);
            return;
        }
        String a = resultStatus;
        String[] arrB = a.split(";");
        for (String c : arrB) {
            RowCount++;
            PromotionData news = new PromotionData();
            news.settxtDescription(c.split("~")[0]);
            news.setimgBase64(c.split("~")[1]);
            news.settxtDetail(c.split("~")[2]);
            news.settxtInfo(c.split("~")[3]);
            newsList.add(news);
        }
        adapter.notifyDataSetChanged();
    }
    private void customer_history_request(String strfrom) {

        final String  strGetLocation =    GetLocation.get(PromotionActivity.this);
       /* final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle(getString(R.string.app_name));
        progress.setMessage(getString(R.string.str_pleasewait_inprocess));
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();*/
// To dismiss the dialog
        String url = getString(R.string.str_url_https) + "WalletPromotion.php";
        // Log.e("url:", url);
        //new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        final String strLimtfrom = strfrom;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Log.e("response0", response);
                        try {
                            //   Log.e("response1", response);


                            //แปลงผลลัพธ์ที่ได้มาเป็น JSON Object
                            JSONObject jObject = new JSONObject(response);
                            //  Log.e("response2", response);

                            if (jObject.getString("status").equals("OK")) {
                               // Log.e("response3", response);


                                CryptoHelper cryptoHelper = new CryptoHelper();
                                String strdecrypt = "";
                                try {
                                    strdecrypt =  cryptoHelper.decrypt(jObject.getString("result"));
                                } catch (Exception e) {
                                }
                             //   Log.e("strdecrypt", strdecrypt);
                              //  dialog(strdecrypt, 2);  // 1:Success, 2:Fail, 3:Error
                                String[] parts = strdecrypt.split("\\|");
                                String resultCode = parts[0];
                                if (resultCode.equals("405000000")) {
                                    String strResul = jObject.getString("promotion");
                                    String strResul2 ="";
                                   String merchantDetail = jObject.getString("merchantDetail");
                                    System.out.println("list1="+merchantDetail);
                                    JSONObject jObj = null;
                                    String  opend_time="" ;
                                    String  close_time="" ;
                                    String  distance_in_km="" ;
                                    String  opendStatus="" ;
                                    String  merchant_id="" ;

                                    try {
                                        String jsonStr = jObject.getString("merchantDetail");
                                        jObj = new JSONObject(jsonStr);
                                      String strjObj = jObj.get("merchant_list").toString();
                                        System.out.println("list="+strjObj);
                                        JSONArray jsonarray = new JSONArray(strjObj);


                                       for (int i = 0; i < jsonarray.length(); i++) {
                                          JSONObject jsonobject = jsonarray.getJSONObject(i);
                                           try { strResul2 = jsonobject.getString("merchant_name") ;
                                           } catch(Exception e) { strResul2 += " " ; }
                                           try { strResul2 += "~"+jsonobject.getString("merchang_advertising_photo") ;
                                           } catch(Exception e) { strResul2 += "~" ; }
                                           try { strResul2 += "~"+jsonobject.getString("merchant_advertising_des") ;
                                           } catch(Exception e) { strResul2 += "~ " ; }

                                           try {
                                               opend_time =  jsonobject.getString("opend_time") ;
                                           } catch(Exception e) { opend_time = "" ; }
                                           try {
                                               close_time =  jsonobject.getString("close_time") ;
                                           } catch(Exception e) { close_time = "" ; }
                                           try {
                                               distance_in_km =  jsonobject.getString("distance_in_km") ;
                                           } catch(Exception e) { distance_in_km = "" ; }
                                           try {
                                               opendStatus =  jsonobject.getString("opendStatus") ;
                                           } catch(Exception e) { opendStatus = "" ; }
                                           try {
                                               merchant_id =  jsonobject.getString("merchant_id") ;
                                           } catch(Exception e) { merchant_id = "" ; }
                                        }
                                        strResul2 += "~";
                                      //  String  opend_time,close_time,distance_in_km , opendStatus,merchant_id ;
                                        if (!opend_time.equals("")) { opend_time =  getString(R.string.str_open)  +": "+opend_time+ "";}
                                        if (!opend_time.equals("")) { close_time =    "-"+close_time+ ", ";}
                                        if (!opend_time.equals("")) { distance_in_km =  getString(R.string.str_distance)  +": "+ distance_in_km + " " + getString(R.string.str_km);}
                                        if (!opend_time.equals("")) {

                                            if (opendStatus.equals("Closed")){
                                                opendStatus =    "\n"+getString(R.string.str_status)+": " + getString(R.string.str_open);
                                            }
                                            else  {
                                                opendStatus =    "\n"+getString(R.string.str_status)+": " + getString(R.string.str_close) ;
                                            }


                                        }


                                        strResul2 += opend_time+close_time+distance_in_km+opendStatus;


                                        for (String cs : jsonStr.split(",")) {
                                          // System.out.println("list="+cs);
                                        }
                                    } catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                    }
                                    set_Refresh(strResul2);
                                   // Log.e("merchantDetail",  name);

                                  //  JSONObject jObject2 = new JSONObject(jObject.getString("promotion"));
                                  //  dialog(jObject2.toString(),  1);
                                } else  {  // No record loading
                                    // String strResul = parts[1];

                                    set_Refresh("");

                                }

                                //1001ถ้าดึงข้อมูลจาก database มีปัญหาจะแสดง error
                            } else {
                                // dialogFail();

                            }


                        } catch (JSONException e) {
                            //Log.e("ConnectServer3", "Error parsing data " + e.toString());
                            // ((ActivityHotspotUser)context).errorConnectToServer_wifi();
                            // Toast.makeText(UserInfoActivity.this, "", Toast.LENGTH_LONG).show();
                            //  Toast.makeText(InternetAdslPaymentActivity.this, getString(R.string.str_can_not_connect_to_server), Toast.LENGTH_LONG).show();
                        }
                     //   progress.dismiss();
                        loadingUp=0;
                        // Log.e("response",response);
                        //Toast.makeText(UserInfoActivity.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  Toast.makeText(InternetAdslPaymentActivity.this, getString(R.string.str_can_not_connect_to_server), Toast.LENGTH_LONG).show();
                    //    progress.dismiss();
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
                String strBasic_info   = StrMobileNumber+"|"+verCode+"|"+currentDate+"|"+Strlanguage+"|"+StrdeviceInfo+"|"+Strtoken+"|"+StrjwtToken;
                // ===== End StrBasic info
                strBasic_info +="|"+strLimtfrom+"|"+strLatitude+"|"+strLongitude;
                CryptoHelper cryptoHelper = new CryptoHelper();
                String encryptString = "";
                try {
                    encryptString =  cryptoHelper.encrypt(strBasic_info);
                } catch (Exception e) {
                }
                Map<String, String> params = new HashMap<String, String>();
                params.put("Active_values", encryptString);
                params.put("publickey", "CheckActive");
                //  Log.e("Active_valuesHistory", encryptString);
                //    params.put("deviceInfo", deviceInfo);
                //   params.put("versionCode", String.valueOf(verCode));
                //params.put("versionCRequestQueue requestQueue = Volley.newRequestQueue(this, new HurlStack(null, getSocketFactory()));ode", String.valueOf(verCode));
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));  // set DefaultRetryPolicy.DEFAULT_MAX_RETRIES =0 no retry
        //= 0 mean retry connect 1 times only
        // stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this, new HurlStack(null, getSocketFactory()));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            //  Toast.makeText(InternetAdslPaymentActivity.this, getString(R.string.str_can_not_connect_to_server), Toast.LENGTH_LONG).show();
            //Log.e("", e.toString());
        }
    }

    private void dialog(String Str, int ico) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Str);
        builder.setCancelable(false)
                .setPositiveButton(R.string.str_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
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
    private SSLSocketFactory getSocketFactory() {
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = getResources().openRawResource(R.raw.ca);
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                //   Log.e("CERT", "ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                caInput.close();
            }

            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);


            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    if ("202.62.111.236".equals(hostname)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });


            SSLContext context = null;
            context = SSLContext.getInstance("TLS");

            context.init(null, tmf.getTrustManagers(), null);
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());

            SSLSocketFactory sf = context.getSocketFactory();

            return sf;

        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        return null;
    }


    public void LoginigFail(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
    public void historyDetail(final String strDetail) {
      //  Log.e("TOKEN=:", strDetail);



        //  SharedPreferences editorMyPrefAfertLogin = getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = getSharedPreferences(GlabaleParameter.PREFS_HISTORY_DETAIL, Context.MODE_PRIVATE).edit();
        editor1.putString(GlabaleParameter.PREFS_ETL_PROMOTION_DETAIL, strDetail);
        editor1.commit();

        Intent refresh1 = new Intent(getApplicationContext(), PromotionDetailActivity.class);
        startActivity(refresh1);



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
