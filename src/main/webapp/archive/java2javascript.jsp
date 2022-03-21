<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>演示Java变量转为等价的Javascript变量</title>
    <%
        Map map = new HashMap();

        map.put("boolean1", false);
        map.put("simpleString", "1234tsdvbsff森'");
        map.put("htmlString", "</p><p><strong>1223</strong>'</p>");
        map.put("multiLine", "111\r\n222\n333\r444");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(map);

        String encodedJsonString = URLEncoder.encode(jsonString, "utf-8");

    %>
</head>
<body>
<hr>
jsonString:
<%=jsonString%>
<hr>
encodedJsonString:
<%=encodedJsonString%>
<hr>
hidden jsonString input:
<input type="hidden" value="<%=jsonString%>">
<hr>
hidden encodedJsonString input:
<input id="encodeJsonStringContainer" type="hidden" value="<%=encodedJsonString%>">
<hr>
<script>
    var jsonInJs = JSON.parse(decodeURIComponent(encodeJsonStringContainer.value))
    console.log(jsonInJs);

    var jsJson = JSON.parse("<%=jsonString%>"); // error
</script>
</body>
</html>
