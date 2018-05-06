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
<title>添加货位</title>

</head>
<body class="easyui-layout">
<!-- 	<div class="tabContentBox" style="display: none;" -->
<!-- 		id="addPurchaseBiLLInfo"> -->
<form id="add" method="post">
		<ul class="inBox">
			<li><label>货位号：</label><input type="text" id="locationNo" style="width: 184px; height: 28px; line-height: 28px; border-color:#c9c9c9;border-radius:4px"
				name="locationNo" class="easyui-validatebox" data-options="required:true"/></li>
			<li><label>所属仓库：</label>
				<select id="wareId" name="wareId"   style="width: 194px; height: 28px; line-height: 28px;border-color:#c9c9c9;border-radius:4px;margin-left:-5px" onchange="wareChange(this.options[this.options.selectedIndex].value)">
						<option value="0">请选择</option>
						<c:forEach items="${warehouseList}" var="houseList">
							<option value="${houseList.id}">${houseList.wareName}</option>
						</c:forEach>
				</select>
			</li>
			<li><label>所属库区：</label>
				<select id="areaId" name="areaId"  style="width: 194px; height: 28px; line-height: 28px;border-color:#c9c9c9;border-radius:4px;margin-left:-5px" >
						<%--<option value="0">请选择</option>
						 <c:forEach items="${warehouseAreaList}" var="areaList">
							<option value="${areaList.id}">${areaList.areaName}</option>
						</c:forEach> --%>
				</select>
			</li>
			<li><label>状态：</label>
				<div class="radioBox dib">
					<input type="radio" id="status" name="status" value="1" checked="checked"/>启用
					<input type="radio" id="status" name="status" value="0"/>禁用
				</div>
		    </li>
		</ul>
		<div class="edBox tac">
			<a href="javascript:;" class="btnG btnBl" id="addlocationBtn" onclick="addlocationBtn()">继续添加</a>
			<a href="javascript:;" class="btnG btnOg"  onclick="cancelLocationBtn()">取消</a>
		</div>
</form>
<script type="text/javascript" src="<%=basePath%>/static/js/location/locationAdd.js"></script>
<!-- 	</div> -->
<script type="text/javascript">


</script>
</body>
</html>