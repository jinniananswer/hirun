require(['vue', 'vant', 'ajax', 'vant-select', 'page-title', 'redirect'], function (Vue, vant, ajax, vantSelect, pageTitle, redirect) {
    let vm = new Vue({
        el: '#app',
        template: `
            <div>
                <page-title title="考试"/>
                <van-radio-group v-model="option">
                    <van-cell-group title="第一题（单选）">
                        <van-cell title="整体产品是由多个单体家装产品、按照鸿扬的规范标准组成的多层次复合产品，横向由（  ）大分类产品组成"></van-cell>
                        <van-cell value="A.坚持产品领先" center="true">
                            <template #right-icon>
                                <van-radio name="A" checked-color="#07c160"></van-radio>
                            </template>
                        </van-cell>
                        
                        <van-cell value="B.坚持产品领先" center="true">
                            <template #right-icon>
                                <van-radio name="B" checked-color="#07c160"></van-radio>
                            </template>
                        </van-cell>
                        <van-cell value="C.坚持产品领先" center="true">
                            <template #right-icon>
                                <van-radio name="C" checked-color="#07c160"></van-radio>
                            </template>
                        </van-cell>
                        <van-cell value="D.坚持产品领先" center="true">
                            <template #right-icon>
                                <van-radio name="D" checked-color="#07c160"></van-radio>
                            </template>
                        </van-cell>
                    </van-cell-group>
                </van-radio-group>

            </div>`,
        data: function () {
            return {
                option: 'B',
                value: ''
            }
        },
        methods: {
            submit : function() {

            },

            switchOption : function(value) {
                this.option = value;
            }
        },
        mounted () {

        }
    });
    return vm;
})