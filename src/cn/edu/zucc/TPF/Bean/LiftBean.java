package cn.edu.zucc.TPF.Bean;

import java.io.Serializable;

public class LiftBean implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String liftid;
    private String pwd;
    private String liftname;

    //    private float accxLimit;
//    private float accyLimit;
//    private float acczLimit;
    public LiftBean() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @return the liftid
     */
    public String getLiftid() {
        return liftid;
    }

    /**
     * @param liftid the liftid to set
     */
    public void setLiftid(String liftid) {
        this.liftid = liftid;
    }

    /**
     * @return the pwd
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * @param pwd the pwd to set
     */
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    /**
     * @return the liftname
     */
    public String getLiftname() {
        return liftname;
    }

    /**
     * @param liftname the liftname to set
     */
    public void setLiftname(String liftname) {
        this.liftname = liftname;
    }
    /**
     * @return the accxLimit
     */
//	public float getAccxLimit() {
//		return accxLimit;
//	}
//	/**
//	 * @param accxLimit the accxLimit to set
//	 */
//	public void setAccxLimit(float accxLimit) {
//		this.accxLimit = accxLimit;
//	}
//	/**
//	 * @return the accyLimit
//	 */
//	public float getAccyLimit() {
//		return accyLimit;
//	}
//	/**
//	 * @param accyLimit the accyLimit to set
//	 */
//	public void setAccyLimit(float accyLimit) {
//		this.accyLimit = accyLimit;
//	}
//	/**
//	 * @return the acczLimit
//	 */
//	public float getAcczLimit() {
//		return acczLimit;
//	}
//	/**
//	 * @param acczLimit the acczLimit to set
//	 */
//	public void setAcczLimit(float acczLimit) {
//		this.acczLimit = acczLimit;
//	}

}
