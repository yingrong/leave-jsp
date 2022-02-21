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
    <div id="todoHeaderContainer">
        <todo-header-component v-on:add-todo="saveTodo"/>
    </div>

    <div id="todoListContainer">
        <todo-list-component :todos="todos" v-on:toggle-todo="toggleCompleted" v-on:delete-todo="deleteTodo" />
    </div>

    <div id="todoFooterContainer">
        <todo-footer-component :hasCompleted="hasCompletedJs" v-on:delete-completed-todo="deleteCompletedTodo"/>
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
    (function () {

        function saveTodo(title) {
            rootPage.saveTodo(title);
        }

        new Vue({
            el: "#todoHeaderContainer",
            components: {
                'todo-header-component': todoHeaderComponent
            },
            methods: {
                saveTodo: function (title) {
                    saveTodo(title)
                }
            }
        })
    })();
</script>
<script>
    (function () {
        var todos = JSON.parse('<%=todoListString%>');

        new Vue({
            el: "#todoListContainer",
            data: function () {
                return {
                    todos
                }
            },
            components: {
                'todo-list-component': todoListComponent
            },
            methods: {
                toggleCompleted: function (id, sAction) {
                    rootPage.toggleTodo(id, sAction);
                },
                deleteTodo: function (id) {
                    rootPage.deleteTodo(id);
                }
            }
        });
    })();
</script>
<script>
    (function () {
        var hasCompletedJs = <%=hasCompleted%>;
        new Vue({
            el: '#todoFooterContainer',
            data: function () {
                return {
                    hasCompletedJs
                }
            },
            components: {
                'todo-footer-component': todoFooterComponent
            },
            methods: {
                deleteCompletedTodo: function () {
                    rootPage.deleteCompleted();
                }
            }
        })
    })();

</script>
</body>
</html>
