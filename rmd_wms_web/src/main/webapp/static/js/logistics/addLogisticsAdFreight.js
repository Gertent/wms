

//验证承运商基本信息，并设置承运商配送范围
function validLogistics(){
    var isValid = $("#formLogis").form("validate");
    if(!isValid){
        return false;
    }else{
        var arrAreaText=new Array();//所有地区名称，展示的配送范围名称
        var arrAreaCode=new Array();//所有地区编码，展示的配送范围编码
        var arrAreaAll=new Array();//所有地区json数组格式:[{cityName:xx,cityCode:xx,provName:xx,provCode:xx},...]
        var objArea=null;
        $(".carcontop .chkmodule [name = checkboxall]:checkbox").each(function(){
            var provFlag=$(this).is(':checked');//省全部选中状态
            if(provFlag){
                // console.log("选中的省："+$(this).attr('text')+"----"+$(this).val());
                arrAreaText.push($(this).attr('text'));
                arrAreaCode.push($(this).val());
            }else{
                var provPartFlag=$(this).parent().hasClass("leftarrow");//省部分选中
                //获取选中的市级地区
                $(this).parent().parent().find(".cityallnm [name = cekItem]:checkbox").each(function(){
                    if($(this).is(":checked")){
                        // console.log("选中的部分市："+$(this).attr('text')+"----"+$(this).val());
                        arrAreaText.push($(this).attr('text'));
                        arrAreaCode.push($(this).val());
                    }
                });
            }
        });
        //配送范围市级别信息
        $(".carcontop .chkmodule .cityallnm [name = cekItem]:checkbox").each(function(){
            if($(this).is(":checked")){
                // console.log("选中的市："+$(this).attr('text')+"----"+$(this).val());
                objArea=new Object();
                objArea.cityName=$(this).attr('text');
                objArea.cityCode=$(this).val();
                objArea.provName=$(this).attr('parentName');
                objArea.provCode=$(this).attr('parentCode');
                arrAreaAll.push(JSON.stringify(objArea));
            }
        });
        // console.log(arrAreaText.toString()+arrAreaCode.toString());
        // console.log("承运商配送范围："+arrAreaAll.toString());
        $('#deliveryName').val(arrAreaText.toString());
        $('#deliveryCode').val(arrAreaCode.toString());
        $('#deliveryRange').val("["+arrAreaAll.toString()+"]");
        if($('#deliveryName').val()==''){
            $.messager.alert('提醒', '请选择配送范围!');
            return false;
        }
        return true;
    }
}
//验证默认运费
function validateDefaultFreightFun(){
    var flag=true;
    var firstWeight=$('#firstWeight').val();
    var firstPrice=$('#firstPrice').val();
    var secondWeight=$('#secondWeight').val();
    var secondPrice=$('#secondPrice').val();
    var deliveryPrice=$('#deliveryPrice').val();
    if (firstWeight == null || firstWeight == undefined || firstWeight == '') {
        $.messager.alert('提醒','首重量不能为空！');
        return false;
    }
    if (firstPrice == null || firstPrice == undefined || firstPrice == '') {
        $.messager.alert('提醒','首费不能为空！');
        return false;
    }
    if (secondWeight == null || secondWeight == undefined || secondWeight == '') {
        $.messager.alert('提醒','续重量不能为空！');
        return false;
    }
    if (secondPrice == null || secondPrice == undefined || secondPrice == '') {
        $.messager.alert('提醒','续费不能为空！');
        return false;
    }
    if (deliveryPrice == null || deliveryPrice == undefined || deliveryPrice == '') {
        $.messager.alert('提醒','派送费不能为空！');
        return false;
    }
    return flag;
}
//验证非默认运费
function validateFreight(freightAreaAllArr){
    var freightFlag=true;
    for(var k=0;k<freightAreaAllArr.length;k++){
        if (freightAreaAllArr[k].firstWeight == null || freightAreaAllArr[k].firstWeight == undefined || freightAreaAllArr[k].firstWeight == '') {
            $.messager.alert('提醒','首重量不能为空！');
            freightFlag=false;
            break;
        }
        if (freightAreaAllArr[k].firstPrice == null || freightAreaAllArr[k].firstPrice == undefined || freightAreaAllArr[k].firstPrice == '') {
            $.messager.alert('提醒','首费不能为空！');
            freightFlag=false;
            break;
        }
        if (freightAreaAllArr[k].secondWeight == null || freightAreaAllArr[k].secondWeight == undefined || freightAreaAllArr[k].secondWeight == '') {
            $.messager.alert('提醒','续重量不能为空！');
            freightFlag=false;
            break;
        }
        if (freightAreaAllArr[k].secondPrice == null || freightAreaAllArr[k].secondPrice == undefined || freightAreaAllArr[k].secondPrice == '') {
            $.messager.alert('提醒','续费不能为空！');
            freightFlag=false;
            break;
        }
        if (freightAreaAllArr[k].deliveryPrice == null || freightAreaAllArr[k].deliveryPrice == undefined || freightAreaAllArr[k].deliveryPrice == '') {
            $.messager.alert('提醒','派送费不能为空！');
            freightFlag=false;
            break;
        }
    }
    return freightFlag;
}


