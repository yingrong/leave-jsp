<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="p" uri="/WEB-INF/taglib/hello.tld" %>
<%@taglib prefix="pi" uri="/WEB-INF/taglib/inputText.tld" %>
<html>
<head>
    <title>test jsp customer tag, Hello</title>
</head>
<body>

<p:Hello/>

<hr>
<span>input text will alert on "onblur" event: </span>
<pi:InputText id="id1" name="name1" onblur="myOnblur()"></pi:InputText>
</body>

<script>
    function myOnblur() {
        alert("my onblur !")
    }
</script>
</html>
