Vue.component('wish-form', {
    props: ['nameProp'],
    data: function () {
        return {
            name: this.nameProp,
            wish: '',
            detail: '',
            believe: 'no'
        }
    },
    template:`
    <form  @submit.prevent="getWishResult" class="form-example">
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
                    _this.$emit('receiveWishNumber', resp.data.wishNumber)
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