package com.most.core.pub.tools.formatter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * @Author jinnian
 * @Date 2018/2/20 23:03
 * @Description: SQL语句格式化工具
 */
public class SQLFormatter
{

	private static class FormatProcess
	{
		private static boolean isFunctionName(String tok)
		{
			final char begin = tok.charAt(0);
			final boolean isIdentifier = Character.isJavaIdentifierStart(begin) || '"' == begin;
			return isIdentifier && !LOGICAL.contains(tok) && !END_CLAUSES.contains(tok) && !QUANTIFIERS.contains(tok)
					&& !DML.contains(tok) && !MISC.contains(tok) && !FUNCTION.contains(tok);
		}

		private static boolean isWhitespace(String token)
		{
			return SQLFormatter.WHITESPACE.indexOf(token) >= 0;
		}

		boolean						beginLine					= true;
		boolean						afterBeginBeforeEnd			= false;
		boolean						afterByOrSetOrFromOrSelect	= false;
		boolean						afterValues					= false;
		boolean						afterOn						= false;
		boolean						afterBetween				= false;

		boolean						afterInsert					= false;
		// huwl add for keyword use same style
		int							keywordCaseChange			= 1;							// 0
		// for
		// no
		// change,1
		// for
		// uppercase
		// 2
		// for
		// lowercase
		// 3
		// for
		// capitalized
		int							inFunction					= 0;
		int							parensSinceSelect			= 0;

		private LinkedList<Integer>	parenCounts					= new LinkedList<Integer>();

		private LinkedList<Boolean>	afterByOrFromOrSelects		= new LinkedList<Boolean>();
		int							indent						= 1;
		StringBuilder				result						= new StringBuilder();
		StringTokenizer				tokens;
		String						lastToken;

		String						token;

		String						lcToken;

		private boolean				is_huwl_function			= false;

		public FormatProcess(String sql)
		{
			tokens = new StringTokenizer(sql, "()+*/-=<>'`\"[]," + SQLFormatter.WHITESPACE, true);
		}

		private void beginNewClause()
		{
			if (!afterBeginBeforeEnd)
			{
				if (afterOn)
				{
					indent--;
					afterOn = false;
				}
				indent--;
				newline();
			}
			out();
			beginLine = false;
			afterBeginBeforeEnd = true;
		}

		private String capitalize(String tok)
		{
			return tok.substring(0, 1).toUpperCase() + tok.toLowerCase().substring(1);
		}

		private void closeParen()
		{
			parensSinceSelect--;
			if (parensSinceSelect < 0)
			{
				indent--;
				parensSinceSelect = parenCounts.removeLast().intValue();
				afterByOrSetOrFromOrSelect = afterByOrFromOrSelects.removeLast().booleanValue();
			}
			if (inFunction > 0)
			{
				inFunction--;
				out();
			}
			else
			{
				if (!afterByOrSetOrFromOrSelect)
				{
					indent--;
					newline();
				}
				out();
			}
			beginLine = false;

			is_huwl_function = false;
		}

		private void commaAfterByOrFromOrSelect()
		{
			out();
			if (!is_huwl_function)
			{
				newline();
			}
		}

		private void commaAfterOn()
		{
			out();
			indent--;
			newline();
			afterOn = false;
			afterByOrSetOrFromOrSelect = true;
		}

		private void endNewClause()
		{
			if (!afterBeginBeforeEnd)
			{
				indent--;
				if (afterOn)
				{
					indent--;
					afterOn = false;
				}
				newline();
			}
			out();
			if (!"union".equals(lcToken))
			{
				indent++;
			}
			newline();
			afterBeginBeforeEnd = false;
			afterByOrSetOrFromOrSelect = "by".equals(lcToken) || "set".equals(lcToken) || "from".equals(lcToken);
		}

		private boolean isKeyWord(String toka)
		{
			String tok = toka.toLowerCase();
			return LOGICAL.contains(tok) || END_CLAUSES.contains(tok) || QUANTIFIERS.contains(tok) || DML.contains(tok)
					|| MISC.contains(tok) || FUNCTION.contains(tok) || BEGIN_CLAUSES.contains(tok);
		}

		private void logical()
		{
			if ("end".equals(lcToken))
			{
				indent--;
			}
			/**
			 * ori code is newline(); out();
			 * 
			 * huwl: Ϊ�˶��������������logic operator ��ǰ
			 */
			if (indentString.length() > 4)
			{
				indent--;
				newline();
				// out();
				String s2 = "";
				if (token.length() == 2)
				{
					s2 = "  ";
				}
				else if (token.length() == 3)
				{
					s2 = " ";
				}
				out(indentString.substring(0, indentString.length() - 5) + s2);
				out(token);
				indent++;
			}
			else
			{
				newline();
				out();
			}
			beginLine = false;
		}

