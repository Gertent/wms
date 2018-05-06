<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head lang="en">
<meta charset="UTF-8">
<title>盘点记录-提报</title>

<style>
.tableCon {
	/* border: 1px #dcdcdc solid; */
	
}

.tableCon tr {
	border-bottom: 1px #dcdcdc solid;
}

.tableCon td,.tableCon th {
	text-align: center;
	border-right: 1px #dcdcdc solid;
}

.tabtr {
	height: 40px
}
</style>
</head>

<body>
	<input type="hidden" id="basepath" value="<%=basePath %>"/>	
	<div id="orderDetailsBox" style="margin: 10px">
		<form id="saveForm" method="post" enctype="multipart/form-data">
		<input type="hidden" id="idsStr" name="idsStr" value="${ids}"/>
			<div style="padding: 10px 1px">
				<table width="650" class="tableCon" >
					<tr class="tabtr">
						<td>上传：</td>
						<td><input type="file" name="files" id="file1" /></td>
					</tr>
					<tr class="tabtr">
						<td></td>
						<td><input type="file" name="files" id="file2" /></td>
					</tr>
					<tr class="tabtr">
						<td></td>
						<td><input type="file" name="files" id="file3" /></td>
					</tr>
					<tr class="tabtr">
						<td></td>
						<td><input type="file" name="files" id="file4" /></td>
					</tr> 
					<tr>
						<td>备注：</td>
						<td><textarea name="description" cols="50" rows="3"></textarea></td>
					</tr>
					<tr>
						<td></td>
						<td><div class="edBox tac">
								<a href="javascript:;" class="btnG btnBl" id="btnSubmit">确定</a>
								<a href="javascript:;" class="btnG btnOg" id="btnCancel">取消</a>
							</div></td>
					</tr>

				</table>

			</div>
		</form>
	</div>
	<script type="text/javascript"
		src="<%=basePath%>/static/js/checkInfo/checkHandIn.js?v=0.06"></script>
	<script type="text/javascript">
		
	</script>
</body>
</html>