package com.etl.money.dashboard;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.etl.money.R;
import com.etl.money.WalletActivity;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.SimpleViewHolder> {

    // private static final int COUNT = 20;

    private final Context mContext;
    private final List<Integer> mItems;
    private int mCurrentItemId = 0;
    String strArr[];

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public final TextView txtdash1;
        public final TextView txtNotificationCount;
        public final ImageView imgdash1;




        public SimpleViewHolder(View view) {
            super(view);
            txtdash1 = (TextView) view.findViewById(R.id.txtdash1);
            txtNotificationCount = (TextView) view.findViewById(R.id.txtNotificationCount);
            imgdash1 = (ImageView) view.findViewById(R.id.imgdash1);
        }
    }

    public CustomAdapter(Context context, String str[]) {
        mContext = context;
        strArr = str;
        int count = strArr.length;



        mItems = new ArrayList<Integer>(count);


        for (int i = 0; i < strArr.length; i++) {
          //  gridArray.add(new Item(Icon, strArr[i]));
            addItem(i);
        }


    }

    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.dashboard_custom_row, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {

        String str =   getString(mContext,"str_"+strArr[position]);
        int resID = mContext.getResources().getIdentifier("dash_" + strArr[position], "drawable", mContext.getPackageName());
        Bitmap Icon = BitmapFactory.decodeResource(mContext.getResources(), resID);
        holder.imgdash1.setImageBitmap(Icon);
        holder.txtdash1.setText(str);
        holder.txtNotificationCount.setVisibility(View.GONE);
        holder.imgdash1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((WalletActivity)mContext).AdapterClick(strArr[position]);
            }
        });

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
    public void addItem(int position) {
        final int id = mCurrentItemId++;
        mItems.add(position, id);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}