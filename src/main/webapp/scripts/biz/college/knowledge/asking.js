require(['vue', 'vant', 'ajax', 'vant-select', 'page-title', 'redirect'], function (Vue, vant, ajax, vantSelect, pageTitle, redirect) {
    let vm = new Vue({
        el: '#app',
        template: `
            <div>
                <page-title title="我要提问"/>
                <van-cell-group title="请填写问题信息">
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
                        <div style="margin: 16px;">
                            <van-button round block type="info" native-type="submit">
                                提交
                            </van-button>
                        </div>
                    </van-form>
                </van-cell-group>
                
            </div>`,
        data: function () {
            return {
                title : '',
                desc : ''
            }
        },
        methods: {
            submit: function() {

            }
        },
        mounted () {

        }
    });
    return vm;
})