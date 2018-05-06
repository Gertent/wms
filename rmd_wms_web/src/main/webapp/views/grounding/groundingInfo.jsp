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
    <title>入库管理-收货计划-上架详情</title>
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
 
        <div  style="padding:10px 1px">
      
            <table width="800"  >
            <tr style="height:40px">
            <td colspan="3" >上架单号：${groundingbill.inStockNo}</td>
            <td colspan="3" style="width:100px">品种：${goodcount}</td>
            <td colspan="3" style="width:100px">件数：${groundingbill.groundingAmount}</td>
            </tr>
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
                    <th>货位</th>
                    <th>上架数量</th>
             </tr>
             	<c:forEach items="${pInInfos}" var="pInfo" varStatus="vstatus" >
             	<tr style="height:40px">
             	    <td>${vstatus.index+1}</td>
             	    <td>${pInfo.goodsCode}</td>
             	    <td>${pInfo.goodsBarCode}</td>
             	    <td>${pInfo.goodsName}</td>
             	    <td>${pInfo.spec}</td>
             	    <td>${pInfo.packageNum}</td>
             	    <td>${pInfo.unit}</td>
             	    <td>${pInfo.locationNo}</td>
             	    <td>${pInfo.inStockNum}</td>
             	    </tr>
             	</c:forEach>
               
            </table>
            
            
        </div>

    </div>
</div>

</body>
</html>