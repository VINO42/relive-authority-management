package com.relive.enums;

import lombok.Getter;

/**
 * @author: ReLive
 * @date: 2021/8/26 1:02 下午
 * <p>
 * 文件类型枚举类
 */
@Getter
public enum FileTypeEnum {

    JPG("jpg", "FFD8FF"),
    PNG("png", "89504E47"),
    GIF("gif", "47494638"),
    TIF("tif", "49492A00"),
    BMP("bmp", "424D"),
    DWG("dwg", "41433130"),
    PSD("psd", "38425053"),
    RTF("rtf", "7B5C727466"),
    XML("xml", "3C3F786D6C"),
    HTML("html", "68746D6C3E"),
    EML("eml", "44656C69766572792D646174653A"),
    DOC("doc", "D0CF11E0"),
    MBD("mbd", "5374616E64617264204A"),
    PS("ps", "252150532D41646F6265"),
    PDF("pdf", "255044462D312E"),
    DOCX("docx", "504B0304"),
    RAR("rar", "52617221"),
    WAV("wav", "57415645"),
    AVI("avi", "41564920"),
    RM("rm", "2E524D46"),
    MPG("mpg", "000001BA"),
    MOV("mov", "6D6F6F76"),
    ASF("asf", "3026B2758E66CF11"),
    MID("mid", "4D546864"),
    GZ("gz", "1F8B08"),
    exe_OR_DLL("exe/dll", "4D5A9000"),
    TXT("txt", "75736167");

    private String type;
    private String code;

    FileTypeEnum(String type, String code) {
        this.type = type;
        this.code = code;
    }
}
