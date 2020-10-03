require(['vue', 'vant', 'ajax', 'vant-select', 'page-title', 'redirect'], function (Vue, vant, ajax, vantSelect, pageTitle, redirect) {
    let vm = new Vue({
        el: '#app',
        template: `
            <div>
                <van-nav-bar
                    title="鸿助手"
                    :left-arrow="false"
                    :fixed="true"
                    :z-index="99"
                    style="background-color: #141516;padding-top: 0.5em;padding-bottom: 0.5em">
                    <template #title>
                        <div class="van-ellipsis" style="color:#8a8b8b">鸿助手</div>
                    </template>
                </van-nav-bar>
                <van-cell :title="employee.name" center="true" :label="employee.orgName + '/' + employee.jobRoleName"  style="margin-top:3.8rem">
                    <template #icon>
                        <van-image
                            round
                            width="3.5em"
                            height="3.5em"
                            fit="cover"
                            :src="employee.sex == '1'? '/frame/img/male.jpg' : '/frame/img/female.jpg'"
                            style="margin-right: 1em"
                        />
                    </template>
                </van-cell>
                <van-tabs v-model="activeKey">
                    <van-tab :title="menu.title" v-for="menu in menus">
                        <van-cell-group title="计划总结">
                            <van-grid :column-num="3" square>
                                <van-grid-item icon="todo-list-o" color="#59bd6d" text="计划录入" >
                                    <template #icon>
                                        <van-icon name="todo-list-o" size="2rem" color="#59bd6d" />
                                    </template>
                                </van-grid-item>
                                <van-grid-item icon="bookmark-o" text="当日总结">
                                    <template #icon>
                                        <van-icon name="bookmark-o" size="2rem" color="#ec9f51" />
                                    </template>
                                </van-grid-item>
                                <van-grid-item icon="notes-o" text="计划补录">
                                    <template #icon>
                                        <van-icon name="notes-o" size="2rem" color="#417ef1" />
                                    </template>
                                </van-grid-item>
                                <van-grid-item icon="label-o" color="#4faaf8" text="总结补录">
                                    <template #icon>
                                        <van-icon name="label-o" size="2rem" color="#4faaf8" />
                                    </template>
                                </van-grid-item>
                                <van-grid-item icon="label-o" color="#4faaf8" text="休假填写">
                                    <template #icon>
                                        <van-icon name="birthday-cake-o" size="2rem" color="#e5605b" />
                                    </template>
                                </van-grid-item>
                                <van-grid-item icon="todo-list-o" color="#59bd6d" text="我的计划" >
                                    <template #icon>
                                        <van-icon name="todo-list-o" size="2rem" color="#59bd6d" />
                                    </template>
                                </van-grid-item>
                                <van-grid-item icon="todo-list-o" text="员工月度目标设定">
                                    <template #icon>
                                        <van-icon name="calendar-o" size="2rem" color="#ec9f51" />
                                    </template>
                                </van-grid-item>
                            </van-grid>
                        </van-cell-group>
                        <van-cell-group title="楼盘规划">
                            <van-grid :column-num="3" square>
                                <van-grid-item icon="todo-list-o" color="#59bd6d" text="新增楼盘" >
                                    <template #icon>
                                        <van-icon name="home-o" size="2rem" color="#59bd6d" />
                                    </template>
                                </van-grid-item>
                                <van-grid-item icon="bookmark-o" text="楼盘查询">
                                    <template #icon>
                                        <van-icon name="hotel-o" size="2rem" color="#ec9f51" />
                                    </template>
                                </van-grid-item>
                                <van-grid-item icon="notes-o" text="新增散盘">
                                    <template #icon>
                                        <van-icon name="shop-collect-o" size="2rem" color="#417ef1" />
                                    </template>
                                </van-grid-item>
                                <van-grid-item icon="label-o" color="#4faaf8" text="散盘查询">
                                    <template #icon>
                                        <van-icon name="shop-o" size="2rem" color="#4faaf8" />
                                    </template>
                                </van-grid-item>
                                <van-grid-item icon="label-o" color="#4faaf8" text="我的楼盘">
                                    <template #icon>
                                        <van-icon name="home-o" size="2rem" color="#e5605b" />
                                    </template>
                                </van-grid-item>
                                <van-grid-item icon="todo-list-o" color="#59bd6d" text="我的散盘" >
                                    <template #icon>
                                        <van-icon name="shop-o" size="2rem" color="#59bd6d" />
                                    </template>
                                </van-grid-item>
                            </van-grid>
                        </van-cell-group>
                        <br/>
                        <br/>
                        <br/>
                    </van-tab>
                    <van-tab title="组织机构">组织机构</van-tab>
                    <van-tab title="培训学习">培训学习</van-tab>
                    <van-tab title="数据中心">数据中心</van-tab>
                    <van-tab title="数据中心">供应链</van-tab>
                    <van-tab title="数据中心">财务管理</van-tab>
                </van-tabs>
                
                <van-tabbar v-model="active" :z-index="99" style="padding-top:0.5rem;padding-bottom: 0.5rem;background-color: #141516">
                    <van-tabbar-item icon="home-o">
                        <template #icon>
                            <van-icon name="wap-home" size="2rem" color="#57be6a" />
                        </template>
                        <div style="color:#57be6a">首页</div>
                    </van-tabbar-item>
                    <van-tabbar-item icon="search">
                        <template #icon>
                            <van-icon name="chat-o" size="2rem" />
                        </template>
                        消息
                    </van-tabbar-item>
                    <van-tabbar-item icon="friends-o">
                        <template #icon>
                            <van-icon name="setting-o" size="2rem"/>
                        </template>
                        工具
                    </van-tabbar-item>
                </van-tabbar>
            </div>`,
        data: function () {
            return {
                option: 'B',
                value: '22c067',
                activeKey: 0,
                active: 0,

                employee : {},
                menus: []
            }
        },
        methods: {
            init : function() {
                let that = this;
                ajax.get('api/organization/employee/getLoginEmployee', '', function(data) {
                    that.employee = data;
                });
              // ajax.get('api/system/menu/listPhone', '', function(data) {
              //     alert('aa');
              //     alert(JSON.stringify(data));
              // });
            },
            submit : function() {

            },

            switchOption : function(value) {
                this.option = value;
            }
        },
        mounted () {
            this.init();
        }
    });
    return vm;
})