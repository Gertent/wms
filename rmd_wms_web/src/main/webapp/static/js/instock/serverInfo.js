//残次品按钮
function incomplete(id) {

	var checkVal = $("input[name='staus_" + id + "']:checked").val();

	// 残次品选中
	if (checkVal != "1") {
		$("#submit" + id).removeAttr("readonly");
		$("#submit" + id).focus();
		$("#submit" + id).css("background","red");
	} else {
		// 良品选中
		$("#submit" + id).attr('readonly', 'readonly');
		$("#submit" + id).val(0);
	}
}

// 确定入库
function serviceOfsingleFormSubmit() {
	var isagree = $("input[name=agree]:checked").val();
	if (typeof (isagree) == "undefined") {
		alert("请选择是否同意入库!");
		return false;
	}

	var reVal = $("#allrepository").val();

	if (isagree == "1" && (reVal == "" || reVal == "undefined")) {
		alert("请选择退入仓库！");
		return false;
	}
	var array = new Object();
	array.serverNo = $("#serverNo").html();
	array.orderNo = $("#orderNo").html();
	array.flag = isagree;
	array.remark = $("#remark").val();
	array.repository = $("#allrepository").combobox('getValue');
	var gCode = "";
	$(".gCode").each(function() {
		gCode += $(this).html() + ',';
	});
	var counts = "";
	$("input[name='count']").each(function(index, item) {
		counts += $(this).val() + ",";
	});

	var oldcounts = "";
	$("input[name='oldcount']").each(function(index, item) {
		oldcounts += $(this).val() + ",";
	});

	var status = '';
	var a = new Array();
	a = gCode.split(",");
	for (var i = 0; i < a.length; i++) {
		if (a[i] != '')
			status += $("input[name=staus_" + a[i] + "]:checked").val() + ",";
	}
	var parameters = array.serverNo + ',' + array.orderNo + ',' + array.flag + ',' + array.remark + ',' + array.repository;
	var basepath = $("#basepath").val();

    $.ajax({
		url:basepath+'/instock/InstockBillSave',
		type:'post',
		data:{'params':parameters,'gCode':gCode,'counts':counts,'oldcounts':oldcounts,'status':status},
		success:function(result){

			if(result == "true"){
			   window.close();
				location.reload();
			}else{
				$.messager.alert('提醒', '操作失败 !');
			}
		}
	});
	
	function sort() {
		location.reload();
	}
	
	//取消按钮
	$("#cancel").click(function() {
		$("#serverStock_list").window("close");
	});
}