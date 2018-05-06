<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>采购列表</title>
    <link rel="stylesheet" href="<%=basePath %>/static/css/common/reset.css" type="text/css"/>
    <link rel="stylesheet" href="<%=basePath %>/static/css/changeNoInfo.css" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>/static/js/easyui-1.4.5/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath %>/static/js/easyui-1.4.5/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath %>/static/js/easyui-1.4.5/demo.css">
</head>
<body class="easyui-layout">
	<div class="rankManageBox" data-options="region:'center'" style="border:0">
		<input type="hidden" id="basepath" value="<%=basePath %>" />
		<input type="hidden" id="id" value="${id }" />
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north'" style="border:0">
				<p class="title fz16f pdL20">采购列表<a href="javascript:void(0)" class="close"></a></p>
			</div>   
    		<div class="center" data-options="region:'center'" style="padding:20px 30px;border:0;">
        		<p class="cenBtn mgT20">
            		<a href="javascript:void(0)" class="btnG btnBl" onclick="sort()">确认排序</a><a href="javascript:void(0)" class="btnG btnBl" id="delproperty">删除</a><a href="javascript:void(0)" class="btnG btnOg" id="editPro">编辑</a><a href="javascript:void(0)" class="btnG btnOg" id="addProBtn">+ 添加属性</a>
        		</p>
        		<div class="mgT20">
            		<table id="proper_list"></table>
            		<table id="proper_parm_list"></table>
        		</div>
    		</div>
    	<!-- editProperty弹窗 -->
    	<div class="tabContentBox hide" id="editProperty" width="100%" height="640"></div>
    	<!-- addProperty弹窗 -->
    	<div class="tabContentBox hide" id="addProperty" width="100%" height="640"></div>
	</div>
	<!--add可选值-->
	<div class="addOptBox tac hide">
		<input type="hidden" id="rowsid" />
    	<textarea class="bE5 pdL10 mgT30" placeholder="每行一条可选值，请用回车换行，空格将被自动过滤" id="choice"></textarea>
    	<div class="edBox tac"><a class="btnG btnBl" id="choiceCommit">确定</a></div>
	</div>
	<script type="text/javascript" src="<%=basePath %>/static/js/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="<%=basePath %>/static/js/easyui-1.4.5/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=basePath %>/static/js/easyui-1.4.5/easyui-lang-zh_CN.js"></script>
<%--	<script type="text/javascript" src="<%=basePath %>/static/js/easyui-1.4.5/datagrid-detailview.js"></script>--%>
	<%--<script type="text/javascript" src="<%=basePath %>/static/js/goods/propertyList.js"></script>--%>
	<script>
    	//单选按钮
    	$(".proBox table td a").click(function(){
        	$(this).find("i").css("background-position","right");
        	$(this).parent().siblings().find("i").css("background-position","left");
        	$(this).find("input").attr('checked','checked');
    	});
   		/* add可选值 */
    	function addOpt(val){
	  		$("#rowsid").val(val);
        	$(".addOptBox").window({
            	width:800,
            	modal:true,
            	collapsible:false,
            	minimizable:false,
            	maximizable:false,
            	title:"添加可选值"
        	});
    	}
	</script>
</body>
</html>