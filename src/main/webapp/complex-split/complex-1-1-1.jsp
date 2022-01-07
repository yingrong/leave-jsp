<%@ page import="com.tw.ComplexObjectVO" %>
<%@ page import="com.tw.HelloVO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div style="background-color: aqua">
    <h3>level 3</h3>
    <p>page 1-1-1</p>
    <input type="text" id="p111input" value="">
    <input type="button" onclick="complex111._updatePage12(complex111._getInputValue())" value="update page 1-2 value"/>
</div>
<script>

    var complex111 = (function () {

        function getInputValue() {
            return form1.p111input.value;
        }

        function getUpdatePage12() {
            return complex11.updatePage12(this.getInputValue());
        }

        return {
            _getInputValue: getInputValue,
            _updatePage12: getUpdatePage12,
            updateInputValue: function () {
                form1.p111input.value = 1111;
            }
        }
    })();
</script>
