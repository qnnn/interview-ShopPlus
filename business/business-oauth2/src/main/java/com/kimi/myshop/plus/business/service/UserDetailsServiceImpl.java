package com.kimi.myshop.plus.business.service;

import com.google.common.collect.Lists;
import com.kimi.myshop.plus.provider.api.UmsAdminService;
import com.kimi.myshop.plus.provider.domain.UmsAdmin;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 模拟认证.
 * @author 郭富城
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Reference(version = "1.0.0")
    private UmsAdminService umsAdminService;

//    public static final String USERNAME = "admin";
//    public static final String PASSWORD = "$2a$10$G//kgKEJdM26nlRfmH.aRO3o6e010tTEL1rU/G2tsF7sFj5v5bJ1.";


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // 给每个用户授予 USER 权限
        List<GrantedAuthority> grantedAuthorities= Lists.newArrayList();
        // 临时授权
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("USER");
        grantedAuthorities.add(grantedAuthority);

        UmsAdmin umsAdmin = umsAdminService.get(s);

        // 账号存在，则授予 USER 权限
        if(umsAdmin!=null){
            return new User(umsAdmin.getUsername(),umsAdmin.getPassword(),grantedAuthorities);
        }

        return null;
    }
}
