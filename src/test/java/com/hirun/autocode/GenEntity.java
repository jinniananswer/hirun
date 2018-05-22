
package com.hirun.autocode;

import com.hirun.autocode.customvar.LowerFirstCharacter;
import com.hirun.autocode.customvar.UpperFirstCharacter;
import com.hirun.autocode.mysqldb.ConnectionFactory;
import com.hirun.autocode.mysqldb.DBHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据表自动生成相应的TradeData对象 使用到了tableTradeDataClass.ftl模板
 */
public class GenEntity
{
	@Test
	public void genFile() throws Exception{
		String packageName = "com.hirun.pub.domain.entity.out";
		Connection conn = null;
		String dirPath = "D:/file/";
		String tableName = "OUT_DATA_GET_INFO";
		try{
			conn = ConnectionFactory.getConnection("out");
			Configuration cfg = new Configuration();
			String fileName = this.getFileName(tableName);


			cfg.setClassForTemplateLoading(GenEntity.class, "/");
			cfg.setSharedVariable("upperFC", new UpperFirstCharacter());
			cfg.setSharedVariable("lowerFC", new LowerFirstCharacter());
			Template t = cfg.getTemplate("freemarker/hirun/entity.ftl");


			Map data = new HashMap();
			data.put("package", packageName);//
			data.put("className", fileName);
			data.put("tableName", tableName);

			checkFileAndMkdir(dirPath + "/" + fileName + ".java");

			FileOutputStream fos = new FileOutputStream(dirPath + "/" + fileName + ".java");

			List pros = new ArrayList();
			String[] cols = DBHelper.getColumnNames(conn, tableName, true);
			for (int i = 0, size = cols.length; i < cols.length; i++)
			{
				StringBuilder proName = new StringBuilder();
				String colName = cols[i];
				String[] tmpArr = colName.split("_");
				for (int j = 0, tmpArrsize = tmpArr.length; j < tmpArrsize; j++)
				{
					String tmpStr = tmpArr[j].toLowerCase();
					if (j != 0)
					{
						tmpStr = tmpStr.substring(0, 1).toUpperCase() + tmpStr.substring(1, tmpStr.length());
					}
					proName.append(tmpStr);
				}

				Map pro_1 = new HashMap();
				pro_1.put("proType", String.class.getSimpleName());
				pro_1.put("proName", proName.toString());
				pro_1.put("colName", colName);
				pros.add(pro_1);
			}


			data.put("properties", pros);
			t.process(data, new OutputStreamWriter(fos));
			fos.flush();
			fos.close();
		} catch(Exception e) {
			e.printStackTrace();
		} finally{
			conn.close();
		}
	}

	public String getFileName(String tableName){
		String temp = tableName.toLowerCase();
		String[] members = temp.split("_");
		String fileName = "";
		for(int i=1;i<members.length;i++){
			String member = members[i];
			fileName += (member.charAt(0)+"").toUpperCase()+member.substring(1);
		}
		return fileName+"Entity";
	}

	public static void checkFileAndMkdir(String fileName) throws Exception
	{
		File f = new File(fileName);
		if (!f.exists() && !f.isDirectory()) {
			int index = fileName.lastIndexOf("\\");
			int index2 = fileName.lastIndexOf("/");
			String filePath = fileName.substring(0, index2 > index ? index2: index);
			File ff = new File(filePath);
			if (!ff.exists()) {
				ff.mkdirs();
			}
		}
	}
}