function arrayToJson(formArray){
    var dataArray = {};
    $.each(formArray,function(){
        if(dataArray[this.name]){
            if(!dataArray[this.name].push){
                dataArray[this.name] = [dataArray[this.name]];
            }
            dataArray[this.name].push(this.value || '');
        }else{
            dataArray[this.name] = this.value || '';
        }
    });
    return JSON.stringify(dataArray);
}

function uuid() {
    var s = [];
    var hexDigits = "0123456789abcdef";
    for (var i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[8] = s[13] = s[18] = s[23] = "-";

    var uuid = s.join("");
    return uuid;
}



//提交事件
$('#btnOk').click(function(){
    // console.log(arrayToJson($('#formLogis').serializeArray()));
    //验证承运商基本信息，并设置承运商配送范围
    var flag=validLogistics();
    if(!flag){
        return;
    }
    //验证默认运费
    var validateDefaultFreightFlag=validateDefaultFreightFun();
    if(!validateDefaultFreightFlag){
        return;
    }
    var freightAreaAll=new Array();//运费模板地区，json数组格式:[{areaName:xx,areaCode:xx},{areaName:xx,areaCode:xx}]
    var freightAreaAllArr=new Array();
    var freightTemplate=new Object();
    //设置默认运费
    freightTemplate.priceType=1;                                    //计价方式 1:按重量
    freightTemplate.name='';
    freightTemplate.code='';
    freightTemplate.freightType=1;                                 //是否默认运费 0:否 1:是
    freightTemplate.firstWeight=$('#firstWeight').val();        //首重量
    freightTemplate.firstPrice=$('#firstPrice').val();          //首运费
    freightTemplate.secondWeight=$('#secondWeight').val();      //续重量
    freightTemplate.secondPrice=$('#secondPrice').val();        //续费
    freightTemplate.deliveryPrice=$('#deliveryPrice').val();    //派送费
    freightTemplate.freightRange='';                               //运费模板范围
    freightAreaAll.push(JSON.stringify(freightTemplate));           //添加默认运费
    //设置非默认运费
    var flag=true;
    $("#freightTb").find("tr").each(function(){
        freightTemplate=new Object();
        var tdArr = $(this).children();
        if(tdArr.eq(0).text()==''||gloabl_templateName==tdArr.eq(0).text()){
            flag =false;
            return flag;
        }
        freightTemplate.priceType=1;                                    //计价方式 1:按重量
        freightTemplate.name=tdArr.eq(0).text();
        freightTemplate.code=tdArr.eq(10).text();
        freightTemplate.freightType=0;                                  //是否默认运费 0:否 1:是
        freightTemplate.firstWeight=tdArr.eq(2).find("input").val();    //首重量
        freightTemplate.firstPrice=tdArr.eq(3).find("input").val();     //首运费
        freightTemplate.secondWeight=tdArr.eq(4).find("input").val();   //续重量
        freightTemplate.secondPrice=tdArr.eq(5).find("input").val();    //续费
        freightTemplate.deliveryPrice=tdArr.eq(6).find("input").val();  //派送费
        freightTemplate.freightRange=tdArr.eq(7).text();                 //运费模板范围
        freightAreaAll.push(JSON.stringify(freightTemplate));           //添加运费
        freightAreaAllArr.push(freightTemplate);
    });
    // console.log("模板范围："+freightAreaAll.toString());
    if(!flag){
        $.messager.alert('提醒','存在未选择地区！');
        return;
    }
    //验证非默认运费
    var freightFlag=validateFreight(freightAreaAllArr);
    if(!freightFlag){
        return;
    }
    var freightTemplate="["+freightAreaAll.toString()+"]";


    $.ajax({
        type : 'post',
        url : "./addLogisticsCompany",
        data : {
            'logisticsCompany':arrayToJson($('#formLogis').serializeArray()),
            'deliveryRange' : $('#deliveryRange').val(),
            'freightTemplate':freightTemplate
        },
        success : function(result) {
            if(result.status=200){
                $.messager.alert('提醒','保存成功！');
                location.reload();
            }

        }
    });

});
//承运商及运费模板关闭界面
$('#btnLogisticsAdFreightClose').click(function(){
    $("#addLogisticsAdFreightBox").hide();
    $(".window").hide();
    $(".window-mask").hide();
    location.reload();
});


//运费模板地区选择
$('#freightAreaBtn').click(function(){
    var freightAreaAll=new Array();//运费模板地区，json数组格式:[{cityName:xx,cityCode:xx,provName:xx,provCode:xx},...]
    var freightArea=null;
    var valuestr =[];//保存checkbox中的value
    var nmstr =[];
    $(".carrierbottom .chkmodule").each(function(){
        //全选
        if($(this).find("[name = checkboxall]:checkbox").is(":checked") && $(this).find("[name = checkboxall]:checkbox").prop("disabled")==false){
            //获取名称
            var str = $.trim($(this).find(">span:nth-child(1)").text());
            nmstr.push(str);
            //获取value值
            var num = $(this).val();
            valuestr.push(num);
        }
        //子集
        $(this).find(".cityallnm  span").each(function(){
            if($(this).find("[name = cekItem]:checkbox").is(":checked") && $(this).find("[name = cekItem]:checkbox").prop("disabled")==false){
                //获取文本
                var str = $.trim($(this).text());
                nmstr.push(str);
                //获取value值
                var num = $(this).val();
                valuestr.push(num);
                freightArea=new Object();
                freightArea.cityName=$(this).find("input").attr('text');
                freightArea.cityCode=$(this).find("input").val();
                freightArea.provName=$(this).find("input").attr('parentName');
                freightArea.provCode=$(this).find("input").attr('parentCode');
                freightAreaAll.push(JSON.stringify(freightArea));
            }
        });
    });debugger;
    var nmstrtwo = [];//保存地区名称
    var nmvaluetwo = [];
    $(".carrierbottom .chkmodule").each(function(){
        //全选
        if($(this).find("[name = checkboxall]:checkbox").is(":checked") && $(this).find("[name = checkboxall]:checkbox").prop("disabled")==false){
            //获取名称
            var str = $.trim($(this).find(" > span:nth-child(1)").text());
            nmstrtwo.push(str);
            //获取value值
            var num = $(this).find("[name = checkboxall]:checkbox").val();
            nmvaluetwo.push(num);
        }else if(!$(this).find("[name = checkboxall]:checkbox").is(":checked") && $(this).find("[name = checkboxall]:checkbox").prop("disabled")==false){
            $(this).find(".cityallnm  span").each(function(){
                if($(this).find("[name = cekItem]:checkbox").is(":checked") && $(this).find("[name = cekItem]:checkbox").prop("disabled")==false){
                    //获取文本
                    var str = $.trim($(this).text());
                    nmstrtwo.push(str);
                    //获取value值
                    var num = $(this).find("[name = cekItem]:checkbox").val();
                    nmvaluetwo.push(num);
                }
            });
        }
    });


    // console.log(nmstrtwo.toString());
    // console.log(freightAreaAll.toString());
    // console.log(nmvaluetwo.toString());
    var freightUuid=$('#freightUuid').val();
    var uuid='';
    $("#freightTb").find("tr").each(function(){
        var tdArr = $(this).children();
        uuid=tdArr.eq(9).text();//行标识符
        if(freightUuid==uuid){
            tdArr.eq(0).html("<span>"+nmstrtwo.toString()+"<span>");//指定地区 名称
            tdArr.eq(7).html("["+freightAreaAll.toString()+"]");//运费模板范围
            tdArr.eq(10).html("<span>"+nmvaluetwo.toString()+"<span>");//指定地区code

        }
    });
    $("#freighttemplate").hide();
});




//修改承运商保存
$('#btnUpdate').click(function(){
    //验证承运商基本信息，并设置承运商配送范围
    var flag=validLogistics();
    if(!flag){
        return;
    }
    $.ajax({
        type : 'post',
        url : "./updateLogisticsCompany",
        data : {
            'logisticsCompany':arrayToJson($('#formLogis').serializeArray()),
            'deliveryRange' : $('#deliveryRange').val()
        },
        success : function(result) {
            if(result.status=200){
                $.messager.alert('提醒', '保存成功!');
                location.reload();
            }

        }
    });
});
//承运商编辑界面关闭
$('#btnEditLogisticsCancel').click(function(){
    $("#editLogisticsBox").hide();
    $(".window").hide();
    $(".window-mask").hide();
    location.reload();
});

//编辑运费模板保存
$('#btnFreightTemplateOk').click(function(){
    //验证默认运费
    var validateDefaultFreightFlag=validateDefaultFreightFun();
    if(!validateDefaultFreightFlag){
        return;
    }
    var freightAreaAll=new Array();//运费模板地区，json数组格式:[{areaName:xx,areaCode:xx},{areaName:xx,areaCode:xx}]
    var freightAreaAllArr=new Array();
    var freightTemplate=new Object();
    //设置默认运费
    freightTemplate.priceType=1;                                    //计价方式 1:按重量
    freightTemplate.name='';
    freightTemplate.code='';
    freightTemplate.freightType=1;                                 //是否默认运费 0:否 1:是
    freightTemplate.firstWeight=$('#firstWeight').val();        //首重量
    freightTemplate.firstPrice=$('#firstPrice').val();          //首运费
    freightTemplate.secondWeight=$('#secondWeight').val();      //续重量
    freightTemplate.secondPrice=$('#secondPrice').val();        //续费
    freightTemplate.deliveryPrice=$('#deliveryPrice').val();    //派送费
    freightTemplate.freightRange='';                               //运费模板范围
    freightAreaAll.push(JSON.stringify(freightTemplate));           //添加默认运费

    var flag=true;
    //设置非默认运费
    $("#freightTb").find("tr").each(function(){
        freightTemplate=new Object();
        var tdArr = $(this).children();
        if(tdArr.eq(0).text()==''||gloabl_templateName==tdArr.eq(0).text()){
            flag =false;
            return flag;
        }
        freightTemplate.priceType=1;                                    //计价方式 1:按重量
        freightTemplate.name=tdArr.eq(0).text();                        //模板名称
        freightTemplate.code=tdArr.eq(10).text();                       //模板名称对应code
        freightTemplate.freightType=0;                                  //是否默认运费 0:否 1:是
        freightTemplate.firstWeight=tdArr.eq(2).find("input").val();    //首重量
        freightTemplate.firstPrice=tdArr.eq(3).find("input").val();     //首运费
        freightTemplate.secondWeight=tdArr.eq(4).find("input").val();   //续重量
        freightTemplate.secondPrice=tdArr.eq(5).find("input").val();    //续费
        freightTemplate.deliveryPrice=tdArr.eq(6).find("input").val();  //派送费
        freightTemplate.freightRange=tdArr.eq(7).text();                 //运费模板范围
        freightAreaAll.push(JSON.stringify(freightTemplate));           //添加运费
        freightAreaAllArr.push(freightTemplate)
    });
    // console.log('运费编辑...');
    if(!flag){
        $.messager.alert('提醒','存在未选择地区！');
        return;
    }
    //验证非默认运费
    var freightFlag=validateFreight(freightAreaAllArr);
    if(!freightFlag){
        return;
    }
    // console.log("模板范围："+freightAreaAll.toString());
    var freightTemplate="["+freightAreaAll.toString()+"]";
    var logisticsId=$('#logisticsId').val();

    $.ajax({
        type : 'post',
        url : "./updateFreightTemplate",
        data : {
            'logisticsId':logisticsId,
            'freightTemplate':freightTemplate
        },
        success : function(result) {
            if(result.status=200){
                $.messager.alert('提醒','保存成功！');
                location.reload();
            }

        }
    });
});
//运费模板界面关闭
$('#btnFreightTemplateClose').click(function(){
    $("#editFreightTemplateBox").hide();
    $(".window").hide();
    $(".window-mask").hide();
    location.reload();
});


//检查承运商名称是否重复
function checkCompany(){
    var flag=true;
    var name=$("#logisName").val();
    var id=$('#logisId').val();
    $.ajax({
        type : 'post',
        async:false,
        url : "./checkCompanyName",
        data : {
            'id':id,
            'name' : name
        },
        success : function(result) {
            if(result=="true"){
                flag=false;
            }
        }
    });
    return flag;
}

//检查承运商编码是否重复
function checkLogisCode(){
    var flag=true;
    var code=$("#code").val();
    var id=$('#logisId').val();
    $.ajax({
        type : 'post',
        async:false,
        url : "./checkCompanyCode",
        data : {
            'id':id,
            'code' : code
        },
        success : function(result) {
            if(result=="true"){
                flag=false;
            }
        }
    });
    return flag;
}

//数据验证
$.extend($.fn.validatebox.defaults.rules, {
    //承运商
    logisName: {
        validator: function (value) {
            var flag=checkCompany();
            return flag;
        },
        message: "承运商名称重复!"
    },
    phone:{//既验证手机号，又验证座机号
        validator: function(value, param){
            return /(^(0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$)|(^((\d{3})|(\d{3}\-))?(1[358]\d{9})$)/.test(value);
        },
        message: '请输入正确的电话号码'
    },
    maxWeight:{
        validator: function(value, param){
            var minWeight=$('#minWeight').val();
            var flag=true;
            if(parseFloat(value)<parseFloat(minWeight)){
                flag=false;
            }
            return flag;
        },
        message: '最大重量应大于最小重量'
    },
    logisCode:{
        validator: function (value) {
            var flag=checkLogisCode();
            return flag;
        },
        message: "承运商编码重复!"
    }
})
//验证整数和浮点数
function CheckInputIntFloat(oInput){
    if('' != oInput.value.replace(/\d{1,}\.{0,1}\d{0,}/,'')){
        oInput.value = oInput.value.match(/\d{1,}\.{0,1}\d{0,}/) == null ? '' :oInput.value.match(/\d{1,}\.{0,1}\d{0,}/);
    }
}

var gloabl_templateName='未添加地区';