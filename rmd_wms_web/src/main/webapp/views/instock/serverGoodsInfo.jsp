<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>入库管理-售后收货-商品信息</title>
    <link rel="stylesheet" href="<%=basePath%>/static/css/common/reset.css" type="text/css"/>
    <link rel="stylesheet" href="<%=basePath%>/static/css/wishlistManage.css" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/static/js/easyui-1.4.5/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/static/js/easyui-1.4.5/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/static/js/easyui-1.4.5/demo.css">
    <script type="text/javascript" src="<%=basePath%>/static/js/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/static/js/easyui-1.4.5/jquery.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/static/js/easyui-1.4.5/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/static/js/easyui-1.4.5/easyui-lang-zh_CN.js"></script>
    <style>
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
    </style>
</head>

<body>
<!-- 意向单查询弹框 订单详情 -->
<div id="orderDetailsBox">
    <%--<input type="text" id="purchaseNo" hidden="true" value="${instockBill.purchaseNo}"/>--%>
    <div  style="padding:10px 1px">

        <table width="800"  >
            <tr class="purchaseTr" style="height:40px;text-align:center;"><td colspan="10"><h3>售后收货商品信息</h3></td></tr>
            <%--<tr class="purchaseTr" style="height:40px">--%>
                <%--<td colspan="3">供应商名称：${instockBill.supplierName}</td>--%>
                <%--<td colspan="3">入库单编号：${instockBill.inStockNo}</td>--%>
                <%--<td>采购单编号：${purchaseBill.purchaseNo}</td>--%>
            <%--</tr>--%>

            <%--<tr class="goodTr" style="height:40px;text-align:center;"><td colspan="10"><h3>售后(良品)入库单</h3></td></tr>--%>
            <%--<tr class="goodTr" style="height:40px">--%>
                <%--<td colspan="3">入库单编号：${instockBill.inStockNo}</td>--%>
                <%--<td colspan="3">服务单号：${instockBill.purchaseNo}</td>--%>
                <%--<td>订单号：${instockBill.orderNo}</td>--%>
            <%--</tr>--%>

            <%--<tr style="height:40px">--%>
                <%--<td colspan="3">收货单位名称：${instockBill.wareName}</td>--%>
                <%--<td colspan="3">入库时间：<fmt:formatDate type="both" value="${instockBill.inStockTime}" /></p></td>--%>
                <%--<td>--%>
                    <%--&lt;%&ndash;${purchaseBill.status}&ndash;%&gt;--%>
                <%--</td>--%>
            <%--</tr>--%>
        </table>
        <table width="800"  class="tableCon">
            <tr style="height:40px">

                <th style="width:40px;"></th>
                <th>商品编码</th>
                <th>条码</th>
                <th>商品名称</th>
                <th>规格型号</th>
                <th>包装数量</th>
                <th>单位</th>
                <%--<th>采购数量</th>--%>
                <%--<th>入库数量</th>--%>
                <th>单价</th>
                <%--<th>有效期</th>--%>
            </tr>
            <c:forEach items="${goodsLists}" var="goodItem" varStatus="vstatus" >
                <tr style="height:40px">
                    <td>${vstatus.index+1}</td>
                    <td>${goodItem.goodsCode}</td>
                    <td>${goodItem.goodsBarCode}</td>
                    <td>${goodItem.goodsName}</td>
                    <td>${goodItem.spec}</td>
                    <td>${goodItem.packageNum}</td>
                    <td>${goodItem.unit}</td>
                    <%--<td>${goodItem.purchaseNum}</td>--%>
                    <%--<td>${goodItem.inStockNum}</td>--%>
                    <td>${goodItem.purchasePrice}</td>
                    <%--<td><fmt:formatDate value="${pInfo.validityTime}" /></td>--%>
                </tr>
            </c:forEach>
        </table>

    </div>

</div>
</div>
<script type="text/javascript">
    $(function(){
        var purchaseNo=$('#purchaseNo').val();
        if(purchaseNo.indexOf('CG')==0){
            $('.purchaseTr').show();
            $('.goodTr').hide();
        }else{
            $('.purchaseTr').hide();
            $('.goodTr').show();
        }
    });
</script>
</body>
</html>