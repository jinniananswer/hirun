package com.hirun.app.dao.org;

import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.GenericDAO;
import com.most.core.pub.data.RecordSet;

import java.sql.SQLException;

/**
 * @program: hirun
 * @description: ${description}
 * @author: jinnian
 * @create: 2019-01-03 22:53
 **/
@DatabaseName("ins")
public class ExamDAO extends GenericDAO {

    public ExamDAO(String databaseName) {
        super(databaseName);
    }

    public RecordSet queryTopicOptions(String topicIds) throws SQLException {
        StringBuilder sql = new StringBuilder();

        sql.append("select option_id, topic_id, name, symbol ");
        sql.append("from ins_exam_topic_option ");
        sql.append("where topic_id in ("+topicIds+") ");
        sql.append("order by topic_id, symbol asc ");

        return this.queryBySql(sql.toString(), null);
    }
}
