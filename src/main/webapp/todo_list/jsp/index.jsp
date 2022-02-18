<%@ page import="com.tw.todo_list.Todo" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Todo List, 重构前</title>
    <script type="text/javascript" src="../../js/jquery.js"></script>
    <link rel="stylesheet" href="/todo_list/common/index.css">
</head>
<body>
<%
    List<Todo> todoList = (List<Todo>) request.getAttribute("todoList");
%>
<section class="todoapp">
    <form name="todoForm" action="" method="post">
        <input type="hidden" name="sAction"/>
        <input type="hidden" name="title"/>
        <input type="hidden" name="id"/>
    </form>
    <header class="header">
        <h1>todos</h1>
        <input class="new-todo" placeholder="What needs to be done?" autofocus>
    </header>
    <section class="main">
        <input id="toggle-all" class="toggle-all" type="checkbox">
        <label for="toggle-all">Mark all as complete</label>
        <ul class="todo-list">
            <% for (int i = 0; i < todoList.size(); i++) {
                Todo todo = todoList.get(i);
            %>
            <li <%if (todo.getCompleted()) {%> class="completed"<%}%> data-id="<%=todo.getId()%>">
                <div class="view" id="todo_<%=todo.getId()%>">
                    <input class="toggle" id="todo_toggle_<%=todo.getId()%>" onchange="toogle(this)"
                           type="checkbox" <%if (todo.getCompleted()) {%> checked <%}%> />
                    <label><%=todo.getTitle()%>
                    </label>
                    <button class="destroy" onclick="deleteTodo(this)"></button>
                </div>
                <%--            <input class="edit" value="<%=todo.getTitle()%>">--%>
            </li>
            <%}%>
        </ul>
    </section>
    <footer class="footer">
        <%
            boolean hasCompleted = false;
            for (Todo todo : todoList) {
                if (todo.getCompleted()) {
                    hasCompleted = true;
                    break;
                }
            }
        %>
        <%if(hasCompleted) {%>
           <button class="clear-completed" onclick="deleteCompletedTodo()">Clear completed</button>
        <%}%>
    </footer>
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

    function toogle(el) {
        console.log(el)
        var completed = el.checked;
        if (completed) {
            todoForm.sAction.value = "markDone";
        } else {
            todoForm.sAction.value = "markUnfinished";
        }
        todoForm.id.value = el.id.substr(12);
        todoForm.submit();
    }

    function deleteTodo(el) {
        var id = el.parentElement.id.substr(5);
        todoForm.sAction.value = "delete"
        todoForm.id.value = id;
        todoForm.submit();
    }

    function deleteCompletedTodo() {
        todoForm.sAction.value = "deleteCompleted"
        todoForm.submit();
    }

    // init
    $('.new-todo').on('keyup', addTodo);
</script>
</body>
</html>
