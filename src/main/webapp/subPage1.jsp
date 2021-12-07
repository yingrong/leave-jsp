<%@ page import="com.tw.SubPage1VO" %><%--
  Created by IntelliJ IDEA.
  User: yingrong.hu
  Date: 2021/12/6
  Time: 1:11 下午
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <title>sub page 1</title>
    <%
        SubPage1VO sub1 = (SubPage1VO) request.getAttribute("sub1");
    %>
<div>
    <p>sub page 1 start</p>
    <p>sub1.subPage1Str = <%=sub1.subPage1Str%></p>
    <p>sub page 1 end</p>
</div>
