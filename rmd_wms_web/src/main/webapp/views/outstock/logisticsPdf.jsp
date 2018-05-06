<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Insert title here</title>
<style>
        *{font-size:10px;font-family: "SimSun";}
        body{margin:0;padding:0;}
        h1{position: relative;text-align: center;height:71px;line-height:71px;font-size:18px;font-weight:bold;margin:0;padding:0;}
        h1 img{position: absolute;left:10px;top:10px;border: none;}
        table{margin:10px;}
        table tr td{
            word-break:break-all;
        }
        .zhang{background:url(${fundsSource.companySeal});width:153px;height:105px;right:10px;bottom:10px;position: absolute;}
        .p-left{margin:3px 0;}
        .pageHead { position: relative; height:100px}
        .phIcon { position: absolute; height: 4.5rem; }
    </style>
</head>
<body>
<c:forEach items="${logisticsBillPrints}" var="obj">  
<div>
<!-- 	<div> -->
<!-- 		<table> -->
<!-- 			<tr><td>寄件人姓名</td> -->
<%-- 				<td>${obj.senderFromName}</td> --%>
<!-- 				<td></td> -->
<!-- 				<td></td> -->
<!-- 			</tr> -->
<!-- 			<tr><td>单位名称</td> -->
<%-- 				<td>${obj.senderCompanyName}</td> --%>
<!-- 				<td>始发地</td> -->
<!-- 				<td></td> -->
<!-- 			</tr> -->
<%-- 			<tr><td>寄件地址</td><td>${obj.senderAddr}</td><td></td><td></td></tr> --%>
<%-- 			<tr><td>手机</td><td>${obj.senderMobile}</td><td></td><td></td></tr> --%>
<!-- 		</table> -->
<!-- 	</div> -->
	<div style="margin-left:400px">
		<table>
			<tr><td>收件人姓名</td><td>${obj.toName}</td><td>目的地</td><td>${obj.toDestination}</td></tr>
			<tr><td>单位名称</td><td>${obj.toCompanyName}</td><td></td><td></td></tr>
			<tr><td>收件地址</td><td>${obj.toAddr}</td><td></td><td></td></tr>
			<tr><td>手机</td><td>${obj.toMobile}</td><td></td><td></td></tr>
		</table>
	</div>
	<div></div>
	<div></div>
</div>
</c:forEach>
</body>
</html>