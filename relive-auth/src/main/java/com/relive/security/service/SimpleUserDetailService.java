package com.relive.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author: ReLive
 * @date: 2021/8/26 8:32 下午
 */
@Service
public class SimpleUserDetailService implements UserDetailsService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws AuthenticationException {
        if (!"user".equals(username)) {
            throw new UsernameNotFoundException("not found user");
        }
        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority("admin");
        return new DefaultUserDetails("user", bCryptPasswordEncoder.encode("user"), Arrays.asList(grantedAuthority));
    }
}
