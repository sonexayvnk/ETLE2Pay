package com.etl.money.setting;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etl.money.R;

import java.util.ArrayList;

public class AdapterSetting extends ArrayAdapter<ItemSetting> {
    Context context;
    int layoutResourceId;
    ArrayList<ItemSetting> data = new ArrayList<ItemSetting>();
    public AdapterSetting(Context context, int layoutResourceId,
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
                ((SettingActivity)context).AdapterClick(item.getTextKey());
            }
        });
        return row;
    }

}