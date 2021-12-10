package com.etl.money;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.etl.money.PackageData.PackageDataActivity;
import com.etl.money.cash.AgentApproveCashoutOfMerchantActivity;
import com.etl.money.cash.AgentCashInApproveActivity;
import com.etl.money.cash.AgentCashOutApproveActivity;
import com.etl.money.cash.ChooseCashInActivity;
import com.etl.money.cash.ChooseCashOutActivity;
import com.etl.money.cash.MerchantCashOutRequestActivity;
import com.etl.money.cash.MerchantTransferToEnduserActivity;
import com.etl.money.change_passwor.ChangePasswordWalletActivity;
import com.etl.money.config.AutoLogout;
import com.etl.money.config.DeviceListActivity;
import com.etl.money.config.MerchantQrCodeActivity;
import com.etl.money.config.QrCodeActivity;
import com.etl.money.config.ScannerActivity;
import com.etl.money.dashboard.CustomAdapter;
import com.etl.money.dashboard.DashboardCustomGridViewAdapter;
import com.etl.money.dashboard.DashboardPersonUtils;
import com.etl.money.dashboard.SectionedGridRecyclerViewAdapter;
import com.etl.money.devise_info.IMEI;
import com.etl.money.global.GlabaleParameter;
import com.etl.money.history.AgentHistoryWalletActivity;
import com.etl.money.history.HistoryWalletActivity;
import com.etl.money.history.MerchantHistoryWalletActivity;
import com.etl.money.notification.NotificationListActivity;
import com.etl.money.notification.NotificationSQLiteAdapter;
import com.etl.money.partner.PaymentsActivity;
import com.etl.money.promotion_and_advertising.PromotionActivity;
import com.etl.money.register.RegisterNewAgentActivity;
import com.etl.money.register.RegisterNewMerchantActivity;
import com.etl.money.security.CSSLSocketFactory;
import com.etl.money.security.CryptoHelper;
import com.etl.money.security.GetLocation;
import com.etl.money.setting.AgentBalanceInfoActivity;
import com.etl.money.setting.AgentSettingActivity;
import com.etl.money.setting.MerchantBalanceInfoActivity;
import com.etl.money.setting.MerchantSettingActivity;
import com.etl.money.setting.SettingActivity;
import com.etl.money.setting.UserWalletInfoActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

public class WalletActivity extends AppCompatActivity implements View.OnTouchListener {
    private RecyclerView mRecyclerView;

    private float oldXvalue;
    private float oldYvalue;
    CustomAdapter mAdapter;
    NotificationSQLiteAdapter helper;
    // String etlPreFix = "(202|302|202|203|202).*";
    String strQrType = "";
    String strEndUserShowMore = "0";
    String strAgentShowMore = "0";
    String strMerchantShowMore = "0";
  //  GridView gridView;
    DashboardCustomGridViewAdapter customGridAdapter;
    // private com.etl.money.dashboard.DashboardCustomRecyclerAdapter DashboardCustomRecyclerAdapter;
    RecyclerView recyclerView;
  //  RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<DashboardPersonUtils> personUtilsList;
    // ArrayList<DashboardPersonUtils> dataModelArrayList;
    ArrayList<HashMap<String, String>> MyArrList;
    String StrShowOrHidePassword = "2";
    String StrDestinationType = "";
    //   StrVerifyType
    String paymentType = "1";
    private Context context;
    private SwipeRefreshLayout swipeContainer;
    TextView txtMobilenumber, txtCardId, txtCardName, txtNotRegistered ,txtAmount;
    ViewStub subHome;
    ViewStub dashboard;
    ViewStub subTopup;
    ViewStub subTransfer;

    ViewStub subEtlPayment;
    ViewStub subEtlPaymentList;
    ViewStub SubQrPay;
    String strMsisdn;
    String ViewStubCode = "Home";
    ImageView imgQrCodeTransfer;
    ImageView imgQrCodeTopup;
    TextView txtDestNum;

    Button btn_topup, btn_submit_tran;
    EditText txtTranAmt;
    String StrVerifyType = "0";
    Integer StrTransferTy = 0;
    String StrStepOPT = "1";
    String StrEtlPaymentKey = "";
    String StrStepPayment = "1";
    String StrDestNumber = "";
    String sss;
    Button btnEtlPaymentQuery, btnEtlPaymentSubmit;
    private RadioButton radioTopupToOwner, radioTopupToOther;
    private TextView txt_owner_number, txt_other_number;
    EditText txtAmountTopupWallet;
    TextView txt_owner, txt_other;
    TextView txtCustomerID, txtCustomerNameForPayment, txtCustomerNameForQrPay, txtPendingAmount, txtEnterEtlPaymentVerifycode,str_customer_id;
    TextView txtCustomerNameForPaymentView, txtPendingAmountView, txtEnterPaymentAmountView, txtEnterEtlPaymentVerifycodeView,tv_enter_opt_password;
    EditText txtEnterPaymentAmount;
    TextView txtVerifyCodeTransfer;
    TextView txtVerifyCodeTopup;
    TextView txtDestinationType;
    TextView txtNumberType;
    TextView txtBalance,txtMerchantNum;
    TextView txtstrNumberType;
    TextView txtstrBalance;
    TextView txtstrAmountTopupWallet;
    TextView txtstrVerifyCodeTopup;
    TextView txtDescrip;
    TextView txtOTPCode;
    TextView txtVerifyCodeQrPay;
    EditText txttxtMerchantAmt;
    TextView txtMerchantDescrip;
    String StrEtlPaymentID = "";
    String StrEtlPaymentText = "";
    String StrEtlPaymentTitle = "";
    ImageView imgProfile;
Button btn_submit_QrPay;
    TextView txtDestination_name;
     LinearLayout LinearLayoutDestination_name ;
    private String numberToTopup, StatusToselect = "1";  // = 1 owner number, 2= other number
    TextView txtClose;
    LinearLayout layoutheaderInfo;
    //TextView txtEnterEtlPaymentVerifycode,txtEnterPaymentAmount ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        helper = new NotificationSQLiteAdapter(this);



      //  ImageView image1 = (ImageView) findViewById(R.id.image1);
      //  image1.setOnTouchListener(this);

    //    CheckConfirmMethod_otp_or_password();

        // Wallate Main

        txtMobilenumber = (TextView) findViewById(R.id.txtMobilenumber);


