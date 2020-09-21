define(['vue','vant', 'ajax'], function(Vue,vant, ajax){
    Vue.component('vant-select', {
        props: ['label', 'value', 'code-type'],

        data : function(){
            return {
                text: null,
                sValue: this.value,
                showPicker: false,
                columns: [],
                loading: true,
                defaultIndex: 1
            }
        },

        template: `
            <div>
                <van-field
                        readonly
                        clickable
                        :name="label"
                        :value="text"
                        :label="label"
                        size="large"
                        :loading="loading"
                        placeholder="点击选择"
                        @click="showPicker = true"
                />
        
                <van-popup v-model="showPicker" position="bottom">
                    <van-picker
                            :columns="columns"
                            title="请选择"
                            :defaultIndex="defaultIndex"
                            @cancel="this.showPicker=false"
                            @change="change"
                    />
                </van-popup>
            </div>
            `,

        methods: {
            init() {
                let that = this;
                ajax.get('/api/system/static-data/getByCodeType', {codeType: this.codeType}, function(response) {
                    let data = response;
                    alert(data.length);
                    if (!data || data.length <= 0) {
                        return;
                    }
                    for (let i=0;i<data.length;i++) {
                        let column = {
                            id : data[i].codeValue,
                            text : data[i].codeName
                        }

                        that.columns.push(column);
                    }
                });
            },

            change(picker, value) {
                this.showPicker = false;
                this.sValue = value.id;
                this.text = value.text;
            },

            confirm() {

            }
        },

        watch: {
            value(val) {
                this.sValue = val;
            },

            sValue(val, oldValue) {
                if (val != oldValue) {
                    this.$emit("input", val);
                }
            }
        },

        mounted () {
            this.init();
        }
    });


})