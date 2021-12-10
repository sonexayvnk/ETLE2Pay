package com.etl.money;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.etl.money.config.DeviceListActivity.EXTRA_DEVICE_ADDRESS;


public class MsdnHistoryActivity extends AppCompatActivity {
    String Sd;
    ArrayList<HashMap<String, String>> MyArrList;
    private Context context2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msdn_history);
        // Add();
        //  Add();
        setList();

    }

    private void setList() {

        MyArrList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;

        // String a = "2022229872;20255423487;8787878;87878878;6565665";
        SharedPreferences pref = getApplicationContext().getSharedPreferences("ETL_TOPUP_HISTORY_MSDN", MODE_PRIVATE);
        String a = pref.getString("ETL_TOPUP_MSDN", "");
        // a.length();

        //   Log.e("AAAA:",a);


        TextView txtNoitem = (TextView) findViewById(R.id.txtNoitem);
        if (a.length()<8){ txtNoitem.setVisibility(View.VISIBLE); return;}else{ txtNoitem.setVisibility(View.GONE);};
        // Toast.makeText(getApplicationContext(), "Your toast message" +  , Toast.LENGTH_LONG).show();

        String[] arrB = a.split(",");

        for (String c : arrB) {
            map = new HashMap<String, String>();
            String mNd = c;
            String[] arrmNd = mNd.split("~");

            map.put("txtMsdn", arrmNd[0]);
            map.put("txtDate", arrmNd[1]);
            MyArrList.add(map);
        }
        final ListView lstView1 = (ListView) findViewById(R.id.listView1);
        lstView1.setAdapter(new MsdnHistoryActivity.PriceGroupAdapter(this));

        lstView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_DEVICE_ADDRESS, MyArrList.get(position).get("txtMsdn"));
                setResult(Activity.RESULT_OK, intent);
                finish();
             /*
                Intent intent = new Intent(getApplicationContext(), ETLDetailActivity.class);
                intent.putExtra("etlMessageID", newsList.get(position).getmessageIDETLStaff());
                //intent.putExtra("secondKeyName", "SecondKeyValue");
                startActivity(intent);
                */
            }
        });
        // MyArrList.add(map);
    }

    public class PriceGroupAdapter extends BaseAdapter {
        private Context context;

        public PriceGroupAdapter(Context c) {
            context = c;
        }

        public int getCount() {
            return MyArrList.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.activity_column_msdn, null);
            }
            TextView txtMsdn = (TextView) convertView.findViewById(R.id.txtMsdn);
            ImageView imageView7 = (ImageView) convertView.findViewById(R.id.imageView7);
            ImageView imageDel = (ImageView) convertView.findViewById(R.id.imageDel);

            View View03 = (View) convertView.findViewById(R.id.View03);
            TextView txDate = (TextView) convertView.findViewById(R.id.txDate);
            //   txtmsin1.setPadding(5, 0, 0, 0);
            txtMsdn.setText(MyArrList.get(position).get("txtMsdn"));
            String StrMsdn = txtMsdn.getText().toString();

            txDate.setText(MyArrList.get(position).get("txtDate"));
            if (StrMsdn.equals("")) {

                txDate.setVisibility(View.GONE);
                imageDel.setVisibility(View.GONE);
                txtMsdn.setVisibility(View.GONE);
                imageView7.setVisibility(View.GONE);
                View03.setVisibility(View.GONE);
            } else {
                txDate.setVisibility(View.VISIBLE);
                imageDel.setVisibility(View.VISIBLE);
                txtMsdn.setVisibility(View.VISIBLE);
                imageView7.setVisibility(View.VISIBLE);
                View03.setVisibility(View.VISIBLE);
            }

            imageDel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Del(MyArrList.get(position).get("txtMsdn"), MyArrList.get(position).get("txtDate"));
                }
            });

            return convertView;
        }
    }


    private void Del(String sd, String txtMsdn) {
        Sd = sd + "~" + txtMsdn;
        // String ss1 =  R.string.str_you_want_to_delete_the_number ;

        String String = getString(R.string.str_you_want_to_delete_the_number) + " " + sd + "" + getString(R.string.str_really);
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.title_confirm_to_exit)
                .setMessage(String)
                .setPositiveButton(R.string.str_yes_to_exit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SharedPreferences pref = getApplicationContext().getSharedPreferences("ETL_TOPUP_HISTORY_MSDN", MODE_PRIVATE);
                        String s = pref.getString("ETL_TOPUP_MSDN", "");
                        s = s.replace(Sd, "");
                        s = s.replace(",,", ",");
                        if (s.length() > 8) {
                            if (s.substring(0, 1).equals(",")) {
                                s = s.substring(1, s.length());
                            }
                        }
                        SharedPreferences.Editor editorfk = pref.edit();
                        editorfk.putString("ETL_TOPUP_MSDN", s);
                        editorfk.commit();

                        setList();
                    }
                })
                .setNegativeButton(R.string.str_no_to_exit, null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_icon_close, menu);
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.actionbar_layout_title_center);
            TextView mytext = (TextView) findViewById(R.id.txtcenter);
            mytext.setText(R.string.str_destination_number);
        } catch (Exception e) {
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_exit:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /*
          private void AccessContact()
          {
              List<String> permissionsNeeded = new ArrayList<String>();
              final List<String> permissionsList = new ArrayList<String>();
              if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS))
                  permissionsNeeded.add("Read Contacts");
              if (!addPermission(permissionsList, Manifest.permission.WRITE_CONTACTS))
                  permissionsNeeded.add("Write Contacts");
              if (permissionsList.size() > 0) {
                  if (permissionsNeeded.size() > 0) {
                      String message = "You need to grant access to " + permissionsNeeded.get(0);
                      for (int i = 1; i < permissionsNeeded.size(); i++)
                          message = message + ", " + permissionsNeeded.get(i);
                      showMessageOKCancel(message,
                              new DialogInterface.OnClickListener() {
                                  @Override
                                  public void onClick(DialogInterface dialog, int which) {
                                      requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                              REQUEST_MULTIPLE_PERMISSIONS);
                                  }
                              });
                      return;
                  }
                  requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                          REQUEST_MULTIPLE_PERMISSIONS);
                  return;
              }
          }
      */
    private void Add() {
        Date dt = new Date();
        CharSequence sdd = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm", dt.getTime());
        String dd = sdd.toString();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("ETL_TOPUP_HISTORY_MSDN", MODE_PRIVATE);

        SharedPreferences.Editor editorfk = pref.edit();
        String a = "2023866116~" + dd + "," +
                "2022440016~" + dd + "," +
                "2029271112~" + dd + "," +
                "2028669933~" + dd + "," +
                "2028757733~" + dd + "," +
                "2028878713~" + dd + "," +
                "2023078093~" + dd + "," +
                "2029815936~" + dd + "," +
                "2029290776~" + dd + "," +
                "2028039816~" + dd + "," +
                "2029815210~" + dd + "," +
                "2022214193~" + dd + "," +
                "2029980579~" + dd + "," +
                "2029826875~" + dd + "," +
                "2023055572~" + dd + "," +
                "2028040439~" + dd + "," +
                "2029443619~" + dd + "," +
                "2029610516~" + dd + "," +
                "2028044816~" + dd + "," +
                "2029815376~" + dd + "," +
                "2029809432~" + dd + "," +
                "2023007752~" + dd + "," +
                "2023033350~" + dd + "," +
                "2022217496~" + dd + "," +
                "2028138172~" + dd + "," +
                "2029441392~" + dd + "," +
                "2029082575~" + dd + "," +
                "2028034835~" + dd + "," +
                "2022052890~" + dd + "," +
                "2028372190~" + dd + "," +
                "2022755455~" + dd + "," +
                "2028576655~" + dd + "," +
                "2029335491~" + dd + "," +
                "2023115591~" + dd + "," +
                "2029451438~" + dd + "," +
                "2029074058~" + dd + "," +
                "2028038399~" + dd + "," +
                "2028954054~" + dd + "," +
                "2028036878~" + dd + "," +
                "2022344201~" + dd + "," +
                "2029680424~" + dd + "," +
                "2029451154~" + dd + "," +
                "2028044974~" + dd + "," +
                "2022344064~" + dd + "," +
                "2029400289~" + dd + "," +
                "2029282944~" + dd + "," +
                "2029813631~" + dd + "," +
                "2022993477~" + dd + "," +
                "2029804808~" + dd + "," +
                "2028039518~" + dd + "," +
                "2028318560~" + dd + "," +
                "2029800228~" + dd + "," +
                "2028035200~" + dd + "," +
                "2028742168~" + dd + "," +
                "2022443448~" + dd + "," +
                "2022731058~" + dd + "," +
                "2028140937~" + dd + "," +
                "2029800138~" + dd + "," +
                "2028518086~" + dd + "," +
                "2028038986~" + dd + "," +
                "2028802065~" + dd + "," +
                "2028550285~" + dd + "," +
                "2028304545~" + dd + "," +
                "2029806207~" + dd + "," +
                "2028035184~" + dd + "," +
                "2028557571~" + dd + "," +
                "2022270687~" + dd + "," +
                "2029444256~" + dd + "," +
                "2029801887~" + dd + "," +
                "2029803903~" + dd + "," +
                "2029808443~" + dd + "," +
                "2028081203~" + dd + "," +
                "2022891086~" + dd + "," +
                "2028462220~" + dd + "," +
                "2028259120~" + dd + "," +
                "2028822248~" + dd + "," +
                "2028140812~" + dd + "," +
                "2028036916~" + dd + "," +
                "2029803930~" + dd + "," +
                "2029807156~" + dd + "," +
                "2028492826~" + dd + "," +
                "2029085280~" + dd + "," +
                "2028636699~" + dd + "," +
                "2029800479~" + dd + "," +
                "2028042226~" + dd + "," +
                "2029450826~" + dd + "," +
                "2023078220~" + dd + "," +
                "2028037842~" + dd + "," +
                "2029804522~" + dd + "," +
                "2029163473~" + dd + "," +
                "2022736325~" + dd + "," +
                "2029443447~" + dd + "," +
                "2028492602~" + dd + "," +
                "2022729933~" + dd + "," +
                "2028951954~" + dd + "," +
                "2022220834~" + dd + "," +
                "2028734422~" + dd + "," +
                "2023666894~" + dd + "," +
                "2028755875~" + dd + "," +
                "2028304563~" + dd + "," +
                "2028040262~" + dd + "," +
                "2022299935~" + dd + "," +
                "2029085283~" + dd + "," +
                "2023636551~" + dd + "," +
                "2023888386~" + dd + "," +
                "2029804329~" + dd + "," +
                "2028616994~" + dd + "," +
                "2028372281~" + dd + ",";
//a=a.replace(" ","");

        editorfk.putString("ETL_TOPUP_MSDN", a);
        editorfk.commit();
    }
}