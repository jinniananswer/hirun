require(['vue', 'vant', 'ajax', 'vant-select', 'page-title', 'redirect', 'util', 'axios', 'vant-upload-img'], function (Vue, vant, ajax, vantSelect, pageTitle, redirect, util, axios, vantUploadImg) {
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
                         <vant-upload-img ref="uploadExperienceImg" maxCount="3" text="心得图片"></vant-upload-img>
                        <!--<van-uploader v-model="experienceImgList" ref="upload" upload-text="心得图片" :after-read="uploadOne" multiple :max-count="5" />-->
                    </van-cell-group>
                    <van-cell-group title="上传照片">
                        <vant-upload-img ref="uploadTaskImg" maxCount="5" text="心得图片"></vant-upload-img>
                        <!--<van-uploader v-model="fileList" ref="upload" upload-text="上传图片" :after-read="uploadOne" multiple :max-count="5" />-->
                    </van-cell-group>
                    <div style="margin-top:1em;margin-right:1em;margin-left:1em;margin-bottom:1em">
                        <van-row :gutter="20">
                            <van-col span="12">
                                <van-button  @click="submitExperience()" type="primary" icon="plus" round block>提交心得</van-button>
                            </van-col>
                        </van-row>
                    </div>
                    <vant-upload-img ref="upload" :maxCount="5"></vant-upload-img>
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
                experienceImgList: []
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
                let experienceImgFileId = this.$refs.uploadExperienceImg.fileId;
                if((null == experience || undefined == experience || '' == experience) && (null == experienceImgFileId || undefined == experienceImgFileId || '' == experienceImgFileId)){
                    vm.$toast.fail('请输入心得描述或上传心得图票再提交！');
                    return;
                }
                let taskImgFileId = this.$refs.uploadTaskImg.fileId;
                if (null == taskImgFileId || undefined == taskImgFileId || '' == taskImgFileId){
                    vm.$toast.fail('请上传照片后再提交！');
                    return;
                }
                //let formData = new FormData();
                /*let param = new URLSearchParams()
                param.append('taskId', taskId)
                param.append('experience', experience)
                param.append('fileList', fileIdList)
                param.append('experienceImgList', experienceImgIdList)*/
                let request = {};
                request.taskId = taskId;
                request.experience = experience;
                request.taskImgFileId = taskImgFileId;
                request.experienceImgFileId = experienceImgFileId;
                ajax.post('/api/CollegeTaskExperience/addExperience', request, function(data) {
                    redirect.open('/biz/college/task/task_detail.html?taskId=' + taskId + '&isFinish=true', '任务详情');
                });
            },
            uploadOne: function (file) {
                let formData = new FormData();
                formData.append("file",file.file);
                formData.append("taskId", this.taskId);
                axios.post('api/system/file/uploadOne', formData, function(data) {
                    alert(JSON.stringify(data));
                });
            },
        },
        mounted () {

        }
    });
    return vm;
})