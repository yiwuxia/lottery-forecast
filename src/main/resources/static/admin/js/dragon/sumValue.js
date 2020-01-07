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
        $(".btn-nums-select").each(function(){
           var numBtn=$(this);
            if (numBtn.hasClass("btn-click-trend")) {
                numBtn.removeClass("btn-click-trend")
            }
        });
    });
    //龙头凤尾选择后的出现次数
    function calcDragonOccurs(dragon,phoen,dragonArea,phoenArea,area0,area1,area2){
        var count=0;
        if (dragon.length>0 || phoen.length>0){
            count++
        }
        var arrTemp=[dragonArea,phoenArea,area0,area1,area2];
        for (var i = 0; i < arrTemp.length; i++) {
            if (arrTemp[i].length>0){
                count++;
            }
        }
        return count;
    }

    //点击基本走势提交按钮
    $("#submit-choose-trend").click(function () {

        console.log("---");

    });

    //如果字符串不为空且以,结尾。则去掉最后一位
    function delStrEndwithComma(str){
        if (str!=null && str.length>0){
            var last=str.substring(str.length-1);
            if (last==','){
                return str.substring(0,str.length-1);
            }
        }
        return str;
    }
    
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