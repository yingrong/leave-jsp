<header class="header">
    <h1>todos</h1>
    <input class="new-todo" placeholder="What needs to be done?" autofocus>
</header>
<script>
    (function () {
        var ENTER_KEY = 13;

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
            rootPage.saveTodo(title);
        }

        // init
        $('.new-todo').on('keyup', addTodo);
    })();
</script>