<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<script type="text/javascript">
window.onload = function() {
	document.getElementById("gomain").onclick = function() {
		console.log('aaa');
		location.href="../index.jsp";
	};
	
};
</script>
</head>
<body>
<h1>사용자가 클라이언트의 접근을 승인하지 않았습니다</h1>
<br><br>
본서비스의 메인 페이지로 이동하시려면 아래버튼을 클릭하세요.<br><br>
<button id="gomain">메인페이지로</button> 
</body>
</html>