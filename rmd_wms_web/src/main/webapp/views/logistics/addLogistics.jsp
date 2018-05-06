<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>添加承运商</title>
    <link rel="stylesheet" href="<%=basePath %>/static/css/common/reset.css" type="text/css"/>
    <link rel="stylesheet" href="<%=basePath %>/static/css/goodsManage.css" type="text/css"/>
    <link rel="stylesheet" href="<%=basePath %>/static/css/memberManage.css" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>/static/js/easyui-1.4.5/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath %>/static/js/easyui-1.4.5/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath %>/static/js/easyui-1.4.5/demo.css">
<%--     <script type="text/javascript" src="<%=basePath%>/static/js/ProJson.js"></script> --%>
    <script type="text/javascript" src="<%=basePath%>/static/js/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>/static/js/easyui-1.4.5/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>/static/js/easyui-1.4.5/easyui-lang-zh_CN.js"></script>
	
</head>
<body>
<div id="addMemBox" >
<form id="myFormPost" method="post">
    <table>
        <tr>
            <td  class="th tar"><label>承运商名称：<span style="color: red;">*</span></label></td>
            <td><input class="easyui-validatebox" onblur="checkCompany()"; required="true" type="text" id="Lname" name="name"  value="${Company.name}" invalidMessage="请输入承运商名称！"/></td>
        </tr>
        <tr>
            <td class="th tar"><label>联系人：</label></td>
            <td><input class="easyui-validatebox"  id="contactName" type="text" name="contactName" value="${Company.contactName}"/></td>
        </tr>
        <tr>
            <td class="th tar"><label>编码：<span style="color: red;">*</span></label></td>
            <td><input type="text" id="code" name="code" value="${Company.code}" required="true" invalidMessage="请输入承运商编码！"/></td>
        </tr>
       <tr>
            <td class="th tar"><label>联系电话：</label></td>
            <td><input type="text" id="phone" value="${Company.phone}" name="phone"/></td>
        </tr>
        <tr>
            <td class="th tar"><label>配送范围：<span style="color: red;">*</span></label></td>
            <td><div id="PROVINCE"></div></td>
        </tr>
        <tr>
            <td class="th tar"><label>承运重量：</label></td>
            <td>
            <input type="text" value="${Company.minWeight}" style="width: 20%;height: 28px;line-height: 28px;" placeholder="最小重量" id="minWeight" name="minWeight"/>-
            <input type="text" value="${Company.maxWeight}" style="width: 20%;height: 28px;line-height: 28px;" placeholder="最大重量" id="maxWeight" name="maxWeight"/>
            </td>
        </tr>
        <tr>
            <td class="th tar"><label>状态：<span style="color: red;">*</span></label></td>
            <td>
                <a href="javascript:void(0)" style="margin-right: 5%;"><i class="on"></i>
                <input  name="status" type="radio" value="1" checked class="hide"/>启用</a>
                <a href="javascript:void(0)"><i></i>
                <input  name="status" type="radio" value="0" class="hide"/>禁用</a>
                <input type="hidden" value="${rangeList}" id="rangeList" />
                <input type="hidden" value="${Company.id}" id="id" />
                <input type="hidden" value="${rangeId}" id="rangeId" />
            </td>
        </tr>
    </table>
    <div class="edBox tac pdT20"><a class="btnG btnBl" onclick="savereg()">确定</a><a onclick="cancle()" class="btnG btnOg">取消</a></div>
</form>
</div>
<script type="text/javascript" src="<%=basePath%>/static/js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/validator.js"></script>
<script type="text/javascript">
$(function(){
	
	$.ajax({
		type : 'get',
		async: false,
		url : "<%=basePath%>/logistics/getAreaInfo?parentCode=",
		success : function(result) {			
			//省级赋值name='province'点击取值所有checkbox选中的值
		    var count=0;
		    $.each(result, function (k, p) {
		    	var checkbox="";
		        checkbox="<input  type='checkbox' name='province' id='p_"+p.areaCode+"' style='height: 22px;line-height: 22px;'   value='"+p.areaCode+"'/><span id='prv_"+p.areaCode+"'>"+p.areaName+"</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
		    	    $("#PROVINCE").append(checkbox);
		    	count++;
		    	if(count%3==0){
		    		$("#PROVINCE").append("</br>");
		    	}
		    });
		}
	});
	
    
    
    //修改配送范围修改信息显示 
    var rangeList =$("#rangeList").val();
    var checkProvince= new Array(); //定义一数组 
    checkProvince=rangeList.split(","); //字符分割 
    for(var i=0;i<checkProvince.length;i++){
    	$("#p_"+checkProvince[i]).attr('checked','checked');
    }

 });
 
 //检查承运商名称是否重复
 function checkCompany(){
     var flag=true;
	 var name=$("#Lname").val();
     var id=$('#id').val();
		$.ajax({
			type : 'post',
            async:false,
			url : "./checkCompanyName",
            data : {
                'id':id,
                'name' : name
            },
			success : function(result) {
			 if(result=="true"){
					$.messager.alert('提醒', '承运商名称重复!');
                 flag=false;
			 }else{
				 
			 }
				
			}
		});
	 return flag;
 }
 
//单选按钮
$(".memEdit table td a").click(function(){
    $(this).find("i").addClass("on");
    $(this).siblings().find("i").removeClass("on");
    $(this).siblings().find("input").attr('checked','false');
    $(this).find("input").attr('checked','checked');

})

function cancle(){
	$("#addMemBox").window("close");
}
</script>
</body>
</html>