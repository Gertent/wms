<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>盘点计划列表</title>
    <link rel="stylesheet" href="<%=basePath %>/static/css/common/reset.css" type="text/css"/>
    <link rel="stylesheet" href="<%=basePath %>/static/css/goodsManage.css" type="text/css"/>
    <link rel="stylesheet" href="<%=basePath %>/static/css/changeNoInfo.css" type="text/css"/>
    <link rel="stylesheet" href="<%=basePath %>/static/css/addGoods.css" type="text/css"/>
    <link rel="stylesheet" href="<%=basePath%>/static/js/date-range-picker/dist/daterangepicker.min.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath %>/static/js/easyui-1.4.5/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath %>/static/js/easyui-1.4.5/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath %>/static/js/easyui-1.4.5/demo.css">
    <script type="text/javascript" src="<%=basePath %>/static/js/ckeditor/ckeditor.js"></script>
    <style>
        .datagrid-btable .datagrid-cell {
            padding: 6px 4px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }

        .tableCon {
            border: 1px #dcdcdc solid;
        }

        .tableCon tr {
            border-bottom: 1px #dcdcdc solid;
        }

        .tableCon td, .tableCon th {
            text-align: center;
            border-right: 1px #dcdcdc solid;
        }
        input[name="locationNo"],
        input[name="goodsName"],
        input[name="spec"]{
        	border: 1px #c9c9c9 solid;
		    height: 28px;
		    border-radius: 4px;
		    padding-left: 10px;
	        margin-right: 20px;
        }
        .tabContentBox .header>label{
			margin-right:10px;
			margin-left: 10px;        
        }
        #createCheckByLocationPop .textbox {height:28px!important;}
    </style>
