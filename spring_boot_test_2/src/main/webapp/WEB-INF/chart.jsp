<%@page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<link href="/resource/css/style.css" type="text/css" rel="stylesheet">
<title>历史数据</title>
<script type="text/javascript" src="/resource/js/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="/resource/js/echarts.3.0.min.js"></script>
<link href="/resource/css/uesr_con.css" type="text/css" rel="stylesheet">
<script type="text/javascript">
$(function(){
	var chart = echarts.init(document.getElementById('chart'));
	chart.showLoading();//加载动画显示
	$.get('/test/showChart').done(function (data) {
		chart.hideLoading();//加载动画隐藏
		if(data.datas!=null&&data.datas!=""){
			chart.setOption({
				 title : {
//	 			        text: data.title,
//	 			        subtext: data.subtitle
				    },
				    tooltip : {
				        trigger: 'axis'
				    },
				    legend: {
				        data:data.names,
				        borderWidth:1,
				        left:'20',
				        top:'5'
				    },
				    toolbox: {
				        show : true,
				        itemSize:15,
				        itemGap:10,
				        feature : {
				            mark : {show: true},
				            dataView : {
				            	show: true,
				            	readOnly: true,
				            	title:"数据视图", 
				            	buttonColor:'#00bfe8',
				            	textareaBorderColor:'#f7f7f7',
				            	optionToContent: function(opt) {
				            	    var axisData = opt.xAxis[0].data;
				            	    var series = opt.series;
				            	    var table = '<table style="width:97%;text-align:center;margin:0 auto;"><tbody><tr>'
				            	    			+ '<td style="padding:8px 0;color:#000; font-weight:bold;border-bottom:1px solid #f2f2f2"><spring:message code="message.time"/></td>';
				            	    for(var i=0;i<series.length;i++){
				            	    	table+='<td style="padding:8px 0;color:#000; font-weight:bold;border-bottom:1px solid #f2f2f2">' + series[i].name + '</td>';
				            	    }
				            	    table+='</tr>';             
				            	    for (var i = 0, l = axisData.length; i < l; i++) {
				            	        table += '<tr>'
				            	        		+ '<td style="padding:8px 0; border-bottom:1px solid #f2f2f2">' + axisData[i] + '</td>';
				            	        for(var j=0;j<series.length;j++){
					            	    	table+='<td style="padding:8px 0;  border-bottom:1px solid #f2f2f2">' + series[j].data[i] + '</td>';
					            	    }
				            	        table+='</tr>';
				            	    }
				            	    table += '</tbody></table>';
				            	    return table;
				            	}
				            },
				            magicType : {
				            	show: true, 
// 				            	title:{
// 				            		line:"折线图",
// 				            		bar:"柱状图",
// 				            		stack:"堆叠",
// 				            		tiled:"<spring:message code='tiled.chart'/>"
// 				            	},
				            	type: ['line','bar', 'stack', 'tiled']
				            },
				            restore : {show: true,title:"还原"},
				            saveAsImage : {show: true,title:"保存为图片"}
				        },
				     	right:'10'
				    },
				    calculable : true,
				    grid:[
				    	{
				    		top:'13%',
				    		bottom:'70'
				    	}],
				dataZoom: [{
				        type: 'inside',
				        xAxisIndex: [0],
				        start: 0,
				        end: 100
				    }, {
				    	type: 'slider',
						xAxisIndex: [0],
				    	start: 0,
				        end: 100,
				        handleIcon: 'M10.7,11.9v-1.3H9.3v1.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4v1.3h1.3v-1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z',
				        handleSize: '80%',
				        	dataBackgroundColor:'#eeeeee',//数据缩略背景颜色，仅以第一个系列的数据作为缩量图显示
							handleColor:'#ffffff',//控制手柄颜色 
							fillerColor:'#1ecde2',//选择区域填充颜色 
							backgroundColor:'#ffffff',//背景颜色，默认透明 
						handleStyle: {
				            color: '#fff',
				            shadowBlur: 3,
				            shadowColor: 'rgba(0, 0, 0, 0.6)',
				            shadowOffsetX: 2,
				            shadowOffsetY: 2
				        }
				    }],
				    xAxis : [
				        {
				            type : 'category',
				            boundaryGap : false,
				            data : data.dates

				        }
				    ],
				    yAxis : [
				        {
				            type : 'value'
// 				            name: 'KWh'
//	 			            show:false,
				        }
				    ],
				    graphic:[],
				    series : data.datas
		    });
		}
	}).done(open_socket(chart)); 
});
function open_socket(chart){
	var websocket = null;

    //判断当前浏览器是否支持WebSocket
    if('WebSocket' in window){
        websocket = new WebSocket("ws://localhost:8080/websocket");
    }
    else{
        alert('Not support websocket')
    }

    //连接发生错误的回调方法
    websocket.onerror = function(){
        setMessageInnerHTML("error");
    };

    //连接成功建立的回调方法
    websocket.onopen = function(event){
        addData(event.data);
    }

    //接收到消息的回调方法
    websocket.onmessage = function(event){
        setMessageInnerHTML(event.data);
    }

    //连接关闭的回调方法
    websocket.onclose = function(){
        setMessageInnerHTML("close");
    }

    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function(){
        websocket.close();
    }

    //将消息显示在网页上
    function addData(data){
//         document.getElementById('message').innerHTML += innerHTML + '<br/>';
		chart.getOption;
    }

    //关闭连接
    function closeWebSocket(){
        websocket.close();
    }

    //发送消息
    function send(){
//         var message = document.getElementById('text').value;
//         websocket.send(message);
    }
}
</script>
</head>

