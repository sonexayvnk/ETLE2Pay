package com.etl.money.notification;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class NotificationSQLiteAdapter{

    myDbHelper myhelper;
    public NotificationSQLiteAdapter(Context context)
    {
        myhelper = new myDbHelper(context);
    }

    public long insertData(String title, String description, String datetime)
    {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.TITLE, title);
        contentValues.put(myDbHelper.DESCRIPTION, description);
        contentValues.put(myDbHelper.DATETIME, datetime);
        contentValues.put(myDbHelper.STATUST, "0");
        long id = dbb.insert(myDbHelper.TABLE_NAME, null , contentValues);
        return id;
    }

    public String getData()
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.UID,myDbHelper.TITLE,myDbHelper.DESCRIPTION,myDbHelper.DATETIME,myDbHelper.STATUST};
        Cursor cursor =db.query(myDbHelper.TABLE_NAME,columns,null,null,null,null,myDbHelper.UID+" DESC ");
        StringBuffer buffer= new StringBuffer();
        while (cursor.moveToNext())
        {
            int cid =cursor.getInt(cursor.getColumnIndex(myDbHelper.UID));
            String title =cursor.getString(cursor.getColumnIndex(myDbHelper.TITLE));
            String  Description =cursor.getString(cursor.getColumnIndex(myDbHelper.DESCRIPTION));
            String  datetime =cursor.getString(cursor.getColumnIndex(myDbHelper.DATETIME));
            String  status =cursor.getString(cursor.getColumnIndex(myDbHelper.STATUST));
            buffer.append(cid+ "~" + title + "~" + Description +"~"+ datetime +"~"+ status +"|");
        }
        return buffer.toString();
    }

    public  int delete(String uname)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={uname};

        int count =db.delete(myDbHelper.TABLE_NAME ,myDbHelper.TITLE+" = ?",whereArgs);
        return  count;
    }
    public  int  CleanUpOldLog(String str_limited_storage)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={str_limited_storage};
        int count =db.delete(myDbHelper.TABLE_NAME ,myDbHelper.TITLE+" = ?",whereArgs);
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
    public int updateName(String oldName , String newName)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.TITLE,newName);
        String[] whereArgs= {oldName};
        int count =db.update(myDbHelper.TABLE_NAME,contentValues, myDbHelper.TITLE+" = ?",whereArgs );
        return count;
    }
    public int updateStatus(String strIds , String strStatus)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.STATUST,strStatus);
        String[] whereArgs= {strIds};
        int count =db.update(myDbHelper.TABLE_NAME,contentValues, myDbHelper.UID+" = ?",whereArgs );
        return count;
    }

    static class myDbHelper extends SQLiteOpenHelper
    {
        private static final String DATABASE_NAME = "myDatabase";    // Database Name
        private static final String TABLE_NAME = "myTable";   // Table Name
        private static final int DATABASE_Version = 1;    // Database Version
        private static final String UID="_id";     // Column I (Primary Key)
        private static final String TITLE = "Title";    //Column II
        private static final String DESCRIPTION = "Description";    // Column III
        private static final String DATETIME = "Datetime";    // Column IV
        private static final String STATUST= "Status";    // Column V
        private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
                " ("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+TITLE+" VARCHAR(100) ,"+ DESCRIPTION+" VARCHAR(225),"+ DATETIME+" VARCHAR(20),"+ STATUST+" VARCHAR(1));";
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