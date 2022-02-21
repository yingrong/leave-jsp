<%@ page import="com.tw.todo_list.Todo" %>
<%@ page import="java.util.List" %>
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>

<%
    List<Todo> todoList = (List<Todo>) request.getAttribute("todoList");
    ObjectMapper objectMapper = new ObjectMapper();
    String todoListString = objectMapper.writeValueAsString(todoList);
%>
<div id="todoListContainer">
    <todo-list-component :todos="todos" />
</div>
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
            }
        });
    })();
</script>