</head>
<body class="easyui-layout">
<input type="hidden" id="basepath" value="<%=basePath %>"/>
<input type="text" id="wareStatus" value="${wareStatus }"/>
<div class="goodsmBox smBox" data-options="region:'center'" style="border:0">
    <div class="easyui-layout" data-options="fit:true">
        <!--搜索头部信息-->
        <div class="header" id="goodsListSearch" data-options="region:'north'" style="border:0">
            <p class="navBar mgB16">
                <a class="btnG btnBl" onclick="checkBillsSearch()">搜索</a>
                <a class="btnG btnBl" onclick="formReset()">重置</a>
            </p>
            <form id="checkBillSearchForm">
                <table class="gList">
                    <tr>
                        <td><label for="" class="dib w77">盘点单号</label>
                            <input class="easyui-validatebox combo" name="checkNo" maxlength="20"/>
                        </td>
                        <td class="std"><label class="dib tar">创建时间</label>
                            <input type="hidden" id="starTime" name="starTime" />
                            <input type="hidden" id="endTime" name="endTime" />
                            <input id="datetimeRange" size="60" value="">
                        </td>
                        <td><label for="" class="dib tar">盘点状态</label>
                            <select id="status" name="status" class="easyui-combobox"
                                    style="width: 194px; height: 28px; line-height: 28px;">
                                <option value="-1">请选择</option>
                                <option value="1">等待</option>
                                <option value="2">盘点中</option>
                                <option value="3">中断</option>
                                <option value="4">完成</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td><label class="dib w77">类型</label>
                            <select id="type" name="type" class="easyui-combobox"
                                    style="width: 194px; height: 28px; line-height: 28px;">
                                <option value="-1">请选择</option>
                                <option value="1">日盘</option>
                                <option value="2">大盘</option>
                            </select>
                        </td>
                        <td>
                            <%--<label for="" class="dib tar">仓库</label>--%>
                            <%--<input class="easyui-validatebox combo" name=wareName maxlength="20"/>--%>
                        </td>
                        <td></td>
                    </tr>
                </table>
            </form>
        </div>
        <!-- 主数据区  -->
        <div class="center" data-options="region:'center'" style="border:0">
            <div class="easyui-layout" data-options="fit:true">
                <div data-options="region:'north'" style="margin:20px 0 12px;height:100px;border:0">
                    <div class="cenBtn clearfix">
                    	<shiro:hasPermission name="WMS_CHECK_PRINT">
                        	<a class="btnG btnBl fl" id="checkPrint" style="margin-right:20px;margin-left: 30px;">打印</a>
                        </shiro:hasPermission>
                        <div class="menuBtn fl">
                        	<shiro:hasPermission name="WMS_CHECK_ADD">
	                        <a class="btnG btnBl"  class="createPlanBtn" id="createCheckBillButton">创建盘点计划</a>
	                        <ul class="createPlanOption">
	                        	<li class="option1" id="createCheckByLocation">按库位创建</li>
	                        	<li class="option1" id="createCheckByGoods">按商品创建</li>
	                        </ul>
	                        </shiro:hasPermission>
                        </div>
                        <shiro:hasPermission name="WMS_CHECK_ORDEROPERATE">
                       	 <a class="btnG btnBl fr" id="orderOperate" style="margin-right:30px">开放/关闭订单</a>
                        </shiro:hasPermission>
                        <%-- <a class="btnG btnBl"  class="createPlanBtn" id="createCheckBill">创建盘点计划</a>--%>
                    </div>
                </div>
                <!-- datagrid列表  -->
                <div data-options="region:'center'" style="padding:1px 30px 20px;border:0">
                    <table id="checkBillList" width="100%"></table>
                </div>
            </div>
        </div>
        <!--新建盘点单通过货位弹框-->
        <div class="tabContentBox" style="display: none;" id="createCheckByLocationPop">
            <!--pop 搜索头部信息-->
            <div class="header" data-options="region:'north'" style="border:0;margin:10px 0">
                <label class="dib tar">库区</label>
                <input class="easyui-combobox" id="warehouseAreaSelect" name="areaIds" value=""
                       data-options="multiple:true, editable:false, panelHeight:'auto'" style="margin-right:10px;">
                <label class="dib tar">库位</label>
                <input class="easyui-validatebox combo" id="locationNo" name="locationNo" maxlength="20"/>
                <a class="btnG btnBl" onclick="searchLocationGoodsBindByLocation()">搜索</a>
            </div>
            <!-- pop主数据区  -->
            <table id="locationGoodsBindListByLoca" class="easyui-datagrid" style="width:80%;margin:0 auto"></table>
            <li><label>盘点类型：</label>
                <div class="radioBox dib">
                    <form>
                        <input type="radio" name="type" value="1" checked/>日盘
                        <input type="radio" name="type" value="2" />大盘
                    </form>
                </div>
            </li>
            <!-- pop按钮区  -->
            <div class="edBox tac " style="clear:both">
                <a href="javascript:;" class="btnG btnOg" id="cancelCheckBtn">取消</a>
                <a href="javascript:;" class="btnG btnBl" id="doCreateCheckBtn">创建计划</a>
            </div>
        </div>
        <!-- 新加盘点单通过商品弹框 -->
        <div class="tabContentBox" style="display: none;" id="createCheckByGoodsPop">
            <!--pop 搜索头部信息-->
            <div class="header" data-options="region:'north'" style="border:0;margin:10px 0">
                <label class="dib tar">商品名称</label>
                <input class="easyui-validatebox combo" id="goodsName" name="goodsName" maxlength="20"/>
                <label class="dib tar">规格型号</label>
                <input class="easyui-validatebox combo" id="spec" name="spec" maxlength="20"/>
                <a class="btnG btnBl" onclick="searchLocationGoodsBindByGoods()">搜索</a>
            </div>
            <!-- pop主数据区  -->
            <table id="locationGoodsBindListByGoods" class="easyui-datagrid" style="width:80%;margin:0 auto"></table>
            <div><label>盘点类型：</label>
                <div class="radioBox dib">
                    <form>
                        <input type="radio" name="type" value="1" checked/>日盘
                        <input type="radio" name="type" value="2" />大盘
                    </form>
                </div>
            </div>
            <!-- pop按钮区  -->
            <div class="edBox tac " style="clear:both">
                <a href="javascript:;" class="btnG btnOg" id="cancelCheckByGoodsBtn">取消</a>
                <a href="javascript:;" class="btnG btnBl" id="doCreateCheckByGoodsBtn">创建计划</a>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="<%=basePath %>/static/js//jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/static/js/easyui-1.4.5/jquery.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/static/js/easyui-1.4.5/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/static/js/easyui-1.4.5/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/LocalResizeIMG.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/LocalResizeDOC.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/date-range-picker/dist/moment.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/date-range-picker/dist/jquery.daterangepicker.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/static/js/check/checkManage.js?v=0.14"></script>

<script type="text/javascript">
    //主页面表单
    $('.cenList .list li').click(function () {
        var index = $(this).index();
        $(this).addClass("active").siblings().removeClass("active");
    });
    $("#createCheckBillButton").click(function(){
    	var state=$(".createPlanOption").css("display");
    	if(state=="none"){
    		$(".createPlanOption").fadeIn();
    	}else{
    		$(".createPlanOption").fadeOut();
    	}
    })
</script>
</body>
</html>