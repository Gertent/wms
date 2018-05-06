//@ sourceURL=image.js
var basepath = $("#basepath").val();
$('input[id=lefile]').localResizeIMG({
    width: 500,
    quality: 0.8,
    success: function(result) {
        var img = new Image();
        img.src = result.base64;
        var fileData = result.blob;
        var form=document.forms[0];
        var formData = new FormData(form);   //这里连带form里的其他参数也一起提交了,如果不需要提交其他参数可以直接FormData无参数的构造函数
        formData.append("file",fileData);  //append函数的第一个参数是后台获取数据的参数名,和html标签的input的name属性功能相同
        //ajax 提交form
        $.ajax({
            url : basepath+"/base/upload/uploadPic",
            type : "POST",
            data : formData,
            dataType:"json",
            processData : false,         // 告诉jQuery不要去处理发送的数据
            contentType : false,        // 告诉jQuery不要去设置Content-Type请求头
            success:function(data){
//                window.location.href="${ctx}"+data;
            	$("#category_img").attr("value",data.url);
            	$("#filemsg").text(data.originalName);
            },
            xhr:function(){            //在jquery函数中直接使用ajax的XMLHttpRequest对象
                var xhr = new XMLHttpRequest();
                xhr.upload.addEventListener("progress", function(evt){
                    if (evt.lengthComputable) {
                        var percentComplete = Math.round(evt.loaded * 100 / evt.total);  
                        console.log("正在提交."+percentComplete.toString() + '%');        //在控制台打印上传进度
                    }
                }, false);
                return xhr;
            }
        });
    }
});

function convertBase64UrlToBlob(urlData){
	 var contentType = contentType || 'image/png';

     var sliceSize = sliceSize || 512;
//     var bytes=window.atob(urlData.split(',')[1]);        //去掉url的头，并转换为byte
    
//     //处理异常,将ascii码小于0的转换为大于0
//     var ab = new ArrayBuffer(bytes.length);
//     var ia = new Uint8Array(ab);
//     for (var i = 0; i < bytes.length; i++) {
//         ia[i] = bytes.charCodeAt(i);
//     }

//     return new Blob( [ab] , {type : 'image/png'});
    
    var byteCharacters = atob(urlData.substring(urlData.indexOf(',') + 1));
    var byteArrays = [];
    var byteNumbers = [];

    for (var offset = 0; offset < byteCharacters.length; offset += sliceSize) {

      var slice = byteCharacters.slice(offset, offset + sliceSize);
      for (var i = 0; i < slice.length; i++) {
        byteNumbers[i] = slice.charCodeAt(i);
      }
      var byteArray = new Uint8Array(byteNumbers);
      byteArrays.push(byteArray);
    }
    var blob = new Blob(byteArrays, {type: contentType});
    return blob;

}
function getFileName(o){
    var pos=o.lastIndexOf("\\");
    return o.substring(pos+1);  
}