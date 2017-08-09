package com.jkpg.ruchu.utils;

import java.util.regex.Pattern;

/**
 * Created by qindi on 2017/5/13.
 */

public class RegexUtils {

    //手机号
    public static final String REGEX_PHONE = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,1,3,5-8])|(18[0-9])|(147))\\d{8}$";
    //密码
    public static final String REGEX_PWD = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";

    public static final String REGEX_NAME = "^[a-zA-Z0-9_\\u4e00-\\u9fa5]{1,12}$";
    /**
     * 判断是否匹配正则
     *
     * @param regex 正则表达式
     * @param input 要匹配的字符串
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMatch(String regex, CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }
}
