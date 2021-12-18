package com.relive.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;

/**
 * 验证码工具类
 */
@Slf4j
public class CaptchaUtils {

    private static final String ALL = "azxcvbnmsdfghjkqwertyupZXCVBNMASDFGHJKQWERTYUP0123456789";

    private static final String ALL_NUM = "0123456789";

    private static final int IMAGE_CODE_LENGTH = 5;

    public static String getCode() {
        return getCode(5);
    }

    public static String getCode(int codeLength) {
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < codeLength; i++) {
            char c = "azxcvbnmsdfghjkqwertyupZXCVBNMASDFGHJKQWERTYUP0123456789".charAt(random.nextInt("azxcvbnmsdfghjkqwertyupZXCVBNMASDFGHJKQWERTYUP0123456789".length()));
            code.append(c);
        }
        return code.toString();
    }

    public static String getNumCode(int codeLength) {
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < codeLength; i++) {
            char c = "0123456789".charAt(random.nextInt("0123456789".length()));
            code.append(c);
        }
        return code.toString();
    }

    public static BufferedImage createImg(String code) {
        int width = 135;
        int height = 50;
        Color background = Color.white;
        Font charFont = new Font("黑体", 1, 40);
        int interferLineNum = 5;
        BufferedImage image = new BufferedImage(135, 50, 5);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(background);
        g.fillRect(0, 0, 135, 50);
        image = g.getDeviceConfiguration().createCompatibleImage(135, 50, 3);
        g.dispose();
        g = image.createGraphics();
        Random ran = new Random();
        g.setColor(new Color(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255)));
        for (int i = 0; i < 5; i++) {
            int x1 = ran.nextInt(135);
            int x2 = ran.nextInt(135);
            int y1 = ran.nextInt(50);
            int y2 = ran.nextInt(50);
            g.drawLine(x1, y1, x2, y2);
        }
        g.setFont(charFont);
        FontMetrics fontMetrics = g.getFontMetrics();
        char[] chars = code.toCharArray();
        for (int j = 0; j < chars.length; j++) {
            g.setColor(new Color(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255)));
            g.drawString(String.valueOf(chars[j]), 135 / chars.length * j, fontMetrics.getMaxAscent());
        }
        return image;
    }

    public static String createBase64Image(String code) {
        BufferedImage image = createImg(code);
        return encodeImageToBase64(image);
    }

    public static String encodeImageToBase64(BufferedImage image) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", os);
            return Base64.encodeBase64String(os.toByteArray());
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        } finally {
            IOUtils.closeQuietly(os);
        }
    }

    public static BufferedImage decodeImageToBase64(String base64) {
        try {
            byte[] bytes1 = Base64.decodeBase64(base64);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
            return ImageIO.read(bais);
        } catch (IOException e) {
            log.error("", e);
            return null;
        }
    }

    public static String tranImageFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists())
            throw new FileNotFoundException("文件不存在");
        return StringUtils.newStringUsAscii(Base64.encodeBase64(IOUtils.toByteArray(new FileInputStream(file)), true));
    }
}