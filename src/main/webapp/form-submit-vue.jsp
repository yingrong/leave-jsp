<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>submit data with jsp</title>
    <script type="text/javascript" src="js/vue.js"></script>
    <script type="text/javascript" src="js/axios.js"></script>
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
<div id="form-hook">
</div>
<script type="text/javascript" src="receive-wish.js"></script>
<script>

    new Vue({
        el: '#form-hook',
        data: {
            name,
            wish: '',
            detail: '',
            believe: 'no',
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
    <form v-if="!hasWishNumber" action="/wish-vue" @submit.prevent="getWishResult" method="post" class="form-example">
        <div class="form-example">
            <label for="name">Enter your name: </label>
            <input type="text" v-model="name" name="name" id="name" required>
        </div>
        <div class="form-example">
            <label for="wish">Enter your wish: </label>
            <input type="text" v-model="wish" name="wish" id="wish" required>
        </div>
        <div class="form-example">
            <label for="detail">Enter detail:</label>
            <textarea v-model="detail" name="detail" id="detail" placeholder="more about the wish" cols="30" rows="10"></textarea>
        </div>
        <div class="form-example">
            <p>R U believe?</p>
            <label for="yes"></label><input v-model="believe" name="believe" id="yes" type="radio" value="yes">Yes
            <label for="no"></label><input v-model="believe" name="believe" id="no" type="radio" value="no">No
        </div>
        <div class="form-example">
            <input type="submit" value="submit!">
        </div>
    </form>

    <wish-result :wish-number="wishNumber" v-if="hasWishNumber"></wish-result>
</div>
`,
        methods: {
            getWishResult: function () {
                const data = {
                    name: this.name,
                    wish: this.wish,
                    detail: this.detail,
                    believe: this.believe
                }
                console.log("todo submit data");
                const _this = this; // 解决 this 指向问题(IE兼容)。另一种方案时箭头函数（IE不兼容）
                axios
                    .post("http://localhost:8080/wish-vue", data)
                    .then(function (resp) {
                        console.log(resp.data.wishNumber);
                        _this.wishNumber = resp.data.wishNumber;
                        history.replaceState({}, "", "wish-vue");
                    })
                    .catch(function (error) {
                        if (error.response) {
                            // 请求成功发出且服务器也响应了状态码，但状态代码超出了 2xx 的范围
                            console.log(error.response.data);
                            console.log(error.response.status);
                            console.log(error.response.headers);
                        } else if (error.request) {
                            // 请求已经成功发起，但没有收到响应
                            // `error.request` 在浏览器中是 XMLHttpRequest 的实例，
                            // 而在node.js中是 http.ClientRequest 的实例
                            console.log(error.request);
                        } else {
                            // 发送请求时出了点问题
                            console.log('Error', error.message);
                        }
                        console.log(error.config);
                        _this.clearDataExceptName();
                        var state = {
                            name: _this.name
                        }
                        var url = "form-submit-vue.jsp?name="+state.name;
                        history.pushState(state, "bl", url);
                    });
            },
            clearDataExceptName: function () {
                this.wish = "";
                this.detail = "";
                this.believe = "no";
            }
        }
    })
</script>
</body>
</html>
