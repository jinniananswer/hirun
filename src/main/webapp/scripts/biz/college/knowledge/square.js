require(['vue', 'vant', 'ajax', 'vant-select', 'page-title', 'redirect'], function (Vue, vant, ajax, vantSelect, pageTitle, redirect) {
    let vm = new Vue({
        el: '#app',
        template: `
            <div>
                <page-title title="家装广场"/>
                <div style="margin-top:3.8rem">
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
                            <van-cell-group v-for="question in questionInfos">
                                <van-cell style="background-color: #f8f8f8;color:#969799" :center="true" :border="false" is-link :title="question.questionTypeName" @click="openDetail(question.questionType)" value="更多"/>
                                <van-cell-group>
                                    <van-cell is-link :center="true" border="false" v-for="item in question.questionList.slice(0,2)">
                                        <template #title>
                                            <div class="van-multi-ellipsis">{{item.questionTitle}}</div>
                                        </template>
                                        <template #label>
                                            <van-row>
                                                <div class="van-multi-ellipsis--l2">{{item.questionContent}}</div>
                                            </van-row>
                                            <van-row style="padding-top:1em" type="flex" align="bottom" justify="center">
                                                <van-col span="6"></van-col>
                                                <van-col span="6">
                                                    <van-icon name="good-job-o" size="1.2rem"/>{{item.thumbsUp}}
                                                </van-col>
                                                <van-col span="6">
                                                    <van-icon name="eye-o" size="1.2rem"/> {{item.clicks}}
                                                </van-col>
                                            </van-row>
                                        </template>
                                    </van-cell>
                                </van-cell-group>
                                <br/>
                            </van-cell-group>
                        </van-tab>
                        <van-tab title="我的问题">
                            <van-cell-group v-for="question in myQuestionInfos">
                                <van-cell style="background-color: #f8f8f8;color:#969799" :center="true" :border="false" is-link :title="question.questionTypeName" value="更多"/>
                                <van-cell-group>
                                    <van-cell is-link :center="true" border="false" v-for="item in question.questionList.slice(0,2)">
                                        <template #title>
                                            <div class="van-multi-ellipsis">{{item.questionTitle}}</div>
                                        </template>
                                        <template #label>
                                            <van-row>
                                                <div class="van-multi-ellipsis--l2">{{item.questionContent}}</div>
                                            </van-row>
                                            <van-row style="padding-top:1em" type="flex" align="bottom" justify="center">
                                                <van-col span="6"></van-col>
                                                <van-col span="6">
                                                    <van-icon name="good-job-o" size="1.2rem"/>{{item.thumbsUp}}
                                                </van-col>
                                                <van-col span="6">
                                                    <van-icon name="eye-o" size="1.2rem"/> {{item.clicks}}
                                                </van-col>
                                            </van-row>
                                        </template>
                                    </van-cell>
                                </van-cell-group>
                                <br/>
                            </van-cell-group>
                        </van-tab>
                    </van-tabs>
                </div>
                
                <bottom :active="2"></bottom>
            </div>`,
        data: function () {
            return {
                value : '',
                active : 0,
                questionInfos: [],
                myQuestionInfos: []
            }
        },
        methods: {
            init: function () {
                let that = this
                ajax.get('/api/CollegeQuestion/queryQuestionByName', '' , function(data) {
                    that.questionInfos = data;
                });

                ajax.get('/api/CollegeQuestion/queryLoginQuestion', '' , function(data) {
                    that.myQuestionInfos = data;
                });
            },
            openDetail: function (questionType) {
                redirect.open('/biz/college/knowledge/question_type_detail.html?questionType='+questionType, '更多问题');
            }
        },
        mounted () {
            this.init();
        }
    });
    return vm;
})