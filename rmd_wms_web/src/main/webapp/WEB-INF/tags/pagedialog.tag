
<%@ tag pageEncoding="UTF-8"%>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>  
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>
 <link id="skin" rel="stylesheet" href="<%=basePath%>/static/js/jBox/Skins/Gray/jbox.css" />
 <script type="text/javascript" src="<%=basePath%>/static/js/jBox/jquery.jBox-2.3.min.js"></script>
 <script type="text/javascript" src="<%=basePath%>/static/js/jBox/i18n/jquery.jBox-zh-CN.js"></script>
 <script>
      var options = { id: 'jboxopenwindow', top: '15px', buttons: {'确定': 1, '取消': 0},  
          submit: function (v, h, f) {
          	if(v==1){
          		/* @lele 加入UEditor后，会多个iframe*/
          		var i = 0;
          		if($("#jboxopenwindow").find("iframe").length>1){
          			i = $("#jboxopenwindow").find("iframe").length-1 ;
          		}
          		var ret_value=$($("#jboxopenwindow").find("iframe"))[i].contentWindow.window_dialog_return();
          		processDialogReturn(ret_value);
          	}
          	return true;
          },  
          showClose: true, 
          persistent: true 
      };  
	function jopen(content, title, width, height) {
    	$.jBox.open(content, title, width, height, options);  
	};

</script>


