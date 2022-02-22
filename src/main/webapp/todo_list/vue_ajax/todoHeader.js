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
            this.$emit('add-todo', value);
        }
    }
}