<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
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
.textbox{margin-left: -4px!important;}
.warehouseEdit li{
	padding:10px 0;
}
.warehouseEdit li>label,.warehouseEdit .radioBox {
	line-height:30px;
}
.warehouseEdit .radioBox  input[type='radio']{
	margin:0 5px 0 15px;
}
.warehouseEdit .radioBox  input[type='radio']:first-child{margin-left:0}
</style>
</head>
<body class="easyui-layout">
	<div class="goodsmBox smBox" data-options="region:'center'"
		style="border: 0">
		<div class="easyui-layout" data-options="fit:true">
			<div class="header" id="warehouseAreaListSearch"
				data-options="region:'north'" style="border: 0">
				<p class="navBar mgB16">
					<a class="btnG btnBl" onclick="warehouseAreaSearchBox()">搜索</a> 
					<a class="btnG btnBl" onclick="warehouseAreaReset()">重置</a>
				</p>
				<table class="gList">
					<td><label for="" class="dib w77">库区编号</label><input
						class="easyui-validatebox combo"  id="code" name="code" maxlength="20" /></td>
					<td><label for="" class="dib tar">库区名称</label><input
						class="easyui-validatebox combo"  id="areaName" name="areaName" style="width: 184px; height: 28px; line-height: 28px;" /></td>
					<td>
					<td><label for="" class="dib tar">状态</label>
						<select id="status" name="status" class="easyui-combobox"  style="width: 194px; height: 28px; line-height: 28px;" >
						<option value="">全部状态</option>
						<option value="1">启用</option>
						<option value="0">禁用</option>
						</select>
				    </td>
				    <td><label for="" class="dib w77">所属仓库</label>
						<select id="wareId" name="wareId" class="easyui-combobox"  style="width: 194px; height: 28px; line-height: 28px;" >
						<option value="">全部仓库</option>
						<c:forEach items="${warehouseList}" var="houseList">
							<option value="${houseList.id}">${houseList.wareName}</option>
						</c:forEach>
						</select>
				    </td>
				    <td><label for="" class="dib tar">库区性质</label>
						<select id="type" name="type" class="easyui-combobox"  style="width: 194px; height: 28px; line-height: 28px;" >
						<option value="">全部性质</option>
						<option value="0">不可卖</option>
						<option value="1">可卖</option>
						</select>
				    </td>
				</table>
			</div>
			<div class="center" data-options="region:'center'" style="border: 0">
				<div class="easyui-layout" data-options="fit:true">
					<div data-options="region:'north'"
						style="padding: 20px 30px 12px; border: 0">
						<p class="cenBtn">
						<shiro:hasPermission name="WMS_WAREAREA_OPEN">
							<a class="btnG btnBl"  onclick="warehouseAreaStatusUpdate(1)">启用</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="WMS_WAREAREA_FORBID">
							<a class="btnG btnBl"  onclick="warehouseAreaStatusUpdate(0)">禁用</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="WMS_WAREAREA_EDIT">
							<a class="btnG btnBl" id="warehouseAreaEdit">编辑</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="WMS_WAREAREA_ADD">
							<a class="btnG btnBl" id="warehouseAreaAdd">+添加库区</a>
							</shiro:hasPermission>
						</p>

					</div>
					<!-- datagrid列表  -->
					<div data-options="region:'center'"
						style="padding: 1px 30px 20px; border: 0">
						<table id="warehouseArea_list" width="100%"></table>
					</div>
				</div>
				<!--编辑库区弹窗页面 -->
				<div id="editWarehouseArea" class="editNoticBox"></div>
				<!-- 添加库区弹窗页面-->
				<div id="addWareaHouseArea" class="addNoticBox"></div>
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
	<script type="text/javascript"
		src="<%=basePath%>/static/js/warehouseArea/warehouseAreaManage.js"></script>

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