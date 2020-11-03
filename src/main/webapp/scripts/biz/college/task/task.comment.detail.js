require(['vue', 'vant', 'ajax', 'vant-select', 'page-title', 'redirect', 'util'], function (Vue, vant, ajax, vantSelect, pageTitle, redirect, util) {
    let vm = new Vue({
        el: '#app',
        template: `
            <div>
                <page-title title="任务点评"/>
                <div style="margin-top:3.8rem">
                    <van-cell style="background-color: #f8f8f8;color:#969799" :border="false" is-link :title="commentTaskInfo.taskNam" value="任务评分" @click="showEval"/>
                    <van-cell-group>
                        <van-cell border="false" center="true">
                            <template #title>
                                <div class="van-multi-ellipsis">{{commentTaskInfo.taskName}}</div>
                            </template>
                            <template #right-icon v-if="commentTaskInfo.isDelayFlag">
                                <van-tag type="danger">延期完成</van-tag>
                            </template>
                            <template #label>
                                <van-row>
                                    <div class="van-multi-ellipsis--l2">{{commentTaskInfo.taskDesc}}</div>
                                </van-row>
                            </template>
                        </van-cell>
                        <van-cell title="任务完成时间">
                            <template #label>
                                <div class="van-multi-ellipsis--l2" style="margin-top:1em">
                                    {{commentTaskInfo.taskCompleteDate}}
                                </div>
                            </template>
                        </van-cell>
                    </van-cell-group>
                    <van-cell-group title="心得体会">
                        <van-field
                          v-model="experience"
                          rows="1"
                          autosize
                          label="心得描述"
                          type="textarea"
                          :readonly="isFinish=='true'"
                          placeholder="请输入心得体会"
                        />
                        <van-field
                          readonly
                          clickable
                          label="心得照片"
                          placeholder="查看照片"
                          @click="seeImg"
                        />
                        <van-image-preview v-model="showImg" :images="fileList" @change="onChange">
                          <template v-slot:index>第{{ index }}页</template>
                        </van-image-preview>
                    </van-cell-group>
                    <van-cell-group title="任务照片">
                        <van-field
                          readonly
                          clickable
                          label="任务照片"
                          placeholder="查看照片"
                          @click="seeImg"
                        />
                        <van-image-preview v-model="showImg" :images="fileList" @change="onChange">
                          <template v-slot:index>第{{ index }}页</template>
                        </van-image-preview>
                    </van-cell-group>
                    <van-action-sheet v-model="show" title="任务评分">
                        <div class="content">
                            <van-field name="rate" label="心得评分">
                                <template #input>
                                    <van-rate v-model="experienceScore" />
                                </template>
                            </van-field>
                            <van-field name="rate" label="图片评分">
                                <template #input>
                                    <van-rate v-model="imgScore" />
                                </template>
                            </van-field>
                            <div style="margin: 16px;">
                                <van-button round block type="info" @click="submitScore" native-type="submit">
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
                value : '',
                active : 0,
                show : false,
                taskId: util.getRequest("taskId"),
                isFinish: util.getRequest("isFinish"),
                commentTaskInfo: {},
                isNoComment: false,
                isFinishComment: false,
                experienceScore: 0,
                imgScore: 0,
                fileList: [],
                experience: '',
                index: 0,
                showImg: false
            }
        },
        methods: {
            init : function() {
                let that = this;
                let taskId=that.taskId;
                if(taskId=='undefined'){
                    taskId=null;
                }
                ajax.get('/api/CollegeEmployeeTask/queryTaskCommentInfoByTaskId', {taskId:taskId}, function(data) {
                    that.commentTaskInfo = data;
                    that.experienceScore = data.experienceScore;
                    that.imgScore = data.imgScore
                    that.experience = data.experience;
                    that.fileList = data.fileList;
                });
            },
            showEval : function() {
                this.show = true;
            },
            seeImg: function () {
                this.showImg = true
            },
            onChange(index) {
                this.index = index;
            },
            submitScore: function (event) {
                let that = this;
                if (that.experienceScore == '' || that.imgScore == '' || undefined == that.imgScore || undefined == that.experienceScore){
                    vm.$toast({
                        message : "请给任务心得和任务图片进行评分后再提交",
                        overlay : true,
                        type : 'fail',
                        closeOnClickOverlay : true
                    });
                    return;
                }
                let taskId=that.taskId;
                if(taskId=='undefined'){
                    taskId=null;
                }
                /*let request = new URLSearchParams()
                request.append('taskId', taskId)
                request.append('experienceScore', that.experienceScore)
                request.append('imgScore', that.imgScore)*/
                let request = {};
                request.taskId = that.taskId
                request.experienceScore = that.experienceScore
                request.imgScore = that.imgScore
                ajax.post('api/CollegeEmployeeTaskScore/evaluateTask', request, function(responseData){
                    that.init();
                    that.show = false;
                    that.isFinish = true;
                },null, true);
            },
        },
        mounted () {
            this.init();
        }
    });
    return vm;
})