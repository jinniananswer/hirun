/**
 * 
 */
package com.hirun.autocode.mysqldb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Administrator
 *
 */
public class DBHelper
{

	public static String[] getColumnNames(Connection conn, String tableName, boolean flag) throws Exception
	{
		PreparedStatement statement = null;
		List<String> columnNames = new ArrayList<String>();
		statement = conn.prepareStatement((new StringBuilder()).append("select * from ").append(tableName.toUpperCase()).append(" where 1 = 0").toString());
	    ResultSetMetaData metaData = statement.executeQuery().getMetaData();
        int i = 1;
		
        int columnCount = metaData.getColumnCount();
        for(int cnt = columnCount; i <= cnt; i++)
        {
        	String colName =metaData.getColumnName(i).toUpperCase();
//        	if(flag && isFilterCol(colName))
//        	{
//        		continue;
//        	}
        	columnNames.add(colName);
        }
		return (String[])columnNames.toArray(new String[0]);
	}
	
	private static boolean isFilterCol(String colName) throws Exception
	{
		boolean flag = false;
		Map<String, String> filterCol = new HashMap<String, String>();
		filterCol.put("REMARKS", "REMARKS");
		filterCol.put("DONE_CODE", "DONE_CODE");
		filterCol.put("ACTION", "ACTION");
		filterCol.put("OP_ID", "OP_ID");
		filterCol.put("ORG_ID", "ORG_ID");
		filterCol.put("CREATE_ORG_ID", "CREATE_ORG_ID");
		filterCol.put("REGION_ID", "REGION_ID");
		filterCol.put("DONE_DATE", "DONE_DATE");
		filterCol.put("CREATE_DATE", "CREATE_DATE");
		filterCol.put("CREATE_OP_ID", "CREATE_OP_ID");
		filterCol.put("VALID_DATE", "CREATE_DATE");
		filterCol.put("EXPIRE_DATE", "CREATE_OP_ID");
		filterCol.put("MGMT_COUNTY", "MGMT_COUNTY");
		filterCol.put("MGMT_DISTRICT", "MGMT_DISTRICT");
		filterCol.put("ORDER_ID", "ORDER_ID");
		filterCol.put("ORDER_LINE_ID", "ORDER_LINE_ID");
		filterCol.put("ORDER_ITEM_ID", "ORDER_ITEM_ID");
		
		if(filterCol.containsKey(colName))
		{
			flag = true;
		}
		
		if(colName.indexOf("KID") != -1)
		{
			flag = true;
		}
		
		return flag;
	}
	
	public static List<String> getTables(Connection conn,String dbUser) throws Exception{
		String sql = "select * from all_tables t where t.owner = ?";
		PreparedStatement statement = conn.prepareStatement(sql);
		statement.setString(1, dbUser);
		ResultSet result = statement.executeQuery();
		List<String> tables = new ArrayList<String>();
		while(result.next()){
			tables.add(result.getString("TABLE_NAME"));
		}
		result.close();
		return tables;
	}
	
	public static List<String> getOrderTables(Connection conn,String dbUser) throws Exception{
        String sql = " SELECT t.* FROM ALL_TABLES T WHERE t.OWNER = ? AND substr(t.table_name,4) in (SELECT substr(k.TABLE_NAME,4) FROM all_tables K where K.owner = 'UCR_JOUR1' AND K.TABLE_NAME LIKE 'OM%')";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, dbUser);
        ResultSet result = statement.executeQuery();
        List<String> tables = new ArrayList<String>();
        while(result.next()){
            tables.add(result.getString("TABLE_NAME"));
        }
        result.close();
        return tables;
    }
	
	public static List<String> getSingleTable(Connection conn,String dbUser, String tableName) throws Exception{
        String sql = " SELECT t.* FROM ALL_TABLES T WHERE t.OWNER = ? AND t.table_name = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, dbUser);
        statement.setString(2,tableName);
        ResultSet result = statement.executeQuery();
        List<String> tables = new ArrayList<String>();
        while(result.next()){
            tables.add(result.getString("TABLE_NAME"));
        }
        result.close();
        return tables;
    }
	
	
	public static String getPk(Connection conn, String tableName, String dbUser) throws Exception
    {
	    String resultPk = "";
	    String sql =" select b.column_name from  (select table_name, constraint_name from user_constraints where constraint_type = 'P' and owner = ? AND TABLE_NAME  = ?) a,  user_cons_columns b where a.constraint_name = b.constraint_name  and a.table_name = b.table_name";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, dbUser);
        statement.setString(2, tableName);
        ResultSet result = statement.executeQuery();
        List<String> tables = new ArrayList<String>();
        int lenght = result.getRow();
        if(lenght >1)
            return resultPk;
        while(result.next()){
            resultPk = result.getString("COLUMN_NAME");
        }
        result.close();
        return resultPk;
        
    }

    /*
	public static String getTableComment(Connection conn, String tableName) throws Exception
    {
        String comment = "";
        String sql ="select comments from user_tab_comments t where table_name = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, tableName);
        ResultSet result = statement.executeQuery();
        while(result.next()){
            comment = result.getString("COMMENTS");
        }
        
        result.close();
        if(StringUtils.isBlank(comment)){
            comment="无";
        }
        return comment;
        
    }
	*/
}
