package com.etl.money.register;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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
import com.etl.money.TestActivity;
import com.etl.money.devise_info.IMEI;
import com.etl.money.global.GlabaleParameter;
import com.etl.money.security.CSSLSocketFactory;
import com.etl.money.security.CryptoHelper;
import com.etl.money.security.GetLocation;
import com.etl.money.setting.UserWalletInfoActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;

    // Camera camera;

    private EditText txtMobileNumber, txtFirstName,txtLastName,txtOccupation;
    private TextView txtDateOfBirth;
    RadioButton rdiMale,rdiFemale ;
    RadioGroup rdiGender ;
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

    Uri image_uri;
    private String strFirstPhotosBase64 ;
    private String strTakePhotosStatus = "" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // takePictureButton = (Button) findViewById(R.id.button_image);
        txtMobileNumber = (EditText) findViewById(R.id.txtMobilenumber);
        txtFirstName = (EditText) findViewById(R.id.txtFirstName);
        txtLastName = (EditText) findViewById(R.id.txtLastName);
        rdiGender = (RadioGroup) findViewById(R.id.rdiGender);
        rdiMale = (RadioButton) findViewById(R.id.rdiMale);
        rdiFemale = (RadioButton) findViewById(R.id.rdiFemale);
        txtDateOfBirth = (TextView) findViewById(R.id.txtDateOfBirth);
        txtOccupation = (EditText) findViewById(R.id.txtOccupation);
        imgTakePhotos = findViewById(R.id.imgTakePhotos);



        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPrefTakePhotos", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Profile", "");  // Saving string
        editor.commit(); // commit changes



        SharedPreferences msn = getSharedPreferences(GlabaleParameter.PREFS_MSISDN, Context.MODE_PRIVATE);
        strMsisdn = msn.getString("msisdn", "");
        // ActionBar actionbar = getActionBar();
        // actionbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));

        //=== Image Base64 ===
        /*
        BitmapDrawable drawable = (BitmapDrawable) imgTakePhotos.getDrawable();
        Bitmap bitmap1 = drawable.getBitmap();
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
         strFirstPhotosBase64 =Base64.encodeToString(b, Base64.DEFAULT);
*/
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

/*
        txtMobileNumber.setText("2029996595");
        txtFirstName.setText("Soexay");
        txtLastName.setText("VANNAKHONE");
        rdiMale.setChecked(true);
        txtDateOfBirth.setText("13-02-1984");
*/
        btn_Submit_register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Toast.makeText(getApplication(), "IMEI=" + get_dev_id(), Toast.LENGTH_SHORT).show();

                String strMobileNumber = txtMobileNumber.getText().toString();
                String strFirstName = txtFirstName.getText().toString();
                String strLastName = txtLastName.getText().toString();
                String strSex = "";
                if (rdiMale.isChecked()) {
                    strSex =  "1001";
                }  if (rdiFemale.isChecked()) {
                    strSex =  "1002";
                }


                String strDateOfBirth = txtDateOfBirth.getText().toString();
                String strOccupation = txtOccupation.getText().toString();

                //=== Image Base64 ===
                /*
                BitmapDrawable drawable = (BitmapDrawable) imgTakePhotos.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                byte[] byteFormat = stream.toByteArray();*/
/*
                BitmapDrawable drawable = (BitmapDrawable) imgTakePhotos.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 5, stream);
                byte[] byteFormat = stream.toByteArray();
                // get the base 64 string
                String strPhotosBase64 = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
*/
                // strFirstPhotosBase64 =imgString;
                //     Log.e("strBase642021210117:", "" + strPhotosBase64);
                //  strFirstPhotosBase64 =Base64.encodeToString(b, Base64.DEFAULT);
                // get the base 64 string




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


                if (strFirstName.trim().length() < 5 || strFirstName.trim().length() > 35) {
                    //  txtFirstName.requestFocus();
                    txtFirstName.setError(getString(R.string.str_Please_use_between_6_and_35_characters));
                    //Toast.makeText(getApplication(), getString(R.string.str_full_name_is_invalid), Toast.LENGTH_SHORT).show();
                    return;
                } else {

                }
                if (strLastName.trim().length() < 5 || strLastName.trim().length() > 35) {
                    txtLastName.requestFocus();
                    txtLastName.setError(getString(R.string.str_Please_use_between_6_and_35_characters));
                    return;
                } else {

                }

                if (strSex.equals("")) {
                    rdiGender.requestFocus();
                    // txtLastName.setError(getString(R.string.str_Please_use_between_6_and_35_characters));
                    Toast.makeText(getApplication(), getString(R.string.str_please_seclect_gender), Toast.LENGTH_SHORT).show();
                    return;
                } else {

                }

                if (strDateOfBirth.trim().length() > 9) {
                    strDateOfBirth = strDateOfBirth.substring(6,10)+ "-"+strDateOfBirth.substring(3,5)+ "-"+strDateOfBirth.substring(0,2);


                } else {
                    //   txtDateOfBirth.requestFocus();
                    //  txtDateOfBirth.setError(getString(R.string.str_Please_use_between_6_and_35_characters));
                    Toast.makeText(getApplication(), getString(R.string.str_please_enter_date_of_birth), Toast.LENGTH_SHORT).show();

                    return;
                }


                if (strTakePhotosStatus.equals("")) {
                    //  rdiGender.requestFocus();
                    // txtLastName.setError(getString(R.string.str_Please_use_between_6_and_35_characters));
                    Toast.makeText(getApplication(), getString(R.string.str_please_take_your_photos), Toast.LENGTH_SHORT).show();
                    return;
                }
                //String ss=  strDateOfBirth ;
                //strDateOfBirth = ss.substring(0,2)+"-"+ ss.substring(3,5)+"-"+ ss.substring(6,10);
                //   Toast.makeText(getApplication(), "is"+strDateOfBirth, Toast.LENGTH_SHORT).show();
                String regiterValues = strMobileNumber + "|" + strFirstName + "|" + strLastName+ "|" + strSex+ "|" + strDateOfBirth+ "|" + strOccupation ;
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

