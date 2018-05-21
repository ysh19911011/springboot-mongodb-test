<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<table>
		<thead>
		<tr>
			<th>姓名</th>
			<th>性别</th>
			<th>年龄</th>
		</tr>
		</thead>
		<tbody>
<%-- 		<c:forEach var="user" items="${users }" varStatus="index"> --%>
		<tr>
			<td>${user.id }</td>
			<td>${user.name }</td>
			<td>${user.sex }</td>
			<td>${user.age }</td>
		</tr>
<%-- 		</c:forEach> --%>
		</tbody>
	</table>
</body>
</html>