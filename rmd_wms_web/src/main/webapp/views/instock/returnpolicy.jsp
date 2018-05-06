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
    <title>退换货说明</title>
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
		.satateHead {
		    margin-left: 30px;
		}
		.imgbox1 .title{
			font-size: 18px;
			text-align:center;
			line-height:60px;
		}
		.imgbox1 label,.imgbox1 ul{float:left;}
		.imgbox1 label{margin-right:10px;padding-left: 10px;}
		.imgbox1 ul>li{
			width:143px;
			height:143px;
			float:left;
			margin:0 30px 20px 0;
		}
		.imgbox1 ul{width:90%;}
		.imgbox1 ul li{
			width:143px;
			height:143px;
			overflow:hidden;
		}
		.imgbox1 ul li img{width:100%;height:100%}
		.explaintTxt{
			font-size: 12px;
			margin-right:10px;
			padding-left: 10px;
			padding-bottom:50px;
			border-bottom:1px #dcdcdc dashed;
		}
		.explaintTxt span{font-size:12px;}
		#imgOriginal img{
			margin:20px;
		
		}
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
<div id="returnpolicy">
	<div class="orderDetailsNav">
		<div class="satateHead clearfix">
			<p class="fl">服务单号：${serverNo}</p>
	        <p class="fl">订单号：${orderNo}</p>
	        <p class="fl">承运商名称：</p>
	        <p class="fl">运单号：</p>
	        <p class="fl">合计金额：${goodsSum}</p>
	        <p class="fl">总件数：${goodsNum}</p>
	    </div>
	</div>
    <div class="easyui-tabs" style="width:100%;height:auto;margin:0 auto" data-options="border:false">
        <div title="商品清单" style="padding:10px 1px">
			<table width="800"  class="tableCon" style="margin-left:20px;">
				<tr style="height:40px">

					<th style="width:40px;"></th>
					<th>商品编码</th>
					<th>条码</th>
					<th>商品名称</th>
					<th>规格型号</th>
					<%--<th>有效期</th>--%>
					<th>包装数量</th>
					<th>单位</th>
					<th>单价</th>
					<th>商品数量</th>
					<th>合计(元)</th>

				</tr>
				<c:forEach items="${goodsLists}" var="goodItem" varStatus="vstatus" >
					<tr style="height:40px">
						<td>${vstatus.index+1}</td>
						<td>${goodItem.goodsCode}</td>
						<td>${goodItem.barcode}</td>
						<td>${goodItem.goodsname}</td>
						<td>${goodItem.spec}</td>
							<%--<td></td><fmt:formatDate value="${pInfo.validityTime}" />--%>
						<td>${goodItem.packnum}</td>
						<td>${goodItem.saleunit}</td>
						<td>${goodItem.unitprice}</td>
						<td>${goodItem.goodsamount}</td>
						<td>${goodItem.unitprice*goodItem.goodsamount}</td>

					</tr>
				</c:forEach>
				<tr style="height:40px">
					<td>总计</td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td>${goodsNum}</td>
					<td>${goodsSum}</td>

				</tr>
			</table>
        </div>
        <!--退换货说明  -->
        <div title="退换货说明" style="padding:10px 1px">
            <div class="imgbox1">
            	<p class="title">客户退换货说明</p>
            	<div class="clearfix">
            		<label for="">图片：</label>
            		<ul class="clearfix">
					<c:forEach items="${customerImageUrls}" var="item" >
						<c:if test="${not empty item and item!=''}">
							<li><img src="${item}" alt=""/></li>
						</c:if>
					</c:forEach>
            		</ul>
            	</div>
            	<p class="explaintTxt"><span>说明：</span>${customerExplain}</p>
            	<p class="title">客服审核说明</p>
            	<div class="clearfix">
            		<label for="">图片：</label>
            		<ul class="clearfix">
						<c:forEach items="${serviceImageUrls}" var="item" >
							<c:if test="${not empty item and item!=''}">
								<li><img src="${item}" alt=""/></li>
							</c:if>
						</c:forEach>
            			<%--<li><img src="<%=basePath%>/static/images/smimg1.png" alt=""/></li>--%>
            		</ul>
            	</div>
            	<p class="explaintTxt"><span>说明：</span>${serviceExplain}</p>
            </div>
        </div>
        
    </div>
</div>
<div id="imgOriginal" style="text-align:center;display:none" ></div>
<script>
    $(function(){
    	$(".imgbox1 ul li img").click(function(){
    		var imgsrc = $(this).attr("src");
    		var imgstr = '<img src="'+imgsrc+'"/>';
    		$("#imgOriginal").html(imgstr);
    		
    		$('#imgOriginal').dialog({
    		    title: 'My img',
    		    width: '90%',
    		    height:'90%',
    		    closed: false,
    		    cache: false,
    		    modal: true
    		});
    	})
    })
</script>
</body>
</html>