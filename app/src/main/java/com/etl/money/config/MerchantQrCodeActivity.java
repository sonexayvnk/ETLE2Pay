package com.etl.money.config;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import com.etl.money.NumberTextWatcher;
import com.etl.money.R;
import com.etl.money.devise_info.IMEI;
import com.etl.money.global.GlabaleParameter;
import com.etl.money.security.CSSLSocketFactory;
import com.etl.money.security.CryptoHelper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MerchantQrCodeActivity extends AppCompatActivity {
    String StrMobileNumber ;
    String StrCustomename ;
    String strStep = "0";
    //Context context;
    final Context context = this;
    TextView txtTitle,txtQrType,txtDescription,txtDetail,txtCustomPrice;
    Button btnCancel,btnMyQr;
    Dialog dialog ;
    EditText txtPrice ;

    ImageView image_view ;
    LinearLayout LinearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        SharedPreferences msn = getSharedPreferences(GlabaleParameter.PREFS_MSISDN, Context.MODE_PRIVATE);
        String Msisdn = msn.getString("msisdn", "");

        txtTitle = findViewById(R.id.txtTitle);
        txtQrType = findViewById(R.id.txtQrType);
        txtDescription =  findViewById(R.id.txtDescription);
        txtDetail =  findViewById(R.id.txtDetail);
        txtPrice =  findViewById(R.id.txtPrice);
        btnMyQr =  findViewById(R.id.btnMyQr);
        btnCancel =  findViewById(R.id.btnCancel);
        txtCustomPrice =  findViewById(R.id.txtCustomPrice);
        image_view  =  findViewById(R.id.image_view);
        LinearLayout = (LinearLayout)findViewById(R.id.LinearLayout);
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.window_popup_qr_code_type);
        // btnMyQr.setVisibility(View.GONE);
        LoadUserInfor(Msisdn);

        txtPrice.addTextChangedListener(new NumberTextWatcher(txtPrice));

        btnCancel.setVisibility(View.GONE);
        txtPrice.setVisibility(View.GONE);
        txtCustomPrice.setVisibility(View.GONE);


    }

    private void LoadUserInfor(String MobileNumber){
        StrMobileNumber = MobileNumber;

        final ProgressDialog progress_sub = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        progress_sub.setMessage(getString(R.string.str_pleasewait_inprocess));
        progress_sub.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress_sub.show();
        String url = getString(R.string.str_url_https) + "WalletMerchantInfo.php";
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
                                        String[] arryData = strdecrypt.split("\\|");
                                        String[] ArrB = arryData[0].split( ";" );
                                        StrCustomename = ArrB[1] ;


                                      //  initQRCode(StrMobileNumber,StrCustomename,"MyQr");
                                        initQRCode(StrMobileNumber,"MerchantQR","0",StrCustomename);
                                    }
                                }else{
                                    Toast.makeText(getApplication(), getString(R.string.str_error_operation_fail93), Toast.LENGTH_LONG).show();


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

    public void onClickCancel(View v) {
        btnCancel.setVisibility(View.GONE);
        txtPrice.setVisibility(View.GONE);
        txtCustomPrice.setVisibility(View.GONE);
        btnMyQr.setText(getString(R.string.str_custom_price));
        initQRCode(StrMobileNumber,"MerchantQR","0",StrCustomename);
        txtPrice.setText("");
        strStep="0";
    }
    public void onClickMyQrTypr(View v) {


        if (strStep.equals("0")){

            btnCancel.setVisibility(View.VISIBLE);
            txtPrice.setVisibility(View.VISIBLE);
            txtCustomPrice.setVisibility(View.VISIBLE);
            btnMyQr.setText(getString(R.string.str_build_price));
            txtPrice.setText("");
            strStep="1";


        } else  if (strStep.equals("1")){


            String EnterPaymentAmount = txtPrice.getText().toString();
            if (EnterPaymentAmount.trim().length() < 1 ) {
                txtPrice.requestFocus();
                String str = getString(R.string.str_please_enter_amount) ;
                txtPrice.setError(str);
                return;
            }

            String strPrice =  txtPrice.getText().toString();
            if (strPrice.equals("")){strPrice="0";}
            initQRCode(StrMobileNumber,"MerchantQR",strPrice,StrCustomename);







        } else  if (strStep.equals("2")){
            btnCancel.setVisibility(View.VISIBLE);
            txtPrice.setVisibility(View.VISIBLE);
            txtCustomPrice.setVisibility(View.VISIBLE);
            btnMyQr.setText(getString(R.string.str_build_price));
            strStep="1";
            txtPrice.setText("");
        }



    }




    private void initQRCode(String Msisdn , String strQrType, String strPrice, String strCustomerName) {





        if (!strPrice.equals("0")){
            strPrice = strPrice.replace(".", "");
            strPrice = strPrice.replace(",", "");
            float floDebit =Float.parseFloat(strPrice);
            String strAmount =   new DecimalFormat("#,###,###").format(floDebit);
            txtDetail.setText(getString(R.string.str_amount)+": "+strAmount+""+" "+getString(R.string.str_kip));
            txtDetail.setVisibility(View.VISIBLE);
            txtQrType.setText("QrPay(SmartPay)");
            txtPrice.setVisibility(View.GONE);
            txtCustomPrice.setVisibility(View.GONE);

            btnMyQr.setText(getString(R.string.str_new_price));



            InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);




            strStep = "2";
        }else {
            txtDetail.setVisibility(View.GONE);
            txtQrType.setText("QrPay");
        }

        //txtDetail.setText("Amount: 10,000");
        txtDescription.setText(strCustomerName);


        String strDetail = "" ;
        txtTitle.setText(Msisdn);
        if  (strQrType.equals("payphone")) {

        }else if (strQrType.equals("transfer")){
        }else if (strQrType.equals("paymer")){
        }else if (strQrType.equals("cashin")){
        }else if (strQrType.equals("chashout")){
        }else if (strQrType.equals("smartpay")){
        }
                 /*
           // btnMyQr.setEnabled(false);
         //   btnQrPay.setEnabled(true);
        } else{

            txtTitle.setText(strCustomerName);
            txtQrType.setText("Qr pay");
            btnMyQr.setEnabled(true);
            btnQrPay.setEnabled(false);

            Msisdn +="|"+strCustomerName;
            try {
                byte[] valueDecoded= new byte[0];
                valueDecoded = android.util.Base64.encode(Msisdn.getBytes("UTF-8"), android.util.Base64.DEFAULT);
                Msisdn = new String(valueDecoded, "UTF-8"); // for UTF-8 encoding
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
   CryptoHelper cryptoHelper = new CryptoHelper();
        String encryptString = "";
        try {
            encryptString =  cryptoHelper.encrypt(strCustomerName);
        } catch (Exception e) {
        }



byte[] decrypt= Base64.decode(base64, Base64.DEFAULT);
String text = new String(decrypt, "UTF-8");

   */
        String encryptString = "";
        try {
            byte[] encrpt= strCustomerName.getBytes("UTF-8");
            encryptString = Base64.encodeToString(encrpt, Base64.DEFAULT);
        } catch (Exception e) {
        }




        StringBuilder textToSend = new StringBuilder();
        textToSend.append(Msisdn+"|"+strQrType+"|"+strPrice+"|"+encryptString+"|0");
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(textToSend.toString(), BarcodeFormat.QR_CODE, 900, 900);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            ImageView imageView = (ImageView) findViewById(R.id.image_view);
            imageView.setImageBitmap(bitmap);
            imageView.setVisibility(View.VISIBLE);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
    private String encodeString(String source) {
        byte[] bytes = Base64.encode(source.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING);
        return new String(bytes, Charset.defaultCharset());
    }

    @SuppressLint("WrongConstant")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_icon_close, menu);
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.actionbar_layout_title_center);
            TextView   mytext = (TextView) findViewById(R.id.txtcenter);
            mytext.setText(R.string.str_generate_qr_code_msisdn);
        } catch (Exception e) {
        }
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

    public void onClickToShare(View v) {



//        final LinearLayout lyt = (LinearLayout) findViewById(R.id.LinearLayout);
//        lyt.setDrawingCacheEnabled(true);
//        lyt.buildDrawingCache(true);
//
//        final   Bitmap b = Bitmap.createBitmap(lyt.getDrawingCache());
//        Canvas canvas = new Canvas(b);
//        final   ImageView img = (ImageView) findViewById(R.id.image_view);
//        img.setImageBitmap(b);






        try {


            LinearLayout.setDrawingCacheEnabled(true);
            // this is the important code :)
            // Without it the view will have a dimension of 0,0 and the bitmap will be null
            //   LinearLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            //       View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            //  LinearLayout.layout(0, 0, LinearLayout.getMeasuredWidth(), LinearLayout.getMeasuredHeight());
            LinearLayout.buildDrawingCache(true);
            Bitmap bitmap = Bitmap.createBitmap(LinearLayout.getDrawingCache());
            LinearLayout.setDrawingCacheEnabled(false); // clear drawing cache
            //  image_view.setImageBitmap(bitmap);

//            LinearLayout view = (LinearLayout)findViewById(R.id.LinearLayout);
//            view.setDrawingCacheEnabled(true);
//            view.buildDrawingCache();
//            Bitmap bitmap =  view.getDrawingCache();;
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, getImageUri(this, bitmap));
            shareIntent.setType("image/jpeg");
            startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.str_share)));
        }catch (Exception e){
            e.getMessage();
        }


    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(),
                    inImage, "", "");
            return Uri.parse(path);
        }catch (Exception e){
            e.getMessage();
        }
        return null;
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