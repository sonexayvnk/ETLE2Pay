package com.etl.money.partner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.service.quicksettings.Tile;

import com.etl.money.notification.NotificationSQLiteAdapter;
import com.etl.money.notification.ToastMessage;


public class PaymentSQLiteAdapter {

    myDbHelper myhelper;
    public PaymentSQLiteAdapter(Context context)
    {
        myhelper = new myDbHelper(context);
    }

    public long insertData(String title, String description , String currentDate , String brand, String type)
    {


        SQLiteDatabase db1 = myhelper.getWritableDatabase();
        String count1 = "SELECT COUNT (*) FROM " + myDbHelper.TABLE_NAME + " WHERE " + myDbHelper.TITLE + " = '" + title + "'  AND  " + myDbHelper.BRANCH + " = '" + brand + "'" ;
        Cursor mcursor1 = db1.rawQuery(count1, null);
        mcursor1.moveToFirst();
        int icount = mcursor1.getInt(0);


        long id = 0 ;
        if (icount>0){
            SQLiteDatabase db2 = myhelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            String count2 = "UPDATE "+myDbHelper.TABLE_NAME+" SET "+myDbHelper.DESCRIPTION+ " = '"+ description +  "' WHERE " + myDbHelper.TITLE + " = '" + title + "'  AND  " + myDbHelper.BRANCH + " = '" + brand + "'" ;
            Cursor mcursor2 = db2.rawQuery(count2, null);
            mcursor2.moveToFirst();
        } else {
            SQLiteDatabase db3 = myhelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(myDbHelper.TITLE, title);
            contentValues.put(myDbHelper.DESCRIPTION, description);
            contentValues.put(myDbHelper.DATETIME, currentDate);
            contentValues.put(myDbHelper.STATUST, "0");
            contentValues.put(myDbHelper.TYPE,type);
            contentValues.put(myDbHelper.BRANCH,brand);
            id = db3.insert(myDbHelper.TABLE_NAME, null , contentValues);
        }
        return id;
    }

    public String getData(String str)
    {


        SQLiteDatabase db = myhelper.getWritableDatabase();
      //  String[] columns = {myDbHelper.UID, myDbHelper.TITLE, myDbHelper.DESCRIPTION, myDbHelper.DATETIME, myDbHelper.STATUST, myDbHelper.TYPE, myDbHelper.BRANCH};

      //  String[] whereArgs ={"uname"};

      //  Cursor cursor =db.query(myDbHelper.TABLE_NAME,columns,null,null,null,null, myDbHelper.UID+" DESC ",null);

        Cursor cursor = db.rawQuery("SELECT * FROM " + myDbHelper.TABLE_NAME + " WHERE " + myDbHelper.TYPE + " = " +"'"+str+"'", null);


        StringBuffer buffer= new StringBuffer();
        while (cursor.moveToNext())
        {
            int cid =cursor.getInt(cursor.getColumnIndex(myDbHelper.UID));
            String title =cursor.getString(cursor.getColumnIndex(myDbHelper.TITLE));
            String  Description =cursor.getString(cursor.getColumnIndex(myDbHelper.DESCRIPTION));
            String  datetime =cursor.getString(cursor.getColumnIndex(myDbHelper.DATETIME));
            String  status =cursor.getString(cursor.getColumnIndex(myDbHelper.STATUST));
            String  type =cursor.getString(cursor.getColumnIndex(myDbHelper.TYPE));
            String  Branch =cursor.getString(cursor.getColumnIndex(myDbHelper.BRANCH));
            buffer.append(cid+ "~" + title + "~" + Description +"~"+ datetime +"~"+ status +"~"+ type + "~"+ Branch + "|");
        }
        return buffer.toString();
    }

