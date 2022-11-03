import {render, fireEvent} from '@testing-library/vue'
import VueRouter from 'vue-router'

import App from './Router/App.vue'
import Home from './Router/Home.vue'
import About from './Router/About.vue'

const routes = [
    {path: '/', component: Home},
    {path: '/about', component: About},
    {path: '*', redirect: '/about'},
]

test('full app rendering/navigating', async () => {
    // Notice how we pass a `routes` object to our render function.
    const {getByTestId, getByText} = render(App, {routes})

    getByText('/')

    await fireEvent.click(getByTestId('about-link'))

    getByText('/about')
})

test('setting initial route', () => {
    // The callback function receives three parameters: the Vue instance where
    // the component is mounted, the store instance (if any) and the router
    // object.
    const {getByTestId, getByText} = render(App, {routes}, (vue, store, router) => {
        router.push('/about')
    })

    getByText('/about')
})

test('can render with an instantiated Vuex store', async () => {
    // Instantiate a router with only one route
    const instantiatedRouter = new VueRouter({
        routes: [{path: '/special-path', component: Home}],
    })

    render(App, {routes: instantiatedRouter}, (vue, store, router) => {
        expect(router.getRoutes()).toHaveLength(1)
        expect(router.getRoutes()[0].path).toEqual('/special-path')
    })
})