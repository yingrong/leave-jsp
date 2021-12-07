    Vue.component('form-demo1', {
        data: function () {
            return {
                checkedVehicle: ['Bike']
            }
        },
        props: [
            'formData'
        ],
        template: `
<div>

<p>------</p>
<input type="checkbox" id="vehicle1" v-model="checkedVehicle" value="Bike" v-on:change="checkboxChange"> I have a bike<br>
<input type="checkbox" id="vehicle2" v-model="checkedVehicle" value="Car" checked> I have a car<br>

<p>------</p>
  First name: <input type="text" v-model="formData.fName" name="fname"><br>
  Last name: <input type="text" v-model="formData.lName" name="lname"><br>
<p>------</p>
  <input type="submit" value="submit">
<div @click="userSubmit" style="
    font-style: ;
    font-variant-ligatures: ;
    font-variant-caps: ;
    font-variant-numeric: ;
    font-variant-east-asian: ;
    font-weight: ;
    font-stretch: ;
    font-size: ;
    font-family: ;
    text-rendering: auto;
letter-spacing: normal;
    word-spacing: normal;
    line-height: normal;
    text-transform: none;
    text-indent: 0px;
    text-shadow: none;
    display: inline-block;

appearance: auto
    user-select: none;
    white-space: pre;
    align-items: flex-start;
    text-align: center;
    cursor: default;
    box-sizing: border-box;
    padding: 1px 6px;
    border-width: 2px;
    border-style: outset;
    border-image: initial;">提交</div>

</div>

        `,
        methods: {
            userSubmit: function () {
                this.$emit('change-text');
            },
            checkboxChange: function () {
                var a = this.checkedVehicle;
                alert(a)
                // this.$emit('childChecked');
                this.$emit('child-checked', a);
            }

        }
    })

