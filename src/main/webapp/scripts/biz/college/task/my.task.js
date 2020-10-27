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
                                <van-cell-group :title="todayTask.taskType" v-for="todayTask in myTaskInfo.todayTask">
                                    <van-cell :title="item.taskName" v-for="item in todayTask.taskList" is-link :label="item.taskDesc" :url="'/biz/college/task/task_detail.html?taskId=' + item.taskId + '&isFinish=false'" />
                                </van-cell-group>
                            </template>
                            <template v-if="!isTodayTask">
                                <van-empty image="search" description="今日没有需要完成的任务哦" />
                            </template>
                        </van-tab>
                        
                        <van-tab title="明日任务">
                            <template v-if="isTomorrowTask">
                                <van-cell-group :title="tomorrowTask.taskType" v-for="tomorrowTask in myTaskInfo.tomorrowTask">
                                    <van-cell :title="item.taskName" v-for="item in tomorrowTask.taskList" is-link :label="item.taskDesc" :url="'/biz/college/task/task_detail.html?taskId=' + item.taskId + '&isFinish=false'" />
                                </van-cell-group>
                            </template>
                            <template v-if="!isTomorrowTask">
                                <van-empty image="search" description="明日没有需要完成的任务哦" />
                            </template>
                        </van-tab>
                        
                        <van-tab title="已完成">
                            <template v-if="isFinishTask">
                                <van-cell-group :title="finishTask.taskType" v-for="finishTask in myTaskInfo.finishTask">
                                    <van-cell :title="item.taskName" v-for="item in finishTask.taskList" is-link :label="item.taskDesc" :url="'/biz/college/task/task_detail.html?taskId=' + item.taskId + '&isFinish=true'" />
                                </van-cell-group>
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
                        if (data.todayTask != null && data.todayTask != '[]'){
                            that.isTodayTask = true;
                        }
                        if(data.tomorrowTask != null && data.tomorrowTask != '[]'){
                            that.isTomorrowTask = true;
                        }
                        if (data.finishTask != null && data.finishTask != '[]'){
                            that.isFinishTask = true;
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