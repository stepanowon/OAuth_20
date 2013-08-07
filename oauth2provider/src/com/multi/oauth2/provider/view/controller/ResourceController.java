package com.multi.oauth2.provider.view.controller;

import javax.servlet.http.HttpServletRequest;

import net.oauth.v2.OAuth2Constant;
import net.oauth.v2.OAuth2ErrorConstant;
import net.oauth.v2.OAuth2Exception;
import net.oauth.v2.OAuth2Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.multi.oauth2.provider.dao.OAuth2DAO;
import com.multi.oauth2.provider.vo.ClientVO;
import com.multi.oauth2.provider.vo.TokenVO;
import com.multi.oauth2.provider.vo.UserVO;

/*
 * 현재 예제는 리소스 서버를 인증서버와 함께 만든 형태이다.
 * 리소스 서버 기능을 분리하랴할 때는 net.oauth.v2 아래의 패키지를 리소스 서버로 배포하여 
 * 사용하면 된다.
 * 권한(scope) 확인 기능은 Spring Interceptor 기능을 이용하는데, 
 * 이때 tbl_token 테이블에 접근이 필요하므로 Sql Map과 dao 클래스도 함께 배포되어야 한다. 
 */

@Controller
public class ResourceController {
	
	@Autowired
	private OAuth2DAO dao;
	
	@RequestMapping(value="resource/myinfo.do", method=RequestMethod.GET)
	public String getMyInfo(Model model, HttpServletRequest request) throws OAuth2Exception {
		
		TokenVO tVO = (TokenVO)request.getAttribute(OAuth2Constant.RESOURCE_TOKEN_NAME);

		//1. User 정보
		UserVO uVOTemp = new UserVO();
		uVOTemp.setUserid(tVO.getUserid());
		UserVO uVO = null;
		try {
			uVO = dao.getUserInfo(uVOTemp);
		} catch(Exception e) {
			e.printStackTrace();
			throw new OAuth2Exception(500, OAuth2ErrorConstant.SERVER_ERROR);
		}
		uVO.setPassword(null);		//의도적으로 null값을...
		
		//2. Client App 정보
		//이 예제에서는 이정보를 이용하지 않음. 콘솔로 확인만!!
		ClientVO cVOTemp = new ClientVO();
		cVOTemp.setClient_id(tVO.getClient_id());
		
		ClientVO cVO = null;
		try {
			cVO = dao.getClientOne(cVOTemp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OAuth2Exception(500, OAuth2ErrorConstant.SERVER_ERROR);
		}
		String jsonClient = OAuth2Util.getJSONFromObject(cVO);
		System.out.println("##Client : " + jsonClient);
		
				
		String jsonUser = OAuth2Util.getJSONFromObject(uVO);
		System.out.println("##user : " + jsonUser);
		model.addAttribute("json", jsonUser);
		
		return "json/json";
	}
}
