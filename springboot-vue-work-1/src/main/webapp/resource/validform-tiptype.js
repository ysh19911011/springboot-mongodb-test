/**
 * 修改valiform默认提示
 * @param msg
 * @param o
 * @param cssctl
 * @returns
 */
$(function(){
	//给页面头部添加toastmessage.css与toastmessage.js
     $("head").append("<link href=\"/resource/css/jquery.toastmessage.css\" type=\"text/css\" rel=\"stylesheet\">" +
     		          "<script type=\"text/javascript\" src=\"/resource/js/jquery.toastmessage.js\"></script>");
})
function validformTipType(msg,o,cssctl,position){
//	$(".toast-item-wrapper").remove();  
	if(o.type!=1 && o.type!=2){
		$().toastmessage('showToast',{
			text     : msg,
			sticky   : false,
			position : position?position:'top-right',
			type     : o.type==2?"success":o.type==3?"error":o.type==1?"notice":"warning",
			close:            null 
		});
	}
}