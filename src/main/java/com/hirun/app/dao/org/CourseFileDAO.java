package com.hirun.app.dao.org;

import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.GenericDAO;
import com.most.core.pub.data.RecordSet;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: hirun
 * @description: ${description}
 * @author: jinnian
 * @create: 2018-10-27 17:45
 **/
@DatabaseName("ins")
public class CourseFileDAO extends GenericDAO {

    public CourseFileDAO(String databaseName) {
        super(databaseName);
    }

    public RecordSet queryCourseFilesByCourseType(String type) throws SQLException {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("TYPE", type);

        StringBuilder sql = new StringBuilder();
        sql.append("select a.*, b.name course_name from ins_course_file a, ins_course b ");
        sql.append("where a.course_id = b.course_id ");
        sql.append("and b.type = :TYPE ");
        sql.append("and a.status = '0' ");
        sql.append("and b.status = '0' ");
        return this.queryBySql(sql.toString(), parameter);
    }

    public RecordSet queryCourseFilesByCourseId(String courseId, String fileName) throws SQLException {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("COURSE_ID", courseId);
        parameter.put("NAME", fileName);

        StringBuilder sql = new StringBuilder();
        sql.append("select a.*, b.name course_name from ins_course_file a, ins_course b ");
        sql.append("where a.course_id = b.course_id ");
        sql.append("and a.status = '0' ");
        sql.append("and b.status = '0' ");
        if(StringUtils.isNotBlank(courseId)) {
            sql.append("and b.course_id = :COURSE_ID ");
        }
        if(StringUtils.isNotBlank(fileName)) {
            sql.append("and a.name like CONCAT('%',:NAME,'%') ");
        }
        return this.queryBySql(sql.toString(), parameter);
    }

    public void deleteCourseFilesById(String fileIds, String updateUserId, String updateTime) throws SQLException {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("UPDATE_USER_ID", updateUserId);
        parameter.put("UPDATE_TIME", updateTime);

        StringBuilder sb = new StringBuilder();
        sb.append("update ins_course_file set status = '1', update_user_id = :UPDATE_USER_ID, update_time = :UPDATE_TIME ");
        sb.append("where file_id in ("+fileIds+") ");

        this.executeUpdate(sb.toString(), parameter);
    }
}