<body style="background: #fff">
	<div class="uesr_rightbox">
    	<div class="uesr_right">
        	<h5>当前位置：历史记录<a href="###"></a></h5>
			<div class="real_tit">
				<div class="real_titleft">
					<ul>
						<li><img src="/resource/images/biaoti.png"></li>
						<li>
<%-- 							<span class="codespan">${attrs[0].deviceCode }</span> --%>
						</li>
						<div class="clear"></div>
					</ul>
					<div class="real_show" style="display:none;">
       				<div class="show_box">
       					<h1><img src="/resource/images/hist.png"></h1>
<%--        						<c:forEach var="attr" items="${attrs}" varStatus="index"> --%>
<!--        						<div class="shape"> -->
<!--        							<dl> -->
<%--        								<dt>${fn:substring(attr.modifydate,0,10) }</dt> --%>
<%--        								<dd class="shape_one" id="${attr.id }"> --%>
<%--        									<a href="###" ><span><c:out value="${attr.deviceAlias }">${attr.deviceCode }</c:out></span></a> --%>
<!--        								</dd> -->
<!--        							</dl> -->
<!--        						</div> -->
<%--        						</c:forEach> --%>
       				</div>
       			</div>
				</div>
				<div class="real_titright">
					<ul>
						<li class="real_titbule"><a href="###">数据展示</a></li>
<%-- 						<li><a href="###"><spring:message code='dealer.inverters'/></a></li> --%>
<%-- 						<li><a href="###"><spring:message code='dealer.batteries'/></a></li> --%>
						<div class="clear"></div>
					</ul>
					
				</div>
				<div class="clear"></div>
			</div>
       		<div class="real_box">
<!--        			<div class="real_hide" id="hide"> -->
<!--        				<dl> -->
<!--        					<dt><img src="/resource/images/hist.png"></dt> -->
<!--        					<dd class="real_one real_onebian"> -->
<!--        						<span></span> -->
<!--        					</dd> -->
<!--        					<dd class="real_two"> -->
<!--        						<span></span> -->
<!--        					</dd> -->
<!--        					<dd class="real_three"> -->
<!--        						<span></span> -->
<!--        					</dd> -->
<!--        					<dd class="real_four"> -->
<!--        						<span></span> -->
<!--        					</dd> -->
<!--        				</dl> -->
<!--        			</div> -->
       			<div class="real_con" id="chart" style="width: 100%;height:685px;"></div> <!-- 充电器 -->
<!--        			<div class="real_con" id="minitor_inverters" style="width: 100%;height:685px;"></div> 逆变器  -->
<!--        			<div class="real_con" id="minitor_batteries" style="width: 100%;height:685px;"></div> 蓄电池 -->
<!--        			<div class="chart_t-img"></div> -->
       		</div>
       
        </div>
    </div>
</body>
</html>
