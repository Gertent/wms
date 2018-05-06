var basepath = $("#basepath").val();

//表单提交
$('#btnSubmit').click(function(){
	$('#saveForm').form({    
		url:basepath+'/check/submitChecks',    
	    onSubmit: function(){
	    	var file1=$('#file1').val();
	    	var file2=$('#file2').val();
	    	var file3=$('#file3').val();
	    	var file4=$('#file4').val();
	        if(file1==''&&file2==''&&file3==''&&file4==''){
	        	$.messager.alert('提醒', '请至少上传一个文件!');
	    		return false;
	        }  
	    },    
	    success:function(data){
			$('#checkInfo_list').datagrid('reload');
			$('#addHandIn').window('close');
	    }    
	});    
	// submit the form    
	$('#saveForm').submit();
});
//取消操作
$('#btnCancel').click(function(){
	$('#addHandIn').window('close');
});