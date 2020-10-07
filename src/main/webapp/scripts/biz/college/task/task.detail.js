require(['vue', 'vant', 'ajax', 'vant-select', 'page-title', 'redirect', 'util'], function (Vue, vant, ajax, vantSelect, pageTitle, redirect, util) {
    let vm = new Vue({
        el: '#app',
        template: `
            <div>
                <page-title title="任务详情"/>
                <div style="margin-top:3.8rem">
                    <van-cell style="background-color: #f8f8f8;color:#969799" :border="false" is-link title="家装知识学习任务" value="评分" @click="showEval"/>
                    <van-cell-group>
                        <van-cell border="false" center="true">
                            <template #title>
                                <div class="van-multi-ellipsis">{{taskDetailInfo.taskName}}</div>
                            </template>
                            <template #right-icon v-if="taskDetailInfo.isDelayFlag">
                                <van-tag type="danger">已延期</van-tag>
                            </template>
                            <template #label>
                                <van-row>
                                    <div class="van-multi-ellipsis--l2">{{taskDetailInfo.taskDesc}}</div>
                                </van-row>
                            </template>
                        </van-cell>
                        <template v-if="taskDetailInfo.studyType != '2'">
                            <van-cell title="学习进度">
                                <template #label>
                                    <van-progress :percentage="taskDetailInfo.taskProgress" style="margin-top:1em"/>
                                    <div class="van-multi-ellipsis--l2" style="margin-top:1em">
                                         {{taskDetailInfo.studyStartDate}} ~ {{taskDetailInfo.sysdate}}
                                    </div>
                                </template>
                            </van-cell>
                        </template>
                        <van-cell title="任务时间进度">
                            <template #label>
                                <van-progress :percentage="taskDetailInfo.taskTimeProgress" style="margin-top:1em"/>
                                <div class="van-multi-ellipsis--l2" style="margin-top:1em">
                                    {{taskDetailInfo.studyStartDate}} ~ {{taskDetailInfo.studyEndDate}}
                                </div>
                            </template>
                        </van-cell>
                    </van-cell-group>
                    <template v-if="taskDetailInfo.taskStudyContentList != null && taskDetailInfo.taskStudyContentList != '[]' && taskDetailInfo.taskStudyContentList.length > 0">
                        <van-cell-group title="学习课件">
                            <van-cell :title="item.fileName" v-for="item in taskDetailInfo.taskStudyContentList" is-link @click="openFile(item.fileUrl)"/>
                        </van-cell-group>
                    </template>
                    <template v-if="taskDetailInfo.studyType != '2'">
                        <div style="margin-top:1em;margin-right:1em;margin-left:1em;margin-bottom:1em">
                            <van-row :gutter="20">
                                <van-col span="12">
                                    <van-button :disabled="!taskDetailInfo.isExerciseFlag" @click="openFile(taskDetailInfo.isExerciseFlag)" type="primary" icon="plus" round block>我要练习</van-button>
                                </van-col>
                                <van-col span="12">
                                    <van-button :disabled="!taskDetailInfo.isExamFlag"  @click="openFile(taskDetailInfo.isExamFlag)" type="danger" icon="fire-o" round block>我要考试</van-button>
                                </van-col>
                            </van-row>
                        </div>
                    </template>
                    <template v-if="!isFinish" align="center">
                        <template v-if="taskDetailInfo.studyType == '2'" align="center">
                            <van-field
                                  readonly
                                  clickable
                                  label="导师"
                                  :value="tutor"
                                  placeholder="选择导师"
                                  @click="selectTutor"
                                />
                                <van-popup v-model="showTutorPicker" round position="bottom">
                                  <van-picker
                                    show-toolbar
                                    :columns="selectTutorList"
                                    @cancel="showTutorPicker = false"
                                    @confirm="onConfirm"
                                  />
                                </van-popup>
                            <template v-if="!taskDetailInfo.isSelectTutorFlag">
                                <van-col span="12" align="center">
                                    <van-button type="primary" icon="plus" round block>上传心得</van-button>
                                </van-col>
                            </template>
                        </template>
                    </template>
                    <van-action-sheet v-model="show" title="评分">
                        <div class="content">
                            <van-field name="rate" label="课程难度">
                                <template #input>
                                    <van-rate v-model="value" />
                                </template>
                            </van-field>
                            <van-field name="rate" label="老师评分">
                                <template #input>
                                    <van-rate v-model="value" />
                                </template>
                            </van-field>
                            <div style="margin: 16px;">
                                <van-button round block type="info" native-type="submit">
                                    提交
                                </van-button>
                            </div>
                        </div>
                    </van-action-sheet>
                </div>
                <bottom :active="2"></bottom>
            </div>`,
        data: function () {
            return {
                value : 2.5,
                active : 0,
                show : false,
                taskId: util.getRequest("taskId"),
                taskDetailInfo: {},
                showTutorPicker: false,
                selectTutorList: [],
                tutor: '',
                isFinish:  util.getRequest("isFinish")
            }
        },
        methods: {
            showEval : function() {
                this.show = true;
            },

            openFile : function(fileUrl) {
                alert(fileUrl);
                redirect.open(fileUrl, '')
            },

            initTaskInfo: function () {
                let that = this;
                let taskId=that.taskId;
                if(taskId=='undefined'){
                    taskId=null;
                }
                ajax.get('/api/CollegeEmployeeTask/queryEmployTaskInfoByTaskId', {taskId:taskId}, function(data) {
                    that.taskDetailInfo = data;
                    that.tutor = data.selectTutor;
                });
            },
            selectTutor: function () {
                let that = this;
                let isSelectTutorFlag = that.taskDetailInfo.isSelectTutorFlag;
                if (!isSelectTutorFlag){
                    vm.$toast.fail('导师已经选择，不能再次选择');
                    return;
                }
                let taskId=that.taskId;
                if(taskId=='undefined'){
                    taskId=null;
                }
                ajax.get('/api/CollegeEmployeeTask/queryLoginEmployeeSelectTutor', '', function(data) {
                    data.forEach((tutor) => {
                        that.selectTutorList.push("[" + tutor.value + "]" + tutor.name);
                    })
                    that.showTutorPicker = true;
                });
            },
            onCancel: function () {
                this.showTutorPicker = false;
            },
            onConfirm: function (value) {
                let that = this;
                let taskId=that.taskId;
                if(taskId=='undefined'){
                    taskId=null;
                }
                let request = {};
                request.taskId = taskId;
                request.selectTutor = value;
                ajax.post('api/CollegeEmployeeTaskTutor/addByTaskIdAndSelectTutor', request, function(responseData){
                    that.showTutorPicker = false;
                    that.initTaskInfo();
                },null, true);
            }
        },
        mounted () {
            this.initTaskInfo();
        }
    });
    return vm;
})