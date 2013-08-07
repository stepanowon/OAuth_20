<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="login.do" method="POST">
		<table>
			<tr>
				<td style="text-align:right">UserID : </td>
				<td><input type="text" name="userid" id="userid" value="" /></td>
			</tr>
			<tr>
				<td style="text-align:right">Password : </td>
				<td><input type="password" name="password"  id="password" value="" /></td>
			</tr>
			<tr>
				<td colspan="2">
					<input type="submit" value="로그인" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>