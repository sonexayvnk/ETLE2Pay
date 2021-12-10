package com.etl.money.partner;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.etl.money.R;
import com.etl.money.register.RegisterActivity;
import com.etl.money.register.TakePhotosActivity;

import java.util.ArrayList;

public class SelectBranchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_branch);



        ListView listView;

      //  ArrayList<PaymentData> arrayList = new ArrayList<>();
      //  PaymentAdapter adapter;

        ArrayList<SelectBranchData> arrayList = new ArrayList<>();
        SelectBranchAdapter adapter;
        listView = findViewById(R.id.listViewBranch);
        listView.setDivider(null);
        String strBranchKey =  getResources().getString(R.string.str_branch_str);
       // String strBranchKey = "|||BK|OX|LB|XL|HP|XK|BX|KM|SN|CS|SV|SK|AP";
        String strBranchColor =  "e6642d|dd5330|cb3536|b33138|983139|8f3037|4d383a|3c383e|353644|2243a5|1045a9|004eb0|0061b8|009bd2|00925f|008d4a|4ca340";
        strBranchColor = getResources().getString(R.string.str_branch_color);
        String[] arryData = strBranchKey.split("\\|");
        String[] arryColor = strBranchColor.split("\\|");
        int i = 0 ;
        for (String str : arryData) {
            if (str.equals("NL")){// Nakhonluang, BranchID = '1';
                arrayList.add(new SelectBranchData(str ,"#"+arryColor[i],""+getString(R.string.str_branch_name_vientiane_capital)));
            }else if (str.equals("VT")){// Vientiane Province, BranchID = '2';
                arrayList.add(new SelectBranchData(str ,"#"+arryColor[i],""+getString(R.string.str_branch_name_vientiane)));
            }else if (str.equals("LT")){// Luangnumtha Province, BranchID = '3';
                arrayList.add(new SelectBranchData(str ,"#"+arryColor[i],""+getString(R.string.str_branch_name_luangnamtha)));
            }else if (str.equals("BK")){// Borkeo Province, BranchID = '4';
                arrayList.add(new SelectBranchData(str ,"#"+arryColor[i],""+getString(R.string.str_branch_name_bokeo)));
            }else if (str.equals("OX")){// Oudomxay Province, BranchID = '5';
                arrayList.add(new SelectBranchData(str ,"#"+arryColor[i],""+getString(R.string.str_branch_name_oudomxay)));
            }else if (str.equals("LB")){// Luangprabang ProvincebranchID = '6';
                arrayList.add(new SelectBranchData(str ,"#"+arryColor[i],""+getString(R.string.str_branch_name_luangprabang)));
            }else if (str.equals("XL")){// Xayabuly ProvincebranchID = '7';
                arrayList.add(new SelectBranchData(str ,"#"+arryColor[i],""+getString(R.string.str_branch_name_xayabury)));
            }else if (str.equals("HP")){// Huaphanh Province, BranchID = '8';
                arrayList.add(new SelectBranchData(str ,"#"+arryColor[i],""+getString(R.string.str_branch_name_huaphanh)));
            }else if (str.equals("XK")){// Xiengkhuang Province, BranchID = '9';
                arrayList.add(new SelectBranchData(str ,"#"+arryColor[i],""+getString(R.string.str_branch_name_xiengkhong)));
            }else if (str.equals("BX")){// Borlikhamxay Province, BranchID = '11';
                arrayList.add(new SelectBranchData(str ,"#"+arryColor[i],""+getString(R.string.str_branch_name_borikhamxay)));
            }else if (str.equals("KM")){// Khammouane Province, BranchID = '13';
                arrayList.add(new SelectBranchData(str ,"#"+arryColor[i],""+getString(R.string.str_branch_name_khammuane)));
            }else if (str.equals("SN")){// Savannakhet Province, BranchID = '14';
                arrayList.add(new SelectBranchData(str ,"#"+arryColor[i],""+getString(R.string.str_branch_name_savannakhet)));
            }else if (str.equals("CS")){// Champasack Province, BranchID = '15';
                arrayList.add(new SelectBranchData(str ,"#"+arryColor[i],""+getString(R.string.str_branch_name_champasack)));
            }else if (str.equals("SV")){// Saravanh Province, BranchID = '16';
                arrayList.add(new SelectBranchData(str ,"#"+arryColor[i],""+getString(R.string.str_branch_name_saravane)));
            }else if (str.equals("SK")){// Sekong Province, BranchID = '17';
                arrayList.add(new SelectBranchData(str ,"#"+arryColor[i],""+getString(R.string.str_branch_name_sekong)));
            }else if (str.equals("AP")){// Attapeu Province, BranchID = '18';
                arrayList.add(new SelectBranchData(str ,"#"+arryColor[i],""+getString(R.string.str_branch_name_attapeu)));
            }







/*


                    if (str.equals("NL")){
                        arrayList.add(new SelectBranchData(str ,"#"+arryColor[i],""+getString(R.string.str_branch_name_vientiane_capital)));
            } else  if (str.equals("VT")){
                        arrayList.add(new SelectBranchData(str ,"#"+arryColor[i],""+getString(R.string.str_branch_name_phongsaly)));
            } else  if (str.equals("LT")){
                        arrayList.add(new SelectBranchData(str ,"#"+arryColor[i],""+getString(R.string.str_branch_name_luangnamtha)));
            } else  if (str.equals("OD")){
                        arrayList.add(new SelectBranchData(str ,"#"+arryColor[i],""+getString(R.string.str_branch_name_oudomxay)));
            } else  if (str.equals("BK")){
                        arrayList.add(new SelectBranchData(str ,"#"+arryColor[i],""+getString(R.string.str_branch_name_bokeo)));
            } else  if (str.equals("LB")){
                        arrayList.add(new SelectBranchData(str ,"#"+arryColor[i],""+getString(R.string.str_branch_name_luangprabang)));
            } else  if (str.equals("HP")){
                        arrayList.add(new SelectBranchData(str ,"#"+arryColor[i],""+getString(R.string.str_branch_name_huaphanh)));
            } else  if (str.equals("XB")){
                        arrayList.add(new SelectBranchData(str ,"#"+arryColor[i],""+getString(R.string.str_branch_name_xayabury)));
            } else  if (str.equals("XK")){
                        arrayList.add(new SelectBranchData(str ,"#"+arryColor[i],""+getString(R.string.str_branch_name_xiengkhong)));
            } else  if (str.equals("VI")){
                        arrayList.add(new SelectBranchData(str ,"#"+arryColor[i],""+getString(R.string.str_branch_name_vientiane)));
            } else  if (str.equals("BR")){
                        arrayList.add(new SelectBranchData(str ,"#"+arryColor[i],""+getString(R.string.str_branch_name_borikhamxay)));
            } else  if (str.equals("KM")){
                        arrayList.add(new SelectBranchData(str ,"#"+arryColor[i],""+getString(R.string.str_branch_name_khammuane)));
            } else  if (str.equals("SN")){
                        arrayList.add(new SelectBranchData(str ,"#"+arryColor[i],""+getString(R.string.str_branch_name_savannakhet)));
            } else  if (str.equals("SV")){
                        arrayList.add(new SelectBranchData(str ,"#"+arryColor[i],""+getString(R.string.str_branch_name_saravane)));
            } else  if (str.equals("SK")){
                        arrayList.add(new SelectBranchData(str ,"#"+arryColor[i],""+getString(R.string.str_branch_name_sekong)));
            } else  if (str.equals("CP")){
                        arrayList.add(new SelectBranchData(str ,"#"+arryColor[i],""+getString(R.string.str_branch_name_champasack)));
            } else  if (str.equals("AP")){
                        arrayList.add(new SelectBranchData(str ,"#"+arryColor[i],""+getString(R.string.str_branch_name_attapeu)));
            }
*/

i++;
        }
       adapter = new SelectBranchAdapter(this, arrayList);
        listView.setAdapter(adapter);




    }
    public void SetBranch( String str){

       // Log.e("value:", ""+str);
        Intent intent=new Intent();
        intent.putExtra("strExtra",str);
        setResult(2,intent);
        finish();//finishing activity

       // Intent intent = new Intent(this, PaymentsActivity.class);
      //  intent.putExtra("key",str);
     //   startActivity(intent);
     //   finish();

      //  Intent intent=new Intent(PaymentsActivity.this, TakePhotosActivity.class);
     //   startActivityForResult(intent, 2);// Activity is started with requestCode 2
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_exit:
                Intent intent=new Intent();
                intent.putExtra("strExtra","");
                setResult(2,intent);
                finish();//finishing activity

                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressLint("WrongConstant")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_icon_close, menu);
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.actionbar_layout_title_center);
            TextView mytext = (TextView) findViewById(R.id.txtcenter);
            mytext.setText(R.string.str_select_branch);
        } catch (Exception e) {
        }
        return true;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent=new Intent();
            intent.putExtra("strExtra","");
            setResult(2,intent);
            finish();//finishing activity

        }
        return super.onKeyDown(keyCode, event);
    }


}