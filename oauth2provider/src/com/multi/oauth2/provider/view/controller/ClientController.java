package com.multi.oauth2.provider.view.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import net.oauth.v2.OAuth2ErrorConstant;
import net.oauth.v2.OAuth2Exception;
import net.oauth.v2.OAuth2Scope;
import net.oauth.v2.OAuth2Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.multi.oauth2.provider.dao.OAuth2DAO;
import com.multi.oauth2.provider.vo.ClientVO;
import com.multi.oauth2.provider.vo.UserVO;

@Controller
public class ClientController {

	@Autowired
	private OAuth2DAO dao;
	
	@RequestMapping(value="clientlist.do", method=RequestMethod.GET)
	public String getClientList(HttpSession session, Model model) throws Exception {
		UserVO loginVO = (UserVO)session.getAttribute("userVO");
		if (loginVO == null) {
			return "redirect:login.do";
		}
		 
		List<ClientVO> list = dao.getClientList(loginVO);
		model.addAttribute("list", list);
		return "client/list";
	}
	
	@RequestMapping(value="detailclient.do", method=RequestMethod.GET)
	public String detailClient(Model model, HttpSession session,
			@RequestParam("client_id") String client_id) throws Exception {
		UserVO loginVO = (UserVO)session.getAttribute("userVO");
		if (loginVO == null) {
			return "redirect:login.do";
		}

		ClientVO vo = new ClientVO();
		vo.setClient_id(client_id);
		ClientVO vo2 = dao.getClientOne(vo);
		model.addAttribute("clientVO", vo2);
		return "client/detail";
	}
	
	@RequestMapping(value="deleteclient.do", method=RequestMethod.GET)
	public String deleteClient(HttpSession session, 
			@RequestParam("client_id") String client_id) throws Exception {
		UserVO loginVO = (UserVO)session.getAttribute("userVO");
		if (loginVO == null) {
			return "redirect:login.do";
		}

		
		ClientVO vo = new ClientVO();
		vo.setClient_id(client_id);
		dao.deleteClient(vo);
		
		return "redirect:clientlist.do";
	}
	
	@RequestMapping(value="insertclient.do" , method=RequestMethod.GET) 
	public String insertView(HttpSession session) {
		UserVO loginVO = (UserVO)session.getAttribute("userVO");
		if (loginVO == null) {
			return "redirect:login.do";
		}

		return "client/insert";
	}

	@RequestMapping(value="insertclient.do" , method=RequestMethod.POST)
	public String insertClient(ClientVO vo,HttpSession session) throws Exception {
		UserVO loginVO = (UserVO)session.getAttribute("userVO");
		if (loginVO == null) {
			return "redirect:login.do";
		}
		
		//컬렉션 값을 csv 형태로 변환
		String strScope = "";
		for (int i=0; i < vo.getScopes().size(); i++) {
			if (i>0)   strScope+=",";
			strScope+=vo.getScopes().get(i);
		}
		
		System.out.println(strScope);
		//scope의 유효성 여부 확인. 유효하지 않으면 400 응답
		if (!OAuth2Scope.isScopeExistInMap(strScope)) {
			throw new OAuth2Exception(400, OAuth2ErrorConstant.INVALID_SCOPE);
		}
		vo.setScope(strScope);
		
		vo.setUserid(loginVO.getUserid());
		OAuth2Util.generateClientIDSecret(vo);
		dao.insertClient(vo);
		
		return "redirect:clientlist.do";
	}
	
}
