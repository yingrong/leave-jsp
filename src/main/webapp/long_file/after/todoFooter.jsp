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
    <button class="clear-completed" onclick="todoFooterPage.deleteCompletedTodo()">Clear completed</button>
    <%}%>
</footer>
<script>
    var todoFooterPage = (function () {

        function deleteCompletedTodo() {
            todoForm.sAction.value = "deleteCompleted"
            todoForm.submit();
        }

        return {
            _deleteCompletedTodo: deleteCompletedTodo
        }
    })();
</script>