        txtCardId = (TextView) findViewById(R.id.txtCardId);
        txtCardName = (TextView) findViewById(R.id.txtCardName);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);


        txtClose = (TextView) findViewById(R.id.txtClose);
        layoutheaderInfo = (LinearLayout) findViewById(R.id.layoutheaderInfo);


        subHome = (ViewStub) findViewById(R.id.ViewStubHone);
        subHome.setLayoutResource(R.layout.wallet_home);
        subHome.inflate();
        dashboard = (ViewStub) findViewById(R.id.ViewDashboard);
        dashboard.setLayoutResource(R.layout.dashboard_main);
        dashboard.inflate();
        subTopup = (ViewStub) findViewById(R.id.ViewStubTopup);
        subTopup.setLayoutResource(R.layout.wallet_topup);
        subTopup.inflate();

        subTransfer = (ViewStub) findViewById(R.id.ViewStubTransfer);
        subTransfer.setLayoutResource(R.layout.wallet_transfer);
        subTransfer.inflate();



        SubQrPay = (ViewStub) findViewById(R.id.ViewStubQrPay);
        SubQrPay.setLayoutResource(R.layout.wallet_qr_pay);
        SubQrPay.inflate();

        subEtlPaymentList = (ViewStub) findViewById(R.id.ViewStubEtlPaymentList);
        subEtlPaymentList.setLayoutResource(R.layout.etl_payment_list);
        subEtlPaymentList.inflate();
        subEtlPayment = (ViewStub) findViewById(R.id.ViewStubEtlPayment);
        subEtlPayment.setLayoutResource(R.layout.etl_payment);
        subEtlPayment.inflate();

        LoadViewStub("Home");
        // Sub View Tansfer
        txtDestNum = (TextView) findViewById(R.id.txtDestNum);

        txtTranAmt = (EditText) findViewById(R.id.txtTranAmt);
        txtTranAmt.addTextChangedListener(new NumberTextWatcher(txtTranAmt));
        // Sub View Topup
        txt_owner_number = (TextView) findViewById(R.id.txt_owner_number);
        txt_other_number = (TextView) findViewById(R.id.txt_other_number);
        txt_owner = (TextView) findViewById(R.id.txt_owner);
        txt_other = (TextView) findViewById(R.id.txt_other);
        txtNumberType = (TextView) findViewById(R.id.txtNumberType);
        txtBalance = (TextView) findViewById(R.id.txtBalance);
        txtAmount = (TextView) findViewById(R.id.txtAmount);
        txtstrNumberType = (TextView) findViewById(R.id.txtstrNumberType);
        txtstrBalance = (TextView) findViewById(R.id.txtstrBalance);
        txtstrAmountTopupWallet = (TextView) findViewById(R.id.txtstrAmountTopupWallet);
        txtstrVerifyCodeTopup = (TextView) findViewById(R.id.txtstrVerifyCodeTopup);

        txtNotRegistered = (TextView) findViewById(R.id.txtNotRegistered);
        txtNotRegistered.setVisibility(View.GONE);

        txtAmountTopupWallet = (EditText) findViewById(R.id.txtAmountTopupWallet);
        radioTopupToOwner = (RadioButton) findViewById(R.id.radioTopupToOwner);
        radioTopupToOther = (RadioButton) findViewById(R.id.radioTopupToOther);
        btn_topup = (Button) findViewById(R.id.btn_topup);

        txtAmountTopupWallet.addTextChangedListener(new NumberTextWatcher(txtAmountTopupWallet));
        radioTopupToOwner.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Owner();
            }
        });
        radioTopupToOther.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Other();
            }
        });
        txt_owner.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Owner();
            }
        });
        txt_other.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Other();
            }
        });

        // Sub ETL Payment
        txtCustomerID = (EditText) findViewById(R.id.txtCustomerID);
        txtCustomerNameForPayment = (TextView) findViewById(R.id.txtCustomerNameForPayment);
        txtCustomerNameForQrPay = (TextView) findViewById(R.id.txtCustomerNameForQrPay);
        txtPendingAmount = (TextView) findViewById(R.id.txtPendingAmount);
        txtEnterPaymentAmount = (EditText) findViewById(R.id.txtEnterPaymentAmount);
        txtEnterEtlPaymentVerifycode = (EditText) findViewById(R.id.txtEnterEtlPaymentVerifycode);
        txtVerifyCodeTransfer = (TextView) findViewById(R.id.txtVerifyCodeTransfer);
        txtVerifyCodeQrPay = (TextView) findViewById(R.id.txtVerifyCodeQrPay);
        txtVerifyCodeTopup = (TextView) findViewById(R.id.txtVerifyCodeTopup);

        txttxtMerchantAmt = (EditText) findViewById(R.id.txttxtMerchantAmt);

        txttxtMerchantAmt.addTextChangedListener(new NumberTextWatcher(txttxtMerchantAmt));
        txtMerchantDescrip = (TextView) findViewById(R.id.txtMerchantDescrip);
        txtDestinationType = (TextView) findViewById(R.id.txtDestinationType);

        txtDescrip = (TextView) findViewById(R.id.txtDescrip);
        txtMerchantNum = (TextView) findViewById(R.id.txtMerchantNum);
        txtDestination_name = (TextView) findViewById(R.id.txtDestination_name);
        txtOTPCode = (TextView) findViewById(R.id.txtOTPCode);
        LinearLayoutDestination_name = (LinearLayout) findViewById(R.id.LinearLayoutDestination_name);




        tv_enter_opt_password = findViewById(R.id.tv_enter_opt_password);


        txtCustomerNameForPaymentView = (TextView) findViewById(R.id.txtCustomerNameForPaymentView);
        txtEnterPaymentAmountView = (TextView) findViewById(R.id.txtEnterPaymentAmountView);
        txtPendingAmountView = (TextView) findViewById(R.id.txtPendingAmountView);
        txtEnterEtlPaymentVerifycodeView = (TextView) findViewById(R.id.txtEnterEtlPaymentVerifycodeView);

        //  LayoutEnterEtlPaymentVerifycode = (TextInputLayout) findViewById(R.id.LayoutEnterEtlPaymentVerifycode);
        //  LayoutEnterPaymentAmount = (TextInputLayout) findViewById(R.id.LayoutEnterPaymentAmount);
        btn_submit_tran = (Button) findViewById(R.id.btn_submit_tran);

        txtCustomerID.setEnabled(true);
        // txtCustomerName.setVisibility(View.GONE);
        //txtPendingAmount.setVisibility(View.GONE);
        //txtEnterPaymentAmount.setVisibility(View.GONE);
        // txtEnterEtlPaymentVerifycode.setVisibility(View.GONE);

        btnEtlPaymentQuery = (Button) findViewById(R.id.btnEtlPaymentQuery);
        btnEtlPaymentSubmit = (Button) findViewById(R.id.btnEtlPaymentSubmit);
        btn_submit_QrPay = (Button) findViewById(R.id.btn_submit_QrPay);
        txtEnterPaymentAmount.addTextChangedListener(new NumberTextWatcher(txtEnterPaymentAmount));

        SharedPreferences msn = getSharedPreferences(GlabaleParameter.PREFS_MSISDN, Context.MODE_PRIVATE);
        strMsisdn = msn.getString("msisdn", "");
        txtMobilenumber.setText(strMsisdn);

        //   txtCardId = (TextView) findViewById(R.id.txtCardId);
        //  txtCardName = (TextView) findViewById(R.id.txtCardName);
        //   imgProfile = (ImageView) findViewById(R.id.imgProfile);
        // txtCardName.setText();
        txtCardId.setText("XXX XXXX " + strMsisdn.substring(strMsisdn.length() - 3));
        txt_owner_number.setText(strMsisdn);

        imgQrCodeTransfer = (ImageView) findViewById(R.id.imgQrCodeTransfer);
        imgQrCodeTransfer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // new IntentIntegrator(WalletActivity.this).setCaptureActivity(MsdnHistoryActivity.class).initiateScan();
                new IntentIntegrator(WalletActivity.this).setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES).setCaptureActivity(ScannerActivity.class).initiateScan();
                //   dialog(getString(R.string.str_not_registered_for_wallet),2);  // 1:Success, 2:Fail, 3:Error
            }
        });
        imgQrCodeTopup = (ImageView) findViewById(R.id.imgQrCodeTopup);
        imgQrCodeTopup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // new IntentIntegrator(WalletActivity.this).setCaptureActivity(MsdnHistoryActivity.class).initiateScan();
                new IntentIntegrator(WalletActivity.this).setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES).setCaptureActivity(ScannerActivity.class).initiateScan();
                //  dialog(getString(R.string.str_not_registered_for_wallet),2);  // 1:Success, 2:Fail, 3:Error
            }
        });
        // refresh();

        txtDestNum.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (txtDestNum.getRight() - txtDestNum.getCompoundDrawables()[2].getBounds().width())) {
                        if (StatusToselect.equals("2")) {
                            new IntentIntegrator(WalletActivity.this).setCaptureActivity(MsdnHistoryActivity.class).initiateScan();
                        }
                        return true;
                    }
                }
                return false;
            }
        });
        txt_other_number.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (txt_other_number.getRight() - txt_other_number.getCompoundDrawables()[2].getBounds().width())) {
                        if (StatusToselect.equals("2")) {
                            new IntentIntegrator(WalletActivity.this).setCaptureActivity(MsdnHistoryActivity.class).initiateScan();
                        }
                        return true;
                    }
                }
                return false;
            }
        });
        txtVerifyCodeTopup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (txtVerifyCodeTopup.getRight() - txtVerifyCodeTopup.getCompoundDrawables()[2].getBounds().width())) {
                        ShowOrHidePassword();
                        return true;
                    }
                }
                return false;
            }
        });
        txtVerifyCodeTransfer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (txtVerifyCodeTransfer.getRight() - txtVerifyCodeTransfer.getCompoundDrawables()[2].getBounds().width())) {
                        ShowOrHidePassword();
                        return true;
                    }
                }
                return false;
            }
        });
        txtEnterEtlPaymentVerifycode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (txtEnterEtlPaymentVerifycode.getRight() - txtEnterEtlPaymentVerifycode.getCompoundDrawables()[2].getBounds().width())) {
                        ShowOrHidePassword();
                        return true;
                    }
                }
                return false;
            }
        });
        ShowOrHidePassword();
        SharedPreferences pref = getApplicationContext().getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, MODE_PRIVATE);
        String StrtMyPrefAfertLogin = "||" + pref.getString(GlabaleParameter.PREFS_ETL_MAIN_BALANCE, "0");
        StrtMyPrefAfertLogin += "|" + pref.getString(GlabaleParameter.PREFS_ETL_SUB1_BALANCE, "0");
        StrtMyPrefAfertLogin += "|" + pref.getString(GlabaleParameter.PREFS_ETL_VERIFY_TYPE, "");



        //   txtCardId = (TextView) findViewById(R.id.txtCardId);
        //  txtCardName = (TextView) findViewById(R.id.txtCardName);
        //   imgProfile = (ImageView) findViewById(R.id.imgProfile);
        txtCardName.setText(pref.getString(GlabaleParameter.PREFS_ETL_CUSTOMER_NAME, "") + " " + pref.getString(GlabaleParameter.PREFS_ETL_CUSTOMER_LAST_NAME, ""));
        txtCardId.setText("XXX XXXX " + strMsisdn.substring(strMsisdn.length() - 3));
        SharedPreferences pref_p1 = getApplicationContext().getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_PROFILE_STORE, MODE_PRIVATE);
        String StrBase64 = pref_p1.getString(GlabaleParameter.PREFS_ETL_PROFILE_PHOTOS_STORE, "");

        if (StrBase64.length() < 10) {
            StrBase64 = getResources().getString(R.string.img_take_photos);
        }

            //   Log.e("Pro file AfterLogin:", strProfileIdStore);

            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] imageBytes = baos.toByteArray();
                imageBytes = Base64.decode(StrBase64 +".", Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                Bitmap circleBitmap = Bitmap.createBitmap(decodedImage.getWidth(), decodedImage.getHeight(), Bitmap.Config.ARGB_8888);
                BitmapShader shader = new BitmapShader(decodedImage, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                Paint paint = new Paint();
                paint.setShader(shader);

                Canvas c = new Canvas(circleBitmap);
                c.drawCircle(decodedImage.getWidth() / 2, decodedImage.getHeight() / 2, decodedImage.getWidth() / 2, paint);
                imgProfile.setImageBitmap(circleBitmap);
            } catch (Exception e) {
            }

        LoadBalance(StrtMyPrefAfertLogin);
        showFirstIconOfEnduser();
        showFirstIconOfAgent();
        showFirstIconOfMerchant();

    }

    private void Owner() {

        // startActivity(getIntent());
        imgQrCodeTopup.setVisibility(View.GONE);
        radioTopupToOwner.setChecked(true);
        radioTopupToOther.setChecked(false);
        StatusToselect = "1";
        txt_owner_number.setEnabled(true);
        txt_other_number.setEnabled(false);
        txt_owner.setTextColor(Color.parseColor("#0064ff"));
        txt_other.setTextColor(Color.parseColor("#858585"));
        txt_other_number.setBackgroundResource(R.drawable.text_border_stype_for_enable);
        txt_owner_number.setBackgroundResource(R.drawable.text_border_stype);
        txt_owner_number.setTextColor(Color.parseColor("#000000"));
        txt_other_number.setTextColor(Color.parseColor("#858585"));
        txt_other_number.setTextColor(Color.parseColor("#858585"));
    }


    private void ClearTextEditTransfer() {

        final TextView txtVerifyCodeTransfer = (TextView) findViewById(R.id.txtVerifyCodeTransfer);
        btn_submit_tran.setText(getString(R.string.str_get_otp));
        txtDestNum.setText("");
        txtTranAmt.setText("");
        txtDescrip.setText("");
        txtDestNum.setEnabled(true);
        txtTranAmt.setEnabled(true);
        txtDescrip.setEnabled(true);
        txtVerifyCodeTransfer.setText("");
        Other();
        if (StrVerifyType.equals("1")) { // OTP
            LoadVerifyEnable("0");
            btn_submit_tran.setText(getString(R.string.str_get_otp));
        } else if (StrVerifyType.equals("2")) { // Password
            LoadVerifyEnable("1");
            btn_submit_tran.setText(getString(R.string.str_submit));
        }
    }
    private void ClearTextEditQrPay() {

        btn_submit_QrPay.setText(getString(R.string.str_get_otp));
        txtMerchantNum.setText("");
        txtCustomerNameForQrPay.setText("");
        txttxtMerchantAmt.setText("");
        txtMerchantDescrip.setText("");
        txtVerifyCodeQrPay.setText("");

        txtMerchantNum.setEnabled(true);
        txtCustomerNameForQrPay.setEnabled(true);
        txttxtMerchantAmt.setEnabled(true);
        txtMerchantDescrip.setEnabled(true);


        if (StrVerifyType.equals("1")) { // OTP
            LoadVerifyEnable("0");
            btn_submit_QrPay.setText(getString(R.string.str_get_otp));
        } else if (StrVerifyType.equals("2")) { // Password
            LoadVerifyEnable("1");
            btn_submit_QrPay.setText(getString(R.string.str_submit));
        }
    }


    private void ClearTextEtlPayment() {
        txtCustomerID.setEnabled(true);
        txtCustomerID.setText("");
        txtCustomerNameForPayment.setText("");
        txtPendingAmount.setText("");
        txtEnterPaymentAmount.setText("");
        txtEnterEtlPaymentVerifycode.setText("");
        txtCustomerNameForPayment.setEnabled(false);
        txtPendingAmount.setEnabled(false);
        txtEnterPaymentAmount.setEnabled(false);
        txtEnterEtlPaymentVerifycode.setEnabled(false);
        // btnEtlPaymentSubmit.setEnabled(false);
        btnEtlPaymentSubmit.setVisibility(View.GONE);
        btnEtlPaymentQuery.setVisibility(View.VISIBLE);


//txtCustomerNameForPaymentView , txtEnterPaymentAmountView , txtPendingAmountView , txtEnterEtlPaymentVerifycodeView

        txtCustomerNameForPayment.setVisibility(View.GONE);
        txtPendingAmount.setVisibility(View.GONE);
        txtEnterPaymentAmount.setVisibility(View.GONE);
        txtEnterEtlPaymentVerifycode.setVisibility(View.GONE);

        txtCustomerNameForPaymentView.setVisibility(View.GONE);
        txtPendingAmountView.setVisibility(View.GONE);
        txtEnterPaymentAmountView.setVisibility(View.GONE);
        txtEnterEtlPaymentVerifycodeView.setVisibility(View.GONE);


    }

    private void Other() {
        // finish();
        // startActivity(getIntent());
        imgQrCodeTopup.setVisibility(View.VISIBLE);
        radioTopupToOwner.setChecked(false);
        radioTopupToOther.setChecked(true);
        StatusToselect = "2";
        txt_owner_number.setEnabled(false);
        txt_other_number.setEnabled(true);
        txt_owner.setTextColor(Color.parseColor("#858585"));
        txt_other.setTextColor(Color.parseColor("#0064ff"));


        txt_owner_number.setBackgroundResource(R.drawable.text_border_stype_for_enable);
        txt_other_number.setBackgroundResource(R.drawable.text_border_stype);
        txt_other_number.setTextColor(Color.parseColor("#000000"));
        txt_owner_number.setTextColor(Color.parseColor("#858585"));
    }

    public void onClickClose(View v) {
        if (ViewStubCode.equals("Home")) {
            Logout_app();
        } else {
            /*if  (ViewStubCode.equals("EtlPayment")){
            LoadViewStub("EtlPaymentList");
        }else{
             */
            LoadViewStub("Home");
        }

        //  finish();


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //Ask the user if they want to quit

            if (ViewStubCode.equals("Home")) {
                Logout_app();
            } else {
            /*if  (ViewStubCode.equals("EtlPayment")){
            LoadViewStub("EtlPaymentList");
        }else{
             */
                LoadViewStub("Home");
            }

            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }


    }

    public void Logout_app() {
        new AlertDialog.Builder(this)
                //  .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.app_name)
                .setMessage(R.string.str_confirm_to_exit)
                .setPositiveButton(R.string.str_yes_to_exit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Stop the activity
                        SharedPreferences setting_logout_session = getSharedPreferences(GlabaleParameter.PREFS_SESSION, MODE_PRIVATE);
                        SharedPreferences.Editor edt_session = setting_logout_session.edit();
                        edt_session.putString(GlabaleParameter.PREFS_SESSION, "0");
                        edt_session.commit();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                        //Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        //startActivity(intent);
                        finish();
                        System.exit(0);
                    }
                })
                .setNegativeButton(R.string.str_no_to_exit, null)
                .show();
    }

    private void LoadBalance(String Str) {
        String[] arry = Str.split("\\|");
        StrVerifyType = arry[4];

       // dialog(Str, 2);  // 1:Success, 2:Fail, 3:Error
     txtAmount.setText(getString(R.string.str_balance)+": "+new DecimalFormat("#,###,###").format(Integer.parseInt(arry[2] + ""))+" "+getString(R.string.str_kip));

        SharedPreferences.Editor editor1 = getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, Context.MODE_PRIVATE).edit();
        editor1.putString(GlabaleParameter.PREFS_ETL_VERIFY_TYPE, "" + StrVerifyType);
        editor1.apply();
        //  Log.e("StrVerifyTypeaa22:", Str);
        StrStepOPT = "1";
        if (StrVerifyType.equals("1")) { // OTP
            LoadVerifyEnable("0");
        } else if (StrVerifyType.equals("2")) { // Password
            LoadVerifyEnable("1");
        }
        LoadVerifyTextHint();

    }

    public void refresh() {
        final String strGetLocation = GetLocation.get(WalletActivity.this);
        final Activity act = WalletActivity.this;
        final ProgressDialog progress_sub = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        progress_sub.setMessage(getString(R.string.str_pleasewait_inprocess));
        progress_sub.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress_sub.show();
        String url = getString(R.string.str_url_https) + "WalletQueryBlance.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response refresh:", response);
                        try {
                            JSONObject jObject = new JSONObject(response);
                            if (jObject.getString("status").equals("OK")) {
                              //  Log.e("Decrpt23:", jObject.getString("result"));
                                CryptoHelper cryptoHelper = new CryptoHelper();
                                String result = "";
                                try {
                                    result = cryptoHelper.decrypt(jObject.getString("result"));
                                } catch (Exception e) {
                                }
                                String[] arry = result.split("\\|");
                                Log.e("Decrpt:", result);
                                if (arry[0].equals("405000000")) {
                                    StrTransferTy = 0;
                                    if (strMsisdn.equals(arry[1])) {
                                        LoadBalance(result);

                                    }
                                } else if (arry[0].equals("300")) {
                                    txtNotRegistered.setVisibility(View.VISIBLE);


                                    //txtbalance.setText(R.string.str_kip+"HELLo");
                                    // }
                                    LoadViewStub("Home");
                                } else {
                                    dialog(result, 2);  // 1:Success, 2:Fail, 3:Error
                                    //dialog(getString(R.string.str_error_operation_fail93),2);  // 1:Success, 2:Fail, 3:Error
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
                        Toast.makeText(getApplication(), getString(R.string.str_can_not_connect_to_server) + "==" + error, Toast.LENGTH_LONG).show();
                        Log.e("error:", "" + error);
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

                CryptoHelper cryptoHelper = new CryptoHelper();
                String encryptString = "";
                try {
                    encryptString = cryptoHelper.encrypt(strBasic_info);
                } catch (Exception e) {
                }
                params.put("publickey", "CheckActive");
                params.put("Active_values", encryptString);
                Log.e("Active_valuesQ:", "" + encryptString);
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

    private void Setnumber(String msisdnStr) {
        Date dt = new Date();
        CharSequence sdd = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm", dt.getTime());
        String dd = sdd.toString();
        int limit = 100;

        SharedPreferences pref = getApplicationContext().getSharedPreferences("ETL_TOPUP_HISTORY_MSDN", MODE_PRIVATE);
        String s = pref.getString("ETL_TOPUP_MSDN", "");


        if (s.length() > 5) {
            String aNumss[] = s.split(",");
            for (String c : aNumss) {
                String mNd = c;
                String[] arrmNd = mNd.split("~");
                sss = arrmNd[0];
                if (msisdnStr.equals(sss)) {
                    String cc = arrmNd[1];
                    s = s.replace(sss + "~" + cc, "");
                }
            }
        }


        //  s = s.replace(msisdnStr,"");
        s = s.replace(",,", ",");
        if (msisdnStr.length() > 8) {
            s = msisdnStr + "~" + dd + "," + s;
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
        editorfk.putString("ETL_TOPUP_MSDN", s);
        editorfk.commit();
        String[] number = s.split(",");
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


    public void onClickTransferConfirm(View v) {
      //  Log.e("Transfer", ViewStubCode);
        if (ViewStubCode.equals("Transfer") || (ViewStubCode.equals("QrPay"))) {
            String strMobileNumber = txtDestNum.getText().toString();

            //if ( strMobileNumber.length()<8) {txtDestNum.requestFocus();txtDestNum.setError(getString(R.string.str_phone_number_is_invalide));return;}
            if (strMobileNumber.trim().startsWith("202") || strMobileNumber.trim().startsWith("302")) {
            } else {
                txtDestNum.requestFocus();
                txtDestNum.setError(getString(R.string.str_phone_number_is_invalide));
                return;
            }
            if (strMobileNumber.length() < 9) {
                txtDestNum.requestFocus();
                txtDestNum.setError(getString(R.string.str_phone_number_is_invalide));
                return;
            }


            if (txtTranAmt.getText().toString().length() < 4) {
                txtTranAmt.requestFocus();
                txtTranAmt.setError(getString(R.string.str_fill_amout_number));
                return;
            }
            if (txtDescrip.getText().toString().equals("")) {
                txtDescrip.requestFocus();
                txtDescrip.setError(getString(R.string.str_please_enter_description));
                return;
            }
            String StrTranAmt = txtTranAmt.getText().toString();
            StrTranAmt = StrTranAmt.replace(".", "");
            StrTranAmt = StrTranAmt.replace(",", "");

            String ms = "";
            if (StrVerifyType.equals("1")) {

                if (StrStepOPT.equals("1")) {
                    ms = getString(R.string.str_would_you_like_to_get_otp);
                } else if (StrStepOPT.equals("2")) {
                    final TextView txtVerifyCodeTransfer = (TextView) findViewById(R.id.txtVerifyCodeTransfer);
                    if (txtVerifyCodeTransfer.getText().toString().length() < 1) {
                        txtVerifyCodeTransfer.setError(getString(R.string.str_modify_code));
                        return;
                    }
                    ms = getString(R.string.str_confirm_to_transfer_from) + " " + strMsisdn + " " + getString(R.string.str_confirm_to_transfer_to) + " " + txtDestNum.getText().toString() + " " + getString(R.string.str_confirm_to_tran_amt) + " " + txtTranAmt.getText().toString() + "?";
                }

            } else if (StrVerifyType.equals("2")) {
                final TextView txtVerifyCodeTransfer = (TextView) findViewById(R.id.txtVerifyCodeTransfer);
                if (txtVerifyCodeTransfer.getText().toString().length() < 1) {
                    txtVerifyCodeTransfer.setError(getString(R.string.str_modify_code));
                    return;
                }

                ms = getString(R.string.str_confirm_to_transfer_from) + " " + strMsisdn + " " + getString(R.string.str_confirm_to_transfer_to) + " " + txtDestNum.getText().toString() + " " + getString(R.string.str_confirm_to_tran_amt) + " " + txtTranAmt.getText().toString() + "?";
            }
            txtTranAmt.setText(new DecimalFormat("#,###,###").format(Integer.parseInt(StrTranAmt + "")));
            new AlertDialog.Builder(this)
//                .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.app_name)
                    .setMessage(ms)
                    .setPositiveButton(R.string.str_yes_to_exit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String StrTranAmt = txtTranAmt.getText().toString();
                            StrTranAmt = StrTranAmt.replace(".", "");
                            StrTranAmt = StrTranAmt.replace(",", "");
                            Transfer(txtDestNum.getText().toString(), StrTranAmt);
                        }
                    })
                    .setNegativeButton(R.string.str_no_to_exit, null)
                    .show();
        }
    }
    public void onClickQrPay(View v) {
        //  Log.e("Transfer", ViewStubCode);
        if ((ViewStubCode.equals("QrPay"))) {
            String strMobileNumber = txtMerchantNum.getText().toString();
            //if ( strMobileNumber.length()<8) {txtDestNum.requestFocus();txtDestNum.setError(getString(R.string.str_phone_number_is_invalide));return;}
            if (strMobileNumber.trim().startsWith("202") || strMobileNumber.trim().startsWith("302")) {
            } else {
                txtMerchantNum.requestFocus();
                txtMerchantNum.setError(getString(R.string.str_phone_number_is_invalide));
                return;
            }
            if (strMobileNumber.length() < 9) {
                txtMerchantNum.requestFocus();
                txtMerchantNum.setError(getString(R.string.str_phone_number_is_invalide));
                return;
            }
            if (txttxtMerchantAmt.getText().toString().length() < 4) {
                txttxtMerchantAmt.requestFocus();
                txttxtMerchantAmt.setError(getString(R.string.str_fill_amout_number));
                return;
            }
            if (txtMerchantDescrip.getText().toString().equals("")) {
                txtMerchantDescrip.requestFocus();
                txtMerchantDescrip.setError(getString(R.string.str_please_enter_description));
                return;
            }
            String StrTranAmt = txttxtMerchantAmt.getText().toString();
            StrTranAmt = StrTranAmt.replace(".", "");
            StrTranAmt = StrTranAmt.replace(",", "");

            String ms = "";
            if (StrVerifyType.equals("1")) {
                if (StrStepOPT.equals("1")) {
                    ms = getString(R.string.str_would_you_like_to_get_otp);
                } else if (StrStepOPT.equals("2")) {
                    final TextView txtVerifyCodeTransfer = (TextView) findViewById(R.id.txtVerifyCodeQrPay);
                    if (txtVerifyCodeTransfer.getText().toString().length() < 1) {
                        txtVerifyCodeTransfer.setError(getString(R.string.str_modify_code));
                        return;
                    }
                    ms = getString(R.string.str_confirm_to_pay_from) + " " + strMsisdn + " " + getString(R.string.str_confirm_to_transfer_to) + " " + txtMerchantNum.getText().toString() + " " + getString(R.string.str_confirm_to_tran_amt) + " " + txttxtMerchantAmt.getText().toString() + "?";
                }
            } else if (StrVerifyType.equals("2")) {
                final TextView txtVerifyCodeTransfer = (TextView) findViewById(R.id.txtVerifyCodeQrPay);
                if (txtVerifyCodeTransfer.getText().toString().length() < 1) {
                    txtVerifyCodeTransfer.setError(getString(R.string.str_modify_code));
                    return;
                }
                ms = getString(R.string.str_confirm_to_pay_from) + " " + strMsisdn + " " + getString(R.string.str_confirm_to_transfer_to) + " " + txtMerchantNum.getText().toString() + " " + getString(R.string.str_confirm_to_tran_amt) + " " + txttxtMerchantAmt.getText().toString() + "?";

            }
            txttxtMerchantAmt.setText(new DecimalFormat("#,###,###").format(Integer.parseInt(StrTranAmt + "")));
            new AlertDialog.Builder(this)
//                .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.app_name)
                    .setMessage(ms)
                    .setPositiveButton(R.string.str_yes_to_exit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String StrTranAmt = txttxtMerchantAmt.getText().toString();
                            StrTranAmt = StrTranAmt.replace(".", "");
                            StrTranAmt = StrTranAmt.replace(",", "");
                            TransQrPay(txtMerchantNum.getText().toString(), StrTranAmt);
                        }
                    })
                    .setNegativeButton(R.string.str_no_to_exit, null)
                    .show();
        }
    }


    private void Transfer(final String txtDestNum, final String txtTranAmt) {
        final String strGetLocation = GetLocation.get(WalletActivity.this);
        final Activity act = WalletActivity.this;
        final ProgressDialog progress_sub = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        progress_sub.setMessage(getString(R.string.str_pleasewait_inprocess));
        progress_sub.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress_sub.show();
        String url = getString(R.string.str_url_https) + "WalletTransfer.php";

        //   Log.e("url_info:", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                                if (arry[0].equals("405000000")) {
                                    if (strMsisdn.equals(arry[1])) {
                                        Setnumber(txtDestNum);
                                        if (StrVerifyType.equals("1")) { // OTP
                                            if (StrStepOPT.equals("1")) {



                                                TextView  txtDestNum = (TextView) findViewById(R.id.txtDestNum);
                                                TextView  txtTranAmt = (TextView) findViewById(R.id.txtTranAmt);

                                                txtDestNum.setEnabled(false);
                                                txtTranAmt.setEnabled(false);
                                                txtDescrip.setEnabled(false);


                                                StrStepOPT = "2";
                                                LoadVerifyEnable("1");
                                                btn_submit_tran.setText(getString(R.string.str_submit));
                                            } else if (StrStepOPT.equals("2")) {
                                                StrStepOPT = "1";
                                                dialog(getString(R.string.title_transfer_successful) + new DecimalFormat("#,###,###").format(Integer.parseInt(arry[2] + "")) + " " + getResources().getString(R.string.str_kip), 1);  // 1:Success, 2:Fail, 3:Error

                                                ClearTextEditTransfer();
                                                StrTransferTy = 0;
                                                refresh();
                                            }
                                        } else if (StrVerifyType.equals("2")) { // Password
                                            StrStepOPT = "1";

                                            dialog(getString(R.string.title_transfer_successful) + new DecimalFormat("#,###,###").format(Integer.parseInt(arry[2] + "")) + " " + getResources().getString(R.string.str_kip), 1);  // 1:Success, 2:Fail, 3:Error

                                            ClearTextEditTransfer();
                                            StrTransferTy = 0;
                                            LoadVerifyEnable("1");
                                            //topup_success
                                            refresh();
                                        }
                                        LoadVerifyTextHint();
                                    }
                                } else {

                                    if (arry[0].length() > 4) {
                                        if (!arry[0].substring(0, 5).equals("Error")) {
                                            dialog(arry[0], 2);  // 1:Success, 2:Fail, 3:Error
                                        } else {
                                            dialog(getString(R.string.str_error_operation_fail93), 3);  // 1:Success, 2:Fail, 3:Error
                                        }
                                    } else {
                                        dialog(getString(R.string.str_error_operation_fail93), 3);  // 1:Success, 2:Fail, 3:Error
                                    }
                                    StrTransferTy = StrTransferTy + 1;
                                    if (StrTransferTy > 5) {
                                        ClearTextEditTransfer();
                                        StrStepOPT = "1";
                                        refresh();
                                    }
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
                if (StrVerifyType.equals("1")) {
                    if (StrStepOPT.equals("1")) {
                        params.put("publickey", "SentSMS");
                        strBasic_info += "|" + txtDestNum + "|" + txtTranAmt;
                    } else if (StrStepOPT.equals("2")) {
                        params.put("publickey", "Transfer");
                        final TextView txtVerifyCodeTransfer = (TextView) findViewById(R.id.txtVerifyCodeTransfer);
                        strBasic_info += "|" + txtDestNum + "|" + txtTranAmt + "|" + txtVerifyCodeTransfer.getText().toString() + "|" + txtDescrip.getText().toString();
                    }
                } else if (StrVerifyType.equals("2")) {
                    params.put("publickey", "Transfer");
                    final TextView txtVerifyCodeTransfer = (TextView) findViewById(R.id.txtVerifyCodeTransfer);
                    strBasic_info += "|" + txtDestNum + "|" + txtTranAmt + "|" + txtVerifyCodeTransfer.getText().toString() + "|" + txtDescrip.getText().toString();
                }
                CryptoHelper cryptoHelper = new CryptoHelper();
                String encryptString = "";
                try {
                    encryptString = cryptoHelper.encrypt(strBasic_info);
                } catch (Exception e) {
                }

                params.put("Active_values", encryptString);
                // Log.e("Active_valuesTransfer:",  encryptString);
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
    private void TransQrPay(final String txtDestNum, final String txtTranAmt) {
        final String strGetLocation = GetLocation.get(WalletActivity.this);
        final Activity act = WalletActivity.this;
        final ProgressDialog progress_sub = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        progress_sub.setMessage(getString(R.string.str_pleasewait_inprocess));
        progress_sub.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress_sub.show();
        String url = getString(R.string.str_url_https) + "WalletQrPay.php";

        //   Log.e("url_info:", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                                if (arry[0].equals("405000000")) {
                                    if (strMsisdn.equals(arry[1])) {
                                        Setnumber(txtDestNum);
                                        if (StrVerifyType.equals("1")) { // OTP
                                            if (StrStepOPT.equals("1")) {

                                                txtMerchantNum.setEnabled(false);
                                                txttxtMerchantAmt.setEnabled(false);
                                                txtMerchantDescrip.setEnabled(false);


                                                StrStepOPT = "2";

                                                LoadVerifyEnable("1");
                                                btn_submit_QrPay.setText(getString(R.string.str_submit));
                                            } else if (StrStepOPT.equals("2")) {
                                                StrStepOPT = "1";
                                                dialog(getString(R.string.title_pay_successful) + new DecimalFormat("#,###,###").format(Integer.parseInt(arry[2] + "")) + " " + getResources().getString(R.string.str_kip), 1);  // 1:Success, 2:Fail, 3:Error

                                                ClearTextEditTransfer();
                                                StrTransferTy = 0;
                                                refresh();
                                            }
                                        } else if (StrVerifyType.equals("2")) { // Password
                                            StrStepOPT = "1";
                                            dialog(getString(R.string.title_pay_successful) + new DecimalFormat("#,###,###").format(Integer.parseInt(arry[2] + "")) + " " + getResources().getString(R.string.str_kip), 1);  // 1:Success, 2:Fail, 3:Error

                                            ClearTextEditQrPay();
                                            StrTransferTy = 0;
                                            LoadVerifyEnable("1");
                                            //topup_success
                                            refresh();
                                        }
                                        LoadVerifyTextHint();
                                    }
                                } else {

                                    if (arry[0].length() > 4) {
                                        if (!arry[0].substring(0, 5).equals("Error")) {
                                            dialog(arry[0], 2);  // 1:Success, 2:Fail, 3:Error
                                        } else {
                                            dialog(getString(R.string.str_error_operation_fail93), 3);  // 1:Success, 2:Fail, 3:Error
                                        }
                                    } else {
                                        dialog(getString(R.string.str_error_operation_fail93), 3);  // 1:Success, 2:Fail, 3:Error
                                    }
                                    StrTransferTy = StrTransferTy + 1;
                                    if (StrTransferTy > 5) {
                                        ClearTextEditTransfer();
                                        StrStepOPT = "1";
                                        refresh();
                                    }
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
                if (StrVerifyType.equals("1")) {
                    if (StrStepOPT.equals("1")) {
                        params.put("publickey", "SentSMS");
                        strBasic_info += "|" + txtDestNum + "|" + txtTranAmt;
                    } else if (StrStepOPT.equals("2")) {
                        params.put("publickey", "Transfer");
                        final TextView txtVerifyCodeTransfer = (TextView) findViewById(R.id.txtVerifyCodeTransfer);
                        strBasic_info += "|" + txtDestNum + "|" + txtTranAmt + "|" + txtVerifyCodeQrPay.getText().toString() + "|" + txtMerchantDescrip.getText().toString()+"|"+StrDestinationType;
                    }
                } else if (StrVerifyType.equals("2")) {
                    params.put("publickey", "Transfer");
                    final TextView txtVerifyCodeTransfer = (TextView) findViewById(R.id.txtVerifyCodeTransfer);
                    strBasic_info += "|" + txtDestNum + "|" + txtTranAmt + "|" + txtVerifyCodeQrPay.getText().toString() + "|" + txtMerchantDescrip.getText().toString()+"|"+StrDestinationType;
                }
                CryptoHelper cryptoHelper = new CryptoHelper();
                String encryptString = "";
                try {
                    encryptString = cryptoHelper.encrypt(strBasic_info);
                } catch (Exception e) {
                }

                params.put("Active_values", encryptString);
               Log.e("Active_valuesTransfer:",  strBasic_info);
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
    private void EtlPayment(final String StrDestNum) {
        final String strGetLocation = GetLocation.get(WalletActivity.this);
        final Activity act = WalletActivity.this;
        final ProgressDialog progress_sub = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        progress_sub.setMessage(getString(R.string.str_pleasewait_inprocess));
        progress_sub.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress_sub.show();
        String url = getString(R.string.str_url_https) + "WalletETLPayment" + StrEtlPaymentID + ".php";
        // Log.e("url_info:", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log.e("response11:", response);
                        try {
                            JSONObject jObject = new JSONObject(response);
                            if (jObject.getString("status").equals("OK")) {
                                CryptoHelper cryptoHelper = new CryptoHelper();
                                String result = "";
                                try {
                                    result = cryptoHelper.decrypt(jObject.getString("result"));
                                } catch (Exception e) {
                                }
                                //  Log.e("result:", result);
                                String[] arry = result.split("\\|");
                                if (arry[0].equals("405000000")) {
                                    // txtCustomerName.setText("sdsdfd");
                                    if (strMsisdn.equals(arry[1])) {
                                        txtCustomerID.setEnabled(false);
                                        txtCustomerNameForPayment.setEnabled(true);
                                        txtPendingAmount.setEnabled(true);
                                        txtEnterPaymentAmount.setEnabled(false);


                                        txtCustomerNameForPayment.setVisibility(View.VISIBLE);
                                        txtPendingAmount.setVisibility(View.VISIBLE);
                                        txtEnterPaymentAmount.setVisibility(View.VISIBLE);


                                        txtCustomerNameForPaymentView.setVisibility(View.VISIBLE);
                                        txtPendingAmountView.setVisibility(View.VISIBLE);
                                        txtEnterPaymentAmountView.setVisibility(View.VISIBLE);


                                        txtCustomerNameForPayment.setBackgroundResource(R.drawable.text_border_stype_for_enable);
                                        txtPendingAmount.setBackgroundResource(R.drawable.text_border_stype_for_enable);
                                        txtEnterPaymentAmount.setBackgroundResource(R.drawable.text_border_stype);
                                        btnEtlPaymentQuery.setVisibility(View.GONE);
                                        btnEtlPaymentSubmit.setVisibility(View.VISIBLE);
                                        // btnEtlPaymentSubmit.setEnabled(true);


                                        String advance_or_debit = "";
                                        float Amount_advance_or_debit = Integer.parseInt(arry[4] + "");
                                        if ((Integer.parseInt(arry[4])) > 0) {
                                            advance_or_debit = getResources().getString(R.string.str_advance_balance);
                                        }
                                        if ((Integer.parseInt(arry[4])) < 0) {
                                            advance_or_debit = getResources().getString(R.string.str_debit_adsl) + ":";
                                            Amount_advance_or_debit = Amount_advance_or_debit * -1;

                                        }


                                        txtPendingAmount.setText(advance_or_debit + " " + new DecimalFormat("#,###,###").format(Amount_advance_or_debit) + " " + getResources().getString(R.string.str_kip));

                                        if (StrStepPayment.equals("1")) {
                                            //  String str  = Base64.decode("");
                                            txtCustomerNameForPayment.setText(arry[5]);
                                            //Log.e("CustomerName:", arry[5]);
                                            //txtCustomerName.setText(arry[5]);
                                            StrStepPayment = "2";
                                            if (StrVerifyType.equals("1")) { // 1	OTP Code
                                                txtEnterPaymentAmount.setEnabled(true);
                                                txtEnterEtlPaymentVerifycode.setEnabled(false);
                                                txtEnterEtlPaymentVerifycode.setHint(R.string.str_modify_code);
                                                Button btnEtlPaymentSubmit = (Button) findViewById(R.id.btnEtlPaymentSubmit);
                                                btnEtlPaymentSubmit.setText(R.string.str_get_otp);
                                            } else if (StrVerifyType.equals("2")) { // 1	Password


                                                txtEnterEtlPaymentVerifycodeView.setText(R.string.str_enter_password);
                                                txtEnterEtlPaymentVerifycodeView.setVisibility(View.VISIBLE);
                                                txtEnterEtlPaymentVerifycode.setVisibility(View.VISIBLE);


                                                txtEnterPaymentAmount.setEnabled(true);
                                                txtEnterEtlPaymentVerifycode.setEnabled(true);
                                                txtEnterEtlPaymentVerifycode.setBackgroundResource(R.drawable.text_border_stype);
                                                txtEnterEtlPaymentVerifycode.setHint(R.string.pass_hint);
                                                Button btnEtlPaymentSubmit = (Button) findViewById(R.id.btnEtlPaymentSubmit);
                                                btnEtlPaymentSubmit.setText(R.string.str_pay);
                                            }
                                            txtEnterPaymentAmount.requestFocus();

                                        } else if (StrStepPayment.equals("2")) { //  Password


                                            txtEnterEtlPaymentVerifycode.setVisibility(View.VISIBLE);
                                            txtEnterEtlPaymentVerifycodeView.setVisibility(View.VISIBLE);
                                            // txtPendingAmount.setText(advance_or_debit + " "+ new DecimalFormat("#,###,###").format(Integer.parseInt(arry[4] + "")) + " " + getResources().getString(R.string.str_kip));
                                            txtEnterPaymentAmount.setEnabled(false);
                                            txtEnterEtlPaymentVerifycode.setEnabled(true);
                                            txtEnterEtlPaymentVerifycode.setBackgroundResource(R.drawable.text_border_stype);
                                            txtEnterEtlPaymentVerifycodeView.setHint(R.string.str_enter_opt);

                                            Button btnEtlPaymentSubmit = (Button) findViewById(R.id.btnEtlPaymentSubmit);
                                            btnEtlPaymentSubmit.setText(R.string.str_pay);
                                            if (StrVerifyType.equals("1")) {
                                                StrStepOPT = "2";
                                            } else if (StrVerifyType.equals("2")) {
                                                //   StrStepOPT
                                            }
                                        }

                                        // dialog(getString(R.string.str_successful)+arry[3],1);  // 1:Success, 2:Fail, 3:Error
                                        if (arry[3].equals("PayAmount")) {
                                            dialog(getString(R.string.str_operation_success), 1);  // 1:Success, 2:Fail, 3:Error

                                            SetCustomerHistory(txtCustomerID.getText().toString(), StrEtlPaymentID);
                                            //   txtbalance.setText(new DecimalFormat("#,###,###").format(Integer.parseInt(arry[4] + "")) + " " + getResources().getString(R.string.str_kip));
                                            //   txtSunBalance.setText(new DecimalFormat("#,###,###").format(Integer.parseInt(arry[3]+""))+" " +getResources().getString(R.string.str_kip));
                                            LoadViewStub(ViewStubCode);
                                            ClearTextEtlPayment();
                                            refresh();
                                        }
                                        LoadVerifyTextHint();
                                    }
                                } else {
                                    dialog(arry[2], 2);  // 1:Success, 2:Fail, 3:Error

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
                if (StrStepPayment.equals("1")) {
                    strBasic_info += "|" + StrDestNum + "|";
                    StrPublickey = "LoadInfo";
                } else if (StrStepPayment.equals("2")) {
                    if (StrVerifyType.equals("1")) {
                        if (StrStepOPT.equals("1")) {
                            StrPublickey = "SentSMS";
                            String StrAmount = txtEnterPaymentAmount.getText().toString();
                            StrAmount = StrAmount.replace(".", "");
                            StrAmount = StrAmount.replace(",", "");
                            strBasic_info += "|" + StrDestNum + "|" + StrAmount;
                        } else if (StrStepOPT.equals("2")) {
                            StrPublickey = "PayAmount";
                            String StrAmount = txtEnterPaymentAmount.getText().toString();
                            String strEtlPaymentVerifycode = txtEnterEtlPaymentVerifycode.getText().toString();
                            StrAmount = StrAmount.replace(".", "");
                            StrAmount = StrAmount.replace(",", "");
                            strBasic_info += "|" + StrDestNum + "|" + StrAmount + "|" + strEtlPaymentVerifycode + "|OTP";
                        }
                    } else if (StrVerifyType.equals("2")) {
                        StrPublickey = "PayAmount";
                        String StrAmount = txtEnterPaymentAmount.getText().toString();
                        String strEtlPaymentVerifycode = txtEnterEtlPaymentVerifycode.getText().toString();
                        StrAmount = StrAmount.replace(".", "");
                        StrAmount = StrAmount.replace(",", "");
                        Log.e("20210115PSW", "PSW");
                        strBasic_info += "|" + StrDestNum + "|" + StrAmount + "|" + strEtlPaymentVerifycode + "|PSW";
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
                //   Log.e("Active_vaADSL", encryptString);
                //  params.put("paymentType", EnDecry.en(paymentType,act));

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

    public void onClickEtlPaymentConfirm(View v) {
        // Log.e("EtlPayment20210115:",  ViewStubCode);
        // Log.e("StrStepPayment20210115:",  StrStepPayment);
        String ms = "";
        if (ViewStubCode.equals("EtlPayment")) {
            String strCustomerID = txtCustomerID.getText().toString();
            if (strCustomerID.length() < 9) {
                txtCustomerID.requestFocus();
                txtCustomerID.setError(getString(R.string.str_customer_id_is_invalide));
                return;
            }
            if (strCustomerID.equals("")) {
                txtCustomerID.requestFocus();
                txtCustomerID.setError(getString(R.string.str_customer_id));
                return;
            }

            if (StrStepPayment.equals("1")) {
                EtlPayment(txtCustomerID.getText().toString());
            } else if (StrStepPayment.equals("2")) {
                String StrTranAmt = txtTranAmt.getText().toString();
                StrTranAmt = StrTranAmt.replace(".", "");
                StrTranAmt = StrTranAmt.replace(",", "");
                // Log.e("StrStepPayment20210115V2:",  StrStepPayment);
                if (StrVerifyType.equals("1")) {
                    if (txtEnterPaymentAmount.getText().toString().length() < 5 || txtEnterPaymentAmount.equals("")) {
                        txtEnterPaymentAmount.requestFocus();
                        txtEnterPaymentAmount.setError(getString(R.string.str_fill_amout_number));
                        txtEnterPaymentAmount.requestFocus();
                        return;
                    }
                    if (StrStepOPT.equals("1")) {
                        ms = getString(R.string.str_would_you_like_to_get_otp);
                    } else if (StrStepOPT.equals("2")) {
                        //  final    TextView  txtVerifyCodeTransfer = (TextView) findViewById(R.id.txtVerifyCodeTransfer);
                        if (txtEnterEtlPaymentVerifycode.getText().toString().length() < 1) {
                            txtEnterEtlPaymentVerifycode.setError(getString(R.string.str_modify_code));
                            txtEnterEtlPaymentVerifycode.requestFocus();
                            return;
                        }
                        // ms = getString(R.string.str_confirm_to_transfer_from)  +"?";
                        ms = getString(R.string.str_confirm_to_pay) + " " + StrEtlPaymentID + " " + getString(R.string.str_for_number) + " " + txtCustomerID.getText().toString() + " " + getString(R.string.str_confirm_to_pay_deduct_balance_from) + " " + getString(R.string.str_wallet) + " " + strMsisdn + " " + getString(R.string.str_confirm_to_tran_amt) + " " + txtEnterPaymentAmount.getText().toString() + " " + getString(R.string.str_kip) + " " + getString(R.string.str_really);
                    }
                } else if (StrVerifyType.equals("2")) {
                    //   Log.e("StrStepPayment20210115V3:",  StrStepPayment);
                    //  Log.e("StepPayment20210115v3:",  StrStepPayment);
                    if (txtEnterPaymentAmount.getText().toString().length() < 5 || txtEnterPaymentAmount.equals("")) {
                        txtEnterPaymentAmount.requestFocus();
                        txtEnterPaymentAmount.setError(getString(R.string.str_fill_amout_number));
                        txtEnterPaymentAmount.requestFocus();
                        return;
                    }
                    if (txtEnterEtlPaymentVerifycode.getText().toString().length() < 1) {
                        txtEnterEtlPaymentVerifycode.setError(getString(R.string.str_please_enter_password_to_confirm));
                        txtEnterEtlPaymentVerifycode.requestFocus();
                        return;
                    }
                    //  ms = getString(R.string.str_confirm_to_transfer_from) + " " + MobileNumber + " " + getString(R.string.str_confirm_to_transfer_to) + " " + txtDestNum.getText().toString() + " " + getString(R.string.str_confirm_to_tran_amt) + " " + txtTranAmt.getText().toString() +"?";
                    ms = getString(R.string.str_confirm_to_pay) + " " + StrEtlPaymentID + " " + getString(R.string.str_for_number) + " " + txtCustomerID.getText().toString() + " " + getString(R.string.str_confirm_to_pay_deduct_balance_from) + " " + getString(R.string.str_wallet) + " " + strMsisdn + " " + getString(R.string.str_confirm_to_tran_amt) + " " + txtEnterPaymentAmount.getText().toString() + " " + getString(R.string.str_kip) + " " + getString(R.string.str_really);

                }
                //  txtTranAmt.setText(new DecimalFormat("#,###,###").format(Integer.parseInt(StrTranAmt+"")));
                final AlertDialog alert = new AlertDialog.Builder(this).create();
                alert.setTitle(R.string.str_confirm);
                alert.setMessage(ms);
                alert.setIcon(R.drawable.ic_question);
                alert.setButton(getResources().getString(R.string.str_yes_to_exit), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String StrTranAmt = txtTranAmt.getText().toString();
                        EtlPayment(txtCustomerID.getText().toString());
                    }
                });
                alert.setButton2(getResources().getString(R.string.str_no_to_exit), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alert.show();
            }
        }
    }

    private void LoadEtlPayment() {
        MyArrList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;

// Internet payment
        //  Log.e("Active_valuesaa:",  Str);

        String Str = "FTTH;FTTH;FTTH|";
        Str += "ADSL;ADSL;ADSL|";
        Str += "HIL;HIL;HIL";

        String[] ArrA = Str.split("~");
        for (String StrA : ArrA) {
            String[] ArrB = StrA.split(";");
            map = new HashMap<String, String>();
            map.put("StrEtlPaymentID", "" + ArrB[0]);
            map.put("txtEtlPaymentText", "" + ArrB[1]);
            map.put("StrEtlPaymentTitle", "" + ArrB[2]);
            MyArrList.add(map);
        }
        final ListView lstView1 = (ListView) findViewById(R.id.listView95);
        lstView1.setAdapter(new WalletActivity.ImageAdapter(this));
    }

    public class ImageAdapter extends BaseAdapter {
        private Context context;

        public ImageAdapter(Context c) {
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
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.etl_payment_rows, null);
            }
            TextView txtEtlPaymentName = (TextView) convertView.findViewById(R.id.txtEtlPaymentText);
            txtEtlPaymentName.setText(MyArrList.get(position).get("txtEtlPaymentText"));
            txtEtlPaymentName.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    StrEtlPaymentID = MyArrList.get(position).get("StrEtlPaymentID");
                    StrEtlPaymentText = MyArrList.get(position).get("txtEtlPaymentText");
                    StrEtlPaymentTitle = MyArrList.get(position).get("StrEtlPaymentTitle");
                    ViewStubCode = "EtlPayment";
                    LoadViewStub(ViewStubCode);
                }
            });
            return convertView;
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

    private void LoadVerifyEnable(String StrEnable) {

      //  Log.e("StrEnable", StrEnable);


        if (StrEnable.equals("0")) {  // Hidden
            //  Log.e("StrVisibility",  StrVerifyType+" is OTP");
            txtVerifyCodeTransfer.setVisibility(View.GONE);
            txtVerifyCodeTransfer.setVisibility(View.GONE);
            tv_enter_opt_password.setVisibility(View.GONE);
            txtstrVerifyCodeTopup.setVisibility(View.GONE);
            txtOTPCode.setVisibility(View.GONE);
                    txtVerifyCodeQrPay.setVisibility(View.GONE);





        } else if (StrEnable.equals("1")) {  // Enable or show
            txtVerifyCodeTransfer.setVisibility(View.VISIBLE);
            tv_enter_opt_password.setVisibility(View.VISIBLE);
            txtstrVerifyCodeTopup.setVisibility(View.VISIBLE);
            // txtVerifyCodeTopup.setVisibility(View.VISIBLE);
            txtOTPCode.setVisibility(View.VISIBLE);
            txtVerifyCodeQrPay.setVisibility(View.VISIBLE);




        }
    }

    private void ShowOrHidePassword() {


        Log.e("StrShowOrHidePassword= ", StrShowOrHidePassword);

//        1	OTP Code
//        2	Password
//        3	Finger print

        if (StrShowOrHidePassword.equals("0")) {  //2 Password (hidden password when clicking the eyes icon)
            txtVerifyCodeTransfer.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_pw_hide), null);
            txtVerifyCodeTransfer.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_pw_hide), null);
            txtEnterEtlPaymentVerifycode.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_pw_hide), null);
            txtVerifyCodeTopup.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
           // txtVerifyCodeTransfer.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
           // txtEnterEtlPaymentVerifycode.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            StrShowOrHidePassword = "1";


        } else if (StrShowOrHidePassword.equals("1")) { //  2	Password	OTP Code (Shows password when clicking the eyes icon)
            txtVerifyCodeTopup.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_pw_show), null);
            txtVerifyCodeTransfer.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_pw_show), null);
            txtEnterEtlPaymentVerifycode.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_pw_show), null);
            txtVerifyCodeTopup.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
           // txtVerifyCodeTransfer.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
           // txtEnterEtlPaymentVerifycode.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
            StrShowOrHidePassword = "0";  // ?

        } else if (StrShowOrHidePassword.equals("2")) {  //   1	OTP Code (hidden icon eyes)
            txtVerifyCodeTopup.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_blank), null);
            txtVerifyCodeTransfer.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_blank), null);
            txtEnterEtlPaymentVerifycode.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_blank), null);
            txtVerifyCodeTopup.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
         //   txtVerifyCodeTransfer.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
         //   txtEnterEtlPaymentVerifycode.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

         //   txtVerifyCodeTransfer.setHint(R.string.str_enter_password);

            StrShowOrHidePassword = "2";  // ?
        }
    }

    public void onClickRefresh(View v) throws Exception {
        refresh();
        // Log.e("Active_valuesaa:",  "EasyAES.encryptString(Standard_info)");
    }

    private void Topup(final String StrDestNum, final String str) {
        final String strGetLocation = GetLocation.get(WalletActivity.this);
        final String Str2 = str.replace(".", "");
        final String StrAmountTopup = Str2.replace(".", "");

        final Activity act = WalletActivity.this;
        final ProgressDialog progress_sub = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        progress_sub.setMessage(getString(R.string.str_pleasewait_inprocess));
        progress_sub.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress_sub.show();
        String url = getString(R.string.str_url_https) + "WalletTopup.php";
        //   Log.e("url_info:", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObject = new JSONObject(response);
                            if (jObject.getString("status").equals("OK")) {
                                CryptoHelper cryptoHelper = new CryptoHelper();
                                String result = "";
                                try {
                                    result = cryptoHelper.decrypt(jObject.getString("result"));
                                } catch (Exception e) {
                                }
                                Log.e("result:", result);
                                String[] arry = result.split("\\|");
                                if (arry[0].equals("405000000")) {
                                    if (strMsisdn.equals(arry[1])) {
                                        Setnumber(StrDestNum);
                                        //  Log.e("StrVerifyType:", StrVerifyType);
                                        txtstrNumberType.setVisibility(View.VISIBLE);
                                        txtNumberType.setVisibility(View.VISIBLE);
                                        txtstrBalance.setVisibility(View.VISIBLE);
                                        radioTopupToOther.setEnabled(false);
                                        radioTopupToOwner.setEnabled(false);
                                        txt_owner.setEnabled(false);
                                        txt_other.setEnabled(false);

                                        if (StrVerifyType.equals("1")) { //  OPT
                                            if (StrStepOPT.equals("1")) {
                                                StrStepOPT = "2";
                                                if (arry[2].equals("0")) { // Prepaid
                                                    txtNumberType.setVisibility(View.VISIBLE);
                                                    txtBalance.setVisibility(View.VISIBLE);
                                                    txtNumberType.setText(getResources().getString(R.string.str_prepaid));
                                                    txtBalance.setText(new DecimalFormat("#,###,###").format(Integer.parseInt(arry[3] + "")) + " " + getResources().getString(R.string.str_kip));
                                                    btn_topup.setText(getString(R.string.str_get_otp));
                                                    txtstrAmountTopupWallet.setVisibility(View.VISIBLE);
                                                    txtAmountTopupWallet.setVisibility(View.VISIBLE);
                                                } else { // Postpaid
                                                    // ==20210309
                                                    txtstrAmountTopupWallet.setVisibility(View.VISIBLE);
                                                    txtAmountTopupWallet.setVisibility(View.VISIBLE);
                                                    //===
                                                    StrStepOPT = "2";
                                                    txtstrNumberType.setVisibility(View.VISIBLE);
                                                    txtNumberType.setVisibility(View.VISIBLE);
                                                    txtstrBalance.setVisibility(View.VISIBLE);
                                                    txtBalance.setVisibility(View.VISIBLE);
                                                    txtstrBalance.setText(getResources().getString(R.string.str_debit));
                                                    txtNumberType.setText(getResources().getString(R.string.str_postpaid));
                                                    Integer intBalance = Integer.parseInt(arry[3] + "");
                                                    btn_topup.setText(getString(R.string.str_submit));
                                                    if (intBalance < 0) {
                                                        intBalance = intBalance * -1;
                                                        txtstrBalance.setText(getResources().getString(R.string.str_advance));
                                                    }
                                                    txtBalance.setText(new DecimalFormat("#,###,###").format(intBalance) + " " + getResources().getString(R.string.str_kip));
                                                }
                                            } else if (StrStepOPT.equals("2")) {
                                                StrStepOPT = "3";

                                                txtstrVerifyCodeTopup.setVisibility(View.VISIBLE);
                                                txtVerifyCodeTopup.setVisibility(View.VISIBLE);
                                                txtstrVerifyCodeTopup.setText(R.string.str_Verifiy_code);
                                                btn_topup.setText(getString(R.string.str_submit));
                                                //  LoadVerifyEnable("1") ;
                                            } else if (StrStepOPT.equals("3")) {
                                                // StrStepOPT = "3";
                                                dialog(getString(R.string.str_topup_successful) + new DecimalFormat("#,###,###").format(Integer.parseInt(arry[2] + "")) + " " + getResources().getString(R.string.str_kip), 1);  // 1:Success, 2:Fail, 3:Error

                                                ClearTextEditTopup();
                                                refresh();
                                            }
                                        } else if (StrVerifyType.equals("2")) { // Password
                                            Log.e("result:", arry[2]);
                                            if (StrStepOPT.equals("1")) {
                                                if (arry[2].equals("0")) { // Prepaid
                                                    StrStepOPT = "2";
                                                    txtstrNumberType.setVisibility(View.VISIBLE);
                                                    txtNumberType.setVisibility(View.VISIBLE);
                                                    txtstrBalance.setVisibility(View.VISIBLE);
                                                    txtBalance.setVisibility(View.VISIBLE);
                                                    txtstrAmountTopupWallet.setVisibility(View.VISIBLE);
                                                    txtAmountTopupWallet.setVisibility(View.VISIBLE);
                                                    txtstrVerifyCodeTopup.setVisibility(View.VISIBLE);
                                                    txtVerifyCodeTopup.setVisibility(View.VISIBLE);
                                                    txtNumberType.setText(getResources().getString(R.string.str_prepaid));
                                                    txtstrBalance.setText(getResources().getString(R.string.str_balance));
                                                    txtstrVerifyCodeTopup.setText(R.string.pass_hint);
                                                    txtBalance.setText(new DecimalFormat("#,###,###").format(Integer.parseInt(arry[3] + "")) + " " + getResources().getString(R.string.str_kip));
                                                } else { // Posptaid
                                                    // ==20210309
                                                    txtstrAmountTopupWallet.setVisibility(View.VISIBLE);
                                                    txtAmountTopupWallet.setVisibility(View.VISIBLE);
                                                    //===
                                                    StrStepOPT = "2";
                                                    txtstrNumberType.setVisibility(View.VISIBLE);
                                                    txtNumberType.setVisibility(View.VISIBLE);
                                                    txtstrBalance.setVisibility(View.VISIBLE);
                                                    txtBalance.setVisibility(View.VISIBLE);
                                                    txtstrAmountTopupWallet.setVisibility(View.VISIBLE);
                                                    txtAmountTopupWallet.setVisibility(View.VISIBLE);
                                                    txtstrVerifyCodeTopup.setVisibility(View.VISIBLE);
                                                    txtVerifyCodeTopup.setVisibility(View.VISIBLE);
                                                    txtstrBalance.setText(getResources().getString(R.string.str_debit));
                                                    txtNumberType.setText(getResources().getString(R.string.str_postpaid));
                                                    txtstrVerifyCodeTopup.setText(R.string.pass_hint);
                                                    btn_topup.setText(getString(R.string.str_submit));
                                                    Integer intBalance = Integer.parseInt(arry[3] + "");
                                                    if (intBalance < 0) {
                                                        intBalance = intBalance * -1;
                                                        txtstrBalance.setText(getResources().getString(R.string.str_advance));
                                                    }
                                                    txtBalance.setText(new DecimalFormat("#,###,###").format(intBalance) + " " + getResources().getString(R.string.str_kip));
                                                    btn_topup.setText(getString(R.string.str_submit));
                                                }
                                            } else if (StrStepOPT.equals("2")) {
                                                LoadVerifyEnable("1");
                                                StrStepOPT = "2";
                                                dialog(getString(R.string.str_topup_successful) + new DecimalFormat("#,###,###").format(Integer.parseInt(arry[2] + "")) + " " + getResources().getString(R.string.str_kip), 1);  // 1:Success, 2:Fail, 3:Error

                                                ClearTextEditTopup();
                                                refresh();
                                            }
                                        }
                                        LoadVerifyTextHint();
                                    }
                                    Toast.makeText(getApplication(), "StrStepOPT: " + StrStepOPT, Toast.LENGTH_LONG).show();
                                } else {
                                    if (arry[0].length() > 4) {
                                        if (!arry[0].substring(0, 5).equals("Error")) {
                                            dialog(arry[0], 2);  // 1:Success, 2:Fail, 3:Error
                                        } else {
                                            dialog(getString(R.string.str_error_operation_fail93), 3);  // 1:Success, 2:Fail, 3:Error
                                        }
                                    } else {
                                        dialog(getString(R.string.str_error_operation_fail93), 2);  // 1:Success, 2:Fail, 3:Error
                                    }
                                    StrTransferTy = StrTransferTy + 1;
                                    if (StrTransferTy > 5) {
                                        ClearTextEditTransfer();
                                        StrStepOPT = "1";
                                        refresh();
                                    }
                                   /*if (!arry[0].substring(0,5).equals("Error")){
                                        dialog(arry[0]);
                                    }else{
                                        dialog(getString(R.string.str_error_operation_fail93));
                                    }*/
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
                if (StrVerifyType.equals("1")) {
                    if (StrStepOPT.equals("1")) {
                        params.put("publickey", "CheckActive");
                        strBasic_info += "|" + StrDestNum + "|" + StrAmountTopup;
                    } else if (StrStepOPT.equals("2")) {
                        params.put("publickey", "SentSMS");
                        strBasic_info += "|" + StrDestNum + "|" + StrAmountTopup;
                    } else if (StrStepOPT.equals("3")) {
                        params.put("publickey", "Topup");
                        final TextView txtVerifyCodeTopup = (TextView) findViewById(R.id.txtVerifyCodeTopup);
                        strBasic_info += "|" + StrDestNum + "|" + StrAmountTopup + "|" + txtVerifyCodeTopup.getText().toString();
                    }
                } else if (StrVerifyType.equals("2")) {

                    if (StrStepOPT.equals("1")) {
                        params.put("publickey", "CheckActive");
                        strBasic_info += "|" + StrDestNum + "|" + StrAmountTopup;
                    } else if (StrStepOPT.equals("2")) {
                        params.put("publickey", "Topup");
                        final TextView txtVerifyCodeTopup = (TextView) findViewById(R.id.txtVerifyCodeTopup);
                        strBasic_info += "|" + StrDestNum + "|" + StrAmountTopup + "|" + txtVerifyCodeTopup.getText().toString();
                    }
                }

                CryptoHelper cryptoHelper = new CryptoHelper();
                String encryptString = "";
                try {
                    encryptString = cryptoHelper.encrypt(strBasic_info);
                } catch (Exception e) {
                }
                params.put("Active_values", encryptString);
                // params.put("paymentType", EnDecry.en(paymentType,act));
                Log.e("Active_valuesTop:", encryptString);
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




//    private void CheckConfirmMethod_otp_or_password() {
//        Log.e("Check Method = ", "In functions CheckConfirmMethod_otp_or_password");
//        Log.e("Check Method = ", "22222222222222222222222222222");
//
//        String confirmMethod = "";
//
//        try {
//
//
//           SharedPreferences prefConfirmMethod = getApplicationContext().getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, MODE_PRIVATE);
//
//             confirmMethod = prefConfirmMethod.getString(GlabaleParameter.PREFS_ETL_VERIFY_TYPE, "");
//
//            Toast.makeText(getApplication(), "confirmMethod="+confirmMethod, Toast.LENGTH_LONG).show();
//
//            Log.e("CheCheck Method = ", "00000000000000000000000000000");
//            Log.e("confirmMethod =", confirmMethod);
//
//
//        } catch (Exception e) {
//
//            Log.e("Check Method ERROR= ", e.getMessage());
//        }
//       // Log.e("ERROR  =", "CheckConfirmMethod_otp_or_password");
//
//
//        if(confirmMethod.equals("1")) {  // OTP
//
//            txtVerifyCodeTransfer.setHint(R.string.str_enter_opt);
//            txtVerifyCodeTransfer.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
//
//        }else if(confirmMethod.equals("2")) { // Password
//
//        }else  if(confirmMethod.equals("3")) { // Finger print
//
//        }else{
//
//        }
//
//
//    }

    private void ClearTextEditTopup() {
        StrStepOPT = "1";
        txt_other_number.setText("");
        txtNumberType.setText("");
        txtBalance.setText("");
        txtAmountTopupWallet.setText("");
        txtVerifyCodeTopup.setText("");
        txtNumberType.setVisibility(View.GONE);
        txtBalance.setVisibility(View.GONE);
        txtAmountTopupWallet.setVisibility(View.GONE);
        txtstrVerifyCodeTopup.setVisibility(View.GONE);
        txtstrNumberType.setVisibility(View.GONE);
        txtstrBalance.setVisibility(View.GONE);
        txtstrAmountTopupWallet.setVisibility(View.GONE);
        txtVerifyCodeTopup.setVisibility(View.GONE);
        btn_topup.setText(getString(R.string.str_next));

        radioTopupToOther.setEnabled(true);
        radioTopupToOwner.setEnabled(true);
        txt_owner.setEnabled(true);
        txt_other.setEnabled(true);

        if (StrVerifyType.equals("1")) { // OTP
            LoadVerifyEnable("0");
        } else if (StrVerifyType.equals("2")) { // Password
            LoadVerifyEnable("1");
        }
    }

    public void onClickTopupConfirm(View v) {
        String StrAmountTopupWallet = "0";
        if (ViewStubCode.equals("Topup")) {
            if (StatusToselect.equals("1")) {
                StrDestNumber = txt_owner_number.getText().toString();
            } else if (StatusToselect.equals("2")) {
                StrDestNumber = txt_other_number.getText().toString();
                if (StrDestNumber.trim().startsWith("202") || StrDestNumber.trim().startsWith("302")) {
                } else {
                    txt_other_number.requestFocus();
                    txt_other_number.setError(getString(R.string.str_phone_number_is_invalide));
                    return;
                }
                if (StrDestNumber.length() < 9) {
                    txt_other_number.requestFocus();
                    txt_other_number.setError(getString(R.string.str_phone_number_is_invalide));
                    return;
                }
            }
            //   if (txtAmountTopupWallet.getText().toString().length()<4){ txtAmountTopupWallet.requestFocus(); txtAmountTopupWallet.setError(getString(R.string.str_fill_amout_number));return;}
            String ms = "";
            if (StrVerifyType.equals("1")) { //  OPT
                if (StrStepOPT.equals("1")) {
                    Topup(StrDestNumber, "0");
                    return;
                } else if (StrStepOPT.equals("2")) {
                    ms = getString(R.string.str_would_you_like_to_get_otp);
                } else if (StrStepOPT.equals("3")) {
                    StrAmountTopupWallet = txtAmountTopupWallet.getText().toString();
                    //  final    TextView  txtVerifyCodeTopup = (TextView) findViewById(R.id.txtVerifyCodeTopup);
                    if (txtVerifyCodeTopup.getText().toString().length() < 1) {
                        txtVerifyCodeTopup.setError(getString(R.string.str_modify_code));
                        return;
                    }
                    ms = getString(R.string.str_confirm_to_Topup) + StrDestNumber + "?";
                }
            } else if (StrVerifyType.equals("2")) { //  Password
                if (StrStepOPT.equals("1")) {
                    Topup(StrDestNumber, "0");
                    return;
                } else if (StrStepOPT.equals("2")) {
                    final TextView txtVerifyCodeTopup = (TextView) findViewById(R.id.txtVerifyCodeTopup);
                    StrAmountTopupWallet = txtAmountTopupWallet.getText().toString();
                    if (txtVerifyCodeTopup.getText().toString().length() < 1) {
                        txtVerifyCodeTopup.setError(getString(R.string.str_modify_code));
                        return;
                    }
                    ms = getString(R.string.str_confirm_to_Topup) + " " + StrDestNumber + "?";
                }
            }
            //  txtAmountTopupWallet.setText(new DecimalFormat("#,###,###").format(Integer.parseInt(StrtxtAmount+"")));
            new AlertDialog.Builder(this)
//                .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.app_name)
                    .setMessage(ms)
                    .setPositiveButton(R.string.str_yes_to_exit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String StrtxtAmount = txtAmountTopupWallet.getText().toString();
                            StrtxtAmount = StrtxtAmount.replace(".", "");
                            StrtxtAmount = StrtxtAmount.replace(",", "");
                            Topup(StrDestNumber, StrtxtAmount);
                        }
                    })
                    .setNegativeButton(R.string.str_no_to_exit, null)
                    .show();
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        try {
            if (data.getStringExtra("keyName").equals("notification")) {
                LoadItem();
            }

        } catch (Exception e) {

        }
        // Log.e("QR resultCode:",  resultCode);
        //  Log.e("QR data:",  data);
        //  Log.e("aaaaaaa:",  "777777");
        // LoadItem();

        txtCustomerNameForQrPay.setText("");

        txtDestNum.setText("");
        txt_other_number.setText("");
        txtDestNum.setText("");
        txt_other_number.setText("");
        txtCustomerID.setText("");


        SharedPreferences pref = getApplicationContext().getSharedPreferences(GlabaleParameter.PREFS_QR_CODE_GALLERY, MODE_PRIVATE);
        String strQrGallery = pref.getString(GlabaleParameter.PREFS_QR_CODE_GALLERY_NUMBER, "");

        SharedPreferences.Editor editor1 = getSharedPreferences(GlabaleParameter.PREFS_QR_CODE_GALLERY, Context.MODE_PRIVATE).edit();
        editor1.putString(GlabaleParameter.PREFS_QR_CODE_GALLERY_NUMBER, "");
        editor1.apply();
        if (strQrGallery.equals("")) {
            if (strQrType.equals("QrPay")) { // QrPay



                try {
                    String str = result.getContents();
                    Log.e("bbb1:",  str);
                    //   byte[] valueDecoded = new byte[0];
                    //  valueDecoded = android.util.Base64.decode(str.getBytes("UTF-8"), android.util.Base64.DEFAULT);
                    //   str = new String(valueDecoded, "UTF-8"); // for UTF-8 encoding
                    String[] arry = str.split("\\|");
                    if (!arry[1].equals("")) {
                        //    Log.e("bbb:",  "data");
                        LoadViewStub("QrPay");

                        txtMerchantNum.setText(arry[0]);



                        StrDestinationType = arry[1];
                        if (StrDestinationType.equals("MerchantQR")){
                            txtDestinationType.setText( getString(R.string.str_merchant));
                        }else{
                            txtDestinationType.setText( getString(R.string.str_end_user));

                        }

                        //  txtVerifyCodeQrPay.setText("33");
                        // txttxtMerchantAmt.setText("44");
                        //   txtMerchantDescrip.setText("55");
                        // txtCustomerNameForQrPay.setText("66");
                        //txtDestNum.setText(arry[0]);

                        if (!arry[2].equals("0")){
                            txttxtMerchantAmt.setText(arry[2]);
    txttxtMerchantAmt.setEnabled(false);
}else {
    txttxtMerchantAmt.setEnabled(true);
}
                        try {
                            byte[] decrypt= Base64.decode(arry[3], Base64.DEFAULT);

                            txtCustomerNameForQrPay.setText(new String(decrypt, "UTF-8"));
                            //  txtDestNum.setText(arry[0]);
                        } catch (Exception e) {
                        }


                       // txtDestNum.setText(arry[0]);
                        // txtDestNumQrPay.setText(arry[0]);
                        // txtCustomerNameForQrPay.setText(arry[1]);
                    }
                } catch (Exception e) {
                    return;
                }
            } else {

                try {
                    if (result.getContents() != null) {  //  Qr Code
                        if (result.getContents().trim().startsWith("202") || result.getContents().trim().startsWith("302") || result.getContents().length() < 9) {
                            txtDestNum.setText(result.getContents().replace(" ", ""));
                            txt_other_number.setText(result.getContents().replace(" ", ""));
                            txtDestNum.setText(result.getContents().replace(" ", ""));
                            txt_other_number.setText(result.getContents().replace(" ", ""));
                            txtCustomerID.setText(result.getContents().replace(" ", ""));
                        } else {
                            Toast.makeText(this, getString(R.string.str_qr_code_is_invalid), Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                } catch (Exception e) {
                    // return;
                }

                try {
                    if (result != null) { //  mSisdn Log
                        String txtMsdn = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                        if (txtMsdn.trim().startsWith("202") || txtMsdn.trim().startsWith("302") || txtMsdn.length() < 9) {
                            txtDestNum.setText(txtMsdn);
                            txt_other_number.setText(txtMsdn);
                            txtCustomerID.setText(txtMsdn);
                        } else {
                            Toast.makeText(this, getString(R.string.str_qr_code_is_invalid), Toast.LENGTH_LONG).show();
                            return;
                        }
                    } else {
                        super.onActivityResult(requestCode, resultCode, data);
                    }
                } catch (Exception e) {
                    // return;
                }
            }
        } else {
            if (strQrType.equals("QrPay")) { // QrPay

                try {
                    String str = strQrGallery;
                    Log.e("bbb1:",  strQrGallery);
                    String[] arry = str.split("\\|");
                    if (!arry[1].equals("")) {
                        //    Log.e("bbb:",  "data");
                        LoadViewStub("QrPay");
                        Log.e("bbb1:",  strQrGallery);

                        txtMerchantNum.setText(arry[0]);
                        StrDestinationType = arry[1];
                        if (StrDestinationType.equals("MerchantQR")){
                            txtDestinationType.setText( getString(R.string.str_merchant));
                        }else{
                            txtDestinationType.setText( getString(R.string.str_end_user));

                        }

                      //  txtVerifyCodeQrPay.setText("33");
                       // txttxtMerchantAmt.setText("44");
                     //   txtMerchantDescrip.setText("55");
                       // txtCustomerNameForQrPay.setText("66");
                        //txtDestNum.setText(arry[0]);

                        if (!arry[2].equals("0")){
                            txttxtMerchantAmt.setText(arry[2]);
                            txttxtMerchantAmt.setEnabled(false);
                        }else {
                            txttxtMerchantAmt.setEnabled(true);
                        }
                        try {
                            byte[] decrypt= Base64.decode(arry[3], Base64.DEFAULT);

                            txtCustomerNameForQrPay.setText(new String(decrypt, "UTF-8"));
                          //  txtDestNum.setText(arry[0]);
                        } catch (Exception e) {
                        }

                    }
                } catch (Exception e) {
                    return;
                }
            } else {
                // Log.e("strQrGallery:",  ""+strQrGallery);
                try {
                    //  Qr Code
                    if (strQrGallery.trim().startsWith("202") || strQrGallery.trim().startsWith("302") || result.getContents().length() < 9) {
                        txtDestNum.setText(strQrGallery.replace(" ", ""));
                        txt_other_number.setText(strQrGallery.replace(" ", ""));
                        txtDestNum.setText(strQrGallery.replace(" ", ""));
                        txt_other_number.setText(strQrGallery.replace(" ", ""));
                        txtCustomerID.setText(strQrGallery.replace(" ", ""));
                    } else {
                        Toast.makeText(this, getString(R.string.str_qr_code_is_invalid), Toast.LENGTH_LONG).show();
                        return;
                    }

                } catch (Exception e) {
                    // return;
                }

            }

        }
        // Log.e("strQrGallery:",  strQrGallery);
    }

    private void LoadViewStub(String Str) {
        // txtClose.setText("bbbbbbbbbb");
        txtClose.setBackgroundResource(R.drawable.ic_close_white);
        ViewStubCode = Str;
        final TextView txtTile = (TextView) findViewById(R.id.txtTile);
        StrStepOPT = "1";
        StrStepPayment = "1";
        subHome.setVisibility(View.GONE);
        subTopup.setVisibility(View.GONE);
        subTransfer.setVisibility(View.GONE);
        subEtlPaymentList.setVisibility(View.GONE);
        subEtlPayment.setVisibility(View.GONE);

        dashboard.setVisibility(View.GONE);
        SubQrPay.setVisibility(View.GONE);
        layoutheaderInfo.setVisibility(View.GONE);

        if (ViewStubCode.equals("Topup")) {
            subTopup.setVisibility(View.VISIBLE);
            Owner();
            ClearTextEditTopup();
            txtTile.setText(R.string.str_Recharge);
            txt_owner_number.setText(txtMobilenumber.getText().toString());
        } else if (ViewStubCode.equals("Transfer")) {
            ClearTextEditTransfer();
            txtTile.setText(R.string.str_transfer_wallet);
            subTransfer.setVisibility(View.VISIBLE);

        } else if (ViewStubCode.equals("QrPay")) {
            ClearTextEditQrPay();
            txtTile.setText(R.string.str_qr_pay);
            SubQrPay.setVisibility(View.VISIBLE);
          //  subPayToMerchant.setVisibility(View.VISIBLE);

        } else if (ViewStubCode.equals("EtlPaymentList")) {
            txtTile.setText("ETL Payment List");
            subEtlPaymentList.setVisibility(View.VISIBLE);
            LoadEtlPayment();
        } else if (ViewStubCode.equals("EtlPayment")) {
            ClearTextEtlPayment();
            txtTile.setText(StrEtlPaymentTitle);
            subEtlPayment.setVisibility(View.VISIBLE);
        } else {
            txtClose.setBackgroundResource(R.drawable.ic_logout);
            txtTile.setText(R.string.str_wallet_etl_company_limited);
            dashboard.setVisibility(View.VISIBLE);
            layoutheaderInfo.setVisibility(View.VISIBLE);

        }
    }

    public void LoadItem() {
        // str =  str+"~show_more";
        //   Toast.makeText(getApplication(), "confirmMethod="+str, Toast.LENGTH_LONG).show();
        //  Log.e("aaaaaaa:",  "777777");
        // String strsplit = "tranfer~adsl_payment~pstn_payment~phone_payment~history~my_qr~qr_pay~change_pw~setting";




        SharedPreferences pref = getApplicationContext().getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, MODE_PRIVATE);

        String strEndUserEnable = pref.getString(GlabaleParameter.PREFS_ETL_IS_END_USER_ENABLE, "");
        String strAgentEnanable = pref.getString(GlabaleParameter.PREFS_ETL_IS_AGENT_ENABLE, "");
        String strMerChantEnable = pref.getString(GlabaleParameter.PREFS_ETL_IS_MERCHANT_ENABLE, "");
        //Your RecyclerView
        mRecyclerView = (RecyclerView) this.findViewById(R.id.RecyclerVie);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        String str = "";
        //     Log.e("PREFS_ETL_END_USER_MENU_LIST= ",""+  pref.getString(GlabaleParameter.PREFS_ETL_END_USER_MENU_LIST, ""));
        //   Log.e("PREFS_ETL_AGENT_MENU_LIST= ",""+  pref.getString(GlabaleParameter.PREFS_ETL_AGENT_MENU_LIST, ""));
        //    Log.e("PREFS_ETL_MERCHANT_MENU_LIST=",""+  pref.getString(GlabaleParameter.PREFS_ETL_MERCHANT_MENU_LIST, ""));

        // Agent

        if (strEndUserEnable.equals("1")) {
            str  = pref.getString(GlabaleParameter.PREFS_ETL_END_USER_MENU_LIST, "");

        }
        // End user
        if (strAgentEnanable.equals("1")) {
            if (str.equals("")) {
                str =  pref.getString(GlabaleParameter.PREFS_ETL_AGENT_MENU_LIST, "");

            }else{
                str += "~" + pref.getString(GlabaleParameter.PREFS_ETL_AGENT_MENU_LIST, "");
            }
        }
        // Merchant
        if (strMerChantEnable.equals("1")) {
            if (str.equals("")) {
                str =  pref.getString(GlabaleParameter.PREFS_ETL_MERCHANT_MENU_LIST, "");
            }else{
                str += "~" + pref.getString(GlabaleParameter.PREFS_ETL_MERCHANT_MENU_LIST, "");
            }
        }
        String strArr[] = str.split("~");
        mAdapter = new CustomAdapter(WalletActivity.this, strArr);
        //Your RecyclerView.Adapter
        //This is the code to provide a sectioned grid
        List<SectionedGridRecyclerViewAdapter.Section> sections = new ArrayList<SectionedGridRecyclerViewAdapter.Section>();
        //==================
        //  Log.e("decryptString2 Login=",""+  arry[12]+"***"+ arry[13]+"***" +arry[14]);
        //   Log.e("decryptString2 Login=",""+  arry[15]+"***"+ arry[16]+"***" +arry[17]);
        int count = 0;
        if (strEndUserEnable.equals("1")) {
            sections.add(new SectionedGridRecyclerViewAdapter.Section(count, getResources().getString(R.string.str_end_user)));
            str  = pref.getString(GlabaleParameter.PREFS_ETL_END_USER_MENU_LIST, "");


            strArr =  str.split("~");

            count = strArr.length;
        }
        // End user
        if (strAgentEnanable.equals("1")) {
            sections.add(new SectionedGridRecyclerViewAdapter.Section(count, getResources().getString(R.string.str_agent)));
            str  = pref.getString(GlabaleParameter.PREFS_ETL_AGENT_MENU_LIST, "");
            strArr =  str.split("~");
            count += strArr.length;

        }
        // Merchant
        if (strMerChantEnable.equals("1")) {
            sections.add(new SectionedGridRecyclerViewAdapter.Section(count, getResources().getString(R.string.str_merchant)));
        }
        //  Log.e("straaa=",str);
        //Sections
        // sections.add(new SectionedGridRecyclerViewAdapter.Section(3,"Agent"));
        //  sections.add(new SectionedGridRecyclerViewAdapter.Section(5,"Merchant"));
        //Add your adapter to the sectionAdapter
        SectionedGridRecyclerViewAdapter.Section[] dummy = new SectionedGridRecyclerViewAdapter.Section[sections.size()];
        SectionedGridRecyclerViewAdapter mSectionedAdapter = new SectionedGridRecyclerViewAdapter(this,R.layout.section,R.id.section_text,mRecyclerView,mAdapter);
        mSectionedAdapter.setSections(sections.toArray(dummy));
        //Apply this adapter to the RecyclerView
        mRecyclerView.setAdapter(mSectionedAdapter);


    }
    public void AdapterClick(final String strText) {
        LoadVerifyTextHint();
        //CheckConfirmMethod_otp_or_password();
        // Log.e("CheckMethod = ", "Call CheckConfirmMethod_otp_or_password");
        str_customer_id = findViewById(R.id.str_customer_id);
        strQrType = "";
        if (strText.equals("transfer")) {
            LoadViewStub("Transfer");
            strQrType = "transfer";
        } else if (strText.equals("internet_pay")) {
            str_customer_id.setText(R.string.str_internet_id);
            txtCustomerID.setHint(R.string.str_internet_id);
            String str = "ADSL";
            StrEtlPaymentID = str;
            StrEtlPaymentText = str;
            StrEtlPaymentTitle = str;
            ViewStubCode = "EtlPayment";
            LoadViewStub(ViewStubCode);
            txtCustomerID.setInputType(InputType.TYPE_CLASS_TEXT);
        } else if (strText.equals("pstn_pay")) {

            str_customer_id.setText(R.string.str_pstn_phone_number);
            txtCustomerID.setHint(R.string.str_pstn_phone_number);
         //   Log.e("strText=", strText);

            String str = "PSTN";
            StrEtlPaymentID = str;
            StrEtlPaymentText = str;
            StrEtlPaymentTitle = str;
            ViewStubCode = "EtlPayment";
            LoadViewStub(ViewStubCode);
            txtCustomerID.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);

        } else if (strText.equals("phone_pay")) {
            LoadViewStub("Topup");
        } else if (strText.equals("user_info")) {
            Intent refresh1 = new Intent(getApplicationContext(), UserWalletInfoActivity.class);
            startActivity(refresh1);
        } else if (strText.equals("history")) {
            Intent refresh1 = new Intent(getApplicationContext(), HistoryWalletActivity.class);
            startActivity(refresh1);
        } else if (strText.equals("my_qr")) {
            Intent refresh1 = new Intent(getApplicationContext(), QrCodeActivity.class);
            //Intent refresh1 = new Intent(getApplicationContext(), AutoGenerateQRCode.class);
            startActivity(refresh1);
        } else if (strText.equals("merchant_qr")) {
            Intent refresh1 = new Intent(getApplicationContext(), MerchantQrCodeActivity.class);
            startActivity(refresh1);
        } else if (strText.equals("qr_pay")) {
            strQrType = "QrPay";
            ClearTextEditQrPay();
            new IntentIntegrator(WalletActivity.this).setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES).setCaptureActivity(ScannerActivity.class).initiateScan();
        } else if (strText.equals("change_pw")) {
            Intent refresh1 = new Intent(getApplicationContext(), ChangePasswordWalletActivity.class);
            startActivity(refresh1);
        } else if (strText.equals("setting")) {
            Intent refresh1 = new Intent(getApplicationContext(), SettingActivity.class);
            startActivity(refresh1);
        } else if (strText.equals("buy_package_data")) {
            Intent refresh1 = new Intent(getApplicationContext(), PackageDataActivity.class);
            startActivity(refresh1);
        } else if (strText.equals("notification")) {
            Intent mIntent = new Intent(WalletActivity.this, NotificationListActivity.class);
            startActivityForResult(mIntent, REQUEST_CODE);
        } else if (strText.equals("enduser_cashin")) {
           Intent intent = new Intent(this, ChooseCashInActivity.class);
            intent.putExtra("ActivityKey", strText);
            startActivity(intent);
        } else if (strText.equals("enduser_cashout")) {
            Intent intent = new Intent(this, ChooseCashOutActivity.class);
            intent.putExtra("ActivityKey", strText);
            startActivity(intent);
        } else if (strText.equals("electricity")) {
            Intent intent = new Intent(this, PaymentsActivity.class);
            intent.putExtra("ActivityKey", strText);
            startActivity(intent);
        } else if (strText.equals("water_supply")) {
            Intent intent = new Intent(this, PaymentsActivity.class);
            intent.putExtra("ActivityKey", strText);
            startActivity(intent);
            //  dialog(getString(R.string.str_coming_soon), 2);
        } else if (strText.equals("agent_cashin")) {
            Intent intent = new Intent(this, AgentCashInApproveActivity.class);
            intent.putExtra("ActivityKey", strText);
            startActivity(intent);
        } else if (strText.equals("agent_cashout")) {
            Intent intent = new Intent(this, AgentCashOutApproveActivity.class);
            intent.putExtra("ActivityKey", strText);
            startActivity(intent);
        } else if (strText.equals("agent_balanceinfo")) {
            Intent intent = new Intent(this, AgentBalanceInfoActivity.class);
            intent.putExtra("ActivityKey", strText);
            startActivity(intent);
        } else if (strText.equals("agent_history")) {
            Intent intent = new Intent(this, AgentHistoryWalletActivity.class);
            intent.putExtra("ActivityKey", strText);
            startActivity(intent);
        } else if (strText.equals("merchant_balanceinfo")) {
            Intent intent = new Intent(this, MerchantBalanceInfoActivity.class);
            intent.putExtra("ActivityKey", strText);
            startActivity(intent);
        } else if (strText.equals("merchant_history")) {
            Intent intent = new Intent(this, MerchantHistoryWalletActivity.class);
            intent.putExtra("ActivityKey", strText);
            startActivity(intent);
        } else if (strText.equals("register_agent")) {
            Intent refresh1 = new Intent(getApplicationContext(), RegisterNewAgentActivity.class);
            startActivity(refresh1);
        } else if (strText.equals("register_merchant")) {
            Intent refresh1 = new Intent(getApplicationContext(), RegisterNewMerchantActivity.class);
            startActivity(refresh1);
        } else if (strText.equals("test")) {
            Intent refresh1 = new Intent(getApplicationContext(), TestActivity.class);
            startActivity(refresh1);
        } else if (strText.equals("agent_setting")) {
            Intent refresh1 = new Intent(getApplicationContext(), AgentSettingActivity.class);
            startActivity(refresh1);
        } else if (strText.equals("merchant_setting")) {
            Intent refresh1 = new Intent(getApplicationContext(), MerchantSettingActivity.class);
            startActivity(refresh1);
        } else if (strText.equals("promotion")) {
            Intent refresh1 = new Intent(getApplicationContext(), PromotionActivity.class);
            startActivity(refresh1);
        } else if (strText.equals("show_basic")) {
            showFirstIconOfEnduser();
        } else if (strText.equals("show_more")) {
            showMoreIconOfEnduser() ;
        } else if (strText.equals("agent_show_basic")) {
            showFirstIconOfAgent();
        } else if (strText.equals("agent_show_more")) {
            showMoreIconOfAgent() ;
        } else if (strText.equals("merchant_show_basic")) {
            showFirstIconOfMerchant();
        } else if (strText.equals("merchant_show_more")) {
            showMoreIconOfMerchant() ;
        } else if (strText.equals("agent_approve_cashout_of_merchant")) {
            Intent intent = new Intent(getApplicationContext(), AgentApproveCashoutOfMerchantActivity.class);
            intent.putExtra("ActivityKey", strText);
            startActivity(intent);
        } else if (strText.equals("merchant_cashout_request")) {
            Intent intent = new Intent(getApplicationContext(), MerchantCashOutRequestActivity.class);
            intent.putExtra("ActivityKey", strText);
            startActivity(intent);
        } else if (strText.equals("merchant_transfer_to_enduser")) {
            Intent intent = new Intent(getApplicationContext(), MerchantTransferToEnduserActivity.class);
            intent.putExtra("ActivityKey", strText);
            startActivity(intent);
        } else {

        }


        //   Toast.makeText(getApplication(), strText, Toast.LENGTH_LONG).show();
    }

    public void showFirstIconOfEnduser(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, MODE_PRIVATE);
        String arr[] = pref.getString(GlabaleParameter.PREFS_ETL_END_USER_MENU_LIST_SHOWMORE, "").split("~");
        String list_count = pref.getString(GlabaleParameter.PREFS_ETL_END_USER_MENU_LIST_ฺCOUNT, "0");
        String str="";
        if (!list_count.equals("0")){
            int i =0;
            int EnuserCountFirst = Integer.parseInt(list_count);
            for (String s : arr) {i++;
                if (i<=EnuserCountFirst){
                    str+=s+"~";
                }
            }
            str+="show_more";
        } else{
            str = pref.getString(GlabaleParameter.PREFS_ETL_END_USER_MENU_LIST_SHOWMORE, "");
        }
        SharedPreferences.Editor editor1 = getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, Context.MODE_PRIVATE).edit();
        editor1.putString(GlabaleParameter.PREFS_ETL_END_USER_MENU_LIST, str);
        editor1.apply();
        LoadItem();
    }

    public void showMoreIconOfEnduser() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, MODE_PRIVATE);
        SharedPreferences.Editor editor1 = getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, Context.MODE_PRIVATE).edit();
        editor1.putString(GlabaleParameter.PREFS_ETL_END_USER_MENU_LIST, pref.getString(GlabaleParameter.PREFS_ETL_END_USER_MENU_LIST_SHOWMORE, "")+"~show_basic");
        editor1.apply();
        LoadItem();
    }


    public void showFirstIconOfAgent(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, MODE_PRIVATE);
        String arr[] = pref.getString(GlabaleParameter.PREFS_ETL_AGENT_MENU_LIST_SHOWMORE, "").split("~");
        String list_count = pref.getString(GlabaleParameter.PREFS_ETL_AGENT_MENU_LIST_COUNT, "0");

        String str="";
        if (!list_count.equals("0")){
            int i =0;
            int EnuserCountFirst = Integer.parseInt(list_count);
            for (String s : arr) {i++;
                if (i<=EnuserCountFirst){
                    str+=s+"~";
                }
            }
            str+="agent_show_more";
        } else{
            str = pref.getString(GlabaleParameter.PREFS_ETL_AGENT_MENU_LIST_SHOWMORE, "");
        }

        SharedPreferences.Editor editor1 = getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, Context.MODE_PRIVATE).edit();
        editor1.putString(GlabaleParameter.PREFS_ETL_AGENT_MENU_LIST, str);
        editor1.apply();
        LoadItem();
    }

    public void showMoreIconOfAgent() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, MODE_PRIVATE);
        SharedPreferences.Editor editor1 = getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, Context.MODE_PRIVATE).edit();
        editor1.putString(GlabaleParameter.PREFS_ETL_AGENT_MENU_LIST, pref.getString(GlabaleParameter.PREFS_ETL_AGENT_MENU_LIST_SHOWMORE, "")+"~agent_show_basic");
        editor1.apply();
        LoadItem();
    }

    public void showFirstIconOfMerchant(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, MODE_PRIVATE);
        String arr[] = pref.getString(GlabaleParameter.PREFS_ETL_MERCHANT_MENU_LIST_SHOWMORE, "").split("~");
        String list_count = pref.getString(GlabaleParameter.PREFS_ETL_MERCHANT_MENU_LIST_COUNT, "0");
        String str="";
        if (!list_count.equals("0")){
            int i =0;
            int EnuserCountFirst = Integer.parseInt(list_count);
            for (String s : arr) {i++;
                if (i<=EnuserCountFirst){
                    str+=s+"~";
                }
            }
            str+="merchant_show_more";
        } else{
            str = pref.getString(GlabaleParameter.PREFS_ETL_MERCHANT_MENU_LIST_SHOWMORE, "");
        }
        SharedPreferences.Editor editor1 = getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, Context.MODE_PRIVATE).edit();
        editor1.putString(GlabaleParameter.PREFS_ETL_MERCHANT_MENU_LIST, str);
        editor1.apply();
        LoadItem();
    }

    public void showMoreIconOfMerchant() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, MODE_PRIVATE);
        SharedPreferences.Editor editor1 = getSharedPreferences(GlabaleParameter.PREFS_MY_PREF_AFTER_LOGIN, Context.MODE_PRIVATE).edit();
        editor1.putString(GlabaleParameter.PREFS_ETL_MERCHANT_MENU_LIST, pref.getString(GlabaleParameter.PREFS_ETL_MERCHANT_MENU_LIST_SHOWMORE, "")+"~merchant_show_basic");
        editor1.apply();
        LoadItem();
    }
    public void onClickQRPay2(View v) throws Exception {
      //  strQrType = "QrPay";
        strQrType = "QrPay";
        ClearTextEditQrPay();
        new IntentIntegrator(WalletActivity.this).setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES).setCaptureActivity(ScannerActivity.class).initiateScan();
