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
    <script src="/scripts/biz/organization/prework/show.prework.evaluation.notice.js"></script>
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
                1、截至2019年1月9日，进公司满60—75天的新员工；
                <br/>
                2、参加过一次考评，但通用或专业科目未达80分的员工；
                <br/>
                4、转岗已满一个月的员工。
                <br/>
                <span class="e_strong">四、考评安排：</span>
                <br/>
                以下为初次考评时间安排，调职和补考的，根据初次考评时间进行相应科目考评。
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
                各家装分公司、鸿扬木制人资必须在<span id="sign_end_date"></span>下午17点前填写好《岗前考评报名表》（详见附件1），以邮件形式报鸿扬集团总部人力资源与公共事务部公共邮箱（邮箱：1498729757@qq.com，注明洪慧收）。
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