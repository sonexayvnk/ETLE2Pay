package com.etl.money.cash;

import static java.lang.String.format;

import androidx.appcompat.app.AppCompatActivity;

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
import android.os.Handler;
import android.provider.MediaStore;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AutoGenerateQRCode extends AppCompatActivity {
    String StrMobileNumber ;
    String StrCustomename ;
    String strStep = "0";
    //Context context;
    final Context context = this;
    TextView txtTitle,txtQrType,txtDescription,txtDetail, txtCountdown,txtReferenceID;
    Button btnCancel,btnMyQr,btnShare;
    Dialog dialog ;
    int countdown=0;

    String outputDate;

    ImageView image_view ;
    android.widget.LinearLayout LinearLayout;

    private final Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_generate_qrcode);
        SharedPreferences msn = getSharedPreferences(GlabaleParameter.PREFS_MSISDN, Context.MODE_PRIVATE);
        String Msisdn = msn.getString("msisdn", "");

        txtTitle = findViewById(R.id.txtTitle);
        txtQrType = findViewById(R.id.txtQrType);
        txtDescription =  findViewById(R.id.txtDescription);
        txtDetail =  findViewById(R.id.txtDetail);
        txtCountdown =  findViewById(R.id.txtCountdown);
        txtReferenceID =  findViewById(R.id.txtReferenceID);
        btnMyQr =  findViewById(R.id.btnMyQr);
        btnCancel =  findViewById(R.id.btnCancel);
        btnShare =  findViewById(R.id.btnShare);
        image_view  =  findViewById(R.id.image_view);
        LinearLayout = (LinearLayout)findViewById(R.id.LinearLayout);

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.window_popup_qr_code_type);
        // btnMyQr.setVisibility(View.GONE);
        getInfor(Msisdn);



    }

    private void getInfor(String MobileNumber){
        StrMobileNumber = MobileNumber;
        String Msisdn = MobileNumber;


        SharedPreferences pref = getApplicationContext().getSharedPreferences(GlabaleParameter.PREFS_CASHINREQUEST, MODE_PRIVATE);

      String  str = pref.getString("strValues", "");
        String[] arry = str.split("\\|");

        String strQrType  = arry[5].split(";")[1];
        String strCustomerName =  arry[1].split(";")[1];
        String strPrice = arry[3].split(";")[1];

        String referenceCode =arry[4].split(";")[1];
        String referenceID =arry[7].split(";")[1];


        //txtDetail.setText("Amount: 10,000");

        txtDetail.setText(getString(R.string.str_amount)+": "+strPrice+""+" "+getString(R.string.str_kip));
        txtQrType.setText(strQrType);
        txtDescription.setText(strCustomerName);
        txtTitle.setText(Msisdn);
        countdown=Integer.parseInt(arry[6].split(";")[1] + "") ;
        txtReferenceID.setText(referenceID);  ;
       // referenceID=90 ;;

        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(currentDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.SECOND, Integer.parseInt(arry[6].split(";")[1] + ""));  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        outputDate = sdf1.format(c.getTime());

        runnable.run();
        String encryptString = "";
        try {
            byte[] encrpt= strCustomerName.getBytes("UTF-8");
            encryptString = Base64.encodeToString(encrpt, Base64.DEFAULT);
        } catch (Exception e) {
        }
        String Qrstr = Msisdn+"|"+strQrType+"|"+strPrice+"|"+encryptString+"|"+referenceCode+"|"+referenceID;
        GenerateQRCode(Qrstr);
    }
    public void onClickCancel(View v) {
  finish();
    }

    private void GenerateQRCode(String str) {
        StringBuilder textToSend = new StringBuilder();
        textToSend.append(str);
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
        try {
            LinearLayout.setDrawingCacheEnabled(true);
            LinearLayout.buildDrawingCache(true);
            Bitmap bitmap = Bitmap.createBitmap(LinearLayout.getDrawingCache());
            LinearLayout.setDrawingCacheEnabled(false); // clear drawing cache
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

    private Runnable runnable = new Runnable() {
        public void run() {
            Date dateNow = new Date();
           Date dateSwcond = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                dateSwcond = format.parse(outputDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            String remaining = DateUtils.formatElapsedTime ((dateSwcond.getTime() - dateNow.getTime())/1000); // Remaining time to seconds

            txtCountdown.setText(getString(R.string.str_countdown)+": "+remaining+"");
            countdown--;
            handler.postDelayed(this, 1000);

            if (countdown<=0){finish();}
        }
    };

}