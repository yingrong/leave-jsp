<%@ page import="com.tw.todo_list.Todo" %>
<%@ page import="java.util.List" %>
<%
    List<Todo> todoList = (List<Todo>) request.getAttribute("todoList");
    boolean hasCompleted = false;
    for (Todo todo : todoList) {
        if (todo.getCompleted()) {
            hasCompleted = true;
            break;
        }
    }
%>
<div id="todoFooterContainer">
        <todo-footer-component :hasCompleted="hasCompletedJs" v-on:delete-completed-todo="deleteCompletedTodo"/>
</div>
<script>
    var hasCompletedJs = <%=hasCompleted%>;
    var todoFooterPage = (function () {

        function deleteCompletedTodo() {
            rootPage.deleteCompleted();
        }

        return {
            _deleteCompletedTodo: deleteCompletedTodo
        }
    })();

    new Vue({
        el:'#todoFooterContainer',
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
                todoFooterPage._deleteCompletedTodo();
            }
        }
    })
</script>