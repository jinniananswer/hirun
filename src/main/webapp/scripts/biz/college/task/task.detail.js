require(['vue', 'vant', 'ajax', 'vant-select', 'page-title', 'redirect'], function (Vue, vant, ajax, vantSelect, pageTitle, redirect) {
    let vm = new Vue({
        el: '#app',
        template: `
            <div>
                <page-title title="任务详情"/>
                <van-cell style="background-color: #f8f8f8;color:#969799" :border="false" is-link title="家装知识学习任务" value="评分" @click="showEval"/>
                <van-cell-group>
                    <van-cell border="false" center="true">
                        <template #title>
                            <div class="van-multi-ellipsis">企业文化学习任务</div>
                        </template>
                        <template #right-icon>
                            <van-tag type="danger">已延期</van-tag>
                        </template>
                        <template #label>
                            <van-row>
                                <div class="van-multi-ellipsis--l2">鸿扬家装鸿扬家装鸿扬家装鸿扬家装鸿扬家装鸿扬家装鸿扬家装鸿扬家装鸿扬家装鸿扬家装鸿扬家装鸿扬家装鸿扬家装</div>
                            </van-row>
                        </template>
                    </van-cell>
                    <van-cell title="任务时间">
                        <template #label>
                            <van-progress :percentage="10" style="margin-top:1em"/>
                            <div class="van-multi-ellipsis--l2" style="margin-top:1em">
                                2020-09-19~2020-09-30
                            </div>
                        </template>
                    </van-cell>
                </van-cell-group>
                <van-cell-group title="学习课件">
                    <van-cell title="学习课件1" is-link @click="openFile(1)"></van-cell>
                    <van-cell title="学习课件2" is-link @click="openFile(2)"></van-cell>
                    <van-cell title="学习课件3" is-link @click="openFile(3)"></van-cell>
                    <van-cell title="学习课件4" is-link @click="openFile(4)"></van-cell>
                </van-cell-group>
                <div style="margin-top:1em;margin-right:1em;margin-left:1em;margin-bottom:1em">
                    <van-row :gutter="20">
                        <van-col span="12">
                            <van-button type="primary" icon="plus" round block>我要练习</van-button>
                        </van-col>
                        <van-col span="12">
                            <van-button type="danger" icon="fire-o" round block>我要考试</van-button>
                        </van-col>
                    </van-row>
                </div>
                <van-action-sheet v-model="show" title="标题">
                    <div class="content">
                        <van-cell :center="true" title="难度">
                            <template #right-icon>
                                <van-rate v-model="value" allow-half void-icon="star" void-color="#eee" />
                            </template>
                        </van-cell>
                        <van-cell :center="true" title="讲师">
                            <template #right-icon>
                                <van-rate v-model="value" allow-half void-icon="star" void-color="#eee" />
                            </template>
                        </van-cell>
                        <div style="margin: 16px;">
                            <van-button round block type="info" native-type="submit">
                                提交
                            </van-button>
                        </div>
                    </div>
                </van-action-sheet>
            </div>`,
        data: function () {
            return {
                value : 2.5,
                active : 0,
                show : false
            }
        },
        methods: {
            showEval : function() {
                this.show = true;
            },

            openFile : function(fileId) {
                alert(fileId);
            }
        },
        mounted () {

        }
    });
    return vm;
})