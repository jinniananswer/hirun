package com.hirun.app.bean.course;

import com.alibaba.fastjson.JSONObject;
import com.hirun.app.dao.org.CourseDAO;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.pub.data.Record;
import com.most.core.pub.data.RecordSet;
import com.most.core.pub.tools.datastruct.ArrayTool;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: hirun
 * @description: ${description}
 * @author: jinnian
 * @create: 2018-11-26 10:37
 **/
public class CourseBean {

    public static JSONObject getCourseTree() throws Exception {
        JSONObject root = new JSONObject();
        root.put("text", "鸿扬课程体系");
        root.put("id", "-1");
        root.put("dataid", "-1");
        root.put("order", "0");
        root.put("value", "-1");
        root.put("expand", "true");
        root.put("disabled", "false");
        root.put("complete", "false");

        CourseDAO dao = DAOFactory.createDAO(CourseDAO.class);
        RecordSet courses = dao.queryAllValidCourse();

        JSONObject children = buildTreeNode(courses, "-1", "-1", 0);
        if (children == null) {
            root.put("haschild", "false");
        }
        else {
            root.put("haschild", "true");
            root.put("childNodes", children);
        }

        JSONObject rst = new JSONObject();
        rst.put("-1", root);
        return rst;
    }

    public static JSONObject buildTreeNode(RecordSet courses, String parentCourseId, String prefix, int level){
        if(courses == null || courses.size() <= 0)
            return null;
        level++;
        JSONObject node = new JSONObject();
        int order = 0;
        int size = courses.size();
        for(int i=0;i<size;i++){
            Record course = courses.get(i);
            if(StringUtils.equals(parentCourseId, course.get("PARENT_COURSE_ID"))){
                JSONObject childNode = new JSONObject();
                childNode.put("text", course.get("NAME"));
                childNode.put("id", course.get("COURSE_ID"));
                childNode.put("order", order+"");
                childNode.put("value", course.get("COURSE_ID"));
                childNode.put("dataid", prefix+"●"+course.get("COURSE_ID"));
                childNode.put("expand", "false");
                childNode.put("complete", "false");
                childNode.put("disabled", "false");
                JSONObject children = buildTreeNode(courses, course.get("COURSE_ID"), prefix+"●"+course.get("COURSE_ID"), level);
                if(children == null)
                    childNode.put("haschild", "false");
                else{
                    childNode.put("haschild", "true");
                    childNode.put("childNodes", children);
                }
                node.put(course.get("COURSE_ID"), childNode);
            }
            order++;
        }

        if(node.isEmpty())
            return null;
        else
            return node;
    }

    public static List<String> findChildren(RecordSet courses, String rootCourseId) throws Exception {
        if(courses == null || courses.size() <= 0) {
            return null;
        }

        List<String> rst = new ArrayList<String>();

        int size = courses.size();
        for(int i=0;i<size;i++) {
            Record course = courses.get(i);
            String courseId = course.get("COURSE_ID");
            String parentCourseId = course.get("PARENT_COURSE_ID");
            if(StringUtils.equals(parentCourseId, rootCourseId)) {
                //是其子节点
                rst.add(courseId);
                List<String> children = findChildren(courses, courseId);
                if(ArrayTool.isNotEmpty(children)) {
                    rst.addAll(children);
                }
            }
        }
        return rst;
    }
}
