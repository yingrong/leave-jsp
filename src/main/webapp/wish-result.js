let wishResult = {
    props:[
        'wishNumber' // 注意这里时camelCase，使用时需要kebab-case
    ],
    template: `
<div>
    <h1>your wish submitted!</h1>
    <p>your wish number is: {{this.wishNumber}}</p>
</div>
    `
};
Vue.component('wish-result', wishResult)
