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
	<title>承运商管理</title>

	<link rel="stylesheet" href="<%=basePath %>/static/css/common/reset.css" type="text/css"/>
	<link rel="stylesheet" href="<%=basePath %>/static/css/goodsManage.css" type="text/css"/>
	<link rel="stylesheet" href="<%=basePath %>/static/css/changeNoInfo.css" type="text/css"/>
	<link rel="stylesheet" href="<%=basePath %>/static/css/memberManage.css" type="text/css"/>
	<link rel="stylesheet" type="text/css" href="<%=basePath %>/static/js/easyui-1.4.5/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=basePath %>/static/js/easyui-1.4.5/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=basePath %>/static/js/easyui-1.4.5/demo.css">
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
		em{color:red;font-size:12px;}
	</style>
</head>
<body class="easyui-layout">
<input type="hidden" id="basepath" value="<%=basePath %>"/>
<input type="hidden" id="scanGoods" value="${scanGoods }"/>
<div class="goodsmBox smBox" data-options="region:'center'" style="border:0">
	<div class="easyui-layout" data-options="fit:true">
		<div class="header" id="logisticsListSearch" data-options="region:'north'" style="border:0">
			<p class="navBar mgB16">
				<a class="btnG btnBl" onclick="logisticsListSearchBox()">搜索</a>
				<a class="btnG btnBl" onclick="formReset()">重置</a>
			</p>
		 <table class="gList">
			 <tr>
					<td>
						<label  class="dib w56">承运商</label>
						<select 	id="logisComId" name="logisComId" class="easyui-combobox" style="width: 194px; height: 28px; line-height: 28px;">
							<option value="0">请选择</option>
							<c:forEach var="company" items="${pList}">
								<option value="${company.id}">${company.name}</option>
							</c:forEach>
						</select>
					</td>
			 </tr>
				</table>
		</div>
		<div class="center" data-options="region:'center'" style="border:0">
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north'" style="padding:20px 30px 12px;border:0">
					<p class="cenBtn">
						<shiro:hasPermission name="WMS_LOGISTICS_OPEN">
						<a class="btnG btnBl" id="openLogistics">启用</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="WMS_LOGISTICS_FORBID">
						<a class="btnG btnBl" id="forbidLogistics">禁用</a>
						</shiro:hasPermission>

						<shiro:hasPermission name="WMS_LOGISTICS_ADD">
						<a  href="javascript:void(0);" class="btnG btnBl" id="addLogistics" >添加承运商</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="WMS_LOGISTICS_EDIT">
							<a class="btnG btnBl" id="editLogistics">编辑基本信息</a>
						</shiro:hasPermission>
						<a class="btnG btnBl" id="editFreightTemplate">编辑运费模板</a>
					</p>
				</div>
				<!-- datagrid列表  -->
				<div data-options="region:'center'" style="padding:1px 30px 20px;border:0">
					<table id="logistics_list" width="100%"></table>
				</div>	
				<div id="addLogisticsAdFreightBox" class="memEdit hide"></div>
				<div id="editLogisticsBox" class="memEdit hide"></div>
				<div id="editFreightTemplateBox" class="memEdit hide"></div>
			</div>
			
				
		</div>
	</div>
</div>
<script type="text/javascript" src="<%=basePath %>/static/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/static/js/easyui-1.4.5/jquery.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/static/js/easyui-1.4.5/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/static/js/easyui-1.4.5/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/LocalResizeIMG.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/LocalResizeDOC.js"></script>
<script type="text/javascript" src="<%=basePath %>/static/js/layer/layer.js"></script>
<script type="text/javascript" src="<%=basePath %>/static/js/logistics/logisticsManage.js?v=0.18"></script>


<script type="text/javascript">
//主页面表单
$('.cenList .list li').click(function () {
	var index = $(this).index();
	$(this).addClass("active").siblings().removeClass("active");
});

</script>
</body>
</html>