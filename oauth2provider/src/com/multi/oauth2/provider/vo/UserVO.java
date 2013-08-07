package com.multi.oauth2.provider.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="user")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserVO {
	private String userid;
	private String password;
	private String username;
	private long userno;
	
	public UserVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserVO(String userid, String password, String username, long userno) {
		super();
		this.userid = userid;
		this.password = password;
		this.username = username;
		this.userno = userno;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getUserno() {
		return userno;
	}

	public void setUserno(long userno) {
		this.userno = userno;
	}

	@Override
	public String toString() {
		return "UserVO [userid=" + userid + ", password=" + password
				+ ", username=" + username + ", userno=" + userno + "]";
	}

	
}
