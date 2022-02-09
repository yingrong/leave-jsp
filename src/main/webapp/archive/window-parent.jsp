<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>parent window</title>
</head>

<body>

<div>
    <form name="parentForm" action="">
        <label>
            <input name="paramString" type="text" value="parent input value"/>
        </label>
        <input type="button" onclick="openAtCurrentWindow()" value="open sub page at current window"/>
        <input type="button" onclick="openAtNewWindow()" value="open sub page at new window"/>
        <input type="button" onclick="closeSubPage()" value="close opened sub page at new window"/>
    </form>
</div>
<script>
    function openAtCurrentWindow() {
        window.open("/window-sub.jsp","_self");
    }
    function openAtNewWindow() {
        let subPageWindow = window.open("/window-sub.jsp", "_blank");
        subPageWindow.paramString = parentForm.paramString.value;
        window.subPageWindow = subPageWindow;
    }

    function closeSubPage() {
        if (window.subPageWindow && !window.subPageWindow.closed) {
            window.subPageWindow.close();
        }

    }
</script>
</body>
</html>
