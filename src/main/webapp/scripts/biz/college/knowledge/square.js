require(['vue', 'vant', 'ajax', 'vant-select', 'page-title', 'redirect'], function (Vue, vant, ajax, vantSelect, pageTitle, redirect) {
    let vm = new Vue({
        el: '#app',
        template: `
            <div>
                <page-title title="家装广场"/>
                <van-search
                    v-model="value"
                    shape="round"
                    placeholder="请输入搜索关键词"
                />
                <div style="margin-top:1em;margin-right:1em;margin-left:1em;">
                    <van-button type="primary" icon="plus" round block url="/biz/college/knowledge/asking.html">我要提问</van-button>
                </div>
                <van-tabs v-model="active">
                    <van-tab title="广场">
                        <van-cell style="background-color: #f8f8f8;color:#969799" :center="true" :border="false" is-link title="设计类" value="更多"/>
                        <van-cell-group>
                            <van-cell is-link :center="true" border="false">
                                <template #title>
                                    <div class="van-multi-ellipsis">什么是风格蓝图？</div>
                                </template>
                                <template #label>
                                    <van-row>
                                        <div class="van-multi-ellipsis--l2">风格蓝图是由三大类28大主题系列构成，每个主题系列代表着风格蓝图是由三大类28大主题系列构成，每个主题系列代表着风格蓝图是由三大类28大主题系列构成，每个主题系列代表着</div>
                                    </van-row>
                                    <van-row style="padding-top:1em" type="flex" align="bottom" justify="center">
                                        <van-col span="6"></van-col>
                                        <van-col span="6">
                                            <van-icon name="good-job-o" size="1.2rem"/>999
                                        </van-col>
                                        <van-col span="6">
                                            <van-icon name="eye-o" size="1.2rem"/> 999
                                        </van-col>
                                    </van-row>
                                </template>
                            </van-cell>
                            <van-cell is-link center="true">
                                <template #title>
                                    <div class="van-multi-ellipsis">什么是功能蓝图？</div>
                                </template>
                                <template #label>
                                    <van-row>
                                        <div class="van-multi-ellipsis--l2">功能蓝图功能蓝图功能蓝图功能蓝图功能蓝图功能蓝图功能蓝图功能蓝图功能蓝图功能蓝图功能蓝图功能蓝图功能蓝图功能蓝图功能蓝图功能蓝图功能蓝图功能蓝图功能蓝图功能蓝图</div>
                                    </van-row>
                                    <van-row style="padding-top:1em" type="flex" align="bottom" justify="center">
                                        <van-col span="6"></van-col>
                                        <van-col span="6">
                                            <van-icon name="good-job-o" size="1.2rem"/>999
                                        </van-col>
                                        <van-col span="6">
                                            <van-icon name="eye-o" size="1.2rem"/> 999
                                        </van-col>
                                    </van-row>
                                </template>
                            </van-cell>
                        </van-cell-group>
                        <br/>
                        <van-cell-group title="产品类">
                            <van-cell title="我司产品主要有哪些？" is-link label="描述信息" />
                            <van-cell title="我司产品的分类？" is-link label="描述信息" />
                        </van-cell-group>
                    </van-tab>
                    <van-tab title="我的问题">
                        <van-cell-group title="产品类">
                            <van-cell title="报销流程是怎样的？" is-link label="描述信息" />
                            <van-cell title="我司的主要竞争对手？" is-link label="描述信息" />
                        </van-cell-group>
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