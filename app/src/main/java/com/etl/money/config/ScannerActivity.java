package com.etl.money.config;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.etl.money.R;
import com.etl.money.global.GlabaleParameter;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class ScannerActivity extends Activity implements
        DecoratedBarcodeView.TorchListener {

    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private Button switchOnFlashlightButton,btnChooseFromGallery;
    private TextView txt_switch_flashlight;
    private Button switchOffFlashlightButton;
    private boolean isFlashLightOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);



        Button   btnClose = (Button) findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //   Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        // setSupportActionBar(toolbar);
        //Initialize barcode scanner view
        barcodeScannerView = (DecoratedBarcodeView) findViewById(R.id.zxing_barcode_scanner);
        //set torch listener
        barcodeScannerView.setTorchListener(this);
        //switch flashlight button
        switchOnFlashlightButton = (Button) findViewById(R.id.switch_on_flashlight);
        switchOffFlashlightButton = (Button) findViewById(R.id.switch_off_flashlight);
        btnChooseFromGallery = (Button) findViewById(R.id.btnChooseFromGallery);
        txt_switch_flashlight= (TextView) findViewById(R.id.txt_switch_flashlight);
        // if the device does not have flashlight in its camera,
        // then remove the switch flashlight button...
        switchOffFlashlightButton.setVisibility(View.GONE);
        switchOnFlashlightButton.setVisibility(View.GONE);
        if (!hasFlash()) {
            //  switchOnFlashlightButton.setVisibility(View.GONE);
            txt_switch_flashlight.setVisibility(View.GONE);
        } else {
            switchOffFlashlightButton.setVisibility(View.VISIBLE);
            switchOnFlashlightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switchFlashlight();
                    switchOnFlashlightButton.setVisibility(View.GONE);
                    switchOffFlashlightButton.setVisibility(View.VISIBLE);
                    txt_switch_flashlight.setText(R.string.str_turn_on_flashlight);

                }
            });
            switchOffFlashlightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switchFlashlight();
                    switchOnFlashlightButton.setVisibility(View.VISIBLE);
                    switchOffFlashlightButton.setVisibility(View.GONE);
                    txt_switch_flashlight.setText(R.string.str_turn_off_flashlight);
                }
            });
        }
        btnChooseFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent refresh1 = new Intent(getApplicationContext(), QrCodeScanInGalleryActivity.class);
                startActivity(refresh1);
                SharedPreferences.Editor editor1 = getSharedPreferences(GlabaleParameter.PREFS_QR_CODE_GALLERY, Context.MODE_PRIVATE).edit();
                editor1.putString(GlabaleParameter.PREFS_QR_CODE_GALLERY_NUMBER, "");
                editor1.commit();
                finish();
            }
        });

        //start capture
        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();
    }


    /**
     * Check if the device's camera has a Flashlight.
     *
     * @return true if there is Flashlight, otherwise false.
     */
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public void switchFlashlight() {
        if (isFlashLightOn) {
            barcodeScannerView.setTorchOff();
            isFlashLightOn = false;
        } else {
            barcodeScannerView.setTorchOn();
            isFlashLightOn = true;
        }

    }

    @Override
    public void onTorchOn() {

    }

    @Override
    public void onTorchOff() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

}