<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>collect wish, handle</title>
    <%
        request.setCharacterEncoding("utf-8");
        String name = request.getParameter("name");
        String believe = request.getParameter("believe");
        boolean promised = believe.equalsIgnoreCase("yes");
    %>
</head>
<body>
<% if (promised) { %>
<h1>your wish submitted!</h1>
<p>hear is your <%=request.getParameter("wish")%>
</p>
<%} else {%>
<c:redirect url="form-submit.jsp">
    <c:param name="name" value="<%=name%>"/>
</c:redirect>
<%}%>
</body>
</html>