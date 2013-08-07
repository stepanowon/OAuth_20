package com.multi.oauth2client;

import java.util.HashMap;

public class TestClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("a", "1");
		map.put("name", "ȫ�浿");
		map.put("tel", "010-222-3333");
		map.put("client_id", "asasas");
		map.put("client_secret", "afsffdf");
		
		System.out.println(Settings.getParamString(map, true)); 
	}

}
