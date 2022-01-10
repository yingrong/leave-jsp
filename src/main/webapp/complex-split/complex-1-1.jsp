<%@ page import="com.tw.ComplexObjectVO" %>
<%@ page import="com.tw.HelloVO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    HelloVO currentHelloVO = (HelloVO) request.getAttribute("currentHelloVO");
%>
<div style="background-color: darkgoldenrod;margin: 10px">
    <h2>level 2</h2>
    <p>page 1-1</p>
    <p>currentHelloVO name from parent is : <%=currentHelloVO.getName()%>
    </p>
    <%
        currentHelloVO.setName("alicf(alice)");
    %>
    <p>currentHelloVO name change to: <%=currentHelloVO.getName()%>
    </p>

    <p>include complex-1-1-1.jsp start--------------</p>
    <jsp:include page="complex-1-1-1.jsp"/>
    <p>include complex-1-1-1.jsp end--------------</p>
    <p>include complex-1-1-2.jsp start--------------</p>
    <jsp:include page="complex-1-1-2.jsp"/>
    <p>include complex-1-1-2.jsp end--------------</p>
    <script>
        var complex11 = (function () {
            function updatePage12(srcValue) {
                complexRoot.updatePage12(srcValue);
            }

            function complex111UpdateInputValue() {
                complex111.updateInputValue();
            }
            return {
                updateChildValue: complex111UpdateInputValue,
                updatePage12: updatePage12
            }
        })();

    </script>
</div>
