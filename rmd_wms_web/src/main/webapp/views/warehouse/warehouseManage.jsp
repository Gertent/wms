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
<title>仓库管理</title>
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
#PROVINCE_edit>label,
#PROVINCE>label{
	display:inline-block;
	width:150px;
}
.addNoticBox .inBox #PROVINCE input[type="checkbox"]{width:auto;}
</style>
</head>
<body class="easyui-layout">
	<div class="goodsmBox smBox" data-options="region:'center'"
		style="border: 0">
		<div class="easyui-layout" data-options="fit:true">
			<div class="center" data-options="region:'center'" style="border: 0">
				<div class="easyui-layout" data-options="fit:true">
					<div data-options="region:'north'"
						style="padding: 20px 30px 12px; border: 0">
						<p class="cenBtn">
							<shiro:hasPermission name="WMS_WAREHOUSE_OPEN">
							<a class="btnG btnBl" id="warehouseStatusUpdate" onclick= "warehouseStatusUpdate(1)">启用</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="WMS_WAREHOUSE_FORBID">
							<a class="btnG btnBl" id="warehouseStatusUpdate" onclick = "warehouseStatusUpdate(0)">禁用</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="WMS_WAREHOUSE_ALLOCATION">
							<a class="btnG btnBl" id="allocation" >分配员工</a> <!-- onclick="allocationBtn()" -->
							</shiro:hasPermission>
							<shiro:hasPermission name="WMS_WAREHOUSE_EDIT">
							<a class="btnG btnBl" id="warehouseEdit">编辑</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="WMS_WAREHOUSE_ADD">
							<a class="btnG btnBl" id="warehouseAdd">+添加仓库</a>
							</shiro:hasPermission>
						</p>

					</div>
					<!-- datagrid列表  -->
					<div data-options="region:'center'"
						style="padding: 1px 30px 20px; border: 0">
						<table id="warehouse_list" width="100%"></table>
					</div>
				</div>
				<!--编辑页面 弹窗-->
				<div id="edit" class="editNoticBox"></div>
				<!-- 添加页面弹窗 -->
				<div id="add" class="addNoticBox"></div>
				<!-- 分配员工弹窗 -->
				<div id="allot" class="allocationNoticBox" ></div>
				
			</div>
		</div>
	</div>
	<script type="text/javascript"
		src="<%=basePath%>/static/js/jquery-1.8.3.min.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>/static/js/easyui-1.4.5/jquery.min.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>/static/js/easyui-1.4.5/jquery.easyui.min.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>/static/js/easyui-1.4.5/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>/static/js/warehouse/warehouseManage.js?v=0.05"></script>
	<%-- 	<script type="text/javascript"
		src="<%=basePath%>/static/js/warehouse/allocationInfo.js?v=0.02"></script> --%>

	<script type="text/javascript">
		//主页面表单
		$('.cenList .list li').click(function() {
			var index = $(this).index();
			$(this).addClass("active").siblings().removeClass("active");
		});
		
		//新增仓库
		$('#warehouseAdd').click(function(){
		 	$(".addNoticBox").window({
		  		href:"<%=basePath%>/warehouse/warehouseAddPage",
		  		 width:800,
		         height:530,
		         modal:true,
		         shadow:false,
		         collapsible:false,
		         minimizable:false,
		         maximizable:false,
		         title:"添加仓库"
		      }); 
		  });
		//编辑仓库
		//修改弹窗
	  $('#warehouseEdit').click(function(){
	  	var id = "";
	  	var rows = $('#warehouse_list').datagrid('getSelections');
	  	if(rows == ""){
	  		$.messager.alert("提示", "请选择一条数据 ！", "info");   
	  		return;
	  	}
	  	if(rows.length > 1){
	  		$.messager.alert("提示", "请选择一条数据 ！", "info");   
	  		return;
	  	}
	  	$('.editNoticBox').window({
	  			href:"<%=basePath%>/warehouse/wareEditPage?id=" + rows[0].id,
				width : 800,
				height : 530,
				modal : true,
				shadow : false,
				collapsible : false,
				minimizable : false,
				maximizable : false,
				title : "编辑仓库"

			}); 
		});
		
	//分配员工弹窗
 	 $('#allocation').click(function(){
 	  	var id = "";
 	  	var rows = $('#warehouse_list').datagrid('getSelections');
 	  	if(rows == ""){
 	  		$.messager.alert("提示", "请选择一条数据 ！", "info");   
 	  		return;
 	  	}
 	  	if(rows.length > 1){
 	  		$.messager.alert("提示", "只能选择一条数据 ！", "info");   
 	  		return;
 	  	}
 	  	$('.allocationNoticBox').window({
 	  			href:"<%=basePath%>/warehouse/allocationWarehouse?id=" + rows[0].id, 
 				width : 800,
 				height : 435,
 				modal : true,
 				shadow : false,
 				collapsible : false,
 				minimizable : false,
 				maximizable : false,
 				title : "分配员工"

 			}); 
 		});
	</script>
</body>
</html>