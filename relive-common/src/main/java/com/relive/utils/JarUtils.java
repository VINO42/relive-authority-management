package com.relive.utils;

import com.relive.exception.UtilsException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @Author ReLive
 * @Date 2021/3/15-19:17
 */
@Slf4j
public class JarUtils {
    private JarUtils() {
    }

    /**
     * @param jarPath       jar包的绝对路径
     * @param jarEntityName 读取jar包中文件的相对路径
     * @return
     */
    public static InputStream readJarFile(String jarPath, String jarEntityName) {
        try {
            JarFile jarFile = new JarFile(jarPath);
            JarEntry jarEntry = jarFile.getJarEntry(jarEntityName);
            return jarFile.getInputStream(jarEntry);
        } catch (IOException e) {
            log.error("读取jar包文件内容错误");
            throw new UtilsException("读取jar包文件内容错误");
        }
    }

    public static String readJarFileToString(String jarPath, String jarEntityName) {
        try {
            InputStream inputStream = readJarFile(jarPath, jarEntityName);
            return IOUtils.toString(inputStream, "UTF-8");
        } catch (IOException e) {
            log.error("读取jar包文件内容错误");
            throw new UtilsException("读取jar包文件内容错误");
        }
    }
}
