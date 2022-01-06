<%@ page import="com.tw.ComplexObjectVO" %>
<%@ page import="com.tw.HelloVO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>a big jsp page</title>
    <%
        request.setAttribute("complex-0-1-2", "complex-0-1-2.jsp");
    %>
</head>
<body>
<h3>level 0-1-1</h3>
<p>page 0-1-2</p>
<p> complex-0-1-2.jsp set attribute complex-0-1-2 to "complex-0-1-2.jsp"</p>
</body>

</html>
