package com.relive.security.access;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * @author: ReLive
 * @date: 2021/12/5 8:50 下午
 */
@Slf4j
public class JdbcFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        HttpServletRequest request = ((FilterInvocation)o).getRequest();
        //TODO 返回该请求匹配的角色
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        //TODO 返回所有角色
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
