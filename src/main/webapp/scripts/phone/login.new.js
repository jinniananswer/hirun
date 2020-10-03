require(['vue', 'vant', 'ajax', 'vant-select', 'page-title', 'redirect'], function (Vue, vant, ajax, vantSelect, pageTitle, redirect) {
    let vm = new Vue({
        el: '#app',
        template: `
            <div>
                <div align="center">
                    <van-image width="15rem" height="15rem" fit="contain" style="margin-top : 6rem;" src="../frame/img/squirrel.png" />
                </div>
                <van-form @submit="onSubmit" style="padding:2rem">
                    <van-cell-group style="background-color: #282333">
                        <van-field size="large" left-icon="user-circle-o" v-model="data.username" label="用户名" placeholder="请输入手机号码">
                            <template #left-icon>
                                <van-icon name="user-circle-o" color="#646566" style="margin-right: 0.4rem"/>
                            </template>
                        </van-field>
                        <van-field size="large" left-icon="closed-eye" v-model="data.password" type="password" label="密码" placeholder="请输入登陆密码" >
                            <template #left-icon>
                                <van-icon name="closed-eye" color="#646566" style="margin-right: 0.4rem"/>
                            </template>
                        </van-field>
                        <van-field size="large" name="checkbox" label="自动登陆">
                            <template #input>
                                <van-checkbox v-model="checked" checked-color="#07c160" />
                            </template>
                            <template #left-icon>
                                <van-icon name="location-o" color="#646566" style="margin-right: 0.4rem"/>
                            </template>
                        </van-field>
                        
                    </van-cell-group>
                    <div style="background-color: #282333;margin:16px;">
                        <van-button round block type="primary" native-type="submit">
                          登陆
                        </van-button>
                    </div>
                </van-form>
            </div>`,
        data: function () {
            return {
                data : {
                  username : '',
                  password : ''
                },
                checked : true
            }
        },
        methods: {
            onSubmit : function() {
                ajax.post('login', this.data, function(response) {
                   alert(JSON.stringify(response));
                });
            },

            switchOption : function(value) {
                this.option = value;
            },

            init : function() {

            }
        },
        mounted () {
            this.init();
        }
    });
    return vm;
})