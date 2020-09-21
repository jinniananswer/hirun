require(['vue', 'vant', 'ajax', 'vant-select', 'page-title', 'redirect'], function (Vue, vant, ajax, vantSelect, pageTitle, redirect) {
    let vm = new Vue({
        el: '#app',
        template: `
            <div>
                <page-title title="家装百科"/>
                <van-search
                    v-model="value"
                    shape="round"
                    placeholder="请输入搜索关键词"
                />
                <van-cell style="background-color: #f8f8f8;color:#969799" :center="true" :border="false" is-link title="设计类" value="更多"/>
                <van-cell-group>
                    <van-cell is-link :center="true" border="false">
                        <template #title>
                            <div class="van-multi-ellipsis">设计的精髓</div>
                        </template>
                        <template #label>
                            <van-row>
                                <div class="van-multi-ellipsis--l2">设计的精髓设计的精髓设计的精髓设计的精髓设计的精髓设计的精髓设计的精髓设计的精髓设计的精髓设计的精髓设计的精髓设计的精髓设计的精髓设计的精髓设计的精髓设计的精髓</div>
                            </van-row>
                            <van-row style="padding-top:1em" type="flex" align="bottom" justify="center">
                                <van-col span="6"></van-col>
                                <van-col span="6">
                                    <van-icon name="good-job-o" size="1.2rem"/>999
                                </van-col>
                                <van-col span="6">
                                    <van-icon name="eye-o" size="1.2rem"/> 999
                                </van-col>
                                <van-col span="6">
                                    <van-icon name="star-o" size="1.2rem"/> 999
                                </van-col>
                            </van-row>
                        </template>
                    </van-cell>
                    <van-cell is-link center="true">
                        <template #title>
                            <div class="van-multi-ellipsis">好的平面图长什么样子</div>
                        </template>
                        <template #label>
                            <van-row>
                                <div class="van-multi-ellipsis--l2">好的平面图长什么样子好的平面图长什么样子好的平面图长什么样子好的平面图长什么样子好的平面图长什么样子好的平面图长什么样子好的平面图长什么样子</div>
                            </van-row>
                            <van-row style="padding-top:1em" type="flex" align="bottom" justify="center">
                                <van-col span="6"></van-col>
                                <van-col span="6">
                                    <van-icon name="good-job-o" size="1.2rem"/>999
                                </van-col>
                                <van-col span="6">
                                    <van-icon name="eye-o" size="1.2rem"/> 999
                                </van-col>
                                <van-col span="6">
                                    <van-icon name="star-o" size="1.2rem"/> 999
                                </van-col>
                            </van-row>
                        </template>
                    </van-cell>
                </van-cell-group>
                <br/>
                
                <van-cell style="background-color: #f8f8f8;color:#969799" :center="true" :border="false" is-link title="基础工程" value="更多"/>
                <van-cell-group>
                    <van-cell title="基础施工的标准流程" is-link label="描述信息" />
                    <van-cell title="水电验收标准" is-link label="描述信息" />
                </van-cell-group>
                
                <van-cell style="background-color: #f8f8f8;color:#969799" :center="true" :border="false" is-link title="软装工程" value="更多"/>
                <van-cell-group>
                    <van-cell title="软装的搭配哲学" is-link label="描述信息" />
                    <van-cell title="软装材料详解" is-link label="描述信息" />
                </van-cell-group>
            </div>`,
        data: function () {
            return {
                value : ''
            }
        },
        methods: {

        },
        mounted () {

        }
    });
    return vm;
})