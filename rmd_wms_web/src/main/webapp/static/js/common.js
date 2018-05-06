/*
 * 正则表达式
 */
var validateRegExp = {
	special:"[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？+-\s]",
	chinese:"^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$",	
	email: "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$", //邮件
	mobile: "^0?(13|15|18|14|17)[0-9]{9}$", //手机
    notempty: "^\\S+$", //非空
   // password: "^.*[A-Za-z0-9\\w_-]+.*$", //密码
    fullNumber: "^[0-9]+$", //数字
    tel: "^[0-9\-()（）]{7,18}$", //电话号码
    url: "^http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-./?%&=]*)?$", //只匹配带有HTTP的url
    username: "^[A-Za-z0-9_\\-\\u4e00-\\u9fa5]+$", //用户名
    deptname: "^[A-Za-z0-9_()（）\\-\\u4e00-\\u9fa5]+$", //单位名
    companyaddr: "^[A-Za-z0-9_()（）\\#\\-\\u4e00-\\u9fa5]+$",
    companysite: "^http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-./?%&#=]*)?$",
    account:"^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+|[A-Za-z0-9_\\-\\u4e00-\\u9fa5]+$",
    zipcode: "^\\d{6}$", //邮编
    blank:"^\S+$",//空格
    telOrMobile:"^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$|^13[0-9]{9}|15[012356789][0-9]{8}|18[0256789][0-9]{8}|147[0-9]{8}$",
    //loginname: "^[a-zA-Z]w{5,9}$", //用户名
//    loginname:"^(?![0-9]+$)(?![a-zA-Z]+$)(?![^\da-zA-Z]+$).{6,10}$",//"^[A-Za-z]{1,}[0-9_-]{1,}|[0-9]{1,}[a-zA-Z_-]{1,}$|[0-9]{1,}[a-zA-Z_-]{1,}|[0-9]{1,}[a-zA-Z_-][a-zA-Z0-9_-]{1,}$|[A-Za-z]{1,}[0-9_-][a-zA-Z0-9_-]{1,}$", //由非空格字符组成的字符串，数字，大写字母，小写字母，特殊字符至少有 两种
    loginname:"^[a-zA-Z0-9-_][a-zA-Z0-9_-]{2,9}$",//"^[A-Za-z]{1,}[0-9_-]{1,}|[0-9]{1,}[a-zA-Z_-]{1,}$|[0-9]{1,}[a-zA-Z_-]{1,}|[0-9]{1,}[a-zA-Z_-][a-zA-Z0-9_-]{1,}$|[A-Za-z]{1,}[0-9_-][a-zA-Z0-9_-]{1,}$", //由非空格字符组成的字符串，数字，大写字母，小写字母，特殊字符至少有 两种
    loginname1:"^[0-9]{2,10}$",
    loginname2:"^[a-zA-Z]{2,10}$",
    loginname3:"^[-_]{2,10}$",
//    password:"^\\d+[a-zA-Z]+|[a-zA-Z_-]+\\w+$",//"^[A-Za-z]{1,}[0-9_-]{1,}|[0-9]{1,}[a-zA-Z_-]{1,}$|[0-9]{1,}[a-zA-Z_-]{1,}|[0-9]{1,}[a-zA-Z_-][a-zA-Z0-9_-]{1,}|[A-Za-z]{1,}[0-9_-][a-zA-Z0-9_-]{1,}$", //由非空格字符组成的字符串，数字，大写字母，小写字母，特殊字符至少有 两种
//    password2:"^[a-zA-Z]{2,16}$",
    password:"^[a-zA-Z0-9-_][a-zA-Z0-9_-]{2,15}$",//"^[A-Za-z]{1,}[0-9_-]{1,}|[0-9]{1,}[a-zA-Z_-]{1,}$|[0-9]{1,}[a-zA-Z_-]{1,}|[0-9]{1,}[a-zA-Z_-][a-zA-Z0-9_-]{1,}$|[A-Za-z]{1,}[0-9_-][a-zA-Z0-9_-]{1,}$", //由非空格字符组成的字符串，数字，大写字母，小写字母，特殊字符至少有 两种
    password2:"^[0-9]{2,16}$",
    password3:"^[a-zA-Z]{2,16}$",
    password4:"^[-_]{2,16}$",
    consignee : "^[\u4e00-\u9fa5]{1,15}$", //结算页新增收货地址中收货人的效验
    detailAddress : "^[a-zA-Z0-9\-\u4e00-\u9fa5]+$", //结算页新增收货地址中详细地址的效验
    buyNumber : "^[1-9][0-9]{0,3}$",//快速下单和购物车购买数量验证
    buyNumberDeil : "^[0-9][0-9]{0,3}$",//详情页购买数量验证
    //^1[3|4|5|7|8]\d{9}$	
    urlRegex : "^(((ht|f)tp(s?))\://)?(www.|[a-zA-Z].)[a-zA-Z0-9\-\.]+\.(com|edu|gov|mil|net|org|biz|info|name|museum|us|ca|uk)(\:[0-9]+)*(/($|[a-zA-Z0-9\.\,\;\?\'\\\+&amp;%\$#\=~_\-]+))*$",//匹配http,www的url
    faxes : "((^\\d{11}$)|(^\\d{12}$))|(^\\d{3}-\\d{8}$)|(^\\d{4}-\\d{7}$)|(^\\d{4}-\\d{8}$)",//传真
    fullNumberDecimals : "(^[1-9]([0-9]+)?(\.[0-9]{1,6})?$)|(^(0){1}$)|(^[0-9]\.[0-9]{1,6}?$)",////只能输入数字和小数(整数开头)
    memberPassword: "^(?![\d]+$)(?![a-zA-Z]+$)(?![^\da-zA-Z]+$).{6,20}$", //会员密码
    userPassword: "^(?![\d]+$)(?![a-zA-Z]+$)(?![^\da-zA-Z]+$).{6,10}$" //员工密码
};

