 
$(function() {
    var count=0;
    $.each(province, function (k, p) {
    	var checkbox="";
        checkbox="<input  type='checkbox'  style='height: 22px;line-height: 22px;' name='province'  value='"+p.ProID+"'/>"+p.name+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
    	$("#PROVINCE").append(checkbox);
    	count++;
    	if(count%3==0){
    		$("#PROVINCE").append("</br>");
    	}
    });

});

//单选按钮
$(".memEdit table td a").click(function(){
    $(this).find("i").addClass("on");
    $(this).siblings().find("i").removeClass("on");
    $(this).siblings().find("input").attr('checked','false');
    $(this).find("input").attr('checked','checked');

});
