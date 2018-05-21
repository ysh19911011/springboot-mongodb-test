<%@page pageEncoding="utf-8" %>
<%-- <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> --%>
<%-- <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>  --%>
<%-- <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> --%>
<%-- <%@taglib prefix="spring" uri="http://www.springframework.org/tags"%> --%>
<%-- <%@ taglib prefix="tag" uri="/WEB-INF/tld/tag.tld"%> --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="/resource/jquery-1.11.0.min.js"></script>
<script type="text/javascript">
$(function(){
	$("#con").change(function(){
		var con=$("#con").val();
		var ls=con.split(" ");
		var use="<script type=\"text/javascript\">\nvar i18n=new Array();\n";
		for(var i=0;i<ls.length;i++){
			use+="i18n['"+ls[i]+"']=\"<spring:message code='"+ls[i]+"'/>\";\n";
		}
		use+="</scr"+"ipt>"
		$("#cp").val(use);
		$("#cp").select();
		document.execCommand("Copy");
		$("#con").focus();
	});
	
});

// function copyUrl2()
// {
// var Url2=document.getElementById("con");
// Url2.select(); // 选择对象
// document.execCommand("Copy"); // 执行浏览器复制命令
// alert("已复制好，可贴粘。");
// }
</script>
</head>
<body>
<textarea style="width:500px;height:300px" cols="60" rows="300" id="con"></textarea>
<textarea style="width:500px;height:300px" id="cp" type="text"  value="" ></textarea>
</body>
</html>