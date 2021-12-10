package com.etl.money.setting;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.media.Image;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class UserWalletInfoActivity extends AppCompatActivity {
    TextView mytext;
    String StrMobileNumber ="";
    ImageView imgProfile ;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_wallet_info);
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.actionbar_layout_title_center);
            mytext = (TextView) findViewById(R.id.txtcenter);
            mytext.setText(R.string.str_user_info);
        } catch (Exception e) {
        }

        SharedPreferences msn = getSharedPreferences(GlabaleParameter.PREFS_MSISDN, Context.MODE_PRIVATE);
        StrMobileNumber = msn.getString("msisdn", "");


        imgProfile = (ImageView) findViewById(R.id.imgProfile);


        SharedPreferences pref_p1 = getApplicationContext().getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_PROFILE_STORE, MODE_PRIVATE);
        String StrBase64 = pref_p1.getString(GlabaleParameter.PREFS_ETL_PROFILE_PHOTOS_STORE, "");

        if (StrBase64.length() < 10) {
            StrBase64 = getResources().getString(R.string.img_take_photos);
        }


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        imageBytes = Base64.decode(StrBase64 +".", Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        Bitmap circleBitmap = Bitmap.createBitmap(decodedImage.getWidth(), decodedImage.getHeight(), Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(decodedImage, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        Canvas c = new Canvas(circleBitmap);
        c.drawCircle(decodedImage.getWidth() / 2, decodedImage.getHeight() / 2, decodedImage.getWidth() / 2, paint);
        imgProfile.setImageBitmap(circleBitmap);





        LoadUserInfor(StrMobileNumber);
       /* String StrResult = "account_id;202715223984447\n" +
                "|mobile tel number;2028044484\n" +
                "|customer name;Sonexay VANHNAKHONE\n" +
                "|village;ບ. ພະຂາວ\n" +
                "|district;ໄຊທານີ\n" +
                "|province;ນະຄອນຫຼວງວຽງຈັນ\n" +
                "|gender;Male\n" +
                "|occupation;BSD-STAFF\n" +
                "|card_passport_id;LA-0212\n" +
                "|password type;OTP Code\n" +
                "|status;Active\n" +
                "|first active date;2020-01-27 15:22:39\n" +
                "|expire date;2020-07-25 23:59:59\n" +
                "|agent name;Thipphavanh PHAILOTH\n" +
                "|created date;2020-01-27 15:24:20";
        String[] ArrA = StrResult.split( "\\|" );

        // listView1
        final ListView lisView1 = (ListView)findViewById(R.id.listView1);

        ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;

            for (String str : ArrA) {

            String[] ArrB = str.split( ";" );
            map = new HashMap<String, String>();
            map.put("Title", ArrB[0]+":");
            map.put("Description", ArrB[1]);
            MyArrList.add(map);
        }
        SimpleAdapter sAdap;
        sAdap = new SimpleAdapter(UserWalletInfoActivity.this, MyArrList, R.layout.wallet_user_info_column,
                new String[] {"Title", "Description"}, new int[] {R.id.txtTitle, R.id.txtDescription});
        lisView1.setAdapter(sAdap);
        */

    }





    private void LoadUserInfor(String MobileNumber){
        final String  strGetLocation =    GetLocation.get(UserWalletInfoActivity.this);
        StrMobileNumber = MobileNumber;
        final Activity act = UserWalletInfoActivity.this ;
        final ProgressDialog progress_sub = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        progress_sub.setMessage(getString(R.string.str_pleasewait_inprocess));
        progress_sub.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress_sub.show();
        String url = getString(R.string.str_url_https) + "WalletUserInfo.php";
        //   Log.e("url_info:", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObject = new JSONObject(response);
                            if (jObject.getString("status").equals("OK")) {

                               //  Log.e("response123:", response);
                                CryptoHelper cryptoHelper = new CryptoHelper();
                                String result = "";
                                try {
                                    result =  cryptoHelper.decrypt(jObject.getString("result"));
                                } catch (Exception e) {
                                }
                                String[] arry = result.split("\\|");

                                //  Log.e("Decrpt123:",  EnDecry.de(jObject.getString("result"),act));
                                if (arry[0].equals("405000000")){
                                    if (StrMobileNumber.equals(arry[1])){

                                        String strdecrypt = "";
                                        try {
                                            strdecrypt =  cryptoHelper.decrypt(jObject.getString("data"));
                                        } catch (Exception e) {
                                        }
                                      //  String strdecodeBase64 = "";
                                      //   strdecodeBase64=    decodeBase64(strdecrypt);
                                        // Log.e("EnDecry64:", strdecodeBase64);
                                      //    EnDecry=    decodeBase64(EnDecry);

                                        String[] arryData = strdecrypt.split("\\|");
                                        // listView1
                                  final ListView lisView1 = (ListView)findViewById(R.id.listView1);
                                        ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
                                        HashMap<String, String> map;
                                        for (String str : arryData) {
                                            String[] ArrB = str.split( ";" );
                                            map = new HashMap<String, String>();
                                            map.put("Title", ArrB[0]+":");
                                            map.put("Description", ArrB[1]);
                                            MyArrList.add(map);
                                        }
                                        SimpleAdapter sAdap;
                                        sAdap = new SimpleAdapter(UserWalletInfoActivity.this, MyArrList, R.layout.wallet_user_info_column,
                                                new String[] {"Title", "Description"}, new int[] {R.id.txtTitle, R.id.txtDescription});
                                        lisView1.setAdapter(sAdap);

                                    }
                                }else{

                                    if (arry[0].length()>4){
                                        if (!arry[0].substring(0,5).equals("Error")){
                                            dialog(arry[0]);
                                        }else{
                                            dialog(getString(R.string.str_error_operation_fail93));
                                        }
                                    }else {
                                        dialog(getString(R.string.str_error_operation_fail93));
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
                StrdeviceInfo    = strGetLocation+"~"+StrdeviceInfo;
                String strBasic_info   = StrMobileNumber+"|"+verCode+"|"+currentDate+"|"+Strlanguage+"|"+StrdeviceInfo+"|"+Strtoken+"|"+StrjwtToken;
                // ===== End StrBasic info


                CryptoHelper cryptoHelper = new CryptoHelper();
                String encryptString = "";
                try {
                    encryptString =  cryptoHelper.encrypt(strBasic_info);
                } catch (Exception e) {
                }
                params.put("publickey","CheckActive");
                params.put("Active_values", encryptString);
              //   Log.e("Active_valuesUser:", encryptString);
                // Log.e("Active_values2:",  EnDecry.de("88819637ec978945044ebc47c155e4956f7413d55f7cd591de0db9f5d60533bc",act));


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
                        //Stop the activity
                        //   finish();
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
    @SuppressLint("WrongConstant")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_icon_close, menu);
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.actionbar_layout_title_center);
            TextView mytext = (TextView) findViewById(R.id.txtcenter);
            mytext.setText(R.string.str_user_info);
        } catch (Exception e) {
        }
        return true;
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