//                String StrBase64 = getResources().getString(R.string.img_take_photos);
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                byte[] imageBytes = baos.toByteArray();
//                imageBytes = Base64.decode(StrBase64 + ".", Base64.DEFAULT);
//                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//                imgTakePhotos.setImageBitmap(decodedImage);



                //   Intent refresh1 = new Intent(getApplicationContext(), TakePhotosActivity.class);
                //  startActivity(refresh1);
                dispatchTakePictureIntent();
             //   Intent intent=new Intent(RegisterActivity.this,TakePhotosActivity.class);
            //   startActivityForResult(intent, 2);// Activity is started with requestCode 2
                /*
                CameraKitView1.captureImage(new CameraKitView.ImageCallback() {
                    @Override
                    public void onImage(CameraKitView cameraKitView, byte[] bytes) {
                        Bitmap capturedBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        GetImage (  capturedBitmap );
                        cameraKitView.onPause();
                       // SuperPause();
                     //    imgTakePhotos.setVisibility(View.VISIBLE);
                      //   CameraKitView1.setVisibility(View.INVISIBLE);

                        // super.onPause();
//                        yImageView2.setImageBitmap(capturedBitmap);
//                        String resultString = runModel(capturedBitmap);
//                        resultTextView.setText(initialCapital(resultString));
//                        progressBar.setVisibility(View.GONE);

                    }
                });

                CameraKitView1.onPause();

                 */
            }
        });


/*
       // === Convert Base64 to Image
        String StrBase64 =  getString(R.string.img_take_photos);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        imageBytes = Base64.decode(StrBase64 +".", Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        imgTakePhotos.setImageBitmap(decodedImage);
*/

        // === Convert Image to Base64
        /*
        ImageView iv1 = (ImageView)findViewById(R.id.image_view1);
        BitmapDrawable drawable = (BitmapDrawable) iv1.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        Log.e("base64String:", temp);
         */

    }

    private String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        dob.set(year, month, day);
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }
        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();
        return ageS;
    }
    //Reponse from Server Is OK


    public void RegisterSuccess(String strPhotosID) {
        // Toast.makeText(this, "Register success, password sent to your mobile number, thank you.", Toast.LENGTH_LONG).show();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPrefTakePhotos", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String strProfile =  pref.getString("Profile", "");

        SharedPreferences.Editor editor1 = getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_PROFILE_STORE, Context.MODE_PRIVATE).edit();
        editor1.putString(GlabaleParameter.PREFS_ETL_PROFILE_PHOTOS_STORE, strProfile );
        editor1.putString(GlabaleParameter.PREFS_ETL_PROFILE_ID_STORE, strPhotosID );
        editor1.apply();

        String ms = getString(R.string.str_register_success);
        new AlertDialog.Builder(this)
                // .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.app_name)
                .setMessage(ms)
                .setPositiveButton(R.string.str_ok_only, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

// To Login page
                        toLogin();
                        //  return;
                    }

                })
                // .setNegativeButton(R.string.str_no_to_exit, null)
                .show();


    }



    private void toLogin() {
        //  Intent refresh = new Intent(this, LoginActivity.class);
        //  startActivity(refresh);

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        finish();

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_exit:
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);
        }
        return super.onKeyDown(keyCode, event);
    }




    public void Close(View v) {
        finish();
    }


    public void onClickSelectDatOfBirth(View v){
        String oldDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());



        //Given Date in String format
        // String oldDate = "2017-01-29";
        System.out.println("Date before Addition: "+oldDate);
        //Specifying date format that matches the given date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try{
            //Setting the date to the given date
            c.setTime(sdf.parse(oldDate));
        }catch(ParseException e){
            e.printStackTrace();
        }

        //Number of Days to add
        c.add(Calendar.YEAR, -30);
        //Date after adding the days to the given date
        String newDate = sdf.format(c.getTime());
        //Displaying the new Date after addition of Days


        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        DateFormat outputYear = new SimpleDateFormat("yyyy");
        DateFormat outputMonth = new SimpleDateFormat("mm");
        DateFormat outputDay = new SimpleDateFormat("dd");
        String startDateStr =newDate; //"2017-02-03";
        Date date = null;
        int selectedYear = 2000;
        int selectedMonth = 6;
        int selectedDayOfMonth = 15;
        try {
            date = inputFormat.parse(startDateStr);
            String startDateStrNewFormat = outputFormat.format(date);
            startDateStrNewFormat = outputYear.format(date);

            selectedYear = Integer.parseInt(outputYear.format(date)) ;
            selectedMonth = 6 ;
            selectedDayOfMonth = 15 ;


            //  Log.d("startDateStrNewFormat", startDateStrNewFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        // Integer.parseInt(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(newDate)




// Date Select Listener.
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {

                txtDateOfBirth.setText(  String.format("%02d", (dayOfMonth ))+ "-" +String.format("%02d", (monthOfYear + 1))   + "-" + year);
            }
        };