		private void misc()
		{
			out();
			if ("between".equals(lcToken))
			{
				afterBetween = true;
			}
			if (afterInsert)
			{
				newline();
				afterInsert = false;
			}
			else
			{
				beginLine = false;
				if ("case".equals(lcToken))
				{
					indent++;
				}
			}
		}

		private void newline()
		{
			result.append("\n");
			for (int i = 0; i < indent; i++)
			{
				result.append(indentString);
			}
			beginLine = true;
		}

		private void on()
		{
			indent++;
			afterOn = true;
			newline();
			out();
			beginLine = false;
		}

		private void openParen()
		{
			if (isFunctionName(lastToken) || inFunction > 0)
			{
				inFunction++;
			}
			beginLine = false;
			if (inFunction > 0)
			{
				out();
			}
			else
			{
				out();
				if (!afterByOrSetOrFromOrSelect)
				{
					indent++;
					newline();
					beginLine = true;
				}
			}
			parensSinceSelect++;

			if (FUNCTION.contains(lastToken))
			{
				this.is_huwl_function = true;
			}

		}

		private void out()
		{
			out(token);
		}

		private void out(String tokenstring)
		{
			if (this.keywordCaseChange != 0)
			{
				if (isKeyWord(tokenstring))
				{
					if (this.keywordCaseChange == 1)
					{
						result.append(tokenstring.toUpperCase());
					}
					else if (this.keywordCaseChange == 2)
					{
						result.append(tokenstring.toLowerCase());
					}
					else if (this.keywordCaseChange == 3)
					{
						result.append(this.capitalize(tokenstring));
					}
					else
					{
						result.append(tokenstring);
					}
				}
				else
				{
					result.append(tokenstring);
				}
			}
			else
			{
				result.append(tokenstring);
			}
		}

		public String perform()
		{

			result.append(initial);

			while (tokens.hasMoreTokens())
			{
				token = tokens.nextToken();
				lcToken = token.toLowerCase();

				if ("'".equals(token))
				{
					String t;
					do
					{
						t = tokens.nextToken();
						token += t;
					}
					while (!"'".equals(t) && tokens.hasMoreTokens()); // cannot
					// handle
					// single
					// quotes
				}
				else if ("\"".equals(token))
				{
					String t;
					do
					{
						t = tokens.nextToken();
						token += t;
					}
					while (!"\"".equals(t));
				}

				if (afterByOrSetOrFromOrSelect && ",".equals(token))
				{
					commaAfterByOrFromOrSelect();
				}
				else if (afterOn && ",".equals(token))
				{
					commaAfterOn();
				}

				else if ("(".equals(token))
				{
					openParen();
				}
				else if (")".equals(token))
				{
					closeParen();
				}

				else if (BEGIN_CLAUSES.contains(lcToken))
				{
					beginNewClause();
				}

				else if (END_CLAUSES.contains(lcToken))
				{
					endNewClause();
				}

				else if ("select".equals(lcToken))
				{
					select();
				}

				else if (DML.contains(lcToken))
				{
					updateOrInsertOrDelete();
				}

				else if ("values".equals(lcToken))
				{
					values();
				}

				else if ("on".equals(lcToken))
				{
					on();
				}

				else if (afterBetween && lcToken.equals("and"))
				{
					misc();
					afterBetween = false;
				}

				else if (LOGICAL.contains(lcToken))
				{
					logical();
				}

				else if (isWhitespace(token))
				{
					white();
				}

				else
				{
					misc();
				}

				if (!isWhitespace(token))
				{
					lastToken = lcToken;
				}

			}
			return result.toString();
		}

		private void select()
		{
			out();
			indent++;
			newline();
			parenCounts.addLast(Integer.valueOf(parensSinceSelect));
			afterByOrFromOrSelects.addLast(Boolean.valueOf(afterByOrSetOrFromOrSelect));
			parensSinceSelect = 0;
			afterByOrSetOrFromOrSelect = true;
		}

		private void updateOrInsertOrDelete()
		{
			out();
			indent++;
			beginLine = false;
			if ("update".equals(lcToken))
			{
				newline();
			}
			if ("insert".equals(lcToken))
			{
				afterInsert = true;
			}
		}

		private void values()
		{
			indent--;
			newline();
			out();
			indent++;
			newline();
			afterValues = true;
		}

		private void white()
		{
			if (!beginLine)
			{
				result.append(" ");
			}
		}
	}

