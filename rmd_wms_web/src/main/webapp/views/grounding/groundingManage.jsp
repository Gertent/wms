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
	<title>上架计划</title>
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
		.gList td.std{
		    width: 36%;
		}
	</style>
</head>
<body class="easyui-layout">
<input type="hidden" id="basepath" value="<%=basePath %>"/>
<input type="hidden" id="scanGoods" value="${scanGoods }"/>
<div class="goodsmBox smBox" data-options="region:'center'" style="border:0">
	<div class="easyui-layout" data-options="fit:true">
		<div class="header" id="groundListSearch" data-options="region:'north'" style="border:0">
			<p class="navBar mgB16">
				<a class="btnG btnBl" onclick="groundListSearchBox()">搜索</a>
				<a class="btnG btnBl" onclick="formReset()">重置</a>
			</p>
	
				<table class="gList">
					<tr>
						<td clsaa="std"><label  class="dib">入库单号</label><input
						class="easyui-validatebox combo" id="inStockNo" name="inStockNo"
						maxlength="20" /></td>
					<td><label  class="dib tar">上架状态</label> <select
						id="status" name="status" class="easyui-combobox"
						style="width: 194px; height: 28px; line-height: 28px;">
							<option value="0">请选择</option>
							<option value="1">等待</option>
							<option value="2">上架中</option>
							<option value="3">完成</option>
					</select></td>
					<td>
						<label class="dib tar">入库日期</label>
						<input type="hidden" id="createTime" name="createTime" />
						<input id="timeRange" size="60" />
					<td>
					</tr>
					<tr>
					<td class="std"><label class="dib tar">上架完成日期</label>
						<input type="hidden" id="starTime" name="starTime" />
						<input type="hidden" id="endTime" name="endTime" />
						<input id="datetimeRange" size="60" />
					</td>
					<td></td>
					<td>
						<label  class="dib tar" style="padding-right:20px;"><input type="checkbox" id="twostatus" name="twostatus" checked='checked'/></label>未完成上架
					</td>
					</tr>										
				</table>
			
		</div>
		<div class="center" data-options="region:'center'" style="border:0">
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north'" style="padding:20px 30px 12px;border:0">
					<p class="cenBtn">
						<a class="btnG btnBl" id="excelGroupingBill">导出Excel</a>
					</p>
					
				</div>
				<!-- datagrid列表  -->
				<div data-options="region:'center'" style="padding:1px 30px 20px;border:0">
					<table id="grounding_list" width="100%"></table>
				</div>	
			</div>	 
			<div id="purchaseBillGoods"></div>		 
	
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
<script type="text/javascript" src="<%=basePath %>/static/js/excelHead/groundingBillHead.js?v=0.01"></script>
<script type="text/javascript" src="<%=basePath %>/static/js/grounding/groundingManage.js?v=0.10"></script>

<script type="text/javascript">
//主页面表单
$('.cenList .list li').click(function () {
	var index = $(this).index();
	$(this).addClass("active").siblings().removeClass("active");
});
</script>
</body>
</html>