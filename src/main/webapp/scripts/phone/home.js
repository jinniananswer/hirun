require(['vue', 'vant', 'ajax', 'vant-select', 'page-title', 'redirect'], function (Vue, vant, ajax, vantSelect, pageTitle, redirect) {
    let vm = new Vue({
        el: '#app',
        template: `
            <div>
                <van-nav-bar
                    title="鸿助手"
                    :left-arrow="false"
                    :fixed="true"
                    :z-index="99"
                    style="background-color: #141516;padding-top: 0.5em;padding-bottom: 0.5em">
                    <template #title>
                        <div class="van-ellipsis" style="color:#8a8b8b">鸿助手</div>
                    </template>
                </van-nav-bar>
                <van-cell :title="employee.name" center="true" :label="employee.orgName + '/' + employee.jobRoleName"  style="margin-top:3.8rem">
                    <template #icon>
                        <van-image
                            round
                            width="3.5em"
                            height="3.5em"
                            fit="cover"
                            :src="employee.sex == '1'? '/frame/img/male.jpg' : '/frame/img/female.jpg'"
                            style="margin-right: 1em"
                        />
                    </template>
                </van-cell>
                <van-tabs v-model="activeKey" :swipe-threshold="3">
                    <van-tab :title="menu.node.title" v-for="menu in menus">
                        <van-cell-group :title="secondMenu.node.title" v-for="secondMenu in menu.children">
                            <van-grid :column-num="3" square>
                                <van-grid-item :text="childMenu.node.title" v-for="(childMenu, index) in secondMenu.children" @click="openMenu(childMenu.node.menuUrl)">
                                    <template #icon>
                                        <van-icon :name="childMenu.node.iconfont" size="2rem" :color="index % 5 == 0 ? '#59bd6d' : (index % 5 == 1 ? '#ec9f51' : (index % 5 == 2 ? '#417ef1' : (index % 5 == 3 ? '#4faaf8' : '#e5605b')))" />
                                    </template>
                                </van-grid-item>
                            </van-grid>
                        </van-cell-group>
                        <br/>
                        <br/>
                        <br/>
                    </van-tab>
                </van-tabs>
                
                <van-tabbar v-model="active" :z-index="99" style="padding-top:0.5rem;padding-bottom: 0.5rem;background-color: #141516">
                    <van-tabbar-item icon="home-o">
                        <template #icon>
                            <van-icon name="wap-home" size="2rem" color="#57be6a" />
                        </template>
                        <div style="color:#57be6a">首页</div>
                    </van-tabbar-item>
                    <van-tabbar-item icon="search">
                        <template #icon>
                            <van-icon name="chat-o" size="2rem" />
                        </template>
                        消息
                    </van-tabbar-item>
                    <van-tabbar-item icon="friends-o">
                        <template #icon>
                            <van-icon name="setting-o" size="2rem"/>
                        </template>
                        工具
                    </van-tabbar-item>
                </van-tabbar>
            </div>`,
        data: function () {
            return {
                activeKey: 0,
                active: 0,
                employee : {},
                menus: []
            }
        },
        methods: {
            init : function() {
                let that = this;
                ajax.get('api/organization/employee/getLoginEmployee', '', function(data) {
                    that.employee = data;
                });
                ajax.get('api/system/menu/listPhone', '', function(data) {
                    that.menus = data;
                });
            },

            submit : function() {

            },

            openMenu : function(url) {
                if (url.charAt(0) != '/') {
                    url = '/' + url;
                }
                redirect.open(url);
            }
        },
        mounted () {
            this.init();
        }
    });
    return vm;
})