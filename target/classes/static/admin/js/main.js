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





    // $("#basic_trend").on('click', function () {
    //     layer.open({
    //         type: 2,
    //         title: '基本走势',
    //         skin: 'layui-layer-rim', //加上边框
    //         area: ['1200px', '500px'], //宽高
    //         content: '/stat/trend'
    //     });
    //
    // });
    //按钮点击  numRegion01。。。
    $(".btn-nums-select").click(function () {
        var val = $(this);
        if (val.hasClass("btn-click-trend")) {
            val.removeClass("btn-click-trend")
        } else {
            val.addClass("btn-click-trend");
        }
    });

    $("#clear-trend").click(function(){
        //在iframe页面选择负页面元素
       // var obj=$("#condition",window.parent.document);
      //  obj.css("background","red");
       // console.log($("#condition",window.parent.document).length);
        $(".btn-nums-select").each(function(){
           var numBtn=$(this);
            if (numBtn.hasClass("btn-click-trend")) {
                numBtn.removeClass("btn-click-trend")
            }
        });
    });

    //点击基本走势提交按钮
    $("#submit-choose-trend").click(function () {

        //测试

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
        //必须要选择
        if (occur1Arr.length+occur2Arr.length==0){
            layer.msg('所选数字不能为空');
            return;
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
        $.post("/stat/getTrendCalcData",params, function(res) {

            layer.msg('成功将您预测的号码提交到"已选条件"里面!',{time:1500},function() {
                layer.close(layer.index);
                parent.location.reload();
            })


        });



    });
    
    $("#trend-clear").click(function () {
        table.render({
            elem: '#test'
            ,url:'/stat/getTrendFullData'
            ,method: 'post'
            ,height: 450
            ,cellMinWidth: 50 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
            ,cols: [[
                {field:'valueComma', width:200, title: '前三位',align:'center'}
                // ,{field:'second', width:80, title: '第二位',align:'center'}
                // ,{field:'third', width:80, title: '第三位',align:'center'}
            ]]
            ,done:function (res, curr, count) {
                $("#count-desc").text(count);
            }
        });
    });

});