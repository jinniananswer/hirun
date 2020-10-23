require(['vue', 'vant', 'ajax', 'vant-select', 'page-title', 'redirect', 'util'], function (Vue, vant, ajax, vantSelect, pageTitle, redirect, util) {
    let vm = new Vue({
        el: '#app',
        template: `
            <div>
                <van-cell value=" ">
                    <template #right-icon>
                        倒计时：<van-count-down :time="time" color="#07c160" />
                    </template>
                </van-cell> 
                <div v-model="topic">
                    <van-cell>{{topic.topicNum}}.{{topic.name}}{{topic.type==1?' (单选题)':topic.type==2?' (多选题)':topic.type==3?' (判断题)':' (填空题)'}}</van-cell>
                    <van-radio-group v-if="topic.type==1 || topic.type==3" :v-model="option" >
                        <van-cell-group v-for="(item ,index) in topic.topicOptions"  @click="switchOption(item.symbol, topic.type)">
                            <van-cell :value="item.name" center="true">
                                <template #right-icon >
                                    <van-radio :name="item.symbol" checked-color="#07c160"></van-radio>
                                </template>
                            </van-cell>   
                        </van-cell-group>
                    </van-radio-group>
                    
                    <van-checkbox-group v-if="topic.type==2" :v-model="option">
                        <van-cell-group v-for="(item ,index) in topic.topicOptions"  @click="switchOption(item.symbol, topic.type)">
                            <van-cell :value="item.name" center="true">
                                <template #right-icon >
                                    <van-checkbox :name="item.symbol" checked-color="#07c160"></van-checkbox>
                                </template>
                            </van-cell>   
                        </van-cell-group>
                        <!--<van-checkbox v-for="(item ,index) in topic.topicOptions" :name="item.symbol">{{item.name}}</van-checkbox>-->
                    </van-checkbox-group>
                </div>
                
                <div>
                    <van-button plain  type="info" size="small" @click="prev">上一题</van-button>
                    <van-button plain  type="info" size="small" @click="next">下一题</van-button>
                    <van-button plain  type="info" size="small" @click="detail">答题状况</van-button>
                    <van-button plain  type="info" size="small" @click="onSubmit">提交</van-button>
                </div>
                
                <van-action-sheet v-model="show" title="答题详情">
                   
                    <div v-for="(item ,index) in topics">
                         <van-button v-if="item.isAnswer==false" class="float"  type="default" size="small" @click="goToIndex(item.topicNum)">{{item.topicNum}}</van-button>
                         <van-button v-if="item.isAnswer==true" class="float"  type="info" size="small" @click="goToIndex(item.topicNum)">{{item.topicNum}}</van-button>
                    </div>
                </van-action-sheet>
                
                <van-action-sheet v-model="showError" title="答题结果详情">
                   
                    <div v-for="(item ,index) in resultInfos">
                         <van-button v-if="item.isCorrect==false" class="float"  type="danger" size="small">{{item.index}}</van-button>
                         <van-button v-if="item.isCorrect==true" class="float"  type="primary" size="small">{{item.index}}</van-button>
                    </div>
                </van-action-sheet>
            </div>`,
        data: function () {
            return {
                // 当前页
                currentPage: 0,
                option: '',
                value: '',
                // 所有题目信息
                topics: [],
                // 当前页的题目信息
                topic: {},
                time: 30 * 60 * 60 * 1000,
                currentIndex: '',
                maxIndex: '',
                // 已答题目信息
                answerInfos: [],
                answerInfo: {},
                resultInfos: [],
                resultInfo: {},
                show: false,
                showError: false,
                screoType: util.getRequest("screoType"),
                score: '',
                taskId: util.getRequest("taskId"),
                result: '',
            }
        },
        // 页面初始化触发点
        created: function () {
            this.taskId = '3017';
            this.scoreType = '0'
            this.queryTopicInfo();
        },
        methods: {
            switchOption : function(value, type) {
                if (type == '2') {
                    this.option += value;
                    this.option.sort();
                } else {
                    this.option = value;
                }
                this.answerInfo = {
                    index: this.currentIndex,
                    answer: this.option
                };
                this.answerInfos.push(this.answerInfo);
                this.topic.isAnswer = true;
                this.answerInfo = {};
            },

            queryTopicInfo : function (obj) {
                let param = new URLSearchParams();
                param.append('taskId', this.taskId);
                let that = this;
                ajax.get('api/CollegeEmployeeTask/queryTopicByTaskId', param, function(data) {
                    that.topics = data.taskTopics;
                    that.maxIndex = that.topics.length;
                    that.topic = that.topics[0];
                    that.currentIndex = 0;
                });
            },

            prev : function () {
                if (0 == this.currentIndex) {
                    vm.$toast("亲，已经是第一页了！");
                    return;
                }
                this.option = '';
                this.currentIndex = this.currentIndex - 1;
                this.topic = this.topics[this.currentIndex];
            },

            next : function () {
                if (this.maxIndex - 1 == this.currentIndex) {
                    vm.$toast("亲，已经是第最后页了！");
                    return;
                }
                this.option = '';
                this.currentIndex = this.currentIndex + 1;
                this.topic = this.topics[this.currentIndex];
            },

            detail : function () {
                this.show = true;
            },

            onSubmit : function () {
                if (this.maxIndex > this.answerInfos.length) {
                    vm.$toast("亲，请答完所有题目后再交卷！");
                    return;
                }
                // this.pause();

                // 算分（目前由于传参问题导致暂时只能js计算）
                this.score = 0;
                for (let i = 0; i < this.topics.length; i++) {
                    for (let j = 0; j < this.answerInfos.length; j++) {
                        let t = this.topics[i];
                        let a = this.answerInfos[j];
                        if (a.index == t.topicNum - 1) {
                            this.resultInfo = {
                                index: a.index + 1,
                                answer: a.answer + 1,
                                isCorrect: false
                            };
                            if (a.answer == t.correctAnswer) {
                                this.score += t.score;
                                this.resultInfo.isCorrect = true;
                            }
                            break;
                        }
                    }
                    // 记录答题信息
                    this.resultInfos.push(this.resultInfo);
                }

                // 入表
                let that = this;
                let param = new URLSearchParams()
                param.append('taskId', this.taskId)
                param.append('score', this.score)
                param.append('scoreType', this.scoreType)
                ajax.post('api/CollegeTaskScore/addScore', param, function(responseData){
                    that.showResult();
                },null, true);
            },

            goToIndex : function (num) {
                this.currentIndex = num - 1;
                this.topic = this.topics[this.currentIndex];
                this.show = false;
            },

            showResult : function () {
                this.showError = true;
            },

            pause: function () {
                this.$refs.countDown.pause();
            },
        },
        mounted () {

        }
    });
    return vm;
})