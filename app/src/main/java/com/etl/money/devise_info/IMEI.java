package com.etl.money.devise_info;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

import androidx.core.app.ActivityCompat;

/**
 * Created by Administrator on 17/09/2015.
 */
public class IMEI {

    private String versionName;

    public String get_dev_id(Context ctx) {


        //Getting the Object of TelephonyManager
        TelephonyManager tManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);


        // Add 2018-05-24 by Khemphone
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // Log.e("PERMISSION", "PERMISSION NOT GRANTED");
            return "";
        }
        // Add 2018-05-24 by Khemphone


        //Getting IMEI Number of Devide
        String Imei = tManager.getDeviceId();
        //http://stackoverflow.com/questions/14517338/android-check-whether-the-phone-is-dual-sim
        //Getting the Object of TelephonyManager
        //Getting IMEI Number of Devide
        String iMEI = "";
        String simSerialNumber = "";
        String networkOperatorName = "";
        String mobileModel = "";
        String mobileBRAND = "";
        String mobileInfo = "";
        String mobileRELEASE = "";
        mobileModel = Build.MODEL;
        mobileBRAND = Build.BRAND;
        mobileRELEASE = Build.VERSION.RELEASE;


        try {
//            mobileInfo = tManager.getDataState();
        } catch (Exception e) {

        }


        try {
            iMEI = tManager.getDeviceId();
        } catch (Exception e) {

        }

        try {
            simSerialNumber = tManager.getSimSerialNumber();
        } catch (Exception e) {

        }

        try {
            networkOperatorName = tManager.getNetworkOperatorName();
        } catch (Exception e) {

        }


//        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//   GsmCellLocation cellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
//int cid = cellLocation.getCid();
//   int lac = cellLocation.getLac();
        return iMEI + "," + simSerialNumber + "," + mobileModel + "," + mobileBRAND +
                "," + mobileRELEASE + "," + networkOperatorName;

    }


    public String get_dev_id_andSim_only(Context ctx) {

        //Getting the Object of TelephonyManager
        TelephonyManager tManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);

        // Add 2018-05-24 by Khemphone
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // Log.e("PERMISSION", "PERMISSION NOT GRANTED");
            return "";
        }
        // Add 2018-05-24 by Khemphone

        //Getting IMEI Number of Devide
        String Imei = tManager.getDeviceId();
        //http://stackoverflow.com/questions/14517338/android-check-whether-the-phone-is-dual-sim
        //Getting the Object of TelephonyManager
        //Getting IMEI Number of Devide
        String iMEI = "";
        String simSerialNumber = "";
//        String networkOperatorName = "";
//        String mobileModel = "";
//        String mobileBRAND = "";
//        String mobileInfo = "";
//        String mobileRELEASE = "";
//        mobileModel = Build.MODEL;
//        mobileBRAND = Build.BRAND;
//        mobileRELEASE = Build.VERSION.RELEASE;


//        try {
////            mobileInfo = tManager.getDataState();
//        } catch (Exception e) {
//
//        }


        try {
            iMEI = tManager.getDeviceId();
        } catch (Exception e) {

        }

        try {
            simSerialNumber = tManager.getSimSerialNumber();
        } catch (Exception e) {

        }

//        try {
//            networkOperatorName = tManager.getNetworkOperatorName();
//        } catch (Exception e) {
//
//        }


//        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//   GsmCellLocation cellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
//int cid = cellLocation.getCid();
//   int lac = cellLocation.getLac();
        return iMEI + "," + simSerialNumber;

    }

