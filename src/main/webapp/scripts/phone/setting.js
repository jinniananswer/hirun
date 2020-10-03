require(['vue', 'vant', 'ajax', 'vant-select', 'page-title', 'redirect', 'bottom'], function (Vue, vant, ajax, vantSelect, pageTitle, redirect, bottom) {
    let vm = new Vue({
        el: '#app',
        template: `
            <div>
                <page-title title="设置"/>
                <van-cell-group title="工具箱">
                    <van-cell title="密码变更" is-link url="/biz/organization/personnel/change_password.jsp"/>
                    <van-cell title="通讯录" is-link url="/biz/organization/personnel/contacts.jsp"/>
                    <van-cell title="退出登陆" is-link url="/out"/>
                </van-cell-group>
                <bottom :active="2"></bottom>
            </div>`,
        data: function () {
            return {
                active : 2
            }
        },
        methods: {
            submit: function() {

            }
        },
        mounted () {

        }
    });
    return vm;
})