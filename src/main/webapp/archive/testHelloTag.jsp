<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="p" uri="/WEB-INF/taglib/hello.tld" %>
<%@taglib prefix="pi" uri="/WEB-INF/taglib/inputText.tld" %>
<html>
<head>
    <title>test jsp customer tag, Hello</title>
    <script type="text/javascript" src="../js/vue.js"></script>

</head>
<body>

<p:Hello/>

<hr>
<span>input text will alert on "onblur" event: </span>
<pi:InputText id="id1" name="name1" onblur="myOnblur()"></pi:InputText>
<hr>
<span>iput text will alert on "onblur" event(vue):</span>
<div id="it"></div>
</body>

<script>
    function myOnblur() {
        alert("my onblur !")
    }

    new Vue({
        el: "#it",
        data: function () {
            return {
                id: "id2",
                name: "name2"
            }
        },
        template: `
        <input type="text" :id="id" :name="name" @blur="myVueOnblur" >
        `,
        methods: {
            myVueOnblur: function () {
                alert("my vue onblur !")
            },
        }
    })
</script>
</html>
