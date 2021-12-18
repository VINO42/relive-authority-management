package com.relive.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 将日志堆栈信息输出到文件
 *
 * @Author ReLive
 * @Date 2020/7/12-19:03
 */
public class ExceptionUtils {
    private ExceptionUtils() {
    }

    public static String getMessage(Exception e) {
        StringWriter stringWriter = null;
        PrintWriter printWriter = null;
        try {
            stringWriter = new StringWriter();
            printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            printWriter.flush();
            stringWriter.flush();
        } finally {
            if (stringWriter != null) {
                try {
                    stringWriter.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (printWriter != null) {
                printWriter.close();
            }
        }
        return stringWriter.toString();
    }
}
