<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>仓储管理系统</title>

<link rel="stylesheet" href="<%=basePath%>/static/css/common/reset.css"/>
<link rel="stylesheet" href="<%=basePath%>/static/css/common/leftmenu.css"/>
<link rel="stylesheet" href="<%=basePath%>/static/css/index.css"/>
<link rel="stylesheet" type="text/css" href="<%=basePath%>/static/js/easyui-1.4.5/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=basePath%>/static/js/easyui-1.4.5/themes/icon.css">
<link rel="stylesheet" type="text/css" href="<%=basePath%>/static/js/easyui-1.4.5/demo.css">
<script type="text/javascript" src="<%=basePath%>/static/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/jquery.slimscroll.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/index.js?v=0.15"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/easyui-1.4.5/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/easyui-1.4.5/easyui-lang-zh_CN.js"></script>
<tag:pagedialog />
<style>
	.memEdit table {
	    margin-top: 14px;
	    width: 100%;
	}
	.memEdit table td.th {
	    width: 32%;
	}
	.memEdit td label {
	    text-align: right;
	    line-height: 44px;
	    padding-right: 5px;
	    display: inline-block;
	}
	.memEdit td label, .memEdit td a {
	    font-size: 15px;
	}
	.memEdit td > input {
	    width: 66%;
	    height: 28px;
	    line-height: 28px;
	    padding-left: 5px;
	    border: 1px solid #e5e5e5;
	}
	.edBox {
	    padding: 30px 0;
	}
	/*头部导航未完成css*/
        .page_tabs{
            margin-left: 50px;
            width: 100000px;
            height: 44px;
            overflow: hidden;
            line-height: 44px;
        }
        .page-tabs-content{
            float: left;
        }
        .page_tabs a{
            color: #fff;
            float: left;
            height: 28px;
            display: block;
            margin-top: 8px;
            font-size: 13px;
            padding: 0 15px;
            margin-left: 40px;
            line-height: 28px;
            background: #454b5b;
            border-radius: 14px;
        }
        .page-tabs a:hover,
        .page-tabs a.active {
            color: #313643;
            background: #f5f5f5;
            cursor: pointer;
        }
        
</style>
</head>

<body>
<div class="bgBox_en">
	<div class="aside">
        <div class="nav">
            <div class="nav_bar">
                <ul class="menuN">
                    <li class="menu_header">
                        <img src="<%=basePath%>/static/images/logo.png" alt="logo.png"/>
                        <h3>仓储管理系统</h3>
                    </li>
                     <c:forEach items="${modlist}" var="m">
						<c:if test="${m.level==1}">
                    	<li>
                    		<a href="#" class="link"><i class="iconfont">${m.icourl}</i><span>${m.menuName}</span></a>
	                        <ul class="nav_left" id="meun_Lfet">
	                         	 <c:forEach items="${modlist}" var="m2">
									<c:if test="${m2.level == 2 && m2.parentid == m.id}"> 
		                        		<li id="value_${m2.id}"><a href="javascript:skipEnter('${m2.menuUrl}')" class="J_menuItem"><i class="icon_en icon_z"></i><span>${m2.menuName}</span></a></li>
		                        	</c:if>
	                        	</c:forEach>  
	                        </ul>
                    	</li>
                    	</c:if>
                    </c:forEach>
                </ul>
            </div>
            
        </div>
    </div>
    <div class="section">
    	
    	<div class="header clearfix">
    		<div class="headerL fl">
	    		<input type="text" value="${wareId}" hidden=true id="wareIdEx"/>
	     		<select name="wareId" id="wareId">
	<!--                 <option value="0">请选择仓库</option> -->
	                <c:forEach items="${warehouseList}" var="wharehouse">
	                <option value="${wharehouse.id}">${wharehouse.wareName}</option>
	                </c:forEach>
	             </select>
	        </div>
            <div class="nav_header fr tar">
                <p><i class="icon_adm"></i><a href="">${user.loginname}</a><i class="icon_dow"></i></p>
                <div class="downBox">
                    <a href="javascript:accEditing();"><span><em></em></span><i></i>账号信息</a>
                    <a href="javascript:pwdModify();"><i></i>修改密码</a>
                    <a href="<%=basePath%>/logout"><i></i>退出登录</a>
                </div>
            </div>
        </div>
        <div class="nav_row ">
            <div class="nav_list">
                <buttom class="prev tac cup"></buttom>
                <div class="page_tabs J_menuTabs">
                    <div class="page-tabs-content">
                        <a href="javascript:;" class="active J_menuTab">首页</a>
                    </div>
                </div>
                <buttom class="next tac cup"></buttom>
            </div>
            <div class="dropdown tac">
                <buttom class="db cup">操作 <i class="va_m dib"></i></buttom>
                <ul>
                    <li></li>
                    <li></li>
                    <li></li>
                </ul>
            </div>
        </div>
        <div class="content" style="height:calc(100% - 126px);overflow:hidden;">
            <iframe id="frame" name="view_frame" src="<%=basePath%>/views/welcome.jsp" width="100%" height="100%" frameborder="no" scrolling=""></iframe>
        </div>
    </div>
