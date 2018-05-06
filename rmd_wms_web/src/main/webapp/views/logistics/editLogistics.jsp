<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>配送管理-承运商管理-修改承运商</title>

	</head>
	<body>
    <link rel="stylesheet" type="text/css"   href="<%=basePath%>/static/css/addLogisticsAdFreight.css?v=0.17" />
		<div class="carrier">
			
			<div class="cartitle">
				<span id="carrierinfo" >基本信息</span>
			</div>
			<div class="carcontext">
				<form id="formLogis" method="post" >
			 	<div id="carcontexttop" class="carcontop">
					<div>
						<input type="hidden"  id="logisId" name="id" value="${Company.id}"/>
                        <%--配送范围名称--%>
                        <input type="hidden"  id="deliveryName" name="deliveryName" value="${Company.deliveryName}"/>
                        <input type="hidden"  id="deliveryCode" name="deliveryCode" value="${Company.deliveryCode}"/>
                        <%--配送范围 格式:[{areaName:xx,areaCode:xx},{areaName:xx,areaCode:xx}]--%>
                        <input type="hidden"  id="deliveryRange" name="deliveryRange"/>
					    <span>名称：<span style="color: red;">*</span></span>
						<input type="text"  id="logisName" name="name" class="easyui-validatebox" data-options="required:true,validType:'logisName'" value="${Company.name}" placeholder="请输入承运商名称" style="border:solid 1px #acacac";/>
					</div>
					<div>
					    <span>联系人：</span>
						<input type="text" id="contactName" name="contactName" value="${Company.contactName}" placeholder="请输入联系人名称"  style="border:solid 1px #acacac";/>
					</div>
				 	<div>
					    <span>编码：<span style="color: red;">*</span></span>
						<input  type="text" id="code" name="code" value="${Company.code}" class="easyui-validatebox" data-options="required:true,validType:'logisCode'" placeholder="请输入编码" style="border:solid 1px #acacac";/>
					</div>
					<div>
					    <span>联系电话： </span><input type="text" id="phone" name="phone" class="easyui-validatebox" data-options="validType:'phone'" value="${Company.phone}" placeholder="请输入联系电话"  style="border:solid 1px #acacac";/>
					</div>
					<div class="widthhundred">
					    <span>配送范围：<span style="color:red;">*</span></span>
					    <div class="borderac" style="padding-bottom:60px">
							<c:forEach var="area" items="${areaList}">
								<div class="chkmodule">
									<span><input type="checkbox" name="checkboxall" value="${area.areaCode}" text="${area.areaName}"/> ${area.areaName}  </span>
									<span class="bottomarrow"></span>
									<span class="countdistrict" name="${area.areaCode}"><i></i></span>
									<div class="cityallnm">
                                        <c:forEach var="child" items="${area.children}">
                                            <span><input type="checkbox" name="cekItem" value="${child.areaCode}" text="${child.areaName}" parentName="${area.areaName}" parentCode="${area.areaCode}" />${child.areaName}</span>
                                        </c:forEach>
										<p class="colsedistrict">关闭</p>
									</div>
								</div>
							</c:forEach>
					     </div>
					</div>
					<div>
					    <span>承运重量：<span style="color:red;">*</span></span>
					    <input type="text"  style="width:100px;height: 28px;line-height: 28px;border:solid 1px #acacac" class="easyui-numberbox" data-options="required:true,min:0" placeholder="最小重量" id="minWeight"  name="minWeight" value="${Company.minWeight}"/>
					    <span style="width:10px;text-align: center;">-</span>
                        <input type="text"  style="width:100px;height: 28px;line-height: 28px;margin-left: 0;border:solid 1px #acacac" class="easyui-numberbox" data-options="required:true,min:0,validType:'maxWeight'" placeholder="最大重量" id="maxWeight" name="maxWeight" value="${Company.maxWeight}"/>
					</div>
					<div class="widthhundred">
					    <span>状态：<span style="color: red;">*</span></span>
					    <div style="margin-top:-5px;"> 
					    <a href="javascript:void(0)">
		                <input name="status" type="radio" value="1" <c:if test="${Company.status eq 1}">checked="true"</c:if> >启用</a>
		                <a href="javascript:void(0)">
		                <input name="status" type="radio" value="0" <c:if test="${Company.status eq 0}">checked="true"</c:if>>禁用</a>
		                </div>
					</div>
					 
					<div class="widthhundred btncenter">
						<lable id="btnUpdate" class="btntempl">保存</lable>
						<lable id="btnEditLogisticsCancel" class="btntempl">取消</lable>
					</div>
					<div class="clearboth"></div>
				</div>
                </form>
				


			</div>
		</div>
	<script src="<%=basePath%>/static/js/logistics/logisticsAdFreightBase.js?v=0.06"></script>
    <script src="<%=basePath%>/static/js/logistics/addLogisticsAdFreight.js?v=0.14"></script>

	<script type="text/javascript">
	//	$(function() {
