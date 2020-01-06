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
        //测试
        var dragon='';
        var phoen='';
        var dragonArea='';
        var phoenArea='';
        var area0='';
        var area1='';
        var area2='';
        $(".btn-click-trend").each(function(){
            var o=$(this);
            if (o.hasClass('dragonPrime')){
                dragon="1:1"
            }
            if (o.hasClass('dragonComposite')){
                dragon="1:0"
            }
            if (o.hasClass('phoenPrime')){
                phoen="0:1"
            }
            if (o.hasClass('phoenComposite')){
                phoen="0:0"
            }
            if (o.hasClass('dragonArea')){
                dragonArea=dragonArea+o.text()+",";
            }
            if (o.hasClass('phoenArea')){
                phoenArea=phoenArea+o.text()+",";
            }
            if (o.hasClass('area0')){
                area0=area0+o.text()+",";
            }
            if (o.hasClass('area1')){
                area1=area1+o.text()+",";
            }
            if (o.hasClass('area2')){
                area2=area2+o.text()+",";
            }
        });
        //质合默认选择 1，2
        dragonArea=delStrEndwithComma(dragonArea);
        phoenArea=delStrEndwithComma(phoenArea);
        area0=delStrEndwithComma(area0);
        area1=delStrEndwithComma(area1);
        area2=delStrEndwithComma(area2);
        var occurArr=calcDragonOccurs(dragon,phoen,dragonArea,phoenArea,area0,area1,area2);
        if (occurArr==0){
            layer.msg('所选数字不能为空');
            return;
        }else {
            var arrOccurs=[];
            for (var i = 0; i <occurArr ; i++) {
                arrOccurs.push(i+1);
            }
            var params={
                headAndTail:dragon+";"+phoen+";"+"1,2",//默认选择 1，2
                headArea:dragonArea,
                tailArea:phoenArea,
                area0:area0,
                area1:area1,
                area2:area2,
                occurs:arrOccurs.join(",")
            };
            console.log(params);
            //处理龙头凤尾的选择数据
            //生成条件，存到redis。页面加载再获取数据
            $.post("/dragon/dealWithDragon",params, function(res) {
                layer.close(layer.index);
                parent.location.reload();

            });
        }
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