<%@ page import="com.tw.ComplexObjectVO" %>
<%@ page import="com.tw.HelloVO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>a big jsp page</title>
    <%
        HelloVO currentHelloVO = (HelloVO) request.getAttribute("currentHelloVO");
    %>
</head>
<body>
<h2>level 0-1</h2>
<p>page 1</p>
<p>currentHelloVO name from parent is : <%=currentHelloVO.getName()%></p>
<%
    currentHelloVO.setName("alicf(alice)");
%>
<p>currentHelloVO name change to: <%=currentHelloVO.getName()%></p>

<p>include complex-0-1-1.jsp start--------------</p>
<jsp:include page="complex-0-1-1.jsp"/>
<p>include complex-0-1-1.jsp end--------------</p>
<p>include complex-0-1-2.jsp start--------------</p>
<jsp:include page="complex-0-1-2.jsp"/>
<p>include complex-0-1-2.jsp end--------------</p>
</body>

</html>
