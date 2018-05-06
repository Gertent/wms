<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
%>
<!DOCTYPE html>
<html>
<head lang="en">
<meta charset="UTF-8">
<title>库区管理</title>
<link rel="stylesheet" href="<%=basePath%>/static/css/common/reset.css"
	type="text/css" />
<link rel="stylesheet" href="<%=basePath%>/static/css/goodsManage.css"
	type="text/css" />
<link rel="stylesheet" href="<%=basePath%>/static/css/changeNoInfo.css"
	type="text/css" />
<link rel="stylesheet" href="<%=basePath%>/static/css/addGoods.css"
	type="text/css" />
<link rel="stylesheet" href="<%=basePath%>/static/js/date-range-picker/dist/daterangepicker.min.css">
<link rel="stylesheet" href="<%=basePath %>/static/css/movement.css" type="text/css"/>
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>/static/js/easyui-1.4.5/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>/static/js/easyui-1.4.5/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>/static/js/easyui-1.4.5/demo.css">

<style>
.datagrid-btable .datagrid-cell {
	padding: 6px 4px;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
}

.tableCon {
	border: 1px #dcdcdc solid;
}

.tableCon tr {
	border-bottom: 1px #dcdcdc solid;
}

.tableCon td,.tableCon th {
	text-align: center;
	border-right: 1px #dcdcdc solid;
}
.magR3{margin-right:3px;}
</style>
</head>
<body class="easyui-layout">
<input type="hidden" id="basepath" value="<%=basePath %>"/>
	<div class="goodsmBox smBox" data-options="region:'center'"
		style="border: 0">
		<div class="easyui-layout" data-options="fit:true">
			<div class="header" id="locationGoodBindListSearch"
				data-options="region:'north'" style="border: 0">
				<p class="navBar mgB16">
					<a class="btnG btnBl" onclick="locationGoodsBindSearchBox()">搜索</a> 
					<a class="btnG btnBl" onclick="locationGoodsBindReset()">重置</a>
				</p>
				<table class="gList">
					<td><label  class="dib tar">商品编码</label><input
						class="easyui-validatebox combo"  id="goodsCode" name="goodsCode" maxlength="20" /></td>
					<td>
						<label class="dib tar">有效期</label>
						<input type="hidden" id="validityTime" name="validityTime" />
						<input id="timeRangeEx" size="60" />
					</td>

					<td>
						<label class="dib tar">入库日期</label>
						<input type="hidden" id="createTime" name="createTime" />
						<input id="timeRange" size="60" />
					</td>
					<td><label  class="dib tar">商品名称</label><input
							class="easyui-validatebox combo"  id="goodsName" name="goodsName" maxlength="20" /></td>

					<td><label  class="dib tar">库区</label>
						<select id="areaId" name="areaId" class="easyui-combobox" data-options="panelHeight:300" style="width: 194px; height: 28px; line-height: 28px;" >
						<option value="">全部库区</option>
						<c:forEach items="${warehouseAreaList}" var="areaList">
							<option value="${areaList.id}">${areaList.areaName}-${areaList.code}</option>
						</c:forEach>
						</select>
				    </td>
				    <td><label  class="dib tar">货位号</label>
						<select id="locationId" name="locationId" class="easyui-combobox" data-options="panelHeight:300,valueField:'id',textField:'locationNo',multiple:true" style="width: 194px; height: 28px; line-height: 28px;" >
						<%--<option value="">全部货位号</option>--%>
						<c:forEach items="${locationList}" var="locaList">
							<option value="${locaList.id}">${locaList.locationNo}</option>
						</c:forEach>
						</select>
				    </td>
				</table>
			</div>
			<div class="center" data-options="region:'center'" style="border: 0">
				<div class="easyui-layout" data-options="fit:true">
							    <div data-options="region:'north'" style="padding:20px 30px 12px;border:0">
					<p class="cenBtn">
						<!--  空一行，页面美观度-->
						<a class="btnG btnBl" id="locationGoodsBindExport">导出Excel</a>
					</p>
					
				</div>
					<!-- datagrid列表  -->
					<div data-options="region:'center'"
						style="padding: 1px 30px 20px; border: 0">
						<table id="locationGoodsBind_list" width="100%"></table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript"
		src="<%=basePath%>/static/js//jquery-1.8.3.min.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>/static/js/easyui-1.4.5/jquery.min.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>/static/js/easyui-1.4.5/jquery.easyui.min.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>/static/js/easyui-1.4.5/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>/static/js/LocalResizeIMG.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>/static/js/LocalResizeDOC.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/date-range-picker/dist/moment.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/date-range-picker/dist/jquery.daterangepicker.min.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>/static/js/locationGoodsBind/locationGoodsBindManage.js?v=0.16"></script>

	<script type="text/javascript">
		//主页面表单
		$('.cenList .list li').click(function() {
			var index = $(this).index();
			$(this).addClass("active").siblings().removeClass("active");
		});
		//新增仓库
		$('#warehouseAreaAdd').click(function(){
		 	$(".addNoticBox").window({
		  		href:'<%=basePath%>/warehouseArea/warehouseAreaAddPage',
		  		 width:800,
		         height:435,
		         modal:true,
		         shadow:false,
		         collapsible:false,
		         minimizable:false,
		         maximizable:false,
		         title:"添加库区"
		      }); 
		  });
		//编辑仓库
		//修改弹窗
	  $('#warehouseAreaEdit').click(function(){
	  	var rows = $('#warehouseArea_list').datagrid('getSelections');
	  	if(rows == ""){
	  		$.messager.alert("提示", "请选择一条数据 ！", "info");   
	  		return;
	  	}
	  	if(rows.length > 1){
	  		$.messager.alert("提示", "请选择一条数据 ！", "info");   
	  		return;
	  	}
	  	$('.editNoticBox').window({
	  			href:"<%=basePath%>/warehouseArea/wareAreaEditPage?id=" + rows[0].id,
				width : 800,
				height : 435,
				modal : true,
				shadow : false,
				collapsible : false,
				minimizable : false,
				maximizable : false,
				minimizable : false,
				maximizable : false,
				title : "编辑库区"

			});
		});
	</script>
</body>
</html>