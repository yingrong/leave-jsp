<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>submit data with jsp</title>
    <script type="text/javascript" src="js/vue.js"></script>
    <script type="text/javascript" src="js/axios.js"></script>
    <script type="text/javascript" src="js/vue-router.js"></script>
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
    <router-view></router-view>
</div>
<script type="text/javascript" src="wish-result.js"></script>
<script type="text/javascript" src="wish-form.js"></script>
<script>
    const routes = [
        { path: '/wish-form', component: wishForm },
        { name:"wishResult", path: '/wish-result', component: wishResult, props: true}
        // { name:"wishResult", path: '/wish-result', component: wishResult, props: route => ({ wishNumber: route.params.wishNumber })}
    ]
    const router = new VueRouter({
        routes // (缩写) 相当于 routes: routes
    })
    const app = new Vue({
        router:router,
        // data: {
        //     wishNumber: 0
        // },
        // computed: {
        //     hasWishNumber: function () {
        //         return this.wishNumber !== 0;
        //     }
        // },
        created: function () {
            let uri = window.location.search.substring(1);
            let params = new URLSearchParams(uri);
            this.name = params.get("name");
            console.log(this.$route.path)
            if (this.$route.path != '/wish-form') {
                this.$router.replace({path:"wish-form", params:{name: this.name}});
            }
        },
    }).$mount('#app')
//     new Vue({
//         el: '#app',
//         router: router,
//         data: {
//             wishNumber: 0
//         },
//         computed: {
//             hasWishNumber: function () {
//                 return this.wishNumber !== 0;
//             }
//         },
//         created: function () {
//             let uri = window.location.search.substring(1);
//             let params = new URLSearchParams(uri);
//             this.name = params.get("name");
//             this.$router.push({path:"wish-form", params:{name: this.name}})
//         },
//         template: `
// <div>
//     <router-view></router-view>
// <!--    <wish-form v-if="!hasWishNumber" :name-prop="name" @receiveWishNumber="receiveWishNumber"></wish-form>-->
// <!--    <wish-result :wish-number="wishNumber" v-if="hasWishNumber"></wish-result>-->
// </div>
// `,
//         methods: {
//             receiveWishNumber: function (wishNumber) {
//                 this.wishNumber = wishNumber;
//                 this.$router.push({path:'wish-vue', params:{wishNumber: wishNumber}})
//                 // this.$router.push('wish-vue')
//             }
//         }
//     })
</script>
</body>
</html>
