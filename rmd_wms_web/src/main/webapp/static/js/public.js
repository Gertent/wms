/**
 * Created by win7 on 2016/10/19.
 */
//tab切换
$('.cenList .list li').click(function () {
    var index = $(this).index();
    $(this).addClass("active").siblings().removeClass("active");
});
