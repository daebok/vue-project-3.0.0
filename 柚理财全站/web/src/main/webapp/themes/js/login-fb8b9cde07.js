webpackJsonp([33],{0:function(e,t,a){"use strict";function r(e){return e&&e.__esModule?e:{"default":e}}function i(){return!1}function o(e){u["default"].error({title:e,okText:"\u5173\u95ed",wrapClassName:"tiperror"})}var n=a(9),l=r(n),s=a(14),c=r(s),d=a(3),u=r(d),f=a(10),m=r(f),g=Object.assign||function(e){for(var t=1;t<arguments.length;t++){var a=arguments[t];for(var r in a)Object.prototype.hasOwnProperty.call(a,r)&&(e[r]=a[r])}return e};a(8);var h=a(2),p=r(h),w=a(1),E=r(w);a(573);var v=(a(5),a(7)),N=m["default"].create,y=m["default"].Item,b=E["default"].createClass({displayName:"Loginform",handleSubmit:function(e){console.log(222222,22222);var t=$("#isRsa").val();if(1==t){var a=new JSEncrypt,r=$("#pubKey").val();a.setPublicKey(r);var i=a.encrypt($("#loginPwd").val())}var n=this;e.preventDefault(),this.props.form.validateFields(function(e,a){return e?void console.log("Errors in form!!!"):(console.log("Submit!!!"),1==t&&(a.loginPwd=i),console.log(a),void $.ajax({url:"/user/doLogin.html",type:"POST",dataType:"json",data:a}).done(function(e){e.result?e.pwdResetFlag?window.location.href="/user/pwdReset.html":n.props.url?window.location.href=n.props.url:window.location.href="/member/account/main.html":(n.props.form.resetFields(["validCode"]),$(".codeimg")[0].click(),e.showCaptcha&&(n.setState({ischeck:!0}),n.refs.imgcoderow.className="row show",n.changeimg()),o(e.msg))}).fail(function(){o("\u7f51\u7edc\u5f02\u5e38\uff0c\u8bf7\u91cd\u8bd5\uff01")}))})},getInitialState:function(){return{imgsrc:"/validimg.html",ischeck:!1,imgcodeerrorbg:!1}},changeimg:function(){this.setState({imgsrc:"/validimg.html?t="+Math.random()})},isloginName:function(e,t,a){t&&v.Reg.islogname(t)?a([new Error("\u62b1\u6b49\uff0c\u8bf7\u8f93\u5165\u6b63\u786e\u7684\u7528\u6237\u540d/\u624b\u673a\u53f7\u7801/\u7535\u5b50\u90ae\u7bb1")]):a()},checkPass:function(e,t,a){this.props.form.validateFields;/\s/.test(t)&&a([new Error("\u5bc6\u7801\u4e0d\u80fd\u5305\u542b\u7a7a\u683c")]),a()},check:function(e){var t=e.keyCode||e.which,a=e.shiftKey||16==t||!1;t>=65&&t<=90&&!a||t>=97&&t<=122&&a?$("#j-capsLock").show():$("#j-capsLock").hide()},close:function(){$("#j-capsLock").hide()},getInfo:function(){var e=this;$.ajax({url:"/user/showCaptcha.html",type:"POST",dataType:"json"}).done(function(t){t.result&&(e.setState({ischeck:!0}),e.refs.imgcoderow.className="row show")}).fail(function(){})},componentWillMount:function(){this.getInfo()},render:function(){var e=1==$("#isRsa").val()?"1":"0",t=1==$("#encrypt").val()?"1":"0",a=this.props.form,r=a.getFieldProps,o=(a.getFieldError,a.isFieldValidating,r("loginName",{validate:[{rules:[{required:!0,message:"\u8bf7\u586b\u5199\u7528\u6237\u540d/\u624b\u673a\u53f7\u7801/\u7535\u5b50\u90ae\u7bb1"},{validator:this.isloginName}],trigger:["onBlur"]}]})),n=r("loginPwd",{validate:[{rules:[{required:!0,whitespace:!0,message:"\u8bf7\u586b\u5199\u5bc6\u7801"}],trigger:["onBlur"]},{rules:[{validator:this.checkPass}],trigger:["onBlur"]}]}),s=r("validCode",{validate:[{rules:[{required:this.state.ischeck,whitespace:!0,message:"\u8bf7\u586b\u5199\u9a8c\u8bc1\u7801"}],trigger:["onBlur"]}]}),d={wrapperCol:{span:20,offset:2}},u=r("redirectURL",{initialValue:""}),f=r("isRsa",{initialValue:e}),h=r("encrypt",{initialValue:t});return E["default"].createElement(m["default"],{horizontal:!0,className:"form-themes",form:this.props.form,onSubmit:this.handleSubmit},E["default"].createElement("div",{className:"login-title"},"\u767b\u5f55"),E["default"].createElement(y,g({},d,{hasFeedback:!0}),E["default"].createElement(c["default"],g({},o,{maxLength:"50",placeholder:"\u7528\u6237\u540d/\u624b\u673a\u53f7\u7801/\u7535\u5b50\u90ae\u7bb1"}))),E["default"].createElement(y,g({},d,{hasFeedback:!0}),E["default"].createElement(c["default"],g({},n,{placeholder:"\u767b\u5f55\u5bc6\u7801",maxLength:"16",type:"password",autoComplete:"off",onContextMenu:i,onPaste:i,onCopy:i,onCut:i,onKeyPress:this.check,onBlur:this.close,size:"large"}))),E["default"].createElement("div",{id:"j-capsLock",className:"ant-tooltip  ant-tooltip-placement-top  ant-tooltip-hidden"},E["default"].createElement("div",{className:"ant-tooltip-content"},E["default"].createElement("div",{className:"ant-tooltip-arrow"}),E["default"].createElement("div",{className:"ant-tooltip-inner"},"\u5927\u5199\u5df2\u6253\u5f00"))),E["default"].createElement("div",{className:"row hide",ref:"imgcoderow"},E["default"].createElement("div",{className:"col-10 col-offset-2"},E["default"].createElement(y,{hasFeedback:!0},E["default"].createElement(c["default"],g({},s,{placeholder:"\u9a8c\u8bc1\u7801",maxLength:"4",size:"large",type:"text",autoComplete:"off"})))),E["default"].createElement("div",{className:"col-8 col-offset-4"},E["default"].createElement("img",{onClick:this.changeimg,src:this.state.imgsrc,className:"ant-form-text codeimg"}))),E["default"].createElement(c["default"],g({},u,{type:"hidden"})),E["default"].createElement(c["default"],g({},f,{type:"hidden"})),E["default"].createElement(c["default"],g({},h,{type:"hidden"})),E["default"].createElement("div",{className:"ant-form-item forget-reg-link"},E["default"].createElement("div",{className:"col-20 col-offset-2 forget-reg-link-cont"},E["default"].createElement("a",{href:"/user/retrievepwd/index.html"},"\u5fd8\u8bb0\u5bc6\u7801?"),E["default"].createElement("a",{href:"/user/register.html",className:"fr"},"\u514d\u8d39\u6ce8\u518c"))),E["default"].createElement(y,{wrapperCol:{span:20,offset:2}},E["default"].createElement(l["default"],{type:"primary",htmlType:"submit"},"\u7acb\u5373\u767b\u5f55")))}});b=N()(b),p["default"].render(E["default"].createElement(b,{url:$(".login-body").data("url")}),document.getElementById("loginbox"))},5:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0}),t.ValidateFn=void 0;var r=a(7),i={regexRealName:function(e,t,a){t?r.Reg.regexRealName(t)?a():a([new Error("\u7528\u6237\u540d\u683c\u5f0f\u4e0d\u6b63\u786e")]):a()},regexChnName:function(e,t,a){t?r.Reg.regexChnName(t)?a():a([new Error("\u4ec5\u4e3a\u4e2d\u6587,\u4e14\u4e0d\u5305\u542b\u7a7a\u683c")]):a()},regexUserName:function(e,t,a){t&&r.Reg.regexUserName(t)?a([new Error("6-20\u4f4d\u5b57\u7b26\uff0c\u7531\u5b57\u6bcd\u6216\u5b57\u6bcd\u52a0\u6570\u5b57\u7ec4\u6210")]):a()},regexUserNameAjax:function(e,t,a){if(t)if(/^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}|[a-zA-Z]{6,20}$/.test(t)){var r=this;if(this.state.regexUserNameflag==t)return void a();$.ajax({url:"/user/checkUserName.html",type:"POST",dataType:"json",data:{userName:t}}).done(function(e){e.result?(r.state.regexUserNameflag=t,a()):a([new Error(e.msg)])}).fail(function(){a([new Error("\u7f51\u7edc\u5f02\u5e38\uff0c\u8bf7\u91cd\u8bd5")])})}else a([new Error("6-20\u4f4d\u5b57\u7b26\uff0c\u7531\u5b57\u6bcd\u6216\u5b57\u6bcd\u52a0\u6570\u5b57\u7ec4\u6210")]);else a()},regexIdCard:function(e,t,a){t?r.Reg.regexIdCard(t)?a():a([new Error("\u8eab\u4efd\u8bc1\u683c\u5f0f\u4e0d\u6b63\u786e,\u4ec5\u9650\u6570\u5b57\u548c\u5927\u5199X")]):a()},regexPW:function(e,t,a){t&&r.Reg.regexPassword(t)?a([new Error("8-16\u4f4d\u5b57\u7b26\uff0c\u5176\u4e2d\u5305\u62ec\u81f3\u5c11\u4e00\u4e2a\u5b57\u6bcd\u548c\u4e00\u4e2a\u6570\u5b57")]):a()},isMobile:function(e,t,a){t&&r.Reg.isMobile(t)?a([new Error("\u62b1\u6b49\uff0c\u8bf7\u8f93\u5165\u6b63\u786e\u7684\u624b\u673a\u53f7\u7801")]):a()},isNumber:function(e,t,a){t&&r.Reg.isNumber(t)?a([new Error("\u62b1\u6b49\uff0c\u8bf7\u8f93\u5165\u6b63\u6574\u6570")]):a()},isApr:function(e,t,a){t&&r.Reg.isApr(t)?a([new Error("\u62b1\u6b49\uff0c\u8bf7\u8f93\u5165\u6b63\u786e\u7684\u6570\u5b57")]):a()},decimal:function(e,t,a){t&&r.Reg.isFigure(t)?a([new Error("\u62b1\u6b49\uff0c\u8bf7\u8f93\u5165\u6b63\u786e\u7684\u6570\u5b57")]):a()},isFigure:function(e,t,a){t&&(r.Reg.isFigure(t)||15!=t.length)?a([new Error("\u62b1\u6b49\uff0c\u8bf7\u8f93\u516515\u4f4d\u6570\u5b57")]):a()},isEmail:function(e,t,a){t&&r.Reg.isEmail(t)?a([new Error("\u62b1\u6b49\uff0c\u8bf7\u8f93\u5165\u6b63\u786e\u7684\u90ae\u7bb1")]):a()},isFixNumber:function(e,t,a){t&&(r.Reg.isFixNumber(t)||r.Reg.isFigure(t)||t.length>12||r.Reg.isPhone(t))?a([new Error("\u62b1\u6b49\uff0c\u8bf7\u8f93\u5165\u6b63\u786e\u7684\u5ea7\u673a\u53f7\u7801")]):a()},isloginName:function(e,t,a){t&&r.Reg.islogname(t)?a([new Error("\u62b1\u6b49\uff0c\u8bf7\u8f93\u5165\u6b63\u786e\u7684\u624b\u673a\u53f7\u7801\u6216\u90ae\u7bb1")]):a()},checkMobile:function(e,t,a){if(t)if(r.Reg.isMobile(t))a([new Error("\u62b1\u6b49\uff0c\u8bf7\u8f93\u5165\u6b63\u786e\u7684\u624b\u673a\u53f7\u7801")]);else{var i=this;if(this.state.mobileflag==t)return void a();$.ajax({url:"/user/checkMobile.html",type:"POST",dataType:"json",data:{mobile:t}}).done(function(e){e.result?(i.setState({mobileflag:t}),a()):a([new Error(e.msg)])}).fail(function(){a([new Error("\u7f51\u7edc\u5f02\u5e38\uff0c\u8bf7\u91cd\u8bd5")])})}else a()},checkValidCode:function(e,t,a){if(t){var r=this;if(this.state.ValidCodeflag==t)return void a();$.ajax({url:"/valicode.html",type:"POST",dataType:"json",data:{validCode:t}}).done(function(e){e.result?(r.setState({ValidCodeflag:t}),a()):(a([new Error(e.msg)]),$(".codeimg")[0].click())}).fail(function(){a([new Error("\u7f51\u7edc\u5f02\u5e38\uff0c\u8bf7\u91cd\u8bd5")])})}else a()}};t.ValidateFn=i},573:534});