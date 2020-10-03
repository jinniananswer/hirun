define(['vue','vant', 'redirect'], function(Vue, vant, redirect){
    Vue.component('page-title', {
        props: {
            title : {
                type : String,
                required : true
            },

            needBack : {
                default : true
            }
        },

        data : function(){
            return {

            }
        },

        template: `
            <van-nav-bar
                    :title="title"
                    style="background-color: #141516;padding-top: 0.5em;padding-bottom: 0.5em"
                    :left-arrow="needBack"
                    :fixed="true"
                    :z-index="99"
                    @click-left="back">
                <template #title>
                    <div class="van-ellipsis" style="color:#8a8b8b">{{title}}</div>
                </template>
            </van-nav-bar>
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