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
	<title>发货交接单</title>
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
	</style>
</head>
<body class="easyui-layout">
<input type="hidden" id="basepath" value="<%=basePath %>"/>
<input type="hidden" id="scanGoods" value="${scanGoods }"/>
<div class="goodsmBox smBox" data-options="region:'center'" style="border:0">
	<div class="easyui-layout" data-options="fit:true">
		<div class="header" id="deliveryListSearch" data-options="region:'north'" style="border:0">
			<p class="navBar mgB16">
				<a class="btnG btnBl" onclick="deliveryListSearchBox()">搜索</a>
				<a class="btnG btnBl" onclick="formReset()">重置</a>
			</p>
			<table class="gList">
			<td><label for="" class="dib tal">订单号</label><input type="text"
						 id="orderNo" name="orderNo"
						maxlength="20" /></td>
			<td><label for="" class="dib tar">承运商</label> <select
						id="logisComId" name="logisComId" 
						style="width: 194px; height: 28px; line-height: 28px;">
							<option value="0">请选择</option>
							<c:forEach items="${companies}" var="company">
								<option value="${company.id}">${company.name }</option>
							</c:forEach>
					</select></td>
				 <td>
				<!--<label for="" class="dib tar">交接状态</label> <select
						id="deliveryUser" name="deliveryUser" class="easyui-combobox"
						style="width: 194px; height: 28px; line-height: 28px;">
							<option value="0">请选择</option>
							<option value="1">未完成</option>
							<option value="2">已完成</option>
					</select>-->
					</td> 
				<td><label for="" class="dib tar">发货单号</label><input type="text"
						 id="deliveryNo" name="deliveryNo"
						maxlength="20" /></td>
				<td class="std"><label class="dib tar">完成时间</label>
					<input type="hidden" id="starTime" name="starTime" />
					<input type="hidden" id="endTime" name="endTime" />
					<input id="datetimeRange" size="60" value="">
				</td>
				<td style="color: #313643;font-size: 15px;"><input type="checkbox" id="doPrint" name="doPrint"  checked="checked" style="margin-left: 93px;margin-right: 17px;"/>交接单未打印</td>
			</table>
		</div>
		<div class="center" data-options="region:'center'" style="border:0">
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north'" style="padding:20px 30px 12px;border:0">
					<p class="cenBtn">
						<a class="btnG btnBl" id="printdeliveryBill">打印发货交接单</a>
					</p>
					
				</div>
				<!-- datagrid列表  -->
				<div data-options="region:'center'" style="padding:1px 30px 20px;border:0">
					<table id="delivery_list" width="100%"></table>
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
<script type="text/javascript" src="<%=basePath %>/static/js/delivery/deliveryManage.js?v=0.14"></script>

<script type="text/javascript">
//主页面表单
$('.cenList .list li').click(function () {
	var index = $(this).index();
	$(this).addClass("active").siblings().removeClass("active");
});
</script>
</body>
</html>