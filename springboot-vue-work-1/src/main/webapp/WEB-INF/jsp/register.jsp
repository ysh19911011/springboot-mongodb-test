<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html>
<head>
<link href="/resource/css/validate.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="/resource/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="/resource/Validform_v5.3.2_min.js"></script>
<script type="text/javascript" src="/resource/jquery-easyui-1.5.3/jquery.easyui.min.js"></script>

<meta charset="utf-8">
<title>Insert title here</title>
<script type="text/javascript">
$(function(){
	var info = [{
		ele : "input[name='name']",
		datatype : "*",
		nullmsg : "姓名不能为空",
	},{
		ele : "input[name='sex']",
		datatype : "*",
		nullmsg : "性别不能为空"
	},{
		ele : "input[name='age']",
		datatype : "n",
		nullmsg : "年龄不能为空",
	}];
	var system=$("#form").Validform({
		btnSubmit:"#load",
		showAllError:false,	
		ajaxPost:true,
		tiptype:function(msg,o,cssctl){
// 			validformTipType(msg,o,cssctl);
		},
		datatype:{
		    "username":function(gets,obj,curform,regxp){
 	        	//参数gets是获取到的表单元素值，obj为当前表单元素，curform为当前验证的表单，regxp为内置的一些正则表达式的引用;
 	        	var reg2=/^[\u4E00-\u9FA5\uf900-\ufa2d]{2,10}$/;
 	        	if(reg2.test(gets)){
 	        	return true;
 	        	}
 	        	return false;
 	        	//注意return可以返回true 或 false 或 字符串文字，true表示验证通过，返回字符串表示验证失败，字符串作为错误提示显示，返回false则用errmsg或默认的错误提示;
 	        }
		},
		callback:function(data){
			var btn=$("input[type='submit']");
			btn.attr('disabled',true).css('cursor','not-allowed');
 	  		if(data==1){
//  	  		 $().toastmessage('showSuccessToast',"成功");
 	  		 setTimeout(function(){
			   	   location.reload();
		  	     },1500);
 	  		}
   		},
		beforeSubmit:function(curform){
		
		}			
	});
	system.addRule(info);
// 	$("#ui").combobox({
// 		url:"/resource/img/all-none-en.png",
// 		request:true,
// 		valueField:"id",
// 		textField:"text"
// 	});
});
// function showP(ele){
// // 	var index=$(ele).parent().index();
// // 	var images=["/resource/img/all-none-en.png","/resource/img/all-none-ft.png","/resource/img/all-none-jn.png","/resource/img/all-none.png"];
// // // 	$("#image").src=images[index];
// // // 	console.log(images[index]);
// 	$("#image").attr("src",$(ele).attr("href"));
	
// }
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
	
	
	<form action="/user/reg" id="form" method="post">
	<div>
	<p>名字</p><input name="name" type="text" value="">
	</div>
	<div>
	<p>性别</p><input name="sex" type="text" value="">
	</div>
	<div>
	<p>年龄</p><input name="age" type="text" value="">
	</div>
	<input type="button" value="提交" id="load">
	<span id="tips"></span>
	</form>
	<div id="ui"></div>
	<div>
<!-- 	<ul> -->
<!-- 	<li><a href="/resource/img/all-none-en.png" onclick="showP(this);return false;">第一图</a></li> -->
<!-- 	<li><a href="/resource/img/all-none-ft.png" onclick="showP(this); return false;">第二图</a></li> -->
<!-- 	<li><a href="/resource/img/all-none-jn.png" onclick="showP(this); return false;">第三图</a></li> -->
<!-- 	<li><a href="/resource/img/all-none.png"  onclick="showP(this); return false;">第四图</a></li> -->
<!-- 	<li><img id="image" src="/resource/img/all-none-en.png" style="width:500px;height:400px"></img></li> -->
<!-- 	</ul> -->
	</div>
</body>
</html>