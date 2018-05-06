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
	<title>报损</title>
	<link rel="stylesheet" href="<%=basePath %>/static/css/common/reset.css" type="text/css"/>
	<link rel="stylesheet" href="<%=basePath %>/static/css/goodsManage.css" type="text/css"/>
	<link rel="stylesheet" href="<%=basePath %>/static/css/changeNoInfo.css" type="text/css"/>
	<link rel="stylesheet" href="<%=basePath %>/static/css/addGoods.css" type="text/css"/>
	<link rel="stylesheet" href="<%=basePath %>/static/css/movement.css" type="text/css"/>
	<link rel="stylesheet" href="<%=basePath%>/static/js/date-range-picker/dist/daterangepicker.min.css">
	<link rel="stylesheet" type="text/css" href="<%=basePath %>/static/js/easyui-1.4.5/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=basePath %>/static/js/easyui-1.4.5/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=basePath %>/static/js/easyui-1.4.5/demo.css">
	<script type="text/javascript" src="<%=basePath %>/static/js/ckeditor/ckeditor.js"></script>
	<style>
		.datagrid-btable .datagrid-cell{padding:6px 4px;overflow: hidden;text-overflow:ellipsis;white-space: nowrap;} 
		.textbox{margin-left: 5px!important;}
	</style>
</head>
<body class="easyui-layout">
<input type="hidden" id="basepath" value="<%=basePath %>"/>
<input type="hidden" id="scanGoods" value="${scanGoods }"/>
<div class="goodsmBox smBox" data-options="region:'center'" style="border:0">
	<div class="easyui-layout" data-options="fit:true">
		<div class="header" id="brokenMoveListSearch" data-options="region:'north'" style="border:0">
			<p class="navBar mgB16">
				<a class="btnG btnBl" onclick="BrokenListSearchBox()">搜索</a>
				<a class="btnG btnBl" onclick="formReset()">重置</a>
			</p>
		<table class="gList">
					<td><label for="" class="dib tar">移出货位号</label> <input
						class="easyui-validatebox combo" id="locationNoOut" name="locationNoOut"
						maxlength="20" /></td>
					<td>
					<td><label for="" class="dib tar">移入货位号</label> <input
						class="easyui-validatebox combo" id="locationNoIn" name="locationNoIn"
						maxlength="20" /></td>
					<td class="std"><label class="dib tar">移出时间</label>
						<input type="hidden" id="moveOutTimeStart" name="moveOutTimeStart" />
						<input type="hidden" id="moveOutTimeEnd" name="moveOutTimeEnd" />
						<input id="datetimeRangeOut" size="60" value="">
					</td>
					<td><label for="" class="dib tar">移出人员</label> <input
						class="easyui-validatebox combo"  name="outUserName"
						maxlength="20" /></td>
					<td><label for="" class="dib tar">商品编码</label> <input
						class="easyui-validatebox combo" name="goodsCode"
						maxlength="20" /></td>

					<td class="std"><label class="dib tar">移入时间</label>
						<input type="hidden" id="moveInTimeStart" name="moveInTimeStart" />
						<input type="hidden" id="moveInTimeEnd" name="moveInTimeEnd" />
						<input id="datetimeRangeIn" size="60" value="">
					</td>
					<td><label for="" class="dib tar">移入人员</label> <input
						class="easyui-validatebox combo"  name="inUserName"
						maxlength="20" /></td>
					<td>
					
					<td><label for="" class="dib tar">移库状态</label><select
						 name="status" class="easyui-combobox"
						style="width: 194px; height: 28px; line-height: 28px;">
							<option value="0">请选择</option>
							<option value="1">未完成</option>
							<option value="2">已完成</option>
					</select></td>

				</table>
		</div>
		<div class="center" data-options="region:'center'" style="border:0">
			<div class="easyui-layout" data-options="fit:true">
			    <div data-options="region:'north'" style="padding:20px 30px 12px;border:0">
					<p class="cenBtn">
						<!--  空一行，页面美观度-->
						<a class="btnG btnBl" id="brokenExport">导出Excel</a>
					</p>
					
				</div>
				<!-- datagrid列表  -->
				<div data-options="region:'center'" style="padding:1px 30px 20px;border:0">
					<table id="brokenmove_list" width="100%" fitColumns="true"></table>
				</div>	
			</div>	 
			<div id="purchaseBillGoods"></div>		 
		</div>
	</div>
</div>
<script type="text/javascript" src="<%=basePath %>/static/js//jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/static/js/easyui-1.4.5/jquery.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/static/js/easyui-1.4.5/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/static/js/easyui-1.4.5/datagrid-detailview.js"></script>
<script type="text/javascript" src="<%=basePath %>/static/js/easyui-1.4.5/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/LocalResizeIMG.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/LocalResizeDOC.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/date-range-picker/dist/moment.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/date-range-picker/dist/jquery.daterangepicker.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/static/js/excelHead/brokenHead.js"></script>
<script type="text/javascript" src="<%=basePath %>/static/js/movement/brokenManage.js?v=0.21"></script>

<script type="text/javascript">
//主页面表单
$('.cenList .list li').click(function () {
	var index = $(this).index();
	$(this).addClass("active").siblings().removeClass("active");
});
</script>
</body>
</html>