<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <title>采购收货</title>
    <link rel="stylesheet" href="<%=basePath%>/static/css/common/reset.css"
          type="text/css"/>
    <link rel="stylesheet" href="<%=basePath%>/static/css/goodsManage.css"
          type="text/css"/>
    <link rel="stylesheet" href="<%=basePath%>/static/css/changeNoInfo.css"
          type="text/css"/>
    <link rel="stylesheet" href="<%=basePath%>/static/css/addGoods.css"
          type="text/css"/>
    <link rel="stylesheet" href="<%=basePath%>/static/js/date-range-picker/dist/daterangepicker.min.css">
    <link rel="stylesheet" type="text/css"
          href="<%=basePath%>/static/js/easyui-1.4.5/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css"
          href="<%=basePath%>/static/js/easyui-1.4.5/themes/icon.css">
    <link rel="stylesheet" type="text/css"
          href="<%=basePath%>/static/js/easyui-1.4.5/demo.css">

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
    </style>
</head>
<body class="easyui-layout">
<input type="hidden" id="basepath" value="<%=basePath%>"/>
<input type="hidden" id="scanGoods" value="${scanGoods }"/>

<div class="goodsmBox smBox" data-options="region:'center'"
     style="border: 0">
    <div class="easyui-layout" data-options="fit:true">
        <div class="header" id="purchaseListSearch"
             data-options="region:'north'" style="border: 0">
            <p class="navBar mgB16">
                <a class="btnG btnBl" onclick="purchaseSearchBox()">搜索</a> <a
                    class="btnG btnBl" onclick="formReset()">重置</a>
            </p>
            <table class="gList">
                <tr>
                    <td><label class="dib w77">采购单号</label><input class="easyui-validatebox combo" id="purchaseNo" name="purchaseNo" maxlength="20"/></td>
                    <td class="std"><label class="dib tar">计划入库日期</label>
                        <input type="hidden" id="inDbData" name="inDbData"/>
                        <input type="hidden" id="inDbDataEnd" name="inDbDataEnd"/>
                        <input id="datetimeRange" size="60" value="">
                    </td>
                    <td><label class="dib tar">入库状态</label>
                        <select id="status" name="status" class="easyui-combobox" style="width: 194px; height: 26px; line-height: 26px;">
                            <option value="0">请选择</option>
                            <option value="1">等待</option>
                            <option value="2">部分</option>
                            <option value="3">完成</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td><label class="dib w77">供应商</label><input class="easyui-validatebox combo" id="supplierName" name="supplierName" maxlength="20"/></td>
                    <td></td>
                    <td></td>
                </tr>
            </table>
        </div>
        <div class="center" data-options="region:'center'" style="border: 0">
            <div class="easyui-layout" data-options="fit:true">
                <div data-options="region:'north'"
                     style="padding: 20px 30px 12px; border: 0">
                    <p class="cenBtn">
                        <!-- <a class="btnG btnBl" id="generateStaticPage">导入采购单</a> -->
<!--                         <a class="btnG btnBl" id="purchaseAdd">新增采购单</a> -->
                    </p>

                </div>
                <!-- datagrid列表  -->
                <div data-options="region:'center'"
                     style="padding: 1px 30px 20px; border: 0">
                    <table id="purchasebill_list" width="100%"></table>
                </div>
            </div>
            <div id="purchaseBillGoods"></div>
            <!--  新增商品弹窗页面 -->
            <div class="tabContentBox" style="display: none;" id="addPurchaseBill">
                <ul class="inBox">
                    <input type="hidden" id="Lid" name="id"/>
                    <li><label >供应商： </label> <select
                            class="easyui-combobox" id="asupplierName" name="asupplierName"
                            required="true" maxlength="200" missingMessage="供应商必须填写"
                            style="width: 57%; height: 30px; line-height: 30px;">
                        <option value="0">请选择供应商</option>
                        <option value="1">供应商1</option>
                        <option value="2">供应商2</option>
                    </select></li>

                    <li><label >计划入库时间：</label> <input id="ainDbData"
                                                             name="ainDbData" class="easyui-datebox"
                                                             style="width: 57%; height: 28px;"/></li>
                </ul>
                <div class="edBox tac">
                    <a href="javascript:;" class="btnG btnBl" id="commitedit">确定</a> <a
                        href="javascript:;" class="btnG btnOg" id="cancel">取消</a>
                </div>
            </div>
            <!--添加商品详情-->
            <div class="tabContentBox" style="display: none;"
                 id="addPurchaseBiLLInfo">
                <ul class="inBox">
                    <input type="hidden" id="cgNo"/>
                    <li><label >商品编码：</label><input type="text"
                                                          id="goodsCode" name="goodsCode"/></li>
                    <li><label >商品条形码：</label><input type="text"
                                                           id="goodsBarCode" name="goodsBarCode"/></li>
                    <li><label >商品名称：</label><input type="text"
                                                          id="goodsName" name="goodsName"/></li>
                    <li><label >商品规格：</label><input type="text" id="spec"
                                                          name="spec"/></li>
                    <li><label >包装数量：</label><input type="text"
                                                          id="packageNum" name="packageNum"/></li>
                    <li><label >单位：</label><input type="text" id="unit"
                                                        name="unit"/></li>
                    <li><label >采购数量：</label><input type="number"
                                                          id="purchaseNum" name="purchaseNum"/></li>
                    <li><label >采购单价：</label><input type="number"
                                                          id="purchasePrice" name="purchasePrice"/></li>
                    <li><label >有效期：</label> <input id="validityTime"
                                                          name="validityTime" class="easyui-datebox"
                                                          style="width: 57%; height: 28px;"/></li>
                </ul>
                <div class="edBox tac">
                    <a href="javascript:;" class="btnG btnBl" id="addPurchaseBtn">继续添加</a>
                    <a href="javascript:;" class="btnG btnOg" id="cancelPurchaseBtn">取消</a>
                </div>
            </div>
            <!--编辑商品弹窗页面 -->
            <div id="editGoods"></div>
            <!-- 转移供应商页面 -->
            <div id="changeSupplier"></div>
        </div>
    </div>
</div>
<script type="text/javascript"
        src="<%=basePath%>/static/js//jquery-1.8.3.min.js"></script>
<script type="text/javascript"
        src="<%=basePath%>/static/js/easyui-1.4.5/jquery.min.js"></script>
<script type="text/javascript"
        src="<%=basePath%>/static/js/easyui-1.4.5/jquery.easyui.min.js"></script>
<script type="text/javascript"
        src="<%=basePath%>/static/js/easyui-1.4.5/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"
        src="<%=basePath%>/static/js/LocalResizeIMG.js"></script>
<script type="text/javascript"
        src="<%=basePath%>/static/js/LocalResizeDOC.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/date-range-picker/dist/moment.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/date-range-picker/dist/jquery.daterangepicker.min.js"></script>
<script type="text/javascript"
        src="<%=basePath%>/static/js/purchase/purchaseManage.js?v=0.08"></script>

<script type="text/javascript">
    //主页面表单
    $('.cenList .list li').click(function () {
        var index = $(this).index();
        $(this).addClass("active").siblings().removeClass("active");
    });
</script>
</body>
</html>