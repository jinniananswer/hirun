/**
 * 
 */
package com.hirun.autocode.mysqldb;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author Administrator
 *
 */
public class ConnectionFactory
{
	public static Connection getConnection(String databaseName)
	{
		Connection conn=null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://139.129.29.141:3316/"+databaseName,"hirun","1q1w1e1r");
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return conn;
	}
}
