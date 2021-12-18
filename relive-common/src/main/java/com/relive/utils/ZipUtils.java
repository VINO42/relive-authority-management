package com.relive.utils;

import com.relive.exception.UtilsException;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.EncryptionMethod;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ZipUtils {
    private ZipUtils() {
    }

    public static void zipFile(String filePath, String zipName) {
        try {
            zipName = verifyZipNameSuffix(zipName);
            ZipFile zipFile = new ZipFile(zipName);
            zipFile.addFile(new File(filePath));
        } catch (Exception e) {
            throw new UtilsException("添加压缩文件失败");
        }
    }

    public static void zipMultipleFile(List<File> files, String zipName) {
        try {
            zipName = verifyZipNameSuffix(zipName);
            ZipFile zipFile = new ZipFile(zipName);
            zipFile.addFiles(files);
        } catch (Exception e) {
            throw new UtilsException("添加多个压缩文件失败");
        }
    }

    public static void zipFolder(String folderPath, String zipName, String keyStore, EncryptionMethod encryptionMethod) {
        try {
            zipName = verifyZipNameSuffix(zipName);
            ZipFile zipFile = new ZipFile(zipName);
            zipFile.addFolder(new File(folderPath));
        } catch (Exception e) {
            throw new UtilsException("添加压缩目录失败");
        }
    }

    public static void zipFileByAESEncrypt(String filePath, String zipName, String keyStore) {
        zipName = verifyZipNameSuffix(zipName);
        zipFileByEncrypt(filePath, zipName, keyStore, EncryptionMethod.AES);
    }

    public static void zipFileByStandardEncrypt(String filePath, String zipName, String keyStore) {
        zipName = verifyZipNameSuffix(zipName);
        zipFileByEncrypt(filePath, zipName, keyStore, EncryptionMethod.ZIP_STANDARD);
    }

    protected static void zipFileByEncrypt(String filePath, String zipName, String keyStore, EncryptionMethod encryptionMethod) {
        try {
            ZipParameters zipParameters = getZipParameters(encryptionMethod);
            ZipFile zipFile = new ZipFile(zipName, keyStore.toCharArray());
            zipFile.addFile(new File(filePath), zipParameters);
        } catch (Exception e) {
            throw new UtilsException("添加加密压缩文件失败");
        }
    }

    public static void zipMultipleFileByAESEncrypt(List<File> files, String zipName, String keyStore) {
        zipName = verifyZipNameSuffix(zipName);
        zipMultipleFileByEncrypt(files, zipName, keyStore, EncryptionMethod.AES);
    }

    public static void zipMultipleFileByStandardEncrypt(List<File> files, String zipName, String keyStore) {
        zipName = verifyZipNameSuffix(zipName);
        zipMultipleFileByEncrypt(files, zipName, keyStore, EncryptionMethod.ZIP_STANDARD);
    }

    protected static void zipMultipleFileByEncrypt(List<File> files, String zipName, String keyStore, EncryptionMethod encryptionMethod) {
        try {
            ZipParameters zipParameters = getZipParameters(encryptionMethod);
            ZipFile zipFile = new ZipFile(zipName, keyStore.toCharArray());
            zipFile.addFiles(files, zipParameters);
        } catch (Exception e) {
            throw new UtilsException("添加加密压缩文件失败");
        }
    }

    public static void zipFolderByAESEncrypt(String folderPath, String zipName, String keyStore) {
        zipName = verifyZipNameSuffix(zipName);
        zipFolderByEncrypt(folderPath, zipName, keyStore, EncryptionMethod.AES);
    }

    protected static void zipFolderByEncrypt(String folderPath, String zipName, String keyStore, EncryptionMethod encryptionMethod) {
        try {
            ZipParameters zipParameters = getZipParameters(encryptionMethod);
            ZipFile zipFile = new ZipFile(zipName, keyStore.toCharArray());
            zipFile.addFolder(new File(folderPath), zipParameters);
        } catch (Exception e) {
            throw new UtilsException("添加加密压缩目录失败");
        }
    }

    public static void splitZipFileByAESEncrypt(String filePath, String zipName, String keyStore) {
        zipName = verifyZipNameSuffix(zipName);
        splitZipFileByEncrypt(filePath, zipName, keyStore, EncryptionMethod.AES);
    }

    protected static void splitZipFileByEncrypt(String filePath, String zipName, String keyStore, EncryptionMethod encryptionMethod) {
        try {
            ZipParameters zipParameters = getZipParameters(encryptionMethod);
            ZipFile zipFile = new ZipFile(zipName, keyStore.toCharArray());
            int splitLength = 1024 * 1024 * 10; //10MB
            zipFile.createSplitZipFile(Arrays.asList(new File(filePath)), zipParameters, true, splitLength);
        } catch (Exception e) {
            throw new UtilsException("压缩加密分割文件失败");
        }
    }

    public static void splitZipFolderByAESEncrypt(String folderPath, String zipName, String keyStore) {
        zipName = verifyZipNameSuffix(zipName);
        splitZipFolderByEncrypt(folderPath, zipName, keyStore, EncryptionMethod.AES);
    }

    protected static void splitZipFolderByEncrypt(String folderPath, String zipName, String keyStore, EncryptionMethod encryptionMethod) {
        try {
            ZipParameters zipParameters = getZipParameters(encryptionMethod);
            ZipFile zipFile = new ZipFile(zipName, keyStore.toCharArray());
            int splitLength = 1024 * 1024 * 10; //10MB
            zipFile.createSplitZipFileFromFolder(new File(folderPath), zipParameters, true, splitLength);
        } catch (Exception e) {
            throw new UtilsException("压缩加密分割文件失败");
        }
    }

    protected static ZipParameters getZipParameters(EncryptionMethod encryptionMethod) {
        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setEncryptFiles(true);
        zipParameters.setCompressionLevel(CompressionLevel.HIGHER);
        zipParameters.setEncryptionMethod(encryptionMethod);
        return zipParameters;
    }

    private static String verifyZipNameSuffix(String zipName) {
        if (zipName.endsWith(".zip")) {
            return zipName;
        }
        return zipName + ".zip";
    }

    public static void extractAllFile(String zipPath, String keyStore, String destinationPath) {
        try {
            ZipFile zipFile = new ZipFile(zipPath, keyStore.toCharArray());
            zipFile.extractAll(destinationPath);
        } catch (Exception e) {
            throw new UtilsException("提取压缩文件失败");
        }
    }

    public static void extractFile(String zipPath, String keyStore, String destinationPath, String destinationFile) {
        try {
            ZipFile zipFile = new ZipFile(zipPath, keyStore.toCharArray());
            zipFile.extractFile(destinationFile, destinationPath);
        } catch (Exception e) {
            throw new UtilsException("提取单个压缩文件失败");
        }
    }
}
