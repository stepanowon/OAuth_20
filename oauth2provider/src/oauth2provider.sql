DROP TABLE tbl_token;
DROP TABLE tbl_client;
DROP TABLE tbl_users;

CREATE TABLE tbl_users 
(
  	userid  	varchar2(20) NOT NULL,
  	password  	varchar2(20) NOT NULL,
  	username 	varchar2(40),
  	userno		number UNIQUE
);

alter table tbl_users add constraint pk_users  primary key (userid);

 
CREATE TABLE tbl_client
(
	client_id      	varchar2(100) not null,		--일련번호:app 고유키
	client_secret	varchar2(100) not null,
	userid			varchar2(20) NOT NULL,
    client_name    	varchar2(300) not null,	
    description 	varchar2(400) not null,	--설명
    client_url		varchar2(300) not null,	--application url
    client_type		varchar2(20) not null,	--유형 예:웹서비스(W), Desktop(D), Mobile(M)
   	scope  			varchar2(300),			--권한:콤마로 구분하여 여러개 입력가능. 권한은 직접 프로바이더가 정의함.
    redirect_uri	varchar2(400)  not null,	--redirect uri
    regdate 		date 	default sysdate			--등록날짜
);

alter table tbl_client add constraint pk_client  primary key (client_id);
alter table tbl_client add constraint fk_client_users foreign key (userid) references tbl_users (userid);

CREATE TABLE tbl_token 
(
	client_id      		varchar2(100)  NOT NULL,		
	userid				varchar2(20) NOT NULL,
	access_token		varchar2(200) unique,
	refresh_token		varchar2(200) unique,
	token_type			varchar2(30),				--bearer,jwt,mac
	scope				varchar2(100),				--권한 : 읽기,읽기쓰기 등을 공백으로 구분하여 사용하는 것이 표준
	code				varchar2(200),				--serverFlow상의 code값(verifier)
	state				varchar2(100),
	client_type			varchar2(20),
	created_at			number(30),				--access token 생성시의 timestamp(1970.1.1부터 현재까지 흐른 초)
	created_rt			number(30),						--refresh token 생성시의 timestamp
	expires_in			number(30)						--토큰 생성시에 부여하는 유효기간 3600
);

alter table tbl_token add constraint pk_token  primary key (client_id, userid);
alter table tbl_token add constraint fk_token_users foreign key (userid) references tbl_users (userid);
alter table tbl_token add constraint fk_token_client foreign key (client_id) references tbl_client (client_id);

INSERT INTO tbl_users (userid, password, username, userno) 
  values ('gdhong', 'gdhong', '홍길동', 1000001);
INSERT INTO tbl_users (userid, password, username, userno) 
  values ('arnold', 'arnold', '아놀드 슈왈제네거', 1000002);
INSERT INTO tbl_users (userid, password, username, userno) 
  values ('t1000', 't1000', 'T-1000', 1000003);
commit;


-- 1970.1.1 부터 현재까지 흐른 시간값 계산을 위한 함수 등록
CREATE OR REPLACE FUNCTION GET_TIMESTAMP
(                 
   ts	  IN  TIMESTAMP WITH LOCAL TIME ZONE
)
RETURN NUMBER
IS
   totalms   number;
BEGIN
     select extract( day from diff )*24*60*60*1000 +
            extract( hour from diff )*60*60*1000 +
            extract( minute from diff )*60*1000 +
            round(extract( second from diff )*1000) into totalms
     from (select ts - TIMESTAMP'1970-01-01 00:00:00 +0:00' as diff from dual);   

   RETURN(totalms);
END;