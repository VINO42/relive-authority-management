package com.relive.utils;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * 一/二维验证码生成工具类
 */
public class QRCodesUtils {

    public static BufferedImage generateEAN13BarcodeImage(String barcodeText) throws Exception {
        Barcode barcode = BarcodeFactory.createEAN13(barcodeText);
        barcode.setFont(Font.createFont(Font.BOLD,new File("")));

        return BarcodeImageHandler.getImage(barcode);
    }
}
