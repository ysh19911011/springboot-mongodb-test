<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html>
<head>
<script type="text/javascript" src="/resource/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
<!-- <script type="text/javascript" src="/resource/vue.js"></script> -->
<meta charset="utf-8">
<title>Insert title here</title>
<script type="text/javascript">
$(function(){
	
});
</script>
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
		<c:forEach var="user" items="${users }" varStatus="index">
		<tr>
			<td>${user.name }</td>
			<td>${user.sex }</td>
			<td>${user.age }</td>
		</tr>
		</c:forEach>
		</tbody>
	</table>
	<div id="vue_test">{{message}}</div>
	<script type="text/javascript">
	var app=new Vue({
		el:'#vue_test',
		data:{message:'Hello world!'}
	});
	</script>
	<div id="app_2">
		<span v-bind:title='message'>
		查看信息
		</span>
	</div>
	<script type="text/javascript">
	var app2=new Vue({
		el:'#app_2',
		data:{message:'test bind!'+new Date().toString()}
	});
	</script>
	<form action="/user/reg">
	<div>
	<p>名字</p><input name="name" type="text" value="">
	</div>
	<div>
	<p>性别</p><input name="sex" type="text" value="">
	</div>
	<div>
	<p>年龄</p><input name="age" type="text" value="">
	</div>
	<input type="submit" value="提交">
	</form>
</body>
</html>