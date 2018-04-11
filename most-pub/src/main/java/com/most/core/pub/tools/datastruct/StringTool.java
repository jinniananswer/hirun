package com.most.core.pub.tools.datastruct;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author jinnian
 * @Date 2018/3/21 17:47
 * @Description:
 */
public class StringTool {

    /**
     * 根据特定符号标识查找字符串中的变量
     * @param text
     * @param symbol
     * @return
     */
    public static List<String> findVariable(String text, char symbol){
        if(StringUtils.isBlank(text)){
            return null;
        }

        char[] textChar = text.toCharArray();
        int size = textChar.length;
        boolean inQuota = false; //在引号中
        boolean inVariable = false; //在变量中
        int begin = -1; //变量的起始位置
        List<String> variables = new ArrayList<String>();

        for(int i=0;i<size;i++){
            char c = textChar[i];

            if('\'' == c){
                if(inQuota)
                    inQuota = false; //表明是结尾的引号，因此退出了引号的作用范围
                else
                    inQuota = true;
                continue;
            }

            if(symbol == c){
                if(i+1<size && '=' == textChar[i+1])
                    continue;

                inVariable = true;
                begin = i+1;
                continue;
            }

            if(inVariable){
                if(isVaribleChar(c))
                    continue;
                else{
                    String variable = text.substring(begin, i);
                    if(StringUtils.isNotBlank(variable))
                        variables.add(variable);

                    inVariable = false;
                    begin = -1;
                }
                continue;
            }
        }

        if(begin != -1){
            String variable = text.substring(begin, text.length());
            if(StringUtils.isNotBlank(variable))
                variables.add(variable);
        }
        return variables;
    }

    public static Object[] parseVariableText(String text, char symbol, String replaceSymbol){
        if(StringUtils.isBlank(text)){
            return null;
        }

        char[] textChar = text.toCharArray();
        int size = textChar.length;
        boolean inQuota = false; //在引号中
        boolean inVariable = false; //在变量中
        int begin = -1; //变量的起始位置
        List<String> variables = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();

        for(int i=0;i<size;i++){
            char c = textChar[i];

            if('\'' == c){
                if(inQuota)
                    inQuota = false; //表明是结尾的引号，因此退出了引号的作用范围
                else
                    inQuota = true;
                sb.append(c);
                continue;
            }

            if(inQuota){
                sb.append(c);
                continue;
            }

            if(symbol == c){
                if(i+1<size && '=' == textChar[i+1]){
                    sb.append(c);
                    continue;
                }

                inVariable = true;
                begin = i+1;
                sb.append(replaceSymbol);
                continue;
            }

            if(inVariable){
                if(isVaribleChar(c))
                    continue;
                else{
                    String variable = text.substring(begin, i);
                    if(StringUtils.isNotBlank(variable))
                        variables.add(variable);

                    inVariable = false;
                    begin = -1;
                }
                continue;
            }
        }

        if(begin != -1){
            String variable = text.substring(begin, text.length());
            if(StringUtils.isNotBlank(variable))
                variables.add(variable);
        }
        return new Object[]{sb, variables};
    }

    public static boolean isVaribleChar(char c){
        if (48 <= c && c <= 57)  return true; // 0-9
        if (65 <= c && c <= 90)  return true; // A-Z
        if (97 <= c && c <= 122) return true; // a-z
        if (95 == c)             return true; // _

        return false;
    }

    public static void main(String[] args){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT :INTACT_ITEM_ID AS INTACT_ITEM_ID, B.* ");
        sql.append("  FROM UM_PROD A, UM_PROD_CHA B ");
        sql.append(" WHERE A.SUBSCRIBER_INS_ID = :SUBSCRIBER_INS_ID ");
        sql.append("   AND A.PARTITION_ID = MOD(:SUBSCRIBER_INS_ID, 10000) ");
        sql.append("   AND A.IS_MAIN = '1' ");
        sql.append("   AND SYSDATE < A.EXPIRE_DATE ");
        sql.append("   AND SYSDATE < B.EXPIRE_DATE ");
        sql.append("   AND B.PROD_INS_ID = A.PROD_INS_ID ");
        sql.append("   AND B.PARTITION_ID = MOD(A.PROD_INS_ID, 10000) ");

        List<String> variables = StringTool.findVariable(sql.toString(), ':');

        if(ArrayTool.isEmpty(variables)){
            System.out.println("没有找到变量");
            return;
        }

        int i = 1;
        for(String variable : variables){
            System.out.println("变量"+i+":"+variable);
            i++;
        }
    }
}