    //public String   delete(String strTitle  )
  int  CleanUpOldLog(String str_limited_storage)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={str_limited_storage};
        int count =db.delete(myDbHelper.TABLE_NAME , myDbHelper.TITLE+" = ?",whereArgs);
        return  count;
    }
    public String  getCount( String strStaus){
       SQLiteDatabase db = myhelper.getWritableDatabase();
        String count = "SELECT COUNT (*) FROM " + myDbHelper.TABLE_NAME + " WHERE " + myDbHelper.STATUST + " = " + strStaus;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        return ""+icount;
    }

    public String update(String title, String description , String currentDate , String brandID, String type)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String count = "UPDATE " + myDbHelper.TABLE_NAME + " SET "  ;
        count += " " + myDbHelper.DESCRIPTION + " = "  + "'" + description +"'";
        count += " " + myDbHelper.DATETIME + " = "  + "'" + currentDate +"'";
        count += " " + myDbHelper.TITLE + " = 'LN' ";
        count += "WHERE " + myDbHelper.TITLE + " = " + "'" + title +"'" + " AND " + myDbHelper.BRANCH + " = " + "'" + brandID +"'" + " AND " + myDbHelper.TYPE + " = " + "'" + type +"'";
      //  String count =db.update(myDbHelper.TABLE_NAME,contentValues, myDbHelper.TITLE+" = ?",whereArgs );
     //   String count = "SELECT COUNT (*) FROM " + myDbHelper.TABLE_NAME + " WHERE " + myDbHelper.STATUST + " = " + strStaus;
        return count;
    }
    public  int delete3(String ids)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={ids};

        int count =db.delete(PaymentSQLiteAdapter.myDbHelper.TABLE_NAME , PaymentSQLiteAdapter.myDbHelper.TITLE+" = ?",whereArgs);
        return  count;
    }

    public int Delete(String BRANCH , String TITLE)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String count = "DELETE FROM "+myDbHelper.TABLE_NAME +" WHERE " + myDbHelper.TITLE + " = '" + TITLE + "'  AND  " + myDbHelper.BRANCH + " = '" + BRANCH + "'    ";
       Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        return 1 ;
    }


    public long updateStatus2(String TITLE , String DESCRIPTION)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
       // contentValues.put(myDbHelper.DESCRIPTION,DESCRIPTION);
       // String[] whereArgs= {TITLE};
       // int count =db.update(myDbHelper.TABLE_NAME,contentValues, myDbHelper.TITLE+" = ?",whereArgs );

        String count = "UPDATE "+myDbHelper.TABLE_NAME+" SET "+myDbHelper.DESCRIPTION+ " = '"+ DESCRIPTION +  "ssss' WHERE " + myDbHelper.TITLE + " = '" + TITLE + "'    ";
        Cursor mcursor = db.rawQuery(count, null);

       // int icount = mcursor.getInt(0);

        int result = mcursor.getCount();
        mcursor.moveToFirst();
       // long changes = icount.longForQuery(db, "SELECT changes()", null);

       //  long affectedRowCount = mcursor.getLong(mcursor.getColumnIndex("affected_row_count"));
       // Log.d("LOG", "affectedRowCount = " + affectedRowCount);
        return 1;
    }

    public int updateStatus(String TITLE , String DESCRIPTION)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.DESCRIPTION,DESCRIPTION);
        String[] whereArgs= {TITLE};
        int count =db.update(myDbHelper.TABLE_NAME,contentValues, myDbHelper.TITLE+" = ?",whereArgs );
        return count;
    }

    static class myDbHelper extends SQLiteOpenHelper
    {
        private static final String DATABASE_NAME = "myDatabase";    // Database Name
        private static final String TABLE_NAME = "custsomerLog";   // Table Name
        private static final int DATABASE_Version = 1;    // Database Version
        private static final String UID="_id";     // Column I (Primary Key)
        private static final String TITLE = "Title";    //Column II
        private static final String DESCRIPTION = "Description";    // Column III
        private static final String DATETIME = "Datetime";    // Column IV
        private static final String STATUST= "Status";    // Column V
        private static final String TYPE= "Type";    // Column VI
        private static final String BRANCH= "Branch";    // Column VII
        private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
                " ("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+TITLE+" VARCHAR(100) ,"+ DESCRIPTION+" VARCHAR(225),"+ DATETIME+" VARCHAR(20),"+ STATUST+" VARCHAR(1),"+ TYPE+" VARCHAR(50),"+ BRANCH+" VARCHAR(3));";
        private static final String DROP_TABLE ="DROP TABLE IF EXISTS "+TABLE_NAME;
        private Context context;

        public myDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context=context;
        }

        public void onCreate(SQLiteDatabase db) {

            try {
                db.execSQL(CREATE_TABLE);
            } catch (Exception e) {
                ToastMessage.notificationMessage(context,""+e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                ToastMessage.notificationMessage(context,"OnUpgrade");
                db.execSQL(DROP_TABLE);
                onCreate(db);
            }catch (Exception e) {
                ToastMessage.notificationMessage(context,""+e);
            }
        }
    }

}