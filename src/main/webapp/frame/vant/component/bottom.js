define(['vue','vant', 'redirect'], function(Vue, vant, redirect){
    Vue.component('bottom', {
        props: {
            active : {default : -1}
        },

        data : function(){
            return {

            }
        },

        template: `
            <van-tabbar v-model="active" :z-index="99" active-color="#57be6a" style="padding-top:0.5rem;padding-bottom: 0.5rem;background-color: #141516">
                <van-tabbar-item icon="home-o" url="/phone/home.jsp">
                    <template #icon>
                        <van-icon name="wap-home" size="2rem" />
                    </template>
                    <div :style="active == 0 ? 'color : #57be6a' : ''">首页</div>
                </van-tabbar-item>
                <van-tabbar-item icon="search" url='/biz/common/msglist_query.jsp'>
                    <template #icon>
                        <van-icon name="chat-o" size="2rem" />
                    </template>
                    <div :style="active == 1 ? 'color : #57be6a' : ''">消息</div>
                </van-tabbar-item>
                <van-tabbar-item icon="friends-o" url="/phone/setting.html">
                    <template #icon>
                        <van-icon name="setting-o" size="2rem"/>
                    </template>
                    <div :style="active == 2 ? 'color : #57be6a' : ''">工具</div>
                </van-tabbar-item>
            </van-tabbar>
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