<%@ page import="com.tw.archive.SubPage1VO" %>
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
