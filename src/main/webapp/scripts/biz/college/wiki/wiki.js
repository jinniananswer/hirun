require(['vue', 'vant', 'ajax', 'vant-select', 'page-title', 'redirect'], function (Vue, vant, ajax, vantSelect, pageTitle, redirect) {
    let vm = new Vue({
        el: '#app',
        template: `
            <div>
                <page-title title="家装百科"/>
                <br/>
                <br/>
                <br/>
                <div style="margin-top:1em;margin-right:1em;margin-left:1em;">
                    <van-button type="primary" icon="plus" round block url="/biz/college/wiki/wiki_report.html">发百科</van-button>
                </div>
                <van-search
                    v-model="value"
                    shape="round"
                    placeholder="请输入搜索关键词"
                />
                <van-cell style="background-color: #f8f8f8;color:#969799" :center="true" :border="false" is-link title="设计类"  @click="openDetail('1')" value="更多"/>
                <van-cell-group v-for="(item ,index) in designWikis.slice(0,2)">
                    <van-cell is-link to="showWikiDetail(item)" :center="true" border="false" >
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
                
                <van-cell style="background-color: #f8f8f8;color:#969799" :center="true" :border="false" is-link title="基础工程" @click="openDetail('2')" value="更多"/>
                <van-cell-group v-for="(item ,index) in baseWikis.slice(0,2)">
                    <van-cell :title="item.wikiTitle" is-link :label="item.wikiContent" />
                </van-cell-group>
                
                <van-cell style="background-color: #f8f8f8;color:#969799" :center="true" :border="false" is-link title="软装工程"  @click="openDetail('3')" value="更多"/>
                <van-cell-group v-for="(item ,index) in softWikis.slice(0,2)">
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
            },
            openDetail: function (wikiType) {
                redirect.open('/biz/college/wiki/wiki_list.html?wikiType='+wikiType, '更多百科');
            },
            showWikiDetail: function (item) {
                redirect.open('/biz/college/wiki/wiki_detail.html?wikiId='+item.wikiId, '百科详情');
            },
        },
        mounted () {
            this.queryByText('');
        }
    });
    return vm;
})