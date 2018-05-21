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
		
		con="i18n['"+con+"']"
		$("#cp").val(con);
		$("#cp").select();
		document.execCommand("Copy");
		$("#con").select();
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
<input cols="20" rows="10" id="con"/>
<input id="cp" type="text"  value="" />
</body>
</html>