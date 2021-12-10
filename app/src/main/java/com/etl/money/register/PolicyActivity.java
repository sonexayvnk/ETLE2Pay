package com.etl.money.register;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.etl.money.R;

public class PolicyActivity extends AppCompatActivity {
    ScrollView ScrollView1;
    CheckBox chAgree1 , chAgree2;
    Button btnAgree ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);
        ScrollView1 = (ScrollView) findViewById(R.id.ScrollView);
        chAgree1 =  findViewById(R.id.chAgree1);
        chAgree2 = findViewById(R.id.chAgree2);
        btnAgree =  findViewById(R.id.btnAgree);
      //  chAgree1.setEnabled(false);
       // chAgree2.setEnabled(false);
        btnAgree.setEnabled(false);
        chAgree1.setEnabled(true);
        chAgree2.setEnabled(true);


        chAgree1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    EnableAgree();
            }
        });
        chAgree2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EnableAgree();
            }
        });


        ScrollView1.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = (View) ScrollView1.getChildAt(ScrollView1.getChildCount() - 1);
                int diff = (view.getBottom() - (ScrollView1.getHeight() + ScrollView1.getScrollY()));
                if (diff == 0) {
                    chAgree1.setEnabled(true);
                    chAgree2.setEnabled(true);
                    // do stuff
                   // Log.d("ScrollView","_scrollY_"+scrollY+"_Height_"+Height);
                }
            }
        });

    }
private void EnableAgree(){
    if (chAgree1.isChecked() & chAgree2.isChecked()) {
        btnAgree.setEnabled(true);
    }else{btnAgree.setEnabled(false);}
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
    /*
    @SuppressLint("WrongConstant")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_icon_close, menu);
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.actionbar_layout_title_center);
            TextView mytext = (TextView) findViewById(R.id.txtcenter);
            mytext.setText(R.string.str_privacy_policy);
        } catch (Exception e) {
        }
        return true;
    }

     */
    public void onClickDisagree(View v){




        finish();
    }
    public void onClickClose(View v){
        finish();
    }
    public void onClickAgree(View v){


        final Context context = this;
        final   Dialog dialog ;
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_choose_register);


        ImageView imgRegisterE2Pay = (ImageView) dialog.findViewById(R.id.imgRegisterE2Pay);
        ImageView imgRegisterKyc = (ImageView) dialog.findViewById(R.id.imgRegisterKyc);

        imgRegisterE2Pay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //  dialog(getString(R.string.str_successful) ,  1);
                Intent intent = new Intent(context, RegisterActivity.class);
                startActivity(intent);
                dialog.dismiss();
                finish();
            }
        });
        imgRegisterKyc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //  dialog(getString(R.string.str_successful) ,  1);
                Intent intent = new Intent(context, RegisterViaKycActivity.class);
                startActivity(intent);
                dialog.dismiss();
                finish();
            }
        });

        dialog.show();

    }
}