require(['vue', 'vant', 'ajax', 'vant-select', 'page-title', 'redirect', 'util'], function (Vue, vant, ajax, vantSelect, pageTitle, redirect, util) {
    let vm = new Vue({
        el: '#app',
        template: `
            <div>
                <page-title title="我的任务"/>
                <div style="margin-top:3.8rem">
                    <van-tabs v-model="active">
                        <van-tab title="当前任务">
                            <template v-if="isTodayTask">
                                <template v-if="isTodayCourseTask">
                                    <van-cell-group title="课程任务">
                                        <van-cell :title="item.taskName" v-for="item in myTaskInfo.todayTask.courseTaskList" is-link :label="item.taskDesc" :url="'/biz/college/task/task_detail.html?taskId=' + item.taskId + '&isFinish=false'" />
                                    </van-cell-group>
                                </template>
                                <template v-if="isTodayCoursewareTask">
                                    <van-cell-group title="课件任务">
                                        <van-cell :title="item.taskName" v-for="item in myTaskInfo.todayTask.coursewareTaskList" is-link :label="item.taskDesc" :url="'/biz/college/task/task_detail.html?taskId=' + item.taskId + '&isFinish=false'"  />
                                    </van-cell-group>
                                </template>
                                <template v-if="isTodayPracticeTask">
                                    <van-cell-group title="实践任务">
                                        <van-cell :title="item.taskName" v-for="item in myTaskInfo.todayTask.practiceTaskList" is-link :label="item.taskDesc" :url="'/biz/college/task/task_detail.html?taskId=' + item.taskId + '&isFinish=false'" />
                                    </van-cell-group>
                                </template>
                            </template>
                            <template v-if="!isTodayTask">
                                <van-empty image="search" description="今日没有需要完成的任务哦" />
                            </template>
                        </van-tab>
                        
                        <van-tab title="明日任务">
                            <template v-if="isTomorrowTask">
                                <template v-if="isTomorrowCourseTask">
                                    <van-cell-group title="课程任务">
                                        <van-cell :title="item.taskName" v-for="item in myTaskInfo.tomorrowTask.courseTaskList" is-link :label="item.taskDesc" :url="'/biz/college/task/task_detail.html?taskId=' + item.taskId + '&isFinish=false'" />
                                    </van-cell-group>
                                </template>
                                <template v-if="isTomorrowCoursewareTask">
                                    <van-cell-group title="课件任务">
                                        <van-cell :title="item.taskName" v-for="item in myTaskInfo.tomorrowTask.coursewareTaskList" is-link :label="item.taskDesc" :url="'/biz/college/task/task_detail.html?taskId=' + item.taskId + '&isFinish=false'" />
                                    </van-cell-group>
                                </template>
                                <template v-if="isTomorrowPracticeTask">
                                    <van-cell-group title="实践任务">
                                        <van-cell :title="item.taskName" v-for="item in myTaskInfo.tomorrowTask.practiceTaskList" is-link :label="item.taskDesc" :url="'/biz/college/task/task_detail.html?taskId=' + item.taskId + '&isFinish=false'" />
                                    </van-cell-group>
                                </template>
                            </template>
                            <template v-if="!isTomorrowTask">
                                <van-empty image="search" description="明日没有需要完成的任务哦" />
                            </template>
                        </van-tab>
                        
                        <van-tab title="已完成">
                            <template v-if="isFinishTask">
                                <template v-if="isFinishCourseTask">
                                    <van-cell-group title="课程任务">
                                        <van-cell :title="item.taskName" v-for="item in myTaskInfo.finishTask.courseTaskList" is-link :label="item.taskDesc" :url="'/biz/college/task/task_detail.html?taskId=' + item.taskId + '&isFinish=true'" />
                                    </van-cell-group>
                                </template>
                                <template v-if="isFinishCoursewareTask">
                                    <van-cell-group title="课件任务">
                                        <van-cell :title="item.taskName" v-for="item in myTaskInfo.finishTask.coursewareTaskList" is-link :label="item.taskDesc" :url="'/biz/college/task/task_detail.html?taskId=' + item.taskId + '&isFinish=true'" />
                                    </van-cell-group>
                                </template>
                                <template v-if="isFinishPracticeTask">
                                    <van-cell-group title="实践任务">
                                        <van-cell :title="item.taskName" v-for="item in myTaskInfo.finishTask.practiceTaskList" is-link :label="item.taskDesc" :url="'/biz/college/task/task_detail.html?taskId=' + item.taskId + '&isFinish=true'" />
                                    </van-cell-group>
                                </template>
                            </template>
                            <template v-if="!isFinishTask">
                                <van-empty image="search" description="还没有完成的任务哦" />
                            </template>
                        </van-tab>
                    </van-tabs>
                </div>  
                <bottom :active="2"></bottom>
            </div>`,
        data: function () {
            return {
                value : '',
                active : 0,
                myTaskInfo: {},
                finishTaskInfo: [],
                isTodayTask: false,
                isTodayCourseTask: false,
                isTodayCoursewareTask: false,
                isTodayPracticeTask: false,
                isTomorrowTask: false,
                isTomorrowCourseTask: false,
                isTomorrowCoursewareTask: false,
                isTomorrowPracticeTask: false,
                isFinishTask: false,
                isFinishCourseTask: false,
                isFinishCoursewareTask: false,
                isFinishPracticeTask: false,
            }
        },
        methods: {
            init : function() {
                let that = this;
                ajax.get('api/CollegeEmployeeTask/queryLoginEmployeeTaskInfo', '', function(data) {
                    that.myTaskInfo = data;
                    if (data != null && data != '{}'){
                        if (data.todayTask != null && data.todayTask != '{}'){
                            let todayTask = data.todayTask;
                            if (todayTask.courseTaskList != null && todayTask.courseTaskList != '[]' && todayTask.courseTaskList.length > 0){
                                that.isTodayCourseTask = true;
                            }
                            if (todayTask.coursewareTaskList != null && todayTask.coursewareTaskList != '[]' && todayTask.coursewareTaskList.length > 0){
                                that.isTodayCoursewareTask = true;
                            }
                            if (todayTask.practiceTaskList != null && todayTask.practiceTaskList != '[]' && todayTask.practiceTaskList.length > 0){
                                that.isTodayPracticeTask = true;
                            }
                            if (that.isTodayCourseTask || that.isTodayCoursewareTask || that.isTodayPracticeTask){
                                that.isTodayTask = true;
                            }
                        }
                        if(data.tomorrowTask != null && data.tomorrowTask != '{}'){
                            let tomorrowTask = data.tomorrowTask;
                            if (tomorrowTask.courseTaskList != null && tomorrowTask.courseTaskList != '[]' && tomorrowTask.courseTaskList.length > 0){
                                that.isTomorrowCourseTask = true;
                            }
                            if (tomorrowTask.coursewareTaskList != null && tomorrowTask.coursewareTaskList != '[]' && tomorrowTask.coursewareTaskList.length > 0){
                                that.isTomorrowCoursewareTask = true;
                            }
                            if (tomorrowTask.practiceTaskList != null && tomorrowTask.practiceTaskList != '[]' && tomorrowTask.practiceTaskList.length > 0){
                                that.isTomorrowPracticeTask = true;
                            }
                            if (that.isTomorrowCourseTask || that.isTomorrowCoursewareTask || that.isTomorrowPracticeTask){
                                that.isTomorrowTask = true;
                            }
                        }
                        if (data.finishTask != null && data.finishTask != '{}'){
                            let finishTask = data.finishTask;
                            if (finishTask.courseTaskList != null && finishTask.courseTaskList != '[]' && finishTask.courseTaskList.length > 0){
                                that.isFinishCourseTask = true;
                            }
                            if (finishTask.coursewareTaskList != null && finishTask.coursewareTaskList != '[]' && finishTask.coursewareTaskList.length > 0){
                                that.isFinishCoursewareTask = true;
                            }
                            if (finishTask.practiceTaskList != null && finishTask.practiceTaskList != '[]' && finishTask.practiceTaskList.length > 0){
                                that.isFinishPracticeTask = true;
                            }
                            if (that.isFinishCourseTask || that.isFinishCoursewareTask || that.isFinishPracticeTask){
                                that.isFinishTask = true;
                            }
                        }
                    }

                });
                // ajax.get('api/system/menu/listPhone', '', function(data) {
                //     alert('aa');
                //     alert(JSON.stringify(data));
                // });
            },
            openTaskDetail: function (taskId) {
                redirect.open('/biz/college/task/task_detail.html?taskId='+taskId, '任务详情');
            }
        },
        mounted () {
            this.init();
        }
    });
    return vm;
})