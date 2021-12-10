package com.etl.money.promotion_and_advertising;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
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
import com.camerakit.CameraKitView;
import com.etl.money.LoginActivity;
import com.etl.money.R;
import com.etl.money.config.AutoLogout;
import com.etl.money.devise_info.IMEI;
import com.etl.money.global.GlabaleParameter;
import com.etl.money.register.RegisterNewAgentActivity;
import com.etl.money.register.TakePhotosActivity;
import com.etl.money.security.CSSLSocketFactory;
import com.etl.money.security.CryptoHelper;
import com.etl.money.security.GetLocation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreateAdvertisingActivity extends AppCompatActivity {

    String StrAdOrEditForDes = "0"; // 1= Add, 2 Edite
    String StrAdOrEditForIMG = "0"; // 1= Add, 2 Edite
    // Camera camera;
    TakePhotosActivity helper;
    private EditText txtDescription;
    private TextView txtMobileNumber;

    private Button btn_Submit_register;
    private Context mContext;
    String strMsisdn ;
    static final int DATE_DIALOG_ID = 0;
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;
    //public Connect_server_register connectServerRegister;
    private static final int PERMISSION_CODE = 100;
    private static final int IMAGE_CAPTURE_CODE = 101;

    Button btnTakePhotos;
    ImageView imgTakePhotos;
    String ActivityKey ;
    Uri image_uri;
    private String strFirstPhotosBase64 ;
    private String strTakePhotosStatus = "" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advertising);
        Intent intent = getIntent();
        ActivityKey = intent.getStringExtra("ActivityKey");
        // takePictureButton = (Button) findViewById(R.id.button_image);
        txtMobileNumber = (TextView) findViewById(R.id.txtMobilenumber);
        txtDescription = (EditText) findViewById(R.id.txtDescription);


        imgTakePhotos = findViewById(R.id.imgTakePhotos);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPrefTakePhotos", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Profile", "");  // Saving string
        editor.commit(); // commit changes

        SharedPreferences msn = getSharedPreferences(GlabaleParameter.PREFS_MSISDN, Context.MODE_PRIVATE);
        strMsisdn = msn.getString("msisdn", "");
        txtMobileNumber.setText(strMsisdn);

        BitmapDrawable drawable = (BitmapDrawable) imgTakePhotos.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        Log.e("imgString:", ""+imgString.length());
        strFirstPhotosBase64 = imgString;

        btn_Submit_register = (Button) findViewById(R.id.bt_register);

        btn_Submit_register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Toast.makeText(getApplication(), "IMEI=" + get_dev_id(), Toast.LENGTH_SHORT).show();

                String strMobileNumber = txtMobileNumber.getText().toString();
                String strAgentName = txtDescription.getText().toString();

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
                if (strAgentName.trim().length() < 5 || strAgentName.trim().length() > 35) {
                    //  txtFirstName.requestFocus();
                    txtDescription.setError(getString(R.string.str_Please_use_between_6_and_35_characters));
                    //Toast.makeText(getApplication(), getString(R.string.str_full_name_is_invalid), Toast.LENGTH_SHORT).show();
                    return;
                } else {

                }

                if (strTakePhotosStatus.equals("")) {
                    if (StrAdOrEditForDes.equals("2")){
                        if (StrAdOrEditForIMG.equals("2")){
                            Toast.makeText(getApplication(), getString(R.string.str_please_take_your_photos), Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                        }
                    }else{
                        Toast.makeText(getApplication(), getString(R.string.str_please_take_your_photos), Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
                //String ss=  strDateOfBirth ;
                //strDateOfBirth = ss.substring(0,2)+"-"+ ss.substring(3,5)+"-"+ ss.substring(6,10);
                //   Toast.makeText(getApplication(), "is"+strDateOfBirth, Toast.LENGTH_SHORT).show();

                String regiterValues = strMobileNumber + "|" + strAgentName + "|0|0"  ;
                // Log.e("strBase642021210117:", "" + strPhotosBase64);
                //  Toast.makeText(getApplication(), getString(R.string.str_please_enter_date_of_birth)+"**"+strDateOfBirth, Toast.LENGTH_SHORT).show();


                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPrefTakePhotos", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                String strProfile =  pref.getString("Profile", null);

                send_Register(regiterValues,strProfile);


            }


        });



        btnTakePhotos = findViewById(R.id.btnTakePhotos);



        btnTakePhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(com.etl.money.promotion_and_advertising.CreateAdvertisingActivity.this,UplodeRegPhotoActivity.class);
                startActivityForResult(intent, 2);// Activity is started with requestCode 2

            }
        });

        Loadnifo();
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

    public void Close(View v) {
        finish();
    }



    public static Bitmap cropToSquare(Bitmap bitmap){
        int width  = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width)? height - ( height - width) : height;
        int cropW = (width - height) / 2;
        cropW = (cropW < 0)? 0: cropW;
        int cropH = (height - width) / 2;
        cropH = (cropH < 0)? 0: cropH;
        Bitmap cropImg = Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);
        return cropImg;
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

    private void send_Register(String str, String PhotosBase64) {
        // RegisterSuccess( str);

        final String strGetLocation = GetLocation.get(this);
        final String regiterValues = str;
        final String strTakePhotosBase64 = PhotosBase64;
        final Activity act = com.etl.money.promotion_and_advertising.CreateAdvertisingActivity.this ;
        final ProgressDialog progress_sub = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        progress_sub.setMessage(getString(R.string.str_pleasewait_inprocess));
        progress_sub.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress_sub.show();

        String url = getString(R.string.str_url_https) + "WalletCreateAdvertising.php";
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
                                // String decrypted = null;
                                CryptoHelper cryptoHelper = new CryptoHelper();
                                try {
                                    result =  cryptoHelper.decrypt(jObject.getString("result"));
                                } catch (Exception e) {
                                    //    Log.e(" Exception decrypted:",e.getMessage());
                                }
                                // Log.e("decrypted20210407:",result);
                                String[] parts = result.split("\\|");
                                String resultCode = parts[0].trim();
                                if(resultCode.equals("405000000")) {

                                    if (StrAdOrEditForDes.equals("2")){
                                        dialog(getString(R.string.str_successful) ,  1);
                                    }else{
                                        dialog(getString(R.string.str_register_successful) ,  1);
                                    }
                                    Loadnifo();
                                }else {
                                    Toast.makeText(getApplication(),""+parts[1], Toast.LENGTH_LONG).show();
                                    //  Toast.makeText(Register.this, getString(R.string.str_can_not_connect_to_server), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                //                          Log.e("NO OK","NO OK =");
                                Toast.makeText(com.etl.money.promotion_and_advertising.CreateAdvertisingActivity.this, getString(R.string.str_can_not_connect_to_server), Toast.LENGTH_LONG).show();

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




                // StrdeviceInfo    = strGetLocation+"~"+StrdeviceInfo;
                StrdeviceInfo    = "";
                String strBasic_info   = strMsisdn+"|"+verCode+"|"+currentDate+"|"+Strlanguage+"|"+StrdeviceInfo+"|"+Strtoken+"|"+StrjwtToken+"|"+regiterValues;
                // ===== End StrBasic info


                String encryptString = "";
                CryptoHelper cryptoHelper = new CryptoHelper();
                try {
                    encryptString = cryptoHelper.encrypt(strBasic_info);
                } catch (Exception e) {
                }

                params.put("publickey", "UpdateDesAndIMG");
                params.put("Active_values", encryptString);
                params.put("TakePhotosBase64", strTakePhotosBase64);


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
    private void Loadnifo() {
        // RegisterSuccess( str);
        final String strGetLocation = GetLocation.get(this);


        final Activity act = com.etl.money.promotion_and_advertising.CreateAdvertisingActivity.this ;
        final ProgressDialog progress_sub = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        progress_sub.setMessage(getString(R.string.str_pleasewait_inprocess));
        progress_sub.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress_sub.show();

        String url = getString(R.string.str_url_https) + "WalletCreateAdvertising.php";
        //   Log.e("url_info:", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("result2:------",response);
                        try {
                            //แปลงผลลัพธ์ที่ได้มาเป็น JSON Object
                            JSONObject jObject = new JSONObject(response);
                            if (jObject.getString("status").equals("OK")) {
                                String result = jObject.getString("result");
                                //    Log.e("result2:------",result);
                                // String decrypted = null;
                                CryptoHelper cryptoHelper = new CryptoHelper();
                                try {
                                    result =  cryptoHelper.decrypt(jObject.getString("result"));
                                } catch (Exception e) {
                                    //    Log.e(" Exception decrypted:",e.getMessage());
                                }
                                // Log.e("decrypted20210407:",result);
                                String[] parts = result.split("\\|");
                                String resultCode = parts[0].trim();
                                if(resultCode.equals("405000000")) {
                                    txtDescription.setText(parts[3].trim());


                                    btn_Submit_register.setText(getString(R.string.str_save_and_edit));

                                    if (parts[6].trim().equals("1")) {

                                    } else   if (parts[6].trim().equals("11")){



                                    }
                                    StrAdOrEditForDes= "2";
                                    StrAdOrEditForIMG = "1";
                                    String  StrBase64 ;
                                    StrBase64 = jObject.getString("agentPhotoProfile");
                                    //  StrBase64 = "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";
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    byte[] imageBytes = baos.toByteArray();
                                    imageBytes = Base64.decode(StrBase64 +".", Base64.DEFAULT);
                                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                                    imgTakePhotos.setImageBitmap(decodedImage);


                                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPrefTakePhotos", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("Profile", StrBase64);  // Saving string
                                    editor.commit(); // commit changes
                                    // dialog(jObject.getString("agentPhotoProfile"), 1);


                                }else {
                                    StrAdOrEditForIMG = "1";
                                    StrAdOrEditForDes= "1";

                                    btn_Submit_register.setText(getString(R.string.str_create));


                                    Toast.makeText(getApplication(),""+parts[1], Toast.LENGTH_LONG).show();
                                    //  Toast.makeText(Register.this, getString(R.string.str_can_not_connect_to_server), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                //                          Log.e("NO OK","NO OK =");
                                Toast.makeText(com.etl.money.promotion_and_advertising.CreateAdvertisingActivity.this, getString(R.string.str_can_not_connect_to_server), Toast.LENGTH_LONG).show();

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

                // StrdeviceInfo    = strGetLocation+"~"+StrdeviceInfo;
                StrdeviceInfo    = "";
                String strBasic_info   = strMsisdn+"|"+verCode+"|"+currentDate+"|"+Strlanguage+"|"+StrdeviceInfo+"|"+Strtoken+"|"+StrjwtToken;
                // ===== End StrBasic info
                CryptoHelper cryptoHelper = new CryptoHelper();
                String encryptString = "";
                String encryptPhotosBase64 = "";
                try {
                    encryptString =  cryptoHelper.encrypt(strBasic_info);

                } catch (Exception e) {
                }
                params.put("publickey", "Loadinfo");
                params.put("Active_values", encryptString);
                // Log.e("strBasic_info:", ""+strBasic_info);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_icon_close, menu);
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.actionbar_layout_title_center);
            TextView mytext = (TextView) findViewById(R.id.txtcenter);
            mytext.setText(getString(R.string.str_create_advertising));



        } catch (Exception e) {
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2)
        {

            try {


            // Log.e("strExtra: (", ""+strTakePhotosStatus);
            strTakePhotosStatus =  data.getStringExtra("strExtra");

            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPrefTakePhotos", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            String strProfile = pref.getString("Profile", "");


            if  (strProfile.equals("")) {
                strTakePhotosStatus =  "";
            }else{
                strTakePhotosStatus =  "TAKEPHOTOS";
                String StrBase64 = strProfile;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] imageBytes = baos.toByteArray();
                imageBytes = Base64.decode(StrBase64 + ".", Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                //   Log.e("20210118Base64abcV1: (", ""+StrBase64);
                // Bitmap bMap =   cropToSquare(BitmapFactory.decodeStream(imageStream));

                 Bitmap bMap =   cropToSquare(decodedImage);
                imgTakePhotos.setImageBitmap(bMap);
                StrAdOrEditForIMG = "2";
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
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
