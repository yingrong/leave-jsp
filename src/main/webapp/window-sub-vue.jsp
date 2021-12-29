<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>sub window</title>
    <script type="text/javascript" src="js/vue.js"></script>
    <script type="text/javascript" src="js/vue-router.js"></script>
</head>
<body>
<div id="app">
    <router-view></router-view>
</div>
<script>
    let subPage = {
        data: function () {
            return {
                subString: 'child input value'
            }
        },
        template: `
    <form name="subForm" action="">
        <label>
            <input name="subString" type="text" :value="subString"/>
        </label>
        <input type="button" @click="changeParentWindowInput" value="change parent window input value"/>
    </form>
        `,
        methods: {
            changeParentWindowInput: function () {
                if (window.opener && !window.opener.closed) {
                    window.opener.parentForm.paramString.value = subForm.subString.value;
                } else {
                    alert("no parent page found !")
                }
            }

        },
        beforeRouteEnter(to, from, next) {
            next(vm => {
                // 通过 `vm` 访问组件实例
                vm.subString = window.paramString;
            })
        }
    }

    Vue.component('sub-page', subPage);

    const routes = [
        { path: '', component: subPage }
    ]
    const router = new VueRouter({
        routes // (缩写) 相当于 routes: routes
    })
    const app = new Vue({
        router:router,
    }).$mount('#app')
</script>
</body>
</html>
