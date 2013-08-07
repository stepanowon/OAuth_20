package com.multi.oauth2.provider.view.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.multi.oauth2.provider.dao.OAuth2DAO;
import com.multi.oauth2.provider.vo.UserVO;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

	@Autowired
	private OAuth2DAO dao;
	
	@RequestMapping(value="login.do", method=RequestMethod.GET)
	public String login() {
		return "login/login";
	}
	
	@RequestMapping(value="login.do", method=RequestMethod.POST)
	public String login(HttpSession session, UserVO vo) throws Exception {
		System.out.println(vo);
		UserVO vo2 = dao.loginProcess(vo);
		if (vo2 == null) { 
			return "redirect:login.do";
		} else {
			session.setAttribute("userVO", vo2);
			return "redirect:clientlist.do";
		}
	}
}
