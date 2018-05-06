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
<title>添加仓库</title>

</head>
<body class="easyui-layout">
<form id="add" method="post">
		<ul class="inBox warehouseEdit">
			<li><label>库区编码：</label><input type="text" id="code"
				name="code" class="easyui-validatebox" data-options="required:true"/></li>
			<li><label >库区名称：</label><input type="text"
				id="areaName" name="areaName" class="easyui-validatebox" data-options="required:true"/></li>
			<li><label >状态：</label>
				<div class="radioBox dib">
					<input type="radio" id="status" name="status" value="1" checked="checked"/>启用
					<input type="radio" id="status" name="status" value="0"/>禁用
				</div></li>
			<li><label >所属仓库：</label>
				<select id="wareId" name="wareId" class="easyui-combobox"  style="width: 194px; height: 28px; line-height: 28px;" >
						<option value="0">全部仓库</option>
						<c:forEach items="${warehouseList}" var="houseList">
							<option value="${houseList.id}">${houseList.wareName}</option>
						</c:forEach>
						</select>
			</li>
			<li><label >库区性质：</label>
				<select id="type" name="type" class="easyui-combobox"  style="width: 194px; height: 28px; line-height: 28px;" >
						<option value="">全部状态</option>
						<option value="1">可卖</option>
						<option value="0">不可卖</option>
				</select>
			</li>
		</ul>
		<div class="edBox tac">
			<a href="javascript:;" class="btnG btnBl" id="addWarehouseAreaBtn" onclick="addWarehouseAreaBtn()">继续添加</a>
			<a href="javascript:;" class="btnG btnOg"  onclick="cancelWarehouseAreaBtn()">取消</a>
		</div>
</form>
<script type="text/javascript" src="<%=basePath%>/static/js/warehouseArea/warehouseAreaAdd.js"></script>
</body>
</html>