/*
 * 字符串操作StringBuilder
 */
(function(A) {
    A.StringBuilder = function() {
        var C = this;
        var B = [];
        this.append = function(D) {//追加到数组中
            B[B.length] = D;
        };
        this.appendLine = function(D) {//追加到数组中，并且每条数据后加换行
            C.append(D);
            C.append("\n");
        };
        this.clear = function() {//清空数组
            B = [];
        };
        this.isEmpty = function() {//判断数组是否为空，返回值true或false
            return B.length === 0
        };
        this.toString = function() {//将数组转换为字符串
            return B.join("");
        };
    };
})(jQuery);

/*
 * 异步调用
 * JsCaller("xx.jsp",{a:"a",b:"b"},function(json) {})
 */
function JsCaller(url,data,callfun,async) {
	if(async==undefined) {
		$.ajax({
			url:url,
			data:data,
			type:"POST",
			dataType:"json",
			success:function(response) {
				callfun(response);
			},
			error:function() {
				//alert("错误");
			}
		});
	} else {
		$.ajax({
			url:url,
			data:data,
			type:"POST",
			dataType:"json",
			async:async,
			success:function(response) {
				callfun(response);
			},
			error:function() {
				//alert("错误");
			}
		});
	}
	
};


/*
* 获取url参数
* 例如：www.xx.com?cc=11111&dd=2222222
* 用法：getParam("cc"),输出11111
*/
function getParam(paramName)
{
        paramValue = "";
        isFound = false;
        if (this.location.search.indexOf("?") == 0 && this.location.search.indexOf("=")>1)
        {
            arrSource = unescape(this.location.search).substring(1,this.location.search.length).split("&");
            i = 0;
            while (i < arrSource.length && !isFound)
            {
                if (arrSource[i].indexOf("=") > 0)
                {
                     if (arrSource[i].split("=")[0].toLowerCase()==paramName.toLowerCase())
                     {
                        paramValue = arrSource[i].split("=")[1];
                        isFound = true;
                     }
                }
                i++;
            }   
        }
   return paramValue;
};


