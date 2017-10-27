/* 
 * description: mod/IePlaceholder.js  适用于ie8下  输入框的placeholder属性不起效
 * version:V2.0.2deposit
 * date: 2015/12/03
 * 
 */

//解决IE不支持HTML5表单属性placeholder的问题
(function ($) {
    $.fn.placeholder = function (options) {
        var defaults = {
            pColor: "#ccc",
            pActive: "#999",
            pFont: "14px",
            activeBorder: "#080",
            posL: 7,
            zIndex: "99",
            oInput:"<label>",
            iH:42
            
        },
        opts = $.extend(defaults, options);
        //
        return this.each(function () {
            if ("placeholder" in document.createElement("input")) return;
            $(this).parent().parent().css("position", "relative");
            var isIE = true,
            version = 8;
            //不支持placeholder的浏览器
            var $this = $(this),
                msg = $this.attr("placeholder"),
                iH = $this.outerHeight(),
                iW = $this.outerWidth(),
                iX = $this.position().left,
                iY = $this.position().top;

            if(opts.iH == 0){
            	iH = $this.outerHeight();
            }else{
            	iH = opts.iH;
            }
             var   oInput = $(opts.oInput, {
	                "class": "placeholderCss",
	                "text": msg,
	                "css": {
	                    "position": "absolute",
	                    "left": iX + "px",
	                    "top": iY + "px",
	                    "width": iW - opts.posL + "px",
	                    "padding-left": opts.posL + "px",
	                    "height": iH + "px",
	                    "line-height": iH + "px",
	                    "color": opts.pColor,
	                    "font-size": opts.pFont,
	                    "z-index": opts.zIndex,
	                    "cursor": "text"
	                }
                }).insertBefore($this);
            //初始状态就有内容
            var value = $this.val();
            if (value.length > 0) {
            	oInput.hide();
            }; 
            //
            $this.on("focus", function () {
                var value = $(this).val();
                if (value.length > 0) {
                    oInput.hide();
                }
                oInput.css("color", opts.pActive);
                //  

                $(this).on('keyup', function () {
                    var value = $(this).val();
                    if (value.length == 0) {
                        oInput.show();
                    } else {
                        oInput.hide();
                    }
                });  
            }).on("blur", function () {
                var value = $(this).val();
                if (value.length == 0) {
                    oInput.css("color", opts.pColor).show();
                }
            });
            //
            oInput.on("click", function () {
                $this.trigger("focus");
                $(this).css("color", opts.pActive)
            });
            //
            $this.filter(":focus").trigger("focus");
        });
    }
})(jQuery)