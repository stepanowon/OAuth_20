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
	client_id      	varchar2(100) not null,		--�Ϸù�ȣ:app ����Ű
	client_secret	varchar2(100) not null,
	userid			varchar2(20) NOT NULL,
    client_name    	varchar2(300) not null,	
    description 	varchar2(400) not null,	--����
    client_url		varchar2(300) not null,	--application url
    client_type		varchar2(20) not null,	--���� ��:��������(W), Desktop(D), Mobile(M)
   	scope  			varchar2(300),			--����:�޸��� �����Ͽ� ������ �Է°���. ������ ���� ���ι��̴��� ������.
    redirect_uri	varchar2(400)  not null,	--redirect uri
    regdate 		date 	default sysdate			--���ϳ�¥
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
	scope				varchar2(100),				--���� : �б�,�б⾲�� ���� �������� �����Ͽ� �����ϴ� ���� ǥ��
	code				varchar2(200),				--serverFlow���� code��(verifier)
	state				varchar2(100),
	client_type			varchar2(20),
	created_at			number(30),				--access token �������� timestamp(1970.1.1���� �������� �帥 ��)
	created_rt			number(30),						--refresh token �������� timestamp
	expires_in			number(30)						--��ū �����ÿ� �ο��ϴ� ��ȿ�Ⱓ 3600
);

alter table tbl_token add constraint pk_token  primary key (client_id, userid,access_token);
alter table tbl_token add constraint fk_token_users foreign key (userid) references tbl_users (userid);
alter table tbl_token add constraint fk_token_client foreign key (client_id) references tbl_client (client_id);

INSERT INTO tbl_users (userid, password, username, userno) 
  values ('gdhong', 'gdhong', 'ȫ�浿', 1000001);
INSERT INTO tbl_users (userid, password, username, userno) 
  values ('arnold', 'arnold', '�Ƴ��� �������װ�', 1000002);
INSERT INTO tbl_users (userid, password, username, userno) 
  values ('t1000', 't1000', 'T-1000', 1000003);
commit;


-- 1970.1.1 ���� �������� �帥 �ð��� ������ ���� �Լ� ����
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