/*
 * 将时间戳转换成日期格式
 */
function getLocalTime(timeStamp) {     
	return new Date(parseInt(timeStamp) * 1000).toLocaleString();     
};


/*
 * 格式化金额
 */
function formatNumber(num, pattern) {
    var strarr = num ? num.toString().split('.') : ['0'];
    var fmtarr = pattern ? pattern.split('.') : [''];
    var retstr = '';

    // 整数部分  
    var str = strarr[0];
    var fmt = fmtarr[0];
    var i = str.length - 1;
    var comma = false;
    for (var f = fmt.length - 1; f >= 0; f--) {
        switch (fmt.substr(f, 1)) {
            case '#':
                if (i >= 0) retstr = str.substr(i--, 1) + retstr;
                break;
            case '0':
                if (i >= 0) retstr = str.substr(i--, 1) + retstr;
                else retstr = '0' + retstr;
                break;
            case ',':
                comma = true;
                retstr = ',' + retstr;
                break;
        }
    }
    if (i >= 0) {
        if (comma) {
            var l = str.length;
            for (; i >= 0; i--) {
                retstr = str.substr(i, 1) + retstr;
                if (i > 0 && ((l - i) % 3) == 0) retstr = ',' + retstr;
            }
        }
        else retstr = str.substr(0, i + 1) + retstr;
    }

    retstr = retstr + '.';
    // 处理小数部分  
    str = strarr.length > 1 ? strarr[1] : '';
    fmt = fmtarr.length > 1 ? fmtarr[1] : '';
    i = 0;
    for (var f = 0; f < fmt.length; f++) {
        switch (fmt.substr(f, 1)) {
            case '#':
                if (i < str.length) retstr += str.substr(i++, 1);
                break;
            case '0':
                if (i < str.length) retstr += str.substr(i++, 1);
                else retstr += '0';
                break;
        }
    }
    return retstr.replace(/^,+/, '').replace(/\.$/, '').replace(/\-\,/,"-");
}
/**
 * 格式化价格
 * @param price
 * @returns {Number}
 */
function formatPrice(price) {
	  var num = String(price).split(".");
	  var tmp = 0;
	  if(num[1]) {
	    var i = num[1].substr(2,1);
	    if(i>0) {
	      tmp = (price+0.005).toFixed(2);
	    } else {
	      tmp = price.toFixed(2);
	    }
	  } else {
	    tmp = price.toFixed(2);
	  }
	  return tmp;
	}

/* 
* 类型转换 
*/ 
function FloatMul(arg1,arg2) { 
	var m=0,s1=arg1.toString(),s2=arg2.toString(); 
	try{m+=s1.split(".")[1].length;}catch(e){} 
	try{m+=s2.split(".")[1].length;}catch(e){} 
	return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m); 
}
   
/**
 * 保留2位小数,不够补上.00
 */
function toDecimal2(x) {   
    var f = parseFloat(x);    
    if (isNaN(f)) {    
        return false;    
    }    
    var f = Math.round(x*100)/100;    
    var s = f.toString();    
    var rs = s.indexOf('.');    
    if (rs < 0) {    
        rs = s.length;    
        s += '.';    
    }    
    while (s.length <= rs + 2) {    
        s += '0';    
    }    
    return s;    
}  

/*
 * 字符串转json
 * function strToJson(str){  
    var json = (new Function("return " + str))();  
    return json;  
}  
 */

//返回顶部
$(document).ready(function(){
	$("#back_top").click(function(){
    	$('html,body').animate({scrollTop:0}, 300);
    });
});

//格式化日期
function formatterdate(val, row) {
	if (val != null) {
	var date = new Date(val);
	return date.getFullYear() + '-' + (date.getMonth() + 1) + '-'+ date.getDate() + ' '+ date.getHours() + ':' + date.getMinutes() + ':' + date.getSeconds();
	}
}