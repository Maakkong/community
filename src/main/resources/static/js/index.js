$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	$("#publishModal").modal("hide");
	//发送Ajax请求前，将csrf令牌放入消息头
	// var token=$("meta[name='_csrf']").attr("content");
	// var header=$("meta[name='_csrf_header']").attr("content");
	// $(document).ajaxSend(function (e,xhr,options) {
	// 	xhr.setRequestHeader(header,token);
	// })

	//获取标题和内容
	var title=$("#recipient-name").val();
	var content=$("#message-text").val();
	console.log("title:"+title);
	console.log("content:"+content);
	$.post(
		CONTEXT_PATH+"/discuss/add",
		{"title":title,"content":content},
		function (data) {
			data=$.parseJSON(data);
			//提示框显示消息
			$("#hintBody").text(data.msg);
			//显示
			$("#hintModal").modal("show");
			//自动隐藏
			setTimeout(function () {
				$("#hintModal").modal("hide");
				//成功则刷新页面
				if(data.code==0){
					window.location.reload();
				}
			},2000);
		}
	)

	$("#hintModal").modal("show");
	setTimeout(function(){
		$("#hintModal").modal("hide");
	}, 2000);
}