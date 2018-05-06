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
	<title>交接任务</title>
	<link rel="stylesheet" href="<%=basePath %>/static/css/common/reset.css" type="text/css"/>
	<link rel="stylesheet" href="<%=basePath %>/static/css/goodsManage.css" type="text/css"/>
	<link rel="stylesheet" href="<%=basePath %>/static/css/changeNoInfo.css" type="text/css"/>
	<link rel="stylesheet" href="<%=basePath %>/static/css/addGoods.css" type="text/css"/>
	<link rel="stylesheet" href="<%=basePath%>/static/js/date-range-picker/dist/daterangepicker.min.css">
	<link rel="stylesheet" type="text/css" href="<%=basePath %>/static/js/easyui-1.4.5/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=basePath %>/static/js/easyui-1.4.5/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=basePath %>/static/js/easyui-1.4.5/demo.css">
	<script type="text/javascript" src="<%=basePath %>/static/js/ckeditor/ckeditor.js"></script>
	<style>
		.datagrid-btable .datagrid-cell{padding:6px 4px;overflow: hidden;text-overflow:ellipsis;white-space: nowrap;} 
	
		.tableCon{
			border:1px #dcdcdc solid;
		}
		.tableCon tr{
			border-bottom:1px #dcdcdc solid;
		}	
		.tableCon td,.tableCon th{
			text-align:center;
			border-right:1px #dcdcdc solid;
		}
		.textbox{margin-left: -4px!important;}
		.datebox{margin-left:0!important;}
	</style>
</head>
<body class="easyui-layout">
<input type="hidden" id="basepath" value="<%=basePath %>"/>
<input type="hidden" id="scanGoods" value="${scanGoods }"/>
<div class="goodsmBox smBox" data-options="region:'center'" style="border:0">
	<div class="easyui-layout" data-options="fit:true">
		<div class="header" id="finishListSearch" data-options="region:'north'" style="border:0">
			<p class="navBar mgB16">
				<a class="btnG btnBl" onclick="finishListSearchBox()">搜索</a>
				<a class="btnG btnBl" onclick="formReset()">重置</a>
			</p>
		<form id="finishForm">
		<table class="gList">
					<td><label class="dib w77">订单号</label><input
						class="easyui-validatebox combo" id="orderNo" name="orderNo"
						maxlength="20" /></td>
					<td><label class="dib tar">承运商</label> <select
						id="logisComId" name="logisComId" class="easyui-combobox"
						style="width: 194px; height: 28px; line-height: 28px;">
							<option value="0">请选择</option>
							<c:forEach items="${companies}" var="company">						
							<option value="${company.id}">${company.name }</option>
					    </c:forEach>
						
					</select></td>
			       <td><label class="dib tar">运单号</label><input
						class="easyui-validatebox combo" id="logisticsNo" name="logisticsNo"
						maxlength="20" /></td>
				<td><label class="dib w77">交接状态</label> <select
						id="status" name="status" class="easyui-combobox"
						style="width: 194px; height: 28px; line-height: 28px;">
					<option value="-1">请选择</option>
					<option value="-12104">未交接</option>
					<option value="12104">已交接</option>
				</select></td>
			<td class="std"><label class="dib tar">订单日期</label>
				<input type="hidden" id="starTime" name="starTime" />
				<input type="hidden" id="endTime" name="endTime" />
				<input id="datetimeRange" size="60" value="">
			</td>
			<td></td>
		</table>
		</form>
		</div>
		<div class="center" data-options="region:'center'" style="border:0">
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north'" style="padding:20px 30px 12px;border:0">
					<p class="cenBtn">
					
					</p>
					
				</div>
				<!-- datagrid列表  -->
				<div data-options="region:'center'" style="padding:1px 30px 20px;border:0">
					<table id="finish_list" width="100%"></table>
				</div>	
			</div>	 
		</div>
				
	</div>
</div>
<script type="text/javascript" src="<%=basePath %>/static/js//jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/static/js/easyui-1.4.5/jquery.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/static/js/easyui-1.4.5/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/static/js/easyui-1.4.5/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/LocalResizeIMG.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/LocalResizeDOC.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/date-range-picker/dist/moment.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/date-range-picker/dist/jquery.daterangepicker.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/static/js/outstock/finishManage.js?v=0.06"></script>

<script type="text/javascript">
//主页面表单
$('.cenList .list li').click(function () {
	var index = $(this).index();
	$(this).addClass("active").siblings().removeClass("active");
});
</script>
</body>
</html>