package com.winer.winerfilestorage.utils;

import org.apache.commons.codec.binary.Base64;
import org.springframework.lang.Nullable;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * @program: winer-file-storage-dev
 * @description: 字符串工具类
 * @Author Jekin
 * @Date 2021/4/26
 */
public class StringUtils {

    /**
     * 包分隔表达式
     */
    private static final String PACKAGE_OR_URL_DELIMITERS = ",; \t\n";

    private static final char EXTENSION_SEPARATOR = '.';

    /**
     * 判断是否为null或空字符串
     *
     * @param str 字符串
     * @return
     */
    public static boolean isNullOrEmpty(Object str) {
        return (str == null || "".equals(str));
    }

    /**
     * 长度是否大于零
     *
     * @param str 字符串
     * @return
     */
    public static boolean hasLength(CharSequence str) {
        return (str != null && str.length() > 0);
    }

    /**
     * 长度是否大于零
     *
     * @param str 字符串
     * @return
     */
    public static boolean hasLength(String str) {
        return hasLength((CharSequence) str);
    }

    /**
     * 判断是否为非null或非空白字符串
     *
     * @param str 字符串
     * @return
     */
    public static boolean isNotNullOrBlank(CharSequence str) {
        return !isNullOrBlank(str);
    }

    /**
     * 判断是否为非null或非空白字符串
     *
     * @param str 字符串
     * @return
     */
    public static boolean isNotNullOrBlank(String str) {
        return !isNullOrBlank(str);
    }

    /**
     * 判断是否为null或空白字符串
     *
     * @param str 字符串
     * @return
     */
    public static boolean isNullOrBlank(CharSequence str) {
        if (!hasLength(str)) {
            return true;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    /**
     * 判断是否为null或空白字符串
     *
     * @param str 字符串
     * @return
     */
    public static boolean isNullOrBlank(String str) {
        return isNullOrBlank((CharSequence) str);
    }

    /**
     * 是否包含空白
     *
     * @param str 字符串
     * @return
     */
    public static boolean containsWhitespace(CharSequence str) {
        if (!hasLength(str)) {
            return false;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否包含空白
     *
     * @param str 字符串
     * @return
     */
    public static boolean containsWhitespace(String str) {
        return containsWhitespace((CharSequence) str);
    }

    private static String changeFirstCharacterCase(String str, boolean capitalize) {
        if (str == null || str.length() == 0) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str.length());
        if (capitalize) {
            sb.append(Character.toUpperCase(str.charAt(0)));
        } else {
            sb.append(Character.toLowerCase(str.charAt(0)));
        }
        sb.append(str.substring(1));
        return sb.toString();
    }

    /**
     * 骆锋命名(首个字母为小写)
     *
     * @param str 字符串
     * @return
     */
    public static String lowerCaseCapitalize(String str) {
        return changeFirstCharacterCase(str, false);
    }

    /**
     * 帕斯卡命名(首个字母为大写)
     *
     * @param str 字符串
     * @return
     */
    public static String upperCaseCapitalize(String str) {
        return changeFirstCharacterCase(str, true);
    }

    /**
     * 数据库标准化命名(自动去掉两关空白)
     *
     * @param str 字符串
     */
    public static String dbStandardCapitalize(String str) {
        if (str == null) {
            return str;
        }
        str = str.trim();
        if (str.length() == 0) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str.length());
        sb.append(Character.toLowerCase(str.charAt(0)));
        for (int i = 1; i < str.length(); i++) {
            char chr = str.charAt(i);
            if (Character.isUpperCase(chr)) {
                boolean isAdd = i > 1 && str.charAt(i - 1) != '_';
                if (i <= 1 || isAdd) {
                    sb.append("_");
                }
            }
            sb.append(Character.toLowerCase(chr));
        }
        return sb.toString();
    }

    /**
     * 获取数据库列对应的属性名称
     *
     * @param columnName 列名称
     * @return
     */
    public static String columnPropertieName(String columnName) {
        return nameCapitalize(columnName, '_');
    }

    /**
     * 获取配置属性名
     *
     * @param str 字符串
     */
    public static String configurePropertieName(String str) {
        return nameCapitalize(str, '-');
    }

    /**
     * 名称序列
     *
     * @param str
     * @param delimiter
     * @return
     */
    private static String nameCapitalize(String str, char delimiter) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str.length());
        sb.append(Character.toLowerCase(str.charAt(0)));
        boolean lastSeparate = false;
        for (int i = 1; i < str.length(); i++) {
            char chr = str.charAt(i);
            lastSeparate = i > 1 && str.charAt(i - 1) == delimiter;
            if (chr == delimiter) {
                continue;
            }
            if (lastSeparate) {
                sb.append(Character.toUpperCase(chr));
            } else {
                sb.append(chr);
            }
        }
        return sb.toString();
    }

