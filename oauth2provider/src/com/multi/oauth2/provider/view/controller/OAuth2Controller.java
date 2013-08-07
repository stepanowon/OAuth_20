package com.multi.oauth2.provider.view.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.oauth.v2.OAuth2Constant;
import net.oauth.v2.OAuth2ErrorConstant;
import net.oauth.v2.OAuth2Exception;
import net.oauth.v2.OAuth2Scope;
import net.oauth.v2.OAuth2Util;
import net.oauth.v2.RequestAccessTokenVO;
import net.oauth.v2.RequestAuthVO;
import net.oauth.v2.ResponseAccessTokenVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.multi.oauth2.provider.dao.OAuth2DAO;
import com.multi.oauth2.provider.util.OAuth2AccessTokenService;
import com.multi.oauth2.provider.vo.ClientVO;
import com.multi.oauth2.provider.vo.TokenVO;
import com.multi.oauth2.provider.vo.UserVO;

@Controller
public class OAuth2Controller {

	@Autowired
	private OAuth2DAO dao;
	
	@Autowired
	private OAuth2AccessTokenService tokenService;
	
//****Utility 메서드 시작
	//Token 값을 비롯한 timestamp 정보를 생성하여 테이블에 추가함.
	private TokenVO createTokenToTable(RequestAuthVO rVO, UserVO uVO, ClientVO cVO) throws OAuth2Exception {
		TokenVO tVO;
		try {
			//5.1 Access Token 랜덤하게 생성하여 TokenVO 생성하고 저장
			//TODO 목요일 : TOKEN 값 설정하고 DB에 저장 (sqlmap, dao, controller)
			tVO = new TokenVO();
			tVO.setClient_id(rVO.getClient_id());
			
			//승인한 사용자
			tVO.setUserid(uVO.getUserid());
			tVO.setToken_type(OAuth2Constant.TOKEN_TYPE_BEARER);
			tVO.setScope(rVO.getScope());
			
			//expires_in과 refresh_token의 사용 여부는 각 조직이 결정한다. 
			tVO.setExpires_in(OAuth2Constant.EXPIRES_IN_VALUE);
			tVO.setRefresh_token(OAuth2Util.generateToken());
			tVO.setCode(OAuth2Util.generateToken());
			tVO.setClient_type(cVO.getClient_type());
			
			tVO.setAccess_token(OAuth2Util.generateToken());
			//생성 시간을 계산을 용이하게 하기 위해 타임스탬프 형태로 저장
			long currentTimeStamp = OAuth2Util.getCurrentTimeStamp();
			tVO.setCreated_at(currentTimeStamp);
			tVO.setCreated_rt(currentTimeStamp);
			
			System.out.println(tVO);
			//tbl_Token테이블에 추가
			dao.createToken(tVO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new OAuth2Exception(401, OAuth2ErrorConstant.UNAUTHORIZED_CLIENT);
		}
		return tVO;
	}
	
	//Access 토큰을 refresh함.
	private TokenVO refreshToken(String clientRefreshToken) throws OAuth2Exception {
		TokenVO tempVO = new TokenVO();
		tempVO.setRefresh_token(clientRefreshToken);
		TokenVO tVO= null;
		try {
			tVO = dao.selectRefreshToken(tempVO);
		} catch(Exception e) {
			e.printStackTrace();
			throw new OAuth2Exception(500, OAuth2ErrorConstant.SERVER_ERROR);
		}
		
		if (tVO == null) {
			throw new OAuth2Exception(401, OAuth2ErrorConstant.INVALID_TOKEN);
		}
		
		tVO.setAccess_token(OAuth2Util.generateToken());
		tVO.setCreated_at(OAuth2Util.getCurrentTimeStamp());
		
		try {
			dao.updateAccessToken(tVO);
		} catch(Exception e) {
			e.printStackTrace();
			throw new OAuth2Exception(401, OAuth2ErrorConstant.INVALID_TOKEN);
		}
		
		return tVO;
	}
//****Utility 메서드 끝	
	
	@RequestMapping(value = "auth", method = RequestMethod.GET)
	public ModelAndView authorize(RequestAuthVO vo, HttpServletResponse response,
			HttpServletRequest request) throws OAuth2Exception {
		ModelAndView mav = new ModelAndView();

		HttpSession session = request.getSession();
		UserVO loginnedVO = (UserVO) session.getAttribute("userVO");

		//1. 현재 로그인한 상태인지 확인
		if (loginnedVO != null) {
			// 이미 로그인한 상황이면 승인버튼만 보여주도록 해야 함.
			mav.addObject("isloginned", true);
		} else {
			mav.addObject("isloginned", false);
		}
		
		System.out.println("## server flow 2.1");
		//2.1 전달된 client_id가 유효한  client_id인지 확인
		ClientVO clientVO1 = new ClientVO();
		clientVO1.setClient_id(vo.getClient_id());
		ClientVO cVO=null;
		try {
			cVO = dao.getClientOne(clientVO1);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OAuth2Exception(500,OAuth2ErrorConstant.SERVER_ERROR);
		}
		
		if (cVO == null) {
			throw new OAuth2Exception(401, OAuth2ErrorConstant.UNAUTHORIZED_CLIENT);
		}
		
		System.out.println("## server flow 2.2");
		System.out.println(vo.getResponse_type());
		
		//2.2 response_type이 code일 때는 client_secret이 일치하는지도 확인함.
		if (!vo.getResponse_type().equals(OAuth2Constant.RESPONSE_TYPE_CODE) && 
			!vo.getResponse_type().equals(OAuth2Constant.RESPONSE_TYPE_TOKEN)	) {
			throw new OAuth2Exception(401, OAuth2ErrorConstant.UNSUPPORTED_RESPONSE_TYPE);
		}
		
		System.out.println("## server flow 3");
		//3. 요청으로 전달된 scope가 client 등록시 포함된 scope에 존재하는지 여부 확인
		if (!OAuth2Scope.isScopeValid(vo.getScope(), cVO.getScope())) {
			throw new OAuth2Exception(400, OAuth2ErrorConstant.INVALID_SCOPE);
		}
		
		System.out.println("## server flow 4");
		//4. grant_type이 유효한지 확인
		String gt = vo.getResponse_type();
		if (!gt.equals(OAuth2Constant.RESPONSE_TYPE_CODE) 
				&& !gt.equals(OAuth2Constant.RESPONSE_TYPE_TOKEN)) {
			throw new OAuth2Exception(400,
					OAuth2ErrorConstant.UNSUPPORTED_RESPONSE_TYPE);
		}

		System.out.println("## server flow 5");
		//5 최종적으로 유효하다면 auth/auth.jsp 를 보여줌. 
		mav.addObject("requestAuthVO", vo);
		mav.addObject("clientVO", cVO);
		mav.setViewName("auth/auth");
		return mav;
	}
	
	//사용자가 승인 또는 거부 버튼을 클릭하는 경우
	@RequestMapping(value = "auth", method = RequestMethod.POST)
	public String authorizePost(Model model, RequestAuthVO rVO, HttpServletRequest request, HttpServletResponse response) throws OAuth2Exception {
		//0. request 값 얻어내기
		String isAllow = request.getParameter("isallow");
		String userid = request.getParameter("userid");
		String password = request.getParameter("password");
		
		//0.1 로그인한 사용자면 현재사용자와 해당 클라이언트를 위한 Token을 생성해야 함.
		//0.2 userid,password로 사용자 정보 조회--> 로그인처리--> 토큰 생성
		//0.3 사용자가 승인을 거부하였다면 사용자에게 알려주고 로그인화면으로 돌아감.
		if (!isAllow.equals("true")) {
			return "auth/auth_deny";
		}
		
		UserVO uVO = null;
		if (userid != null && password!=null) {
			UserVO uVOTemp = new UserVO(userid, password, "", 0);
			try {
				uVO = dao.loginProcess(uVOTemp);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "auth/auth.jsp";
			}
		} else if (request.getSession().getAttribute("userVO") != null){
			uVO = (UserVO)request.getSession().getAttribute("userVO");
		} else {
			throw new OAuth2Exception(401, OAuth2ErrorConstant.UNAUTHORIZED_CLIENT);
		}
		
		// 1. client_id 존재여부 확인
		ClientVO cVOTemp = new ClientVO();
		cVOTemp.setClient_id(rVO.getClient_id());
		System.out.println("## rVO ClientID : " + rVO.getClient_id());
		ClientVO cVO;
		try {
			cVO = dao.getClientOne(cVOTemp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new OAuth2Exception(400, OAuth2ErrorConstant.SERVER_ERROR);
		}
		
		if (cVO == null) {
			throw new OAuth2Exception(400, OAuth2ErrorConstant.UNAUTHORIZED_CLIENT);
		}
		
		// 2. scope 포함여부 확인
		if (!OAuth2Scope.isScopeValid(rVO.getScope(), cVO.getScope())) {
			throw new OAuth2Exception(400, OAuth2ErrorConstant.INVALID_SCOPE);
		}

		// 3. redirect_uri 일치여부 확인
		if (!rVO.getRedirect_uri().equals(cVO.getRedirect_uri())) {
			throw new OAuth2Exception(400, OAuth2ErrorConstant.NOT_MATCH_REDIRECT_URI);
		}

		//4. token, code 생성하여 테이블에 추가, 
		//   refresh token을 사용하지 않을 경우는 테이블에 저장된 값에서 code 필드값만 사용함
		TokenVO tVO = createTokenToTable(rVO, uVO, cVO);
		
		// 5. response_type 확인하고 code, token인 경우에 따라 각기 다른 흐름 처리
		String response_type = rVO.getResponse_type();
		String redirect = "";
		if (response_type.equals(OAuth2Constant.RESPONSE_TYPE_CODE)) {
			//6.1 4번 단계에서 생성된 값들 중 code 값을 이용해 redirect 함.
			// redirect_uri?code=XXXXXXXXX
			redirect = "redirect:"+ rVO.getRedirect_uri() + "?code=" + tVO.getCode();
			if (rVO.getState() != null) {
				redirect += "&state=" + rVO.getState(); 
			}
		} else if (response_type.equals(OAuth2Constant.RESPONSE_TYPE_TOKEN)) {
			//useragent 흐름
			ResponseAccessTokenVO tokenVO = null;

			if (OAuth2Constant.USE_REFRESH_TOKEN) {
				tokenVO =new ResponseAccessTokenVO(
						tVO.getAccess_token(), tVO.getToken_type(), 
						tVO.getExpires_in(), tVO.getRefresh_token(), rVO.getState(), tVO.getCreated_at());				
			} else {
				//OAuth2AccessToken 클래스 기능을 이용해 정해진 규칙에 의해 생성함.
				tokenVO = new ResponseAccessTokenVO(
						this.tokenService.generateAccessToken(cVO.getClient_id(), cVO.getClient_secret(), uVO.getUserid(), password), 
						OAuth2Constant.TOKEN_TYPE_BEARER, 0, null, rVO.getState(), tVO.getCreated_at());
				tokenVO.setExpires_in(0);
				tokenVO.setRefresh_token(null);
			}

			String acc = OAuth2Util.getAccessTokenToFormUrlEncoded(tokenVO);
			redirect = "redirect:" + rVO.getRedirect_uri() + "#" + acc;
		} else {
			throw new OAuth2Exception(400, OAuth2ErrorConstant.UNSUPPORTED_RESPONSE_TYPE);
		}
		
		return redirect;
	}
	
	
	//access_token 처리, refresh_token 처리
	//grant_type이 password 와 client_credentials 인 경우는 if 문 블럭만 작성하였음. 추후 작성해야 함.  
	@RequestMapping(value = "token")
	public String accessToken(RequestAccessTokenVO ratVO, Model model, HttpServletRequest request) throws OAuth2Exception {
		
		String json = "";
		
		//grant type이 무엇이냐에 따라 각기 다른 처리
		//password, client_credential은 미구현.. 추후 구현 요망됨.
		System.out.println("### token flow 1");
		System.out.println("### grant_type : " + ratVO.getGrant_type());
		
		if (ratVO.getGrant_type().equals(OAuth2Constant.GRANT_TYPE_AUTHORIZATION_CODE)) {
			ResponseAccessTokenVO resVO = accessTokenServerFlow(ratVO, request);
			json = OAuth2Util.getJSONFromObject(resVO);
		} else if (ratVO.getGrant_type().equals(OAuth2Constant.GRANT_TYPE_PASSWORD)) {
			//차후 구현할 것
			throw new OAuth2Exception(500, OAuth2ErrorConstant.UNSUPPORTED_RESPONSE_TYPE);
		} else if (ratVO.getGrant_type().equals(OAuth2Constant.GRANT_TYPE_CLIENT_CREDENTIALS)) {
			//차후 구현할 것
			throw new OAuth2Exception(500, OAuth2ErrorConstant.UNSUPPORTED_RESPONSE_TYPE);
		} else if (ratVO.getGrant_type().equals(OAuth2Constant.GRANT_TYPE_REFRESH_TOKEN)) {
			//refresh token 사용 여부에 따라 값이 달라짐.
			if (OAuth2Constant.USE_REFRESH_TOKEN) {
				ResponseAccessTokenVO resVO = refreshTokenFlow(ratVO, request);
				json = OAuth2Util.getJSONFromObject(resVO);
			} else {
				throw new OAuth2Exception(500, OAuth2ErrorConstant.UNSUPPORTED_RESPONSE_TYPE);
			}
		} else {
			throw new OAuth2Exception(500, OAuth2ErrorConstant.UNSUPPORTED_RESPONSE_TYPE);
		}
		model.addAttribute("json", json);
		return "json/json";
	}

	//grant_type이 authorization_code일 때 
	private ResponseAccessTokenVO accessTokenServerFlow(RequestAccessTokenVO ratVO, HttpServletRequest request) throws OAuth2Exception {
		
		
		//GET 방식일 때는 Client ID와 Client Secret은 Authorization Header를 통해 전달되어야 함.
		System.out.println("### token flow 2");
		if (request.getMethod().equalsIgnoreCase("GET")) {
			String authHeader = (String)request.getHeader("Authorization");
			if (authHeader == null || authHeader.equals("")) {
				throw new OAuth2Exception(400, OAuth2ErrorConstant.INVALID_PARAMETER);
			}
			//Basic 인증 헤더 파싱
			OAuth2Util.parseBasicAuthHeader(authHeader, ratVO);
		}
		
		//1. ClientID, Secret 모두 전달되었는지 여부 --> 존재 여부 확인
		System.out.println("### token flow 3");
		if (ratVO.getClient_id() ==null || ratVO.getClient_secret() == null ) {
			throw new OAuth2Exception(400, OAuth2ErrorConstant.INVALID_PARAMETER);
		}
		
		ClientVO cVOTemp = new ClientVO();
		cVOTemp.setClient_id(ratVO.getClient_id());
		cVOTemp.setClient_secret(ratVO.getClient_secret());
		ClientVO cVO = null;
		try {
			cVO = dao.getClientOne(cVOTemp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OAuth2Exception(500, OAuth2ErrorConstant.SERVER_ERROR);
		}
		
		//1.2 client 존재 여부 확인
		System.out.println("### token flow 4");
		if (cVO == null) {
			throw new OAuth2Exception(401, OAuth2ErrorConstant.UNAUTHORIZED_CLIENT);
		}
		
		//2. redirect_uri 일치 여부 확인
		System.out.println("### token flow 5");
		if (!ratVO.getRedirect_uri().equals(cVO.getRedirect_uri())) {
			throw new OAuth2Exception(400, OAuth2ErrorConstant.NOT_MATCH_REDIRECT_URI);
		}
		
		//3. code값 일치 여부 확인
		System.out.println("### token flow 6");
		if (ratVO.getCode() == null) {
			throw new OAuth2Exception(400, OAuth2ErrorConstant.INVALID_PARAMETER);
		}
		
		TokenVO tVOTemp = new TokenVO();
		tVOTemp.setCode(ratVO.getCode());
		TokenVO tVO = null;
		try {
			tVO = dao.selectTokenByCode(tVOTemp);
		} catch(Exception e) {
			e.printStackTrace();
			throw new OAuth2Exception(500, OAuth2ErrorConstant.SERVER_ERROR);
		}
		
		if (tVO == null) {
			throw new OAuth2Exception(400, OAuth2ErrorConstant.INVALID_CODE);
		}
		
		//4. expire되었는지 여부 확인, refresh token 사용 여부에 따라 결정함.
		if (OAuth2Constant.USE_REFRESH_TOKEN) {
			System.out.println("### token flow 7");
			long expires = tVO.getCreated_at() + tVO.getExpires_in();
			if (System.currentTimeMillis() > expires) {
				//토큰 생성되고 한참 뒤에 code로 access token을 요청하는 것은 금지
				throw new OAuth2Exception(400, OAuth2ErrorConstant.EXPIRED_TOKEN);
			}
		}	
		
		//5. 전달받은 state값 그대로 전달(CSRF 공격 방지용)
		//6. ResponseAccessToken객체 생성 --> json 포맷 응답
		System.out.println("### token flow 9");
		ResponseAccessTokenVO resVO = new ResponseAccessTokenVO();

		resVO.setIssued_at(tVO.getCreated_at());
		resVO.setState(ratVO.getState());
		resVO.setToken_type(tVO.getToken_type());
		if (OAuth2Constant.USE_REFRESH_TOKEN) {
			resVO.setAccess_token(tVO.getAccess_token());
			resVO.setExpires_in(tVO.getExpires_in());
			resVO.setRefresh_token(tVO.getRefresh_token());
		} else {
			//6.1. password를 확보하기 위해 UserVO 객체 획득
			//     ResponsToken을 생성하고 나면 token 테이블의 레코드 삭제
			UserVO uVOTemp = new UserVO();
			uVOTemp.setUserid(tVO.getUserid());
			UserVO uVO = null;
			try {
				uVO = dao.getUserInfo(uVOTemp);
			} catch (Exception e) {
				e.printStackTrace();
				throw new OAuth2Exception(500, OAuth2ErrorConstant.SERVER_ERROR);
			}
			
			if (uVO == null) {
				throw new OAuth2Exception(500, OAuth2ErrorConstant.INVALID_USER);
			}
			
			//token 테이블 레코드 삭제
			try {
				dao.deleteToken(tVO);
			} catch (Exception e) {
				e.printStackTrace();
				throw new OAuth2Exception(500, OAuth2ErrorConstant.SERVER_ERROR);
			}
			
			resVO.setAccess_token(this.tokenService.generateAccessToken(cVO.getClient_id(), cVO.getClient_secret(), uVO.getUserid(), uVO.getPassword()));
		}

		return resVO;
		
	}
	
	//grant_type이 authorization_code일 때 
	private ResponseAccessTokenVO refreshTokenFlow(RequestAccessTokenVO ratVO, HttpServletRequest request) throws OAuth2Exception {
		//1. 전달된 refresh Token과 
		//GET 방식일 때는 Client ID와 Client Secret은 Authorization Header를 통해 전달되어야 함.
		if (request.getMethod().equalsIgnoreCase("GET")) {
			String authHeader = (String)request.getHeader("Authorization");
			if (authHeader == null || authHeader.equals("")) {
				throw new OAuth2Exception(400, OAuth2ErrorConstant.INVALID_PARAMETER);
			}
			//Basic 인증 헤더 파싱
			OAuth2Util.parseBasicAuthHeader(authHeader, ratVO);
		}
		
		//2. ClientID, Secret 모두 전달되었는지 여부 --> 존재 여부 확인
		if (ratVO.getClient_id() ==null || ratVO.getClient_secret() == null ) {
			throw new OAuth2Exception(400, OAuth2ErrorConstant.INVALID_PARAMETER);
		}

		//3. clientID 와 client_secret의 일치여부
		ClientVO cVOTemp = new ClientVO();
		cVOTemp.setClient_id(ratVO.getClient_id());
		ClientVO cVO = null;
		try {
			cVO = dao.getClientOne(cVOTemp);
		} catch(Exception e) {
			throw new OAuth2Exception(500, OAuth2ErrorConstant.SERVER_ERROR);
		}
		
		if (cVO == null) {
			throw new OAuth2Exception(500, OAuth2ErrorConstant.UNAUTHORIZED_CLIENT);
		}
		
		if (ratVO.getClient_secret() != null && !cVO.getClient_secret().equals(ratVO.getClient_secret())) {
			throw new OAuth2Exception(500, OAuth2ErrorConstant.UNAUTHORIZED_CLIENT);
		}
		
		//4. refresh token의 일치 여부
		if (ratVO.getRefresh_token() == null) {
			throw new OAuth2Exception(400, OAuth2ErrorConstant.INVALID_PARAMETER);
		}
		
		TokenVO tVOTemp = new TokenVO();
		tVOTemp.setRefresh_token(ratVO.getRefresh_token());
		TokenVO tVO = null;
		try {
			tVO = dao.selectRefreshToken(tVOTemp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OAuth2Exception(500, OAuth2ErrorConstant.SERVER_ERROR);
		}
		
		if (tVO == null) {
			throw new OAuth2Exception(400, OAuth2ErrorConstant.INVALID_TOKEN);
		}
		
		//5. TokenVO의 accessToken 갱신 --> DB 업데이트 --> 
		// --> refreshToken 값 없이  ResponseAccessTokenVO객체 생성 --> JSON 포맷으로 응답
		tVO.setAccess_token(OAuth2Util.generateToken());
		tVO.setCreated_at(OAuth2Util.getCurrentTimeStamp());
		try {
			dao.updateAccessToken(tVO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OAuth2Exception(500, OAuth2ErrorConstant.SERVER_ERROR);
		}

		ResponseAccessTokenVO resVO = new ResponseAccessTokenVO(
				tVO.getAccess_token(), tVO.getToken_type(), 
				tVO.getExpires_in(), null, ratVO.getState(), tVO.getCreated_at());
		
		
		return resVO;
	}
}
