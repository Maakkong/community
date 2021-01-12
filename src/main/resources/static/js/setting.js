$(function () {
    $("#upLoadForm").submit(upload);
});

function upload() {
    $.ajax({
        url: "http://up-z2.qiniup.com",
        method: "post",
        //不转为字符串
        processData: false,
        //不让转换JQury类型
        contentType: false,
        data: new FormData($("#upLoadForm")[0]),
        success:function (data) {
            if(data && data.code==0){
                //更新头像访问路径
                $.post(
                    CONTEXT_PATH+"/user/header/url",
                    {"fileName":$("input[name='key']").val()},
                    function (data) {
                        data=$.parseJSON(data);
                        if(data.code==0){
                            window.location.reload();
                        }
                        else {
                            alert(data.msg)
                        }
                    }
                )
            }
            else {
                alert("上传失败！");
            }
        }
    });

    //不要提交表单，事件到此为止
    return false;
}