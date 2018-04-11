package com.most.core.pub.tools.formatter;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @Author jinnian
 * @Date 2018/2/20 23:03
 * @Description: 字符串格式化工具
 */
public class StringFormatter {
	private static int maxKeyLength(Set<String> ss)
	{
		int ilength=0;
		for(String s:ss)
		{
			if(s.length()>ilength)
				ilength=s.length();
		}
		return ilength;
	}
	
	private Set<String> getKeys(Map<String,String> m)
	{
		return m.keySet();
	}
	
	private static Set<String> getKeys(Map<String,String> m,boolean isSort)
	{
		if(isSort==false)
			return m.keySet();
		ArrayList<String> hs=new ArrayList<String>();
		hs.addAll(m.keySet());
		if(hs.size()==0)
			return m.keySet();
		Collections.sort(hs);
		HashSet<String> s=new HashSet<String>();
		for(int i=0;i<hs.size();i++)
		{
			s.add(hs.get(i));
		}
		return s;
	}
	
	private static Set<String> getKeys(List<Map<String,String>> m,boolean isSort)
	{
		ArrayList<String> hs=new ArrayList<String>();
		
		for(Map<String,String> ms:m)
		{
			hs.addAll(getKeys(ms,false));
		}
		Collections.sort(hs);
		HashSet<String> s=new HashSet<String>();
		for(int i=0;i<hs.size();i++)
		{
			s.add(hs.get(i));
		}
		return s;
	}
	
	private static String nullData()
	{
		return "";
	}
	
	public static String toStringV(List<Map<String,String>> v)
	{
		if(v==null || v.size()==0)
			return nullData();
		StringBuilder sb=new StringBuilder();
		
		boolean isSort=false;
		String[] titles=getKeys(v,isSort).toArray(new String[]{});
		int[] tsize=new int[titles.length];
		int[] vsize=new int[titles.length];
		
		for(int i=0;i<titles.length;i++)
		{
			tsize[i]=titles[i].length();
			vsize[i]=tsize[i];
		}
		for(Map<String,String> ms:v)
		{	
			for(int i=0;i<titles.length;i++)
			{
				String sv=ms.get(titles[i]);
				int l=(sv == null?4:sv.length());
				if(l>vsize[i])
				{
					vsize[i]=l;
				}
			}
		}
		String padstr= StringUtils.leftPad("", 500);
		String padstr2=StringUtils.leftPad("", 500,'-');

		for(int i=0;i<titles.length;i++)
		{
			int vpad=vsize[i]-tsize[i]+1;
			if(vpad>1)
			{
				sb.append(padstr,0,vpad/2);
			}
			sb.append(titles[i]);
			sb.append(padstr,0,vpad-vpad/2);
		}
		sb.append("\n");

		for(int i=0;i<titles.length;i++)
		{
			sb.append(padstr2,0,vsize[i]);
			sb.append(" ");
		}
		sb.append("\n");
		
		for(Map<String,String> ms:v)
		{	
			for(int i=0;i<titles.length;i++)
			{
				String sv=ms.get(titles[i]);
				sb.append(sv);
				sb.append(padstr,0,vsize[i]-(sv==null?4:sv.length()));
				sb.append(" ");
			}
			sb.append("\n");
		}
		
		return sb.toString();
		
	}
	
	public static String toStringV(Map<String,String> map)
	{
		if(map==null || map.size()==0)
			return nullData();
		StringBuilder sb=new StringBuilder();
		
		boolean isSort=false;
		String[] titles=getKeys(map,isSort).toArray(new String[]{});
		int[] tsize=new int[titles.length];
		int[] vsize=new int[titles.length];
		
		for(int i=0;i<titles.length;i++)
		{
			tsize[i]=titles[i].length();
			vsize[i]=tsize[i];
		}

		for(int i=0;i<titles.length;i++)
		{
			String sv=map.get(titles[i]);
			int l=(sv == null?4:sv.length());
			if(l>vsize[i])
			{
				vsize[i]=l;
			}
		}
		String padstr=StringUtils.leftPad("", 500);
		String padstr2=StringUtils.leftPad("", 500,'-');

		for(int i=0;i<titles.length;i++)
		{
			int vpad=vsize[i]-tsize[i]+1;
			if(vpad>1)
			{
				sb.append(padstr,0,vpad/2);
			}
			sb.append(titles[i]);
			sb.append(padstr,0,vpad-vpad/2);
		}
		sb.append("\n");

		for(int i=0;i<titles.length;i++)
		{
			sb.append(padstr2,0,vsize[i]);
			sb.append(" ");
		}
		sb.append("\n");
		
		for(int i=0;i<titles.length;i++)
		{
			String sv=map.get(titles[i]);
			sb.append(sv);
			sb.append(padstr,0,vsize[i]-(sv==null?4:sv.length()));
			sb.append(" ");
		}
		sb.append("\n");
		return sb.toString();
		
	}
	