    /**
     * 替换
     *
     * @param inString   输入字符
     * @param oldPattern 旧字符
     * @param newPattern 新字符
     * @return
     */
    public static String replace(String inString, String oldPattern, String newPattern) {
        if (!hasLength(inString) || !hasLength(oldPattern) || newPattern == null) {
            return inString;
        }
        StringBuilder sb = new StringBuilder();
        // our position in the old string
        int pos = 0;
        int index = inString.indexOf(oldPattern);
        // the index of an occurrence we've found, or -1
        int patLen = oldPattern.length();
        while (index >= 0) {
            sb.append(inString.substring(pos, index));
            sb.append(newPattern);
            pos = index + patLen;
            index = inString.indexOf(oldPattern, pos);
        }
        sb.append(inString.substring(pos));
        // remember to append any characters to the right of a match
        return sb.toString();
    }


    /**
     * 获左边获取字符长度
     *
     * @param value  值
     * @param length 长度
     * @return
     */
    public static String getLeft(String value, int length) {
        if (value == null) {
            return null;
        }
        if (value.length() <= length) {
            return value;
        }
        return value.substring(0, length);
    }

    /**
     * 获右边获取字符长度
     *
     * @param value  值
     * @param length 长度
     * @return
     */
    public static String getRigth(String value, int length) {
        if (value == null) {
            return null;
        }
        if (value.length() <= length) {
            return value;
        }
        return value.substring(value.length() - length, value.length());
    }


    /**
     * 转换为数组
     *
     * @param enumeration 枚举
     * @return
     */
    public static String[] toArray(Enumeration<String> enumeration) {
        if (enumeration == null) {
            return null;
        }
        List<String> list = Collections.list(enumeration);
        return list.toArray(new String[list.size()]);
    }

