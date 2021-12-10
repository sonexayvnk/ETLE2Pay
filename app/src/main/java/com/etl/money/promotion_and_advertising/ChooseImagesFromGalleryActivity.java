package com.etl.money.promotion_and_advertising;

import static com.etl.money.config.DeviceListActivity.EXTRA_DEVICE_ADDRESS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.etl.money.LoginActivity;
import com.etl.money.R;
import com.etl.money.config.AutoLogout;
import com.etl.money.global.GlabaleParameter;
import com.etl.money.register.RegisterNewAgentActivity;
import com.etl.money.security.GetLocation;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChooseImagesFromGalleryActivity extends AppCompatActivity implements View.OnClickListener {
    String StrBase64;
    Bitmap bitmapOrg ;
    //initialize variables to make them global
    private ImageView imageViewGallery;
    private Button btnChooseFromGallery,btnOk;
    EditText txtQuality ;
    TextView txtLength ;

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private String strQrCodeGallery = "";

    private static final int SELECT_PHOTO = 100;
    //for easy manipulation of the result
    public String barcode;
    //call oncreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_images_from_gallery);

        //cast neccesary variables to their views
        imageViewGallery = (ImageView)findViewById(R.id.imageViewGallery);
        btnChooseFromGallery = (Button)findViewById(R.id.btnChooseFromGallery);
        txtQuality  = (EditText) findViewById(R.id.txtQuality);
        txtLength  = (TextView) findViewById(R.id.txtLength);
        btnOk = (Button)findViewById(R.id.btnOk);
        strQrCodeGallery = "";


        //set a new custom listener
        btnChooseFromGallery.setOnClickListener(this);
        btnOk.setOnClickListener(this);

        //launch gallery via intent
        Intent photoPic = new Intent(Intent.ACTION_PICK);
        photoPic.setType("image/*");
        startActivityForResult(photoPic, SELECT_PHOTO);

/*
        BitmapDrawable drawable = (BitmapDrawable) imageViewGallery.getDrawable();
        bitmapOrg =   cropToSquare(drawable.getBitmap());
        RefreshQualityOfImage();

 */
    }

    //do necessary coding for each ID
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnChooseFromGallery:
                //launch gallery via intent
                //check runtime permission
      /*          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED){
                        //permission not granted, request it.
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        //show popup for runtime permission
                        requestPermissions(permissions, PERMISSION_CODE);
                    }
                    else {
                        //permission already granted
                        pickImageFromGallery();
                    }
                }
                else {
                    //system os is less then marshmallow
                    pickImageFromGallery();
                }
                break;

*/

            //launch gallery via intent
                /*
            Intent photoPic = new Intent(Intent.ACTION_PICK);
            photoPic.setType("image/*");
            startActivityForResult(photoPic, SELECT_PHOTO);
            */
                //launch gallery via intent
                Intent photoPic = new Intent(Intent.ACTION_PICK);
                photoPic.setType("image/*");
                startActivityForResult(photoPic, SELECT_PHOTO);

            case R.id.btnOk:
                saveActivity();
                //launch gallery via intent
                /*
                SharedPreferences.Editor editor1 = getSharedPreferences(GlabaleParameter.PREFS_QR_CODE_GALLERY, Context.MODE_PRIVATE).edit();
                editor1.putString(GlabaleParameter.PREFS_QR_CODE_GALLERY_NUMBER, ""+strQrCodeGallery);
                editor1.commit();

                Intent intent = new Intent();
                intent.putExtra(EXTRA_DEVICE_ADDRESS,""+strQrCodeGallery);
                setResult(Activity.RESULT_OK, intent);*/
                finish();
                break;
        }
    }
    private void pickImageFromGallery() {
        //intent to pick image
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    //handle result of runtime permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if (grantResults.length >0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    //permission was granted
                    pickImageFromGallery();
                }
                else {
                    //permission was denied
                    Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    //call the onactivity result method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case SELECT_PHOTO:
                try {
                    Toast.makeText(getApplication(), "KKKKKKKKKKKKKKKKK", Toast.LENGTH_LONG).show();

                    if (resultCode == RESULT_OK) {
                      //  btnOk.setEnabled(false);


                        /*
                        String strWalletNumber =null ;
//doing some uri parsing
                        Uri selectedImage = imageReturnedIntent.getData();
                        InputStream imageStream = null;
                        try {
                            //getting the image
                            imageStream = getContentResolver().openInputStream(selectedImage);
                            btnOk.setEnabled(true);
                        } catch (FileNotFoundException e) {
                            Toast.makeText(getApplicationContext(), "File not found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        //decoding bitmap
                       Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
*/
                        //  Bitmap bitmap =   cropToSquare(BitmapFactory.decodeStream(imageStream));

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


/*

                         ByteArrayOutputStream stream = new ByteArrayOutputStream();
                         bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                         byte[] byteFormat = stream.toByteArray();
                         String  StrBase64 = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

                        imgString = StrBase64;


                         ByteArrayOutputStream baos = new ByteArrayOutputStream();
                         byte[] imageBytes = baos.toByteArray();
                         imageBytes = Base64.decode(StrBase64 + ".", Base64.DEFAULT);
                         Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                        imageViewGallery.setImageBitmap(decodedImage);

                        Log.e("20210118Base64abcV1: (", ""+StrBase64);

                      //  imageViewGallery.setImageURI(selectedImage);// To display selected image in image view
*/


                        imageViewGallery.setImageURI(imageReturnedIntent.getData());
                         BitmapDrawable drawable = (BitmapDrawable) imageViewGallery.getDrawable();
                         bitmapOrg =   cropToSquare(drawable.getBitmap());




                        RefreshQualityOfImage();




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
    public void  saveActivity(){
       //  BitmapDrawable drawable = (BitmapDrawable) imgTakePhotos.getDrawable();


        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPrefTakePhotos", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Profile", StrBase64);  // Saving string
        editor.commit(); // commit changes
        Intent intent=new Intent();
        intent.putExtra("strExtra","TAKEPHOTOS");
        setResult(2,intent);

        finish();//finishing activity
    }




    public Bitmap getRoundedShape (Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
       canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);


        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }
    public Bitmap getCroppedBitmap(Bitmap scaleBitmapImage) {
        // TODO Auto-generated method stub
        int targetWidth = 200;
        int targetHeight = 200;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
       /* path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);*/

        //canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth,
                        targetHeight), null);
        return targetBitmap;
    }
    public void onClickRefreshQualityOfImage(View v){
        RefreshQualityOfImage();
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