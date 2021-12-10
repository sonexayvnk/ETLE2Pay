package com.etl.money.partner;

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

import java.util.ArrayList;

public class SelectBranchAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<SelectBranchData> arrayList;
    private TextView txtCirclesTitle, txtDescription;
    LinearLayout LinearBranch ;
    public SelectBranchAdapter(Context context, ArrayList<SelectBranchData> arrayList) {
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
        convertView = LayoutInflater.from(context).inflate(R.layout.select_branch_column, parent, false);
        txtCirclesTitle = convertView.findViewById(R.id.txtCirclesTitle);
        txtDescription = convertView.findViewById(R.id.txtDescription);
        LinearBranch = convertView.findViewById(R.id.LinearBranch);
        ShapeDrawable shapeDrawable= new ShapeDrawable();
        shapeDrawable.setShape(new OvalShape());
        shapeDrawable.getPaint().setColor(Color.parseColor(arrayList.get(position).getCirclesColor()));
        txtCirclesTitle.setText(arrayList.get(position).getCirclesTitle());
        txtDescription.setText(arrayList.get(position).getDescription());
        txtCirclesTitle.setBackground(shapeDrawable);
        LinearBranch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((SelectBranchActivity)context).SetBranch(arrayList.get(position).getCirclesTitle());
                   // ((PaymentsActivity)context).SetCustomer(arrayList.get(position).getCusID());
                }
            });

        return convertView;
    }
}