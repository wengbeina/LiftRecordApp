package cn.edu.zucc.TPF.App;

import java.sql.Timestamp;

import cn.edu.zucc.TPF.Bean.AccCheckValue;
import cn.edu.zucc.TPF.Bean.AccRecordBean;
import cn.edu.zucc.TPF.Bean.LiftDataBean;
import cn.edu.zucc.TPF.SQLiteDB.LiftDataDAO;

public class LiftDataToLocalDBHandle implements Runnable{
    private AccRecordBean accBean;
    private LiftDataDAO liftDao;
    private int period = 5;
    private String liftid;
    private AccCheckValue accCheck;
	public LiftDataToLocalDBHandle(LiftDataDAO liftDao, AccRecordBean accBean, AccCheckValue accCheck, 
			String liftid) {
		this.accBean = accBean;
		this.liftid = liftid;
		this.liftDao = liftDao;
		this.accCheck = accCheck;
		// TODO Auto-generated constructor stub
	}
	
	public void run(){
		LiftDataBean liftData = new LiftDataBean();
		while(true){
			try {				
				while(!accBean.isSaveOn()){	
					Thread.sleep(3*1000);
				}
				while(accCheck.isPause()){
					Thread.sleep(3*1000);
				}
				
				Thread.sleep(period*100);				
				int sizeX = accBean.getAccX().size();
				int sizeY = accBean.getAccY().size();
				int sizeZ = accBean.getAccZ().size();
				if(sizeX>0 && sizeY>0 && sizeZ>0 ){				   
				   liftData.setLiftid(liftid);
				   liftData.setAccx(accBean.getAccX().get(sizeX-1) - accCheck.getAccXCheck());
				   liftData.setAccy(accBean.getAccY().get(sizeY-1) - accCheck.getAccYCheck());
				   liftData.setAccz(accBean.getAccZ().get(sizeZ-1) - accCheck.getAccZCheck());
				   liftData.setRotatex(accBean.getRotateX());
				   liftData.setRotatey(accBean.getRotateY());
				   liftData.setRotatez(accBean.getRotateZ());				   
				   liftData.setRecordtime(new Timestamp(System.currentTimeMillis()));				
				   liftDao.insertData(liftData);				
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
