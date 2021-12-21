<%@ page import="com.tw.HelloVO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<script type="text/javascript" src="js/vue.js" ></script>
<script type="text/javascript" src="form-test.js"></script>
<%-- common start--%>
<% HelloVO helloVO = new HelloVO("MyName", true); %>
<%-- common end--%>
<body>
<jsp:include page="subPage1vue.jsp"/>


<div id="app">
    <p>{{ lpText }}</p>
    <form action="index.jsp">

    <form-demo1 :form-data="formData" v-on:child-checked="parentHandle" v-on:change-text="changeText"></form-demo1>
    </form>

    <hr>
    <% request.setAttribute("sub1", helloVO.getSubPage1VO()); %>
    <jsp:include page="subPage1.jsp"/>
    <hr>
    <sub-page1 v-bind:sub-page1-str="subPage1Str"> </sub-page1>
    <hr>
    <%-- use java start--%>
    <% if (helloVO.isShouldShow()) { %>
    <h1>should show <span><%=helloVO.getName()%></span></h1>
    <%}%>
    <h2>Hello World!</h2>
    <p><%=System.currentTimeMillis()%>
    </p>
    <%-- use java end--%>

    <%-- use JS start--%>
    <% if (helloVO.isShouldShow()) { %>
    <h1>should show <span><%=helloVO.getName()%></span></h1>
    <%}%>
    <h2>Hello World!</h2>
    <p><%=System.currentTimeMillis()%>
    </p>
    <%-- use JS end--%>
</div>
</body>

<script type="text/javascript">

    function showName() {
        <% if (helloVO.isShouldShow()) { %>
        console.log("Java:<%=helloVO.getName()%>");
        <%} else {%>
        console.log("Java: no name");
        <%}%>
    }


    showName();
</script>
<script type="text/javascript">
    function HelloVOJS() {
        <%--  TODO  String 类型需要 加上 双引号--%>
        this.getName = "<%=helloVO.getName()%>";
        <%--   TODO   boolean  了行 不需要 加上 双引号--%>
        <%--   TODO   java 方法 执行的结果，转换成 JS 变量--%>
        this.isShouldShow = true;
        <%--= <%=helloVO.isShouldShow()%>;--%>
    }

    const helloVOJS = new HelloVOJS();
    function showNameJS(helloVOJS1) {
        if (helloVOJS1.isShouldShow) {
            <%--     TODO --%>
            console.log("JS:" + helloVOJS1.getName);
        } else {
            console.log("JS:no name");
        }
    }


    showNameJS(helloVOJS);
</script>
<script type="text/javascript">

    var formData = {
        fName :"tian",
        lName : "di"
    }
    var vm = new Vue({
        el: '#app',
        data: {
            subPage1Str: "<%=helloVO.getSubPage1VO().subPage1Str%>",
            formData: formData,
            lpText: "old lpText"
        },
        methods: {
            parentHandle: function (a) {
                console.log("root childChecked")
                // console(a)
                alert("root  " + a[0] )
            },
            changeText: function () {
                this.lpText = "new text";
            }
        }
    })

    vm.subPage1Str = "new subPage1  string"
</script>
</html>
