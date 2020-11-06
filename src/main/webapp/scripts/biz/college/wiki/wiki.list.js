require(['vue', 'vant', 'ajax', 'vant-select', 'page-title', 'redirect', 'util'], function (Vue, vant, ajax, vantSelect, pageTitle, redirect, util) {
    let vm = new Vue({
        el: '#app',
        template: `
            <div>
                <page-title title="百科列表"/>
                <div style="margin-top:3.8rem">
                    <van-cell-group v-for="wikis in wikiInfos">
                        <van-cell style="background-color: #f8f8f8;color:#969799" :center="true" :border="false" :title="wikis.wikiTypeName"/>
                        <van-cell-group>
                            <van-cell is-link :center="true" border="false" v-for="item in wikis.wikiList" @click="showWikiDetail(item)">
                                <template #title>
                                    <div class="van-multi-ellipsis">{{item.wikiTitle}}</div>
                                </template>
                                <template #label>
                                    <van-row>
                                        <div class="van-multi-ellipsis--l2">{{item.wikiContent}}</div>
                                    </van-row>
                                    <van-row style="padding-top:1em" type="flex" align="bottom" justify="center">
                                        <van-col span="6"></van-col>
                                        <van-col span="6" @click="addThumbs">
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
                    </van-cell-group>
                </div>
                
                <bottom></bottom>
            </div>`,
        data: function () {
            return {
                value : '',
                active : 0,
                wikiType: util.getRequest("wikiType"),
                wikiInfos: [],
            }
        },
        methods: {
            init: function () {
                let that = this
                let param = new URLSearchParams()
                param.append("wikiType", that.wikiType);
                ajax.get('/api/CollegeWiki/queryWikiByType', param , function(data) {
                    that.wikiInfos = data;
                });
            },

            showWikiDetail: function (item) {
                redirect.open('/biz/college/wiki/wiki_detail.html?wikiId='+item.wikiId, '百科详情');
            },

            addClicks: function () {

            },

            addThumbs: function () {
                alert(1);
            },
        },
        mounted () {
            this.init();
        }
    });
    return vm;
})