//aaaaaa
        // Log.e("Active_valuesaa:",  "EasyAES.encryptString(Standard_info)");
        //  dialog("onClickQR",1);
       // Intent refresh1 = new Intent(getApplicationContext(), TestActivity.class);
       // startActivity(refresh1);
    }
    @Override
    protected void onResume() {
        super.onResume();
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
        // === Load balance after charge
        if (strCheckBalanceAfterCharge.equals("1")){
            editor.putString(GlabaleParameter.PREFS_RESUM_CHECK_BALANCE_AFTER_CHARGE, "0" );
            refresh();
        }
        editor.apply();
    }
    @Override
    public boolean onTouch(View v, MotionEvent me) {
/*
        if (me.getAction() == MotionEvent.ACTION_DOWN) {
            oldXvalue = me.getX();
            oldYvalue = me.getY();
         //   Log.i("Omid", "Action Down " + oldXvalue + "," + oldYvalue);
        } else if (me.getAction() == MotionEvent.ACTION_MOVE) {
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.my_relative_layout);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(v.getWidth(), v.getHeight());
            params.leftMargin = (int) (me.getRawX() - (v.getWidth() / 2));
            params.topMargin = (int) (me.getRawY() - (v.getHeight()));
            // v.getHeight(), (int) (me.getRawX() - (v.getWidth() / 2)), (int) (me.getRawY() - (v.getHeight())));
            v.setLayoutParams(params);
        }



 */
        return true;


    }
}

