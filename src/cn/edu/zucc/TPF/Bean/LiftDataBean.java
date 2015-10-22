package cn.edu.zucc.TPF.Bean;

import java.sql.Timestamp;

public class LiftDataBean {
    private String liftid;
    private float accx;
    private float accy;
    private float accz;
    private float rotatex;
    private float rotatey;
    private float rotatez;
    
    private Timestamp recordtime;
	public LiftDataBean() {
		// TODO Auto-generated constructor stub
	}
	public String getLiftid() {
		return liftid;
	}
	public void setLiftid(String liftid) {
		this.liftid = liftid;
	}
	public float getAccx() {
		return accx;
	}
	public void setAccx(float accx) {
		this.accx = accx;
	}
	public float getAccy() {
		return accy;
	}
	public void setAccy(float accy) {
		this.accy = accy;
	}
	public float getAccz() {
		return accz;
	}
	public void setAccz(float accz) {
		this.accz = accz;
	}
	public Timestamp getRecordtime() {
		return recordtime;
	}
	public void setRecordtime(Timestamp recordtime) {
		this.recordtime = recordtime;
	}
	public float getRotatex() {
		return rotatex;
	}
	public void setRotatex(float rotatex) {
		this.rotatex = rotatex;
	}
	public float getRotatey() {
		return rotatey;
	}
	public void setRotatey(float rotatey) {
		this.rotatey = rotatey;
	}
	public float getRotatez() {
		return rotatez;
	}
	public void setRotatez(float rotatez) {
		this.rotatez = rotatez;
	}

}
