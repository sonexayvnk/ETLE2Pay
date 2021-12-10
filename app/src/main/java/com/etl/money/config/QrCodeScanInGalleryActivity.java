package com.etl.money.config;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.etl.money.R;
import com.etl.money.global.GlabaleParameter;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.etl.money.config.DeviceListActivity.EXTRA_DEVICE_ADDRESS;

public class QrCodeScanInGalleryActivity extends AppCompatActivity implements View.OnClickListener {

    //initialize variables to make them global
    private ImageView imageViewGallery;
    private Button btnChooseFromGallery,btnOk;
    private TextView txtWalletAccount;

    private String strQrCodeGallery = "";

    private static final int SELECT_PHOTO = 100;
    //for easy manipulation of the result
    public String barcode;

    //call oncreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_scan_in_gallery);

        //cast neccesary variables to their views
        imageViewGallery = (ImageView)findViewById(R.id.imageViewGallery);
        btnChooseFromGallery = (Button)findViewById(R.id.btnChooseFromGallery);
        btnOk = (Button)findViewById(R.id.btnOk);
        txtWalletAccount = (TextView)findViewById(R.id.txtWalletAccount);
        strQrCodeGallery = "";


        //set a new custom listener
        btnChooseFromGallery.setOnClickListener(this);
        btnOk.setOnClickListener(this);

        //launch gallery via intent
        Intent photoPic = new Intent(Intent.ACTION_PICK);
        photoPic.setType("image/*");
        startActivityForResult(photoPic, SELECT_PHOTO);

    }

    //do necessary coding for each ID
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnChooseFromGallery:
                //launch gallery via intent
                Intent photoPic = new Intent(Intent.ACTION_PICK);
                photoPic.setType("image/*");
                startActivityForResult(photoPic, SELECT_PHOTO);
                break;
            case R.id.btnOk:
                //launch gallery via intent
                SharedPreferences.Editor editor1 = getSharedPreferences(GlabaleParameter.PREFS_QR_CODE_GALLERY, Context.MODE_PRIVATE).edit();
                editor1.putString(GlabaleParameter.PREFS_QR_CODE_GALLERY_NUMBER, ""+strQrCodeGallery);
                editor1.commit();

                Intent intent = new Intent();
                intent.putExtra(EXTRA_DEVICE_ADDRESS,""+strQrCodeGallery);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
        }
    }

    //call the onactivity result method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case SELECT_PHOTO:
                try {
                if (resultCode == RESULT_OK) {
                    btnOk.setEnabled(false);
                    String strWalletNumber =null ;
//doing some uri parsing
                    Uri selectedImage = imageReturnedIntent.getData();
                    InputStream imageStream = null;
                    try {
                        //getting the image
                        imageStream = getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        Toast.makeText(getApplicationContext(), "File not found", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    //decoding bitmap
                    Bitmap bMap = BitmapFactory.decodeStream(imageStream);
                    imageViewGallery.setImageURI(selectedImage);// To display selected image in image view
                    String contents = null;
                    int[] intArray = new int[bMap.getWidth()*bMap.getHeight()];
                    //copy pixel data from the Bitmap into the 'intArray' array
                    bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());
                    LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                    Reader reader = new MultiFormatReader();
                    Result result = null;
                    try {
                        result = reader.decode(bitmap);
                    } catch (NotFoundException e) {
                        e.printStackTrace();
                    } catch (ChecksumException e) {
                        e.printStackTrace();
                    } catch (FormatException e) {
                        e.printStackTrace();
                    }
                    contents = result.getText();
                    TextView txtWalletAccount = (TextView)findViewById(R.id.txtWalletAccount);

                    if (contents.length()>20){
                        try {
                            /*
                            byte[] valueDecoded= new byte[0];
                            valueDecoded = android.util.Base64.decode(contents.getBytes("UTF-8"), android.util.Base64.DEFAULT);
                            strWalletNumber = new String(valueDecoded, "UTF-8"); // for UTF-8 encoding

                             */
                            String[] arry = contents.split("\\|");
                            strQrCodeGallery = contents;
                            strWalletNumber = arry[0];
                        } catch (Exception e) {
                        }
                    }else{
                            strQrCodeGallery = contents;
                            strWalletNumber   = contents;
                    }
                    if (strWalletNumber.trim().startsWith("202") || strWalletNumber.startsWith("302")||strWalletNumber.length()<9){
                        txtWalletAccount.setText(strWalletNumber);
                        btnOk.setEnabled(true);
                    }else  {
                        Toast.makeText(getApplication(), getString(R.string.str_qr_code_is_invalid), Toast.LENGTH_LONG).show();
                    }



             //        Log.e("aa",contents);


/*
                    SharedPreferences.Editor editor1 = getSharedPreferences(GlabaleParameter.PREFS_QR_CODE_GALLERY, Context.MODE_PRIVATE).edit();
                    editor1.putString(GlabaleParameter.PREFS_QR_CODE_GALLERY_NUMBER, ""+strQrCodeGallery);
                    editor1.commit();

                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_DEVICE_ADDRESS,""+strQrCodeGallery);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
*/




                }
                } catch (Exception e) {
                    btnOk.setEnabled(false);
                    Toast.makeText(getApplication(), getString(R.string.str_sorry_this_image_could_not_be_scaned), Toast.LENGTH_LONG).show();
                    return;
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
}