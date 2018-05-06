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
<title>添加仓库</title>
</head>
<body class="easyui-layout">
<!-- 	<div class="tabContentBox" style="display: none;" -->
<!-- 		id="addPurchaseBiLLInfo"> -->
		<ul class="inBox">
			<li><label for="">仓库编码：</label><input type="text" id="code"
				name="code" /></li>
			<li><label for="">仓库名称：</label><input type="text"
				id="wareName" name="wareName" /></li>
			<li><label for="">地址：</label><input type="text" id="address"
				name="address" /></li>
			<li class="type"><label for="">状态：</label>
				<div class="radioBox dib">
					<input type="radio" id="status" name="status" value="1"/>启用
					<input type="radio" id="status" name="status" value="0"/>禁用
				</div>
				</li>
			<li><label for="">联系人：</label><input type="text"
				id="contactName" name="contactName" /></li>
			<li><label for="">联系电话：</label><input type="text" id="contactTel"
				name="contactTel" /></li>
			
			<li><label for="">邮箱：</label><input type="text"
				id="contactEmail" name="contactEmail" /></li>
			<li>
	            <label>配送范围：<span style="color: red;">*</span></label>
	            <div id="PROVINCE" style="margin-left:203px"></div>
        	</li>
		</ul>
		<div class="edBox tac">
			<a href="javascript:;" class="btnG btnBl" id="addWarehouseBtn" onclick="addWarehouseBtn()">继续添加</a>
			<a href="javascript:;" class="btnG btnOg" id="cancelWarehouseBtn" onclick="cancelWarehouseBtn(0)">取消</a>
		</div>
<script type="text/javascript">
$(function(){
	$.ajax({
		type : 'get',
		url : "<%=basePath%>/logistics/getAreaInfo?parentCode=",
		success : function(result) {			
			//省级赋值name='province'点击取值所有checkbox选中的值
		    var count=0;
		    $.each(result, function (k, p) {
		    	var checkbox="";
		        checkbox="<label><input  type='checkbox' name='province' id='p_"+p.areaCode+"'  value='"+p.areaCode+"'/><span id='prv_"+p.areaCode+"'>"+p.areaName+"</span></label>";
		    	    $("#PROVINCE").append(checkbox);
		    	count++;
		    	if(count%3==0){
		    		$("#PROVINCE").append("</br>");
		    	}
		    });
		}
	});
 
 });
 </script>
</body>
</html>