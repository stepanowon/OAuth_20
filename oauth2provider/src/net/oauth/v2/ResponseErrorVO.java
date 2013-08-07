package net.oauth.v2;

public class ResponseErrorVO {
	private String error;
	private String error_description;
	private String state;
	
	public ResponseErrorVO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ResponseErrorVO(String error, String error_description, String state) {
		super();
		this.error = error;
		this.error_description = error_description;
		this.state = state;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getError_description() {
		return error_description;
	}
	public void setError_description(String error_description) {
		this.error_description = error_description;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
}
