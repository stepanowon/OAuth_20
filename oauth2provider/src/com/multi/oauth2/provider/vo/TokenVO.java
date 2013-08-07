//CREATE TABLE tbl_token 
//(
//	client_id      		varchar2(100)  NOT NULL,		
//	userid				varchar2(20) NOT NULL,
//	access_token		varchar2(200) unique,
//	refresh_token		varchar2(200) unique,
//	token_type			varchar2(30),				--bearer,jwt,mac
//	scope				varchar2(100),				--권한 : 읽기,읽기쓰기 등을 공백으로 구분하여 사용하는 것이 표준
//	created_at			number(30),						--access token 생성시의 timestamp
//	created_rt			number(30),						--refresh token 생성시의 timestamp
//	expires_in			number(30)						--토큰 생성시에 부여하는 유효기간 3600
//);

package com.multi.oauth2.provider.vo;

public class TokenVO {
	private String client_id;
	private String userid;
	private String access_token;
	private String refresh_token;
	private String token_type;
	private String scope;
	private String code;
	private String state;
	private String client_type;
	private long created_at;
	private long created_rt;
	private long expires_in;
	
	public TokenVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TokenVO(String client_id, String userid, String access_token,
			String refresh_token, String token_type, String scope, String client_type,
			long created_at, long created_rt, long expires_in) {
		super();
		this.client_id = client_id;
		this.userid = userid;
		this.access_token = access_token;
		this.refresh_token = refresh_token;
		this.token_type = token_type;
		this.scope = scope;
		this.client_type = client_type;
		this.created_at = created_at;
		this.created_rt = created_rt;
		this.expires_in = expires_in;
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	public String getToken_type() {
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public long getCreated_at() {
		return created_at;
	}

	public void setCreated_at(long created_at) {
		this.created_at = created_at;
	}

	public long getCreated_rt() {
		return created_rt;
	}

	public void setCreated_rt(long created_rt) {
		this.created_rt = created_rt;
	}

	public long getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(long expires_in) {
		this.expires_in = expires_in;
	}
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getClient_type() {
		return client_type;
	}

	public void setClient_type(String client_type) {
		this.client_type = client_type;
	}

	@Override
	public String toString() {
		return "TokenVO [client_id=" + client_id + ", userid=" + userid
				+ ", access_token=" + access_token + ", refresh_token="
				+ refresh_token + ", token_type=" + token_type + ", scope="
				+ scope + ", code=" + code + ", state=" + state
				+ ", client_type=" + client_type + ", created_at=" + created_at
				+ ", created_rt=" + created_rt + ", expires_in=" + expires_in
				+ "]";
	}
	
	
	
}
