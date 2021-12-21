<%@ page import="java.util.Optional" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setCharacterEncoding("utf-8");
    String nullableName = request.getParameter("name");
    String name = nullableName == null ? "": nullableName;
%>
<html>
<head>
    <title>submit data with jsp</title>
    <style>
        form.form-example {
            display: table;
        }

        div.form-example {
            display: table-row;
        }

        label, input {
            display: table-cell;
            margin-bottom: 10px;
        }

        label {
            padding-right: 10px;
        }

    </style>
</head>
<body>

<form action="receive-wish.jsp" method="post" class="form-example">
    <div class="form-example">
        <label for="name">Enter your name: </label>
        <input type="text" name="name" id="name" value="<%=name%>" required>
    </div>
    <div class="form-example">
        <label for="wish">Enter your wish: </label>
        <input type="text" name="wish" id="wish" required>
    </div>
    <div class="form-example">
        <label for="detail">Enter detail:</label>
        <textarea name="detail" id="detail" placeholder="more about the wish" cols="30" rows="10"></textarea>
    </div>
    <div class="form-example">
        <p>R U believe?</p>
        <label for="yes"></label><input name="believe" id="yes" type="radio" value="yes">Yes
        <label for="no"></label><input name="believe" id="no" type="radio" value="no" checked>No
    </div>
    <div class="form-example">
        <input type="submit" value="submit!">
    </div>
</form>

</body>
</html>
