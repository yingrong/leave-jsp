<%@ page import="com.tw.todo_list.Todo" %>
<%@ page import="java.util.List" %>
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Todo List, 重构后</title>
    <script type="text/javascript" src="../../js/jquery.js"></script>
    <script type="text/javascript" src="../../js/vue.js"></script>
    <link rel="stylesheet" href="/todo_list/common/index.css"/>
    <script type="text/javascript" src="/todo_list/vue/todoForm.js"></script>
    <script type="text/javascript" src="/todo_list/vue/todoHeader.js"></script>
    <script type="text/javascript" src="/todo_list/vue/todoList.js"></script>
    <script type="text/javascript" src="/todo_list/vue/todoFooter.js"></script>
</head>
<%
    List<Todo> todoList = (List<Todo>) request.getAttribute("todoList");
    ObjectMapper objectMapper = new ObjectMapper();
    String todoListString = objectMapper.writeValueAsString(todoList);

    boolean hasCompleted = false;
    for (Todo todo : todoList) {
        if (todo.getCompleted()) {
            hasCompleted = true;
            break;
        }
    }
%>
<body>
<section class="todoapp">
    <%@ include file="todoForm.jspf" %>
    <div id="app">
        <todo-header-component v-on:add-todo="saveTodo"></todo-header-component>
        <todo-list-component :todos="todos" v-on:toggle-todo="toggleCompleted" v-on:delete-todo="deleteTodo" >
        </todo-list-component>
        <todo-footer-component :hasCompleted="hasCompletedJs" v-on:delete-completed-todo="deleteCompletedTodo">
        </todo-footer-component>
    </div>
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
<script>
    var todos = JSON.parse('<%=todoListString%>');
    var hasCompletedJs = <%=hasCompleted%>;
    new Vue({
        el: '#app',
        data: function () {
            return {
                todos,
                hasCompletedJs
            }
        },
        components: {
            'todo-header-component': todoHeaderComponent,
            'todo-list-component': todoListComponent,
            'todo-footer-component': todoFooterComponent
        },
        methods: {
            saveTodo: function (title) {
                rootPage.saveTodo(title);
            },
            toggleCompleted: function (id, sAction) {
                rootPage.toggleTodo(id, sAction);
            },
            deleteTodo: function (id) {
                rootPage.deleteTodo(id);
            },
            deleteCompletedTodo: function () {
                rootPage.deleteCompleted();
            }
        }
    })
</script>
</body>
</html>
