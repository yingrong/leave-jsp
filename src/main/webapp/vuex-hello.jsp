<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Vuex hello world</title>
    <script type="text/javascript" src="js/vue.js"></script>
    <script src="js/http_cdn.jsdelivr.net_npm_es6-promise@4_dist_es6-promise.auto.js"></script>
    <script src="js/http_unpkg.com_vuex@3.6.2_dist_vuex.js"></script>
</head>
<body>
<div id="app">
    <p>{{ count }}</p>
    <p>is negative:</p>
    <p>{{ isNegative }}</p>
    <p>
        <button @click="increment">+</button>
        <button @click="decrement">-</button>
    </p>
</div>
</body>

<script>
    const store = new Vuex.Store({
        state: {
            count: 0
        },
        getters: {
            isNegative: function (state) {
                return state.count < 0;
            }
        },
        mutations: {
            increment: function (state) {
                state.count++
            },
            decrement: function (state) {
                return state.count--;
            }
        }
    })

    new Vue({
        el: '#app',
        store: store,
        computed: {
            count: function () {
                return store.state.count
            },
            isNegative: function () {
                return this.$store.getters.isNegative;
            }
        },
        methods: {
            increment: function () {
                this.$store.commit('increment')
                console.log(this.$store.state.count)
            },
            decrement: function () {
                store.commit('decrement')
            }
        }
    })

</script>
</html>
