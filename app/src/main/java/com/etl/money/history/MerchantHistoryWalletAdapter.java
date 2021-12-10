package com.etl.money.history;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etl.money.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Kuncoro on 29/02/2016.
 */
public class MerchantHistoryWalletAdapter extends BaseAdapter {



    MerchantHistoryWalletAdapter(Context p_context)
    {
        context=p_context;
    }


    private Context context;
    private Activity activity;
    private LayoutInflater inflater;
    private List<HistoryWalletData> newsItems;
    public MerchantHistoryWalletAdapter(Activity activity, List<HistoryWalletData> newsItems) {this.activity = activity;this.newsItems = newsItems;}
    @Override
    public int getCount() {
        return newsItems.size();
    }
    @Override
    public Object getItem(int location) {return newsItems.get(location);}
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(   final int position, View convertView, ViewGroup parent) {




        if (inflater == null) inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) convertView = inflater.inflate(R.layout.activity_column_history_wallet, null);

        TextView txtSubject = (TextView) convertView.findViewById(R.id.txtSubject);
        txtSubject.setText(newsItems.get(position).gettxtSubject());

        TextView Description = (TextView) convertView.findViewById(R.id.txtDescription);
        Description.setText(newsItems.get(position).gettxtDescription());



        TextView txtDatetine = (TextView) convertView.findViewById(R.id.txtDatetine);
        txtDatetine.setText(newsItems.get(position).gettxtDatetine());

        TextView txtAmount = (TextView) convertView.findViewById(R.id.txtAmount);
        String StrAmt = newsItems.get(position).gettxtAmount();

       // StrAmt = StrAmt.replace(".","");
       int IntAmt = Integer.parseInt(StrAmt);

        if (IntAmt>0){
            txtAmount.setTextColor(Color.parseColor("#07930c"));
          //
        } else {
            txtAmount.setTextColor(Color.parseColor("#dc0027"));
        }


      txtAmount.setText(new DecimalFormat("#,###,###").format(Integer.parseInt(StrAmt)));
       // txtAmount.setText(StrAmt);
     //   txtAmount.setText(StrAmt);

        TextView txtCirclesTitle = (TextView) convertView.findViewById(R.id.txtCirclesTitle);
        txtCirclesTitle.setText(newsItems.get(position).gettxtCircleTitle());
        ShapeDrawable shapeDrawable= new ShapeDrawable();
        shapeDrawable.setShape(new OvalShape());
        shapeDrawable.getPaint().setColor(Color.parseColor(newsItems.get(position).gettxtColor()));
        txtCirclesTitle.setBackgroundDrawable(shapeDrawable);



        TextView txtMonthly = (TextView) convertView.findViewById(R.id.txtMonthly);
        txtMonthly.setText(newsItems.get(position).gettxtMonthly());

        View View1 = (View) convertView.findViewById(R.id.View1);
        View View2 = (View) convertView.findViewById(R.id.View2);

        String StrMonthly = newsItems.get(position).gettxtMonthly();
        //txtDescription
       // View1.setVisibility(View.GONE);
        if (StrMonthly.length()>3){
            txtMonthly.setVisibility(View.VISIBLE);
          //  View1.setVisibility(View.INVISIBLE);
          //  View2.setVisibility(View.VISIBLE);
          //
        } else {
            txtMonthly.setVisibility(View.GONE);
           // View2.setVisibility(View.GONE);

          //  View1.setVisibility(View.VISIBLE);
           //
        }

        LinearLayout historyDetial = (LinearLayout) convertView.findViewById(R.id.historyDetial);
        historyDetial.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { ((MerchantHistoryWalletActivity)activity).historyDetal(newsItems.get(position).gettxtDetail()); }});
        txtCirclesTitle.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { ((MerchantHistoryWalletActivity)activity).historyDetal(newsItems.get(position).gettxtDetail()); }});
        txtSubject.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { ((MerchantHistoryWalletActivity)activity).historyDetal(newsItems.get(position).gettxtDetail()); }});
        Description.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { ((MerchantHistoryWalletActivity)activity).historyDetal(newsItems.get(position).gettxtDetail()); }});
        txtDatetine.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { ((MerchantHistoryWalletActivity)activity).historyDetal(newsItems.get(position).gettxtDetail()); }});
        txtAmount.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { ((MerchantHistoryWalletActivity)activity).historyDetal(newsItems.get(position).gettxtDetail()); }});


            return convertView;







    }



}