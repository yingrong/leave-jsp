<%@ page import="java.lang.reflect.Array" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    class Entity {
        private long id;
        private String name;

        public Entity(long id, String name) {
            this.id = id;
            this.name = name;
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    Entity product = new Entity(10010, "百万医疗保险");
    List<Entity> liabilities = Arrays.asList(
            new Entity(201, "意外责任"),
            new Entity(202, "非意外责任"),
            new Entity(203, "已赔付过责任"),
            new Entity(204, "意外责任附属责任"));

%>

<html>
<head>
    <title>长函数拆分重构前</title>
</head>
<body>
<h1 id="product_<%=product.getId()%>"><%=product.getName()%>
</h1>
<h2>责任列表</h2>
<table border="1px">
    <tr>
        <th>是否理赔</th>
        <th>责任名称</th>
        <th>赔偿金额</th>
    </tr>
    <%for (int i = 0; i < liabilities.size(); i++) {%>
    <tr>
        <td><input id="liability_<%=liabilities.get(i).getId()%>" type="checkbox"
                   onclick="clickLiabCheck(this, <%=product.getId()%>, <%=liabilities.get(i).getId()%>)"></td>
        <td><span><%=liabilities.get(i).getName()%></span></td>
        <td><input id="liability_<%=liabilities.get(i).getId()%>_amount"></td>
    </tr>
    <%}%>
</table>

<h2>勾选责任进行理赔，规则如下：</h2>
<ul>
    <li>
        <h3>勾选责任时：</h3>
        <ul>
            <li>赔偿金额不能为空</li>
            <li>某些责任不能同时赔付，比如"意外责任"和"非意外责任"</li>
            <li>曾经赔付过的某些责任不能继续赔付</li>
            <li>发送请求，存库</li>
        </ul>
    </li>
    <li>
        <h3>取消勾选责任时：</h3>
        <ul>
            <li>需清空理赔金额</li>
            <li>某如果附属责任取消理赔，则对应的主责任取消理赔</li>
            <li>发送请求，存库</li>
        </ul>
    </li>
</ul>

<script>

    function getMutexedLiability(productId, liabilityId) {
        if (productId === 10010 && liabilityId === 201) {
            return 202;
        }

        if (productId === 10010 && liabilityId === 202) {
            return 201;
        }

        if (productId === 10086 && liabilityId === 901) {
            return 902;
        }

        if (productId === 10010 && liabilityId === 902) {
            return 901;
        }
    }

    function getProductName(productId) {
        if (productId === 10010) {
            return "百万医疗保险";
        }

        if(productId === 100186) {
            return "重疾保险";
        }
    }

    function getLiabilityName(liabilityId) {
        if (liabilityId === 201) {
            return "意外责任";
        }
        if (liabilityId === 202) {
            return "非意外责任";
        }

        if (liabilityId === 901) {
            return "轻症责任";
        }

        if (liabilityId === 202) {
            return "重症责任";
        }
    }

    function validateMutexLiability(liabilityCheckedObject, productId, liabilityId) {
        var mutextedLiability;
        var checkBox;

        if ((productId === 10010 && (liabilityId === 201 || liabilityId === 202)) ||
            (productId === 10086 && (liabilityId === 901 || liabilityId === 902))) {
            mutextedLiability = getMutexedLiability(productId, liabilityId);
            checkBox = document.getElementById("liability_" + mutextedLiability);
            if (checkBox && checkBox.checked) {
                alert("在" + getProductName(productId) + "中，" + getLiabilityName(liabilityId) + "和" + getLiabilityName(mutextedLiability) + "不能同时赔付！");
                liabilityCheckedObject.checked = false;
                return false;
            }
        }

        return true;
    }

    function clickLiabCheck(liabilityCheckedObject, productId, liabilityId) {
        if (liabilityCheckedObject.checked) {
            var amountInput = document.getElementById("liability_" + liabilityId + "_amount");
            if (amountInput.value === "") {
                alert("必须先填入理赔金额！");
                liabilityCheckedObject.checked = false;
                return;
            }

            if (!validateMutexLiability(liabilityCheckedObject, productId, liabilityId)) {
                return;
            }

            var result = payLiability(liabilityCheckedObject, productId, liabilityId);
            if (!result.success) {
                alert(result.errorMessage);
                amountInput.value = "";
                liabilityCheckedObject.checked = false;
                return;
            }

            return;
        } else {
            var amountInput = document.getElementById("liability_" + liabilityId + "_amount");
            amountInput.value = "";
            liabilityCheckedObject.checked = false;

            if (productId === 10010 && liabilityId === 204) {
                var checkBox201 = document.getElementById("liability_201");
                if (checkBox201 && checkBox201.checked) {
                    checkBox201.click();
                }
            }

            if (productId === 10086 && liabilityId === 999) {
                var checkBox998 = document.getElementById("liability_998");
                if (checkBox998 && checkBox998.checked) {
                    checkBox998.click();
                }
            }

            unPayLiability(liabilityCheckedObject, productId, liabilityId);
            return;
        }

        return;
    }

    function payLiability(liablityCheckedObject, productId, liabilityId) {
        if (productId == 10010 && liabilityId == 203) {
            console.log('Check Liability with request:' + JSON.stringify({productId, liabilityId}) + " Failed.");

            return {
                success: false,
                errorMessage: "该责任已经赔付过，不可再次赔付"
            }
        }

        console.log('Check Liability with request:' + JSON.stringify({productId, liabilityId}) + " Successful.");
        return {success: true};
    }

    function unPayLiability(liablityCheckedObject, productId, liabilityId) {
        console.log('Uncheck liability with request:' + JSON.stringify({productId, liabilityId}) + " Successful.");
    }

</script>
</body>
</html>
