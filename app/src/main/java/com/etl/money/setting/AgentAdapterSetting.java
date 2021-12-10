package com.etl.money.setting;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.etl.money.R;
import com.etl.money.change_passwor.ChangePasswordWalletActivity;
import com.etl.money.fingerprint.FingerprintActivity;

import java.util.ArrayList;

public class AgentAdapterSetting extends ArrayAdapter<ItemSetting> {
    Context context;
    int layoutResourceId;
    ArrayList<ItemSetting> data = new ArrayList<ItemSetting>();
    public AgentAdapterSetting(Context context, int layoutResourceId,
                          ArrayList<ItemSetting> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }
    static class RecordHolder {
        ImageView imgItem;

        TextView txtTitele;
        TextView txtDescrip;
        LinearLayout LinearLayout1 ;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        AdapterSetting.RecordHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new AdapterSetting.RecordHolder();
            holder.LinearLayout1 = (LinearLayout) row.findViewById(R.id.LinearLayout1);
            holder.imgItem = (ImageView) row.findViewById(R.id.imgItem);
            holder.txtTitele = (TextView) row.findViewById(R.id.txtTitele);
            holder.txtDescrip = (TextView) row.findViewById(R.id.txtDescrip);

            row.setTag(holder);
        } else {
            holder = (AdapterSetting.RecordHolder) row.getTag();
        }

        final   ItemSetting item = data.get(position);

        holder.imgItem.setImageBitmap(item.getImage());
        holder.txtTitele.setText(item.getTitle());
        holder.txtDescrip.setText(item.getDescrip());
        holder.LinearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AgentSettingActivity)context).AdapterClick(item.getTextKey());
            }
        });
        return row;
    }

}