package com.multi.oauth2.provider.vo;

import java.util.Date;
import java.util.List;

public class ClientVO {
	private String client_id;
	private String client_secret;
	private String userid;
	private String client_name;
	private String description;
	private String client_url;
	private String client_type;
	private List<String> scopes;
	private String scope;
	private String redirect_uri;
	private Date regdate;
	
	public ClientVO() {
		super();
		// TODO Auto-generated constructor stub
	}


	public ClientVO(String client_id, String client_secret, String userid,
			String client_name, String description, String client_url,
			String client_type, String scope, String redirect_uri, Date regdate) {
		super();
		this.client_id = client_id;
		this.client_secret = client_secret;
		this.userid = userid;
		this.client_name = client_name;
		this.description = description;
		this.client_url = client_url;
		this.client_type = client_type;
		this.scope = scope;
		this.redirect_uri = redirect_uri;
		this.regdate = regdate;
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

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getClient_name() {
		return client_name;
	}

	public void setClient_name(String client_name) {
		this.client_name = client_name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getClient_url() {
		return client_url;
	}

	public void setClient_url(String client_url) {
		this.client_url = client_url;
	}

	public String getClient_type() {
		return client_type;
	}

	public void setClient_type(String client_type) {
		this.client_type = client_type;
	}
	
	public List<String> getScopes() {
		return scopes;
	}


	public void setScopes(List<String> scopes) {
		this.scopes = scopes;
	}


	public String getScope() {
		return scope;
	}


	public void setScope(String scope) {
		this.scope = scope;
	}


	public String getRedirect_uri() {
		return redirect_uri;
	}

	public void setRedirect_uri(String redirect_uri) {
		this.redirect_uri = redirect_uri;
	}

	public Date getRegdate() {
		return regdate;
	}

	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}


	@Override
	public String toString() {
		return "ClientVO [client_id=" + client_id + ", client_secret="
				+ client_secret + ", userid=" + userid + ", client_name="
				+ client_name + ", description=" + description
				+ ", client_url=" + client_url + ", client_type=" + client_type
				+ ", scopes=" + scopes + ", redirect_uri=" + redirect_uri
				+ ", regdate=" + regdate + "]";
	}


	
	
	
}
