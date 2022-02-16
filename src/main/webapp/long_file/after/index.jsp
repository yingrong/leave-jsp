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
    <header class="header">
        <h1>todos</h1>
        <input class="new-todo" placeholder="What needs to be done?" autofocus>
    </header>
    <jsp:include page="todoList.jsp"/>
    <%@ include file="todoFooter.jsp" %>
</section>

<script>
    var ENTER_KEY = 13;
    var ESCAPE_KEY = 27;

    todos = [];

    function addTodo(e) {
        console.log("add new todo start");
        var $input = $(e.target);
        var val = $input.val().trim();
        if (e.which !== ENTER_KEY || !val) {
            return;
        }
        $input.val('');
        saveTodo(val)
    }

    function saveTodo(title) {
        todoForm.sAction.value = "add";
        todoForm.title.value = title;
        todoForm.submit();
    }


    function deleteCompletedTodo() {
        todoForm.sAction.value = "deleteCompleted"
        todoForm.submit();
    }

    // init
    $('.new-todo').on('keyup', addTodo);


    var rootPage = (function () {
        function deleteTodo(id) {
            todoForm.sAction.value = "delete"
            todoForm.id.value = id;
            todoForm.submit();
        }

        function toggleTodo(id, sAction) {
            todoForm.sAction.value = sAction;
            todoForm.id.value = id;
            todoForm.submit();
        }

        return {
            toggleTodo: toggleTodo,
            deleteTodo: deleteTodo
        }
    })();
</script>
</body>
</html>
