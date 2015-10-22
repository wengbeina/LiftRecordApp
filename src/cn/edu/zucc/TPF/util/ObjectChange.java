package cn.edu.zucc.TPF.util;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import cn.edu.zucc.TPF.Bean.AlarmDealBean;
import cn.edu.zucc.TPF.Bean.LiftDataBean;

public class ObjectChange {

	public ObjectChange() {
		// TODO Auto-generated constructor stub
	}
    
	public static Map<String, Object> LiftDataBeanToMap(LiftDataBean liftData){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("liftid", liftData.getLiftid());
		map.put("accx", new Float(liftData.getAccx()));
		map.put("accy", new Float(liftData.getAccy()));
		map.put("accz", new Float(liftData.getAccz()));
		map.put("rotatex", new Float(liftData.getRotatex()));
		map.put("rotatey", new Float(liftData.getRotatey()));
		map.put("rotatez", new Float(liftData.getRotatez()));
		map.put("recordtime", liftData.getRecordtime());
		
		return map;
	}
	
	public static LiftDataBean MapToLiftDataBean(Map<String, Object> map){
		LiftDataBean liftData = new LiftDataBean();
		liftData.setLiftid((String)map.get("liftid"));
	    liftData.setAccx(Float.parseFloat(map.get("accx").toString()));
	    liftData.setAccy(Float.parseFloat(map.get("accy").toString()));
	    liftData.setAccz(Float.parseFloat(map.get("accz").toString()));
		liftData.setRotatex(Float.parseFloat(map.get("rotatex").toString()));
		liftData.setRotatey(Float.parseFloat(map.get("rotatey").toString()));
		liftData.setRotatez(Float.parseFloat(map.get("rotatez").toString()));
		liftData.setRecordtime(new Timestamp((Long)map.get("recordtime")));
		
		return liftData;
	}
	
	public static Map<String, Object> AlarmDealBeanToMap(AlarmDealBean alarm){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", new Integer(alarm.getId()));
		map.put("liftid", alarm.getLiftid());
		map.put("senderid", alarm.getSenderid());
		map.put("warninglevel", new Integer(alarm.getWarninglevel()));
		map.put("accx", new Float(alarm.getAccx()));
		map.put("accy", new Float(alarm.getAccy()));
		map.put("accz", new Float(alarm.getAccz()));
		map.put("unusualtime", alarm.getUnusualtime());
		map.put("recordtime", alarm.getRecordtime());
		map.put("dealtag", alarm.getDealtag());
		
		return map;
	}
	
	public static AlarmDealBean MapToAlarmDealBean(Map<String, Object> map){
		AlarmDealBean alarm = new AlarmDealBean();
		alarm.setId(Integer.parseInt(map.get("id").toString()));
		alarm.setLiftid(map.get("liftid").toString());
		alarm.setSenderid(map.get("senderid").toString());
		alarm.setWarninglevel(Integer.parseInt(map.get("warninglevel").toString()));
		alarm.setAccx(Float.parseFloat(map.get("accx").toString()));
		alarm.setAccy(Float.parseFloat(map.get("accy").toString()));
		alarm.setAccz(Float.parseFloat(map.get("accz").toString()));
		alarm.setUnusualtime(new Timestamp((Long)map.get("unusualtime")));
		alarm.setRecordtime(new Timestamp((Long)map.get("recordtime")));
		alarm.setDealtag(Integer.parseInt(map.get("dealtag").toString()));
		
		return alarm;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
