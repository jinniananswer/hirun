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
                    <van-radio-group v-if="topic.type==1 || topic.type==3" v-model="option" >
                        <van-cell-group v-for="(item ,index) in topic.topicOptions">
                            <van-cell :value="item.name" center="true">
                                <template #right-icon >
                                    <van-radio :name="item.symbol" checked-color="#07c160"></van-radio>
                                </template>
                            </van-cell>   
                        </van-cell-group>
                    </van-radio-group>
                    
                    <van-checkbox-group v-if="topic.type==2" v-model="options">
                        <van-cell-group v-for="(item ,index) in topic.topicOptions">
                            <van-cell :value="item.name" center="true">
                                <template #right-icon >
                                    <van-checkbox :name="item.symbol" checked-color="#07c160"></van-checkbox>
                                </template>
                            </van-cell>   
                        </van-cell-group>
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
                   
                    <div v-for="(item ,index) in topics">
                         <van-button v-if="item.isCorrect==false" class="float"  type="danger" size="small">{{item.topicNum}}</van-button>
                         <van-button v-if="item.isCorrect==true" class="float"  type="primary" size="small">{{item.topicNum}}</van-button>
                    </div>
                </van-action-sheet>
            </div>`,
        data: function () {
            return {
                // 当前页
                currentPage: 0,
                option: '',
                options: [],
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
                scoreType: util.getRequest("scoreType"),
                score: '',
                taskId: util.getRequest("taskId"),
            }
        },
        methods: {
            // 页面初始化触发点
            created: function () {
                // this.taskId = '3017';
                // this.scoreType = '0'
                this.queryTopicInfo();
            },

            // 习题信息初始化
            queryTopicInfo : function () {
                let param = new URLSearchParams();
                param.append('taskId', this.taskId);
                param.append('scoreType', this.scoreType);
                let that = this;
                ajax.get('api/CollegeEmployeeTask/queryTopicByTaskId', param, function(data) {
                    that.topics = data.taskTopics;
                    that.maxIndex = that.topics.length;
                    that.topic = that.topics[0];
                    that.currentIndex = 0;
                });
            },

            // 上一页
            prev : function () {
                if (0 == this.currentIndex) {
                    vm.$toast("亲，已经是第一页了！");
                    return;
                }
                this.setAnswer();

                this.currentIndex = this.currentIndex - 1;
                this.topic = this.topics[this.currentIndex];

                this.setOption();
            },

            // 下一页
            next : function () {
                if (this.maxIndex - 1 == this.currentIndex) {
                    vm.$toast("亲，已经是第最后页了！");
                    return;
                }
                this.setAnswer();

                this.currentIndex = this.currentIndex + 1;
                this.topic = this.topics[this.currentIndex];

                this.setOption();
            },

            // 初始化下页选项信息
            setOption: function() {
                if (this.topic.type == '2' && this.topic.answer != '') {
                    for (let j = 0; j < this.topic.answer.length; j++) {
                        this.options.push(this.topic.answer.substring(j, j+1));
                    }
                } else {
                    this.option = this.topic.answer;
                }
            },

            // 保存本题信息
            setAnswer: function() {
                let type = this.topic.type;
                let answer = '';
                if (type == '2') {
                    this.options.forEach(option => {
                        answer += option
                    })
                    this.options = [];
                } else {
                    answer = this.option;
                    this.option = '';
                }
                this.topic.answer = answer;
                if (answer != '' && answer != undefined) {
                    this.topic.isAnswer = true;
                }
            },

            // 展示答题板
            detail : function () {
                if (this.topic.answer != '' && this.topic.answer != undefined) {
                    this.topic.isAnswer = true;
                }
                this.show = true;
            },

            // 交卷
            onSubmit : function () {
                if ((this.topic.answer == '' || this.topic.answer == undefined)) {
                    this.setAnswer();
                }

                this.topics.forEach(x => {
                    if (!x.isAnswer) {
                        vm.$toast("亲，请答完所有题目后再交卷！");
                        return;
                    }
                });
                // this.pause();
                // 算分（目前由于传参问题导致暂时只能js计算）
                this.score = 0;
                for (let i = 0; i < this.topics.length; i++) {
                    let temp = this.topics[i];
                    temp.isCorrect = false;
                    if (temp.answer == temp.correctAnswer) {
                        this.score += temp.score;
                        temp.isCorrect = true;
                    }
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

            // 从答题板跳转至题目
            goToIndex : function (num) {
                if ((this.topic.answer == '' || this.topic.answer == undefined)) {
                    this.setAnswer();
                }
                this.currentIndex = num - 1;
                this.topic = this.topics[this.currentIndex];
                this.show = false;
                this.setOption();
            },

            // 展示答题情况
            showResult : function () {
                this.showError = true;

            },

            pause: function () {
                this.$refs.countDown.pause();
            },
        },
        mounted () {
            this.created();
        }
    });
    return vm;
})