<%--
  Created by IntelliJ IDEA.
  User: yingrong.hu
  Date: 2021/12/29
  Time: 3:54 下午
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>sub window</title>
</head>
<body onload="change()">
<div>
    <form name="subForm" action="">
        <label>
            <input name="subString" type="text" value="child input value"/>
        </label>
        <input type="button" onclick="changeParentWindowInput()" value="change parent window input value"/>
    </form>
</div>
<script>
    function change() {
        subForm.subString.value = window.paramString;
    }

    function changeParentWindowInput() {
        if (window.opener && !window.opener.closed) {
            window.opener.parentForm.paramString.value = subForm.subString.value;
        } else {
            alert("no parent page found !")
        }
    }
</script>
</body>
</html>
