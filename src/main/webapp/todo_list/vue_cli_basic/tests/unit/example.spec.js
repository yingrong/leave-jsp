import { shallowMount } from '@vue/test-utils'
import { render, fireEvent} from "@testing-library/vue";
import HelloWorld from '@/components/HelloWorld.vue'
import axios from "axios";
jest.mock('axios')

describe('HelloWorld.vue', () => {
  it('renders props.msg when passed', () => {
    const msg = 'new message'
    const wrapper = shallowMount(HelloWorld, {
      propsData: { msg }
    })
    expect(wrapper.text()).toMatch(msg)
  })

    beforeEach(() => {
        axios.get.mockResolvedValue('new info');
    });

  it('should renders msg when mounted', async () => {

      const resp = {
          info: "new info"
      }
      // axios.get.mockResolvedValue('new info')

      const { getByText, findByText } = await render(HelloWorld, {
        props: {
          msg: 'new message for testing-library'
        }
      });


      const button = getByText('increment')

      getByText('new message for testing-library')
      findByText('new info')

      await fireEvent.click(button)
      await fireEvent.click(button)
      getByText('Times clicked: 2')
  });
})
