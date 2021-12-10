package com.etl.money.config;

import android.text.format.DateUtils;

import com.etl.money.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import kotlin.contracts.Returns;

public class AutoLogout {





    //1 minute = 60 seconds
   //1 hour = 60 x 60 = 3600
   //1 day = 3600 x 24 = 86400
    public static Integer Difference(String dateStart1, String dateStop1) {

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
         String dateStart = dateStart1;
         String dateStop = dateStop1;


        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = d2.getTime() - d1.getTime();
        long diffSeconds = diff / 1000 ;
        int ints = 0 ;
        ints = (int) diffSeconds;
        return  ints;
    }
}
