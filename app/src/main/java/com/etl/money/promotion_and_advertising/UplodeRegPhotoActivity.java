package com.etl.money.promotion_and_advertising;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.etl.money.LoginActivity;
import com.etl.money.R;
import com.etl.money.config.AutoLogout;
import com.etl.money.global.GlabaleParameter;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UplodeRegPhotoActivity extends AppCompatActivity {
    ImageView imageViewGallery, imgRefreshQualityOfImage;
    Button btnChooseFromGallery, btnOk;
    int PICK_IMAGE_REQUEST = 111;


    EditText txtQuality ;
    TextView txtLength ;
    String StrBase64;
    Bitmap bitmapOrg ;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uplode_reg_photo);

        imageViewGallery = (ImageView)findViewById(R.id.imageViewGallery);
        btnChooseFromGallery = (Button)findViewById(R.id.btnChooseFromGallery);
        btnOk = (Button)findViewById(R.id.btnOk);
        imgRefreshQualityOfImage = (ImageView)findViewById(R.id.imgRefreshQualityOfImage);
        txtQuality  = (EditText) findViewById(R.id.txtQuality);
        txtLength  = (TextView) findViewById(R.id.txtLength);

         BitmapDrawable drawable = (BitmapDrawable) imageViewGallery.getDrawable();
         bitmapOrg = drawable.getBitmap();
        RefreshQualityOfImage();
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
        //opening image chooser option
        btnChooseFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
            }
        });
        imgRefreshQualityOfImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefreshQualityOfImage();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveAndClose();
            }
        });

    }

    public void RefreshQualityOfImage(){

        // 1 === *** Convert Bitmap to imageView *** ===
        // imageViewGallery.setImageBitmap(bMap);
        // 2 === *** Convert imageView to Bitmap
        // BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        // Bitmap bitmap = drawable.getBitmap();
        // 3 === *** Convert Bitmap to Base64 *** ===
        // Bitmap bitmap = drawable.getBitmap();
        // ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        // byte[] byteFormat = stream.toByteArray();
        // String  StrBase64 = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        // === 4 *** Convert Base64 to Bitmap *** ===
        // ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // byte[] imageBytes = baos.toByteArray();
        // imageBytes = Base64.decode(StrBase64 + ".", Base64.DEFAULT);
        // Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        // === 5 *** Crop Image To Square *** ===
        //   int width  = bitmap.getWidth();
        //  int height = bitmap.getHeight();
        //  int newWidth = (height > width) ? width : height;
        //  int newHeight = (height > width)? height - ( height - width) : height;
        //  int cropW = (width - height) / 2;
        //   cropW = (cropW < 0)? 0: cropW;
        //   int cropH = (height - width) / 2;
        //  cropH = (cropH < 0)? 0: cropH;
        //  Bitmap cropImg = Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);

        // 3 === *** Convert Bitmap to Base64 *** ===

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmapOrg.compress(Bitmap.CompressFormat.JPEG, Integer.parseInt( txtQuality.getText().toString()+"") , stream);
        byte[] byteFormat = stream.toByteArray();
        StrBase64 = Base64.encodeToString(byteFormat, Base64.NO_WRAP);


        // === 4 *** Convert Base64 to Bitmap *** ===
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        imageBytes = Base64.decode(StrBase64 + ".", Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        // 1 === *** Convert Bitmap to imageView *** ===
        imageViewGallery.setImageBitmap(decodedImage);



        imageViewGallery.setImageBitmap(decodedImage);
        Integer StrBase64Len = StrBase64.length();
        txtLength.setText( new DecimalFormat("#,###,###").format(Integer.parseInt( StrBase64Len+""))+"/60,000");

        StrBase64 = StrBase64;

        if (StrBase64.length()>60000){
            btnOk.setEnabled(false);
        }else{
            btnOk.setEnabled(true);
        }


    }
    private void SaveAndClose(){

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPrefTakePhotos", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Profile", StrBase64);  // Saving string
        editor.commit(); // commit changes
        Intent intent=new Intent();
        intent.putExtra("strExtra","TAKEPHOTOS");
        setResult(2,intent);

        finish();//finishing activity
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
        float h =  newHeight;


        double doubleHeight = newHeight;
        double doubleNumber1 = doubleHeight/5;
        double doubleNumber3 = 3.75;
        double doubleNumber5 = doubleNumber1*doubleNumber3;
        int Height1 = (int) doubleNumber5;

        Bitmap cropImg = Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, Height1);
        return cropImg;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();

            try {
                //getting image from gallery
              //  bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                bitmapOrg =   cropToSquare(MediaStore.Images.Media.getBitmap(getContentResolver(), filePath));

                //Setting image to ImageView
                imageViewGallery.setImageBitmap(bitmapOrg);
                RefreshQualityOfImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
            mytext.setText(R.string.str_choose_from_gallery);
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