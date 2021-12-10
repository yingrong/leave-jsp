<%@ page import="com.tw.ComplexObjectVO" %>
<%@ page import="com.tw.HelloVO" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Complex Object translate demo</title>
    <meta name="description" content="Java 中 复杂对象 转成 Javascript 对象" >
    <%
        ComplexObjectVO complexObjectVO = ComplexObjectVO.getInstance();
        String complexObjString = complexObjectVO.toJson();


    %>
    <script>
        const complexObjectVOStr = '<%=complexObjString%>';
        var complexObjJS = JSON.parse(complexObjectVOStr);

        var amap = complexObjJS.aliasToHelloMap;
        <% for(Map.Entry<String,HelloVO> entry: complexObjectVO.getAliasToHelloMap().entrySet()) {String alias=entry.getKey();
HelloVO helloVO=entry.getValue();
%>
        if (amap.hasOwnProperty("<%=alias%>")) {
            console.log("assert equales <%=alias%> begin")
            console.log(amap["<%=alias%>"].name == '<%=helloVO.getName()%>')
            console.log(amap["<%=alias%>"].subPage1VO.subPage1Str == '<%=helloVO.getSubPage1VO().subPage1Str%>')
            console.log("assert equales <%=alias%>  end")
        }
        <% } %>

        var listInJS = [];
        <% for (int i = 0; i < complexObjectVO.getHelloVOList().size(); i++) {
            %>
            <%--console.log("<%=i%>");--%>
            listInJS.push('<%=complexObjectVO.getHelloVOList().get(i).getName()%>')
        <% } %>

        console.log("list size eq:" + listInJS.length)


    </script>
</head>
<body>

</body>
</html>
