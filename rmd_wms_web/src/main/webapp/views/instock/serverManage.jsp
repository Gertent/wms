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
	<title>入库列表</title>
	<link rel="stylesheet" href="<%=basePath %>/static/css/common/reset.css" type="text/css"/>
	<link rel="stylesheet" href="<%=basePath %>/static/css/goodsManage.css" type="text/css"/>
	<link rel="stylesheet" href="<%=basePath %>/static/css/changeNoInfo.css" type="text/css"/>
	<link rel="stylesheet" href="<%=basePath %>/static/css/addGoods.css" type="text/css"/>
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
	</style>
</head>
<body class="easyui-layout">
<input type="hidden" id="basepath" value="<%=basePath %>"/>
<div class="goodsmBox smBox" data-options="region:'center'" style="border:0">
	<div class="easyui-layout" data-options="fit:true">
		<div class="header" id="serverManageSearch" data-options="region:'north'" style="border:0">
			<p class="navBar mgB16">
				<a class="btnG btnBl" onclick="serverManageSearch()">搜索</a>
				<a class="btnG btnBl" onclick="serverManageReset()">重置</a>
			</p>
			<table class="gList">
				<td><label class="dib w77">服务单号</label><input class="easyui-validatebox combo" name="serviceId" id="serviceId" maxlength="20"/></td>
				<td><label class="dib tar">订单号</label><input class="easyui-validatebox combo" name="ordernumber" id="ordernumber" maxlength="20"/></td>
				<td style="display: none"><label  class="dib tar">入库状态</label> <select
						id="flag" name="flag" class="easyui-combobox"
						style="width: 194px; height: 28px; line-height: 28px;">
					<option value="">请选择</option>
					<option value="0">等待</option>
					<option value="1">完成</option>

				</select></td>
			</table>
		</div>
		<div class="center" data-options="region:'center'" style="border:0">
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north'" style="padding:20px 30px 12px;border:0">
					<p class="cenBtn">
						<a class="btnG btnBl" id="checkServer">验货入库</a>
						<a class="btnG btnBl" id="serverManageExport">导出Excel</a>
						<%--<a class="btnG btnBl" id="testInfo" hidden="true">详情测试</a>--%>
					</p>
				</div>
				<!-- datagrid列表  -->
				<div data-options="region:'center'" style="padding:1px 30px 20px;border:0">
					<table id="serverStock_list" width="100%"></table>
				</div>	
			</div>	 
			
		
		</div>
	</div>
	<div id="validateInstock"></div>
</div>
<script type="text/javascript" src="<%=basePath %>/static/js//jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/static/js/easyui-1.4.5/jquery.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/static/js/easyui-1.4.5/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/static/js/easyui-1.4.5/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/LocalResizeIMG.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/LocalResizeDOC.js"></script>
<script type="text/javascript" src="<%=basePath %>/static/js/excelHead/afterReceiptInStockHead.js"></script>
<script type="text/javascript" src="<%=basePath %>/static/js/instock/serverManage.js?v=0.11"></script>

<script type="text/javascript">
//主页面表单
$('.cenList .list li').click(function () {
	var index = $(this).index();
	$(this).addClass("active").siblings().removeClass("active");
});
</script>
</body>
</html>