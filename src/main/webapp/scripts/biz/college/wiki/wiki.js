require(['vue', 'vant', 'ajax', 'vant-select', 'page-title', 'redirect'], function (Vue, vant, ajax, vantSelect, pageTitle, redirect) {
    let vm = new Vue({
        el: '#app',
        template: `
            <div>
                <page-title title="家装百科"/>
                <van-search
                    v-model="value"
                    shape="round"
                    placeholder="请输入搜索关键词"
                />
                <van-cell style="background-color: #f8f8f8;color:#969799" :center="true" :border="false" is-link title="设计类" value="更多"/>
                <van-cell-group v-for="(item ,index) in designWikis">
                    <van-cell is-link :center="true" border="false" >
                        <template #title>
                            <div class="van-multi-ellipsis">{{item.wikiTitle}}</div>
                        </template>
                        <template #label>
                            <van-row>
                                <div class="van-multi-ellipsis--l2">{{item.wikiContent}}</div>
                            </van-row>
                            <van-row style="padding-top:1em" type="flex" align="bottom" justify="center">
                                <van-col span="6"></van-col>
                                <van-col span="6"></van-col>
                                <van-col span="6">
                                    <van-icon name="good-job-o" size="1.2rem"/>{{item.thumbsUp}}
                                </van-col>
                                <van-col span="6">
                                    <van-icon name="eye-o" size="1.2rem"/> {{item.clicks}}
                                </van-col>
                            </van-row>
                        </template>
                    </van-cell>
                </van-cell-group>
                <br/>
                
                <van-cell style="background-color: #f8f8f8;color:#969799" :center="true" :border="false" is-link title="基础工程" value="更多"/>
                <van-cell-group v-for="(item ,index) in baseWikis">
                    <van-cell :title="item.wikiTitle" is-link :label="item.wikiContent" />
                </van-cell-group>
                
                <van-cell style="background-color: #f8f8f8;color:#969799" :center="true" :border="false" is-link title="软装工程" value="更多"/>
                <van-cell-group v-for="(item ,index) in softWikis">
                    <van-cell :title="item.wikiTitle" is-link :label="item.wikiContent" />
                </van-cell-group>
                
                
            </div>`,
        data: function () {
            return {
                value : '',
                designWikis: [],
                baseWikis: [],
                softWikis:[],
                wiki: {}
            }
        },
        methods: {
            queryByText: function (keyStr) {
                let param = new URLSearchParams()
                param.append('keyStr', '1');
                let that = this;
                ajax.get('api/CollegeWiki/queryByText', param, function(responseData){
                    if (null != responseData) {
                        for (let i = 0; i < responseData.length; i++) {
                            let wiki = responseData[i];

                            if (wiki.wikiType == '1') {
                                that.designWikis.push(wiki);
                            }else if (wiki.wikiType == '2') {
                                that.baseWikis.push(wiki);
                            }else if (wiki.wikiType == '3') {
                                that.softWikis.push(wiki);
                            }
                        }
                    }
                },null, true);
            }
        },
        mounted () {
            this.queryByText('');
        }
    });
    return vm;
})