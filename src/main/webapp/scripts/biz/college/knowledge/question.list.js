require(['vue', 'vant', 'ajax', 'vant-select', 'page-title', 'redirect', 'util'], function (Vue, vant, ajax, vantSelect, pageTitle, redirect, util) {
    let vm = new Vue({
        el: '#app',
        template: `
            <div>
                <page-title title="问题列表"/>
                <div style="margin-top:3.8rem">
                    <van-cell-group v-for="question in questionInfos">
                        <van-cell style="background-color: #f8f8f8;color:#969799" :center="true" :border="false" :title="question.questionTypeName"/>
                        <van-cell-group>
                            <van-cell is-link :center="true" border="false" v-for="item in question.questionList">
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
                </div>
                
                <bottom></bottom>
            </div>`,
        data: function () {
            return {
                value : '',
                active : 0,
                questionType: util.getRequest("questionType"),
                questionInfos: [],
            }
        },
        methods: {
            init: function () {
                let that = this
                let param = new URLSearchParams()
                param.append("questionType", that.questionType);
                ajax.get('/api/CollegeQuestion/queryQuestionByQuestionType', param , function(data) {
                    that.questionInfos = data;
                });
            }
        },
        mounted () {
            this.init();
        }
    });
    return vm;
})