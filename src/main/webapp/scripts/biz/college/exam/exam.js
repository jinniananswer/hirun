require(['vue', 'vant', 'ajax', 'vant-select', 'page-title', 'redirect', 'util'], function (Vue, vant, ajax, vantSelect, pageTitle, redirect, util) {
    let vm = new Vue({
        el: '#app',
        template: `
            <div>
                <page-title title="答题"/>
                <div style="margin-top:3.8rem">
                    <van-cell value=" ">
                        <template #title>
                            <van-tag plain size="large"> 倒计时：<van-count-down :time="time" color="#07c160" ref="countDown" @finish="stopExam"/></van-tag>
                        </template>
                    </van-cell> 
                    <div v-model="topic">
                        <van-cell>{{topic.topicNum}}.{{topic.name}}{{topic.type==1?' (单选题)':topic.type==2?' (多选题)':topic.type==3?' (判断题)':' (填空题)'}}</van-cell>
                        <van-radio-group v-if="topic.type==1 || topic.type==3" v-model="option" >
                            <van-cell-group v-for="(item ,index) in topic.topicOptions">
                                <van-cell :value="item.name" center="true">
                                    <template #right-icon >
                                        <van-radio :disabled="examStop" :name="item.symbol" checked-color="#07c160" @click="setAnswer(item.symbol)"></van-radio>
                                    </template>
                                </van-cell>   
                            </van-cell-group>
                        </van-radio-group>
                        
                        <van-checkbox-group v-if="topic.type==2" v-model="options" @change="setAnswer()">
                            <van-cell-group v-for="(item ,index) in topic.topicOptions">
                                <van-cell :value="item.name" center="true">
                                    <template #right-icon >
                                        <van-checkbox shape="square" :disabled="examStop" :name="item.symbol" checked-color="#07c160"></van-checkbox>
                                    </template>
                                </van-cell>   
                            </van-cell-group>
                        </van-checkbox-group>
                    </div>
                    <van-pagination v-model="currentIndex" prev-text="上一题" next-text="下一题" @change="changeTopic" :page-count="topics.length" mode="simple" />
                    <van-cell>
                        <van-col v-if="!examStop" span="12">
                            <van-button @click="detail" type="primary" icon="browsing-history-o" round block>答题状况</van-button>
                        </van-col>
                        <van-col v-if="examStop" span="12">
                            <van-button @click="showResult(score)" type="primary" icon="browsing-history-o" round block>答题详情</van-button>
                        </van-col>
                        <van-col span="12">
                            <van-button :disabled="examStop"  @click="onSubmit" type="danger" icon="completed" round block>提交</van-button>
                        </van-col>
                    </van-cell>
                    
                    <van-action-sheet v-model="show" title="答题状况">
                       
                        <div v-for="(item ,index) in topics">
                             <van-button v-if="item.isAnswer==false" class="float"  type="default" size="small" @click="goToIndex(item.topicNum)">{{item.topicNum}}</van-button>
                             <van-button v-if="item.isAnswer==true" class="float"  type="info" size="small" @click="goToIndex(item.topicNum)">{{item.topicNum}}</van-button>
                        </div>
                    </van-action-sheet>
                    <van-dialog v-model="showError" title="考试结果" show-cancel-button cancel-button-text="返回任务详情" @cancel="returnTaskDetail">
                        <van-tag size="large" >{{resultTipStart}}<van-tag text-color="#ad0000">{{score}}</van-tag>{{resultTipEnd}}</van-tag>
                        <van-cell center>
                            <van-icon :name="examPassIcon" :color="examColor" size="50px"/>
                        </van-cell>
                        <van-tag v-if="scoreType == '1'" size="large">{{resultPassMsg}}</van-tag>
                        <van-cell>
                            <van-button v-for="(item ,index) in topics.slice(topicDetailStart,topicDetailEnd)" class="float" @click=goToIndex(item.topicNum) :type="item.isCorrect==false ? 'danger':'primary'" size="small">{{item.topicNum}}</van-button>
                        </van-cell>
                        <van-pagination v-model="topicDetailCurrentPage" @change="changeTopicDetailPage" :page-count="topicDetailCount" mode="simple" />
                    </van-dialog>
                </div>
                
                <bottom :active="2"></bottom>
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
                currentIndex: 0,
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
                isFinish: util.getRequest("isFinish"),
                passScore: '',
                resultTipStart: '',
                resultTipEnd: '',
                resultPassMsg: '',
                resultTip: '',
                examPassIcon: '',
                topicDetailCurrentPage: 1,
                topicDetailSize: 12,
                topicDetailCount: 0,
                topicDetailStart: 0,
                topicDetailEnd: 0,
                examStop: false,
                examColor: ''
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
            queryTopicInfo: function () {
                let param = new URLSearchParams();
                param.append('taskId', this.taskId);
                param.append('scoreType', this.scoreType);
                let that = this;
                ajax.get('api/CollegeEmployeeTask/queryTopicByTaskId', param, function (data) {
                    that.topics = data.taskTopics;
                    that.maxIndex = that.topics.length;
                    that.topic = that.topics[0];
                    that.currentIndex = 0;
                    that.passScore = data.passScore;
                    that.time = data.taskTimeLen*60*1000;
                });
            },

            changeTopic: function(){
                this.options = [];
                this.option = '';
                this.topic = this.topics[this.currentIndex - 1];
                this.setOption();
            },
            // 初始化下页选项信息
            setOption: function () {
                if (this.topic.type == '2' && undefined != this.topic.answer && '' != this.topic.answer) {
                    for (let j = 0; j < this.topic.answer.length; j++) {
                        this.options.push(this.topic.answer.substring(j, j + 1));
                    }
                } else {
                    this.option = this.topic.answer;
                }
            },

            // 保存本题信息
            setAnswer: function (value) {
                let type = this.topic.type;
                let answer = '';
                if (type == '2') {
                    let options = this.options;
                    if (undefined != options && options.length > 0){
                        options.forEach(option => {
                            answer += option
                        })
                        //给答案排序
                        if (undefined != answer && '' != answer){
                            let answerTmp = [];
                            for (let i = 0 ; i < answer.length; i++){
                                answerTmp.push(answer.substring(i, i+1));
                            }
                            if (undefined != answerTmp && [] != answerTmp && answerTmp.length > 0){
                                answerTmp = answerTmp.sort(function(a,b){return a.localeCompare(b)})
                                let answerStr = '';
                                answerTmp.forEach(option => {
                                    answerStr += option
                                })
                                answer = answerStr;
                            }
                        }
                    }
                } else {
                    answer = value;
                }
                this.topic.answer = answer;
                if (answer != '' && answer != undefined) {
                    this.topic.isAnswer = true;
                }else {
                    this.topic.isAnswer = false;
                }
            },

            // 展示答题板
            detail: function () {
                if (this.topic.answer != '' && this.topic.answer != undefined) {
                    this.topic.isAnswer = true;
                }
                this.show = true;
            },

            // 交卷
            onSubmit: function () {
                this.test();
                return;
                for (let i = 0; i < this.topics.length; i++){
                    let topic = this.topics[i];
                    if (!topic.isAnswer) {
                        vm.$toast("亲，请答完所有题目后再交卷！");
                        return;
                    }
                }
                this.examStop = true;
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
                ajax.post('api/CollegeTaskScore/addScore', param, function (responseData) {
                    that.showResult(that.score);
                }, null, true);
            },

            // 从答题板跳转至题目
            goToIndex: function (num) {
                if ((this.topic.answer == '' || this.topic.answer == undefined)) {
                    this.setAnswer();
                }
                this.currentIndex = num - 1;
                this.topic = this.topics[this.currentIndex];
                this.show = false;
                this.setOption();
                this.showError = false;
            },

            // 展示答题情况
            showResult: function (score) {
                this.showError = true;
                if (this.scoreType == '0') {
                    this.resultTipStart = '本次练习得分为';
                    this.resultTipEnd = '分!';
                } else if (this.scoreType == '1') {
                    this.resultTipStart = '本次考试得分为';
                    this.resultTipEnd = '分';
                    if (score < this.passScore) {
                        this.resultPassMsg = '不合格!';
                        this.examPassIcon = 'close';
                        this.examColor = '#009900';
                    } else {
                        this.resultPassMsg = '合格!';
                        this.examPassIcon = 'passed';
                        this.examColor = '#FF0000';
                    }
                }
                let topicSize = this.topics.length
                let pieNum = parseFloat(topicSize / this.topicDetailSize);
                let page = Math.floor(pieNum);
                if (pieNum > page){
                    this.topicDetailCount = page + 1;
                }else {
                    this.topicDetailCount = page;
                }
                this.topicDetailEnd = this.topicDetailCurrentPage * this.topicDetailSize
            },

            pause: function () {
                this.$refs.countDown.pause();
            },

            test: function () {
                let that = this
                let topics = [];
                for (let i = 0; i < 50; i++) {
                    let topic = {};
                    topic.topicNum = i + 1;
                    topic.index = i + 1;
                    topic.answer = 'A';
                    if (i % 3 == 0) {
                        topic.correctAnswer = 'A';
                        topic.isCorrect = true;
                    } else {
                        topic.correctAnswer = 'B';
                        topic.isCorrect = false;
                    }
                    topics.push(topic);
                }
                that.topics = topics;
                that.score = 90;
                that.scoreType = '1'
                that.passScore = '80'
                that.showResult(that.score);
            },
            changeTopicDetailPage: function () {
                this.topicDetailStart = (this.topicDetailCurrentPage - 1) * this.topicDetailSize
                this.topicDetailEnd = this.topicDetailCurrentPage * this.topicDetailSize
            },
            returnTaskDetail: function () {
                let examType = this.scoreType;
                let isFinish = false;
                if ('1' == examType){
                    isFinish = true
                }
                redirect.open('/biz/college/task/task_detail.html?taskId=' + this.taskId + '&isFinish=' + isFinish, '任务详情');
            },
            stopExam: function () {

                vm.$dialog.confirm({
                    title: '答题超时',
                    message: '未在规定时间内完成答题，考试结束，点击确定重新考试，点击取消返回任务详情',
                })
                    .then(() => {
                        this.created();
                        this.$refs.countDown.reset();
                    })
                    .catch(() => {
                        let that = this;
                        let taskId=that.taskId;
                        if(taskId=='undefined'){
                            taskId = null;
                        }
                        let isFinish = that.isFinish;
                        if(isFinish=='undefined'){
                            isFinish = false;
                        }
                        redirect.open('/biz/college/task/task_detail.html?taskId='+taskId+'&scoreType='+that.examType + '&isFinish=' + isFinish, '考试');
                    });
            }
        },
        mounted() {
            this.created();
        }
    });
    return vm;
})