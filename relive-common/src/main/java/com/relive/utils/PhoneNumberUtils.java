package com.relive.utils;

import com.google.i18n.phonenumbers.PhoneNumberToCarrierMapper;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;

import java.util.Locale;

/**
 * @author: relive
 * @date: 2021/8/17 3:59 下午
 * @description:
 */
public class PhoneNumberUtils {

    private static PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    private static PhoneNumberToCarrierMapper carrierMapper = PhoneNumberToCarrierMapper.getInstance();

    private static PhoneNumberOfflineGeocoder geocoder = PhoneNumberOfflineGeocoder.getInstance();

    /**
     * 根据国家代码和手机号  判断手机号是否有效
     *
     * @param phoneNumber
     * @param countryCode
     * @return
     */
    public static boolean checkPhoneNumber(String phoneNumber, String countryCode) {
        int ccode = Integer.parseInt(countryCode);
        long phone = Long.parseLong(phoneNumber);
        Phonenumber.PhoneNumber pn = new Phonenumber.PhoneNumber();
        pn.setCountryCode(ccode);
        pn.setNationalNumber(phone);
        return phoneNumberUtil.isValidNumber(pn);
    }

    /**
     * 根据国家代码和手机号  判断手机运营商
     *
     * @param phoneNumber
     * @param countryCode
     * @return
     */
    public static String getPhoneOperator(String phoneNumber, String countryCode) {
        int ccode = Integer.parseInt(countryCode);
        long phone = Long.parseLong(phoneNumber);
        Phonenumber.PhoneNumber pn = new Phonenumber.PhoneNumber();
        pn.setCountryCode(ccode);
        pn.setNationalNumber(phone);
        //返回结果只有英文，自己转成成中文
        String carrierEn = carrierMapper.getNameForNumber(pn, Locale.ENGLISH);
        String carrierZh = "";
        carrierZh += geocoder.getDescriptionForNumber(pn, Locale.CHINESE);
        switch (carrierEn) {
            case "China Mobile":
                carrierZh += "移动";
                break;
            case "China Unicom":
                carrierZh += "联通";
                break;
            case "China Telecom":
                carrierZh += "电信";
                break;
            default:
                break;
        }
        return carrierZh;
    }


    /**
     * @param @param  phoneNumber
     * @param @param  countryCode
     * @param @return
     * @throws
     * @Description: 根据国家代码和手机号  手机归属地
     */
    public static String getPhoneAttribution(String phoneNumber, String countryCode) {
        int ccode = Integer.parseInt(countryCode);
        long phone = Long.parseLong(phoneNumber);
        Phonenumber.PhoneNumber pn = new Phonenumber.PhoneNumber();
        pn.setCountryCode(ccode);
        pn.setNationalNumber(phone);
        return geocoder.getDescriptionForNumber(pn, Locale.CHINESE);
    }

}
