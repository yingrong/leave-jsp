<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>collect wish, handle</title>
    <%
        request.setCharacterEncoding("utf-8");
    %>
</head>
<body>
<h1>your wish submitted!</h1>
<p>hear is your <%=request.getParameter("wish")%>
</p>
</body>
</html>