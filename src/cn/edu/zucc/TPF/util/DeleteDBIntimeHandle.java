package cn.edu.zucc.TPF.util;

import java.sql.Timestamp;

import cn.edu.zucc.TPF.SQLiteDB.LiftDataDAO;
import android.content.Context;

public class DeleteDBIntimeHandle implements Runnable{
	private String liftid;
    private Context context;
    private long period = 1000*3600*24*3;
	public DeleteDBIntimeHandle(Context context , String liftid) {
		liftid = this.liftid;
		context = this.context;
		// TODO Auto-generated constructor stub
	}
	public void run(){
		LiftDataDAO liftDao = new LiftDataDAO(context);		
		while(true){
			Timestamp current = new Timestamp(System.currentTimeMillis()-period);
			liftDao.deleteDataBefore(liftid, current);			
			try {
				Thread.sleep(period);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
