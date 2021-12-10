package com.etl.money.register;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.camerakit.CameraKit;
import com.camerakit.CameraKitView;
import com.etl.money.LoginActivity;
import com.etl.money.MainActivity;
import com.etl.money.R;
import com.etl.money.TestActivity;

import java.io.ByteArrayOutputStream;


public class TakePhotosActivity extends AppCompatActivity {
    private CameraKitView cameraKitView;
    String FACING_SWITCH = "BACK";
    Bitmap capturedBitmap ;
    ImageView myImageView1, myImageView2;
    Button btnCapture, btnResume, btnSwitchCamera, btnDone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photos);
        cameraKitView = findViewById(R.id.camerakit);

        btnCapture = findViewById(R.id.btnCapture);
        btnResume = findViewById(R.id.btnResume);
        btnSwitchCamera = findViewById(R.id.btnSwitchCamera);
        btnDone = findViewById(R.id.btnDone);
        myImageView2 =  findViewById(R.id.myImageView2);
        cameraKitView.setFacing(CameraKit.FACING_BACK);


        btnResume.setVisibility(View.INVISIBLE);
        btnDone.setVisibility(View.GONE);

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnResume.setVisibility(View.VISIBLE);
                btnDone.setVisibility(View.VISIBLE);
                btnCapture.setVisibility(View.GONE);
                btnSwitchCamera.setVisibility(View.INVISIBLE);
                cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                    @Override
                    public void onImage(CameraKitView cameraKitView, byte[] bytes) {
                        Bitmap capturedBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        // btnDone.setVisibility(View.VISIBLE);
                        //   btnCapture.setVisibility(View.GONE);

                        //  btnSwitchCamera.setVisibility(View.VISIBLE);
                        GetImage(capturedBitmap );
                        cameraKitView.onPause();

                        // super.onPause();
//                        yImageView2.setImageBitmap(capturedBitmap);
//                        String resultString = runModel(capturedBitmap);
//                        resultTextView.setText(initialCapital(resultString));
//                        progressBar.setVisibility(View.GONE);

                    }
                });
            }
        });

        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // cameraKitView.onPause();
                restartActivity();
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // cameraKitView.onPause();
                //  restartActivity();
                saveActivity();

            }
        });
        btnSwitchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // cameraKitView.onPause();
                //  restartActivity();
                if (FACING_SWITCH.equals("FRONT")){
                    cameraKitView.setFacing(CameraKit.FACING_BACK);
                    FACING_SWITCH= "BACK";
                } else if (FACING_SWITCH.equals("BACK")){
                    cameraKitView.setFacing(CameraKit.FACING_FRONT);
                    FACING_SWITCH= "FRONT";
                }


            }
        });


    }
    public void  saveActivity(){
        //  BitmapDrawable drawable = (BitmapDrawable) imgTakePhotos.getDrawable();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        capturedBitmap.compress(Bitmap.CompressFormat.JPEG, 18, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPrefTakePhotos", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Profile", imgString);  // Saving string
        editor.commit(); // commit changes
        Intent intent=new Intent();
        intent.putExtra("strExtra","TAKEPHOTOS");
        setResult(2,intent);

        finish();//finishing activity
    }
    public void restartActivity()
    {
        Intent intent= new Intent(this, TakePhotosActivity.class);
        startActivity(intent);
        finish();
    }
    private void  GetImage(Bitmap bmp ){
        try {
            capturedBitmap = bmp ;
        } catch (Exception e) {
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_exit:
                Intent intent=new Intent();
                intent.putExtra("strExtra","");
                setResult(2,intent);
                finish();//finishing activity

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
            mytext.setText(R.string.str_take_photos);
        } catch (Exception e) {
        }
        return true;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent=new Intent();
            intent.putExtra("strExtra","");
            setResult(2,intent);
            finish();//finishing activity

        }
        return super.onKeyDown(keyCode, event);
    }






    @Override
    protected void onStart() {
        super.onStart();
        //cameraKitView.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();


    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraKitView.onResume();

    }

    @Override
    protected void onPause() {
        cameraKitView.onPause();

        super.onPause();
    }

    @Override
    protected void onStop() {
        //  cameraKitView.onStop();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}