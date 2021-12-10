package com.etl.money;

import android.content.Intent;
import android.os.Bundle;


import android.view.MotionEvent;

import android.view.View;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class TestActivity extends AppCompatActivity implements View.OnTouchListener {
    private ImageView mRelLay,image2;
    private float mInitialX, mInitialY;
    private int mInitialLeft, mInitialTop;
    private View mMovingView = null;
    RelativeLayout my_relative_layout;
    int x =  0;
    int y =  0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mRelLay = (ImageView) findViewById(R.id.image);
        //image2 = (ImageView) findViewById(R.id.image2);
        my_relative_layout = (RelativeLayout) findViewById(R.id.my_relative_layout);
       // params.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;

        for (int i = 0; i < my_relative_layout.getChildCount(); i++)
            mRelLay.setOnTouchListener(this);
       // mRelLay.setGravity(Gravity.CENTER_VERTICAL| Gravity.RIGHT);

       // mMovingView.setLayoutParams();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        RelativeLayout.LayoutParams mLayoutParams;

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mMovingView = view;
                mLayoutParams = (RelativeLayout.LayoutParams) mMovingView.getLayoutParams();
                mInitialX = motionEvent.getRawX();
                mInitialY = motionEvent.getRawY();
                mInitialLeft = mLayoutParams.leftMargin;
                mInitialTop = mLayoutParams.topMargin;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mMovingView != null) {
                    mLayoutParams = (RelativeLayout.LayoutParams) mMovingView.getLayoutParams();
                     x =  (int) (mInitialLeft + motionEvent.getRawX() - mInitialX);
                     y =  (int) (mInitialTop + motionEvent.getRawY() - mInitialY);
                    mLayoutParams.leftMargin = x;
                    mLayoutParams.topMargin = y;
                   // txt.setText(x+"x"+y);
                    mRelLay.setLayoutParams(mLayoutParams);
                }
                break;
            case MotionEvent.ACTION_UP:
                mMovingView = null;
                break;
        }

        return true;
    }
    public void onClickQRPay23(View v) throws Exception {
        //  strQrType = "QrPay";
        //  ClearTextEditQrPay();
        // new IntentIntegrator(WalletActivity.this).setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES).setCaptureActivity(ScannerActivity.class).initiateScan();
        // Log.e("Active_valuesaa:",  "EasyAES.encryptString(Standard_info)");
        //  dialog("onClickQR",1);
        Toast.makeText(getApplication(), x+"x"+y, Toast.LENGTH_LONG).show();
    }

}