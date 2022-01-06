<%@ page import="com.tw.ComplexObjectVO" %>
<%@ page import="com.tw.HelloVO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>a big jsp page</title>
    <%
        ComplexObjectVO complexObjectVO = (ComplexObjectVO) request.getAttribute("complexObjectVO");
        String  attributeFromRequest = (String) request.getAttribute("complex-0-1-2");
    %>
</head>
<body>
<h2>level-0-1</h2>
<p>page 2</p>
<p>complexObjectVO from root page, no need to set attribute before include page</p>
<p>complexObjectVO.helloList.size is <%=complexObjectVO.getHelloVOList().size()%></p>
<p>attributeFromRequest complex-0-1-2 is <%=attributeFromRequest%> </p>
</body>

</html>