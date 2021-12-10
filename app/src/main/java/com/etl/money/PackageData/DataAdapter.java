package com.etl.money.PackageData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.etl.money.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {
    Context ctx;
    private LayoutInflater inflater;
    private ArrayList<DataModel> dataModelArrayList;
    public DataAdapter(Context ctx, ArrayList<DataModel> dataModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.dataModelArrayList = dataModelArrayList;
        this.ctx = ctx;
    }
    @Override
    public DataAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.package_data_item_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(DataAdapter.MyViewHolder holder, int position) {
        Picasso.Builder picassoBuilder = new Picasso.Builder(ctx);
        holder.data_price.setText(dataModelArrayList.get(position).getData_price());
        holder.package_name.setText(dataModelArrayList.get(position).getPackage_name());
        holder.package_more_detail.setText(dataModelArrayList.get(position).getPackage_more_detail());
        holder.textCircle.setText(dataModelArrayList.get(position).getPackage_name());
        String iamageLoad = dataModelArrayList.get(position).getImghit();
        //  Log.e("Pick=", iamageLoad );

        /*
        if (iamageLoad.toLowerCase().equals("newlogo")) {
            Picasso.get().load(R.drawable.newlogo).into(holder.imgHit);
        } else if (iamageLoad.toLowerCase().toLowerCase().equals("hitlogo")) {
            Picasso.get().load(R.drawable.hit_fire2).into(holder.imgHit);
        } else if (iamageLoad.toLowerCase().equals("promotion")) {
            Picasso.get().load(R.drawable.promot_icon).into(holder.imgHit);
        }else {
            //   Picasso.get().load(R.drawable.newlogo).into(holder.imgHit);
        }
        */

        String ckage_name = dataModelArrayList.get(position).getPackage_name()+"";
        holder.textCircle.setBackgroundResource(R.drawable.donuspacgate);
        if (ckage_name.length()>6){
            String StrTop =ckage_name.substring(0,7);
            if (StrTop.equals("Topping")) {
                holder.textCircle.setBackgroundResource(R.drawable.donuspacgate_top);
                // holder.textCircle.setTextSize(R.dimen.text_size_pacgage_top);
                //  holder.textCircle.setTextSize(TypedValue.COMPLEX_UNIT_PX,32);
            }

        }
        /*
        String ckage_name = dataModelArrayList.get(position).getPackage_name()+"";
        holder.textCircle.setTextSize(TypedValue.COMPLEX_UNIT_PX,55);
        holder.textCircle.setBackgroundResource(R.drawable.donuspacgate);
        if (ckage_name.length()>3){
            String StrTop =ckage_name.substring(0,3);
            if (StrTop.equals("Top")) {
                holder.textCircle.setBackgroundResource(R.drawable.donuspacgate_top);
                // holder.textCircle.setTextSize(R.dimen.text_size_pacgage_top);
                holder.textCircle.setTextSize(TypedValue.COMPLEX_UNIT_PX,32);
            }
        }

         */
    }
    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView package_name, data_price, package_more_detail, textCircle;
        LinearLayout linearpackage;
        ImageView iv, imgHit;
        protected Button btn_plus, btn_minus;
        public MyViewHolder(View itemView) {
            super(itemView);
            // Add new
            itemView.setTag(itemView);
            textCircle = (TextView) itemView.findViewById(R.id.textcircle);
            package_name = (TextView) itemView.findViewById(R.id.package_name);
            data_price = (TextView) itemView.findViewById(R.id.data_price);
            package_more_detail = (TextView) itemView.findViewById(R.id.package_more_detail);
//            iv = (ImageView) itemView.findViewById(R.id.iv);
            imgHit = (ImageView) itemView.findViewById(R.id.img_hit);
            btn_plus = (Button) itemView.findViewById(R.id.plus);
            linearpackage = itemView.findViewById(R.id.linearpackage);
            btn_plus.setOnClickListener(this);
            linearpackage.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            final String pg_price = (String) data_price.getText();
            final String pgName = (String) package_name.getText();
            String city_pg = (String) package_more_detail.getText();
            //  Toast.makeText(v.getContext(), "Clicked:" + pgName + "-" + pg_price + "-" + city_pg, Toast.LENGTH_SHORT).show();


            ((PackageDataActivity)ctx).BuyPackageAlert(pgName);
        }
    }
}