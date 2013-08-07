package net.oauth.v2;

public class ResponseAccessTokenVO {
	private String access_token;
	private String token_type;
	private long expires_in;
	private String refresh_token;
	private String state;
	private long issued_at;
	
	public ResponseAccessTokenVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ResponseAccessTokenVO(String access_token, String token_type,
			long expires_in, String refresh_token, String state, long issued_at) {
		super();
		this.access_token = access_token;
		this.token_type = token_type;
		this.expires_in = expires_in;
		this.refresh_token = refresh_token;
		this.state = state;
		this.issued_at = issued_at;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getToken_type() {
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	public long getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(long expires_in) {
		this.expires_in = expires_in;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public long getIssued_at() {
		return issued_at;
	}

	public void setIssued_at(long issued_at) {
		this.issued_at = issued_at;
	}

	@Override
	public String toString() {
		return "ResponseAccessTokenVO [access_token=" + access_token
				+ ", token_type=" + token_type + ", expires_in=" + expires_in
				+ ", refresh_token=" + refresh_token + ", state=" + state
				+ ", issued_at=" + issued_at + "]";
	}

	
	
}	
