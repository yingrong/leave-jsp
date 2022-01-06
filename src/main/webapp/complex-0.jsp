<%@ page import="com.tw.ComplexObjectVO" %>
<%@ page import="com.tw.HelloVO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>a big jsp page</title>
    <%
        ComplexObjectVO complexObjectVO = (ComplexObjectVO) request.getAttribute("complexObjectVO");

        HelloVO currentHelloVO = complexObjectVO.getCurrentHelloVO();

    %>
</head>
<body>
<h1>root level 0</h1>
<div id="level0-1"></div>
<form action="">
    <%
        request.setAttribute("currentHelloVO", currentHelloVO);
    %>
    <label for="level0-input1">level0-input1:currentHelloVO.name</label>:
    <input id="level0-input1" type="text"
           value="<%=currentHelloVO.getName()%>"/>





    <p>------------------------------------------------- complex-0-1.jsp start</p>
    <jsp:include page="complex-0-1.jsp"/>

    <p>-------------------------------------------------- complex-0-1.jsp end</p>



    <%
        HelloVO currentHelloVOAfterChange = (HelloVO) request.getAttribute("currentHelloVO");
        request.setAttribute("currentHelloVO", currentHelloVOAfterChange);
    %>
    <label for="level0-input20">level0-input2:currentHelloVO.name changed by <b>complex-0-1.jsp</b></label>:
    <input id="level0-input20"
           name="level0-input20"
           type="text"
           value="<%=currentHelloVO.getName()%>"/>
    <input id="level0-input21"
           name="level0-input21"
           type="text"
           value="<%=currentHelloVOAfterChange.getName()%>"/>
    <p>------------------------------------------------- complex-0-2.jsp start</p>
    <jsp:include page="complex-0-2.jsp"/>
    <p>------------------------------------------------- complex-0-2.jsp end</p>
</form>
</body>

</html>
