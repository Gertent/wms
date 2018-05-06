<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>运费明细单</title>
    <link rel="stylesheet" href="<%=basePath%>/static/css/common/reset.css" type="text/css"/>
    <link rel="stylesheet" href="<%=basePath%>/static/css/goodsManage.css" type="text/css"/>
    <link rel="stylesheet" href="<%=basePath%>/static/css/changeNoInfo.css" type="text/css"/>
    <link rel="stylesheet" href="<%=basePath%>/static/css/addGoods.css" type="text/css"/>
    <link rel="stylesheet" href="<%=basePath%>/static/js/date-range-picker/dist/daterangepicker.min.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/static/js/easyui-1.4.5/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/static/js/easyui-1.4.5/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/static/js/easyui-1.4.5/demo.css">
    <script type="text/javascript" src="<%=basePath%>/static/js/ckeditor/ckeditor.js"></script>
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
        .showLogistics{width:80%;margin:30px auto 0 auto;}
        .showLogistics>li>label{
        	width:40%;
        	text-align:left;
        }
        .showLogistics>li>label:first-child{text-align:right;}
        
    </style>
</head>
<body class="easyui-layout">
<input type="hidden" id="basepath" value="<%=basePath%>"/>
<div class="goodsmBox smBox" data-options="region:'center'" style="border: 0">
    <div class="easyui-layout" data-options="fit:true">
        <%--头部搜索--%>
        <div class="header" id="logisticsListSearch" data-options="region:'north'" style="border: 0">
            <p class="navBar mgB16">
                <a class="btnG btnBl" onclick="logisticsListSearchBox()">搜索</a>
                <a class="btnG btnBl" onclick="formReset()">重置</a>
            </p>
            <form id="logisFreiBillSearchForm">
                <table class="gList">
                    <tr>
                        <td><label class="dib tal">运单号</label>
                            <input class="easyui-validatebox combo" name="logisticsNo" maxlength="20"/>
                        </td>
                        <td><label class="dib tar">订单号</label>
                            <input class="easyui-validatebox combo" name="orderNo" maxlength="20"/>
                        </td>
                        <td><label class="dib tar">承运商商名称</label>
                            <input class="easyui-validatebox combo" name="logisComName" maxlength="20"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label class="dib tar">运费修改状态</label>
                            <select id="doChange" name="doChange" class="easyui-combobox" style="width: 194px; height: 28px; line-height: 28px;">
                                <option value="">全部</option>
                                <option value="1">是</option>
                                <option value="0">否</option>
                            </select>
                        </td>
                        <td class="std"><label class="dib tar">时间</label>
                            <input type="hidden" id="starTime" name="starTime" />
                            <input type="hidden" id="endTime" name="endTime" />
                            <input id="datetimeRange" size="60" value="">
                        </td>
                        <td></td>
                    </tr>
                </table>
            </form>
        </div>
        <%--主数据列表--%>
        <div class="center" data-options="region:'center'" style="border: 0">
            <div class="easyui-layout" data-options="fit:true">
                <div data-options="region:'north'" style="padding: 20px 30px 12px; border: 0">
                    <p class="cenBtn">
                        <a class="btnG btnBl" id="alterPrice">修改运费</a>
                        <a class="btnG btnBl" id="lfBillExport">导出Excel</a>
                    </p>
                </div>
                <!-- datagrid列表  -->
                <div data-options="region:'center'" style="padding: 1px 30px 20px; border: 0">
                    <table id="logistics_bill_list" width="100%"></table>
                </div>
            </div>
        </div>
        <%--查看费用--%>
        <div class="tabContentBox" style="display: none;" id="showLogisticsBillInfo">
            <ul class="inBox showLogistics">
                <li><label>操作员：</label><label></label></li>
                <li><label>增加费用：</label><label></label></li>
                <li><label>修改时间：</label><label></label></li>
            </ul>
        </div>
        <%--添加费用--%>
        <div class="tabContentBox" style="display: none;" id="editLogisticsBill">
            <ul class="inBox" style="margin-top: 50px;margin-bottom: 35px;">
                <input id="alterId" type="hidden" />
                <li><label style="line-height:28px;">增加运费：</label><input name="extraCharges" id="extraCharges"/>&nbsp;元</li>
            </ul>
            <div class="edBox tac">
                <a href="javascript:;" class="btnG btnBl" id="commitedit">确定</a>
                <a href="javascript:;" class="btnG btnOg" id="cancel">取消</a>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="<%=basePath%>/static/js//jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/easyui-1.4.5/jquery.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/easyui-1.4.5/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/easyui-1.4.5/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/LocalResizeIMG.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/LocalResizeDOC.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/date-range-picker/dist/moment.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/date-range-picker/dist/jquery.daterangepicker.min.js"></script>
<%--<script type="text/javascript" src="moment.min.js"></script>--%>
<script type="text/javascript" src="<%=basePath%>/static/js/excelHead/logisticsFreightBillHead.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/logistics/logisticsFreightBillManage.js?v=0.03"></script>
<script type="text/javascript">
    //主页面表单
    $('.cenList .list li').click(function () {
        var index = $(this).index();
        $(this).addClass("active").siblings().removeClass("active");
    });

</script>
</body>
</html>