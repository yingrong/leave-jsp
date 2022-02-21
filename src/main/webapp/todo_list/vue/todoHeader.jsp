<div id="todoHeaderContainer">
    <todo-header-component v-on:add-todo="saveTodo"/>
</div>
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