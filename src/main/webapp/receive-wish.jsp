<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>collect wish, handle</title>
    <%
        String wish = request.getParameter("wish");
        wish = new String(wish.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    %>
</head>
<body>
<h1>your wish submitted!</h1>
<p>hear is your <%=wish%>
</p>
</body>
</html>