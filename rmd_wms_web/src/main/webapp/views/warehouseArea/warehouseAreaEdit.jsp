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
<title>编辑仓库</title>
</head>
<body class="easyui-layout">
<form id="edit" method="post">
		<input type="hidden" id="id" name="id" value="${warehouseArea.id}">
		<ul class="inBox warehouseEdit">
			<li><label >库区编码：</label><input type="text" id="code"
				name="code" value="${warehouseArea.code}" class="easyui-validatebox" data-options="required:true"/></li>
			<li><label >库区名称：</label><input type="text"
				id="areaName" name="areaName" value="${warehouseArea.areaName}" class="easyui-validatebox" data-options="required:true"/></li>
			<li><label >状态：</label>
				<div class="radioBox dib">
					<input type="radio" id="status" name="status" value="1" <c:if test="${warehouseArea.status == 1}">checked</c:if>/>启用
					<input type="radio" id="status" name="status" value="0" <c:if test="${warehouseArea.status == 0}">checked</c:if>/>禁用
				</div></li>
			<li><label >所属仓库：</label>
				<select id="wareId" name="wareId" class="easyui-combobox"  style="width: 194px; height: 28px; line-height: 28px;" >
						<option value="0">全部仓库</option>
						<c:forEach items="${warehouseList}" var="houseList">
							<option value="${houseList.id}" <c:if test="${houseList.id == warehouseArea.wareId }">selected</c:if> >${houseList.wareName}</option>
						</c:forEach>
				</select>
			</li>
			<li><label >库区性质：</label>
				<select id="type" name="type" class="easyui-combobox"  style="width: 194px; height: 28px; line-height: 28px;" >
						<option value="">全部状态</option>
						<option value="1" <c:if test="${warehouseArea.type ==1 }">selected</c:if>>可卖</option>
						<option value="0" <c:if test="${warehouseArea.type ==0 }">selected</c:if>>不可卖</option>
				</select>
			</li>
		</ul>
		<div class="edBox tac">
			<a href="javascript:;" class="btnG btnBl" id="editWarehouseAreaBtn" onclick="editWarehouseAreaBtn()">编辑</a>
			<a href="javascript:;" class="btnG btnOg" id="cancelWarehouseAreaBtn" onclick="cancelWarehouseAreaBtn()">取消</a>
		</div>
</form>
<script type="text/javascript" src="<%=basePath%>/static/js/warehouseArea/warehouseAreaEdit.js"></script>
</body>
</html>