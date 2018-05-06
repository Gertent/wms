/**
 * 扩展easyui表单的验证
 */
//@ sourceURL=validator.js
$.extend($.fn.validatebox.defaults.rules, {
    //用户账号验证(6-10位数字、字母或字符"-"、"_"组合) 
	loginname: {
        validator: function (value) {
        	return new RegExp(validateRegExp.userPassword).test(value)?true:false;
        },
        message: '请输入6-10位数字、字母或字符"-"、"_"组合'
    },
    
    //系统用户密码验证(6-10位数字、字母或字符"-"、"_"组合) 
    password: {
        validator: function (value) {
        	return new RegExp(validateRegExp.userPassword).test(value)?true:false;
        },
        message: '请输入6-10位数字、字母或字符"-"、"_"组合'
    },
    
    //移动手机号码验证
    mobile: {
        validator: function (value) {
            return new RegExp(validateRegExp.mobile).test(value)?true:false;
        },
        message: '请输入正确的手机号码！'
    },
    range:{
		validator:function(value,param){
			if(/^0|[1-9]\d*$/.test(value)){
				return value >= param[0] && value <= param[1];
			}else{
				return false;
			}
		},
		message:'输入的数字在{0}到{99999}之间'
	},
	//不能输入全空格
	space:{
		validator:function(value){
			var val = value.replace(/(^\s*)|(\s*$)/g, "");
			if(val == ""){
				return false;
			}else{
				return val;
			}
		},
		message:'不能输入全空格'
	},
	//只能输入数字和小数(整数开头)
	numberDecimals:{
		validator:function(value){
			return new RegExp(validateRegExp.fullNumberDecimals).test(value)?true:false;
		},
		message:'请输入数字'
	},
	//会员密码验证(请输入长度为6-20位数字、字母或符号至少两种组合) 
    memberPassword: {
        validator: function (value) {
        	return new RegExp(validateRegExp.memberPassword).test(value)?true:false;
        },
        message: '请输入6-20位字母、数字和符号中的至少两种组合'
    },
    //手机号和固定电话验证 
    telOrMobile: {
        validator: function (value) {
        	return new RegExp(validateRegExp.telOrMobile).test(value)?true:false;
        },
        message: '请输入正确的联系电话'
    },
})