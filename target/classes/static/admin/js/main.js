layui.use(['form','element','layer','jquery'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        element = layui.element,
        $ = layui.jquery;

    $(".panel a").on("click",function(){
        window.parent.addTab($(this));
    });

    //数字格式化
    $(".panel span").each(function(){
        $(this).html($(this).text()>9999 ? ($(this).text()/10000).toFixed(2) + "<em>万</em>" : $(this).text());
    });

    $("#basic_trend").on('click',function () {

        layer.open({
            type: 2,
            title: '基本走势',
            skin: 'layui-layer-rim', //加上边框
            area: ['1200px', '500px'], //宽高
            content: '/stat/trend'
        });

    });
    //按钮点击  numRegion01。。。
    $(".btn-nums-select").click(function(){
        var val=$(this);
        if(val.hasClass("btn-click")){
            val.removeClass("btn-click")
        }else{
            val.addClass("btn-click");
        }
    });

});