    /**
     * 连接字符窜
     *
     * @param arr       数组
     * @param separator 分隔符
     * @return
     */
    public static String join(String[] arr, String separator) {
        if (arr == null || arr.length == 0) {
            return "";
        }
        if (arr.length == 1) {
            return arr[0];
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                sb.append(separator);
            }
            sb.append(arr[i]);
        }
        return sb.toString();
    }

    /**
     * 返回一个新字符串，该字符串通过在此实例中的字符左侧填充字符来达到指定的总长度，从而实现右对齐。
     *
     * @param str
     * @param totalWidth  总账度
     * @param paddingChar 填充的字符串
     * @return
     */
    public static String padLeft(String str, int totalWidth, char paddingChar) {
        int diff = totalWidth - str.length();
        if (diff <= 0) {
            return str;
        }
        char[] charr = new char[totalWidth];
        System.arraycopy(str.toCharArray(), 0, charr, diff, str.length());
        for (int i = 0; i < diff; i++) {
            charr[i] = paddingChar;
        }
        return new String(charr);
    }

    /**
     * 返回一个新字符串，该字符串通过在此字符串中的字符右侧填充指定的 Unicode 字符来达到指定的总长度，从而使这些字符左对齐。
     *
     * @param str
     * @param totalWidth  总账度
     * @param paddingChar 填充的字符串
     * @return
     */
    public static String padRight(String str, int totalWidth, char paddingChar) {
        int diff = totalWidth - str.length();
        if (diff <= 0) {
            return str;
        }
        char[] charr = new char[totalWidth];
        System.arraycopy(str.toCharArray(), 0, charr, 0, str.length());
        for (int i = str.length(); i < totalWidth; i++) {
            charr[i] = paddingChar;
        }
        return new String(charr);
    }

    /**
     * 移除所有空格、换行符、Tab键
     *
     * @param str
     * @return 返回云掉所有空格的字符窜
     */
    public static String removeALLWhitespace(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        if (str.length() == 0) {
            return str;
        }
        char[] chars = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char ch : chars) {
            if (!Character.isWhitespace(ch)) {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    /**
     * 移除开始
     *
     * @param str        字符
     * @param removeChar 移除字符
     * @return
     */
    public static String removeStart(String str, char removeChar) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return str;
        }
        String value = String.valueOf(removeChar);
        while (str.length() > 0 && str.startsWith(value)) {
            str = str.substring(value.length());
        }
        return str;
    }

    /**
     * 移除结束
     *
     * @param str        字符
     * @param removeChar 移除字符
     * @return
     */
    public static String removeEnd(String str, char removeChar) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return str;
        }
        String value = String.valueOf(removeChar);
        while (str.length() > 0 && str.endsWith(value)) {
            str = str.substring(0, str.length() - value.length());
        }
        return str;
    }

    /**
     * 是否是空白字符或换行字符
     *
     * @param c
     * @return
     */
    public static boolean isWhiteChar(char c) {
        return c == ' ' || c == '\t' || c == '\r' || c == '\n';
    }

    /**
     * 获取字二进制
     *
     * @param string  字符
     * @param charset 编码
     * @return
     */
    public static byte[] getBytes(final String string, final Charset charset) {
        if (string == null) {
            return null;
        }
        return string.getBytes(charset);
    }

    /**
     * 获取按字符 utf8 字节编码
     *
     * @param string
     * @return
     */
    public static byte[] getBytesUtf8(final String string) {
        return getBytes(string, StandardCharsets.UTF_8);
    }

    /**
     * 获取二进制字符窜
     *
     * @param bytes   字节数组
     * @param charset 编码
     * @return
     */
    public static String getString(final byte[] bytes, final Charset charset) {
        return bytes == null ? null : new String(bytes, charset);
    }

    /**
     * 获取二进制的utf8编码字符窜
     *
     * @param bytes 字节数组
     * @return
     */
    public static String getStringUtf8(final byte[] bytes) {
        return getString(bytes, StandardCharsets.UTF_8);
    }

    /**
     * 是否是 base64 位编码
     *
     * @param base64  值
     * @param charset 编码类型
     * @return
     */
    public static boolean isBase64(final String base64, final Charset charset) {
        return Base64.isBase64(StringUtils.getBytes(base64, charset));
    }

    /**
     * 是否是utf-8 的 base64 位编码
     *
     * @param base64 值
     * @return
     */
    public static boolean isBase64Utf8(final String base64) {
        return Base64.isBase64(StringUtils.getBytes(base64, StandardCharsets.UTF_8));
    }

    /**
     * 字符窜编码为Bese64字节数组
     *
     * @param str     值
     * @param charset 编码
     * @return
     */
    public static byte[] encodeBase64(final String str, final Charset charset) {
        return Base64.encodeBase64(StringUtils.getBytes(str, charset));
    }

    /**
     * 字符窜编码为Bese64字符串
     *
     * @param str     值
     * @param charset 编码
     * @return
     */
    public static String encodeBase64String(final String str, final Charset charset) {
        return getString(encodeBase64(str, charset), charset);
    }

    /**
     * 采用utf8将字符窜编码为Bese64二进制
     *
     * @param str 值
     * @return
     */
    public static byte[] encodeBase64Utf8(final String str) {
        return encodeBase64(str, StandardCharsets.UTF_8);
    }

    /**
     * 采用Utf8字符窜编码为Bese64字符串
     *
     * @param str 值
     * @return
     */
    public static String encodeBase64Utf8String(final String str) {
        return getString(encodeBase64Utf8(str), StandardCharsets.UTF_8);
    }

    /**
     * 将base64 解码为字节数组
     *
     * @param base64  值
     * @param charset 编码
     * @return
     */
    public static byte[] decodeBase64(final String base64, final Charset charset) {
        return Base64.decodeBase64(StringUtils.getBytes(base64, charset));
    }

    /**
     * 将base64 解码为字符窜
     *
     * @param base64  base64值
     * @param charset 编码
     * @return
     */
    public static String decodeBase64String(final String base64, final Charset charset) {
        return getString(decodeBase64(base64, charset), charset);
    }

    /**
     * 使用utf8将base64 解码为字节数组
     *
     * @param base64 base64值
     * @return
     */
    public static byte[] decodeBase64Utf8(final String base64) {
        return decodeBase64(base64, StandardCharsets.UTF_8);
    }

    /**
     * 采用Utf8将base64 解码为字符窜
     *
     * @param base64 base64值
     * @return
     */
    public static String decodeBase64Utf8String(final String base64) {
        return getString(decodeBase64Utf8(base64), StandardCharsets.UTF_8);
    }

    /**
     * 传入一个字符串，获取倒序结果,如果字符串为null获者为空字符串，就返回自身
     *
     * @param code 例如："123456"
     * @return 结果："654321"
     */
    public static String reverseOrder(final String code) {
        if (code == null || "".equals(code)) {
            return code;
        }
        char[] charArray = code.toCharArray();
        String result = "";
        for (int i = charArray.length - 1; i >= 0; i--) {
            result += charArray[i];
        }
        return result;
    }

    public final static String REGEX_NEWLINE = "\n|\r\n|\r";

    /**
     * split 换行
     *
     * @param value 值
     * @return
     */
    public static String[] splitNewLine(String value) {
        if (value == null) {
            return null;
        }
        return value.split(REGEX_NEWLINE);
    }

    /**
     * 字符串分割
     *
     * @param regex         正则表达式
     * @param source        源字符串
     * @param isRemoveEmpty 是否移除空项
     * @return
     */
    public static String[] split(String regex, String source, boolean isRemoveEmpty) {
        String[] tmp = source.split(regex);
        if (!isRemoveEmpty) {
            return tmp;
        }
        List<String> res = new ArrayList<>();
        for (String s : tmp) {
            if (!StringUtils.isNullOrBlank(s)) {
                res.add(s.trim());
            }
        }
        return res.toArray(new String[res.size()]);
    }

    /**
     * 字符串分割，移除空项
     *
     * @param regex  正则表达式
     * @param source 源字符串
     * @return
     */
    public static String[] split(String regex, String source) {
        return split(regex, source, true);
    }

    /**
     * 根据标记解析为数组
     * <p>
     * {@link org.springframework.util.StringUtils#tokenizeToStringArray}
     * </p>
     *
     * @param str
     * @param delimiters
     * @return
     */
    public static String[] tokenizeToStringArray(@Nullable String str, String delimiters) {
        return org.springframework.util.StringUtils.tokenizeToStringArray(str, delimiters);
    }

    /**
     * Url或包的拼接(,; \n\r) 转换为数组
     *
     * @param str
     * @return
     */
    public static String[] urlOrPackageToStringArray(@Nullable String str) {
        return tokenizeToStringArray(str, PACKAGE_OR_URL_DELIMITERS);
    }
}
