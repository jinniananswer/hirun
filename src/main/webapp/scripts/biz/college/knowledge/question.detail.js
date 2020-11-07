require(['vue', 'vant', 'ajax', 'vant-select', 'page-title', 'redirect', 'util'], function (Vue, vant, ajax, vantSelect, pageTitle, redirect, util) {
    let vm = new Vue({
        el: '#app',
        template: `
            <div>
                <page-title title="问题详情"/>
                <div style="margin-top:3.8rem">
                    <!--问题内容-->
                    <van-cell>
                        <template #title>
                            <div style="font-weight: bold" class="van-multi-ellipsis">{{questionInfo.questionTitle}}</div>
                        </template>
                    </van-cell>
                    <!--提问者-->
                    <van-cell>
                        <template #title>
                            <div style="background-color: #f8f8f8;color:#969799;font-size: 10px" :center="true" :border="false">提问者：{{questionInfo.questioner}}</div>
                        </template>
                    </van-cell>
                    <!--问题说明-->
                    <van-cell>
                        <template #title>
                            <div style="font-size: 12px" :center="true" :border="false">{{questionInfo.questionContent}}</div>
                        </template>
                    </van-cell>
                    <!--回复列表-->
                    <van-divider>回复内容</van-divider>
                    <van-cell-group>
                        <van-cell :center="true" border="false" v-for="reply in questionInfo.replyInfos">
                            <!--回复内容-->
                            <template #title>
                                <van-row>
                                    <div class="van-multi-ellipsis--l2">{{reply.replyContent}}</div>
                                </van-row>
                            </template>
                            <van-cell>
                                <template #right-icon>
                                    <van-row>
                                        <van-col @click="thumbsUp">
                                            <van-icon name="good-job-o" size="1.2rem" @click="thumbsUp(reply)"/>{{reply.thumbsUp}}
                                        </van-col>
                                    </van-row>
                                </template>
                            </van-cell>
                            <template #label>
                                <!--回复者-->
                                <van-row>
                                    <div class="van-multi-ellipsis--l2">{{reply.replyer}}</div>
                                </van-row>
                                <!--回复时间-->
                                <van-row>
                                    <div class="van-multi-ellipsis--l2">{{reply.replyTime}}</div>
                                </van-row>
                            </template>
                        </van-cell>
                    </van-cell-group>
                </div>
            </div>`,
        data: function () {
            return {
                questionInfo: {},
                questionId: util.getRequest("questionId"),
                cancelTag: '0',
                thumbsUpTag: false,
            }
        },
        methods: {
            init: function () {
                let that = this
                let param = new URLSearchParams()
                param.append("questionId", that.questionId);
                ajax.get('/api/CollegeQuestion/getQuestionById', param , function(data) {
                    if (null != data && undefined != data) {
                        that.questionInfo = data;
                    }
                });
            },

            addClicks: function () {

            },

            thumbsUp(reply) {
                let that = this;
                let param = new URLSearchParams();
                that.thumbsUpTag = !that.thumbsUpTag;
                that.cancelTag = that.thumbsUpTag ? '0' : '1';
                param.append("replyId", reply.replyId);
                param.append("cancelTag", that.cancelTag);
                ajax.post('api/CollegeQuestion/replyThumbsUp', param, function (data) {
                    that.init();
                });
            },
        },
        mounted () {
            this.init();
        }
    });
    return vm;
})