package com.multi.oauth2.provider.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.oauth.v2.OAuth2Constant;
import net.oauth.v2.OAuth2ErrorConstant;
import net.oauth.v2.OAuth2Exception;
import net.oauth.v2.OAuth2Scope;
import net.oauth.v2.OAuth2Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.multi.oauth2.provider.dao.OAuth2DAO;
import com.multi.oauth2.provider.vo.TokenVO;

@Controller
public class OAuth2Interceptor extends HandlerInterceptorAdapter {

	@Autowired
	private OAuth2DAO dao;

	@Autowired
	private OAuth2AccessTokenService tokenService;
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// TODO Auto-generated method stub
		String uri = request.getMethod() + " " + 
				request.getRequestURI().substring(request.getContextPath().length());
		
		String scope = OAuth2Scope.getScopeFromURI(uri);
		String authHeader = request.getHeader("Authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new OAuth2Exception(401, OAuth2ErrorConstant.UNAUTHORIZED_CLIENT);
		}
		

		String accessToken = OAuth2Util.parseBearerToken(authHeader);
		TokenVO tVO = null;
		if (OAuth2Constant.USE_REFRESH_TOKEN) {
			//1. 토큰이 존재하는지...
			TokenVO tVOTemp = new TokenVO();
			tVOTemp.setAccess_token(accessToken);
			
			try {
				tVO = dao.selectToken(tVOTemp);
			} catch (Exception e) {
				e.printStackTrace();
				throw new OAuth2Exception(500, OAuth2ErrorConstant.SERVER_ERROR);
			}
			
			if (tVO == null) {
				throw new OAuth2Exception(401, OAuth2ErrorConstant.UNAUTHORIZED_CLIENT);
			}
		} else {
			tVO = tokenService.validateAccessToken(accessToken);
		}
		
		//2. scope은 일치하는지...
		if (!OAuth2Scope.isUriScopeValid(scope, tVO.getScope())) {
			throw new OAuth2Exception(401, OAuth2ErrorConstant.INSUFFICIENT_SCOPE);
		}
		
		//3. 유효기간은 일치하는지..
		if (OAuth2Constant.USE_REFRESH_TOKEN) {
			long expires_in = tVO.getExpires_in();		//초단위
			long created_at = tVO.getCreated_at();		//밀리초
			long current_ts = OAuth2Util.getCurrentTimeStamp();	//현재의 timestamp
			if (current_ts > created_at + expires_in * 1000) {
				throw new OAuth2Exception(401, OAuth2ErrorConstant.EXPIRED_TOKEN);
			}
		}
		
		//4. client_type이 U(User Agent)이면 CORS(Cross Origin Resource Sharing) 허용 여부 고려
		//   redirect_uri 에서 포트 번호까지 만을 전달하면 됨.
		//   Access-Control-Allow-Origin : [Origin]
		if (tVO.getClient_type().equals("U")) {
			String referer = request.getHeader("Referer");
			
			int index = referer.indexOf("/", 7);
			String origin = referer.substring(0, index);
			
			System.out.println("### origin : " + origin);
			if (origin != null) {
				response.addHeader("Access-Control-Allow-Origin", origin);	
			}
		}
		
		//5. 너무 오래된 tbl_token 상의 token 정보들을 제거함.
		//이 예제에서 정한 기준은 access_token을 발급받은지 3일로 함.
		if (OAuth2Constant.USE_REFRESH_TOKEN) {
			dao.deleteExpiredToken(7*24*60*60*1000);
		}
		
		//9. 다른 문제가 없다면 controller로 Token 정보를 전달 
		request.setAttribute(OAuth2Constant.RESOURCE_TOKEN_NAME, tVO);
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		
		return true;
	}
	
}