	private static final String			WHITESPACE		= " \n\r\f\t";
	private static final Set<String>	BEGIN_CLAUSES	= new HashSet<String>();
	private static final Set<String>	END_CLAUSES		= new HashSet<String>();
	private static final Set<String>	LOGICAL			= new HashSet<String>();
	private static final Set<String>	QUANTIFIERS		= new HashSet<String>();
	private static final Set<String>	DML				= new HashSet<String>();
	private static final Set<String>	MISC			= new HashSet<String>();

	/* huwl add oracle function support */
	private static final Set<String>	FUNCTION		= new HashSet<String>();

	static final String					indentString	= "      ";
	static final String					initial			= "\n    ";

	static
	{
		BEGIN_CLAUSES.add("left");
		BEGIN_CLAUSES.add("right");
		BEGIN_CLAUSES.add("inner");
		BEGIN_CLAUSES.add("outer");
		BEGIN_CLAUSES.add("group");
		BEGIN_CLAUSES.add("order");

		END_CLAUSES.add("where");
		END_CLAUSES.add("set");
		END_CLAUSES.add("having");
		END_CLAUSES.add("join");
		END_CLAUSES.add("from");
		END_CLAUSES.add("by");
		END_CLAUSES.add("join");
		END_CLAUSES.add("into");
		END_CLAUSES.add("union");

		LOGICAL.add("and");
		LOGICAL.add("or");
		LOGICAL.add("when");
		LOGICAL.add("else");
		LOGICAL.add("end");
		LOGICAL.add("not");

		QUANTIFIERS.add("in");
		QUANTIFIERS.add("all");
		QUANTIFIERS.add("exists");
		QUANTIFIERS.add("some");
		QUANTIFIERS.add("any");

		QUANTIFIERS.add("like");

		DML.add("insert");
		DML.add("update");
		DML.add("delete");

		MISC.add("select");
		MISC.add("on");

		FUNCTION.add("abs");
		FUNCTION.add("acos");
		FUNCTION.add("add_months");
		FUNCTION.add("ascii");
		FUNCTION.add("asin");
		FUNCTION.add("atan");
		FUNCTION.add("atan2");
		FUNCTION.add("avg");
		FUNCTION.add("bfilename");
		FUNCTION.add("ceil");
		FUNCTION.add("chartorowid");
		FUNCTION.add("chr");
		FUNCTION.add("concat");
		FUNCTION.add("convert");
		FUNCTION.add("cos");
		FUNCTION.add("cosh");
		FUNCTION.add("count");
		FUNCTION.add("deref");
		FUNCTION.add("dump");
		FUNCTION.add("empty_blob");
		FUNCTION.add("empty_clob");
		FUNCTION.add("exp");
		FUNCTION.add("floor");
		FUNCTION.add("greatest");
		FUNCTION.add("grouping");
		FUNCTION.add("hextoraw");
		FUNCTION.add("initcap");
		FUNCTION.add("instr");
		FUNCTION.add("instrb");
		FUNCTION.add("lpad");
		FUNCTION.add("ltrim");
		FUNCTION.add("last_day");
		FUNCTION.add("least");
		FUNCTION.add("length");
		FUNCTION.add("lengthb");
		FUNCTION.add("ln");
		FUNCTION.add("log");
		FUNCTION.add("lower");
		FUNCTION.add("make_ref");
		FUNCTION.add("max");
		FUNCTION.add("min");
		FUNCTION.add("mod");
		FUNCTION.add("months_between");
		FUNCTION.add("nlssort");
		FUNCTION.add("nls_charset_decl_len");
		FUNCTION.add("nls_charset_id");
		FUNCTION.add("nls_charset_name");
		FUNCTION.add("nls_initcap");
		FUNCTION.add("nls_lower");
		FUNCTION.add("nls_upper");
		FUNCTION.add("new_time");
		FUNCTION.add("next_day");
		FUNCTION.add("nvl");
		FUNCTION.add("power");
		FUNCTION.add("rpad");
		FUNCTION.add("rtrim");
		FUNCTION.add("rawtohex");
		FUNCTION.add("ref");
		FUNCTION.add("reftohex");
		FUNCTION.add("replace");
		FUNCTION.add("round");
		FUNCTION.add("rowidtochar");
		FUNCTION.add("sys_context");
		FUNCTION.add("sys_guid");
		FUNCTION.add("sign");
		FUNCTION.add("sin");
		FUNCTION.add("sinh");
		FUNCTION.add("soundex");
		FUNCTION.add("sqrt");
		FUNCTION.add("stddev");
		FUNCTION.add("substr");
		FUNCTION.add("substrb");
		FUNCTION.add("sum");
		FUNCTION.add("sysdate");
		FUNCTION.add("tan");
		FUNCTION.add("tanh");
		FUNCTION.add("to_lob");
		FUNCTION.add("to_char");
		FUNCTION.add("to_date");
		FUNCTION.add("to_multi_byte");
		FUNCTION.add("to_number");
		FUNCTION.add("to_single_byte");
		FUNCTION.add("translate");
		FUNCTION.add("trim");
		FUNCTION.add("trunc");
		FUNCTION.add("uid");
		FUNCTION.add("upper");
		FUNCTION.add("user");
		FUNCTION.add("userenv");
		FUNCTION.add("vsize");
		FUNCTION.add("value");
		FUNCTION.add("variance");

	}

