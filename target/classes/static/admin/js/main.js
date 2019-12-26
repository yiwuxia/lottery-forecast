layui.use(['form', 'element', 'table','layer', 'jquery'], function () {

    var table = layui.table;

    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        element = layui.element,
        $ = layui.jquery;

    $(".panel a").on("click", function () {
        window.parent.addTab($(this));
    });

    //数字格式化
    $(".panel span").each(function () {
        $(this).html($(this).text() > 9999 ? ($(this).text() / 10000).toFixed(2) + "<em>万</em>" : $(this).text());
    });

    $("#basic_trend").on('click', function () {

        layer.open({
            type: 2,
            title: '基本走势',
            skin: 'layui-layer-rim', //加上边框
            area: ['1200px', '500px'], //宽高
            content: '/stat/trend'
        });

    });
    //按钮点击  numRegion01。。。
    $(".btn-nums-select").click(function () {
        var val = $(this);
        if (val.hasClass("btn-click-trend")) {
            val.removeClass("btn-click-trend")
        } else {
            val.addClass("btn-click-trend");
        }
    });

    //点击基本走势提交按钮
    $("#submit-choose-trend").click(function () {
        var arr = $(".btn-click-trend");
        var regions = [];
        var firsts = [];
        var seconds = [];
        var thirds = [];

        //获取所选参数
        for (var i = 0; i < arr.length; i++) {
             var obj=$(arr[i]);
            if (obj.hasClass("region")) {
                regions.push(obj.text())
            }
            if (obj.hasClass("first")) {
                firsts.push(obj.text())
            }
            if (obj.hasClass("second")) {
                seconds.push(obj.text())
            }
            if (obj.hasClass("third")) {
                thirds.push(obj.text())
            }
        }
        console.log("regions:"+regions);
        var occur1=regions.length>3?3:regions.length;
        var occur2=0;
        if (firsts.length>0){
            occur2++
        }
        if (seconds.length>0){
            occur2++
        }
        if (thirds.length>0){
            occur2++
        }
        var occur1Arr=[];
        var occur2Arr=[];
        for (var i =1 ; i <=occur1; i++) {
            occur1Arr.push(i);
        }
        for (var j =1 ; j <=occur2; j++) {
            occur2Arr.push(j);
        }
        //将参数带到后台
        var params = {
            regionsPredict:regions.join(","),
            firstPredict:firsts.join(","),
            secondPredict:seconds.join(","),
            thirdPredict:thirds.join(","),
            occurTimesRegion:occur1Arr.join(","),
            occurTimes:occur2Arr.join(","),
        }
        console.log(params);
        $.post("/stat/getTrendCalcData",params, function(res) {

        });

    });
    
    $("#trend-clear").click(function () {
        table.render({
            elem: '#test'
            ,url:'/stat/getTrendFullData'
            ,method: 'post'
            ,height: 315
            ,cellMinWidth: 100 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
            ,cols: [[
                ,{field:'value', width:200, title: '列表'}
            ]]
        });
    });

});