package com.etl.money.partner;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.http.DelegatingSSLSession;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etl.money.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PaymentAdapter  extends BaseAdapter {
    private Context context;
    private ArrayList<PaymentData> arrayList;
    private TextView txtCirclesTitle, txtTitle, txtDescription;
    LinearLayout LinearLayoutCustomerLog ;
    private Button btnDel;

    public PaymentAdapter(Context context, ArrayList<PaymentData> arrayList) {
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

      //  Log.e("LoadCustomerInfoaaa:",  StrColumn);



        if (StrColumn.equals("1")){
            convertView = LayoutInflater.from(context).inflate(R.layout.wallet_customer_log_column, parent, false);
            btnDel = convertView.findViewById(R.id.btnDel);
            txtCirclesTitle = convertView.findViewById(R.id.txtCirclesTitle);

            ShapeDrawable shapeDrawable= new ShapeDrawable();
            shapeDrawable.setShape(new OvalShape());
            shapeDrawable.getPaint().setColor(Color.parseColor(arrayList.get(position).getCirclesColor()));

            txtCirclesTitle.setText(arrayList.get(position).getCirclesTitle());
            txtCirclesTitle.setBackground(shapeDrawable);
            LinearLayoutCustomerLog = convertView.findViewById(R.id.LinearLayoutCustomerLog);

        }else{
            convertView = LayoutInflater.from(context).inflate(R.layout.wallet_customer_info_column, parent, false);



        };
        txtTitle = convertView.findViewById(R.id.txtTitle);
        txtDescription = convertView.findViewById(R.id.txtDescription);
        txtTitle.setText(arrayList.get(position).getTitle()) ;
        txtDescription.setText(arrayList.get(position).getDescription());
        /*
        txtCustID = convertView.findViewById(R.id.txtTitle);
        txtCustName = convertView.findViewById(R.id.txtDescription);
        txtCustID.setText(arrayList.get(position).getCusID());
        txtCustName.setText(arrayList.get(position).getCustName());

         */




        if (StrColumn.equals("1")){

            LinearLayoutCustomerLog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((PaymentsActivity)context).SetCustomer(arrayList.get(position).getTitle(),arrayList.get(position).getCirclesTitle());
                }
            });
            btnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((PaymentsActivity)context).DelCustomerLog(arrayList.get(position).getCirclesTitle()+"",arrayList.get(position).getTitle()+"",arrayList.get(position).getDescription()+"");
                }
            });
        } else {


            if (position==1){
                txtDescription.setText(getString(context,""+arrayList.get(position).getDescription()));
            }
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
        }

        return convertView;
    }

    public static String getString(Context context, String idName) {
        Resources res = context.getResources();
        String str =  idName;
        try {

            if (str.equals("NL")){// Nakhonluang, BranchID = '1';
                str = ""+context.getString(R.string.str_branch_name_vientiane_capital);
            }else if (str.equals("VT")){// Vientiane Province, BranchID = '2';
                str = ""+context.getString(R.string.str_branch_name_vientiane);
            }else if (str.equals("LT")){// Luangnumtha Province, BranchID = '3';
                str = ""+context.getString(R.string.str_branch_name_luangnamtha);
            }else if (str.equals("BK")){// Borkeo Province, BranchID = '4';
                str = ""+context.getString(R.string.str_branch_name_bokeo);
            }else if (str.equals("OX")){// Oudomxay Province, BranchID = '5';
                str = ""+context.getString(R.string.str_branch_name_oudomxay);
            }else if (str.equals("LB")){// Luangprabang ProvincebranchID = '6';
                str = ""+context.getString(R.string.str_branch_name_luangprabang);
            }else if (str.equals("XL")){// Xayabuly ProvincebranchID = '7';
                str = ""+context.getString(R.string.str_branch_name_xayabury);
            }else if (str.equals("HP")){// Huaphanh Province, BranchID = '8';
                str = ""+context.getString(R.string.str_branch_name_huaphanh);
            }else if (str.equals("XK")){// Xiengkhuang Province, BranchID = '9';
                str = ""+context.getString(R.string.str_branch_name_xiengkhong);
            }else if (str.equals("BX")){// Borlikhamxay Province, BranchID = '11';
                str = ""+context.getString(R.string.str_branch_name_borikhamxay);
            }else if (str.equals("KM")){// Khammouane Province, BranchID = '13';
                str = ""+context.getString(R.string.str_branch_name_khammuane);
            }else if (str.equals("SN์")){// Savannakhet Province, BranchID = '14';
                str = ""+context.getString(R.string.str_branch_name_savannakhet);
            }else if (str.equals("CS")){// Champasack Province, BranchID = '15';
                str = ""+context.getString(R.string.str_branch_name_champasack);
            }else if (str.equals("SV")){// Saravanh Province, BranchID = '16';
                str = ""+context.getString(R.string.str_branch_name_saravane);
            }else if (str.equals("SK")){// Sekong Province, BranchID = '17';
                str = ""+context.getString(R.string.str_branch_name_sekong);
            }else if (str.equals("AP")){// Attapeu Province, BranchID = '18';
                str = ""+context.getString(R.string.str_branch_name_attapeu);
            }




         //   str =  res.getString(res.getIdentifier(idName, "string", context.getPackageName()));
        } catch (Exception e) {
        }
        return str;
    }
}