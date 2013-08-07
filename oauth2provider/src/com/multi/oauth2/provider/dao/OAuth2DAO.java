package com.multi.oauth2.provider.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.multi.oauth2.provider.vo.ClientVO;
import com.multi.oauth2.provider.vo.TokenVO;
import com.multi.oauth2.provider.vo.UserVO;

@Repository
public class OAuth2DAO extends SqlMapClientDaoSupport {
	@Autowired
	public void setMySqlMapClient(SqlMapClient sqlMapClient) {
		this.setSqlMapClient(sqlMapClient);
	}

	//------로그인 시작
	public UserVO loginProcess(UserVO vo) throws Exception {
		return (UserVO) this.getSqlMapClient().queryForObject("login",
				vo);

	}

	public UserVO getUserInfo(UserVO vo) throws Exception {
		return (UserVO) this.getSqlMapClient().queryForObject(
				"userinfo", vo);
	}
	//------로그인 끝
	
	//----- Client 관리 시작
	public List<ClientVO> getClientList(UserVO vo) throws Exception {
		return (List<ClientVO>)this.getSqlMapClient().queryForList("clientlist", vo);
	}
	
	public ClientVO getClientOne(ClientVO vo) throws Exception {
		return (ClientVO)this.getSqlMapClient().queryForObject("clientone", vo);
	}
	
	public void deleteClient(ClientVO vo) throws Exception {
		this.getSqlMapClient().delete("deleteclient", vo.getClient_id());
	}
	
	public void insertClient(ClientVO vo) throws Exception {
		this.getSqlMapClient().insert("insertclient", vo);
	}
	//----- Client 관리 끝
	
	
	//------Token 관리 시작
	public void createToken(TokenVO vo) throws Exception {
		this.getSqlMapClient().insert("createToken", vo);
	}
	
	//refresh token으로 조회
	public TokenVO selectRefreshToken(TokenVO vo) throws Exception {
		return (TokenVO)this.getSqlMapClient().queryForObject("selectRefreshToken", vo);
	}
	//access token으로 조회
	public TokenVO selectToken(TokenVO vo) throws Exception {
		return (TokenVO)this.getSqlMapClient().queryForObject("selectToken", vo);
	}
	
	//code로 조회
	public TokenVO selectTokenByCode(TokenVO vo) throws Exception {
		return (TokenVO)this.getSqlMapClient().queryForObject("selectTokenByCode", vo);
	}
	
	public void updateAccessToken(TokenVO vo) throws Exception {
		this.getSqlMapClient().update("updateAccessToken", vo);
	}
	
	//expired 된 access token을 주기적으로 삭제하도록 할 것 
	public void deleteExpiredToken(TokenVO vo) throws Exception {
		this.getSqlMapClient().delete("deleteExpiredToken", vo);
	}
	
	//사용자가 명시적으로 로그아웃한 경우
	public void deleteToken(TokenVO vo) throws Exception {
		this.getSqlMapClient().delete("deleteToken", vo);
	}
	
	//오래된 토큰 제거
	public void deleteExpiredToken(long ms) throws Exception {
		this.getSqlMapClient().delete("deleteExpiredToken", ms);
	}
	//------Token 관리 끝
	
	
	
}
