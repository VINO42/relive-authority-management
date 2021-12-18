package com.relive.utils;

import com.relive.enums.FileTypeEnum;
import com.relive.exception.UtilsException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author: ReLive
 * @date: 2021/8/26 12:47 下午
 */
@Slf4j
public class FileUtils {

    private FileUtils() {
    }

    public static boolean verifyFileType(InputStream inputStream, FileTypeEnum type) {
        String fileHeader = getFileHeader(inputStream);
        if (fileHeader.startsWith(type.getCode())) {
            return true;
        }
        return false;
    }

    /**
     * 获取文件头信息
     */
    public static String getFileHeader(InputStream fileInputStream) {
        String value = null;
        try {
            byte[] b = new byte[4];
            fileInputStream.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception e) {
            throw new UtilsException("解析文件头信息错误");
        } finally {
            if (null != fileInputStream) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    log.error("输入流关闭错误");
                }
            }
        }
        return value;
    }

    /**
     * 将要读取文件头信息的文件的byte数组转换成string类型表示
     *
     * @param src 要读取文件头信息的文件的byte数组
     * @return 文件头信息
     */
    private static String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (int i = 0; i < src.length; i++) {
            // 以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式，并转换为大写
            hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return builder.toString();
    }
}
