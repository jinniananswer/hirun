package com.hirun.pub.tool;
import com.most.core.app.database.dao.GenericDAO;
import com.most.core.pub.data.RecordSet;
import com.most.core.pub.tools.time.TimeTool;
import org.apache.commons.lang3.StringUtils;
import java.util.HashMap;

/**
 * 获取customerNo
 */
public class CustomerNoTool {

    public static String getCustomerNoSeq() throws Exception {
        GenericDAO dao = new GenericDAO("sys");
        String sql = "select nextval('SEQ_ID_DAY_CYCLE') as id";
        RecordSet recordSet = dao.queryBySql(sql, new HashMap<>());
        String id = recordSet.get(0).get("ID");
        String strNextval = StringUtils.leftPad(String.valueOf(id), 4, '0');
        String timestamp = TimeTool.now("yyyyMMdd");
        return "KH"+timestamp+strNextval;
    }
}