	public static void main(String[] args)
	{
		SQLFormatter f = new SQLFormatter();

		String s = "select * from dual where (���� < to_date('2012-03-21','yyyy-MM-dd') AND ״̬ IN ( '����','ͣ��' ) AND ��� IN ( '���1','���3' )) OR (����ʱ�� >to_date('2012-03-22 11:28:00','yyyy-MM-dd hh24:mi:ss') AND ��� IN ( '���2' ))";
		// s="select eparchy_code,res_type_code,depart_id,depart_code,depart_kind_code,depart_name,depart_frame,valid_flag,area_code,parent_depart_id,manager_id,order_no,to_char(warnning_value_u) warnning_value_u,to_char(warnning_value_d) warnning_value_d,to_char(start_date,'yyyy-mm-dd hh24:mi:ss') start_date,to_char(end_date,'yyyy-mm-dd hh24:mi:ss') end_date,depart_level,stock_level,rsrv_tag1,rsrv_tag2,rsrv_tag3,rsrv_num1,rsrv_num2,rsrv_num3,to_char(rsrv_date1,'yyyy-mm-dd hh24:mi:ss') rsrv_date1,to_char(rsrv_date2,'yyyy-mm-dd hh24:mi:ss') rsrv_date2,to_char(rsrv_date3,'yyyy-mm-dd hh24:mi:ss') rsrv_date3,rsrv_str1,rsrv_str2,rsrv_str3,rsrv_str4,rsrv_str5,remark,to_char(update_time,'yyyy-mm-dd hh24:mi:ss') update_time,update_staff_id,update_depart_id FROM td_s_assignrule a WHERE eparchy_code=:VEPARCHY_CODE AND res_type_code=:VRES_TYPE_CODE AND (:VAREA_CODE IS NULL OR (:VAREA_CODE IS NOT NULL AND area_code=:VAREA_CODE)) AND ( ( depart_frame LIKE :VDEPART_FRAME||'%' AND depart_id&lt;&gt;:VDEPART_ID --AND depart_kind_code IN ('3','4','5','6','7','8','B') AND depart_kind_code IN (SELECT depart_kind_code FROM td_m_departkind WHERE code_type_code='0' AND eparchy_code=:VEPARCHY_CODE) ) OR ( depart_frame LIKE :VDEPART_FRAME2||'%' AND depart_kind_code='006' ) ) AND valid_flag='0' ORDER BY depart_level,depart_code";
		s = "update td_s_assignrule a SET a.depart_frame=(SELECT b.depart_frame ||(SELECT SUBSTR(depart_frame,INSTR(depart_frame,:VDEPART_ID,1,1)) FROM td_s_assignrule c WHERE c.depart_frame LIKE (SELECT rsrv_str4 FROM td_s_assignrule WHERE depart_id=:VDEPART_ID AND eparchy_code=:VEPARCHY_CODE AND res_type_code=:VRES_TYPE_CODE)||'%' AND c.depart_id=a.depart_id AND c.eparchy_code=:VEPARCHY_CODE AND c.res_type_code=:VRES_TYPE_CODE) FROM td_s_assignrule b WHERE b.depart_id=:VDEPART_CODE AND b.eparchy_code=:VEPARCHY_CODE AND b.res_type_code=:VRES_TYPE_CODE) WHERE a.depart_frame LIKE (SELECT rsrv_str4 FROM td_s_assignrule WHERE depart_id=:VDEPART_ID AND eparchy_code=:VEPARCHY_CODE AND res_type_code=:VRES_TYPE_CODE)||'%' AND a.eparchy_code=:VEPARCHY_CODE AND a.res_type_code=:VRES_TYPE_CODE";
		// s="select * from dual where sysdate between to_date('2012-03-21','yyyy-MM-dd') and to_date('2012-03-21','yyyy-MM-dd')";
		// sys.out.println(f.format(s));
	}

	public String format(String source)
	{
		return new FormatProcess(source).perform();
	}
}
