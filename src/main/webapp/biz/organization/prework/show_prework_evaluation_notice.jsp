<%--
  Created by IntelliJ IDEA.
  User: jinnian
  Date: 2019/1/24
  Time: 11:45 AM
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
    <script src="/scripts/biz/organization/prework/show.prework.evaluation.notice.js?v=20190321"></script>
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
        <div class="c_article">
            <div class="title">岗前考评通知</div>
            <div id="subtitle" name="subtitle" class="subtitle"></div>
            <div class="content content-auto">
                <span class="e_strong">鸿扬集团总部、家装事业部转各家装分公司、鸿扬木制、鸿扬宅配：</span><br/>
                鸿扬集团总部人力资源与公共事务管理中心将在<span id="simple_date"></span>组织集团总部、各家装分公司、鸿扬木制员工进行岗前培训考评，现将有关事项通知如下：
                <br/>
                <span class="e_strong">一、考评时间及地点：</span>
                <br/>
                时间：<span id="date"></span>
                <br/>
                地点：<span id="address"></span>
                <br/>
                <span class="e_strong">二、主持单位：</span>鸿扬集团总部
                <br/>
                <span class="e_strong">三、参加人员：</span>
                <br/>
                1、截至<span id="new_start_date"></span>，进公司满60—75天的新员工；
                <br/>
                2、参加过一次考评，但通用或专业科目未达80分的员工；
                <br/>
                4、转岗已满一个月的员工。
                <br/>
                <span class="e_strong">四、考评安排：</span>
                <br/>
                以下为初次考评时间安排，调职和补考的，根据初次考评时间进行相应科目考评。
                <br/>
                <div class="c_list c_list-line c_list-border c_list-space l_padding">
                    <ul>
                        <li class='link'>
                            <div class="group">
                                <div class="content">
                                    <div class='l_padding'>
                                        <div class="pic pic-middle">
                                        </div>
                                    </div>
                                    <div class="main">
                                        <div class="title">
                                            设计师协会
                                        </div>
                                        <div class="content content-auto">
                                            报到时间：<span id="design_sign_date"></span>
                                        </div>
                                        <div class="content content-auto">
                                            考试时间：<span id="design_start_date"></span>
                                        </div>
                                        <div class="content content-auto">
                                            主持人：陈志斌、李强
                                        </div>
                                        <div class="content content-auto">
                                            参加人员：方案设计师、助理设计师
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </li>
                        <li class='link'>
                            <div class="group">
                                <div class="content">
                                    <div class='l_padding'>
                                        <div class="pic pic-middle">
                                        </div>
                                    </div>
                                    <div class="main">
                                        <div class="title">
                                            制作设计师协会
                                        </div>
                                        <div class="content content-auto">
                                            报到时间：<span id="make_design_sign_date"></span>
                                        </div>
                                        <div class="content content-auto">
                                            考试时间：<span id="make_design_start_date"></span>
                                        </div>
                                        <div class="content content-auto">
                                            主持人：张瑞、朱平
                                        </div>
                                        <div class="content content-auto">
                                            参加人员：制作设计师
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </li>
                        <li class='link'>
                            <div class="group">
                                <div class="content">
                                    <div class='l_padding'>
                                        <div class="pic pic-middle">
                                        </div>
                                    </div>
                                    <div class="main">
                                        <div class="title">
                                            客户代表协会
                                        </div>
                                        <div class="content content-auto">
                                            报到时间：<span id="customer_sign_date"></span>
                                        </div>
                                        <div class="content content-auto">
                                            考试时间：<span id="customer_start_date"></span>
                                        </div>
                                        <div class="content content-auto">
                                            主持人：杜世昌、徐灿
                                        </div>
                                        <div class="content content-auto">
                                            参加人员：客户代表、客户文员
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </li>
                        <li class='link'>
                            <div class="group">
                                <div class="content">
                                    <div class='l_padding'>
                                        <div class="pic pic-middle">
                                        </div>
                                    </div>
                                    <div class="main">
                                        <div class="title">
                                            橱柜设计师协会
                                        </div>
                                        <div class="content content-auto">
                                            报到时间：<span id="cg_design_sign_date"></span>
                                        </div>
                                        <div class="content content-auto">
                                            考试时间：<span id="cg_design_start_date"></span>
                                        </div>
                                        <div class="content content-auto">
                                            主持人：曾令军、陈谨
                                        </div>
                                        <div class="content content-auto">
                                            参加人员：橱柜设计师
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </li>
                        <li class='link'>
                            <div class="group">
                                <div class="content">
                                    <div class='l_padding'>
                                        <div class="pic pic-middle">
                                        </div>
                                    </div>
                                    <div class="main">
                                        <div class="title">
                                            家装顾问协会
                                        </div>
                                        <div class="content content-auto">
                                            报到时间：<span id="counselor_sign_date"></span>
                                        </div>
                                        <div class="content content-auto">
                                            考试时间：<span id="counselor_start_date"></span>
                                        </div>
                                        <div class="content content-auto">
                                            主持人：唐晓江、伍迎湘
                                        </div>
                                        <div class="content content-auto">
                                            参加人员：家装/家居顾问、小区推广员、市场文员、小区宣传员等
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </li>
                        <li class='link'>
                            <div class="group">
                                <div class="content">
                                    <div class='l_padding'>
                                        <div class="pic pic-middle">
                                        </div>
                                    </div>
                                    <div class="main">
                                        <div class="title">
                                            会计协会
                                        </div>
                                        <div class="content content-auto">
                                            报到时间：<span id="kj_sign_date"></span>
                                        </div>
                                        <div class="content content-auto">
                                            考试时间：<span id="kj_start_date"></span>
                                        </div>
                                        <div class="content content-auto">
                                            主持人：旷红斌、魏姣
                                        </div>
                                        <div class="content content-auto">
                                            参加人员：会计、出纳
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </li>
                        <li class='link'>
                            <div class="group">
                                <div class="content">
                                    <div class='l_padding'>
                                        <div class="pic pic-middle">
                                        </div>
                                    </div>
                                    <div class="main">
                                        <div class="title">
                                            人资协会
                                        </div>
                                        <div class="content content-auto">
                                            报到时间：<span id="rz_sign_date"></span>
                                        </div>
                                        <div class="content content-auto">
                                            考试时间：<span id="rz_start_date"></span>
                                        </div>
                                        <div class="content content-auto">
                                            主持人：薛帅、蒋丹
                                        </div>
                                        <div class="content content-auto">
                                            参加人员：人资专干、人资文员
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </li>
                        <li class='link'>
                            <div class="group">
                                <div class="content">
                                    <div class='l_padding'>
                                        <div class="pic pic-middle">
                                        </div>
                                    </div>
                                    <div class="main">
                                        <div class="title">
                                            工程协会
                                        </div>
                                        <div class="content content-auto">
                                            报到时间：<span id="gc_sign_date"></span>
                                        </div>
                                        <div class="content content-auto">
                                            考试时间：<span id="gc_start_date"></span>
                                        </div>
                                        <div class="content content-auto">
                                            主持人：王召、陈波
                                        </div>
                                        <div class="content content-auto">
                                            参加人员：项目经理、工程助理（文员）
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </li>
                        <li class='link'>
                            <div class="group">
                                <div class="content">
                                    <div class='l_padding'>
                                        <div class="pic pic-middle">
                                        </div>
                                    </div>
                                    <div class="main">
                                        <div class="title">
                                            供应链协会
                                        </div>
                                        <div class="content content-auto">
                                            报到时间：<span id="gyl_sign_date"></span>
                                        </div>
                                        <div class="content content-auto">
                                            考试时间：<span id="gyl_start_date"></span>
                                        </div>
                                        <div class="content content-auto">
                                            主持人：文春桥、贺秋
                                        </div>
                                        <div class="content content-auto">
                                            参加人员：采购员、主材下单员
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </li>
                        <li class='link'>
                            <div class="group">
                                <div class="content">
                                    <div class='l_padding'>
                                        <div class="pic pic-middle">
                                        </div>
                                    </div>
                                    <div class="main">
                                        <div class="title">
                                            订单管理协会
                                        </div>
                                        <div class="content content-auto">
                                            报到时间：<span id="ddgl_sign_date"></span>
                                        </div>
                                        <div class="content content-auto">
                                            考试时间：<span id="ddgl_start_date"></span>
                                        </div>
                                        <div class="content content-auto">
                                            主持人：刘友庆、李丽
                                        </div>
                                        <div class="content content-auto">
                                            参加人员：订单管理员
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </li>
                        <li class='link'>
                            <div class="group">
                                <div class="content">
                                    <div class='l_padding'>
                                        <div class="pic pic-middle">
                                        </div>
                                    </div>
                                    <div class="main">
                                        <div class="title">
                                            售后服务协会
                                        </div>
                                        <div class="content content-auto">
                                            报到时间：<span id="shfw_sign_date"></span>
                                        </div>
                                        <div class="content content-auto">
                                            考试时间：<span id="shfw_start_date"></span>
                                        </div>
                                        <div class="content content-auto">
                                            主持人：杜世昌、雍新辉
                                        </div>
                                        <div class="content content-auto">
                                            参加人员：售后专干、售后文员
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </li>
                        <li class='link'>
                            <div class="group">
                                <div class="content">
                                    <div class='l_padding'>
                                        <div class="pic pic-middle">
                                        </div>
                                    </div>
                                    <div class="main">
                                        <div class="title">
                                            结构工程师协会
                                        </div>
                                        <div class="content content-auto">
                                            报到时间：<span id="jg_sign_date"></span>
                                        </div>
                                        <div class="content content-auto">
                                            考试时间：<span id="jg_start_date"></span>
                                        </div>
                                        <div class="content content-auto">
                                            主持人：晏松涛、肖军
                                        </div>
                                        <div class="content content-auto">
                                            参加人员：结构工程师
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </li>
                    </ul>
                </div>
                <br/>
                <span class="e_strong">五、员工参加岗前培训考评的资格：</span>
                <br/>
                1、已参加职前培训并考试合格。
                <br/>
                2、公司层面培训的通用科目及部门层面培训的专业规范考试每门均在80分以上。
                <br/>
                3、岗前培训中师傅层面培训的专业实践技能，必须每项技能都合格：工作态度“认真”为合格，专业知识与技能熟悉程度“一般”以上评价为合格。
                <br/>
                <span class="e_strong">六、报名截止时间：</span>
                <br/>
                各家装分公司、鸿扬木制人资必须在<span id="sign_end_date"></span>前完成报名。
                <br/>
                <span class="e_strong">七、注意事项：</span>
                <br/>
                1、从2018年5月岗前考评开始，鸿扬集团将按照新的岗前考评评分标准进行计分。请各分公司人资提醒参加岗前考评的新员工均需将2018版《岗前培训记录档案》带至集团，交由考官检查（补考的除外）。如培训内容填写不完整、签字缺失的则根据相应规范进行扣分。
                <br/>
                2、在此次考评过程中，通用科目成绩和专业科目的最后得分都要达到80分方为合格。
                <br/>
                3、参加设计师协会的中级/主任设计师和客户协会的客户代表，必须携带一套完整的、最近已开工的，由本人独立完成设计或预算 的客户档案资料，如到考评前仍未开工的，请准备一整套自己模拟做的客户档案资料（该资料在专业技能考评时交给考官）。
                <br/>
                4、参加工程协会的调度员须携带本公司工程日控表与月控表各一份。
                <br/>
                5、请各家装分公司参加补考的员工带好补考费用。通用科目补考费10元/次/人，专业科目补考费45元/次/人。如补考两门，需交纳55元/次/人。补考费用在报到当天由鸿扬集团总部人力资源与公共事务管理中心收取，并开具收据。
                <br/>
                特此通知。
            </div>
            <div class="aside">
                鸿扬集团人力资源与公共事务管理中心
                <br/>
                鸿扬集团
            </div>
        </div>

    </div>
    <div class="c_space-4"></div>
</div>
<!-- 滚动 结束 -->
<jsp:include page="/base/buttom/base_buttom.jsp"/>
<script>
    Wade.setRatio();
    $.prework.init();
</script>
</body>
</html>