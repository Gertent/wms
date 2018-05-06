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
<title>录入运单号</title>
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
<script type="text/javascript"
	src="<%=basePath%>/static/js/ckeditor/ckeditor.js"></script>
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
.gList td label {
    width: 62px;
}
</style>
</head>
<body class="easyui-layout">
	<input type="hidden" id="basepath" value="<%=basePath%>" />
	<input type="hidden" id="scanGoods" value="${scanGoods }" />
	<div class="goodsmBox smBox" data-options="region:'center'"
		style="border: 0">
		<div class="easyui-layout" data-options="fit:true">
			<div class="header" id="logisticsListSearch"
				data-options="region:'north'" style="border: 0">
				<p class="navBar mgB16">
					<a class="btnG btnBl" onclick="logisticsListSearchBox()">搜索</a> <a
						class="btnG btnBl" onclick="formReset()">重置</a>
				</p>
				<table class="gList">
					<td><label for="" class="dib tar">订单号</label><input
						class="easyui-validatebox combo" id="orderNo" name="orderNo"
						maxlength="20" /></td>
					<td><label for="" class="dib tar">承运商</label> <select
						id="logisComId" name="logisComId" class="easyui-combobox"
						style="width: 194px; height: 28px; line-height: 28px;">
							<option value="0">请选择</option>
							<c:forEach items="${companies}" var="company">
								<option value="${company.id}">${company.name }</option>
							</c:forEach>

					</select></td>
					<td style="font-size:15px;"><input type="checkbox" id="dowaybillPrint"
						name="dowaybillPrint" style="margin-right: 10px;" />未打印
						<%--<input type="checkbox" id="noLogisticsNo" name="noLogisticsNo" />无运单号--%>

					</td>
				</table>
			</div>
			<div class="center" data-options="region:'center'" style="border: 0">
				<div class="easyui-layout" data-options="fit:true">
					<div data-options="region:'north'"
						style="padding: 20px 30px 12px; border: 0">
						<p class="cenBtn">
							<a class="btnG btnBl" id="logisticsNoAdd">录入运单号</a>
						</p>

					</div>
					<!-- datagrid列表  -->
					<div data-options="region:'center'"
						style="padding: 1px 30px 20px; border: 0">
						<table id="logistics_list" width="100%"></table>
					</div>
				</div>

			</div>
			<div class="tabContentBox" style="display: none;" id="editLogistics">
				<ul class="inBox">
					<input type="hidden" id="Lid" name="id" />
					<li><label for="">订单号： </label><label id="LorderNo" /></li>
					<li><label for="">承运商：</label><label id="logisComName"
						name="logisComName" /></li>
					<li><label for="">运单号：</label><input id="logisticsNo"
						name="logisticsNo" /></li>
				</ul>
				<div class="edBox tac">
					<a href="javascript:;" class="btnG btnBl" id="commitedit">确定</a> <a
						href="javascript:;" class="btnG btnOg" id="cancel">取消</a>
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
	<script type="text/javascript"
		src="<%=basePath%>/static/js/outstock/logisticsNumberManage.js?v=0.06"></script>

	<script type="text/javascript">
		//主页面表单
		$('.cenList .list li').click(function() {
			var index = $(this).index();
			$(this).addClass("active").siblings().removeClass("active");
		});
	</script>
</body>
</html>