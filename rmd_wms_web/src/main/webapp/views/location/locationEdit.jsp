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
<form id="edit" method="post">
		<input type="hidden" id="id" name="id" value="${location.id}">
		<ul class="inBox">
			<li><label>货位号：</label><input type="text" id="locationNo" style="width:184px;border-radius:4px;border-color: #c9c9c9;height: 28px; line-height: 28px;"
				name="locationNo" value="${location.locationNo}" class="easyui-validatebox" data-options="required:true"/></li>
			<li><label>所属仓库：</label>
				<select id="wareId" name="wareId" class="easyui-combobox"  style="width: 194px; height: 28px; line-height: 28px;" >
						<option value="0">全部仓库</option>
						<c:forEach items="${warehouseList}" var="houseList">
							<option value="${houseList.id}" <c:if test="${houseList.id == location.wareId}">selected</c:if>>${houseList.wareName}</option>
							
						</c:forEach>
				</select>
			</li>
			<li><label>所属库区：</label>
				<select id="areaId" name="areaId" class="easyui-combobox"  style="width: 194px; height: 28px; line-height: 28px;" >
					<option value="0">全部库区</option>
					<c:forEach items="${warehouseAreaList}" var="areaList">
						<option value="${areaList.id}" <c:if test="${areaList.id == location.areaId}">selected</c:if>>${areaList.areaName}</option>
					</c:forEach>
				</select>
			</li>
			<li><label>状态：</label>
				<div class="radioBox dib">	
					<input type="radio" id="status" name="status" value="1" <c:if test="${location.status == 1}">checked</c:if>/>启用
					<input type="radio" id="status" name="status" value="0" <c:if test="${location.status == 0}">checked</c:if>/>禁用
				</div>
		    </li>
		</ul>
		<div class="edBox tac">
			<a href="javascript:;" class="btnG btnBl" id="editlocationBtn" onclick="editlocationBtn()">编辑</a>
			<a href="javascript:;" class="btnG btnOg"  onclick="cancelLocationBtn()">取消</a>
		</div>
</form>
<script type="text/javascript" src="<%=basePath%>/static/js/location/locationEdit.js"></script>

</body>
</html>