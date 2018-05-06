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
    <title>订单详情</title>
    <link rel="stylesheet" href="<%=basePath%>/static/css/common/reset.css" type="text/css"/>
    <link rel="stylesheet" href="<%=basePath%>/static/css/wishlistManage.css" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/static/js/easyui-1.4.5/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/static/js/easyui-1.4.5/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/static/js/easyui-1.4.5/demo.css">
    <script type="text/javascript" src="<%=basePath%>/static/js/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>/static/js/easyui-1.4.5/jquery.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>/static/js/easyui-1.4.5/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>/static/js/easyui-1.4.5/easyui-lang-zh_CN.js"></script>
</head>

<body>
<!-- 意向单查询弹框 订单详情 -->
<div id="orderDetailsBox">
	<div class="orderDetailsNav">
		<div class="satateHead clearfix">
	        <%-- <p class="fl">订单号：${wishList.orderCode}</p>
	        <p class="fl">当前状态：
	        <c:if test="${wishList.ordStatus == 100}">
	       		 待处理
	        </c:if>
	        <c:if test="${wishList.ordStatus == 101}">
	       		 待发货
	        </c:if>
	        <c:if test="${wishList.ordStatus == 102}">
	       		 已发货
	        </c:if>
	        <c:if test="${wishList.ordStatus == 103}">
	       		 已取消
	        </c:if>
	        <c:if test="${wishList.ordStatus == 104}">
	       		 交易完成
	        </c:if>
	        <i class="pendingPayment"></i></p> --%>
	    </div>
    	<div class="downBox">
    	<c:forEach items="${logList}" var="log">
    		<c:if test="${log.status == wishList.ordStatus}">
    		 	<em></em>
	            <span></span>
	            <p>处理时间：<fmt:formatDate value="${log.operasTime}" pattern="yyyy-MM-dd:HH:mm:ss" /></p>
	            <p>操作：${ugList.modLoginname}（${ugList.modRealname}）</p>
	            <p>备注：${wishList.remark}</p>
    		</c:if>
    	</c:forEach>
           
        </div>
	</div>
    <div class="easyui-tabs" style="width:754px;height:auto;margin:0 auto" data-options="border:false">
        <div title="订单明细" style="padding:10px 1px">
            <table class="tabsTab">
                <tr><td colspan="7">收货人信息</td></tr>
                <tr>
                    <td class="th">收货人</td>
                    <td>${orderLogisticsInfo.receivername}</td>
                    <td class="th">收货地址</td>
                    <td colspan="3">${orderLogisticsInfo.provName}${orderLogisticsInfo.cityName}${orderLogisticsInfo.detailedAddress}</td>
                </tr>
              <tr>
                    <td class="th">固定电话</td>
                    <td>${orderLogisticsInfo.receiveTel }</td>
                    <td class="th">移动电话</td>
                    <td colspan="3">${orderLogisticsInfo.receiveMobile}</td>
                </tr>
                <tr>
                    <td colspan="7">物流信息</td>
                </tr>
                <tr>
                    <td class="th">物流状态</td>
                    <td><a href="http://wap.guoguo-app.com/wuliuDetail.htm?spm=a312p.7915420.0.0.QXtlB7&mailNo=${stockOutBill.logisticsNo}" target="_blank">查看状态</a></td>
                    <td class="th">标准重量(㎏)</td>
                    <td>${stockOutBill.weight}</td>
                    <td class="th">发货仓库</td>
                    <td>${stockOutBill.wareName }</td>
                </tr>
                <tr>
                    <td class="th">物流公司</td>
                    <td>${stockOutBill.logisComName }</td>
                    <td class="th">官方电话</td>
                    <td>${lCompany.phone }</td>
                    <td class="th">运单编号</td>
                    <td>${stockOutBill.logisticsNo}</td>
                </tr>
              
            </table>
        </div>
        <div title="商品清单" style="padding:10px 1px">
            <table class="tabs2Tab">
            <tr>
                    <th>商品编码</th>
                    <th>条码</th>
                    <th>商品名称</th>
                    <th>订单数量</th>
                    <th>有效期</th>
                    <th>规格型号</th>
                    <th>包装数量</th>
                    <th>单位</th>
                    <th>货位</th>
                    <th>单价</th>
                    
                </tr>
            <c:forEach items = "${StockOutGoodsList}" var="stockOutInfo">
            	<tr style="height:30px">
                    <td>${stockOutInfo.goodsCode}</td>
                    <td>${stockOutInfo.goodsBarCode}</td>
                    <td>${stockOutInfo.goodsName}</td>
                    <td>${stockOutInfo.stockOutNum}</td>
                    <td><fmt:formatDate type="both" value="${stockOutInfo.validityTime}" /></td>
                    <td>${stockOutInfo.spec}</td>
                    <td>${stockOutInfo.packageNum}</td>
                    <td>${stockOutInfo.unit}</td>
                    <td>
                    <c:forEach items = "${stockOutInfo.lGoodsBindOuts}" var="BindOutGood">
                    <c:if test="${BindOutGood.locationNo=='-1'}">
                    "缺货" <br/>
                    </c:if>

                      <c:if test="${BindOutGood.locationNo!='-1'}">
                   ${BindOutGood.locationNo} <br/>
                    </c:if>
                   
                    </c:forEach>
                    </td>
                    <td>${stockOutInfo.salesPrice}</td>
                </tr>
            </c:forEach>
            </table>
        </div>
       <%--   <div title="处理状态" style="padding:10px 1px" class="tabs3Tab">
           <ul>
            <c:forEach items="${logList}" var="log">
            	<c:if test="${log.status == 104}">
	            	<li>
	                    <i <c:choose>
				            <c:when test="${log.ident == 'new'}">
				            	class="progressIcon"
				            </c:when>
				            <c:otherwise>
				            	class="progressIcon progressIcon2"
				            </c:otherwise>
				            </c:choose>></i>
	                    <p class="dataDetail"><fmt:formatDate value="${log.operasTime}" pattern="yyyy-MM-dd:HH:mm:ss" /></p>
	                    <div class="stateCon">
	                        <p>订单交易完成  操作人：${ugList.modLoginname}（${ugList.modRealname} | ${ugList.modGroupname}）</p>
	                    </div>
	                </li>
            	</c:if>
            	<c:if test="${log.status == 103}">
            		<li>
                    <i <c:choose>
			            <c:when test="${log.ident == 'new'}">
			            	class="progressIcon"
			            </c:when>
			            <c:otherwise>
			            	class="progressIcon progressIcon2"
			            </c:otherwise>
			            </c:choose>></i>
                    <p class="dataDetail"><fmt:formatDate value="${log.operasTime}" pattern="yyyy-MM-dd:HH:mm:ss" /></p>
                    <div class="stateCon">
                        <p>订单已发货  操作人：${ugList.modLoginname}（${ugList.modRealname} | ${ugList.modGroupname}）</p>
                    </div>
                </li>
            	</c:if>
            	<c:if test="${log.status == 102}">
	            	<li>
	                    <i <c:choose>
				            <c:when test="${log.ident == 'new'}">
				            	class="progressIcon"
				            </c:when>
				            <c:otherwise>
				            	class="progressIcon progressIcon2"
				            </c:otherwise>
				            </c:choose>></i>
	                    <p class="dataDetail"><fmt:formatDate value="${log.operasTime}" pattern="yyyy-MM-dd:HH:mm:ss" /></p>
	                    <div class="stateCon">
	                        <p>操作人：${ugList.modLoginname}（${ugList.modRealname} | ${ugList.modGroupname}）</p>
	                        <p>订单指派给 ：${ugList.staffLoginname}（${ugList.staffRealname} | ${ugList.staffGroupname}）</p>
	                    </div>
	                </li>
            	</c:if>
            	<c:if test="${log.status == 101}">
            		<li>
                    <i <c:choose>
			            <c:when test="${log.ident == 'new'}">
			            	class="progressIcon"
			            </c:when>
			            <c:otherwise>
			            	class="progressIcon progressIcon2"
			            </c:otherwise>
			            </c:choose>></i>
                    <p class="dataDetail"><fmt:formatDate value="${log.operasTime}" pattern="yyyy-MM-dd:HH:mm:ss" /></p>
                    <div class="stateCon">
                        <p>订单待发货  操作人：${ugList.modLoginname}（${ugList.modRealname} | ${ugList.modGroupname}）</p>
                    </div>
                </li>
            	</c:if>
            	<c:if test="${log.status == 100}">
            	<li>
                    <i <c:choose>
			            <c:when test="${log.ident == 'new'}">
			            	class="progressIcon"
			            </c:when>
			            <c:otherwise>
			            	class="progressIcon progressIcon2"
			            </c:otherwise>
			            </c:choose>></i>
                    <p class="dataDetail"><fmt:formatDate value="${log.operasTime}" pattern="yyyy-MM-dd:HH:mm:ss" /></p>
                    <div class="stateCon">
                        <p>提交意向订单，订单号：${ugList.orderCode}</p>
                    </div>
                    <div class="buyerMessage">
                            <span>买家留言：</span>
                            <p>${wishList.remark}</p>
                        </div>
                </li>
            	</c:if>
            </c:forEach>
            </ul> 
        </div>--%>
    </div>
