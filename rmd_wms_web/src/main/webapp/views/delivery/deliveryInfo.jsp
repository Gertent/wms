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
<title>出库管理-发货交接-发货单打印</title>
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
#orderDetailsBox{width:802px;}
</style>
</head>

<body>
	<!-- 意向单查询弹框 订单详情 -->
	<div id="orderDetailsBox">

		<div style="padding: 10px 1px">

			<table width="800">
				<tr style="height: 40px">
					<td colspan="2">发货单号：${deliveryBill.deliveryNo}</td>
					<td colspan="2">承运商：${deliveryBill.logisComName}</td>
					<td colspan="2" style="width: 100px">合计金额：${deliveryGoodsSum}</td>
					<td colspan="2" style="width: 100px">运单件数：${deliveryBill.orderSum}</td>

				</tr>
			</table>
			<table width="800" class="tableCon">
				<tr style="height: 40px">
					<th style="width: 40px;">NO</th>
					<th>运单号</th>
					<th>订单号</th>
					<%--<th>订单件数</th>--%>
					<th>商品名称</th>
					<th>规格型号</th>
					<th>单品数量</th>
					<th>单价(元)</th>
					<th>订单总金额(元)</th>
					<th>订单总重量(kg)</th>
				</tr>
				
					<c:forEach items="${sOutList}" var="goodOut" varStatus="vstatus">
					
							<tr style="height: 40px">
								<td>${vstatus.index+1}</td>
								<td>${goodOut.logisticsNo}</td>
								<td>${goodOut.orderNo}</td>
								<%--<td>${goodOut.goodsAmount}</td>--%>
								<td colspan="4">
								     <table width="100%" border="0" cellspacing="0" cellpadding="0">
										<c:forEach items="${sInfoList}" var="sInfo">
											<c:if test="${sInfo.orderNo==goodOut.orderNo}">
												<tr style="height: 40px">
													<td width="25%">${sInfo.goodsName}</td>
													<td width="25.5%">${sInfo.spec}</td>
													<td width="25.5%">${sInfo.stockOutNum}</td>
													<td width="24%">${sInfo.salesPrice}</td>
												</tr>
											</c:if>
										</c:forEach>
									</table>
								</td>
								<td>${goodOut.goodsSum}</td>
								<td><fmt:formatNumber type="number" value="${goodOut.parcelWeight}" pattern="0.00" maxFractionDigits="2"/></td>
								<%--<td></td>--%>
							</tr>
					
					</c:forEach>
			

			</table>
			<table width="800">
				<tr style="height: 40px">
					<td style="width: 20px"></td>
					<td colspan='4'>承运商签字：</td>
					<td colspan='4'>发货员签字：</td>

				</tr>
			</table>

		</div>

	</div>
	</div>

</body>
</html>