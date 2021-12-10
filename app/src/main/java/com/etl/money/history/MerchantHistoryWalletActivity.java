package com.etl.money.history;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.etl.money.security.CryptoHelper;
import com.etl.money.security.GetLocation;

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

public class MerchantHistoryWalletActivity extends AppCompatActivity {
    String StrMobileNumber ;
    private MerchantHistoryWalletAdapter HistoryAdapter;
    Context context;
    LayoutInflater layoutinflater;
    TextView txtloading;
    android.widget.ProgressBar ProgressBar;
    String language;
    public static String loadingstatus = "";
    public boolean loadingMore = false;
    String StrLastMonthEnd = "" ;
    public static Integer loadingUp = 0;
/*  boolean loadingMore = false;
    int RowCount = 0;
    ListView listView;
    ArrayList<String> myListItems;
    TextView   mytext;
    TelephonyManager tManager;
    ArrayAdapter adapter;
    ArrayList<HashMap<String, String>> MyArrList;*/

    Integer More = 1;
    int RowCount = 0;
    TextView mytext;
    TelephonyManager tManager;
    //boolean loadingMore = false;
    Integer a = 0;
    ListView list;
    TextView judul, tgl, isi;
    // SwipeRefreshLayout swipe;
    List<HistoryWalletData> newsList = new ArrayList<HistoryWalletData>();
    MerchantHistoryWalletAdapter adapter;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_wallet);
        StrLastMonthEnd = "" ;
        tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        SharedPreferences msn = getSharedPreferences(GlabaleParameter.PREFS_MSISDN, Context.MODE_PRIVATE);
         StrMobileNumber = msn.getString("msisdn", "");


        loadingUp = 0;
        list = (ListView) findViewById(R.id.listView1);
        newsList.clear();
        adapter = new MerchantHistoryWalletAdapter(MerchantHistoryWalletActivity.this, newsList);
        list.setAdapter(adapter);
        View footerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading_view, null, false);
        this.list.addFooterView(footerView);
        txtloading = (TextView) list.findViewById(R.id.txtloading);
        ProgressBar = (ProgressBar) list.findViewById(R.id.progressBar);
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

        int i = 0;
        String a = new String(resultStatus);
        ;
        String[] arrB = a.split(";");
        for (String c : arrB) {
            i = i + 1;
            RowCount++;
            String ab = new String(c);
            String[] arrc = ab.split("~");


            int j = 0;
            HistoryWalletData news = new HistoryWalletData();


            // === Get MonthLy
            String ArrBInMonth = arrB[i-1];
            String[] arrcInMonth = ArrBInMonth.split("~");
            String StrInMonth = arrcInMonth[4].substring(3,10);


            String StrLastMonth = "";

            if (StrLastMonthEnd.length()>3){
                if (i==1){
                    StrLastMonth=StrLastMonthEnd;
                }
            }


            if (i>1){
                String ArrBLastMonth = arrB[i-2];
                String[] arrcLastMonth = ArrBLastMonth.split("~");
                StrLastMonth = arrcLastMonth[4].substring(3,10);
                if (i>=arrB.length) {
                    StrLastMonthEnd = StrLastMonth;
                }
            }
            if (StrLastMonth.equals(StrInMonth)){
                StrInMonth  = "";
            }
            // === End MonthLy

            news.settxtMonthly(StrInMonth);

          //  Log.e("responsec:", resultStatus);
            for (String cb : arrc) {

                j = j + 1;
                if (j == 1) {
                    news.settxtID(cb);
                }
                if (j == 2) {
                    news.settxtSubject(cb);
                }
                if (j == 3) {
                    news.settxtDescription(cb);
                }
                if (j == 4) {
                    //  news.settxtStatus(cb);
                }
                if (j == 5) {
                    news.settxtDatetine(cb);
                }
                if (j == 6) {
                    news.settxtAmount(cb);
                }
                if (j == 7) {
                    news.settxtCircleTitle(cb);
                }
                if (j == 8) {
                    news.settxtColor(cb);
                }



            }
            news.settxtDetail(c);
            newsList.add(news);
        }
        adapter.notifyDataSetChanged();
    }

    public void cannotConnectToServer() {
        Toast.makeText(this, "cannotConnectToServer", Toast.LENGTH_LONG).show();
    }

    public void errorConnectToServer() {
        Toast.makeText(this, "errorConnectToServer", Toast.LENGTH_LONG).show();
    }

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
            mytext.setText( getString(this, "str_"+ActivityKey+"_title") );

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_exit:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void customer_history_request(String strfrom) {

        final String  strGetLocation =    GetLocation.get(MerchantHistoryWalletActivity.this);
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle(getString(R.string.app_name));
        progress.setMessage(getString(R.string.str_pleasewait_inprocess));
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
// To dismiss the dialog
        String url = getString(R.string.str_url_https) + "WalletMerchantHistory.php";
        // Log.e("url:", url);
        //new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        final String strLimtfrom = strfrom;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response0", response);
                        try {
                         //   Log.e("response1", response);


                            //แปลงผลลัพธ์ที่ได้มาเป็น JSON Object
                            JSONObject jObject = new JSONObject(response);
                          //  Log.e("response2", response);

                            if (jObject.getString("status").equals("OK")) {
                                Log.e("response3", response);


                                CryptoHelper cryptoHelper = new CryptoHelper();
                                String strdecrypt = "";
                                try {
                                    strdecrypt =  cryptoHelper.decrypt(jObject.getString("result"));
                                } catch (Exception e) {
                                }
                             //   Log.e("strdecrypt", strdecrypt);

                               // String strdecodeBase64 = "";
                              //  strdecodeBase64=    decodeBase64(strdecrypt);
                             //   Log.e("decodeBase64", strdecodeBase64);


                                String[] arry = strdecrypt.split("\\|");
                                // Log.e("decrypted:------",decrypted);
                                String[] parts = strdecrypt.split("\\|");
                                String resultCode = parts[0];
                                if (resultCode.equals("00")) {
                                    String strResul = parts[1];

                                    set_Refresh(strResul);

                                } else if (resultCode.equals("99")) {  // No record loading
                                    // String strResul = parts[1];

                                    set_Refresh("");

                                } else if (resultCode.equals("4072")) {
                                    //// Length of number or pin is not march.

                                    String msg = resultCode + " " + getString(R.string.str_error_operation_fail93);
                                    LoginigFail(msg);
                                    return;
                                } else if (resultCode.equals("4044")) {
                                    // sum parameter is empty
                                    String msg = resultCode + " " + getString(R.string.str_error_operation_fail93);
                                    LoginigFail(msg);
                                    return;
                                } else if (resultCode.equals("4043")) {

                                    String msg = resultCode + " " + getString(R.string.str_error_operation_fail93);
                                    LoginigFail(msg);
                                    return;
                                } else {
                                    String msg = getString(R.string.str_package_buy_fail);
                                    // String msg = "Sorry, your request is fail, try again or contact ETL customer care service, thank you.";
                                    LoginigFail(msg);
                                    return;
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
                        progress.dismiss();
                        loadingUp=0;
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
                strBasic_info +="|"+strLimtfrom;
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
    public void historyDetal(final String strDetail) {
         Log.e("TOKEN=:", strDetail);



        //  SharedPreferences editorMyPrefAfertLogin = getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = getSharedPreferences(GlabaleParameter.PREFS_HISTORY_DETAIL, Context.MODE_PRIVATE).edit();
        editor1.putString(GlabaleParameter.PREFS_ETL_HISTORY_DETAIL, strDetail);
        editor1.commit();

        Intent refresh1 = new Intent(getApplicationContext(), HistoryWalletDetailActivity.class);
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

