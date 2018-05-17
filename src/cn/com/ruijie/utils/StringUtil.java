
package cn.com.ruijie.utils;

/**
 *  字符串操作工具
 * 
 */
public class StringUtil {
    //检测字符串是否不为null 或 ""
    public static boolean notEmpty(String s) {
        return s != null && !"".equals(s) && !"null".equals(s);
    }

    //检测字符串是否为null 或 ""
    public static boolean isEmpty(String s) {
        return s == null || "".equals(s) || "null".equals(s);
    }
    
    //字符串转化为字符串数组
    public static String[] str2StrArray(String str, String splitRegex) {
        if (isEmpty(str)) {
            return null;
        }
        return str.split(splitRegex);
    }
}
