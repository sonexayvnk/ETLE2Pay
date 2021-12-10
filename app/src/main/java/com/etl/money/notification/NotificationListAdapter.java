package com.etl.money.notification;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.etl.money.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotificationListAdapter  extends RecyclerView.Adapter<NotificationListAdapter.MyViewHolder> {
    Context ctx;
    private LayoutInflater inflater;
    private ArrayList<NotificationListData> dataModelArrayList;
    public NotificationListAdapter(Context ctx, ArrayList<NotificationListData> dataModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.dataModelArrayList = dataModelArrayList;
        this.ctx = ctx;
    }
    @Override
    public NotificationListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = inflater.inflate(R.layout.notification_column, parent, false);
      //  View view = inflater.inflate(R.layout.package_data_item_row, parent, false);
        NotificationListAdapter.MyViewHolder holder = new NotificationListAdapter.MyViewHolder(view);
        return holder;
    }
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(NotificationListAdapter.MyViewHolder holder, int position) {
        Picasso.Builder picassoBuilder = new Picasso.Builder(ctx);

        holder.strIds = dataModelArrayList.get(position).getIds();
        holder.strData = dataModelArrayList.get(position).getIds() ;
        holder.strData += "~" + dataModelArrayList.get(position).getTitle() ;
        holder.strData += "~" + dataModelArrayList.get(position).getDescription() ;
        holder.strData += "~" + dataModelArrayList.get(position).getDatetime() ;
        holder.strData += "~" + dataModelArrayList.get(position).getStatus() ;
        holder.txtTitle.setText(dataModelArrayList.get(position).getTitle());
        holder.txtDescription.setText(dataModelArrayList.get(position).getDescription());
        holder.txtDatetime.setText(dataModelArrayList.get(position).getDatetime());
        if (dataModelArrayList.get(position).getStatus().equals("1")) {
             holder.txtStatus.setTextColor(ctx.getResources().getColor(R.color.colorGreen));

        }else{
            holder.txtStatus.setTextColor(ctx.getResources().getColor(R.color.colorOrange));
            holder.txtStatus.setText( ctx.getResources().getString(R.string.str_status_unread));
        }
    }
    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtTitle,txtDescription, txtDatetime, txtStatus;
        String strIds , strData;;
        LinearLayout linearLayout;
        public MyViewHolder(View itemView) {
            super(itemView);
            // Add new

            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtDescription = (TextView) itemView.findViewById(R.id.txtDescription);
            txtDatetime = (TextView) itemView.findViewById(R.id.txtDatetime);
            txtStatus = (TextView) itemView.findViewById(R.id.txtStatus);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            txtTitle.setOnClickListener(this);
            txtDescription.setOnClickListener(this);
            txtDatetime.setOnClickListener(this);
            txtStatus.setOnClickListener(this);
            linearLayout.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            txtStatus.setTextColor(ctx.getResources().getColor(R.color.colorGreen));
            txtStatus.setText( ctx.getResources().getString(R.string.str_status_readed));
           ((NotificationListActivity)ctx).ShowDetail(strData);
        }
    }
}