</div>
<!-- 账号-->
<div id="accountEdit" class="memEdit hide"></div>
<!-- 修改密码-->
<div id="modifyPwd" class="hide">
	<div class="memEdit">
	<form id="myForm" method="post" >
		<table>
        <tr>
            <td class="th tar"><label>原密码：</label></td>
            <td>
            	<input  type="hidden" name="id" value="${user.id}"/>
            	<input id="oldPassword" type="password" name="password1"/>
            </td>
        </tr>
        <tr>
           <td class="th tar"><label>新密码：</label></td>
           <td><input type="password" name="password" id="password"
			class="easyui-validatebox"
				data-options="required:true,validType:['isBlank','enAndnum','length[6,16]']" 
				missingMessage='(6-16位字符,可包含数字，字母区分大小写)' invalidMessage='(6-16位字符,可包含数字，字母区分大小写)'/></td>
        </tr>
        <tr>
            <td class="th tar"><label>确认新密码：</label></td>
            <td><input  id="newsPassword" type="password" name="password2"
			 class="easyui-validatebox"
			 	data-options="required:true,validType:['isBlank','enAndnum','length[6,16]']" 
			 	missingMessage='(6-16位字符,可包含数字，字母区分大小写)' invalidMessage='(6-16位字符,可包含数字，字母区分大小写)'/></td>
        </tr>
    </table>
    <div class="edBox tac"><a href="javascript:;" onclick="savePassword()" class="btnG btnBl">保存</a><a onclick="cancle()" class="btnG btnOg">取消</a></div>
	</form>
	</div>    
</div>
<script type="text/javascript">

//选择进入
function skipEnter(enterurl){
	var url = "";
	if(enterurl.indexOf("http://")==0){
		url = enterurl;
	}else{
		url = "<%=basePath%>"+enterurl;
	}
	document.getElementById("frame").src=url;
}

//账户信息
function accEditing(){
  	var id = ${user.id};
    var url='${acountUrlInfo}';
    window.parent.skipEnter(url+"?id="+id);
}

//修改密码
function pwdModify(){
    $("#modifyPwd").window({
        width:800,
        modal:true,
        shadow:false,
        collapsible:false,
        minimizable:false,
        maximizable:false,
        title:"修改密码"
    });
}

function cancle(){
	 $("#modifyPwd").window("close");
	 $("#accountEdit").window("close");
}

function savePassword(){
	
	var isValid = $("#myForm").form("validate");
	//alert(isValid);
	if(!isValid){
		return false;
	}else{
		var password = $("#password").val();
		var oldpassword = $("#oldPassword").val();
		var newsPassword = $("#newsPassword").val();
		if(password != newsPassword){
			$.messager.alert('提示', '两次密码不一致，请重新输入!', 'error');
    		return;
		} else{
				$.ajax({
					url:'<%=basePath %>/user/editPassword',
					data:$("#myForm").serialize()+"&oldpassword="+oldpassword,
					dataType:"json",
					success:function(ret){
						if(ret == "1"){
							$.messager.alert('提示', '原始密码不正确,请重新输入！', 'error');
							return;
						}else{
							 $.messager.confirm('提示:','修改成功，请跳转首页重新登录',function(event){   
								if(event){
									location.href="<%=basePath%>/logout";
								}else{
									location.href="<%=basePath%>/logout";
								}	
							});
						}
					}
				});
			}
		}
	}
</script>
</body>
</html>