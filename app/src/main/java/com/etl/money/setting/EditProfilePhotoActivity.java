package com.etl.money.setting;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
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
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
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
import com.etl.money.LoginActivity;
import com.etl.money.R;
import com.etl.money.config.AutoLogout;
import com.etl.money.devise_info.IMEI;
import com.etl.money.global.GlabaleParameter;
import com.etl.money.register.RegisterActivity;
import com.etl.money.register.TakePhotosActivity;
import com.etl.money.security.CSSLSocketFactory;
import com.etl.money.security.CryptoHelper;
import com.etl.money.security.GetLocation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditProfilePhotoActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String strTakePhotosStatus = "" ;
    ImageView imgTakePhotos ;
    Button btnTakePhotos , btnEditProFilePhoto;
    TextView txtProfileName ;
String StrMobileNumber ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_photo);

        txtProfileName =  findViewById(R.id.txtProfileName);
        imgTakePhotos =  findViewById(R.id.imgTakePhotos);
        btnTakePhotos =  findViewById(R.id.btnTakePhotos);
        btnEditProFilePhoto =  findViewById(R.id.btnEditProFilePhoto);
        btnEditProFilePhoto.setEnabled(false);

        SharedPreferences prefpp = getApplicationContext().getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, MODE_PRIVATE);

        txtProfileName.setText(prefpp.getString(GlabaleParameter.PREFS_ETL_CUSTOMER_NAME, "") + " " + prefpp.getString(GlabaleParameter.PREFS_ETL_CUSTOMER_LAST_NAME, ""));
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPrefTakePhotos", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Profile", "");  // Saving string
        editor.commit(); // commit changes

        SharedPreferences msn = getSharedPreferences(GlabaleParameter.PREFS_MSISDN, Context.MODE_PRIVATE);
        StrMobileNumber = msn.getString("msisdn", "");

        SharedPreferences pref_p1 = getApplicationContext().getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_PROFILE_STORE, MODE_PRIVATE);
        String StrBase64 = pref_p1.getString(GlabaleParameter.PREFS_ETL_PROFILE_PHOTOS_STORE, "");

        if (StrBase64.length() < 10) {
            StrBase64 = getResources().getString(R.string.img_take_photos);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        imageBytes = Base64.decode(StrBase64 +".", Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        imgTakePhotos.setImageBitmap(decodedImage);




        btnTakePhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();

            }
        });
        btnEditProFilePhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (strTakePhotosStatus.equals("")) {
                    Toast.makeText(getApplication(), getString(R.string.str_please_take_your_photos), Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPrefTakePhotos", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                String strProfile =  pref.getString("Profile", null);

                editProfile(strProfile);

            }


        });

    }

private void editProfile(final String str){

    final String  strGetLocation =    GetLocation.get(EditProfilePhotoActivity.this);
    final Activity act = EditProfilePhotoActivity.this ;
    final ProgressDialog progress_sub = new ProgressDialog(this, R.style.MyAlertDialogStyle);
    progress_sub.setMessage(getString(R.string.str_pleasewait_inprocess));
    progress_sub.setCancelable(false); // disable dismiss by tapping outside of the dialog
    progress_sub.show();
    String url = getString(R.string.str_url_https) + "WalletEditProfilePhoto.php";
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
                                result =  cryptoHelper.decrypt(jObject.getString("result"));
                            } catch (Exception e) {
                            }
                            String[] arry = result.split("\\|");
                            if (arry[0].equals("405000000")){
                                if (StrMobileNumber.equals(arry[1])){

                                    Log.e("result:",  result);

                                   String strPhotosID = arry[2] ;
                                    //LoadSMSSetting(strSMSSetting);
                                   // Toast.makeText(getApplication(), getString(R.string.str_successful), Toast.LENGTH_LONG).show();

                                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPrefTakePhotos", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();
                                    String strProfile =  pref.getString("Profile", "");

                                    SharedPreferences.Editor editor1 = getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_PROFILE_STORE, Context.MODE_PRIVATE).edit();
                                    editor1.putString(GlabaleParameter.PREFS_ETL_PROFILE_PHOTOS_STORE, strProfile );
                                    editor1.putString(GlabaleParameter.PREFS_ETL_PROFILE_ID_STORE, strPhotosID );
                                    editor1.apply();


                                 dialog(getString(R.string.str_edit_profile_photo_successful),1) ;  // 1:Success, 2:Fail, 3:Error
                                   // dialog(result,1) ;  // 1:Success, 2:Fail, 3:Error
                                    // Toast.makeText(getApplication(),""+arry[1], Toast.LENGTH_LONG).show();
                                    // finish();
                                }
                            }else{
                               // Toast.makeText(getApplication(),""+arry[1], Toast.LENGTH_LONG).show();
                                dialog(""+arry[1],2) ;  // 1:Success, 2:Fail, 3:Error

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

            strBasic_info +=  "|"+str;
            //  Log.e("strSMSSetting11:",  strBasic_info);
            String encryptString = "";
            CryptoHelper cryptoHelper = new CryptoHelper();
            try {
                encryptString =  cryptoHelper.encrypt(strBasic_info);
            } catch (Exception e) {
            }
            params.put("publickey","EditProfilePhoto");
            params.put("Active_values", encryptString);
            // Log.e("Active_valuesTransfer:",  encryptString);
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


    private void dialog(final String Str, final int ico) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Str);
        builder.setCancelable(false)
                .setPositiveButton(R.string.str_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

if (ico==1) {
    SharedPreferences setting_logout_session = getSharedPreferences(GlabaleParameter.PREFS_SESSION, MODE_PRIVATE);
    SharedPreferences.Editor edt_session = setting_logout_session.edit();
    edt_session.putString(GlabaleParameter.PREFS_SESSION, "0");
    edt_session.commit();
    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(intent);

    //Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
    //startActivity(intent);
    finish();
}
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_icon_close, menu);
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.actionbar_layout_title_center);
            TextView mytext = (TextView) findViewById(R.id.txtcenter);
            mytext.setText(getString(R.string.str_register_agent));
        } catch (Exception e) {
        }
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            strTakePhotosStatus =  "TAKEPHOTOS";
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            imageBitmap= cropToSquare(imageBitmap);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            imgTakePhotos.setImageBitmap(imageBitmap);
            // === Covert to Base64
            byte[] byteFormat = stream.toByteArray();
            String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
            //=== Save
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPrefTakePhotos", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("Profile", imgString);  // Saving string
            editor.commit(); // commit changes
            strTakePhotosStatus =  "TAKEPHOTOS";
            btnEditProFilePhoto.setEnabled(true);
        }else{
            strTakePhotosStatus =  "";
        }
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
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
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