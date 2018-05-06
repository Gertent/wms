<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>首页/内容</title>
    <link rel="stylesheet" href="<%=basePath%>/static/css/common/reset.css" type="text/css"/>
    <style>
        .iframeBox{
            width: auto;
            height: calc(100% - 40px);
            background: url(<%=basePath%>/static/images/indexPic.png) no-repeat center;
        }
    </style>
</head>
<body>
<div class="center iframeBox">
</div>
</body>
</html>