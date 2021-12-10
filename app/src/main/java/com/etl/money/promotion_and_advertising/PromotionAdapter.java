package com.etl.money.promotion_and_advertising;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etl.money.R;
import com.etl.money.history.HistoryWalletData;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Kuncoro on 29/02/2016.
 */
public class PromotionAdapter extends BaseAdapter {
    PromotionAdapter(Context p_context)
    {
        context=p_context;
    }
    private Context context;
    private Activity activity;
    private LayoutInflater inflater;
    private List<PromotionData> newsItems;
    public PromotionAdapter(Activity activity, List<PromotionData> newsItems) {this.activity = activity;this.newsItems = newsItems;}
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
        if (convertView == null) convertView = inflater.inflate(R.layout.promotion_custom_row, null);
        TextView Description = (TextView) convertView.findViewById(R.id.txtDescription);
        TextView Detail = (TextView) convertView.findViewById(R.id.txtDetail);
        TextView txtInfo = (TextView) convertView.findViewById(R.id.txtInfo);
        ImageView imgBase64 = (ImageView) convertView.findViewById(R.id.imgBase64);
        Description.setText(newsItems.get(position).gettxtDescription());
        String  StrBase64 = newsItems.get(position).getimgBase64();
        if (StrBase64.equals("")){
            imgBase64.setBackgroundResource(R.drawable.promosio_no_image);
        } else {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] imageBytes = baos.toByteArray();
                imageBytes = Base64.decode(StrBase64 +".", Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                imgBase64.setImageBitmap(decodedImage);
            } catch (Exception e) {
                imgBase64.setBackgroundResource(R.drawable.promosio_no_image);
            }
        }

        Detail.setText(newsItems.get(position).gettxtDetail());
        txtInfo.setText(newsItems.get(position).gettxtInfo());

        ShapeDrawable shapeDrawable= new ShapeDrawable();
        shapeDrawable.setShape(new OvalShape());
        //shapeDrawable.getPaint().setColor(Color.parseColor(newsItems.get(position).gettxtColor()));
        LinearLayout historyDetial = (LinearLayout) convertView.findViewById(R.id.historyDetial);
        imgBase64.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
            ((PromotionActivity)activity).historyDetail(newsItems.get(position).gettxtDescription()+"~"+newsItems.get(position).getimgBase64());
        }});
      //  Description.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { ((PromotionActivity)activity).historyDetal(newsItems.get(position).gettxtDetail()); }});
        return convertView;
    }
}