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
    <script type="text/javascript" src="./product_and_liability_info.js"></script>
    <script type="text/javascript" src="./liability_row.js"></script>
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
                   onclick="clickLiabCheck(<%=product.getId()%>, <%=liabilities.get(i).getId()%>)"></td>
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
    function validateMutexLiability(productId, liabilityId) {
        var liability = mutexedLiability[productId] && mutexedLiability[productId][liabilityId];
        if (liability) {
            var checkBox = document.getElementById("liability_" + liability);
            if (checkBox && checkBox.checked) {
                return {
                    success: false,
                    errorMessage: "在" + productInfo[productId] + "中，" + liabilityInfo[liabilityId] + "和" + liabilityInfo[liability] + "不能同时赔付！"
                }
            }
        }

        return {success: true};
    }

    function validateEmptyPayAmount(liabilityId) {
        var amountInput = document.getElementById("liability_" + liabilityId + "_amount");
        if (amountInput.value === "") {
            return {
                success: false,
                errorMessage: "必须先填入理赔金额！"
            }
        }

        return {success: true};
    }

    function validatePayingProduct(productId, liabilityId) {
        let result = validateEmptyPayAmount(liabilityId);
        if(!result.success) {
            return result;
        }

        return validateMutexLiability(productId, liabilityId);
    }

    function payLiability(productId, liabilityId) {
        let result = validatePayingProduct(productId, liabilityId);
        if(!result.success) {
            return result;
        }

        return sendPayLiabilityRequest(productId, liabilityId);
    }

    function unPayMainLiability(productId, liabilityId) {
        var theMainLiability = mainLiability[productId] && mainLiability[productId][liabilityId];
        if (theMainLiability) {
            var mainLiabilityElementRow = new LiabilityElementRow(theMainLiability);
            if (mainLiabilityElementRow.isChecked()) {
                unPayLiability(mainLiabilityElementRow, productId, theMainLiability);
            }
        }
    }

    function unPaySingleLiability(liabilityElementRow, productId, liabilityId) {
        liabilityElementRow.setChecked(false);
        liabilityElementRow.clearAmount()
        sendUnPayLiabilityRequest(productId, liabilityId);
    }

    function unPayLiability(liabilityElementRow, productId, liabilityId) {
        unPayMainLiability(productId, liabilityId);
        unPaySingleLiability(liabilityElementRow, productId, liabilityId);
    }

    function clickLiabCheck(productId, liabilityId) {
        var liabilityElementRow = new LiabilityElementRow(liabilityId);
        if(!liabilityElementRow.isExist()) {
            throw Error('该元素不存在')
        }

        if (liabilityElementRow.isChecked()) {
            let result = payLiability(productId, liabilityId);
            if (!result.success) {
                alert(result.errorMessage);
                liabilityElementRow.setChecked(false);
            }
        } else {
            unPayLiability(liabilityElementRow, productId, liabilityId);
        }
    }

    function sendPayLiabilityRequest(productId, liabilityId) {
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

    function sendUnPayLiabilityRequest(productId, liabilityId) {
        console.log('Uncheck liability with request:' + JSON.stringify({productId, liabilityId}) + " Successful.");
    }

</script>
</body>
</html>
