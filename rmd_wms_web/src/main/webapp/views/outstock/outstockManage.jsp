<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>
<!DOCTYPE html>
<html>
<head lang="en">
	<meta charset="UTF-8">
	<title>出库制单</title>
	<link rel="stylesheet" href="<%=basePath %>/static/css/common/reset.css" type="text/css"/>
	<link rel="stylesheet" href="<%=basePath %>/static/css/goodsManage.css" type="text/css"/>
	<link rel="stylesheet" href="<%=basePath %>/static/css/changeNoInfo.css" type="text/css"/>
	<link rel="stylesheet" href="<%=basePath %>/static/css/addGoods.css" type="text/css"/>
	<link rel="stylesheet" href="<%=basePath %>/static/css/outstock.css" type="text/css"/>
	<link rel="stylesheet" href="<%=basePath%>/static/js/date-range-picker/dist/daterangepicker.min.css">
	<link rel="stylesheet" type="text/css" href="<%=basePath %>/static/js/easyui-1.4.5/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=basePath %>/static/js/easyui-1.4.5/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=basePath %>/static/js/easyui-1.4.5/demo.css">
	<script type="text/javascript" src="<%=basePath %>/static/js//jquery-1.8.3.min.js"></script>
	<style>
		.datagrid-btable .datagrid-cell{padding:6px 4px;overflow: hidden;text-overflow:ellipsis;white-space: nowrap;} 	
	    .gList td input[type=checkbox] {
    		margin-right: 10px;
		}
		#editLogisticsCompany .inBox>li>label{
			width:22%;
			line-height:26px;
		}
	</style>
</head>
<body class="easyui-layout">
<input type="hidden" id="basepath" value="<%=basePath %>"/>
<input type="hidden" id="scanGoods" value="${scanGoods }"/>
<div class="goodsmBox smBox" data-options="region:'center'" style="border:0">
	<div class="easyui-layout" data-options="fit:true">
		<div class="header" id="outstockListSearch" data-options="region:'north'" style="border:0">
			<p class="navBar mgB16">
				<a class="btnG btnBl" onclick="outstockListSearch()">搜索</a>
				<a class="btnG btnBl" onclick="formReset()">重置</a>
			</p>
				<table class="gList">
					<tr>
						<td><label class="dib tar">下单时间</label>
							<input type="hidden" id="starTime" name="starTime" />
							<input type="hidden" id="endTime" name="endTime" />
							<input id="datetimeRange" size="60" value="">
						</td>
						<td><label  class="dib tar">承运商</label>
							<select id="logisComId" name="logisComId" class="easyui-combobox" style="width: 194px; height: 28px; ">
								<option value="0">请选择</option>
								<c:forEach items="${companies}" var="company">
									<option value="${company.id}">${company.name }</option>
								</c:forEach>
							</select>
						</td>
						<td>
							<label  class="dib tar">运单号</label>
							<input	class="easyui-validatebox combo" id="logisticsNo" name="logisticsNo" maxlength="20" />
						</td>
					</tr>
					<tr>
						<td><label  class="dib">订单号</label><input
								class="easyui-validatebox combo" id="orderNo" name="orderNo"
								maxlength="20" /></td>
						<td>
							<label  class="dib tar">状态</label>
							<select id="status" name="status" class="easyui-combobox" style="width: 194px; height: 28px; line-height: 28px;">
								<option value="-1">请选择</option>
								<option value="12101">拣货</option>
								<option value="12102">打包复检</option>
								<option value="12103">录入运单号</option>
								<option value="12104">交接发货</option>
							</select>
						</td>
						<td class="">
							<label  class="dib tar">发货单号</label><input class="easyui-validatebox combo" id="deliveryNo" name="deliveryNo" maxlength="20" />
						</td>
					</tr>
					<tr>
						<td colspan="3" style="width: initial;">
							<input type="checkbox" id="dobinningPrint" name="dobinningPrint"/>装箱单未打印
							<input type="checkbox" id="dopickingPrint" name="dopickingPrint"/>拣货单未打印
							<input type="checkbox" id="dowaybillPrint" name="dowaybillPrint"/>运单未打印
							<%--<input type="checkbox" id="pickingStatus" name="pickingStatus" />拣货缺货--%>
						</td>
					</tr>
				</table>
			</div>
		<div class="center" data-options="region:'center'" style="border:0">
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north'" style="padding:20px 30px 12px;border:0">
					<p class="cenBtn">
						<shiro:hasPermission name="WMS_OUTSTOCK_PRINTINBOX">
							<a class="btnG btnBl" id="printInBoxBill">打印装箱单</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="WMS_OUTSTOCK_PRINTPACK"> 
							<a class="btnG btnBl" id="printPackBill">打印拣货单</a>
						</shiro:hasPermission>
						 <shiro:hasPermission name="WMS_OUTSTOCK_PRINTLOGIC">
							<!--  <a class="btnG btnBl" id="printLogicBill">打印运单</a>-->
							<a class="btnG btnBl" id="printLogisticsBillIpt">打印运单</a>
						 </shiro:hasPermission> 
						<shiro:hasPermission name="WMS_OUTSTOCK_EDITLOGIC">
							<a class="btnG btnBl" id="updateLogistics">修改承运商</a>
						 </shiro:hasPermission>
						<%--隐藏模拟数据--%>
						<%--<a class="btnG btnBl" id="insertDemo">批量模拟数据</a>--%>
						<%--<a class="btnG btnBl" id="outStockExport">导出</a>--%>
						<a class="btnG btnBl" id="outStockBillExport">导出Excel</a>
					</p>
				</div>
				<!-- datagrid列表  -->
				<div data-options="region:'center'" style="padding:1px 30px 20px;border:0">
					<table id="outstock_list" width="100%"></table>
				</div>	
			</div>	 
			<div class="tabContentBox" style="display: none;" id="editLogisticsCompany">
				<ul class="inBox " style="margin-top: 50px; margin-bottom: 35px;">
					<input type="hidden" id="Lid" name="id" />
					<li><label>承运商：</label>
					<select name="logisticsCompany" id="logisticsCompany" 
						style="width: 194px; height: 28px; line-height: 28px;border-color:#c9c9c9;border-radius: 4px;"></select>
					</li>
				</ul>
				<div class="edBox tac">
					<a href="javascript:;" class="btnG btnBl" id="commitedit">确定</a> 
					<a href="javascript:;" class="btnG btnOg" id="cancel">取消</a>
				</div>
			</div>
			<div id="purchaseBillGoods"></div>		 
		</div>
	</div>
</div>

<script type="text/javascript" src="<%=basePath %>/static/js/easyui-1.4.5/jquery.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/static/js/easyui-1.4.5/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/static/js/easyui-1.4.5/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/LocalResizeIMG.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/LocalResizeDOC.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/date-range-picker/dist/moment.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/date-range-picker/dist/jquery.daterangepicker.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/static/js/excelHead/outStockBillHead.js?v=0.01"></script>
<script type="text/javascript" src="<%=basePath %>/static/js/outstock/outstockManage.js?v=0.23"></script>

<script type="text/javascript">
//主页面表单
$('.cenList .list li').click(function () {
	var index = $(this).index();
	$(this).addClass("active").siblings().removeClass("active");
});
</script>
</body>
</html>