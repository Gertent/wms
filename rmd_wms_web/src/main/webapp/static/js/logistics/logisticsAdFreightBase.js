//------------------------------------------
var vh = $(document).height();
$(".carrier").css("height", vh+"px");
//tab 切换
var $carrierinfo = $("#carrierinfo");
var $carriertempl = $("#carriertempl");
var $carcontexttop=$("#carcontexttop");
var $carcontextbottom=$("#carcontextbottom");
$carrierinfo.click(function(){
    $(this).removeClass("active");
    $carriertempl.addClass("active");
    $carcontexttop.show();
    $carcontextbottom.hide();
    $(".widthhundred .chkmodule").show();
});
function carriertempl(){
    $carriertempl.click(function(){
        $(this).removeClass("active");
        $carrierinfo.addClass("active");
        $carcontexttop.hide();
        $carcontextbottom.show();
        // displaynonechk();
    });
}
$("#btnnext").click(function(){

    //验证承运商基本信息，并设置承运商配送范围
    var flag=validLogistics();
    if(!flag){
        return;
    }

    $carriertempl.removeClass("active");
    $carrierinfo.addClass("active");
    $carcontexttop.hide();
    $carcontextbottom.show();
    //触发运费模板点击事件
    carriertempl();
    // displaynonechk();
});
//隐藏掉没有选中的方法
function displaynonechk(){
    $("#freighttemplate .freighttel").html("");
    //隐藏掉没有选中的
    $(".chkmodule [name = checkboxall]:checkbox").each(function(){
        if(!$(this).parent().hasClass("leftarrow") && !$(this).is(":checked")){
            $(this).parent().parent().hide();
        }else{
            $(this).prop("checked","checked");
            $(this).parent().parent().find(".cityallnm [name = cekItem]:checkbox").each(function(){
                if($(this).is(":checked")){
                    $(this).prop("checked","checked");
                }
            });
            var strhtm = $(this).parent().parent().html();
            $("#freighttemplate .freighttel").append("<div class='chkmodule'>"+ strhtm +"</div>");
        }
    });
}
//城市全选
$(".widthhundred ").on("click",".chkmodule > span:nth-child(2)",function(){
    //进行判断
    if ($(this).hasClass("bottomarrow")) {

        //每次点击要清空
        $(".chkmodule > span:nth-child(1)").removeClass("bgfffbc9");
        $(".chkmodule > span:nth-child(2)").removeClass("toparrow").addClass("bottomarrow");
        $(".cityallnm").hide();
        //end

        $(this).removeClass("bottomarrow").addClass("toparrow");
        $(this).prev().addClass("bgfffbc9");
        $(this).parent().find(".cityallnm").show();
    }else {
        $(this).removeClass("toparrow").addClass("bottomarrow");
        $(this).prev().removeClass("bgfffbc9");
        $(this).parent().find(".cityallnm").hide();
    }
});
//点击关闭
$(".widthhundred ").on("click",".chkmodule .colsedistrict",function(){
    $(this).parent().parent().find(" > span:nth-child(2)").removeClass("toparrow").addClass("bottomarrow");
    $(this).parent().parent().find(" > span:nth-child(1)").removeClass("bgfffbc9");
    $(this).parent().hide();
});
//checkbox 绑定全选事件
$(".widthhundred").on("change",".chkmodule [name = checkboxall]:checkbox",function(){
    if($(this).is(":checked")){
        $(this).parent().find("input").removeClass("searchallchk");
        var countchk = 0;
        $(this).parent().parent().find(".cityallnm  [name = cekItem]:checkbox").each(function(){
            countchk++;
            if($(this).prop("disabled")==false){
                $(this).prop("checked",true);
            }
        });
        if(countchk > 0){
            $(this).parent().next().next().show().find("i").text("("+countchk+")");
        }else{
            $(this).parent().next().next().hide().find("i").text("");
        }
    }else{
        var countchk1 = 0;
        $(this).parent().parent().find(".cityallnm  [name = cekItem]:checkbox").each(function(){
            if($(this).prop("disabled")==false){
                $(this).prop("checked",false);
            }else{
                countchk1++;
            }
        });
        if(countchk1 > 0){
            $(this).parent().next().next().show().find("i").text("("+countchk1+")");
        }else{
            $(this).parent().next().next().hide().find("i").text("");
        }
    }
});
//每个复选框添加change事件
$(".widthhundred").on("change","[name = cekItem]:checkbox",function(){
    //当选中所有复选框  选中总复选框元素也设置它选中
    var allchecked = true;
    var flgtrue = 0;
    var flgfalse = 0;
    $(this).parent().parent().find("[name = cekItem]:checkbox").each(function(){
        if(!$(this).is(":checked")){
            flgtrue++;
            allchecked = false;
        }else{
            flgfalse++;
        }
    });
    if(allchecked==true){
        $(this).parent().parent().parent().find("[name = checkboxall]:checkbox").removeClass("searchallchk");
        $(this).parent().parent().parent().find("[name = checkboxall]:checkbox").prop("checked", true);
    }else if(flgtrue > 0 && flgfalse > 0){
        $(this).parent().parent().parent().find("[name = checkboxall]:checkbox").addClass("searchallchk");
        $(this).parent().parent().parent().find("[name = checkboxall]:checkbox").prop("checked", false);
    }
    //显示选中
    // 的数量
    if(flgfalse > 0){
        $(this).parent().parent().prev().show().find("i").text("("+flgfalse+")");
    }else{
        $(this).parent().parent().prev().hide().find("i").text("");
    }
});
//运费模板--------------------------------------------------------------------------------------------
$("#addcarrier").click(function(){

    $("#carrierlist tbody").append("<tr> <td><span>未添加地区</span></td> <td><span class='carspancolor errieditor'>编辑</span></td>"
        +"<td><input type='text' onkeyup='javascript:CheckInputIntFloat(this);' style='width:55px;height:22px;'/></td>"
        +"<td><input type='text' onkeyup='javascript:CheckInputIntFloat(this);' style='width:55px;height:22px;'/></td>"
        +"<td><input type='text' onkeyup='javascript:CheckInputIntFloat(this);' style='width:55px;height:22px;'/></td>"
        +"<td><input type='text' onkeyup='javascript:CheckInputIntFloat(this);' style='width:55px;height:22px;'/></td>"
        +"<td><input type='text' onkeyup='javascript:CheckInputIntFloat(this);' style='width:55px;height:22px;'/></td>"

        +"<td style='display:none;'></td>"
        +"<td style='display:none;'></td>"
        +"<td style='display:none;'>"+uuid()+"</td>"
        +"<td style='display:none;'></td>"

        +"<td><span class='carspancolor erridelete'>删除</span></td>"
        +"</tr>");
});
//编辑事件-------------------------------------------------------------------------------
$("#carrierlist").on('click','.errieditor',function(){
    //设置要操作的行标识
    $('#freightUuid').val($(this).parent().parent().children().eq(9).text());

    //每次调用都要去除disabled
    $("#freighttemplate [name = checkboxall]:checkbox").each(function(){
        $(this).prop("checked",false);
        $(this).prop("disabled",false);
    });
    $("#freighttemplate [name = cekItem]:checkbox").each(function(){
        $(this).prop("checked",false);
        $(this).prop("disabled",false);
    });
    $("#freighttemplate .countdistrict").each(function(){
        $(this).hide().find("i").text("");
    });
    //清空 end
    //------------------------------------------------------------------------------
    var currentlist = new Array();debugger;
   // var currentnm = $.trim($(this).parent().prev().text()).split(",");//字符分割;
    var currentnm = $.trim($(this).parent().parent().find("td").eq(10).text()).split(",");
    if(currentnm.length > 0){
        for(var i=0;i<currentnm.length;i++)
        {
            currentlist.push(currentnm[i]);
        }
    }else{
       // currentlist.push($.trim($(this).parent().prev().text()));
        currentlist.push($.trim($(this).parent().parent().find("td").eq(10).text()));
    }
    $("#freighttemplate").show();
    var citynm=[];
    var strtxt;
    $("#freightTb tr td:nth-child(1)").each(function(){
        //strtxt = $.trim($(this).find("span").text());
         strtxt = $.trim($(this).parent().find("td").eq(10).text());
        if(strtxt!="未添加地区"){
            var strlist = strtxt.split(",");//字符分割
            if(strlist.length > 0){
                for(var i=0;i<strlist.length ;i++)
                {
                    citynm.push(strlist[i]);
                }
            }else{
                citynm.push(strtxt);
            }
        }
    });
    var clist=[];
    if(citynm.length > 0){
        //先进行一次筛选 选中
        for(var i=0; i<citynm.length;i++){
            $("#freighttemplate .chkmodule > span:nth-child(1)").each(function(){
                //var str = $(this).text();
                var str = $(this).find("[name = checkboxall]:checkbox").val();
                if($.trim(str)==citynm[i]){
                    //父集
                    $(this).find("[name = checkboxall]:checkbox").each(function(){
                        $(this).prop("checked", true);
                        //禁闭掉子集
                        $(this).parent().parent().find(".cityallnm [name = cekItem]:checkbox").each(function(){
                            $(this).prop("checked",true);
                        });
                    });
                }
            });
            $("#freighttemplate .chkmodule .cityallnm > span").each(function(){
               // var str1 = $(this).text();
                 var str1 = $(this).find("[name = cekItem]:checkbox").val();
                if($.trim(str1)==citynm[i]){
                    //子集
                    $(this).find("[name = cekItem]:checkbox").each(function(){
                        $(this).prop("checked", true);
                    });
                }
            });
        }
        ///显示对应的模块当前选中的个数
        $("#freighttemplate .chkmodule").each(function(){
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
                // console.log("ok"+count);
                $(this).find(">span [name = checkboxall]:checkbox").addClass("searchallchk");
            }
        });
        //禁闭掉不属于当前的城市
        for(var i=0;i<citynm.length;i++){
            var flg = $.inArray(citynm[i],currentlist);
            if(flg==-1){
                clist.push(citynm[i]);
            }
        }
        //禁闭掉相应的复选框
        if(clist.length > 0){
            for(var i=0;i<clist.length;i++){
                $("#freighttemplate .chkmodule > span:nth-child(1)").each(function(){
                   // var str = $(this).text();
                    var str = $(this).find("[name = checkboxall]:checkbox").val();
                    if($.trim(str)==clist[i]){
                        //父集
                        $(this).find("[name = checkboxall]:checkbox").each(function(){
                            $(this).prop("disabled",true);
                            //禁闭掉子集
                            $(this).parent().parent().find(".cityallnm [name = cekItem]:checkbox").each(function(){
                                $(this).prop("disabled",true);
                            });
                        });
                    }
                });
                $("#freighttemplate .chkmodule .cityallnm > span").each(function(){
                    //var str1 = $(this).text();
                    var str1 =  $(this).find("[name = cekItem]:checkbox").val();
                    if($.trim(str1)==clist[i]){
                        //子集
                        $(this).find("[name = cekItem]:checkbox").each(function(){
                            $(this).prop("disabled",true);
                        });
                    }
                });


            }
        }
    }
}).on('click','.erridelete',function(){
    //写删除事件
    $(this).parent().parent().remove();
});
//取消
$('.btncencle').click(function(){
    $("#freighttemplate").hide();
});

