package cn.edu.zucc.TPF.Bean;

import java.io.Serializable;


public class UserBean implements Serializable{
	private static final long serialVersionUID = 1L;
    private String id;
    private String pwd;
    private String address;
	public UserBean() {
		// TODO Auto-generated constructor stub
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

}
