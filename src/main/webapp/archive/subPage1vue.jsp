<script>
    Vue.component('sub-page1', {
        props: [
            'subPage1Str'
        ],
        template: '   <div><p>sub page 1 vue start</p> ' +
            '<div id="subPage1Container"> sub1.subPage1Str =  {{ subPage1Str }} </div> ' +
            '<p>sub page 1 vue end</p> </div>'
    })

</script>