//    public int get_versionCode(Context ctx) {
//
//        PackageInfo pinfo;
//        int versionCode =-1;
//        try {
//            pinfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
//            versionCode= pinfo.versionCode;
//
//            //ET2.setText(versionNumber);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        return versionCode;
//    }



    public int get_versionCode(Context ctx) {

        //PackageInfo pinfo;
        int versionCode = -1;
        versionCode = 1;
        return versionCode;
    }


    public String get_versionName(Context ctx) {

        PackageInfo pinfo;

        String versionName = "1.0.0";
        try {
            pinfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            //versionCode= pinfo.versionCode;
            versionName = pinfo.versionName;
            //ET2.setText(versionNumber);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return versionName;
    }

    //----------------------------------------------------------------------------------------------


    public String get_dev_id02(Context ctx) {
        //http://stackoverflow.com/questions/14517338/android-check-whether-the-phone-is-dual-sim
        //Getting the Object of TelephonyManager
//        TelephonyManager tManager;
//        tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        TelephonyManager tManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // Log.e("get_dev_id_andSim_only", "77777:::: " );
            return "";
        }


        //Getting IMEI Number of Devide
        String iMEI = "";
        String simSerialNumber = "";
        String networkOperatorName = "";
        String mobileModel = "";
        String mobileBRAND = "";
        String networkCountryIso = "";
        String mobileInfo = "";
        String mobileRELEASE = "";
        String mobileMANUFACTURER="";
        String simCountryIso = "";

        mobileModel = Build.MODEL;
        mobileBRAND = Build.BRAND;


        mobileInfo = Build.TYPE;
        mobileMANUFACTURER = Build.MANUFACTURER;
        mobileRELEASE = Build.VERSION.RELEASE;


//        Log.e("mobileInfo:",mobileInfo);
//        Log.e("mobileMANUFACTURER:",mobileMANUFACTURER);
 //       Log.e("mobileRELEASE:",mobileRELEASE);


        //TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        // String simno = mTelephonyMgr.getSimSerialNumber();


        try {
//            mobileInfo = tManager.getDataState();
        } catch (Exception e) {

        }

        try {
            iMEI = tManager.getDeviceId();
        } catch (Exception e) {

        }

        try {
            simCountryIso = tManager.getSimCountryIso();
        } catch (Exception e) {

        }


        try {
            networkCountryIso = tManager.getNetworkCountryIso();
        } catch (Exception e) {

        }

        try {
            simSerialNumber = tManager.getSimSerialNumber();
        } catch (Exception e) {

        }

        try {
            networkOperatorName = tManager.getNetworkOperatorName();
        } catch (Exception e) {

        }



        int lac = 0;
        int cid = 0;


        try {
            //  TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            // GsmCellLocation cellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
            //  TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            // TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            // GsmCellLocation cellLocation = (GsmCellLocation) telephonyManager.getCellLocation();


            GsmCellLocation cellLocation = (GsmCellLocation) tManager.getCellLocation();
            cid = cellLocation.getCid() & 0xffff;
            lac = cellLocation.getLac() & 0xffff;

        } catch (Exception e) {

        }


        String ConnectType = null;


        boolean wifiDataAvailable = false;
        boolean mobileDataAvailable = false;
        try {


            ConnectivityManager conManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] networkInfo = conManager.getAllNetworkInfo();
            for (NetworkInfo netInfo : networkInfo) {
                if (netInfo.getTypeName().equalsIgnoreCase("WIFI"))
                    if (netInfo.isConnected())
                        wifiDataAvailable = true;
                if (netInfo.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (netInfo.isConnected())
                        mobileDataAvailable = true;
            }
            ConnectType = "wifi:" + wifiDataAvailable + ",Mobile:" + mobileDataAvailable;
        } catch (Exception e) {

        }



        //  return iMEI + "," + languseCode + "," + simSerialNumber + "," + mobileModel + "," + mobileBRAND +
        //        "," + mobileRELEASE + "," + networkOperatorName + "," + networkCountryIso + "," + cid + "," + lac + "," + ConnectType;

        return iMEI  + "," + simSerialNumber + "," + mobileModel + "," + mobileBRAND +
                "," + mobileRELEASE + "," + networkOperatorName + "," + networkCountryIso + "," + cid + "," + lac + "," + ConnectType;
    }


}
