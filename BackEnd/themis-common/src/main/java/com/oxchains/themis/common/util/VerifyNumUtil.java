package com.oxchains.themis.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author luoxuri
 * @create 2017-12-01 16:32
 **/
public class VerifyNumUtil {

    /**
     * 判断这个字符串中的值是一个符合钱的输入规范的数字，保留两位小数
     * @param str
     * @return
     */
    public static boolean isNumber(String str){
        Pattern pattern = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()){
            return false;
        }
        return true;
    }

}
