package net.oauth.v2;

public class RequestBaseVO {
	private String client_id;
	private String client_secret;
	private String redirect_uri;
	
	public RequestBaseVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RequestBaseVO(String client_id, String client_secret,
			String redirect_uri) {
		super();
		this.client_id = client_id;
		this.client_secret = client_secret;
		this.redirect_uri = redirect_uri;
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getClient_secret() {
		return client_secret;
	}

	public void setClient_secret(String client_secret) {
		this.client_secret = client_secret;
	}

	public String getRedirect_uri() {
		return redirect_uri;
	}

	public void setRedirect_uri(String redirect_uri) {
		this.redirect_uri = redirect_uri;
	}
	
	
}
