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
    <button class="clear-completed" onclick="todoFooterPage._deleteCompletedTodo()">Clear completed</button>
    <%}%>
</footer>
<script>
    var todoFooterPage = (function () {

        function deleteCompletedTodo() {
            rootPage.deleteCompleted();
        }

        return {
            _deleteCompletedTodo: deleteCompletedTodo
        }
    })();
</script>