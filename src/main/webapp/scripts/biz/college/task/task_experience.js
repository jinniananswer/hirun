require(['vue', 'vant', 'ajax', 'vant-select', 'page-title', 'redirect', 'util', 'axios'], function (Vue, vant, ajax, vantSelect, pageTitle, redirect, util, axios) {
    let vm = new Vue({
        el: '#app',
        template: `
            <div>
                <page-title title="任务心得填写"/>
                <div style="margin-top:3.8rem">
                    <van-cell-group title="心得体会">
                        <van-field
                          v-model="experience"
                          rows="1"
                          autosize
                          label="心得描述"
                          type="textarea"
                          placeholder="请输入心得体会"
                        />
                    </van-cell-group>
                    <van-cell-group title="上传照片">
                        <van-uploader v-model="fileList" ref="upload" upload-text="上传图片" :after-read="uploadOne" multiple :max-count="5" />
                    </van-cell-group>
                    <div style="margin-top:1em;margin-right:1em;margin-left:1em;margin-bottom:1em">
                        <van-row :gutter="20">
                            <van-col span="12">
                                <van-button  @click="submitExperience()" type="primary" icon="plus" round block>提交心得</van-button>
                            </van-col>
                        </van-row>
                    </div>
                </div>
                <bottom :active="2"></bottom>
            </div>`,
        data: function () {
            return {
                value : 2.5,
                active : 0,
                taskId: util.getRequest("taskId"),
                experience: '',
                fileList: [],
            }
        },
        methods: {
            submitExperience : function() {
                let that = this;
                let taskId=that.taskId;
                if(taskId=='undefined'){
                    taskId=null;
                }
                let experience = that.experience;
                if(null == experience || undefined == experience || '' == experience){
                    vm.$toast.fail('请输入心得描述再提交！');
                    return;
                }
                let fileList = that.fileList
                if (null == fileList || undefined == fileList || [] == fileList || 0 == fileList.length){
                    vm.$toast.fail('请上传照片后再提交！');
                    return;
                }
                let fileIdList = ['49fab7a3-992b-4242-9074-2d20ac4293f1', '49fab7a3-992b-4242-9074-2d20ac4293f1'];
                let param = new URLSearchParams()
                param.append('taskId', taskId)
                param.append('experience', experience)
                param.append('fileIdList', fileIdList)
                ajax.post('/api/CollegeTaskExperience/addExperience', param, function(data) {
                    redirect.open('/biz/college/task/task_detail.html?taskId=' + taskId + '&isFinish=true', '任务详情');
                });
            },
            uploadOne: function (file) {
                let param = new URLSearchParams()
                param.append('file', file.file)
                const token = sessionStorage.getItem('hirun-helper-jwt')
                axios.post('api/system/file/uploadOne', param, {headers: {'Content-Type': 'multipart/form-data'}}).then(res => {

                }).catch(error => {
                    alert('更新用户数据失败' + error)
                })

            },
        },
        mounted () {

        }
    });
    return vm;
})