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





    //按钮点击  设置选中项的颜色
    $(".btn-nums-select").click(function () {
        var val = $(this);
        if (val.hasClass("btn-click-trend")) {
            val.removeClass("btn-click-trend")
        } else {
            val.addClass("btn-click-trend");
        }
    });

    //清空选中项的颜色
    $("#clear-trend").click(function(){
        $(".btn-nums-select").each(function(){
           var numBtn=$(this);
            if (numBtn.hasClass("btn-click-trend")) {
                numBtn.removeClass("btn-click-trend")
            }
        });
    });

    function getOccursCounts(paramArr){
        var count=0;
        for (var i = 0; i < paramArr.length; i++) {
            if (paramArr[i]!='' && paramArr[i].length>0) {
                count++;
            }
        }
        return count;
    }

    //点击基本走势提交按钮
    $("#submit-choose-trend").click(function () {

        var sumValues='';
        var leftPass='';
        var break1='';
        var rightPass='';
        var break2='';
        var fall='';
        $(".btn-click-trend").each(function() {
            var o=$(this);
            if (o.hasClass('sumValues')){
                sumValues=sumValues+o.text()+",";
            }
            if (o.hasClass('leftPass')){
                leftPass=o.text();
            }
            if (o.hasClass('break1')){
                break1=o.text();
            }
            if (o.hasClass('rightPass')){
                rightPass=o.text();
            }
            if (o.hasClass('break2')){
                break2=o.text();
            }
            if (o.hasClass('fall')){
                fall=o.text();
            }

        });
        sumValues=delStrEndwithComma(sumValues);
        console.log("sumValues:"+sumValues);
        console.log("leftPass:"+leftPass);
        console.log("break1:"+break1);
        console.log("rightPass:"+rightPass);
        console.log("break2:"+break2);
        console.log("fall:"+fall);
        var paramArr=[sumValues,leftPass,break1,rightPass,break2,fall];
        var occurs=getOccursCounts(paramArr);
        if (occurs==0){
            layer.msg('所选项不能为空');
            return;
        } else {
            var arrOccurs=[];
            for (var i = 0; i <occurs;i++ ){
                arrOccurs.push(i+1);
            }
            var params={
                sumValues:sumValues,//默认选择 1，2
                leftPass:leftPass,
                break1:break1,
                rightPass:rightPass,
                break2:break2,
                fall:fall,
                occurs:arrOccurs.join(",")
            };
            $.post("/sum/dealWithSumValue",params, function(res) {
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
    


});