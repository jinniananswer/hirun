require(['vue', 'vant', 'ajax', 'vant-select', 'page-title', 'redirect', 'bottom'], function (Vue, vant, ajax, vantSelect, pageTitle, redirect, bottom) {
    let vm = new Vue({
        el: '#app',
        template: `
            <div>
                <page-title title="鸿助手" :needBack="false"></page-title>
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
                
                <bottom :active="0"></bottom>
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
                let jwt = sessionStorage.getItem("hirun-helper-jwt");
                if (undefined == jwt || null == jwt || '' == jwt){
                    let cookie = document.cookie
                    let arr,reg = new RegExp("(^| )auth=([^;]*)(;|$)");
                    if(arr = document.cookie.match(reg)){
                        let auth = arr[2].replace('\"',"").split("@")
                        let userName = auth[0].replace('\"',"");
                        let password = auth[1].replace('\"',"");
                        let param = new URLSearchParams()
                        param.append('username', userName)
                        param.append('password', password)
                        ajax.post('/api/system/auth/login', param,function(resultData){
                            sessionStorage.setItem('hirun-helper-jwt', resultData.jwt); // 保存 token
                            sessionStorage.setItem('hirun-helper-funcCodes', JSON.stringify(resultData.funcCodes)); // 保存功能权限
                        }, function(resultCode, resultInfo){
                            if(resultCode == "HIRUN_LOGIN_000001")
                                vm.$toast({
                                    message : resultInfo,
                                    overlay : true,
                                    type : 'fail',
                                    closeOnClickOverlay : true
                                });
                            else if(resultCode == "HIRUN_LOGIN_000002") {
                                vm.$toast({
                                    message : resultInfo,
                                    overlay : true,
                                    type : 'fail',
                                    closeOnClickOverlay : true
                                });
                            }
                            else{
                                vm.$toast({
                                    message : resultInfo,
                                    overlay : true,
                                    type : 'fail',
                                    closeOnClickOverlay : true
                                });
                            }
                        });
                    }
                }

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