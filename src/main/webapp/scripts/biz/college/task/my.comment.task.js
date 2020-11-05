require(['vue', 'vant', 'ajax', 'vant-select', 'page-title', 'redirect', 'util'], function (Vue, vant, ajax, vantSelect, pageTitle, redirect, util) {
    let vm = new Vue({
        el: '#app',
        template: `
            <div>
                <page-title title="我的点评"/>
                <div style="margin-top:3.8rem">
                    <van-tabs v-model="active">
                        <van-tab title="待点评任务">
                            <template v-if="isNoComment">
                                <van-cell :title="item.employeeName + ':' + item.taskName" v-for="item in commentTaskInfo.noComment" is-link :label="item.taskDesc" :url="'/biz/college/task/task_comment_detail.html?taskId=' + item.taskId + '&isFinish=false'" />
                            </template>
                            <template v-if="!isNoComment">
                                <van-empty image="search" description="没有需要点评的任务哦" />
                            </template>
                        </van-tab>
                        
                        <van-tab title="已点评任务">
                            <template v-if="isFinishComment">
                                <van-cell :title="item.employeeName + ':' + item.taskName" v-for="item in commentTaskInfo.finishComment" is-link :label="item.taskDesc" :url="'/biz/college/task/task_comment_detail.html?taskId=' + item.taskId + '&isFinish=true'" />
                            </template>
                            <template v-if="!isFinishComment">
                                <van-empty image="search" description="没有已经评点的任务哦" />
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
                commentTaskInfo: {},
                isNoComment: false,
                isFinishComment: false,
            }
        },
        methods: {
            init : function() {
                let that = this;
                ajax.get('api/CollegeEmployeeTask/queryLoginEmployeeCommentTaskInfo', '', function(data) {
                    that.commentTaskInfo = data;
                    if (data != null && data != '{}'){
                        if (data.noComment != null && data.noComment != '[]'){
                            that.isNoComment = true;
                        }
                        if(data.finishComment != null && data.finishComment != '[]'){
                            that.isFinishComment = true;
                        }
                    }

                });
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