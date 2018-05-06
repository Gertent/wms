<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head lang="en">
<meta charset="UTF-8">
<title>入库管理-售后收货-手动确认</title>
<link rel="stylesheet" href="<%=basePath%>/static/css/common/reset.css"
	type="text/css" />
<link rel="stylesheet"
	href="<%=basePath%>/static/css/wishlistManage.css" type="text/css" />
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>/static/js/easyui-1.4.5/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>/static/js/easyui-1.4.5/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>/static/js/easyui-1.4.5/demo.css">
<script type="text/javascript"
	src="<%=basePath%>/static/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/static/js/easyui-1.4.5/jquery.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/static/js/easyui-1.4.5/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/static/js/easyui-1.4.5/easyui-lang-zh_CN.js"></script>

<style>
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
</style>
</head>

<body>
<input type="hidden" id="basepath" value="<%=basePath %>"/>
	<!-- 意向单查询弹框 订单详情 -->
	<div id="orderDetailsBox">

		<div style="padding: 10px 1px;margin-left:5px">

			<table width="800">
				<tr style="height: 40px; text-align: center;">
					<td colspan="10"><h3>退/换货入库确认</h3></td>
				</tr>
				<tr style="height: 40px">
					<td colspan="3">服务单号：<label id="serverNo">${serverNo}</label> </td>
					<td colspan="3"></td>
					<td>订单号：<label id="orderNo">${orderNo}</label></td>
				</tr>
			</table>
			<table width="800" class="tableCon">
				<tr style="height: 40px">
					<th>商品编码</th>
					<th>条码</th>
					<th>商品名称</th>
					<th>规格型号</th>
					<th>包装数量</th>
					<th>单位</th>
					<th>订单数量</th>
					<th>残次品</th>
					<th colspan="2">货品状态</th>

				</tr>
				<c:forEach items="${purchaseInInfos}" var="pInfo">
					<tr style="height: 40px">
						<td class="gCode">${pInfo.goodsCode}</td>
						<td>${pInfo.goodsBarCode}</td>
						<td>${pInfo.goodsName}</td>
						<td>${pInfo.spec}</td>
						<td>${pInfo.packageNum}</td>
						<td>${pInfo.unit}</td>
						<td>${pInfo.purchaseNum}</td>
						<td class="goodscounts">
						<input id='submit${pInfo.goodsCode}'  name='count' onblur='checkNum('+${pInfo.goodsCode}+')' type='text' value='0' readonly='readonly' style="border:1px;width:30px;"/>
						<input id='oldsubmit${pInfo.goodsCode}' name='oldcount'  type='hidden' value='${pInfo.purchaseNum}' readonly='readonly' />
		                </td>
						<td colspan="2">
						<input type="radio"	name="staus_${pInfo.goodsCode}" value="1" checked onclick='incomplete("${pInfo.goodsCode}")'/>良品 
					    <input type="radio"	name="staus_${pInfo.goodsCode}" value="0" onclick='incomplete("${pInfo.goodsCode}")'/>残次品</td>
					</tr>
				</c:forEach>

			</table>
			<br />
			<hr />
			<table width="800">
				<tr style="height: 40px">
					<td colspan="2">意见：</td>
					<td colspan="3"><input type="radio" value="1" name="agree" checked />同意 &nbsp;
						&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; <input type="radio" value="0" name="agree" />拒绝</td>
				</tr>
				<tr style="height: 60px">
					<td colspan="3">退入仓库：</td>
					<td colspan="2"><select class="easyui-combobox"
						id="allrepository" name="allrepository" required="true"
						maxlength="200" missingMessage="收货仓库必须填写"
						style="width: 57%; height: 30px; line-height: 30px;">
							<!-- <option value="0">请选择供应商</option> -->
							<c:forEach items="${wList}" var="warehouse">
							<option value="${warehouse.id }">${warehouse.wareName}</option>
							</c:forEach>
					</select></td>
				</tr>
				<tr style="height: 40px">
					<td colspan="3">备注：</td>
					<td colspan="2"><textarea name="remark" id="remark" cols="100" rows="3"></textarea> </td>
				</tr>
				<tr style="height: 40px">
				<td colspan="3"></td>
					<td rowspan="2">
						<a href="javascript:serviceOfsingleFormSubmit();" class="btnG btnBl" id="commitedit">确定</a>
					</td>
				</tr>
					
			</table>
			
		</div>

	</div>
	</div>
<script type="text/javascript"
		src="<%=basePath %>/static/js/instock/serverInfo.js?v=0.03"></script>
</body>
</html>