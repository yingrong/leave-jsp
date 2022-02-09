<%@ page import="com.tw.archive.ComplexObjectVO" %>
<%@ page import="com.tw.archive.HelloVO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    ComplexObjectVO complexObjectVO = (ComplexObjectVO) request.getAttribute("complexObjectVO");
    String attributeFromRequest = (String) request.getAttribute("complex-1-1-2");
%>
<div style="background-color: chocolate;margin: 10px">
    <h2>level 2</h2>
    <p>page 12</p>
    <p>complexObjectVO from root page, no need to set attribute before include page</p>
    <p>complexObjectVO.helloList.size is <%=complexObjectVO.getHelloVOList().size()%>
    </p>
    <p>attributeFromRequest complex-1-1-2 is <%=attributeFromRequest%>
    </p>
    <input type="text" id="complex12input" value=""/>
</div>