</div>
<script>
    //主页面表单
    $('.cenList .list li').click(function () {
        var index = $(this).index();
        $(this).addClass("active").siblings().removeClass("active");
//        $('.tabContentBox').eq(index).show(200).siblings('.tabContentBox').hide();
//        $('#ddaa').datagrid('reload');
    });
    $('#select,#select2,#select3,#select4,#select5').combobox({
        formatter:function(row){
            var imageFile = 'images/' + row.icon;
            return '<img class="item-img" src="'+imageFile+'"/><span class="item-text">'+row.text+'</span>';
        }
    });
    //alert( $("#tt tr td:eq(4) a").text())
    
    function billList(){
        $("#orderDetailsBox").window({
                width:800,
                height:487,
                modal:true,
                shadow:false,
                collapsible:false,
                minimizable:false,
                maximizable:false,
                title:"添加品牌"
            })
        event.stopPropagation();
    }
    $('#designate').menubutton({
        menu: '#mm'
    });
    $('#reviseState').menubutton({
        menu: '#mm2'
    });
  //点击图片出现具体信息
    $(".satateHead i").click(function(){
        if($(this).attr("class")=="pendingPayment"){
            $(this).addClass("on");
            $(".orderDetailsNav .downBox").show();
        }else{
            $(this).removeClass("on");
            $(".orderDetailsNav .downBox").hide();
        }
    });
</script>
</body>
</html>