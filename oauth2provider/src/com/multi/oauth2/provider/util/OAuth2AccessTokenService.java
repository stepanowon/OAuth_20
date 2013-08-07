/* access token을 DB에 저장하지 않고 규칙에 의해 생성/검증하는 방법
 * 
 * 1. access token 생성 방법(나름대로 잡은 룰)
 * Enc(user_id&client_id)_Hash(password&client_secret)
 * 
 * 2. 검증시에는 거꾸로 수행
 *   가. _ 으로 split
 *   나. 앞부분을 복호화하고 &로 split한뒤 userid, clientid 확보
 *   다. 데이터베이스에서 password, client_secret을 알아낸 다음 사후해시 생성
 *   라. 사후 해시와 전달된 access_token 상의 정보 비교 --> 검증 완료
 */

package com.multi.oauth2.provider.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import net.oauth.v2.OAuth2Constant;
import net.oauth.v2.OAuth2Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.multi.oauth2.provider.dao.OAuth2DAO;
import com.multi.oauth2.provider.vo.ClientVO;
import com.multi.oauth2.provider.vo.TokenVO;
import com.multi.oauth2.provider.vo.UserVO;


@Service("TokenService")
public class OAuth2AccessTokenService {

	
	@Autowired
	private OAuth2DAO dao = new OAuth2DAO();
	//암호화
    public String encrypt(String message) throws Exception {

        // use key coss2
        SecretKeySpec skeySpec = new SecretKeySpec(OAuth2Constant.AES_ENCRYPTION_KEY.getBytes("UTF-8"), "AES");

        // Instantiate the cipher
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        byte[] encrypted = cipher.doFinal(message.getBytes());
        return OAuth2Util.binaryToHex(encrypted);
    }
    
    //복호화
    public String decrypt(String encrypted) throws Exception {

        SecretKeySpec skeySpec = new SecretKeySpec(OAuth2Constant.AES_ENCRYPTION_KEY.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] original = cipher.doFinal(OAuth2Util.hexToBinary(encrypted));
        String originalString = new String(original);
        return originalString;
    }
    
    public String generateAccessToken(String client_id, String client_secret, String userid, String password) {
    	try {
    		String prev = encrypt(userid+"&"+client_id);
    		String next = OAuth2Util.getHmacSha256(password+"&"+client_secret);
    		next = next.substring(0, 16);
    		
    		System.out.println("### TOKEN BASE GEN : " + userid+"&"+client_id + ", " + password+"&"+client_secret);
    		System.out.println("### HASH : " + next );

    		return prev+"_"+next;
    	} catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }
	
    //AccessToken을 검증한 후 TokenVO객체를 리턴(client_id, userid 포함) 
    public TokenVO validateAccessToken(String access_token) {    
    	try {
    		String[] temp = access_token.split("_");
    		//클라이언트 해시값
    		String clientHash = temp[1];
    		
    		String base = decrypt(temp[0]);
    		temp = base.split("&");
    		String userid = temp[0];
    		String client_id = temp[1];
    		
    		ClientVO cVOTemp = new ClientVO();
    		cVOTemp.setClient_id(client_id);
    		ClientVO cVO = dao.getClientOne(cVOTemp);
    		
    		UserVO uVOTemp = new UserVO();
    		uVOTemp.setUserid(userid);
    		UserVO uVO = dao.getUserInfo(uVOTemp);

    		System.out.println("### TOKEN BASE GEN : " + userid+"&"+client_id + ", " + uVO.getPassword()+"&"+cVO.getClient_secret());

    		//해시값 검증
    		base = uVO.getPassword() + "&" + cVO.getClient_secret();
    		//사후 해시
    		String hash = OAuth2Util.getHmacSha256(base).substring(0, 16);
    		
    		System.out.println(hash + "<><>" + clientHash);
    		
    		if (!clientHash.equals(hash)) {
    			throw new Exception();
    		}
    		
    		TokenVO tVO = null;
    		if (uVO != null && cVO != null) {
    			tVO = new TokenVO();
    			tVO.setClient_id(client_id);
    			tVO.setScope(cVO.getScope());
    			tVO.setAccess_token(access_token);
    			tVO.setClient_type(cVO.getClient_type());
    			tVO.setUserid(userid);
    		}
    		
    		return tVO;
    	}catch(Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
	// 테스트 코드
	public static void main(String[] args) {

		
	}


}
