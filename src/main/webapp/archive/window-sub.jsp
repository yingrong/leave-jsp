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
