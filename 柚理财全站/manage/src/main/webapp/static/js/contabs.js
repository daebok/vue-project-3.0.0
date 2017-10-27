$(function() {
    function p(a) {
        var b = 0;
        $(a).each(function() {
            b += $(this).outerWidth(true)
        });
        return b
    }
    function o(g) {
        var f = p($(g).prevAll()),
            d = p($(g).nextAll());
        var b = p($(".content-tabs").children().not(".J_menuTabs"));
        var c = $(".content-tabs").outerWidth(true) - b;
        var e = 0;
        if ($(".page-tabs-content").outerWidth() < c) {
            e = 0
        } else {
            if (d <= (c - $(g).outerWidth(true) - $(g).next().outerWidth(true))) {
                if ((c - $(g).next().outerWidth(true)) > d) {
                    e = f;
                    var a = g;
                    while ((e - $(a).outerWidth()) > ($(".page-tabs-content").outerWidth() - c)) {
                        e -= $(a).prev().outerWidth();
                        a = $(a).prev()
                    }
                }
            } else {
                if (f > (c - $(g).outerWidth(true) - $(g).prev().outerWidth(true))) {
                    e = f - $(g).prev().outerWidth(true)
                }
            }
        }
        $(".page-tabs-content").animate({
                marginLeft: 0 - e + "px"
            },
            "fast")
    }
    function u() {
        var e = Math.abs(parseInt($(".page-tabs-content").css("margin-left")));
        var b = p($(".content-tabs").children().not(".J_menuTabs"));
        var c = $(".content-tabs").outerWidth(true) - b;
        var d = 0;
        if ($(".page-tabs-content").width() < c) {
            return false
        } else {
            var a = $(".J_menuTab:first");
            var f = 0;
            while ((f + $(a).outerWidth(true)) <= e) {
                f += $(a).outerWidth(true);
                a = $(a).next()
            }
            f = 0;
            if (p($(a).prevAll()) > c) {
                while ((f + $(a).outerWidth(true)) < (c) && a.length > 0) {
                    f += $(a).outerWidth(true);
                    a = $(a).prev()
                }
                d = p($(a).prevAll())
            }
        }
        $(".page-tabs-content").animate({
                marginLeft: 0 - d + "px"
            },
            "fast")
    }
    function t() {
        var e = Math.abs(parseInt($(".page-tabs-content").css("margin-left")));
        var b = p($(".content-tabs").children().not(".J_menuTabs"));
        var c = $(".content-tabs").outerWidth(true) - b;
        var d = 0;
        if ($(".page-tabs-content").width() < c) {
            return false
        } else {
            var a = $(".J_menuTab:first");
            var f = 0;
            while ((f + $(a).outerWidth(true)) <= e) {
                f += $(a).outerWidth(true);
                a = $(a).next()
            }
            f = 0;
            while ((f + $(a).outerWidth(true)) < (c) && a.length > 0) {
                f += $(a).outerWidth(true);
                a = $(a).next()
            }
            d = p($(a).prevAll());
            if (d > 0) {
                $(".page-tabs-content").animate({
                        marginLeft: 0 - d + "px"
                    },
                    "fast")
            }
        }
    }
    $(".J_menuItem").each(function(a) {
        if (!$(this).attr("data-index")) {
            $(this).attr("data-index", a)
        }
    });
    function s() {
        $("#side-menu").find('a').removeClass('a-active');
        $(this).addClass('a-active');
        var e = $(this).attr("data-href"),
            id=$(this).attr("id"),
            a = $(this).data("index"),
            b = $.trim($(this).attr('title')),
            c = true;
        if (e == undefined || $.trim(e).length == 0) {
            return false
        }
        $(".J_menuTab").each(function() {
            if ($(this).data("id") == e) {
                if (!$(this).hasClass("active")) {
                    $(this).addClass("active").siblings(".J_menuTab").removeClass("active");
                    o(this);
                    $(".J_mainContent .J_iframe").each(function() {
                        if ($(this).data("id") == e) {
                            $(this).show().siblings(".J_iframe").hide();
                            return false
                        }
                    })
                } else {
                    $(".J_mainContent .J_iframe").each(function() {
                        if ($(this).data("id") == e) {
                            $(this).attr("src", e);
                            return false
                        }
                    })
                }
                c = false;
                return false
            }
        });
        if (c) {
            var d = '<a href="javascript:;" class="active J_menuTab" data-alink="'+id+'" data-id="' + e + '">' + b + ' <i class="fa fa-times-circle"></i></a>';
            $(".J_menuTab").removeClass("active");
            var f = '<iframe class="J_iframe" name="iframe' + a + '" width="100%" height="100%" src="' + e + '" frameborder="0" data-id="' + e + '" seamless></iframe>';
            $(".J_mainContent").find("iframe.J_iframe").hide().parents(".J_mainContent").append(f);
            $(".J_menuTabs .page-tabs-content").append(d);
            o($(".J_menuTab.active"))
        }
        return false
    }
    $("body").on("click",".J_menuItem", s);

    function m() {
        var a = $(this).parents(".J_menuTab").data("id");
        var b = $(this).parents(".J_menuTab").width();
        if ($(this).parents(".J_menuTab").hasClass("active")) {
            if ($(this).parents(".J_menuTab").next(".J_menuTab").size()) {
                var c = $(this).parents(".J_menuTab").next(".J_menuTab:eq(0)").data("id");
                $(this).parents(".J_menuTab").next(".J_menuTab:eq(0)").addClass("active");
                $(".J_mainContent .J_iframe").each(function() {
                    if ($(this).data("id") == c) {
                        $(this).show().siblings(".J_iframe").hide();
                        return false
                    }
                });
                var d = parseInt($(".page-tabs-content").css("margin-left"));
                if (d < 0) {
                    $(".page-tabs-content").animate({
                            marginLeft: (d + b) + "px"
                        },
                        "fast")
                }
                $(this).parents(".J_menuTab").remove();
                $(".J_mainContent .J_iframe").each(function() {
                    if ($(this).data("id") == a) {
                        $(this).remove();
                        return false
                    }
                })
            }
            if ($(this).parents(".J_menuTab").prev(".J_menuTab").size()) {
                var c = $(this).parents(".J_menuTab").prev(".J_menuTab:last").data("id");
                $(this).parents(".J_menuTab").prev(".J_menuTab:last").addClass("active");
                $(".J_mainContent .J_iframe").each(function() {
                    if ($(this).data("id") == c) {
                        $(this).show().siblings(".J_iframe").hide();
                        return false
                    }
                });
                $(this).parents(".J_menuTab").remove();
                $(".J_mainContent .J_iframe").each(function() {
                    if ($(this).data("id") == a) {
                        $(this).remove();
                        return false
                    }
                })
            }
        } else {
            $(this).parents(".J_menuTab").remove();
            $(".J_mainContent .J_iframe").each(function() {
                if ($(this).data("id") == a) {
                    $(this).remove();
                    return false
                }
            });
            o($(".J_menuTab.active"))
        }
        return false
    }
    $(".J_menuTabs").on("click", ".J_menuTab i", m);

    function l() {
        $(".page-tabs-content").children("[data-id]").not(":first").not(".active").each(function() {
            $('.J_iframe[data-id="' + $(this).data("id") + '"]').remove();
            $(this).remove()
        });
        $(".page-tabs-content").css("margin-left", "0")
    }
    $(".J_tabCloseOther").on("click", l);
    function k() {
        o($(".J_menuTab.active"))
    }
    $(".J_tabShowActive").on("click", k);
    //var n = ["/product/manage/newProductManage.html", "/loan/borrow/borrowAddPage.html"];
    function q() {
        if (!$(this).hasClass("active")) {
            var a = $(this).data("id");
            $(".J_mainContent .J_iframe").each(function() {
                if ($(this).data("id") == a) {
                    $(this).show().siblings(".J_iframe").hide();
                    /*var b = $(this).attr("src");
                    if (b == n[0]) {
                        $(this).attr("src", $(this).attr("src"))
                    }*/
                    return false
                }
            });
            $(this).addClass("active").siblings(".J_menuTab").removeClass("active");
            $("#side-menu").find('a').removeClass('a-active');
            $('#'+$(this).attr('data-alink')).addClass('a-active');
            o(this)
        }
    }
    $(".J_menuTabs").on("click", ".J_menuTab", q);
    function r() {
        var a = $('.J_iframe[data-id="' + $(this).data("id") + '"]');
        var b = a.attr("src")
    }
    $(".J_menuTabs").on("dblclick", ".J_menuTab", r);
    $(".J_tabLeft").on("click", u);
    $(".J_tabRight").on("click", t);
    $(".J_tabCloseAll").on("click",
        function() {
            $(".page-tabs-content").children("[data-id]").not(":first").each(function() {
                $('.J_iframe[data-id="' + $(this).data("id") + '"]').remove();
                $(this).remove()
            });
            $(".page-tabs-content").children("[data-id]:first").each(function() {
                $('.J_iframe[data-id="' + $(this).data("id") + '"]').show();
                $(this).addClass("active")
            });
            $(".page-tabs-content").css("margin-left", "0")
        })
});
