<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
<title>编辑仓库</title>
</head>
<body class="easyui-layout">
<!-- 	<div class="tabContentBox" style="display: none;" > -->
		<input type="hidden" id="id" name="id" value="${warehouse.id}">
		<input type="hidden" id="areaCode" name="areaCode" value="${areaCode}">
		<ul class="inBox">
			<li><label for="">仓库编码：</label><input type="text" id="code"
				name="code" value="${warehouse.code}"/></li>
			<li><label for="">仓库名称：</label><input type="text"
				id="wareName" name="wareName" value="${warehouse.wareName}"/></li>
			<li><label for="">地址：</label><input type="text" id="address"
				name="address" value="${warehouse.address}"/></li>
			<li><label for="">状态：</label>
				<div class="radioBox dib">
					<input type="radio" id="status" name="status" value="1" <c:if test="${warehouse.status == 1}">checked</c:if>/>启用
					<input type="radio" id="status" name="status" value="0" <c:if test="${warehouse.status == 0}">checked</c:if>/>禁用
				</div>
			</li>
			<li><label for="">联系人：</label><input type="text"
				id="contactName" name="contactName" value="${warehouse.contactName}"/></li>
			<li><label for="">联系电话：</label><input type="text" id="contactTel"
				name="contactTel" value="${warehouse.contactTel}"/></li>
			<li><label for="">邮箱：</label><input type="text"
				id="contactEmail" name="contactEmail" value="${warehouse.contactEmail}"/></li>
			<li>
	            <label>配送范围：<span style="color: red;">*</span></label>
	            <div id="PROVINCE_edit" style="margin-left:165px"></div>
        	</li>
		</ul>
		<div class="edBox tac">
			<a href="javascript:;" class="btnG btnBl" id="editWarehouseBtn" onclick="editWarehouseBtn()">编辑</a>
			<a href="javascript:;" class="btnG btnOg" id="cancelWarehouseBtn" onclick="cancelWarehouseBtn(1)">取消</a>
		</div>
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
		        checkbox="<label><input  type='checkbox' name='province' id='p_"+p.areaCode+"'  value='"+p.areaCode+"'/><span id='prv_"+p.areaCode+"'>"+p.areaName+"</span></label>";
		    	    $("#PROVINCE_edit").append(checkbox);
		    	count++;
		    	if(count%3==0){
		    		$("#PROVINCE_edit").append("</br>");
		    	}
		    });
		}
	});
    
    //修改配送范围修改信息显示 
     var rangeList =$("#areaCode").val();
    var checkProvince= new Array(); //定义一数组 
    checkProvince=rangeList.split(","); //字符分割 
    for(var i=0;i<checkProvince.length;i++){
    	$("#p_"+checkProvince[i]).attr('checked','checked');
    } 

 });
 </script>
</body>
</html>