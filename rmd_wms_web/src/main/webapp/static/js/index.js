/**
 * Created by win7 on 2016/9/27.
 */
$(function(){
    //leftscroll
    $(".nav_bar").slimScroll({
        height: "100%", railOpacity: .9, alwaysVisible: !1
    });
    $(".nav_left>li>a").click(function(){
        $(this).parent().addClass("active").siblings().removeClass("active");
    });
    //header
    $(".nav_header").hover(function(){
        $(".downBox").css("display","block");
    },function(){
        $(".downBox").css("display","none");
    });
    $(".nav_left>li:last-child a").css("padding-bottom","20px");
    
    $('#wareId').find("option[value='"+$('#wareIdEx').val()+"']").attr("selected", "selected");
    $('#wareId').change(function(){
        var parames = new Array();
        parames.push({ name: "wareId", value: $('#wareId').val()});
        Post('./checkLogin',parames);

    });
    //仓库信息为空则隐藏
    var wareIdEx=$('#wareIdEx').val();
    if(wareIdEx==''||wareIdEx.length<=0){
        $('#wareId').hide();
    }
});
//leftmenuslider
$(function() {
    var Accordion = function(el, multiple) {
        this.el = el || {};
        this.multiple = multiple || false;
        // Variables privadas
        var links = this.el.find('.link');
        // Evento
        links.on('click', {el: this.el, multiple: this.multiple}, this.dropdown)
    }
    Accordion.prototype.dropdown = function(e) {
        var $el = e.data.el,
            $this = $(this),
            $next = $this.next();
        $next.slideToggle();
        $this.parent().toggleClass('active');
        if (!e.data.multiple) {
            $el.find('.nav_left').not($next).slideUp().parent().removeClass('active');
        };
    }
    var accordion = new Accordion($('.menuN'), false);
});

/*
 *功能： 模拟form表单的提交
 *参数： URL 跳转地址 PARAMTERS 参数
 *返回值：
 *创建人：
 */
function Post(URL, PARAMTERS) {
    //创建form表单
    var temp_form = document.createElement("form");
    temp_form.action = URL;
    //如需打开新窗口，form的target属性要设置为'_blank'
    temp_form.target = "_self";
    temp_form.method = "post";
    temp_form.style.display = "none";
    //添加参数
    for (var item in PARAMTERS) {
        var opt = document.createElement("textarea");
        opt.name = PARAMTERS[item].name;
        opt.value = PARAMTERS[item].value;
        temp_form.appendChild(opt);
    }
    document.body.appendChild(temp_form);
    //提交数据
    temp_form.submit();
}