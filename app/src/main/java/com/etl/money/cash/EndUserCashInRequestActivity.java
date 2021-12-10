package com.etl.money.cash;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.etl.money.LoginActivity;
import com.etl.money.NumberTextWatcher;
import com.etl.money.R;
import com.etl.money.config.AutoLogout;
import com.etl.money.devise_info.IMEI;
import com.etl.money.global.GlabaleParameter;
import com.etl.money.partner.PaymentSQLiteAdapter;
import com.etl.money.security.CSSLSocketFactory;
import com.etl.money.security.CryptoHelper;
import com.etl.money.security.GetLocation;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EndUserCashInRequestActivity extends AppCompatActivity {
    PaymentSQLiteAdapter helper;
    String strBrandID = "3";
    TextView txtCustomerID, txtCustomerNameForPayment, txtCustomerNameForQrPay, txtPendingAmount, txtEnterEtlPaymentVerifycode;
    TextView txtCustomerIDForPaymentView,txtCustomerNameForPaymentView, txtPendingAmountView, txtEnterPaymentAmountView, txtEnterEtlPaymentVerifycodeView,tv_enter_opt_password;
    EditText txtEnterPaymentAmount;
    Button btnSubmit, btnCancel;
    String StrEtlPaymentID = "";


    String StrVerifyType = "1"; // 1=OTP, 2=PSW
    String strRequestType ;
    String strMsisdn;
    String paymentType = "1";
    String StrShowOrHidePassword = "2";
    String StrStepOPT = "1";
    String sss;
    String ViewStubCode = "Home";
    String ActivityKey ;
    ImageView imgBill ;
    String strCustomerInfo = "";
    LinearLayout lnlCustomerID, lnlCustomertInfo, lnlAmtInput , lnlVerifycodeInput ;
    RadioButton rdoCode, rdoQR ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_user_cash_in_request);
        helper = new PaymentSQLiteAdapter(this);
        SharedPreferences msn = getSharedPreferences(GlabaleParameter.PREFS_MSISDN, Context.MODE_PRIVATE);
        strMsisdn = msn.getString("msisdn", "");

        Intent intent = getIntent();
      //  ActivityKey = intent.getStringExtra("ActivityKey");
        ActivityKey =  "enduser_cashin";
        // Sub ETL Payment

        txtCustomerID = (EditText) findViewById(R.id.txtCustomerID);
        txtCustomerNameForPayment = (TextView) findViewById(R.id.txtCustomerNameForPayment);
        txtCustomerNameForQrPay = (TextView) findViewById(R.id.txtCustomerNameForQrPay);
        txtPendingAmount = (TextView) findViewById(R.id.txtPendingAmount);
        txtEnterPaymentAmount = (EditText) findViewById(R.id.txtEnterPaymentAmount);
        txtEnterEtlPaymentVerifycode = (EditText) findViewById(R.id.txtEnterEtlPaymentVerifycode);
        tv_enter_opt_password = findViewById(R.id.tv_enter_opt_password);
        txtCustomerNameForPaymentView = (TextView) findViewById(R.id.txtCustomerNameForPaymentView);
        txtEnterPaymentAmountView = (TextView) findViewById(R.id.txtEnterPaymentAmountView);
        txtPendingAmountView = (TextView) findViewById(R.id.txtPendingAmountView);
        txtEnterEtlPaymentVerifycodeView = (TextView) findViewById(R.id.txtEnterEtlPaymentVerifycodeView);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        lnlCustomertInfo = (LinearLayout) findViewById(R.id.lnlCustomertInfo);
        lnlAmtInput = (LinearLayout) findViewById(R.id.lnlAmtInput);
        lnlVerifycodeInput = (LinearLayout) findViewById(R.id.lnlVerifycodeInput);
        lnlCustomerID = (LinearLayout) findViewById(R.id.lnlCustomerID);
        txtCustomerIDForPaymentView  = (TextView) findViewById(R.id.txtCustomerIDForPaymentView);

        rdoCode  = (RadioButton) findViewById(R.id.rdoCode);
        rdoQR  = (RadioButton) findViewById(R.id.rdoQR);

        txtEnterPaymentAmount.addTextChangedListener(new NumberTextWatcher(txtEnterPaymentAmount));
        imgBill = (ImageView) findViewById(R.id.imgBill);

        //Log.e("StrStepOPT:",  StrStepOPT);
        // insert(String strTitle , String strDescription )
        //  Intent refresh1 = new Intent(getApplicationContext(), SelectBranchActivity.class);
        //  startActivity(refresh1);
        StrStepOPT="1";
        txtCustomerID.setText(strMsisdn);
        txtCustomerID.setEnabled(false);
        ClearTextEtlPayment();
    //    String data = helper.getData(ActivityKey);
    }

    private void insert(String strTitle , String strDescription, String strBrandID, String strType ) {
        String currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
        if(strTitle.isEmpty() || strDescription.isEmpty())
        {
            //   NotificationMessage.notificationMessage(getApplicationContext(),"Enter Both Name and Password");
        }
        else
        {
            long id = helper.insertData(strTitle,strDescription,currentDate,strBrandID,strType);
            if(id<=0)
            {
                //    NotificationMessage.notificationMessage(getApplicationContext(),"Insertion Unsuccessful");

            } else
            {
                //    NotificationMessage.notificationMessage(getApplicationContext(),"Insertion Successful");

            }
        }
    }
    private void delete( String strDescription ) {
/*
        if(strDescription.isEmpty())
        {
            //   NotificationMessage.notificationMessage(getApplicationContext(),"Enter Both Name and Password");
        }
        else
        {
           // long id = helper.delete(strDescription);
            if(id<=0)
            {
                //    NotificationMessage.notificationMessage(getApplicationContext(),"Insertion Unsuccessful");

            } else
            {
                //    NotificationMessage.notificationMessage(getApplicationContext(),"Insertion Successful");
            }
            // }
        }

 */
    }
    private void dialog(String Str, int ico) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Str);
        builder.setCancelable(false)
                .setPositiveButton(R.string.str_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
        AlertDialog alert = builder.create();
        if (ico == 1) { // Success
            alert.setTitle(R.string.str_successful);
            alert.setIcon(R.drawable.ic_success);
        } else if (ico == 2) { //Fail
            alert.setTitle(R.string.str_failed);
            alert.setIcon(R.drawable.ic_warning);
        } else if (ico == 3) { // Error
            alert.setTitle(R.string.str_error);
            alert.setIcon(R.drawable.ic_error);
        }
        alert.show();
    }

    private void ConnectAPI(final String StrDestNum) {
        final String strGetLocation = GetLocation.get(this);
        final Activity act = this;
        final ProgressDialog progress_sub = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        progress_sub.setMessage(getString(R.string.str_pleasewait_inprocess));
        progress_sub.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress_sub.show();
     //   String url = getString(R.string.str_url_https) + "WalletPayment_" + ActivityKey + ".php"; // WalletPayment_water_supply WalletAgentCashIn
        String url = getString(R.string.str_url_https) + "WalletEndUserCashInRequest.php"; // WalletPayment_water_supply
        // Log.e("url_info:", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                           Log.e("response11:", response);
                        try {
                            JSONObject jObject = new JSONObject(response);
                            if (jObject.getString("status").equals("OK")) {
                                CryptoHelper cryptoHelper = new CryptoHelper();
                                String result = "";
                                try {
                                    result = cryptoHelper.decrypt(jObject.getString("result"));
                                } catch (Exception e) {
                                }

                                String[] arry = result.split("\\|");
                                //  Log.e("resultaaa:", result);
                                if (arry[0].equals("405000000")) {
                                    // txtCustomerName.setText("sdsdfd");
                                    Log.e("key:", result+"aa"+arry[3]);
                                    if (strMsisdn.equals(arry[1])) {

                                        if (arry[3].equals("LoadInfo")) {
                                            btnCancel.setVisibility(View.VISIBLE);
                                            //  String str = getString(R.string.str_please_enter) +", "+ getString(act, "str_"+ActivityKey).toLowerCase()+" "+getString(R.string.str_code).toLowerCase();;
                                            lnlCustomerID.setVisibility(View.GONE);
                                            lnlAmtInput.setVisibility(View.VISIBLE);
                                            float floDebit =Float.parseFloat(arry[4]);
                                            String strDebitName =   getString(R.string.str_debit2);
                                            if (floDebit<0){
                                                strDebitName =   getString(R.string.str_advance2);
                                            }
                                            String Str ="";
                                            String strDebitAmount =   new DecimalFormat("#,###,###").format(floDebit);
                                            txtEnterPaymentAmount.setText(strDebitAmount+"");
                                            if (floDebit<1){
                                                txtEnterPaymentAmount.setText("");
                                            }
                                            Str += getString(act, "str_code_"+ActivityKey)+";"+ txtCustomerID.getText().toString() +"|";
                                            Str += getString(R.string.str_branch)+";"+ arry[6] +"|";
                                            Str += getString(R.string.str_name)+";"+arry[5]+"|";

                                            Str += strDebitName+";"+ floDebit +"|";
                                            strCustomerInfo = Str;
                                            LoadCustomerInfo(Str);
                                            StrStepOPT = "2" ;
                                            btnSubmit.setText(getString(R.string.str_next));
                                            String currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
                                            helper.insertData(txtCustomerID.getText().toString() ,arry[5],currentDate,arry[6],""+ActivityKey);
                                        }   else if (arry[3].equals("GetOTP")) {
                                            lnlAmtInput.setVisibility(View.GONE);
                                            lnlVerifycodeInput.setVisibility(View.VISIBLE);
                                            StrStepOPT = "4" ;
                                            btnSubmit.setText(getString(R.string.str_submit));
                                        }   else if (arry[3].equals("GenQR")) {

                                            /*
                                            lnlAmtInput.setVisibility(View.GONE);
                                            lnlVerifycodeInput.setVisibility(View.VISIBLE);
                                            //StrStepOPT = "4" ;
                                            String str = getString(act, "str_you_have_successful_for_send_request_cash_in");
                                            dialog(str, 1);  // 1:Success, 2:Fail, 3:Error
                                            LoadCustomerInfo("");

                                             */




                                            String strQrTypeName  = getResources().getString(R.string.str_enduser_cashin);

                                            String referenceID =arry[5] ;
                                            String referenceCode =arry[6] ;
                                            String countdown =arry[7] ;


                                            String strpref = strCustomerInfo+"ReferenceCode;"+referenceCode+"|QrTypeName;"+strQrTypeName+"|Countdown;"+countdown+"|referenceID;"+referenceID;




                                            SharedPreferences pref = getApplicationContext().getSharedPreferences(GlabaleParameter.PREFS_CASHINREQUEST, MODE_PRIVATE);
                                            SharedPreferences.Editor editor = pref.edit();
                                            editor.putString("strValues", strpref);  // Saving string
                                            editor.commit(); // commit changes

                                            LoadCustomerInfo("");
                                            ClearTextEtlPayment();
                                            Intent refresh1 = new Intent(getApplicationContext(), AutoGenerateQRCode.class);
                                            startActivity(refresh1);
                                        }     else if (arry[3].equals("PayAmount")) {
                                            lnlAmtInput.setVisibility(View.GONE);
                                            lnlVerifycodeInput.setVisibility(View.VISIBLE);
                                            //StrStepOPT = "4" ;
                                            String str = getString(act, "str_you_have_successful_for_send_request_cash_in");
                                            dialog(str, 1);  // 1:Success, 2:Fail, 3:Error
                                            LoadCustomerInfo("");
                                            ClearTextEtlPayment();
                                            // btnSubmit.setText(getString(R.string.str_pay));
                                        }
                                    }
                                } else {

                                    dialog(arry[2], 2);  // 1:Success, 2:Fail, 3:Error
                                    //  ClearTextEtlPayment();
                                }
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplication(), getString(R.string.str_can_not_connect_to_server), Toast.LENGTH_LONG).show();
                        }
                        progress_sub.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplication(), getString(R.string.str_can_not_connect_to_server), Toast.LENGTH_LONG).show();
                        progress_sub.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                // ===== StrBasic_info info
                IMEI imei = new IMEI();
                int verCode = imei.get_versionCode(getApplication());
                String currentDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
                SharedPreferences pref = getApplicationContext().getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, MODE_PRIVATE);
                String Strlanguage = pref.getString(GlabaleParameter.PREFS_ETL_LANGGAUGE, "en");
                String StrdeviceInfo = pref.getString(GlabaleParameter.PREFS_ETL_DEVICEINFO, null);
                String Strtoken = pref.getString(GlabaleParameter.PREFS_ETL_TOKEN_WALLET_API, null);
                String StrjwtToken = pref.getString(GlabaleParameter.PREFS_JWT_TOKEN, null);
                StrdeviceInfo = strGetLocation + "~" + StrdeviceInfo;
                String strBasic_info = strMsisdn + "|" + verCode + "|" + currentDate + "|" + Strlanguage + "|" + StrdeviceInfo + "|" + Strtoken + "|" + StrjwtToken;
                // ===== End StrBasic info

                SharedPreferences prefpaymentType = getApplicationContext().getSharedPreferences(GlabaleParameter.PAYMENT_TPYE, MODE_PRIVATE);
                paymentType = prefpaymentType.getString(GlabaleParameter.PREFS_PAYMENT_TPYE, "1");
                strBasic_info += "|" + paymentType;

                String StrPublickey = "";

                if (StrVerifyType.equals("1")) { // 1=OTP, 2=PSW
                    if (StrStepOPT.equals("1")) {  // LoadInfo
                        strBasic_info += "|" + StrDestNum + "|";
                        StrPublickey = "LoadInfo";
                    } else if (StrStepOPT.equals("3")) { //  Input Amt
                        if (rdoCode.isChecked()) { StrPublickey = "GetOTP";}else{  StrPublickey = "GenQR";}
                      //  StrPublickey = "GetOTP";
                        String StrAmount = txtEnterPaymentAmount.getText().toString();
                        StrAmount = StrAmount.replace(".", "");
                        StrAmount = StrAmount.replace(",", "");
                      //  strBasic_info += "|" + StrDestNum + "|" + StrAmount;
                        //String StrAmount = txtEnterPaymentAmount.getText().toString();
                        String strEtlPaymentVerifycode = txtEnterEtlPaymentVerifycode.getText().toString();
                        StrAmount = StrAmount.replace(".", "");
                        StrAmount = StrAmount.replace(",", "");
                        strBasic_info += "|" + StrDestNum + "|" + StrAmount + "|" + strEtlPaymentVerifycode + "|OTP|"+strRequestType;

                    } else if (StrStepOPT.equals("4")) { //  Pay

                        StrPublickey = "PayAmount";
                        String StrAmount = txtEnterPaymentAmount.getText().toString();
                        String strEtlPaymentVerifycode = txtEnterEtlPaymentVerifycode.getText().toString();
                        StrAmount = StrAmount.replace(".", "");
                        StrAmount = StrAmount.replace(",", "");
                        strBasic_info += "|" + StrDestNum + "|" + StrAmount + "|" + strEtlPaymentVerifycode + "|OTP|"+strRequestType;
                    }
                }
                CryptoHelper cryptoHelper = new CryptoHelper();
                String encryptString = "";
                try {
                    encryptString = cryptoHelper.encrypt(strBasic_info);
                } catch (Exception e) {
                }
                params.put("publickey", StrPublickey);
                params.put("Active_values", encryptString);
                  Log.e("publickey", StrPublickey);
                 Log.e("Active_values", strBasic_info);
                  Log.e("encryptString20210409", encryptString);
                   Log.e("StrStepOPT", StrStepOPT);
                 params.put("",encryptString);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this, new HurlStack(null, CSSLSocketFactory.getSocketFactory(this)));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            Toast.makeText(getApplication(), getString(R.string.str_can_not_connect_to_server), Toast.LENGTH_LONG).show();
            //Log.e("", e.toString());
            progress_sub.dismiss();
        }
    }
    private void LoadVerifyTextHint() {
        Log.e("LoadVerifyTextHint:", StrVerifyType);
        final TextView txtVerifyCodeTransfer = (TextView) findViewById(R.id.txtVerifyCodeTransfer);
        final TextView txtVerifyCodeTopup = (TextView) findViewById(R.id.txtVerifyCodeTopup);
        if (StrVerifyType.equals("1")) { // OTP
            Log.e("StrVerifyType20210115", StrVerifyType + " is OTP");
            txtVerifyCodeTransfer.setHint(R.string.str_modify_code);
            txtEnterEtlPaymentVerifycode.setHint(R.string.str_modify_code);
            txtVerifyCodeTransfer.setInputType(InputType.TYPE_CLASS_NUMBER);
            tv_enter_opt_password.setText(R.string.pls_enter_opt);
            StrShowOrHidePassword = "2";
            ShowOrHidePassword();
        } else if (StrVerifyType.equals("2")) {  // Password
            tv_enter_opt_password.setText(R.string.pls_enter_password);
            Log.e("StrVerifyType20210115", StrVerifyType + " is Password");
            txtVerifyCodeTransfer.setHint(R.string.pass_hint);
            txtEnterEtlPaymentVerifycode.setHint(R.string.pass_hint);
            txtVerifyCodeTransfer.setInputType(InputType.TYPE_CLASS_TEXT);
            StrShowOrHidePassword = "0";
            ShowOrHidePassword();
        }
    }

    private void ShowOrHidePassword() {
        Log.e("StrShowOrHidePassword= ", StrShowOrHidePassword);
//        1	OTP Code
//        2	Password
//        3	Finger print

        if (StrShowOrHidePassword.equals("0")) {  //2 Password (hidden password when clicking the eyes icon)
            txtEnterEtlPaymentVerifycode.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_pw_hide), null);
            // txtVerifyCodeTransfer.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            // txtEnterEtlPaymentVerifycode.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            StrShowOrHidePassword = "1";


        } else if (StrShowOrHidePassword.equals("1")) { //  2	Password	OTP Code (Shows password when clicking the eyes icon)

            txtEnterEtlPaymentVerifycode.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_pw_show), null);

            // txtVerifyCodeTransfer.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
            // txtEnterEtlPaymentVerifycode.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
            StrShowOrHidePassword = "0";  // ?

        } else if (StrShowOrHidePassword.equals("2")) {  //   1	OTP Code (hidden icon eyes)

            txtEnterEtlPaymentVerifycode.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_blank), null);
            StrShowOrHidePassword = "2";  // ?
        }
    }
    private void SetCustomerHistory(String StrCustomer, String CustomerKey) {
        Date dt = new Date();
        CharSequence sdd = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm", dt.getTime());
        String dd = sdd.toString();
        int limit = 100;
        SharedPreferences pref = getApplicationContext().getSharedPreferences("ETL_CUSTOMER_HISTORY", MODE_PRIVATE);
        String s = pref.getString(CustomerKey, "");

        if (s.length() > 5) {
            String aNumss[] = s.split(",");
            for (String c : aNumss) {
                String mNd = c;
                String[] arrmNd = mNd.split("~");
                sss = arrmNd[0];
                if (StrCustomer.equals(sss)) {
                    String cc = arrmNd[1];
                    s = s.replace(sss + "~" + cc, "");
                }
            }
        }

        //  s = s.replace(msisdnStr,"");
        s = s.replace(",,", ",");
        if (StrCustomer.length() > 8) {
            s = StrCustomer + "~" + dd + "," + s;
        }
        String aNums[] = s.split(",");
        s = "";
        int j = 0;
        for (int i = 0; i < aNums.length; i++) {

            if (aNums[i].length() > 8) {
                j++;
                if (j <= limit) {
                    s = s + "," + aNums[i];
                }
            }
        }
        if (s.length() > 8) {
            if (s.substring(0, 1).equals(",")) {
                s = s.substring(1, s.length());
            }
        }
        ///  s="==Select==,"+s;
        SharedPreferences.Editor editorfk = pref.edit();
        editorfk.putString(CustomerKey, s);
        editorfk.apply();
        // String[] number = s.split(",");
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

    public static String getString(Context context, String idName) {
        Resources res = context.getResources();
        String str =  idName;
        try {
            str =  res.getString(res.getIdentifier(idName, "string", context.getPackageName()));
        } catch (Exception e) {
        }
        return str;
    }
    public void onClickCancel(View v) {
        ClearTextEtlPayment();
    }

    private void ClearTextEtlPayment() {
        btnCancel.setVisibility(View.GONE);
        btnSubmit.setText(getString(R.string.str_next));
        lnlCustomerID.setVisibility(View.VISIBLE);
        lnlCustomertInfo.setVisibility(View.GONE);
        lnlAmtInput.setVisibility(View.VISIBLE);
        lnlVerifycodeInput.setVisibility(View.GONE);
        StrStepOPT="1";
        String data = helper.getData(ActivityKey);
        LoadCustomerInfo("");
        // ===
/*
        btnCancel.setVisibility(View.VISIBLE);
        lnlCustomerID.setVisibility(View.GONE);
        lnlAmtInput.setVisibility(View.VISIBLE);
        SharedPreferences pref = getApplicationContext().getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, MODE_PRIVATE);
        String Strlanguage = pref.getString(GlabaleParameter.PREFS_ETL_LANGGAUGE, "en");
        String Str ="";
        Str += getString(this, "str_code_"+ActivityKey)+";"+ txtCustomerID.getText().toString() +"|";
        Str += getString(R.string.str_name)+";"+pref.getString(GlabaleParameter.PREFS_ETL_CUSTOMER_NAME, "")+" "+pref.getString(GlabaleParameter.PREFS_ETL_CUSTOMER_LAST_NAME, "")+"|";
        strCustomerInfo = Str;
        LoadCustomerInfo(Str);
        StrStepOPT = "2" ;
        btnSubmit.setText(getString(R.string.str_next));

 */
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        //   Log.e("strExtra: (", ""+data.getStringExtra("strExtra"));
        if(requestCode==2)
        {
            ConnectAPI(  txtCustomerID.getText().toString());
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_icon_close, menu);
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.actionbar_layout_title_center);
            TextView mytext = (TextView) findViewById(R.id.txtcenter);
            //  mytext.setText(R.string.str_sms_setting);
            mytext.setText( getString(this, "str_"+ActivityKey) );
            imgBill.setImageResource(getResources().getIdentifier("drawable/"+"bill_"+ActivityKey, null, getPackageName()));
            txtCustomerID.setHint( getString(this, "str_code_enter_"+ActivityKey));
            txtCustomerIDForPaymentView.setText( getString(this, "str_code_"+ActivityKey));

        } catch (Exception e) {
        }
        return true;
    }


    private void LoadCustomerInfo(String Str){
         //Log.e("LoadCustomerInfoaaa:",  Str);
/*
        String Str ="";
        String strBalance = " "+ new DecimalFormat("#,###,###").format(Integer.parseInt(arry[4])) + " " + getResources().getString(R.string.str_kip);
        Str += "Custommer ID;"+ txtCustomerID.getText().toString() +"|";
        Str += "Branch;"+ arry[6] +"|";
        Str += "Custommer Name;"+arry[5]+"|";
        Str += "Debit;"+ strBalance +"|";
        strCustomerInfo = Str;
        LoadCustomerInfo(Str);
        */

        ListView listView;
        ArrayList<CashData> arrayList = new ArrayList<>();
        CashAdapter adapter;
        listView = findViewById(R.id.listViewPayment);
        if (!Str.equals("")) {
        String[] arryData = Str.split("\\|");
        for (String str : arryData) {
            String[] ArrB = str.split( ";" );

            arrayList.add(new CashData(2, ArrB[0]+":",""+ ArrB[1],"",""));
        }
        adapter = new CashAdapter(this, arrayList);
        listView.setAdapter(adapter);
        } else {
            listView.setAdapter(null);
        }
    }

    public void onClickSubmitEndUserCashIn(View v) {
        //Log.e("StrStepOPT20210409:",  StrStepOPT);
        String EnterPaymentAmount = txtEnterPaymentAmount.getText().toString();
        if (StrStepOPT.equals("1")){ // === Query info
            // StrStepOPT = "1" ;
            String strDestNum = txtCustomerID.getText().toString();
            if (strDestNum.trim().length() < 2 ) {
                txtCustomerID.requestFocus();
                String str = getString(R.string.str_please_enter) + ", " + getString(this, "str_code_" + ActivityKey).toLowerCase();
                txtCustomerID.setError(str);
                return;


            } else   if (EnterPaymentAmount.trim().length() < 1 ) {
                    txtEnterPaymentAmount.requestFocus();
                    String str = getString(R.string.str_please_enter_amount) ;
                    txtEnterPaymentAmount.setError(str);
                    return;

            } else   {

                btnCancel.setVisibility(View.VISIBLE);
                lnlCustomerID.setVisibility(View.GONE);
                lnlAmtInput.setVisibility(View.VISIBLE);
                SharedPreferences pref = getApplicationContext().getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, MODE_PRIVATE);
                String Strlanguage = pref.getString(GlabaleParameter.PREFS_ETL_LANGGAUGE, "en");
                String Str ="";
                Str += getString(this, "str_code_"+ActivityKey)+";"+ txtCustomerID.getText().toString() +"|";
                Str += getString(R.string.str_name)+";"+pref.getString(GlabaleParameter.PREFS_ETL_CUSTOMER_NAME, "")+" "+pref.getString(GlabaleParameter.PREFS_ETL_CUSTOMER_LAST_NAME, "")+"|";

                if (rdoCode.isChecked()) {
                    strRequestType =  "1";
                    Str += getResources().getString(R.string.str_request_type)+";"+getResources().getString(R.string.str_cash_in_code)+"|";
                    btnSubmit.setText(getString(R.string.str_get_otp));
                }else {
                    strRequestType =  "2";
                    Str += getResources().getString(R.string.str_request_type)+";"+getResources().getString(R.string.str_cash_in_qr_code)+"|";
                    btnSubmit.setText(getString(R.string.str_get_QR));
                }
                strCustomerInfo = Str;
                LoadCustomerInfo(Str);
                StrStepOPT = "2" ;


                // ConnectAPI( strDestNum);






                lnlAmtInput.setVisibility(View.GONE);
                String   strBalance   = txtEnterPaymentAmount.getText().toString();
                strBalance =  strBalance.replace(".","");
                strBalance =   strBalance.replace(",","");
                strBalance =  new DecimalFormat("#,###,###").format(Integer.parseInt(strBalance + ""));
                txtEnterPaymentAmount.setText(strBalance+"");
                strCustomerInfo += getResources().getString(R.string.str_amount)+";"+ strBalance  +"|";
                LoadCustomerInfo(strCustomerInfo);
                StrStepOPT = "3" ;

                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);




            }

        }else if (StrStepOPT.equals("2")){ // === Input Amount
          /*  String EnterPaymentAmount = txtEnterPaymentAmount.getText().toString();
            if (EnterPaymentAmount.trim().length() < 1 ) {
                txtEnterPaymentAmount.requestFocus();
                String str = getString(R.string.str_please_enter_amount) ;
                txtEnterPaymentAmount.setError(str);
                return;
            }
            lnlAmtInput.setVisibility(View.GONE);
            String   strBalance   = txtEnterPaymentAmount.getText().toString();
            strBalance =  strBalance.replace(".","");
            strBalance =   strBalance.replace(",","");
            strBalance =  new DecimalFormat("#,###,###").format(Integer.parseInt(strBalance + ""));
            txtEnterPaymentAmount.setText(strBalance+"");
            strCustomerInfo += getResources().getString(R.string.str_amount)+";"+ strBalance  +"|";
            LoadCustomerInfo(strCustomerInfo);
            StrStepOPT = "3" ;
            btnSubmit.setText(getString(R.string.str_get_otp));
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            //  InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            // imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            //  InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            //  imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);*/
        }else if (StrStepOPT.equals("3")){  // === Get OTP
            String strDestNum = txtCustomerID.getText().toString();
            ConnectAPI( strDestNum);
        }else if (StrStepOPT.equals("4")){  // === Pay
            String EnterEtlPaymentVerifycode = txtEnterEtlPaymentVerifycode.getText().toString();
            if (EnterEtlPaymentVerifycode.trim().length() < 5 ) {
                txtEnterEtlPaymentVerifycode.requestFocus();
                String str = getString(R.string.str_please_enter_verify_code) ;
                txtEnterEtlPaymentVerifycode.setError(str);
                return;
            }
            final AlertDialog alert = new AlertDialog.Builder(this).create();
            alert.setTitle(R.string.str_confirm);
            alert.setMessage(getString(this, "str_you_would_like_to_sent_request_cash_in"));
            alert.setIcon(R.drawable.ic_question);
            alert.setButton(getResources().getString(R.string.str_yes_to_exit), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String strDestNum = txtCustomerID.getText().toString();
                    ConnectAPI( strDestNum);
                }
            });
            alert.setButton2(getResources().getString(R.string.str_no_to_exit), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alert.show();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
       /* SharedPreferences.Editor editor = getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, Context.MODE_PRIVATE).edit();
        editor.putString(GlabaleParameter.PREFS_RESUM_CHECK_BALANCE_AFTER_CHARGE, "1" );
        editor.apply();*/
        //===Auto Logout
        SharedPreferences pref = getApplicationContext().getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, MODE_PRIVATE);
        String strFromDate = pref.getString(GlabaleParameter.PREFS_RESUM_LAST_DATE, "");
        int Countdow  = Integer.parseInt(pref.getString(GlabaleParameter.PREFS_ETL_AUT_LOGOOT, ""));
        String strCheckBalanceAfterCharge = pref.getString(GlabaleParameter.PREFS_RESUM_CHECK_BALANCE_AFTER_CHARGE, "");
        String strToDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
        SharedPreferences.Editor editor = getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, Context.MODE_PRIVATE).edit();
        editor.putString(GlabaleParameter.PREFS_RESUM_LAST_DATE, strToDate);

        if (strFromDate.equals("")){strFromDate=strToDate;}
        Integer DateDateDiff = AutoLogout.Difference(strFromDate, strToDate);
        if (DateDateDiff>Countdow){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        editor.apply();
    }

}