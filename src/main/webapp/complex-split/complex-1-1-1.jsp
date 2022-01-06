<%@ page import="com.tw.ComplexObjectVO" %>
<%@ page import="com.tw.HelloVO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div style="background-color: aqua">
    <h3>level 3</h3>
    <p>page 1-1-1</p>
    <input type="text" id="p111input" value="">
    <input type="button" onclick="updatePage12()" value="update page 1-2 value"/>
</div>
<script>
    function updatePage12() {
        console.log("updatePage12 start")
        form1.complex12input.value = form1.p111input.value;
        console.log("updatePage12 end")
    }

    var complex111 = (function () {
        return {
            updateInputValue: function () {
                form1.p111input.value = 1111;
            }
        }
    })();
</script>
