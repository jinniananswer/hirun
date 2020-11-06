define(['vue','vant', 'ajax', 'axios'], function(Vue,vant,ajax, axios){
    Vue.component('vant-upload-img', {
        props: ['maxCount', 'text'],

        data : function(){
            return {
                text: this.text,
                sMaxCount: this.maxCount,
                fileList: []
            }
        },

        template: `
            <div>
                <van-field name="fileId" id="fileId" type="hidden" v-model="fileId"></van-field>
               <van-uploader v-model="fileList" :upload-text="text" :after-read="uploadImg" :max-count="sMaxCount" />
            </div>
            
            `,


        computed: {
            fileId: function () {
                let fileId = '';
                this.fileList.forEach(function (e, index, array) {
                    fileId += e.response
                    if (index < array.length - 1) {
                        fileId += ','
                    }
                })
                return fileId;
            }
        },

        methods: {
            uploadImg(file) {
                let that = this;
                let formData = new FormData();
                formData.append("file",file.file);
                let fileList = that.fileList;
                axios.post('api/system/file/uploadOne', formData)
                    .then((res) => {
                        file.response = res.data
                        that.fileList = []
                        that.fileList = fileList
                });
            }
        },

        watch: {

        },

        mounted () {

        }
    });


})