<%@ page import="com.tw.long_file.Todo" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Todo List, 重构前</title>
    <script type="text/javascript" src="../../js/jquery.js"></script>
    <link rel="stylesheet" href="/long_file/common/index.css">
</head>
<body>
<%
    List<Todo> todoList = (List<Todo>) request.getAttribute("todoList");
%>
<section class="todoapp">
    <%@ include file="todoForm.jspf" %>
    <jsp:include page="todoHeader.jsp"/>
    <jsp:include page="todoList.jsp"/>
    <%@ include file="todoFooter.jsp" %>
</section>

<script>
    var rootPage = (function () {

        function saveTodo(title) {
            todoFormPage.saveTodo(title);
        }

        function deleteTodo(id) {
            todoFormPage.deleteTodo(id);
        }

        function toggleTodo(id, sAction) {
            todoFormPage.toggleTodo(id, sAction);
        }

        function deleteCompleted() {
            todoFormPage.deleteCompleted();
        }

        return {
            saveTodo: saveTodo,
            toggleTodo: toggleTodo,
            deleteTodo: deleteTodo,
            deleteCompleted: deleteCompleted
        }
    })();
</script>
</body>
</html>
