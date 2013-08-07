OAuth_20
========

Samples for OAuth 2.0 Provider &amp; Client 

<<OAuth2 Provider & Consumer Sample입니다.>>

1. 환경
  가. Oracle 10g Express
  나. Java 1.6 + Spring 3.1 + Eclipse(indigo) + Maven + iBatis 2.0 + Tomcat 6.0(HTTP Port 8000)

2. 구성 요소
  가. oauth2provider : 클라이언트
  나. oauth2client : web server flow 클라이언트
  다. oauth2client_agentflow : User Agent flow 클라이언트 

3. 설정해야 할 값
  가. table 생성
    - DB는 oracle 10g express에 oauth2/oauth2 계정을 생성하여야 함.
    - oauth2provider의 src 디렉토리에 oauth2provider.sql 파일을 읽어 테이블 설치
      사용자 계정은 t1000, gdhong, arnold 세개의 계정(암호 동일)

  나. 상수값 설정
    - net.oauth.v2 패키지의 OAuth2Constant클래스에서 상수값변경
      * USE_REFRESH_TOKEN : refresh token 기능을 사용할지 말지를 결정함.
      * AES_ENCRYPTION_KEY : 내부에서 토큰 생성시 사용할 AES 암호화 키 값
      * EXPIRES_IN_VALUE : refresh token 기능을 사용할 때 토큰의 유효기간(기본값:3600(초))
      
    - net.oauth.v2 패키지의 OAuth2Scope 클래스에서 상수값 변경
      * 조직에 따라 scope 상수값 설정(현재는 6개의 샘플 scope을 설정하였음)
      * resource 엔드포인트 url 별로 권한 설정(scope 지정)
      
4. oauth2 인증용 endpoint
  가. authorization
     - /oauth2provider/oauth2/auth
       * response_type 파라미터가 code일 경우는 web server flow
       * response_type 파라미터가 token일 경우는 user agent flow(Mobile App, Desktop포함)

  나.  token  
     - /oauth2provider/oauth2/token
       * grant_type 파라미터가 authorization_code 인 경우는 server flow로 access token 발급
       * grant_type 파라미터가 refresh_token 인 경우는  access token을 갱신하게 됨.

  다. protected resource 접근시
     - 여러 protected resource에 대해 권한을 부여하기 위해 end point 별 권한은 net.oauth.v2.OAuth2Scope 클래스의
       scopeUrlMap 필드에 Hashmap으로 추가한다.
     - access token 정보 검증, scope 검증은 Interceptor(com.multi.oauth2.provider.util.Oauth2Interceptor)를 
       이용해 Controller 실행 전에 처리한다.
     - 예외 처리는 Controller 상에서 OAuth2Exception 을 발생시키면
       ExceptionResolver가 error 페이지로 이동시켜 OAuth2.0 spec에 따른 에러 코드와 메시지를 응답한다.


5. 추가/개선할 사항..
  가. OAuth 2.0 에서는 에러 발생시 WWW-Authenticate 헤더를 통해 응답하도록 하고 있으나
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

 나. OAuth2.0 의 처리과정 중 Web Server flow 와 user agent flow만 처리하였다.
     - password credential과 client credential 방식은 작성하지 않았다. 대신 
       com.multi.oauth2.provider.view.controller.OAuth2Controller 클래스의 280번 라인에서 
       주석처리하여 향후 구현해야 함을 명시하였다.


6. 주의사항
   - 이 샘플은 잘 작동하지만 제대로된 설계없이 뚝딱거리면서 만들었음.
   - 따라서 잘 정리된 코드는 아님. 주석도 개발새발임.
   - 디버깅 목적으로 코드 사이사이에 콘솔 출력하는 코드가 많으니 알아서들 제거하고 사용해야 함.

   - oracle 10g 대신에 다른 DB 쓸거면 maven dependency, applicationContext.xml, oauth2.xml, 
     oauth2provider.sql 파일을 수정하여 쓰면 됨.
   - 한마디로 상업적인 목적으로 쓰기엔 조금 부끄러운 코드이므로 OAuth2 전반적 흐름을 이해하기 위한 
     용도로만 살펴봐야 함.



      
    
        

  
