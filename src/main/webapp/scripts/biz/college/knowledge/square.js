require(['vue', 'vant', 'ajax', 'vant-select', 'page-title', 'redirect'], function (Vue, vant, ajax, vantSelect, pageTitle, redirect) {
    let vm = new Vue({
        el: '#app',
        template: `
            <div>
                <page-title title="你问我答"/>
                <div style="margin-top:3.8rem">
                    <van-search
                        v-model="value"
                        shape="round"
                        placeholder="请输入搜索关键词"
                        @search="onSearch"
                        @cancel="onCancel"
                    />
                    <div style="margin-top:1em;margin-right:1em;margin-left:1em;">
                        <van-button type="primary" icon="plus" round block url="/biz/college/knowledge/asking.html">我要提问</van-button>
                    </div>
                    <van-tabs v-model="active">
                        <van-tab title="广场">
                            <van-cell-group v-for="question in questionInfos">
                                <van-cell style="background-color: #f8f8f8;color:#969799" :center="true" :border="false" is-link :title="question.questionTypeName" @click="openQuestionList(question.questionType)" value="更多"/>
                                <van-cell-group>
                                    <van-cell is-link :center="true" border="false" v-for="item in question.questionList.slice(0,5)" @click="openQuestionDetail(item)">
                                        <template #title>
                                            <div class="van-multi-ellipsis">{{item.questionTitle}}</div>
                                        </template>
                                        <template #label>
                                            <van-row>
                                                <div class="van-multi-ellipsis--l2">{{item.questionContent}}</div>
                                            </van-row>
                                            <van-row style="padding-top:1em" type="flex" align="bottom" justify="center">
                                                <van-col span="6"></van-col>
                                                <van-col span="6" @click.stop>
                                                    <van-icon name="good-job-o" size="1.2rem" @click="addThumbsUp(item)"/>{{item.thumbsUp}}
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
                                <van-cell style="background-color: #f8f8f8;color:#969799" :center="true" :border="false" is-link :title="question.questionTypeName" @click="openQuestionList(question.questionType)" value="更多"/>
                                <van-cell-group>
                                    <van-cell is-link :center="true" border="false" v-for="item in question.questionList.slice(0,2)" @click="openQuestionDetail(item)">
                                        <template #title>
                                            <div class="van-multi-ellipsis">{{item.questionTitle}}</div>
                                        </template>
                                        <template #label>
                                            <van-row>
                                                <div class="van-multi-ellipsis--l2">{{item.questionContent}}</div>
                                            </van-row>
                                            <van-row style="padding-top:1em" type="flex" align="bottom">
                                                <van-col span="6"></van-col>
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
                myQuestionInfos: [],
                question: {
                    thumbs: 0
                }
            }
        },
        methods: {
            init: function () {
                let that = this;;
                ajax.get('/api/CollegeQuestion/queryQuestionByName', '' , function(data) {
                    that.questionInfos = data;
                });

                ajax.get('/api/CollegeQuestion/queryLoginQuestion', '' , function(data) {
                    that.myQuestionInfos = data;
                });
            },
            openQuestionList: function (questionType) {
                redirect.open('/biz/college/knowledge/question_list.html?questionType='+questionType, '更多问题');
            },
            openQuestionDetail: function (item) {
                this.addClick(item);
                redirect.open('/biz/college/knowledge/question_detail.html?questionId='+item.questionId, '问题详情');
            },
            onSearch: function (val) {
                this.query(val);
            },
            onCancel: function () {

            },
            query: function (val) {
                let that = this;
                let param = new URLSearchParams();
                param.append("name", val);
                ajax.get('/api/CollegeQuestion/queryQuestionByText', param , function(data) {
                    that.questionInfos = data;
                });

                ajax.get('/api/CollegeQuestion/queryLoginQuestionByText', param , function(data) {
                    that.myQuestionInfos = data;
                });
            },

            addClick: function (item) {
                let param = new URLSearchParams()
                param.append('questionId', item.questionId);
                let that = this;
                that.question = item;
                ajax.post('api/CollegeQuestion/addClick', param, function (responseData) {

                });
            },

            addThumbsUp: function (item) {
                let param = new URLSearchParams()
                param.append('questionId', item.questionId);
                let that = this;
                that.question = item;
                let cancelTag = '0';
                if (item.thumbs > 0) {
                    cancelTag = '1';
                } else {
                    that.question.thumbs = 0;
                }
                param.append('cancelTag', cancelTag);
                ajax.post('api/CollegeQuestion/addThumbsUp', param, function (responseData) {
                    let id = that.question.questionId;
                    let thumb = that.question.thumbs;
                    for (let i = 0; i < that.questionInfos[0].questionList.length; i++) {
                        let temp = that.questionInfos[0].questionList[i];
                        if (temp.questionId == id) {
                            if (thumb == 0) {
                                temp.thumbsUp = temp.thumbsUp + 1;
                                temp.thumbs = 1;
                            } else {
                                temp.thumbsUp = temp.thumbsUp - 1;
                                temp.thumbs = 0;
                            }
                        }
                    }
                });
            }
        },
        mounted () {
            this.init();
        }
    });
    return vm;
})