package com.etl.money;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by Administrator on 16/09/2015.
 */
public class DialogConnectVasService extends ProgressDialog {

    private AsyncTask task;

    public DialogConnectVasService(Context context,AsyncTask task) {
        //สร้าง Dialog จาก super class
        super(context);

        //เชื่อม AsyncTask (ConnectServer) เข้ากับ Dialog
        this.task = task;
    }

    //ถ้ามีการยกเลิกตอนที่ Dialog ขึ้นมาจะมาทำงาน Function นี้
    public void cancel() {
        //ยกเลิกเชื่อมต่อกับ Server
        task.cancel(true);
        super.cancel();
    }

}
