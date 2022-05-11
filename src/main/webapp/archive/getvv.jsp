<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>test get form value after vue</title>
    <script type="text/javascript" src="../js/vue.js"></script>
    <script type="text/javascript" src="../js/jquery.js"></script>

</head>
<body>
<form action="#" name="f1">
    <div id="app">
    </div>
</form>

</body>
<script>
    console.log("new vue start")
    var v = new Vue({
        el: '#app',
        data: function () {
            return {
                name: "xiaoming",
                age: 18,
                address: "beijing"
            }
        },
        template: `
<div>
    <input type="text" name="name" :value="name">
    <input type="text" name="age" :value="age">
    <input type="text" name="address" :value="address">
</div>
`,
        mounted: function () {
            console.log("mounted")
        },
        created: function () {
            console.log("created start")
            this.age = 25;

            var that = this;
            // setTimeout(function () {
            //     that.age = 100;
            // },0);

            this.$nextTick(function () {
                that.age = 50;
            });

            console.log("created end")
        },
        methods: {
            getAge: function () {
                return this.age;
            },
        }
    })

    console.log("new vue end")

    $(function () {
        console.log("~~~~~~~jquery ready start")
        console.log("form xiaoming age = " + f1.age.value)
        console.log("vue xiaoming age = " + v.age)
        console.log("~~~~~~~jquery ready end")
    });


    console.log("~~~~~~~after new vue start")
    console.log("form xiaoming age = " + f1.age.value)
    console.log("vue xiaoming age = " + v.age)
    console.log("~~~~~~~after new vue end")

</script>
</html>
