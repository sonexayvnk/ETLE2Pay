package com.etl.money;

import android.os.Bundle;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;


import com.etl.money.dashboard.Item;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    GridView gridView;
    ArrayList<Item> gridArray = new ArrayList<Item>();
   // DashboardCustomGridViewAdapter customGridAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
//      Bitmap homeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.wallet_qr_code);
        Bitmap userIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.wallet_user_info);
        gridArray.add(new Item(homeIcon,"1House"));
        gridArray.add(new Item(userIcon,"1Friend"));
        gridArray.add(new Item(homeIcon,"2Home"));
        gridArray.add(new Item(userIcon,"2Personal"));
        gridArray.add(new Item(homeIcon,"3Home"));
        gridArray.add(new Item(userIcon,"3User"));
        gridArray.add(new Item(homeIcon,"4Building"));
        gridArray.add(new Item(userIcon,"4User"));
        gridArray.add(new Item(homeIcon,"5Home"));
        gridArray.add(new Item(userIcon,"5xyz"));
        gridView = (GridView) findViewById(R.id.gridView1);
        customGridAdapter = new DashboardCustomGridViewAdapter(this, R.layout.dashboard_custom_row, gridArray);
        gridView.setAdapter(customGridAdapter);
         */

    }
}