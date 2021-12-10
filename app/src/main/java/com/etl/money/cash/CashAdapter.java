package com.etl.money.cash;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etl.money.R;
import com.etl.money.partner.PaymentData;
import com.etl.money.partner.PaymentsActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CashAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CashData> arrayList;
    private TextView txtCirclesTitle, txtTitle, txtDescription;
    LinearLayout LinearLayoutCustomerLog ;
    private Button btnDel;

    public CashAdapter(Context context, ArrayList<CashData> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
       String StrColumn = ""+arrayList.get(position).getIdType();
        if (StrColumn.equals("1")){
        }else{
            convertView = LayoutInflater.from(context).inflate(R.layout.wallet_customer_info_column, parent, false);
         txtTitle = convertView.findViewById(R.id.txtTitle);
            txtDescription = convertView.findViewById(R.id.txtDescription);
             txtTitle.setText(arrayList.get(position).getTitle()) ;
            txtDescription.setText(arrayList.get(position).getDescription());
/*

            if (position==3){
                float floDebit =Float.parseFloat(arrayList.get(position).getDescription());
                txtDescription.setTextColor(context.getResources().getColor(R.color.colorRed));
                if (floDebit<0){
                    floDebit= floDebit*-1;
                    txtDescription.setTextColor(context.getResources().getColor(R.color.colorBlue));
                }
                String strDebitAmount =   new DecimalFormat("#,###,###").format(floDebit);
                txtDescription.setText(strDebitAmount);
                txtDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.text_size_xlarge));
            }
            if (position==4){
                txtDescription.setTextColor(context.getResources().getColor(R.color.colorGreen));
                txtDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.text_size_xlarge));
            }


 */

        }



        return convertView;
    }


}