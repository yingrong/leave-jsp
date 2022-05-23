var inputTextTagComponent = {
    props: {
        id: String,
        name: String,
        cb: Function // 通过props传递回调函数
    },
    template: `
        <input type="text" :id="id" :name="name" @blur="myVueOnblur" >
        `,
    methods: {
        myVueOnblur: function () {
            this.cb();
        },
    }
}
