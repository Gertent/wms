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
		<title>配送管理-承运商管理-添加承运商</title>
	</head>
	<body>
    <link rel="stylesheet" type="text/css"   href="<%=basePath%>/static/css/addLogisticsAdFreight.css?v=0.14" />
		<div class="carrier">
			
			<div class="cartitle">
				<span id="carrierinfo" >基本信息</span>
				<span id="carriertempl" class="active">运费模板</span>
			</div>
			<div class="carcontext">
				<form id="formLogis" method="post" >
			 	<div id="carcontexttop" class="carcontop">
					<div>
						<input type="hidden"  id="logisId" name="id"/>
                        <%--配送范围名称--%>
                        <input type="hidden"  id="deliveryName" name="deliveryName"/>
                        <input type="hidden"  id="deliveryCode" name="deliveryCode"/>
                        <%--配送范围 格式:[{areaName:xx,areaCode:xx},{areaName:xx,areaCode:xx}]--%>
                        <input type="hidden"  id="deliveryRange" name="deliveryRange"/>
					    <span>名称：<span style="color: red;">*</span></span>
						<input type="text" id="logisName" name="name" class="easyui-validatebox" data-options="required:true,validType:'logisName'" placeholder="请输入承运商名称" style="border:solid 1px #acacac";/>

					</div>
					<div>
					    <span>联系人：</span>
						<input type="text" id="contactName" name="contactName" placeholder="请输入联系人名称"  style="border:solid 1px #acacac";/>
					</div>
				 	<div>
					    <span>编码：<span style="color: red;">*</span></span>
						<input  type="text" id="code" name="code" class="easyui-validatebox" data-options="required:true,validType:'logisCode'" placeholder="请输入编码" style="border:solid 1px #acacac";/>
					</div>
					<div>
					    <span>联系电话： </span><input type="text" id="phone" name="phone" class="easyui-validatebox" data-options="validType:'phone'" placeholder="请输入联系电话"  style="border:solid 1px #acacac";/>
					</div>
					<div class="widthhundred">
					    <span>配送范围：<span style="color:red;">*</span></span>
					    <div class="borderac" style="padding-bottom:60px">
							<c:forEach var="area" items="${areaList}">
								<div class="chkmodule">
									<span><input type="checkbox" name="checkboxall" value="${area.areaCode}" text="${area.areaName}"/> ${area.areaName}  </span>
									<span class="bottomarrow"></span>
									<span class="countdistrict"><i></i></span>
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
					    <span style="margin-right: 20px;">承运重量：<span style="color:red;">*</span></span>
					    <input type="text"  style="width:100px;height: 28px;line-height: 28px;border:solid 1px #acacac" class="easyui-numberbox" data-options="required:true,min:0" placeholder="最小重量" id="minWeight"  name="minWeight"/>
					    <span style="width:10px;text-align: center;">-</span>
                        <input type="text"  style="width:100px;height: 28px;line-height: 28px;margin-left: 0;border:solid 1px #acacac" class="easyui-numberbox" data-options="required:true,min:0,validType:'maxWeight'" placeholder="最大重量" id="maxWeight" name="maxWeight"/>
					</div>
					<div class="widthhundred">
					    <span>状态：<span style="color: red;">*</span></span>
					    <div style="margin-top:-5px;margin-left: 10px;"> 
					    <a href="javascript:void(0)" style="margin-right: 10px;">
		                <input name="status" type="radio" value="1" checked="true" >启用</a>
		                <a href="javascript:void(0)">
		                <input name="status" type="radio" value="0">禁用</a>
		                </div>
					</div>
					 
					<div class="widthhundred btncenter">
						<lable id="btnnext" class="btntempl">下一步，添加运费模板</lable>
					</div>
					<div class="clearboth"></div>
				</div>
                </form>
				
				<div id="carcontextbottom" class="carconbottom">
					
					<div>
					    <span>计价方式：<span style="color: red;">*</span></span>
					     <input type="radio" checked="checked" id="priceType" name="priceType" style="margin-left:20px"/>按重量
					</div>
					
					<div class="widthhundred">
					    <span>计算方式：<span style="color: red;">*</span></span>
					    <div>
					    	<p>除指定地区外，其余地区的运费采用“默认运费”</p>
					    	<div class="carrierbottom borderac">
					    	    <ul>
					    			<li>默认:</li>
					    			<li>首重<input type="text" name="firstWeight" id="firstWeight" class="easyui-numberbox" data-options="min:0,precision:2" style="width:55px;height:22px;" />kg</li>
					    			<li>首费<input type="text" name="firstPrice" id="firstPrice" class="easyui-numberbox" data-options="min:0,precision:2" style="width:55px;height:22px;"/>元</li>
					    			<li>续重<input type="text" name="secondWeight" id="secondWeight" class="easyui-numberbox" data-options="min:0,precision:2" style="width:55px;height:22px;"/>kg</li>
					    			<li>续费<input type="text" name="secondPrice" id="secondPrice" class="easyui-numberbox" data-options="min:0,precision:2" style="width:55px;height:22px;"/>元</li>
					    			<li>派送费<input type="text" name="deliveryPrice" id="deliveryPrice" class="easyui-numberbox" data-options="min:0,precision:2" style="width:55px;height:22px;"/>元</li>
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
					     		<button class="btntempl" id="btnOk">确定</button>
					     		<button class="btncencle" id="btnLogisticsAdFreightClose">取消</button>
				</div>
				</div>

			</div>
		</div>
    <script src="<%=basePath%>/static/js/logistics/logisticsAdFreightBase.js?v=0.06"></script>
    <script src="<%=basePath%>/static/js/logistics/addLogisticsAdFreight.js?v=0.14"></script>

	</body>
</html>
