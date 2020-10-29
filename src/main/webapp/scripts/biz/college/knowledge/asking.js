require(['vue', 'vant', 'ajax', 'vant-select', 'page-title', 'redirect', 'util'], function (Vue, vant, ajax, vantSelect, pageTitle, redirect, util) {
    let vm = new Vue({
        el: '#app',
        template: `
            <div>
                <page-title title="我要提问"/>
                <div style="margin-top:3.8rem">
                    <van-form @submit="submit">
                        <van-field
                            v-model="title"
                            name="问题标题"
                            label="问题标题"
                            required
                            left-icon="smile-o"
                            placeholder="请输入问题的标题"/>
                        <van-field
                            v-model="desc"
                            name="问题描述"
                            label="问题描述"
                            required
                            placeholder="请输入问题描述"
                            type="textarea"
                            rows="6"
                            autosize
                            left-icon="music-o"
                            maxlength="200"
                            show-word-limit/>
                        <van-field
                          readonly
                          clickable
                          label="问题类型"
                          :value="questionType"
                          placeholder="问题类型"
                          @click="selectQuestionType"
                        />
                        <van-popup v-model="showQuestionType" round position="bottom">
                          <van-picker
                            show-toolbar
                            :columns="selectQuestionTypeList"
                            @cancel="showQuestionType = false"
                            @confirm="onConfirmType"
                          />
                        </van-popup>
                        
                        <van-field
                          readonly
                          clickable
                          label="回答老师"
                          :value="teacher"
                          placeholder="回答老师"
                          @click="selectQuestionTeacher"
                        />
                        <van-popup v-model="showQuestionTeacher" round position="bottom">
                          <van-picker
                            show-toolbar
                            :columns="selectQuestionTeacherList"
                            @cancel="showQuestionTeacher = false"
                            @confirm="onConfirmTeacher"
                          />
                         </van-popup>
                        <div style="margin: 16px;">
                            <van-button round block type="info" native-type="submit">
                                提交
                            </van-button>
                        </div>
                    </van-form>
                </div>
                <bottom :active="2"></bottom>
            </div>`,
        data: function () {
            return {
                title : '',
                desc : '',
                selectQuestionTypeList: [],
                selectQuestionTeacherList: [],
                questionType: '',
                teacher: '',
                showQuestionType: false,
                showQuestionTeacher: false
            }
        },
        methods: {
            submit: function() {
                let that = this;
                let param = new URLSearchParams()
                param.append("questionType", that.questionType);
                param.append("teacherId", that.teacher);
                param.append("title", that.title);
                param.append("desc", that.desc);
                ajax.post('/api/CollegeQuestion/addQuestionByType', param, function(data) {
                    redirect.open('/biz/college/knowledge/square.html', '上传心得');
                });
            },
            selectQuestionType: function () {
                let that = this;
                ajax.get('/api/CollegeQuestion/queryQuestionTypeOptions', '', function(data) {
                    data.forEach((tutor) => {
                        that.selectQuestionTypeList.push("[" + tutor.value + "]" + tutor.name);
                    })
                    that.showQuestionType = true;
                });
            },
            selectQuestionTeacher: function(){
                let that = this;
                ajax.get('/api/CollegeQuestion/queryQuestionTeacherOptions', '', function(data) {
                    data.forEach((tutor) => {
                        that.selectQuestionTeacherList.push("[" + tutor.value + "]" + tutor.name);
                    })
                    that.showQuestionTeacher = true;
                });
            },
            onConfirmType: function (value) {
                this.questionType = value;
                this.showQuestionType = false;
            },
            onConfirmTeacher: function (value) {
                this.teacher = value;
                this.showQuestionTeacher = false;
            }
        },
        mounted () {

        }
    });
    return vm;
})