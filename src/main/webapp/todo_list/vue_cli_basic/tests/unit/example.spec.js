import { shallowMount } from '@vue/test-utils'
import { render} from "@testing-library/vue";
import HelloWorld from '@/components/HelloWorld.vue'

describe('HelloWorld.vue', () => {
  it('renders props.msg when passed', () => {
    const msg = 'new message'
    const wrapper = shallowMount(HelloWorld, {
      propsData: { msg }
    })
    expect(wrapper.text()).toMatch(msg)
  })


  it('should renders msg when mounted', () => {
      const { getByText } = render(HelloWorld, {
        props: {
          msg: 'new message for testing-library'
        }
      });

      getByText('new message for testing-library')

  });
})
