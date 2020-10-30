require(['vue', 'vant', 'ajax', 'vant-select', 'page-title', 'redirect', 'util'], function (Vue, vant, ajax, vantSelect, pageTitle, redirect, util) {
    let vm = new Vue({
        el: '#app',
        template: `
            <div>
                <br/>
                <br/>
                <br/>
                <page-title title="我要发表"/>
                <van-cell-group title="请填写百科标题">
                    <van-form @submit="submit">
                        <van-field
                            v-model="wikiTitle"
                            name="百科标题"
                            label="百科标题"
                            required
                            left-icon="smile-o"
                            placeholder="请输入百科的标题"/>
                        <van-field
                            v-model="wikiContent"
                            name="百科内容"
                            label="百科内容"
                            required
                            placeholder="请输入百科内容"
                            type="textarea"
                            rows="6"
                            autosize
                            left-icon="music-o"
                            maxlength="200"
                            show-word-limit/>
                        <van-field
                          readonly
                          clickable
                          label="百科类型"
                          :value="questionType"
                          placeholder="百科类型"
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
               
                        <div style="margin: 16px;">
                            <van-button round block type="info" native-type="submit">
                                提交
                            </van-button>
                        </div>
                    </van-form>
                </van-cell-group>
                <bottom :active="2"></bottom>
            </div>`,
        data: function () {
            return {
                wikiContent : '',
                selectQuestionTypeList: [],
                selectQuestionTeacherList: [],
                questionType: '',
                wikiTitle: '',
                showQuestionType: false,
                showQuestionTeacher: false
            }
        },
        methods: {
            submit: function() {
                let that = this;
                let param = new URLSearchParams()
                param.append("wikiType", that.questionType);
                param.append("wikiTitle", that.wikiTitle);
                param.append("wikiContent", that.wikiContent);
                ajax.post('/api/CollegeWiki/addWiki', param, function(data) {
                    redirect.open('/biz/college/wiki/wiki.html', '上传百科');
                });
            },
            selectQuestionType: function () {
                let that = this;
                ajax.get('/api/CollegeQuestion/queryWikiOptions', '', function(data) {
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