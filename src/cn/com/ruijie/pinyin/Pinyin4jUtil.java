package cn.com.ruijie.pinyin;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class Pinyin4jUtil {

    /**
     *
     * Description:汉字转换位汉语拼音首字母，英文字符不变，特殊字符丢失 支持多音字
     *
     * @param
     * @return String
     * @throws
     * @Author anguo Create Date: 2015年11月2日 下午5:28:17
     */
    public static String converterToFirstSpell(String chines) {
        StringBuffer pinyinName = new StringBuffer();
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    // 取得当前汉字的所有全拼
                    String[] strs = PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat);
                    if (strs != null) {
                        for (int j = 0; j < strs.length; j++) {
                            // 取首字母
                            pinyinName.append(strs[j].charAt(0));
                            if (j != strs.length - 1) {
                                pinyinName.append(",");
                            }
                        }
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyinName.append(nameChar[i]);
            }
            pinyinName.append(" ");
        }
        // return pinyinName.toString();
        return parseTheChineseByObject(discountTheChinese(pinyinName.toString()));
    }

    /**
     *
     * Description:汉字转换位汉语全拼，英文字符不变，特殊字符丢失
     * 支持多音字，生成方式如（重当参:zhongdangcen,zhongdangcan,chongdangcen
     *
     * @param
     * @return String
     * @throws
     * @Author anguo Create Date: 2015年11月2日 下午5:29:29
     */
    public static String converterToSpell(String chines) {
        StringBuffer pinyinName = new StringBuffer();
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    // 取得当前汉字的所有全拼
                    String[] strs = PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat);
                    if (strs != null) {
                        for (int j = 0; j < strs.length; j++) {
                            pinyinName.append(strs[j]);
                            if (j != strs.length - 1) {
                                pinyinName.append(",");
                            }
                        }
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyinName.append(nameChar[i]);
            }
            pinyinName.append(" ");
        }
        // return pinyinName.toString();
        return parseTheChineseByObject(discountTheChinese(pinyinName.toString()));
    }

    /**
     *
     * Description:去除多音字重复数据
     *
     * @param
     * @return List<Map<String,Integer>>
	 * @
     * throws
     * @Author anguo Create Date: 2015年11月2日 下午5:31:00
     */
    private static List<Map<String, Integer>> discountTheChinese(String theStr) {
        // 去除重复拼音后的拼音列表
        List<Map<String, Integer>> mapList = new ArrayList<Map<String, Integer>>();
        // 用于处理每个字的多音字，去掉重复
        Map<String, Integer> onlyOne = null;
        String[] firsts = theStr.split(" ");
        // 读出每个汉字的拼音
        for (String str : firsts) {
            onlyOne = new Hashtable<String, Integer>();
            String[] china = str.split(",");
            // 多音字处理
            for (String s : china) {
                Integer count = onlyOne.get(s);
                if (count == null) {
                    onlyOne.put(s, new Integer(1));
                } else {
                    onlyOne.remove(s);
                    count++;
                    onlyOne.put(s, count);
                }
            }
            mapList.add(onlyOne);
        }
        return mapList;
    }

    /**
     *
     * Description:解析并组合拼音，对象合并方案
     *
     * @param
     * @return String
     * @throws
     * @Author anguo Create Date: 2015年11月2日 下午5:31:46
     */
    private static String parseTheChineseByObject(List<Map<String, Integer>> list) {
        Map<String, Integer> first = null; // 用于统计每一次,集合组合数据
        // 遍历每一组集合
        for (int i = 0; i < list.size(); i++) {
            // 每一组集合与上一次组合的Map
            Map<String, Integer> temp = new Hashtable<String, Integer>();
            // 第一次循环，first为空
            if (first != null) {
                // 取出上次组合与此次集合的字符，并保存
                for (String s : first.keySet()) {
                    for (String s1 : list.get(i).keySet()) {
                        String str = s + s1;
                        temp.put(str, 1);
                    }
                }
                // 清理上一次组合数据
                if (temp != null && temp.size() > 0) {
                    first.clear();
                }
            } else {
                for (String s : list.get(i).keySet()) {
                    String str = s;
                    temp.put(str, 1);
                }
            }
            // 保存组合数据以便下次循环使用
            if (temp != null && temp.size() > 0) {
                first = temp;
            }
        }
        String returnStr = "";
        if (first != null) {
            // 遍历取出组合字符串
            for (String str : first.keySet()) {
                returnStr += (str + ".");
            }
        }
        if (returnStr.length() > 0) {
            returnStr = returnStr.substring(0, returnStr.length() - 1);
        }
        return returnStr;
    }

    /**
     *
     * Description:名字转拼音
     *
     * @param
     * @return String
     * @throws
     * @Author anguo Create Date: 2015年10月30日 下午2:57:56
     */
    public static String getPinYin(String name) {
        char[] t1 = null;
        t1 = name.toCharArray();
        String[] t2 = new String[t1.length];
        //设置汉字拼音输出的格式
        HanyuPinyinOutputFormat pyFormat = new HanyuPinyinOutputFormat();
        // UPPERCASE：大写  (ZHONG)  
        // LOWERCASE：小写  (zhong) 
        pyFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        // WITHOUT_TONE：无音标  (zhong)  
        // WITH_TONE_NUMBER：1-4数字表示英标  (zhong4)  
        // WITH_TONE_MARK：直接用音标符（必须WITH_U_UNICODE否则异常）  (zhòng)
        pyFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        // WITH_V：用v表示ü  (nv)  
        // WITH_U_AND_COLON：用"u:"表示ü  (nu:)  
        // WITH_U_UNICODE：直接用ü (nü)
        pyFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

        String t3 = "";
        int t4 = t1.length;
        try {
            for (int i = 0; i < t4; i++) {
                // 判断能否为汉字字符
                // System.out.println(t1[i]);
                if (Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], pyFormat);// 将汉字的几种全拼都存到t2数组中
                    if (t2 != null) {
                        t3 += t2[0];// 取出该汉字全拼的第一种读音并连接到字符串t3后
                    }
                } else {
                    // 如果不是汉字字符，间接取出字符并连接到字符串t3后
                    t3 += Character.toString(t1[i]);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return t3;
    }

    /**
     *
     * Description:提取每个汉字的首字母
     *
     * @param
     * @return String
     * @throws
     * @Author anguo Create Date: 2015年10月30日 下午4:34:46
     */
    public static String getPinYinHeadChar(String str) {
        String convert = "";
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            // 提取汉字的首字母
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            }
        }
        return convert;

    }

    public static void main(String[] args) {
        //String str = "长沙市长";
        String str = "黄耀稥";

        String pinyin1 = Pinyin4jUtil.getPinYin(str);
        System.out.println(str + " pin yin ：" + pinyin1);

        String pinyin = Pinyin4jUtil.getPinYinHeadChar(str);
        System.out.println(str + " short pin yin ：" + pinyin);
        System.out.println("." + pinyin1 + "." + pinyin + ".");

    }

}