<%--//			$(window).load(function(){--%>
			<%--var deliveryName='${Company.deliveryName}';--%>
			<%--var arr=deliveryName.split(",");--%>
			<%--console.log(deliveryName);--%>
			<%--console.log('${Company.deliveryName}');--%>
			<%--console.log(6666);--%>

			<%--$(" .chkmodule [name = checkboxall]:checkbox").each(function(){--%>
				<%--if(arr.length>0){--%>
					<%--for(var i=0;i<arr.length;i++){--%>
						<%--if(arr[i]==$(this).attr('text')){--%>
							<%--$(this).click();--%>
							<%--//$(this).click();--%>
						<%--//	$(this).click();--%>
						<%--//	$(this).prop("checked",true);--%>
						<%--}--%>
					<%--}--%>
				<%--}--%>
			<%--});--%>
			<%--//配送范围市级别信息--%>
			<%--$(" .chkmodule .cityallnm [name = cekItem]:checkbox").each(function(){--%>
				<%--if(arr.length>0){--%>
					<%--for(var i=0;i<arr.length;i++){--%>
						<%--if(arr[i]==$(this).attr('text')){--%>
							<%--$(this).click();--%>
						  <%--//	$(this).click();--%>
						  <%--//	$(this).click();--%>
     					  <%--//$(this).prop("checked",true);--%>
						<%--}--%>
					<%--}--%>
				<%--}--%>
			<%--});--%>

			<%--$(".chkmodule [name = checkboxall]:checkbox").each(function(){--%>
				    <%--var count=0;--%>
					<%--$(this).parent().parent().find(".cityallnm [name = cekItem]:checkbox").each(function(){--%>
						<%--if($(this).is(":checked")){--%>
							<%--count++;--%>
						<%--}--%>
					<%--});--%>
				<%--if(count > 0){--%>
			       <%--$(this).parent().next().next().show().find("i").text(count);--%>
<%--//					$(this).click();--%>
<%--//					$(this).click();--%>
				<%--}--%>
			<%--});--%>

			var deliveryCode='${Company.deliveryCode}';

			var citynm=deliveryCode.split(",");

			if(citynm.length > 0){
				//先进行一次筛选 选中
				for(var i=0; i<citynm.length;i++){
					$(" .chkmodule > span:nth-child(1)").each(function(){
						//var str = $(this).text();
						var str = $(this).find("[name = checkboxall]:checkbox").val();
						if($.trim(str)==citynm[i]){
							//父集
							$(this).find("[name = checkboxall]:checkbox").each(function(){
								$(this).prop("checked", true);
								$(this).parent().parent().find(".cityallnm [name = cekItem]:checkbox").each(function(){
									$(this).prop("checked",true);
								});
							});
						}
					});
					$(".chkmodule .cityallnm > span").each(function(){
						//var str1 = $(this).text();
						var str1 = $(this).find("[name = cekItem]:checkbox").val();
						if($.trim(str1)==citynm[i]){
							//子集
							$(this).find("[name = cekItem]:checkbox").each(function(){
								$(this).prop("checked", true);
							});
						}
					});
				}

				//input显示状态效果
				$(".chkmodule").each(function(){
					var num=0;
					var count=0;
					$(this).find(".cityallnm span [name = cekItem]:checkbox").each(function(){
						count++;
						if($(this).is(":checked")){
							num++;
						}
					});
					if(num > 0) {
						$(this).find(".countdistrict").show().find("i").text("("+num+")");
					}
					if(count > 0 && num > 0 && count > num){
						console.log("ok"+count);
						$(this).find(">span [name = checkboxall]:checkbox").addClass("searchallchk");
					}
				});
				///显示对应的模块当前选中的个数
				$(" .chkmodule").each(function(){
					var num=0;
					$(this).find(".cityallnm span [name = cekItem]:checkbox").each(function(){
						if($(this).is(":checked")){
							num++;
						}
					});
					if(num > 0) {
						$(this).find(".countdistrict").show().find("i").text("("+num+")");
					}
				});
			}
	//	});


	</script>
	</body>
</html>
