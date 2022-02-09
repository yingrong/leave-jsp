<%@ page import="com.tw.archive.ComplexObjectVO" %>
<%@ page import="com.tw.archive.HelloVO" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Complex Object translate demo</title>
    <meta name="description" content="Java 中 复杂对象 转成 Javascript 对象">
    <script type="text/javascript" src="../js/vue.js"></script>
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
            const ap= "<%=alias%>";
            console.log(amap[ap].name == '<%=helloVO.getName()%>')
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

<div id="bothBoss">
    <label for="s`">sLabelForSelect</label><span>  </span>
    <select name="selectName" id="s`">
        <% for (HelloVO helloVO : complexObjectVO.getHelloVOList()) {%>
        <option value="<%=helloVO.getName()%>"
                <%if (helloVO.getName().startsWith("C")){%>selected<%}%>><%=helloVO.getName()%>
        </option>
        <%} %>
    </select>
    <hr>
    <div id="vv">
    </div>
</div>
</body>
<script>
    var app4 = new Vue({
        el: '#vv',
        data: function () {
            return {
                hellos: complexObjJS.helloVOList
            }
        },
        template: `
<div>
        <label for="s1">sLabelForSelect VUE</label><span> </span>
        <select name="selectNameVue" id="s1">
            <option :value="h.name" v-for="h in hellos" :selected="h.name.startsWith('d')">{{ h.name }}</option>
        </select>
</div>
        `
    })
</script>
</html>
