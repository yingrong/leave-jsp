var todoHeaderComponent = {
    data: function () {
        return {
            newTodo: ''
        }
    },
    template: `
    <header class="header">
        <h1>todos</h1>
        <input class="new-todo" placeholder="What needs to be done?" autofocus v-model="newTodo" @keyup.enter="addTodo">
    </header>
    `,
    methods: {
        addTodo: function () {
            var value = this.newTodo && this.newTodo.trim();
            if (!value) {
                return;
            }
            var _this = this;
            $.ajax({
                url: "/todo-list/ajax?sAction=add",
                dataType: "json",
                method: 'post',
                data: {
                    title: this.newTodo
                },
                success: function (todo) {
                    _this.$emit('add-todo', todo);
                }
            })
            this.newTodo = '';
        }
    }
}