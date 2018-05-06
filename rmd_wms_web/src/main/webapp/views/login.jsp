<%@page import="org.springframework.context.annotation.Import"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
<html>
<head lang="en">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>登录页</title>
    <link rel="stylesheet" href="<%=basePath%>/static/css/common/reset.css" type="text/css"/>
    <link rel="stylesheet" href="<%=basePath%>/static/css/login.css" type="text/css"/>
    <link rel="stylesheet" href="<%=basePath%>/static/js/easyui-1.4.5/themes/default/easyui.css"/>
    <link rel="stylesheet" href="<%=basePath%>/static/js/easyui-1.4.5/themes/icon.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/static/js/easyui-1.4.5/demo.css">
    <script type="text/javascript" src="<%=basePath%>/static/js/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/static/js/easyui-1.4.5/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/static/js/easyui-1.4.5/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript">
    
	$(document).ready(function(){
		$("#loginname").focus();
		document.onkeydown=function(event){
			var e = event || window.event || arguments.callee.caller.arguments[0];
			if(e && e.keyCode==13){ // enter 键
				checkLogin();
			}
		};

		var authorityFlag=$("#authorityFlag").val();
        if(authorityFlag=='1'){//有角色管理和仓库管理权限，显示系统设置按钮
            $('#systemSet').show();
        }else{
            $('#systemSet').hide();
        }
		
	});
	
	function changeImg(){    
	    var imgSrc = $("#image_VerifyCode");    
	    var src = imgSrc.attr("src");    
	    imgSrc.attr("src",chgUrl(src));    
	}

	function chgUrl(url){    
	    var timestamp = (new Date()).valueOf();    
	    urlurl = url.substring(0,17);    
	    if((url.indexOf("&")>=0)){    
	        urlurl = url + "×tamp=" + timestamp;    
	    }else{    
	        urlurl = url + "?timestamp=" + timestamp;    
	    }    
	    return urlurl;    
	} 
    
	//登录
	function checkLogin(){
/* 		var loginname = $("#loginname").val();
		var password = $("#password").val();
		var validateCode = $("#validateCode").val(); */
		var wareId=$("#wareId").val();
	    if(wareId==0){
	    	$.messager.alert("提示","请选择仓库", "info");
	    }else{
	    	$("#check").submit();
	    }

			
	}
    //系统设置
	function systemSetFun(){
        $("#check").submit();
    }

</script>
</head>
<body>
<input id="authorityFlag" name="authorityFlag" value="${authorityFlag}" type="text" hidden="true"/>
<div class="loginbg"><img src="<%=basePath%>/static/images/bgpic.png" width="100%" height="100%" alt="logo"/></div>
<div class="loginForm">
    <div class="nav">
        <img src="<%=basePath%>/static/images/logoIcon.png" width="50%" height="50%" alt="logo"/>
        <h1>WMS后台</h1>
    </div>
    <form  id="check" action="<%=basePath%>checkLogin" method="post">
        <div class="section">
            <ul class="formIn">
                <li><img src="<%=basePath%>/static/images/userIcon.png" alt=""/><input id="loginname" name="loginname" value="${userName}" type="text"/></li>
                <%-- <li><img src="<%=basePath%>/static/images/passIcon.png" alt=""/><input id="password" name="password" type="password"/></li> --%>
                <li><i class="iconfont" style="font-size: 22px;color:#55627d;">&#xe611;</i>
                <select name="wareId" id="wareId">
                <option value="0">请选择仓库</option>
                <c:forEach items="${warehouseList}" var="wharehouse">
                <option value="${wharehouse.id}">${wharehouse.wareName}</option>
                </c:forEach>
                </select>
                </li>
            </ul>
            <%-- <div class="validation clearfix">
                <div class="dateBox">
                    <input type="text" id="validateCode" name="validateCode" placeholder="验证码"/>
                </div>
                <a href="#" class="moveAnother" onclick="changeImg()"></a>
                <div class="dateImg">
                	<img height="40px" width="100px" id="image_VerifyCode" src="<%=basePath%>ValidateCodeServlet">                   
                </div>
            </div> --%>
        </div>
    <%--    <input type="button" value="立即登录" class="loBtn btnG btnOg" onclick="checkLogin()" />--%>
        <input type="button" onclick="checkLogin()" value="进入仓库" class="loBtn btnG btnOg"  />
        <input type="button" onclick="systemSetFun()" value="系统设置" class="loBtn btnG btnOg"  id="systemSet"/>
    </form>
</div>
</body>
</html>