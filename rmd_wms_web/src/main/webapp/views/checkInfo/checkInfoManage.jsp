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
<title>盘点记录</title>
<link rel="stylesheet" href="<%=basePath%>/static/css/common/reset.css"
	type="text/css" />
<link rel="stylesheet" href="<%=basePath%>/static/css/goodsManage.css"
	type="text/css" />
<link rel="stylesheet" href="<%=basePath%>/static/css/changeNoInfo.css"
	type="text/css" />
<link rel="stylesheet" href="<%=basePath%>/static/css/addGoods.css"
	type="text/css" />
<link rel="stylesheet" href="<%=basePath%>/static/js/date-range-picker/dist/daterangepicker.min.css">
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
.sp .textbox{margin-left: -4px!important;}
</style>
</head>
<body class="easyui-layout">
	<input type="hidden" id="basepath" value="<%=basePath %>"/>
	<div class="goodsmBox smBox" data-options="region:'center'"
		style="border: 0">
		<div class="easyui-layout" data-options="fit:true">
			<div class="header" id="checkInfoListSearch"
				data-options="region:'north'" style="border: 0">
				<p class="navBar mgB16">
					<a class="btnG btnBl" onclick="checkInfoSearchBox()">搜索</a> 
					<a class="btnG btnBl" onclick="checkInfoReset()">重置</a>
				</p>
				<table class="gList">
					<td><label class="dib w77">商品编码</label><input
						class="easyui-validatebox combo"  id="goodsCode" name="goodsCode" maxlength="20" /></td>
					<td class="std"><label class="dib tar">盘点日期</label>
						<input type="hidden" id="starTime" name="starTime" />
						<input type="hidden" id="endTime" name="endTime" />
						<input id="datetimeRange" size="60" value="">
					</td>
					<td><label class="dib tar">提报状态</label>
						<select id="submitStatus" name="submitStatus" class="easyui-combobox"  style="width: 194px; height: 28px; line-height: 28px;" >
						<option value="">全部</option>
						<option value="1">已完成</option>
						<option value="0">待处理</option>
						</select>
				    </td>
				    <td class="sp"><label class="dib w77">盘点差异</label>
						<select id="checkState" name="checkState" class="easyui-combobox"  style="width: 194px; height: 28px; line-height: 28px;" >
						<option value="0">全部状态</option>
						<option value="1">盘盈</option>
						<option value="2">盘亏</option>
						</select>
				    </td>
				    <td><label class="dib tar">盘点任务号</label><input
						class="easyui-validatebox combo"  id="checkNo" name="checkNo" style="width: 194px; height: 28px; line-height: 28px;" /></td>
					<td>
				</table>
			</div>
			<div class="center" data-options="region:'center'" style="border: 0">
				<div class="easyui-layout" data-options="fit:true">
					<div data-options="region:'north'"
						style="padding: 20px 30px 12px; border: 0">
						<p class="cenBtn">
							<%--<shiro:hasPermission name="WMS_CHECKINFO_HANDIN"> --%>
								<a class="btnG btnBl" id="handInOperate">提报</a>
							<%--</shiro:hasPermission>--%>
								<a class="btnG btnBl" id="checkInfoExport">导出Excel</a>
						</p>

					</div>
					<!-- datagrid列表  -->
					<div data-options="region:'center'"
						style="padding: 1px 30px 20px; border: 0">
						<table id="checkInfo_list" style="width:80%;margin:0 auto"></table>
					</div>
				</div>
				
				<!-- 提报界面-->
				<div id="addHandIn" class="addHandIn"></div>
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
		src="<%=basePath%>/static/js/checkInfo/checkInfoManage.js?v=0.15"></script>

	<script type="text/javascript">
		//主页面表单
		$('.cenList .list li').click(function() {
			var index = $(this).index();
			$(this).addClass("active").siblings().removeClass("active");
		});
		
		
		
		
	</script>
</body>
</html>