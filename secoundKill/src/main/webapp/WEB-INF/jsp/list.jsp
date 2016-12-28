<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- import jstl -->
<%@include file="common/tag.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title>秒杀列表页</title>
    <%@include file="common/head.jsp"%><!-- 静态包含 -->
</head>
<body>
	<!-- 页面显示部分  -->
    <div class="container">
        <div class="panel panel-default">
            <div class="panel-heading text-center">
                <h1>Seckill List</h1>
            </div>
            <div class="panel-body">
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th>名称Name</th>
                            <th>库存Inventory</th>
                            <th>StartTime</th>
                            <th>EndTime</th>
                            <th>CreateTime</th>
                            <th>详情页Detail</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="sk" items="${seckills}">
                            <tr>
                                <td>${sk.name}</td>
                                <td>${sk.number}</td>
                                <td>
                                    <fmt:formatDate value="${sk.startTime}" pattern="yyyy-MM-dd HH:mm:SS"/>
                                </td>
                                <td>
                                    <fmt:formatDate value="${sk.endTime}" pattern="yyyy-MM-dd HH:mm:SS"/>
                                </td>
                                <td>
                                    <fmt:formatDate value="${sk.createTime}" pattern="yyyy-MM-dd HH:mm:SS"/>
                                </td>
                                <td>
                                    <a class="btn btn-info" href="/seckill/${sk.seckillId}/detail" >link</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>

<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>

</body>
</html>