// import '@testing-library/jest-dom'
import {render, fireEvent} from '@testing-library/vue'
import Vue from 'vue'
import Vuex from 'vuex'

import VuexTest from '../../src/components/VuexTest'
import {store} from '../../src/store/store'

describe('test vuex', () => {
    it('should render components with vuex', async () => {
        // This is a silly store that can never be changed.
        // eslint-disable-next-line no-shadow
        const store = {
            state: {count: 1000},
            actions: {
                increment: () => jest.fn(),
                decrement: () => jest.fn(),
            },
        }

        // Notice how here we are not using the helper method, because there's no
        // need to do that.
        const { getByText} = render(VuexTest, {store})

        await fireEvent.click(getByText('+'))
        getByText('1000')

        await fireEvent.click(getByText('-'))
        getByText('1000')
    });

    it('should render2', async () => {
        Vue.use(Vuex)

        const { getByText} = render(VuexTest, {
            store: new Vuex.Store({
                state: {count: 3},
                mutations: {
                    increment(state) {
                        state.count++
                    },
                    decrement(state) {
                        state.count--
                    },
                },
                actions: {
                    increment(context) {
                        context.commit('increment')
                    },
                    decrement(context) {
                        context.commit('decrement')
                    },
                },
            }),
        })

        await fireEvent.click(getByText('+'))
        getByText('4')

        await fireEvent.click(getByText('-'))
        getByText('3')
    });
})