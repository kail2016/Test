<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
    <title>Seckill Detail</title>
    <%@include file="common/head.jsp"%>
</head>
<body>
<div class="container">
    <div class="panel panel-default text-center">
        <div class="panel-heading">
            <h1>${seckill.name}</h1>
        </div>
        <div class="panel-body">
            <h2 class="text-danger">
            	<!-- 显示倒计时图标 -->
                <span class="glyphicon glyphicon-time"></span>
                <!-- 显示倒计时 -->
                <span class="glyphicon" id="seckill-box"></span>
            </h2>
        </div>
    </div>
</div>
<div id="killPhoneModal" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h3 class="modal-title text-center">
                    <span class="glyphicon glyphicon-phone"></span>phone：
                </h3>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-xs-8 col-xs-offset-2">
                        <input type="text" name="killphone" id="killphoneKey"
                               placeholder="please input your phone" class="form-control"/>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
            <!-- 验证信息 -->
                <span id="killphoneMessage" class="glyphicon"></span>
                <button type="button" id="killPhoneBtn" class="btn btn-success">
                    <span class="glyphicon glyphicon-phone"></span>
                    Submit
                </button>
            </div>
        </div>
    </div>
</div>
<script src="/seckill/resources/script/jquery/js/jquery.min.js"></script>
<script src="/seckill/resources/script/bootstrap/js/bootstrap.min.js"></script>
<script src="/seckill/resources/script/cookie/js/cookie.js"></script>
<!-- 使用cdn获取公共Js  -->
<script src="https://cdn.bootcss.com/jquery.countdown/2.1.0/jquery.countdown.js"></script>
<!--开始编写交互逻辑  -->
<script type="text/javascript" src="/seckill/resources/script/seckill.js"></script>
<script type="text/javascript">
    $(function (){
		//使用EL表达式传入参数
        seckill.detail.init({
            seckillId : ${seckill.seckillId},
            startTime : ${seckill.startTime.time},  //.time 毫秒
            endTime : ${seckill.endTime.time} 
        });
    });
</script>
</body>

<%--<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->--%>
<%--<script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>--%>

<%--<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->--%>
<%--<script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>--%>

</body>
</html>