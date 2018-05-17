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
	public static Connection getConnection()
	{
		Connection conn=null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://192.168.10.128:3306/ins","system","123");
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return conn;
	}
}
