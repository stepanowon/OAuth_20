package com.multi.oauth2client;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.sun.org.apache.xml.internal.security.utils.Base64;

public class OAuth2ClientUtil {
	private static ObjectMapper mapper;

	static {
		mapper = new ObjectMapper();
		// 1.9.x 버전 이상
		mapper.setSerializationInclusion(Inclusion.NON_NULL);
		mapper.setSerializationInclusion(Inclusion.NON_EMPTY);
	}

	public static String getJSONFromObject(Object obj) {
		try {
			StringWriter sw = new StringWriter(); // serialize
			mapper.writeValue(sw, obj);
			sw.close();

			return sw.getBuffer().toString();
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T getObjectFromJSON(String json, Class<T> classOfT) {
		try {
			StringReader sr = new StringReader(json);
			return mapper.readValue(json.getBytes("UTF-8"), classOfT);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// hex to byte[]
	public static byte[] hexToBinary(String hex) {
		if (hex == null || hex.length() == 0) {
			return null;
		}

		byte[] ba = new byte[hex.length() / 2];
		for (int i = 0; i < ba.length; i++) {
			ba[i] = (byte) Integer
					.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return ba;
	}

	// byte[] to hex
	public static String binaryToHex(byte[] ba) {
		if (ba == null || ba.length == 0) {
			return null;
		}

		StringBuffer sb = new StringBuffer(ba.length * 2);
		String hexNumber;
		for (int x = 0; x < ba.length; x++) {
			hexNumber = "0" + Integer.toHexString(0xff & ba[x]);

			sb.append(hexNumber.substring(hexNumber.length() - 2));
		}
		return sb.toString();
	}

	public static String generateRandomState() {

		SecureRandom secureRandom;
		try {
			secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(secureRandom.generateSeed(256));
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			byte[] dig = digest.digest((secureRandom.nextLong() + "")
					.getBytes());
			return binaryToHex(dig).substring(0, 5);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	
	// URL Encoding Utility
	public static String encodeURIComponent(String s) {
		String result;

		try {
			result = URLEncoder.encode(s, "UTF-8").replaceAll("\\+", "%20")
					.replaceAll("\\%21", "!").replaceAll("\\%27", "'")
					.replaceAll("\\%28", "(").replaceAll("\\%29", ")")
					.replaceAll("\\%7E", "~");
		} catch (UnsupportedEncodingException e) {
			result = s;
		}

		return result;
	}

	public static String decodeURIComponent(String s) {
		if (s == null) {
			return null;
		}
		String result = null;
		try {
			result = URLDecoder.decode(s, "UTF-8");
		}
		// This exception should never occur.
		catch (UnsupportedEncodingException e) {
			result = s;
		}
		return result;
	}

	
	// 용도 : GET 방식으로 access token을 요청할 때 사용
	// grant_type이 password인 경우(Password Credential 방식인 경우 Access Token 요청할 때)
	//
	// Authorization : Basic XXXXXXXXXX
	public static String generateBasicAuthHeaderString(String client_id, String client_secret) {
		try {
			String base = "";
			if (client_secret == null
					|| client_secret.equals("")) {
				base = encodeURIComponent(client_id);
			} else {
				base = encodeURIComponent(client_id) + ":"
						+ encodeURIComponent(client_secret);
			}

			return "Basic " + Base64.encode(base.getBytes("UTF-8"));

		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	public static String generateBearerTokenHeaderString(String access_token) {
			return "Bearer " + access_token;
	}

	

}
