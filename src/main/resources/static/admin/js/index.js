var $, tab, skyconsWeather;
layui.config({
    base: "static/admin/js/"
}).use(['bodyTab', 'form', 'element', 'layer', 'jquery'], function () {
    var form = layui.form,
        layer = layui.layer,
        element = layui.element;
    $ = layui.jquery;
    tab = layui.bodyTab({
        openTabNum: "50",  //最大可打开窗口数量
        url: "/admin/user/getUserMenu" //获取菜单json地址
    });

    var updateConditionId = '';

    //更换皮肤
    function skins() {
        var skin = window.sessionStorage.getItem("skin");
        if (skin) {  //如果更换过皮肤
            if (window.sessionStorage.getItem("skinValue") != "自定义") {
                var skin = window.sessionStorage.getItem("skin");
                $("body").addClass(skin);
                $(".layui-side").removeAttr("class").addClass("layui-side").addClass("layui-bg-" + skin + "");
            } else {
                $(".layui-layout-admin .layui-header").css("background-color", skin.split(',')[0]);
                // $(".layui-bg-black").css("background-color",skin.split(',')[1]);
                $(".layui-side").removeAttr("class").addClass("layui-side").addClass("layui-bg-" + skin.split(',')[1] + "");
                $(".hideMenu").css("background-color", skin.split(',')[2]);
            }
        }
    }

    skins();
    $(".changeSkin").click(function () {
        layer.open({
            skin: 'layui-layer-molv', //墨绿风格弹窗
            title: "更换皮肤",
            area: ["310px", "280px"],
            type: "1",
            content: '<div class="skins_box">' +
                '<form class="layui-form">' +
                '<div class="layui-form-item">' +
                '<input type="radio" name="skin" value="默认" title="默认" lay-filter="default" checked="">' +
                '<input type="radio" name="skin" value="橙色" title="橙色" lay-filter="orange">' +
                '<input type="radio" name="skin" value="蓝色" title="蓝色" lay-filter="blue">' +
                '<input type="radio" name="skin" value="自定义" title="自定义" lay-filter="custom">' +
                '<div class="skinCustom">' +
                '<input type="text" class="layui-input topColor" name="topSkin" placeholder="顶部颜色" />' +
                '<input type="text" class="layui-input leftColor" name="leftSkin" placeholder="左侧颜色" />' +
                '<input type="text" class="layui-input menuColor" name="btnSkin" placeholder="顶部菜单按钮" />' +
                '</div>' +
                '</div>' +
                '<div class="layui-form-item skinBtn">' +
                '<a href="javascript:;" class="layui-btn layui-btn-small layui-btn-normal" lay-submit="" lay-filter="changeSkin">确定更换</a>' +
                '<a href="javascript:;" class="layui-btn layui-btn-small layui-btn-primary" lay-submit="" lay-filter="noChangeSkin">我再想想</a>' +
                '</div>' +
                '</form>' +
                '</div>',
            success: function (index, layero) {
                var skin = window.sessionStorage.getItem("skin");
                if (window.sessionStorage.getItem("skinValue")) {
                    $(".skins_box input[value=" + window.sessionStorage.getItem("skinValue") + "]").attr("checked", "checked");
                }
                ;
                if ($(".skins_box input[value=自定义]").attr("checked")) {
                    $(".skinCustom").css("visibility", "inherit");
                    $(".topColor").val(skin.split(',')[0]);
                    $(".leftColor").val(skin.split(',')[1]);
                    $(".menuColor").val(skin.split(',')[2]);
                }
                ;
                form.render();
                $(".skins_box").removeClass("layui-hide");
                $(".skins_box .layui-form-radio").on("click", function () {
                    var skinColor;
                    if ($(this).find("div").text() == "橙色") {
                        skinColor = "orange";
                    } else if ($(this).find("div").text() == "蓝色") {
                        skinColor = "blue";
                    } else if ($(this).find("div").text() == "默认") {
                        skinColor = "black";
                    }
                    if ($(this).find("div").text() != "自定义") {
                        $(".topColor,.leftColor,.menuColor").val('');
                        $("body").removeAttr("class").addClass("main_body " + skinColor + "");
                        $(".skinCustom").removeAttr("style");
                        $(".layui-bg-black,.hideMenu,.layui-layout-admin .layui-header").removeAttr("style");
                        $(".layui-side").removeAttr("class").addClass("layui-side").addClass("layui-bg-" + skinColor + "");
                    } else {
                        $(".skinCustom").css("visibility", "inherit");
                    }
                })
                var skinStr, skinColor;
                $(".topColor").blur(function () {
                    $(".layui-layout-admin .layui-header").css("background-color", $(this).val());
                })
                $(".leftColor").blur(function () {
                    // $(".layui-bg-black").css("background-color",$(this).val());
                    $(".layui-side").removeAttr("class").addClass("layui-side").addClass("layui-bg-" + $(this).val() + "");
                })
                $(".menuColor").blur(function () {
                    $(".hideMenu").css("background-color", $(this).val());
                })

                form.on("submit(changeSkin)", function (data) {
                    if (data.field.skin != "自定义") {
                        if (data.field.skin == "橙色") {
                            skinColor = "orange";
                        } else if (data.field.skin == "蓝色") {
                            skinColor = "blue";
                        } else if (data.field.skin == "默认") {
                            skinColor = "black";
                        }
                        window.sessionStorage.setItem("skin", skinColor);
                    } else {
                        skinStr = $(".topColor").val() + ',' + $(".leftColor").val() + ',' + $(".menuColor").val();
                        window.sessionStorage.setItem("skin", skinStr);
                        $("body").removeAttr("class").addClass("main_body");
                    }
                    window.sessionStorage.setItem("skinValue", data.field.skin);
                    layer.closeAll("page");
                });
                form.on("submit(noChangeSkin)", function () {
                    $("body").removeAttr("class").addClass("main_body " + window.sessionStorage.getItem("skin") + "");
                    $(".layui-bg-black,.hideMenu,.layui-layout-admin .layui-header").removeAttr("style");
                    skins();
                    layer.closeAll("page");
                });
            },
            cancel: function () {
                $("body").removeAttr("class").addClass("main_body " + window.sessionStorage.getItem("skin") + "");
                $(".layui-bg-black,.hideMenu,.layui-layout-admin .layui-header").removeAttr("style");
                skins();
            }
        })
    })

    //退出
    $(".signOut").click(function () {
        window.sessionStorage.removeItem("menu");
        menu = [];
        window.sessionStorage.removeItem("curmenu");
    })

    //隐藏左侧导航
    $(".hideMenu").click(function () {
        $(".layui-layout-admin").toggleClass("showMenu");
        //渲染顶部窗口
        tab.tabMove();
    })

    //渲染左侧菜单
    tab.render();

    //锁屏
    function lockPage() {
        layer.open({
            title: false,
            type: 1,
            content: '	<div class="admin-header-lock" id="lock-box">' +
                '<div class="admin-header-lock-img"><img src="images/face.jpg"/></div>' +
                '<div class="admin-header-lock-name" id="lockUserName"><shiro:principal property = "nickName"/></div>' +
                '<div class="input_btn">' +
                '<input type="password" class="admin-header-lock-input layui-input" autocomplete="off" placeholder="请输入密码解锁.." name="lockPwd" id="lockPwd" />' +
                '<button class="layui-btn" id="unlock">解锁</button>' +
                '</div>' +
                '<p>请输入“123456”，否则不会解锁成功哦！！！</p>' +
                '</div>',
            closeBtn: 0,
            shade: 0.9
        })
        $(".admin-header-lock-input").focus();
    }

    $(".lockcms").on("click", function () {
        window.sessionStorage.setItem("lockcms", true);
        lockPage();
    })
    // 判断是否显示锁屏
    if (window.sessionStorage.getItem("lockcms") == "true") {
        lockPage();
    }
    // 解锁
    $("body").on("click", "#unlock", function () {
        if ($(this).siblings(".admin-header-lock-input").val() == '') {
            layer.msg("请输入解锁密码！");
            $(this).siblings(".admin-header-lock-input").focus();
        } else {
            if ($(this).siblings(".admin-header-lock-input").val() == "123456") {
                window.sessionStorage.setItem("lockcms", false);
                $(this).siblings(".admin-header-lock-input").val('');
                layer.closeAll("page");
            } else {
                layer.msg("密码错误，请重新输入！");
                $(this).siblings(".admin-header-lock-input").val('').focus();
            }
        }
    });
    $(document).on('keydown', function () {
        if (event.keyCode == 13) {
            $("#unlock").click();
        }
    });

    //手机设备的简单适配
    var treeMobile = $('.site-tree-mobile'),
        shadeMobile = $('.site-mobile-shade')

    treeMobile.on('click', function () {
        $('body').addClass('site-mobile');
    });

    shadeMobile.on('click', function () {
        $('body').removeClass('site-mobile');
    });

    // 添加新窗口
    $("body").on("click", ".layui-nav .layui-nav-item a", function () {
        //如果不存在子级
        if ($(this).siblings().length == 0) {
            addTab($(this));
            $('body').removeClass('site-mobile');  //移动端点击菜单关闭菜单层
        }
        $(this).parent("li").siblings().removeClass("layui-nav-itemed");
    })

    //判断是否处于锁屏状态(如果关闭以后则未关闭浏览器之前不再显示)
    /*    if(window.sessionStorage.getItem("lockcms") != "true" && window.sessionStorage.getItem("showNotice") != "true"){
            showNotice();
        }*/
    // $(".showNotice").on("click",function(){
    //     showNotice();
    // })

    //刷新后还原打开的窗口
    if (window.sessionStorage.getItem("menu") != null) {
        menu = JSON.parse(window.sessionStorage.getItem("menu"));
        curmenu = window.sessionStorage.getItem("curmenu");
        var openTitle = '';
        for (var i = 0; i < menu.length; i++) {
            openTitle = '';
            if (menu[i].icon) {
                if (menu[i].icon.split("-")[0] == 'icon') {
                    openTitle += '<i class="iconfont ' + menu[i].icon + '"></i>';
                } else {
                    openTitle += '<i class="layui-icon">' + menu[i].icon + '</i>';
                }
            }
            openTitle += '<cite>' + menu[i].title + '</cite>';
            openTitle += '<i class="layui-icon layui-unselect layui-tab-close" data-id="' + menu[i].layId + '">&#x1006;</i>';
            element.tabAdd("bodyTab", {
                title: openTitle,
                content: "<iframe src='" + menu[i].href + "' data-id='" + menu[i].layId + "'></frame>",
                id: menu[i].layId
            })
            //定位到刷新前的窗口
            if (curmenu != "undefined") {
                if (curmenu == '' || curmenu == "null") {  //定位到后台首页
                    element.tabChange("bodyTab", '');
                } else if (JSON.parse(curmenu).title == menu[i].title) {  //定位到刷新前的页面
                    element.tabChange("bodyTab", menu[i].layId);
                }
            } else {
                element.tabChange("bodyTab", menu[menu.length - 1].layId);
            }
        }
        //渲染顶部窗口
        tab.tabMove();
    }

    //刷新当前
    $(".refresh").on("click", function () {  //此处添加禁止连续点击刷新一是为了降低服务器压力，另外一个就是为了防止超快点击造成chrome本身的一些js文件的报错(不过貌似这个问题还是存在，不过概率小了很多)
        if ($(this).hasClass("refreshThis")) {
            $(this).removeClass("refreshThis");
            $(".clildFrame .layui-tab-item.layui-show").find("iframe")[0].contentWindow.location.reload(true);
            setTimeout(function () {
                $(".refresh").addClass("refreshThis");
            }, 2000)
        } else {
            layer.msg("您点击的速度超过了服务器的响应速度，还是等两秒再刷新吧！");
        }
    })

    //关闭其他
    $(".closePageOther").on("click", function () {
        if ($("#top_tabs li").length > 2 && $("#top_tabs li.layui-this cite").text() != "后台首页") {
            var menu = JSON.parse(window.sessionStorage.getItem("menu"));
            $("#top_tabs li").each(function () {
                if ($(this).attr("lay-id") != '' && !$(this).hasClass("layui-this")) {
                    element.tabDelete("bodyTab", $(this).attr("lay-id")).init();
                    //此处将当前窗口重新获取放入session，避免一个个删除来回循环造成的不必要工作量
                    for (var i = 0; i < menu.length; i++) {
                        if ($("#top_tabs li.layui-this cite").text() == menu[i].title) {
                            menu.splice(0, menu.length, menu[i]);
                            window.sessionStorage.setItem("menu", JSON.stringify(menu));
                        }
                    }
                }
            })
        } else if ($("#top_tabs li.layui-this cite").text() == "后台首页" && $("#top_tabs li").length > 1) {
            $("#top_tabs li").each(function () {
                if ($(this).attr("lay-id") != '' && !$(this).hasClass("layui-this")) {
                    element.tabDelete("bodyTab", $(this).attr("lay-id")).init();
                    window.sessionStorage.removeItem("menu");
                    menu = [];
                    window.sessionStorage.removeItem("curmenu");
                }
            })
        } else {
            layer.msg("没有可以关闭的窗口了@_@");
        }
        //渲染顶部窗口
        tab.tabMove();
    })
    //关闭全部
    $(".closePageAll").on("click", function () {
        if ($("#top_tabs li").length > 1) {
            $("#top_tabs li").each(function () {
                if ($(this).attr("lay-id") != '') {
                    element.tabDelete("bodyTab", $(this).attr("lay-id")).init();
                    window.sessionStorage.removeItem("menu");
                    menu = [];
                    window.sessionStorage.removeItem("curmenu");
                }
            })
        } else {
            layer.msg("没有可以关闭的窗口了@_@");
        }
        //渲染顶部窗口
        tab.tabMove();
    })
    //点击基本趋势按钮
    $("#basic_trend").on('click', function () {
        layer.open({
            type: 2,
            title: '基本走势',
            skin: 'layui-layer-rim', //加上边框
            area: ['1200px', '500px'], //宽高
            content: '/stat/trend'
        });

    });

    //basic_dragon
    $("#basic_dragon").on('click', function () {
        layer.open({
            type: 2,
            title: '龙头凤尾012路',
            skin: 'layui-layer-rim', //加上边框
            area: ['1200px', '500px'], //宽高
            content: '/dragon/index'
        });

    });


    function initCombinationTable() {
        //请求初始数据，获取组合列表 返回[1-1-2,2-2-3]格式
        $.post("/stat/getInitCombination", {}, function (res) {
            var count = res.data.length;
            $("#combination-count").text(count);
            //cb-table
            var dataArr = res.data;
            var table = $("#cb-table");
            //table.empty();
            for (var i = 0; i < dataArr.length; i++) {
                let obj = dataArr[i];
                let arrTemp = obj.split("-");
                var trStr = "<tr>" +
                    "<td>" + arrTemp[0] + "</td>" +
                    "<td>" + arrTemp[1] + "</td>" +
                    "<td>" + arrTemp[2] + "</td>" +
                    "</tr>";
                table.append(trStr);
            }
        });
    }

    /**
     * 删除条件
     */
    $("#condition-table").on('click', "button[class='del-condition']", function () {
        var id = $(this).parent().parent().find(".con-id");
        //去后台删除掉
        $.post("/stat/delConditionById", {id: id.eq(0).val()}, function (res) {
            id.parent().parent().remove();//移除当前的条件行
            initCombinationTable();
        });
    });

    //处理胆码条件修改
    function handlerDanMa(regions, occurs, uuid) {
        var regionArr = regions.split(",");
        var occursArr = occurs.split(",");
        //当前修改条件的id
        updateConditionId = uuid;
        //弹出修改窗,动态复制
        $('#danmaConditionWin').modal('show');
        //
        $("#danmaConditionWin input:checkbox[name='region']").each(function () {
            var cbVal = $(this).val();
            if ($.inArray(cbVal, regionArr) >= 0) {
                $(this).prop("checked", true); //标准写法，推荐！
            }
        });
        $("#danmaConditionWin input:checkbox[name='occurs']").each(function () {
            var cbVal = $(this).val();
            if ($.inArray(cbVal, occursArr) >= 0) {
                $(this).prop("checked", true); //标准写法，推荐！
            }
        });

    }

    /**
     * 修改定位码的条件
     * @param firsts
     * @param seconds
     * @param thirds
     * @param occurs
     * @param uuid
     */
    function handlerDingweima(firsts, seconds, thirds, occurs, uuid) {
        updateConditionId = uuid;
        var firstArr = firsts.split(",");
        var secondArr = seconds.split(",");
        var thirdArr = thirds.split(",");
        var occurs = occurs.split(",");
        $('#dingweimaConditionWin').modal('show');
        $("#dingweimaConditionWin input:checkbox[name='first']").each(function () {
            var cbVal = $(this).val();
            if ($.inArray(cbVal, firstArr) >= 0) {
                $(this).prop("checked", true); //标准写法，推荐！
            }
        });
        $("#dingweimaConditionWin input:checkbox[name='second']").each(function () {
            var cbVal = $(this).val();
            if ($.inArray(cbVal, secondArr) >= 0) {
                $(this).prop("checked", true); //标准写法，推荐！
            }
        });
        $("#dingweimaConditionWin input:checkbox[name='third']").each(function () {
            var cbVal = $(this).val();
            if ($.inArray(cbVal, thirdArr) >= 0) {
                $(this).prop("checked", true); //标准写法，推荐！
            }
        });
        $("#dingweimaConditionWin input:checkbox[name='occurs']").each(function () {
            var cbVal = $(this).val();
            if ($.inArray(cbVal, occurs) >= 0) {
                $(this).prop("checked", true); //标准写法，推荐！
            }
        });
    }

    function getCheckValuesFromModal(modalId, cbName) {
        var str = "";
        $("#" + modalId + " input:checkbox[name='" + cbName + "']:checked").each(function () {
            str += $(this).val() + ",";
        });
        if (str.substr(str.length - 1) == ',') {
            str = str.substring(0, str.length - 1);
        }
        return str;
    }

    /**
     * 对龙头凤尾对条件修改点击确定
     */
    $("#dragonOk").click(function () {
        var head = getCheckValuesFromModal("dragonConditionWin",
            "head");
        var tail = getCheckValuesFromModal("dragonConditionWin",
            "tail");
        var htoccur = getCheckValuesFromModal("dragonConditionWin",
            "htoccur");
        if (head.length>0){
            head="1:"+head;
        }
        if (tail.length>0){
            tail="0:"+tail;
        }
        var  headAndTail=head+";"+tail+";"+htoccur;
        var  dragonArea=getCheckValuesFromModal("dragonConditionWin",
            "dragonArea");
        var  phoenArea=getCheckValuesFromModal("dragonConditionWin",
            "phoenArea");
        var area0=getCheckValuesFromModal("dragonConditionWin",
            "area0");
        var area1=getCheckValuesFromModal("dragonConditionWin",
            "area1");
        var area2=getCheckValuesFromModal("dragonConditionWin",
            "area2");
        var occurs=getCheckValuesFromModal("dragonConditionWin",
            "occurs");
        //必须选一个出几
        if (occurs.length==0){
            alert("wrong select");
            return;
        }
        if ((head!='' || tail!='') && htoccur==''){
            alert("wrong select");
            return;
        }
        if (dragonArea.length==0 && phoenArea.length==0 &&
            area0.length==0 && area1.length==0 && area2.length==0
            && head.length==0 && tail.length==0
        ) {
            alert("wrong select");
            return;
        }
        var params={
            headAndTail:headAndTail,
            headArea:dragonArea,
            tailArea:phoenArea,
            area0:area0,
            area1:area1,
            area2:area2,
            occurs:occurs,
            uuid: updateConditionId

        };
        $.post("/dragon/dragonConditionChange",params, function(res) {
            $('#dragonConditionWin').modal('hide');
            location.reload();

        });
        console.log();

    });

    /**
     * 对定位码的修改确定定义点击事件
     */
    $("#dingweimaOk").click(function () {
        var firsts = getCheckValuesFromModal("dingweimaConditionWin", "first");
        var seconds = getCheckValuesFromModal("dingweimaConditionWin", "second");
        var thirds = getCheckValuesFromModal("dingweimaConditionWin", "third");
        var occurs = getCheckValuesFromModal("dingweimaConditionWin", "occurs");
        //第一位,第二位,第三位必须选一个
        if (occurs.length == 0 || (firsts.length == 0 && seconds.length == 0 && thirds.length == 0)) {
            alert("wrong select");
        } else {
            var params = {
                firstPredict: firsts,
                secondPredict: seconds,
                thirdPredict: thirds,
                occurTimes: occurs,
                uuid: updateConditionId
            };
            $.post("/stat/dingweimaConditionChange", params, function (res) {
                $('#dingweimaConditionWin').modal('hide');
                location.reload();
            });
        }


    });


    /**
     * 胆码条件修改确定
     */
    $("#danmaOk").click(function () {
        var regions = getCheckValuesFromModal("danmaConditionWin", "region");
        var occurs = getCheckValuesFromModal("danmaConditionWin", "occurs");
        //没有选择，不操作
        if (regions.length == 0 || occurs.length == 0) {
            alert("wrong select");
        } else {
            var params = {
                regions: regions,
                occurs: occurs,
                uuid: updateConditionId
            };
            $.post("/stat/danmaConditionChange", params, function (res) {
                $('#danmaConditionWin').modal('hide');
                location.reload();
            });
        }


    });

    /**
     * 处理龙头凤尾选择条件
     */
    function handlerDragonPhoen(conditions, uuid) {
        updateConditionId = uuid;
        $('#dragonConditionWin').modal('show');
        var arr = conditions.split(";");
        var dragonAndTail = arr[1];
        var dtArr = dragonAndTail.split("-");
        var dragon = dtArr[0];
        if (dragon.split(":").length == 2) {
            var a2 = dragon.split(":")[1];
            $("#head_" + a2).prop("checked", true);
        }
        var tail = dtArr[1];
        if (tail.split(":").length == 2) {
            var b2 = tail.split(":")[1];
            $("#tail_" + b2).prop("checked", true);
        }
        var dtOccur = dtArr[2];
        if (dtOccur.length > 0) {
            var arrTemp = dtOccur.split(",");
            for (x in arrTemp) {
                if (x.length > 0) {
                    $("#htoccur_" + arrTemp[x]).prop("checked", true);
                }
            }
        }
        //龙头012路 dragonArea
        var headArea = arr[2];
        var headAreaArr = headArea.split(",");
        for (x in headAreaArr) {
            if (x.length > 0) {
                $("#dragonArea_" + headAreaArr[x]).prop("checked", true);
            }
        }
        //凤尾012路
        var headArea = arr[3];
        var headAreaArr = headArea.split(",");
        for (x in headAreaArr) {
            if (x.length > 0) {
                $("#phoenArea_" + headAreaArr[x]).prop("checked", true);
            }
        }
        //o路个数 area1_0
        var area0=arr[4];
        var area0Arr = area0.split(",");
        for (x in area0Arr) {
            if (x.length > 0) {
                $("#area0_" + area0Arr[x]).prop("checked", true);
            }
        }
        var area1=arr[5];
        var area1Arr = area1.split(",");
        for (x in area1Arr) {
            if (x.length > 0) {
                $("#area1_" + area1Arr[x]).prop("checked", true);
            }
        }
        var area2=arr[6];
        var area2Arr = area2.split(",");
        for (x in area2Arr) {
            if (x.length > 0) {
                $("#area2_" + area2Arr[x]).prop("checked", true);
            }
        }
        //occurs_6
        var occurs=arr[7];
        var occursArr = occurs.split(",");
        for (x in occursArr) {
            if (x.length > 0) {
                $("#occurs_" + occursArr[x]).prop("checked", true);
            }
        }

    }

    /**
     * 修改条件
     */
    $("#condition-table").on('click', "button[class='edit-condition']", function () {
        var idObj = $(this).parent().parent().find(".con-id");
        var uuid = idObj.eq(0).val();
        //去后台删除掉
        $.post("/stat/getConditionById", {id: uuid}, function (res) {
            var conditions = res.data;
            var arr = conditions.split(";");
            if (arr[0] == 1) {
                var regions = arr[1];
                var occurs = arr[2];
                //胆码
                handlerDanMa(regions, occurs, uuid);
            } else if (arr[0] == 2) {
                var firsts = arr[1];
                var seconds = arr[2]
                var thirds = arr[3];
                var occurs = arr[4];
                //定位码
                handlerDingweima(firsts, seconds, thirds, occurs, uuid);
            } else if (arr[0] == 3) {
                //龙头凤尾
                handlerDragonPhoen(conditions, uuid);
            }

        });
    });


    $.post("/stat/getConditions", {}, function (res) {
        var dataArr = res.data;
        var conditionTable = $("#condition-table");
        conditionTable.empty();
        for (var i = 0; i < dataArr.length; i++) {
            var condition = dataArr[i];
            var str = "<tr><td><input class='con-id' type='hidden' value='" + condition.id + "' /> " + condition.type + "</td><td>" + condition.content
                + "</td><td>" + condition.count + "</td><td>" +
                "<input type='checkbox'>容错" +
                "</td><td> <button type='button'  class='edit-condition'>编 辑</button>" +
                "</td><td><button type='button' class='del-condition'>删 除</button></td>" +
                "</tr>";
            conditionTable.append(str);
        }
        //加载完条件再加载数据集合
        initCombinationTable();
    });

    /**
     * 龙头凤尾质合互斥
     */
    $("input[type=checkbox][name='head']").change(function() {
        var obj=$(this);
        var  val=obj.val();
        if(obj.prop("checked")){
            $("input[type=checkbox][name='head']").not(this).attr("checked", false);
        }
    });
    $("input[type=checkbox][name='tail']").change(function() {
        var obj=$(this);
        var  val=obj.val();
        if(obj.prop("checked")){
            $("input[type=checkbox][name='tail']").not(this).attr("checked", false);
        }
    });


})

//打开新窗口
function addTab(_this) {
    tab.tabAdd(_this);
}
