var todoListComponent = {
    props:{
        todos: {
            type: Array
        }
    },
    template:`
<section class="main" v-show="todos.length">
    <ul class="todo-list">
        <li v-for="todo in todos" :key="todo.id" :class="{completed: todo.completed}">
            <div class="view">
                <input class="toggle" v-model="todo.completed" type="checkbox" @change="toggleComplted(todo)"/>
                <label>{{todo.title}}</label>
                <button class="destroy" @click="deleteTodo(todo)"></button>
            </div>
        </li>
    </ul>
</section>
            `,
    methods: {
        toggleComplted: function (todo) {
            var sAction = "markDone";
            if (!todo.completed) {
                sAction = "markUnfinished";
            }
            rootPage.toggleTodo(todo.id, sAction);
        },
        deleteTodo: function (todo) {
            rootPage.deleteTodo(todo.id);
        }
    }
}