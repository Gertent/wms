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
		.gList td label {
    		width: 75px;
		}
		.textbox {margin-left: -4px!important;}
	</style>
</head>
<body class="easyui-layout">
<input type="hidden" id="basepath" value="<%=basePath %>"/>
<input type="hidden" id="scanGoods" value="${scanGoods }"/>
<div class="goodsmBox smBox" data-options="region:'center'" style="border:0">
	<div class="easyui-layout" data-options="fit:true">
		<div class="header" id="instockListSearch" data-options="region:'north'" style="border:0">
			<p class="navBar mgB16">
				<a class="btnG btnBl" onclick="instockSearchBox()">搜索</a>
				<a class="btnG btnBl" onclick="formReset()">重置</a>
			</p>
				<table class="gList">
					<td><label for="" class="dib w77">采购单号</label><input
						class="easyui-validatebox combo" id="purchaseNo" name="purchaseNo"
						maxlength="20" /></td>
					<td><label for="" class="dib tar">入库单号</label><input
						class="easyui-validatebox combo" id="inStockNo" name="inStockNo"
						maxlength="20" /></td>
					<td>
						<label class="dib tar">入库日期</label>
						<input type="hidden" id="instockTime" name="instockTime" />
						<input id="timeRange" size="60" />
					</td>
					<td>
					<td><label for="" class="dib w77">打印状态</label> <select
						id="status" name="doPrint" class="easyui-combobox"
						style="width: 194px; height: 28px; line-height: 28px;">
							<option value="-1">请选择</option>
							<option value="0">未打印</option>
							<option value="1">已打印</option>
					</select></td>

					<td><label for="" class="dib tar">供应商</label><input
						class="easyui-validatebox combo" id="supplierName"
						name="supplierName" maxlength="20" /></td>
				</table>
			</div>
		<div class="center" data-options="region:'center'" style="border:0">
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north'" style="padding:20px 30px 12px;border:0">
					<p class="cenBtn">
						 <a class="btnG btnBl" id="printInstockBill">打印入库单</a>
						<a class="btnG btnBl" id="excelInstockBill">导出Excel</a>
					</p>
					
				</div>
				<!-- datagrid列表  -->
				<div data-options="region:'center'" style="padding:1px 30px 20px;border:0">
					<table id="instock_list" width="100%"></table>
				</div>	
			</div>	 
			<div id="purchaseBillGoods"></div>		 
			<!--  新增商品弹窗页面 -->
			<div id="addGoods"></div>
			<!--编辑商品弹窗页面 -->
			<div id="editGoods"></div>
			<!-- 转移供应商页面 -->
			<div id="changeSupplier"></div>
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
<script type="text/javascript" src="<%=basePath %>/static/js/excelHead/inStockHead.js?v=0.02"></script>
<script type="text/javascript" src="<%=basePath %>/static/js/instock/instockManage.js?v=0.13"></script>
<script type="text/javascript">
//主页面表单
$('.cenList .list li').click(function () {
	var index = $(this).index();
	$(this).addClass("active").siblings().removeClass("active");
});
</script>
</body>
</html>