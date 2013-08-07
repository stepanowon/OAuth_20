package com.multi.oauth2client;

//{ "access_token":"36cce240502d310e804609433f07e1e81e7b6738",
//  "token_type":"bearer","expires_in":3600,
//  "refresh_token":"e0cc85bae752910ed9f21b8d7ccedc3f9f729e55",
//  "issued_at":1375700647394 }

public class AccessTokenVO {
	private String refresh_token;
	private String access_token;
	private long expires_in;
	private long issued_at;
	private String token_type;
	private String state;
	

	public AccessTokenVO(String refresh_token, String access_token,
			long expires_in, long issued_at, String token_type, String state) {
		super();
		this.refresh_token = refresh_token;
		this.access_token = access_token;
		this.expires_in = expires_in;
		this.issued_at = issued_at;
		this.token_type = token_type;
		this.state = state;
	}

	public AccessTokenVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public long getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(long expires_in) {
		this.expires_in = expires_in;
	}

	public long getIssued_at() {
		return issued_at;
	}

	public void setIssued_at(long issued_at) {
		this.issued_at = issued_at;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getToken_type() {
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	@Override
	public String toString() {
		return "AccessTokenVO [refresh_token=" + refresh_token
				+ ", access_token=" + access_token + ", expires_in="
				+ expires_in + ", issued_at=" + issued_at + ", state=" + state
				+ "]";
	}
	
	
}
