<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>submit data with jsp</title>
    <script type="text/javascript" src="../js/vue.js"></script>
    <script type="text/javascript" src="../js/axios.js"></script>
    <style>
        form.form-example {
            display: table;
        }

        div.form-example {
            display: table-row;
        }

        label, input {
            display: table-cell;
            margin-bottom: 10px;
        }

        label {
            padding-right: 10px;
        }

    </style>
</head>
<body>
<div id="app">
</div>
<script type="text/javascript" src="wish-result.js"></script>
<script type="text/javascript" src="wish-form.js"></script>
<script>

    new Vue({
        el: '#app',
        data: {
            wishNumber: 0
        },
        computed: {
            hasWishNumber: function () {
                return this.wishNumber !== 0;
            }
        },
        created: function () {
            let uri = window.location.search.substring(1);
            let params = new URLSearchParams(uri);
            this.name = params.get("name");
        },
        template: `
<div>
    <wish-form v-if="!hasWishNumber" :name-prop="name" @receiveWishNumber="receiveWishNumber"></wish-form>
    <wish-result :wish-number="wishNumber" v-if="hasWishNumber"></wish-result>
</div>
`,
        methods: {
            receiveWishNumber: function (wishNumber) {
                this.wishNumber = wishNumber;
            }
        }
    })
</script>
</body>
</html>
