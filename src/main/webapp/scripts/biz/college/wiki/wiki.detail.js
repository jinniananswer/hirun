require(['vue', 'vant', 'ajax', 'vant-select', 'page-title', 'redirect', 'util'], function (Vue, vant, ajax, vantSelect, pageTitle, redirect, util) {
    let vm = new Vue({
        el: '#app',
        template: `
            <div>
                <page-title title="百科详情"/>
                <div style="margin-top:3.8rem">
                    <!--标题-->
                    <van-cell>
                        <template #title>
                            <div style="font-weight: bold" class="van-multi-ellipsis">{{wikiInfo.wiki.wikiTitle}}</div>
                        </template>
                    </van-cell>
                    <!--作者-->
                    <van-cell>
                        <template #title>
                            <div style="background-color: #f8f8f8;color:#969799;font-size: 10px" :center="true" :border="false">作者：{{wikiInfo.authorName}}</div>
                        </template>
                    </van-cell>
                    <!--内容-->
                    <van-cell>
                        <template #title>
                            <div style="font-size: 12px" :center="true" :border="false">{{wikiInfo.wiki.wikiContent}}</div>
                        </template>
                    </van-cell>
                    <!--回复列表-->
                    <van-divider>评论区</van-divider>
                    <van-cell-group>
                        <van-cell :center="true" border="false" v-for="reply in replyInfos">
                            <template #title>
                                <van-row>
                                    <div class="van-multi-ellipsis--l2">{{reply.replyer}}</div>
                                </van-row>
                            </template>
                            <van-cell>
                                <template #right-icon>
                                    <van-row>
                                        <van-col @click="addThumbs">
                                            <van-icon name="good-job-o" size="1.2rem" @click="addThumbs"/>{{reply.thumbsUp}}
                                        </van-col>
                                    </van-row>
                                </template>
                            </van-cell>
                            <template #label>
                                <van-row>
                                    <div class="van-multi-ellipsis--l2">{{reply.replyContent}}</div>
                                </van-row>
                                <van-row>
                                    <div class="van-multi-ellipsis--l2">{{reply.replyTime}}</div>
                                </van-row>
                            </template>
                        </van-cell>
                    </van-cell-group>
                    <!--回复-->
                    <van-field
                        v-model="replyContent"
                        rows="1"
                        autosize
                        type="textarea"
                        placeholder="我要发言"
                    >
                        <template #button>
                            <van-button size="small" type="primary" @click="reply">回复</van-button>
                        </template>
                    </van-field>
                </div>
                
                <bottom></bottom>
            </div>`,
        data: function () {
            return {
                value : '',
                active : 0,
                wikiInfo: {
                    wiki: {},
                },
                wikiId: util.getRequest("wikiId"),
                replyInfos: [],
                replyContent: '',
            }
        },
        methods: {
            init: function () {
                let that = this
                let param = new URLSearchParams()
                param.append("wikiId", that.wikiId);
                ajax.get('/api/CollegeWiki/getDetailByWikiId', param , function(data) {
                    if (null != data && undefined != data) {
                        that.wikiInfo = data;
                        that.replyInfos = that.wikiInfo.replyInfos;
                    }
                });
            },

            addClicks: function () {

            },

            addThumbs: function () {
                alert(1);
            },

            reply: function () {
                alert(this.replyContent);
                let that = this;
                let param = new URLSearchParams()
                param.append("replyContent", that.replyContent);
                param.append("wikiId", that.wikiId);
                ajax.post('/api/CollegeWiki/replyWiki', param, function(data) {
                    that.replyContent = '';
                    that.init();
                });
            }
        },
        mounted () {
            this.init();
        }
    });
    return vm;
})