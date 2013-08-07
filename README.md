OAuth_20
========

Samples for OAuth 2.0 Provider &amp; Client 

<<OAuth2 Provider & Consumer Sample입니다.>>

A. 환경

  - Oracle 10g Express
  - Java 1.6 + Spring 3.1 + Eclipse(indigo) + Maven + iBatis 2.0 + Tomcat 6.0(HTTP Port 8000)

B. 구성 요소

  - oauth2provider : 클라이언트
  - oauth2client : web server flow 클라이언트
  - oauth2client_agentflow : User Agent flow 클라이언트 

C. 설정해야 할 값

  - table 생성
    - DB는 oracle 10g express에 oauth2/oauth2 계정을 생성하여야 함.
    - oauth2provider의 src 디렉토리에 oauth2provider.sql 파일을 읽어 테이블 설치
      사용자 계정은 t1000, gdhong, arnold 세개의 계정(암호 동일)

  - 상수값 설정
    - net.oauth.v2 패키지의 OAuth2Constant클래스에서 상수값변경
      * USE_REFRESH_TOKEN : refresh token 기능을 사용할지 말지를 결정함.
      * AES_ENCRYPTION_KEY : 내부에서 토큰 생성시 사용할 AES 암호화 키 값
      * EXPIRES_IN_VALUE : refresh token 기능을 사용할 때 토큰의 유효기간(기본값:3600(초))
      
    - net.oauth.v2 패키지의 OAuth2Scope 클래스에서 상수값 변경
      * 조직에 따라 scope 상수값 설정(현재는 6개의 샘플 scope을 설정하였음)
      * resource 엔드포인트 url 별로 권한 설정(scope 지정)
      
D. endpoint
  - login & client app 등록
     * com.multi.oauth2.provider.view.controller.LoginController 클래스 참조
     * com.multi.oauth2.provider.view.controller.ClientController 클래스 참조
     
  - authorization
     * /oauth2provider/oauth2/auth
       - response_type 파라미터가 code일 경우는 web server flow
       - response_type 파라미터가 token일 경우는 user agent flow(Mobile App, Desktop포함)

  -  token  
     * /oauth2provider/oauth2/token
       - grant_type 파라미터가 authorization_code 인 경우는 server flow로 access token 발급
       - grant_type 파라미터가 refresh_token 인 경우는  access token을 갱신하게 됨.

  - protected resource
     * 이 샘플에서의 protected resource는 승인한 사용자의 계정 정보로 가정하였고, 
       endpoint는 /oauth2provider/resource/myinfo.do 이다.
     * 여러 protected resource에 대한 권한을 부여하기 위해 end point별 권한은 net.oauth.v2.OAuth2Scope 클래스의
       scopeUrlMap 필드에 Hashmap으로 작성한다
     * access token 정보 검증, scope 검증은 Interceptor(com.multi.oauth2.provider.util.Oauth2Interceptor)를 
       이용해 Controller 실행 전에 처리한다.
     * 예외 처리는 Controller 상에서 OAuth2Exception 을 발생시키면
       ExceptionResolver가 error 페이지로 이동시켜 OAuth2.0 spec에 따른 에러 코드와 메시지를 응답한다.
     * 클라이언트 앱이 user agent 타입으로 등록되었다면 Protected Resource 접근시 Cross Domain 문제를
       해결해줄 수 있도록 CORS(Cross Origin Resource Sharing)기법을 지원하도록 하였다.
  
  - 인증과정 또는 token 발급 과정에서 CSRF(Cross Site Request Forgery)공격에 대한 대응으로 
    OAuth2.0에서 recommended된 state 파라미터를 사용하였다.

E. 추가/개선할 사항..
  - OAuth 2.0 에서는 에러 발생시 WWW-Authenticate 헤더를 통해 응답하도록 하고 있으나
      Google, Facebook은 다른 방식을 사용하고 있다. 본 샘플은 facebook 스타일(?)로 작성하였다.


   If the protected resource request contains an invalid access token or
   is malformed, the resource server MUST include the HTTP
   "WWW-Authenticate" response header field.  The "WWW-Authenticate"
   header field uses the framework defined by [RFC2617] as follows:
 

     challenge       = "OAuth" RWS token-challenge

     token-challenge = realm
                       [ CS error ]
                       [ CS error-desc ]
                       [ CS error-uri ]
                       [ CS scope ]
                       [ CS 1#auth-param ]

     error           = "error" "=" <"> token <">
     error-desc      = "error_description" "=" quoted-string
     error-uri       = "error_uri" = <"> URI-Reference <">
     scope           = quoted-value /
                       <"> quoted-value *( 1*SP quoted-value ) <">
     quoted-value    = 1*quoted-char

   For example:

     HTTP/1.1 401 Unauthorized
     WWW-Authenticate: OAuth realm='Example Service', error='expired-token'

  - OAuth2.0 의 처리과정 중 Web Server flow 와 user agent flow만 처리하였다.
     * password credential과 client credential 방식은 작성하지 않았다. 대신 
       com.multi.oauth2.provider.view.controller.OAuth2Controller 클래스의 280번 라인에서 
       주석처리하여 향후 구현해야 함을 명시하였다.
     * refresh token을 사용할 것인지는 OAuth2Constant의 상수값을 변경하면 됨.
     * refresh token을 사용하지 않는 경우는 access token을 생성하지 않고 정해진 규칙에 따라
     	 생성하도록 하였음. --> 랜덤값으로 토큰을 생성하여 DB에 저장하도록 변경가능
  
  - 이 샘플에서는 redirect_uri 값을 비교하는 validation 과정을 거치므로 클라이언트 App을 등록할 때
    반드시 접근가능한 URL을 입력해야 함( localhost 허용)
     

F. 알림사항
   - 이 샘플은 잘 작동하지만 제대로된 설계없이 뚝딱거리면서 만들었음.
   - 따라서 잘 정리된 코드는 아님. 주석도 개발새발임.
   - 디버깅 목적으로 코드 사이사이에 콘솔 출력하는 코드가 많으니 알아서들 제거하고 사용해야 함.

   - oracle 10g 대신에 다른 DB 쓸거면 maven dependency, applicationContext.xml, oauth2.xml, 
     oauth2provider.sql 파일을 수정하여 쓰면 됨.
   - 한마디로 상업적인 목적으로 쓰기엔 조금 부끄러운 코드이므로 OAuth2 전반적 흐름을 이해하기 위한 
     용도로만 살펴봐야 함.
   
   
   
   
G. OAuth2.0 Client
  - oauth2Client 
    * Web Server flow로 처리하도록 만든 client임.
    * HTTP 통신을 위해 apache common의 HttpClent 클래스 사용
    * client 각 요소는 jsp로 간단히 작성
    * Settings.java 파일을 찾아 client_id, client_secret, 각각의 endpoint uri를 변경한 후 실행함.
     
  - oauth2client_agentflow
    * User Agent Flow 방식의 Client임.
    * html 파일로 작성
    * jQuery를 사용한 웹앱, webview를 통해 인증하고 access token을 획득하는 모바일앱. 이렇게 두가지의 경우
      이 코드를 참조할 수 있음.
    * index.html과 callback.html의 내부의 client_id, client_secret, 각각의 endpoint를 설정하고 실행함.
   
  - 실행에 앞서 client app 을 인증서버(oauth2provider)에 등록해야 함.
  



      
    
        

  
