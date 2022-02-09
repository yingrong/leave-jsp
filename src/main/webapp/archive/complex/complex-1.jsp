<%@ page import="com.tw.archive.ComplexObjectVO" %>
<%@ page import="com.tw.archive.HelloVO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>a big jsp page</title>
    <%
        ComplexObjectVO complexObjectVO = (ComplexObjectVO) request.getAttribute("complexObjectVO");
        HelloVO currentHelloVO = complexObjectVO.getCurrentHelloVO();
    %>
</head>
<body style="background-color: antiquewhite">
<div class="tushi" style="background-color: darkgoldenrod">page 1-1</div>
<div class="tushi" style="background-color: chocolate">page 1-2</div>
<div class="tushi" style="background-color: aqua">page 1-1-1</div>
<div class="tushi" style="background-color: aquamarine">page 1-1-2</div>
<h1>root level 1</h1>
<p>page 1</p>
<div id="level0-1"></div>
<form id="form1" name="from1" action="">
    <input type="button" onclick="updateP111()" value="update page 1-1-1's value"/>
    <%
        request.setAttribute("currentHelloVO", currentHelloVO);
    %>
    <span> level0-input1:currentHelloVO.name: <%=currentHelloVO.getName()%></span>

    <p>------------------------------------------------- complex-1-1.jsp start</p>
    <jsp:include page="complex-1-1.jsp"/>

    <p>-------------------------------------------------- complex-1-1.jsp end</p>

    <%
        HelloVO currentHelloVOAfterChange = (HelloVO) request.getAttribute("currentHelloVO");
        request.setAttribute("currentHelloVO", currentHelloVOAfterChange);
    %>
    <span>level0-input2:currentHelloVO.name changed by <b>complex-1-1.jsp</b>,<%=currentHelloVO.getName()%>,<%=currentHelloVO.getName()%></span>:
    <p>------------------------------------------------- complex-1-2.jsp start</p>
    <jsp:include page="complex-1-2.jsp"/>
    <p>------------------------------------------------- complex-1-2.jsp end</p>
</form>

</body>
</html>
