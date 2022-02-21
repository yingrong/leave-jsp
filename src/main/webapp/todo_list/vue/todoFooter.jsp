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