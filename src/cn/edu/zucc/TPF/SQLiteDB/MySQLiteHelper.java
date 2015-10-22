package cn.edu.zucc.TPF.SQLiteDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper{
	//如需存储在SD卡，加上sd_path的绝对路径
	//private String sd_path = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
    private static final String DATABASE_NAME = "liftdata.db";
    private static final int DATABASE_VERSION = 1;
	
    public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);// TODO Auto-generated constructor stub
	}
    
    public void onCreate(SQLiteDatabase db){
    	db.execSQL("create table if not exists liftdata(id integer primary key autoincrement, liftid text, accx float," +
    			"accy float, accz float, rotatex float, rotatey float, rotatez float, recordtime datetime)");
        Log.d(db.toString(), "----DB Created!");
    }
    
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
    	db.execSQL("drop table if exists liftdata");
    	onCreate(db);
    }

}
