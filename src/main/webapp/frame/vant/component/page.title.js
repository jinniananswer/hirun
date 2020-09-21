define(['vue','vant', 'redirect'], function(Vue, vant, redirect){
    Vue.component('page-title', {
        props: ['title'],

        data : function(){
            return {

            }
        },

        template: `
            <van-nav-bar
                    :title="title"
                    :left-arrow="true"
                    @click-left="back"
            />
            `,

        methods: {
            back : function() {
                redirect.toHome();
            }
        },

        watch: {

        },

        mounted () {

        }
    });


})