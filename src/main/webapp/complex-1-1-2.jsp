<%@ page import="com.tw.ComplexObjectVO" %>
<%@ page import="com.tw.HelloVO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setAttribute("complex-0-1-2", "complex-1-1-2.jsp");
%>
<div style="background-color: aquamarine">
    <h3>level 0-1-1</h3>
    <p>page 0-1-2</p>
    <p> complex-0-1-2.jsp set attribute complex-0-1-2 to "complex-0-1-2.jsp"</p>
</div>
<script>
    function updateP111() {
        console.log("updateP111 start")
        form1.p111input.value = 1111;
        console.log("updateP111 end")
    }
</script>