	public static String toStringH(List<Map<String,String>> v)
	{
		if(v==null)
			return nullData();
		StringBuilder sb=new StringBuilder();
		
		
		int index=0;
		boolean isSort=true;
		String line="----------------------";
		String padstr="                                                                  ";

		for(Map<String,String> ms:v)
		{		
			sb.append(++index);
			sb.append(" ");
			sb.append(line);
			sb.append("\n");
			int maxlength=maxKeyLength(ms.keySet());
			if(maxlength<10)
				maxlength=10;
			for(String key:getKeys(ms,isSort))
			{	
				sb.append(" ");
				sb.append(key);
				sb.append(padstr, 0, maxlength-key.length());
				sb.append(": ");
				sb.append(ms.get(key));

				sb.append("\n");
			}

		}
		sb.append("END");
		sb.append(line);
		sb.append("\n");
		return sb.toString();
		
	}
	
	public static String toStringH(Map<String,String> map)
	{
		if(map==null)
			return nullData();
		StringBuilder sb=new StringBuilder();
		
		
		boolean isSort=true;
		String line="----------------------";
		String padstr="                                                                  ";

		sb.append("start");
		sb.append(" ");
		sb.append(line);
		sb.append("\n");
		int maxlength=maxKeyLength(map.keySet());
		if(maxlength<10)
			maxlength=10;
		for(String key:getKeys(map,isSort))
		{	
			sb.append(" ");
			sb.append(key);
			sb.append(padstr, 0, maxlength-key.length());
			sb.append(": ");
			sb.append(map.get(key));

			sb.append("\n");
		}
		sb.append("END");
		sb.append(line);
		sb.append("\n");
		return sb.toString();
		
	}
	
	private String toStringX(List<Map<String,String>> v)
	{
		if(v==null)
			return nullData();
		StringBuilder sb=new StringBuilder();
		int index=0;
		boolean isSort=true;
		sb.append("<records>");
		sb.append("\n");
		for(Map<String,String> ms:v)
		{
			sb.append("<record index=\"");
			sb.append(++index);
			sb.append("\">");
			sb.append("\n");
			
			for(String key:getKeys(ms,isSort))
			{	
				sb.append("\t");
				sb.append("<");
				sb.append(key);
				sb.append(">");
				sb.append(ms.get(key));
				sb.append("</");
				sb.append(key);
				sb.append(">");
				sb.append("\n");
			}
			
			sb.append("</record>");
			sb.append("\n");
		}
		sb.append("</records>");
		return sb.toString();
	}
	
	public String objToString(List<Map<String,String>> v)
	{
		return toStringV(v);
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try
		{
			StringFormatter t=new StringFormatter();
			ArrayList<Map<String,String>> al=new ArrayList<Map<String,String>>();
			
			HashMap<String,String> m=new HashMap<String,String>();
			m.put("A1tttttttttttttttttt", "A1Value");
			m.put("B3", "value                          hug");
			m.put("A2", "A2Val888ue");
			m.put("A3", "A2Val888ue");
			m.put("A4", "A2Val888ue");
			m.put("A5", "A2Val888ue");
			m.put("A6", "A2Val888ue");
			m.put("A7", "A2Val888ue");
			m.put("A8", "A2Val888ue");
			m.put("A9", "A2Val888ue");
			m.put("A10", "A2Val888ue");
			m.put("A11", "A2Val888ue");
			m.put("A12", "A2Val888ue");
			m.put("A13", "A2Val888ue");
			m.put("A14", "A2Val888ue");
			m.put("A15", "A2Val888ue");
			m.put("A16", "A2Val888ue");
			m.put("A17", "A2Val888ue");
			al.add(m);
			al.add(m);
 			String s=t.objToString(al);
			// sys.out.println(s);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

}
