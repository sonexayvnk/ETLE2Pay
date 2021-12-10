package com.etl.money.dashboard;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.etl.money.R;
import com.etl.money.WalletActivity;
import com.etl.money.dashboard.Item;
import com.etl.money.notification.NotificationSQLiteAdapter;

import java.util.ArrayList;

public class DashboardCustomGridViewAdapter   extends ArrayAdapter<Item> {
    Context context;
    int layoutResourceId;
    ArrayList<Item> data = new ArrayList<Item>();
    public DashboardCustomGridViewAdapter(Context context, int layoutResourceId,
                                          ArrayList<Item> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }
    static class RecordHolder {
        TextView txtTitle;
        TextView txtNotificationCount;
        ImageView imageItem;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecordHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new RecordHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.txtdash1);
            holder.txtNotificationCount = (TextView) row.findViewById(R.id.txtNotificationCount);
            holder.imageItem = (ImageView) row.findViewById(R.id.imgdash1);

            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }

        final   Item item = data.get(position);
        // holder.txtTitle.setText(item.getTitle());
        holder.txtNotificationCount.setVisibility(View.GONE);
        if (item.getTitle().equals("notification")) {
            NotificationSQLiteAdapter helper2;
            helper2 = new NotificationSQLiteAdapter(context);
            if (!helper2.getCount("0").equals("0"))
            {
                holder.txtNotificationCount.setText( helper2.getCount("0"));
                holder.txtNotificationCount.setVisibility(View.VISIBLE);
            }
        }
        holder.txtTitle.setText(getString(context,"str_"+item.getTitle()));
        try {
            holder.imageItem.setImageBitmap(item.getImage());
        } catch (Exception e) {
           // holder.imageItem.setImageDrawable(context.getResources().getDrawable(R.drawable.standard));

        }

        holder.imageItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((WalletActivity)context).AdapterClick(item.getTitle());
            }
        });
        return row;
    }
    public static String getString(Context context, String idName) {
        Resources res = context.getResources();
        String str =  idName;
        try {
            str =  res.getString(res.getIdentifier(idName, "string", context.getPackageName()));
        } catch (Exception e) {
        }
        return str;
    }
}