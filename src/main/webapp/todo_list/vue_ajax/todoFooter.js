var todoFooterComponent = {
    props: ['hascompleted'],
    template: `
<footer class="footer">
            <button v-show='hascompleted' class="clear-completed" @click="deleteCompletedTodo">Clear completed</button>
</footer>
        `,
    methods: {
        deleteCompletedTodo: function () {
            this.$emit('delete-completed-todo');
        }
    }
}