// Create DatePickerDialog (Spinner Mode):
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                dateSetListener, selectedYear, selectedMonth, selectedDayOfMonth);

// Show
        datePickerDialog.show();

    }





/*
    public void onClickTakePhotos(View view) {

        // Start the SecondActivity
        Intent intent = new Intent(this, TakePhotosActivity.class);
        startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
    }

    // This method is called when the second activity finishes
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that it is the SecondActivity with an OK result
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // Get String data from Intent
                String returnString = data.getStringExtra("keyName");
/*
                String StrBase64 =  returnString;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] imageBytes = baos.toByteArray();
                imageBytes = Base64.decode(StrBase64 +".", Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                imgTakePhotos.setImageBitmap(decodedImage);


              txtOccupation.setText(returnString);
            }
        }
    }
    */







    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();

        // Get the Base64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }



    private void send_Register(String str, String PhotosBase64) {
        final String  strGetLocation =    GetLocation.get(RegisterActivity.this);
        final String regiterValues = str;
        final String strTakePhotosBase64 = PhotosBase64;
        final Activity act = RegisterActivity.this ;
        final ProgressDialog progress_sub = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        progress_sub.setMessage(getString(R.string.str_pleasewait_inprocess));
        progress_sub.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress_sub.show();

        String url = getString(R.string.str_url_https) + "WalletRegister.php";
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
                                    RegisterSuccess( parts[2].trim());
                                }else {
                                    Toast.makeText(getApplication(),""+parts[1], Toast.LENGTH_LONG).show();
                                    //  Toast.makeText(Register.this, getString(R.string.str_can_not_connect_to_server), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                //                          Log.e("NO OK","NO OK =");
                                Toast.makeText(RegisterActivity.this, getString(R.string.str_can_not_connect_to_server), Toast.LENGTH_LONG).show();

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
                String Strlanguage     = "";
                String StrdeviceInfo   = "";
                String Strtoken        = "";
                String StrjwtToken     = "";
                // StrdeviceInfo    = strGetLocation+"~"+StrdeviceInfo;
                StrdeviceInfo    = "";
                String strBasic_info   = strMsisdn+"|"+verCode+"|"+currentDate+"|"+Strlanguage+"|"+StrdeviceInfo+"|"+Strtoken+"|"+StrjwtToken+"|"+regiterValues+"|"+strTakePhotosBase64;
                // ===== End StrBasic info

                CryptoHelper cryptoHelper = new CryptoHelper();
                String encryptString = "";
                String encryptPhotosBase64 = "";
                try {
                    encryptString =  cryptoHelper.encrypt(strBasic_info);
                    encryptPhotosBase64 =  cryptoHelper.encrypt(strTakePhotosBase64);
                } catch (Exception e) {
                }
                params.put("publickey", "Register");
                params.put("Active_values", encryptString);
                params.put("TakePhotosBase64", encryptPhotosBase64);
/*
                Log.e("Active_valuesQa1:", ""+encryptString);
                Log.e("strBasic_info:", ""+strBasic_info);
                Log.e("strTaotosBase642155:", ""+encryptPhotosBase64);
                Log.e("20210118Base64abcV2: (", ""+strTakePhotosBase64);
                //  Log.e("strTakePhotosBase64:", ""+strTakePhotosBase64);
*/


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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_icon_close, menu);
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.actionbar_layout_title_center);
            TextView mytext = (TextView) findViewById(R.id.txtcenter);
            mytext.setText(getString(R.string.str_register));
        } catch (Exception e) {
        }
        return true;
    }
}
