require(['vue', 'vant', 'ajax', 'vant-select', 'page-title', 'redirect'], function (Vue, vant, ajax, vantSelect, pageTitle, redirect) {
    let vm = new Vue({
        el: '#app',
        template: `
            <div>
                <page-title title="我的任务"/>
                <van-tabs v-model="active">
                    <van-tab title="当前任务">
                        <van-cell-group title="产品类">
                            <van-cell title="家装知识学习任务" is-link label="描述信息" url="/biz/college/task/task_detail.html"/>
                            <van-cell title="产品知识学习任务" is-link label="描述信息" />
                        </van-cell-group>
                    </van-tab>
                    <van-tab title="明日任务">
                        <van-cell-group title="产品类">
                            <van-cell title="企业文化学习任务" is-link label="描述信息" />
                            <van-cell title="客户服务学习任务" is-link label="描述信息" />
                        </van-cell-group>
                    </van-tab>
                    <van-tab title="已完成">
                        <van-empty image="search" description="还没有完成的任务哦" />
                    </van-tab>
                </van-tabs>
                
            </div>`,
        data: function () {
            return {
                value : '',
                active : 0
            }
        },
        methods: {

        },
        mounted () {

        }
    });
    return vm;
})