var todoFormComponent = {

    props: {
        sAction: String,
        id: String,
        title: String
    },
    template: `
        <form name="todoForm" action="" method="post">
            <input type="hidden" name="sAction" v-model="sAction"/>
            <input type="hidden" name="title" v-model="title"/>
            <input type="hidden" name="id" v-model="id"/>
        </form>`,
    watch: {
        sAction: function () {
            this.$emit("submit");
        }
    }
}