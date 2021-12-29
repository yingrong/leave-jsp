<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>parent window</title>
    <script type="text/javascript" src="js/vue.js"></script>
    <script type="text/javascript" src="js/vue-router.js"></script>
</head>

<body>

<div id="app">
    <router-view></router-view>
</div>
<script>
    let parentPage = {
        template: `
    <form name="parentForm" action="">
        <label>
            <input name="paramString" type="text" value="parent input value"/>
        </label>
        <input type="button" @click="openAtCurrentWindow" value="open sub page at current window"/>
        <input type="button" @click="openAtNewWindow" value="open sub page at new window"/>
        <input type="button" @click="closeSubPage" value="close opened sub page at new window"/>
    </form>
        `,
        methods: {
            openAtCurrentWindow: function () {
                window.open("/window-sub-vue.jsp","_self");
            },
            openAtNewWindow: function () {
                let subPageWindow = window.open("/window-sub-vue.jsp", "_blank");
                subPageWindow.paramString = parentForm.paramString.value;
                window.subPageWindow = subPageWindow;
            },
            closeSubPage: function () {
                if (window.subPageWindow && !window.subPageWindow.closed) {
                    window.subPageWindow.close();
                }
            }
        }
    }
    Vue.component('parent-page', parentPage);
</script>
<script>
    const routes = [
        { path: '/window-parent-vue.jsp', component: parentPage }
        // { name:"subPage", path: '/sub-page', component: subPage, props: true}
    ]
    const router = new VueRouter({
        mode: 'history',
        routes: routes
    })
    const app = new Vue({
        router:router,
    }).$mount('#app')

</script>
</body>
</html>
