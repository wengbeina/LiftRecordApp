package cn.edu.zucc.TPF.SQLiteDB;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.zucc.TPF.Bean.LiftDataBean;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class LiftDataDAO {
	private MySQLiteHelper mySQLiteHelper ;

	public LiftDataDAO(Context context) {
		mySQLiteHelper = new MySQLiteHelper(context);
		// TODO Auto-generated constructor stub
	}
	
	/**插入*/
	public void insertData(LiftDataBean liftData){
		SQLiteDatabase db = null;
		try{
		db = mySQLiteHelper.getWritableDatabase();
		String insertsql = "Insert into liftdata(liftid, accx, accy, accz, rotatex, rotatey, rotatez, recordtime) Values" +
				"('"+liftData.getLiftid()+"', '"+liftData.getAccx()+"', '"+liftData.getAccy()+"'," +
						"'"+liftData.getAccz()+"', '"+liftData.getRotatex()+"','"+ liftData.getRotatey()+
						"','"+liftData.getRotatez()+"','"+liftData.getRecordtime()+"')";
		db.beginTransaction();
		
		db.execSQL(insertsql);
		db.setTransactionSuccessful();
		Log.d(liftData.getLiftid(), "----插入数据成功！----");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(db!=null && db.isOpen()){
				db.endTransaction();
				db.close();
			}
		}
	}
	/**删除所有的数据*/
	public void deleteAllData(String liftid){
		SQLiteDatabase db = null;
		try{
			db = mySQLiteHelper.getWritableDatabase();
			String whereClause = "liftid = ?";
			String[]  whereArgs ={liftid};
			db.beginTransaction();
			db.delete("liftdata", whereClause, whereArgs);
			db.setTransactionSuccessful();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(db!=null && db.isOpen()){
				db.endTransaction();
				db.close();
			}
		}
	}
	/**删除某个liftid某个时间点之前的所有数据*/
	public void deleteDataBefore(String liftid, Timestamp time){
		SQLiteDatabase db = null;
		try{
			db = mySQLiteHelper.getWritableDatabase();
			String whereClause = "liftid = ? and recordtime <= ?";
			String[]  whereArgs ={liftid, "'"+time+"'"};
			db.beginTransaction();
			db.delete("liftdata", whereClause, whereArgs);
			db.setTransactionSuccessful();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(db!=null && db.isOpen()){
				db.endTransaction();
				db.close();
			}
		}
	}
	
	/**查询数据库所有电梯的所有数据记录*/
	public List<Map<String, Object>> selectAllLiftData(){
		List<Map<String, Object>> listTemp = new ArrayList<Map<String, Object>>();
		Cursor cursor = null;
		SQLiteDatabase db = null;
		try{
			db = mySQLiteHelper.getReadableDatabase();
			cursor = db.query("liftdata", null, null, null, null, null, "recordtime desc");
			int idIndex = cursor.getColumnIndex("liftid");
			int accxIndex = cursor.getColumnIndex("accx");
			int accyIndex = cursor.getColumnIndex("accy");
			int acczIndex = cursor.getColumnIndex("accz");
			int rotatexIndex = cursor.getColumnIndex("rotatex");
			int rotateyIndex = cursor.getColumnIndex("rotatey");
			int rotatezIndex = cursor.getColumnIndex("rotatez");
			int recordtimeIndex = cursor.getColumnIndex("recordtime");
			
			for(cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()){
				Map<String, Object> map =new HashMap<String, Object>();
				map.put("liftid", cursor.getString(idIndex));
				map.put("accx", cursor.getString(accxIndex));
				map.put("accy", cursor.getString(accyIndex));
				map.put("accz", cursor.getString(acczIndex));
				map.put("rotatex", cursor.getString(rotatexIndex));
				map.put("rotatey", cursor.getString(rotateyIndex));
				map.put("rotatez", cursor.getString(rotatezIndex));
				map.put("recordtime", cursor.getString(recordtimeIndex));
				listTemp.add(map);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(cursor != null){
				cursor.close();
			}
			if(db!=null &&db.isOpen())
				db.close();
		}
		return listTemp;
	}
	
	/**某一段时间内某个电梯数据存储记录的总数量*/
	public int getLiftdataCount(String liftid, Timestamp from, Timestamp to){
		Cursor cursor = null;
		SQLiteDatabase db = null;
		int count = 0;
		try{
			db = mySQLiteHelper.getReadableDatabase();
			String selectClause = "liftid = ? and recordtime >= '"+from+"' and recordtime <= '"+to+"'";
			String[] selectArgs = {liftid};
			cursor = db.query("liftdata", null, selectClause, selectArgs, null, null, null);
			if(cursor == null)
				return 0;
			count = cursor.getCount();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(cursor!=null){
				cursor.close();
				cursor = null;
			}
			if(db!=null && db.isOpen()){
				db.close();
				db = null;
			}				
		}
		return count;
	}
	
	public List<Map<String, Object>> SelectLiftdata(String liftid, Timestamp from, Timestamp to){
		List<Map<String, Object>> listTemp = new ArrayList<Map<String, Object>>();
		Cursor cursor = null;
		SQLiteDatabase db = null;
		try{
			db = mySQLiteHelper.getReadableDatabase();
			String selectClause = "liftid = ? and recordtime >= '"+from
					+"' and recordtime <= '"+to+"'";
			String [] selectArgs = {liftid};
			//分页查询
			cursor = db.query("liftdata", null, selectClause, selectArgs, null, null, null);
			if(cursor == null){
				return null;
			}
			
			int idIndex = cursor.getColumnIndex("liftid");
			int accxIndex = cursor.getColumnIndex("accx");
			int accyIndex = cursor.getColumnIndex("accy");
			int acczIndex = cursor.getColumnIndex("accz");
			int rotatexIndex = cursor.getColumnIndex("rotatex");
			int rotateyIndex = cursor.getColumnIndex("rotatey");
			int rotatezIndex = cursor.getColumnIndex("rotatez");
			int recordtimeIndex = cursor.getColumnIndex("recordtime");
			
			for(cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("liftid", cursor.getString(idIndex));
				map.put("accx", cursor.getFloat(accxIndex));
				map.put("accy", cursor.getFloat(accyIndex));
				map.put("accz", cursor.getFloat(acczIndex));
				map.put("rotatex", cursor.getFloat(rotatexIndex));
				map.put("rotatey", cursor.getFloat(rotateyIndex));
				map.put("rotatez", cursor.getFloat(rotatezIndex));
				//在显示的时候将日期和时间分开，可以去掉毫秒
				Date date = new Date(Timestamp.valueOf(cursor.getString(recordtimeIndex)).getTime());
				Time time = new Time(Timestamp.valueOf(cursor.getString(recordtimeIndex)).getTime());
				String datetime = ""+date+" "+time;
				map.put("recordtime",datetime);
				listTemp.add(map);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(cursor!=null){
				cursor.close();
				cursor = null;
			}
			if(db!=null && db.isOpen()){
				db.close();
				db = null;
			}
		}
		return listTemp;
	}
	
	
	/**实现上一页和下一页的逻辑控制,实时动态刷新*/
	public List<Map<String, Object>> getLiftdataList(String liftid, Timestamp from, Timestamp to, int pageNum, int pageSize){
		List<Map<String, Object>> listTemp = new ArrayList<Map<String, Object>>();;
		Cursor cursor = null;
		SQLiteDatabase db = null;
		try{
			db = mySQLiteHelper.getReadableDatabase();
			String selectClause = "liftid = ? and recordtime >= '"+from+"' and recordtime <= '"+to+"'";
			String [] selectArgs = {liftid};
			//分页查询
			cursor = db.query("liftdata", null, selectClause, selectArgs, null, null, "recordtime desc limit "+
			         String.valueOf(pageSize)+" offset "+ String.valueOf((pageNum-1)*pageSize));
			if(cursor == null){
				return null;
			}
			
			int idIndex = cursor.getColumnIndex("liftid");
			int accxIndex = cursor.getColumnIndex("accx");
			int accyIndex = cursor.getColumnIndex("accy");
			int acczIndex = cursor.getColumnIndex("accz");
			/*int rotatexIndex = cursor.getColumnIndex("rotatex");
			int rotateyIndex = cursor.getColumnIndex("rotatey");
			int rotatezIndex = cursor.getColumnIndex("rotatez");*/
			
			int recordtimeIndex = cursor.getColumnIndex("recordtime");			
			
			for(cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("liftid", cursor.getString(idIndex));
				map.put("accx", cursor.getFloat(accxIndex));
				map.put("accy", cursor.getFloat(accyIndex));
				map.put("accz", cursor.getFloat(acczIndex));
				/*map.put("rotatex", cursor.getString(rotatexIndex));
				map.put("rotatey", cursor.getString(rotateyIndex));
				map.put("rotatez", cursor.getString(rotatezIndex));*/
				//在显示的时候将日期和时间分开，可以去掉毫秒
				Date date = new Date(Timestamp.valueOf(cursor.getString(recordtimeIndex)).getTime());
				Time time = new Time(Timestamp.valueOf(cursor.getString(recordtimeIndex)).getTime());
				String datetime = ""+date+" "+time;
				map.put("recordtime",datetime);
				listTemp.add(map);
			}
			
		}catch(Exception e){
			e.printStackTrace();;
		}finally{
			if(cursor!=null){
				cursor.close();
				cursor = null;
			}
			if(db!=null && db.isOpen()){
				db.close();
				db = null;
			}
		}
		return listTemp;
	}

}
