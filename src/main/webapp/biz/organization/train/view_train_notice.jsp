<%--
  Created by IntelliJ IDEA.
  User: jinnian
  Date: 2019/1/26
  Time: 9:45 PM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>查看岗前考评通知</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/organization/train/view.train.notice.js"></script>
</head>
<body>
<!-- 标题栏 开始 -->
<div class="c_header e_show">
    <div class="back" ontap="$.redirect.closeCurrentPage();">查看岗前考评通知</div>
    <div class="fn">
        <input type="hidden" name="TRAIN_ID" id="TRAIN_ID" nullable="no" value="${pageContext.request.getParameter("TRAIN_ID") }" desc="培训ID" />
    </div>
</div>
<!-- 标题栏 结束 -->
<!-- 滚动（替换为 java 组件） 开始 -->
<div class="c_scroll c_scroll-float c_scroll-header">
    <div class="l_padding">
        <div class="c_title">
            <div class="text"><span class="e_strong" id="notice_title"></span></div>
            <div class="fn">

            </div>
        </div>
        <div class="c_list c_list-line c_list-border c_list-space l_padding">
            <ul id="trains">
                <li>
                    <div class="group">
                        <div class="content">
                            <div class="main">
                                <div class="title">
                                </div>

                                <div class="content content-auto">
                                    <span id="employee"></span>
                                    <br/>
                                    热烈欢迎您来参加鸿扬集团鸿扬学院<span id="train_name" class="e_strong"></span>
                                    <br/>
                                    <br/>
                                    为了给大家营造一个良好的学习环境，保证培训顺利进行并取得较好的培训效果，同时保障每位同事自身财产安全，学院特将相关事项告知如下：
                                    <br/>
                                    一、培训开班时间：<span id="train_date" class="e_strong e_red"></span>
                                    <br/>
                                    二、培训报到时间：<span id="start_sign" class="e_strong e_red"></span>
                                    <br/>
                                    三、本期带班组织：<span id="charge_employee" class="e_strong e_red"></span>
                                    <br/>
                                    四、培训报到地点：长沙市宁乡县金洲新区金洲北路108号鸿扬木制生活楼二楼教务中心（详见指示图）；
                                    <br/>
                                    <img src="/frame/img/trainposition.png"/>
                                    <br/>
                                    五、乘车路线：
                                    <br/>
                                    ①长沙火车站——长沙汽车西站：地铁2号线(望城坡方向),望城坡下(2A/1B口出)；
                                    <br/>
                                    长沙火车南站——长沙汽车西站：地铁2号线(望城坡方向),望城坡下(2A/1B口出)；
                                    <br/>
                                    ②长沙汽车西站——宁乡：可在地铁1B出口处乘坐长沙至宁乡的黄色公交车或者到汽车西站乘坐301路公交车（金洲大道方向，8元/人）到圣德西下车，在标有“空谈误国，实干兴邦”处右拐直走1000m，即到鸿扬木制门口（公司在金洲高速收费站出口处）。
                                    <br/>
                                    六、学员须带物品
                                    <br/>
                                    （一）学习所需物品：笔记本、签字笔；
                                    <br/>
                                    （二）生活所需物品：衣物（女生禁止穿短裤、吊带；男生禁止穿背心）、拖鞋（宿舍洗澡用，禁止穿拖鞋上课）、餐具、水杯及个人日常生活用品。
                                    <br/>
                                    七、培训期间应遵守的日常管理细则：
                                    <br/>
                                    （一）课堂纪律
                                    <br/>
                                    1、课前10分钟进入课堂，并做好上课准备。
                                    <br/>
                                    （1）未提前10分钟进入课堂的，课堂纪律考核项目扣1分/次，出现四次记0分。
                                    <br/>
                                    （2）无故缺勤的，出现一次课堂纪律考核项目扣3分，出现两次记0分。
                                    <br/>
                                    2、上课期间无手机响铃或震动，无玩手机、打瞌睡、随意走动、交头接耳讨论与课程无关问题等现象。
                                    <br/>
                                    （1）手机铃响或震动的，课堂纪律考核项目扣1分/次，出现四次记0分。
                                    <br/>
                                    （2）玩手机、打瞌睡、随意走动、交头接耳讨论与课程无关问题等的，课堂纪律考核项目扣1分/次，出现四次记0分。
                                    <br/>
                                    3、外出观摩学习期间，听从老师指挥，无擅自离队、大声喧哗等现象。
                                    <br/>
                                    不听从老师指挥的，出现一次课堂纪律考核项目扣3分，出现两次记零分；擅自离队的，课堂纪律考核项目扣2分/次，出现两次记零分；大声喧哗的，课堂纪律考核项目扣1分/次，出现三次记零分。
                                    <br/>
                                    （二）培训室值日纪律
                                    <br/>
                                    1、值日当天未提前10分钟开窗通风，未及时擦干净黑板、清理黑板槽及边框、更换没墨白板笔，未打扫卫生、清理垃圾篓，未关窗户、空调、灯、门等的，日常行为项目扣0.5分/项。
                                    <br/>
                                    2、上午、下午、晚上离开培训室前，未将椅子归位、书本及相关物品整齐摆放至右上角的，扣日常行为考核项目0.5分/次/项。
                                    <br/>
                                    （三）食堂就餐纪律：排队、有序就餐。未按整队要求进行排队的，日常行为考核项目扣1分/次。
                                    <br/>
                                    （四）宿舍纪律
                                    <br/>
                                    1、在宿舍内饮酒、赌博的，日常行为考核项目记0分；
                                    <br/>
                                    2、未按要求整理内务的，宿舍未在22：30前熄灯的，日常行为考核项目扣1分/次。
                                    <br/>
                                    （五）考试纪律
                                    <br/>
                                    考试期间严禁东张西望、交头接耳、检阅资料、抄袭等，违者一律0分处理,并全集团通报批评。
                                    <br/>
                                    （六）其他
                                    <br/>
                                    1、在培训期间工厂内所有区域吸烟（除大门口保安室旁的吸烟房外）的，扣2分/次,另根据各分公司《员工奖惩条例》第二章纪律管理2.1.2.2在客户家里或工地上吸烟的，扣30-50分相关条款处罚，并全集团通报批评。
                                    <br/>
                                    2、在教室、楼道等教学区域嚼槟榔、乱吐痰、乱丢垃圾的，在楼道或室内打闹嬉戏的，扣1分/次。
                                    <br/>
                                    3．爱护培训室及宿舍内等其他范围的公共财物，如有人为损坏，照价赔偿。
                                    <br/>
                                    4.已报名参加培训者，如无故缺席培训者或迟到早退者，根据《员工奖惩条例》2.3条会议/培训纪律的2.3.1条相关规定进行处罚，并全集团通报批评。
                                    <br/>
                                    5．培训期间所有学员必须住校，进行全封闭式管理，凡未向带班老师报备而擅自离开学校的，予以淘汰处理；原则上不容许请假，因个人突发事情必须请假的，须经各公司总经理、鸿扬学院副院长确认同意后，方可请假。
                                    <br/>
                                    本细则未尽事项，将根据事件轻重，按公司相关制度进行扣分处罚。
                                    <br/>
                                    <br/>
                                    感谢您对培训工作的支持！

                                </div>
                            </div>
                        </div>
                    </div>
                </li>
            </ul>
        </div>

    </div>
    <div class="c_space-4"></div>
</div>
<!-- 滚动 结束 -->
<jsp:include page="/base/buttom/base_buttom.jsp"/>
<script>
    Wade.setRatio();
    $.train.init();
</script>
</body>
</html>
