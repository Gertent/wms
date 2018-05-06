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
		<title>配送管理-承运商管理-编辑运费模板</title>
	</head>
	<body>
    <link rel="stylesheet" type="text/css"   href="<%=basePath%>/static/css/addLogisticsAdFreight.css?v=0.013" />
		<div class="carrier">
			
			<div class="cartitle">
				<span id="carrierinfoEx" >运费模板</span>
			<%--	<span id="carriertempl" class="active">运费模板</span>--%>
			</div>
			<div class="carcontext">
				
				<div id="carcontextbottom" class="carconbottom" style="display: block;">
					<input type="hidden" id="logisticsId" value="${logisticsId}"/>
					<div>
					    <span>计价方式：<span style="color: red;">*</span></span>
					     <input type="radio" checked="checked" id="priceType" name="priceType" style="margin-left:20px"/>按重量
					</div>
					
					<div class="widthhundred">
					    <span>计算方式：<span style="color: red;">*</span></span>
					    <div>
					    	<p style="color:lightslategray">除指定地区外，其余地区的运费采用“默认运费”</p>
					    	<div class="carrierbottom borderac">
					    	    <ul>
					    			<li>默认:</li>
					    			<li>首重量<input type="text" name="firstWeight" id="firstWeight" value="${defaultTemplate.firstWeight}"/>kg</li>
					    			<li>首费<input type="text" name="firstPrice" id="firstPrice" value="${defaultTemplate.firstPrice}"/>元</li>
					    			<li>续重量<input type="text" name="secondWeight" id="secondWeight" value="${defaultTemplate.secondWeight}"/>kg</li>
					    			<li>续费<input type="text" name="secondPrice" id="secondPrice" value="${defaultTemplate.secondPrice}"/>元</li>
					    			<li>派送费<input type="text" name="deliveryPrice" id="deliveryPrice" value="${defaultTemplate.deliveryPrice}"/>元</li>
					    		</ul>
					    		
					    		<table id="carrierlist">
					    			<thead>
					    				<tr>
					    					<th style="width: 200px">指定地区</th>
					    					<th></th>
					    					<th>首重量</th>
						    				<th>首费</th>
						    				<th>续重量</th>
						    				<th>续费</th>
						    				<th>派送费</th>
                                            <th style="display:none;">范围</th>
                                            <th style="display:none;">id</th>
                                            <th  style="display:none;">uuid</th>
                                            <th  style="display:none;">指定地区IDs</th>
						    				<th>操作</th>
					    				</tr>
					    			</thead>
					    			<tbody id="freightTb">
										<c:forEach var="template" items="${templateList}">
											<tr> <td><span>${template.name}</span></td> <td><span class='carspancolor errieditor'>编辑</span></td>
												<td><input type='text' value="${template.firstWeight}" onkeyup='javascript:CheckInputIntFloat(this);'/></td>
												<td><input type='text' value="${template.firstPrice}" onkeyup='javascript:CheckInputIntFloat(this);'/></td>
												<td><input type='text' value="${template.secondWeight}" onkeyup='javascript:CheckInputIntFloat(this);'/></td>
												<td><input type='text' value="${template.secondPrice}" onkeyup='javascript:CheckInputIntFloat(this);'/></td>
												<td><input type='text' value="${template.deliveryPrice}" onkeyup='javascript:CheckInputIntFloat(this);'/></td>

												<td style='display:none;'>${template.freightRange}</td>
												<td style='display:none;'>${template.id}</td>
												<td style='display:none;'>${template.id}</td>
												<td style='display:none;'>${template.code}</td>

												<td><span class='carspancolor erridelete'>删除</span></td>
												</tr>
										</c:forEach>
					    			</tbody>
					    		</table>
					    		<span id="addcarrier" class="carspancolor">为指定地区城市设置运费 </span>
					    	
 	
					       <div id="freighttemplate" class="freighttemplate  borderac">
                           <%--要操作的行标识--%>
                           <input type="hidden" id="freightUuid"/>
					       	<h3 style="padding-left:20px;">选择地区</h3>
							   <div class="freighttel">



                                   <c:forEach var="area" items="${areaList}">
                                       <div class="chkmodule">
                                           <span><input type="checkbox" name="checkboxall" value="${area.areaCode}" text="${area.areaName}"/> ${area.areaName}  </span>
                                           <span class="bottomarrow"></span>
                                           <span class="countdistrict"><i></i></span>
                                           <div class="cityallnm">
                                               <c:forEach var="child" items="${area.children}">
                                                   <span><input type="checkbox" name="cekItem" value="${child.areaCode}" text="${child.areaName}" parentCode="${area.areaCode}" parentName="${area.areaName}"/>${child.areaName}</span>
                                               </c:forEach>
                                               <p class="colsedistrict">关闭</p>
                                           </div>
                                       </div>
                                   </c:forEach>
                               </div>
					     	 
					     	<div style="width:740px; margin-top:430px; text-align: right;">
                                <lable class="btntempl" id="freightAreaBtn">保存</lable>
                                <lable class="btncencle">取消</lable>
							</div>
					     </div>
 
					   </div>
					 </div>
					</div>
				<div style="width:740px; margin-top:330px; text-align: right;"> 
					     		<button class="btntempl" id="btnFreightTemplateOk">确定</button>
					     		<button class="btncencle" id="btnFreightTemplateClose">取消</button>
				</div>
				</div>

			</div>
		</div>
    <script src="<%=basePath%>/static/js/logistics/logisticsAdFreightBase.js?v=0.06"></script>
    <script src="<%=basePath%>/static/js/logistics/addLogisticsAdFreight.js?v=0.14"></script>
	<script type="text/javascript">
		$(function() {
			$('.carrierinfo').click(function(){});
		});


	</script>
	</body>
</html>
