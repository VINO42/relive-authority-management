package com.relive.utils;

import com.relive.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.passay.CharacterData;
import org.passay.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * @Author ReLive
 * @Date 2021/3/18-22:00
 */
@Slf4j
public class PasswordUtils {
    /**
     * 生成随机密码
     *
     * @param len 密码长度
     * @return
     */
    public static String randomPassword(int len) {
        PasswordGenerator passwordGenerator = new PasswordGenerator();
        EnglishCharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
        lowerCaseRule.setNumberOfCharacters(2);
        EnglishCharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
        upperCaseRule.setNumberOfCharacters(2);
        EnglishCharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(2);
        CharacterData specialChars = new CharacterData() {
            public String getErrorCode() {
                return "INSUFFICIENT_COMPLEXITY";
            }

            public String getCharacters() {
                return "~!@#$%^&*()_+=";
            }
        };
        CharacterRule splCharRule = new CharacterRule(specialChars);
        splCharRule.setNumberOfCharacters(2);
        return passwordGenerator.generatePassword(len, new CharacterRule[]{splCharRule, lowerCaseRule, upperCaseRule, digitRule});
    }

    public static boolean verifyPasswordStrength(String password) {
        List<Rule> rules = new ArrayList<>();
        //密码长度8-11
        LengthRule lengthRule = new LengthRule(8, 11);
        rules.add(lengthRule);
        //密码不允许有空白符
        WhitespaceRule whitespaceRule = new WhitespaceRule();
        rules.add(whitespaceRule);
        //密码包含2位以上小写字母
        CharacterRule lowerCaseRule = new CharacterRule(EnglishCharacterData.LowerCase, 2);
        rules.add(lowerCaseRule);
        //密码包含2位以上大写字母
        CharacterRule upperCaseRule = new CharacterRule(EnglishCharacterData.UpperCase, 2);
        rules.add(upperCaseRule);
        //密码包含2位以上数字
        CharacterRule digitRule = new CharacterRule(EnglishCharacterData.Digit, 2);
        rules.add(digitRule);
        CharacterData specialChars = new CharacterData() {
            public String getErrorCode() {
                return "INSUFFICIENT_COMPLEXITY";
            }

            public String getCharacters() {
                return "~!@#$%^&*()_+=";
            }
        };
        //密码包含2位以上特殊字符
        CharacterRule splCharRule = new CharacterRule(specialChars, 2);
        rules.add(splCharRule);
        //密码不允许重复字符3位以上
        RepeatCharacterRegexRule repeatCharacter = new RepeatCharacterRegexRule(3);
        rules.add(repeatCharacter);

        PasswordValidator passwordValidator = getPasswordValidator(rules);
        PasswordData passwordData = new PasswordData(password);
        RuleResult validate = passwordValidator.validate(passwordData);
        boolean valid = validate.isValid();
        if (!valid) {
            throw new BusinessException(passwordValidator.getMessages(validate).get(0));
        }
        return valid;
    }

    private static PasswordValidator getPasswordValidator(List<Rule> rules) {
        MessageResolver messageResolver = getMessageResolver("messages.properties");
        if (Objects.isNull(messageResolver)) {
            return new PasswordValidator(rules);
        }
        return new PasswordValidator(messageResolver, rules);
    }

    private static MessageResolver getMessageResolver(String path) {
        //自定义消息解析
        MessageResolver resolver = null;
        try {
            Properties props = new Properties();
            Resource resource = new ClassPathResource(path);
            props.load(resource.getInputStream());
            resolver = new PropertiesMessageResolver(props);
        } catch (Exception e) {
            log.error("自定义消息文件不存在", e);
        }
        return resolver;
    }
}

