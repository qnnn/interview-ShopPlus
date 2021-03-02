package com.kimi.myshop.plus.business.controller;

import com.google.common.collect.Maps;
import com.kimi.myshop.plus.business.dto.LoginInfo;
import com.kimi.myshop.plus.business.dto.LoginParam;
import com.kimi.myshop.plus.business.feign.ProfileFeign;
import com.kimi.myshop.plus.commons.dto.ResponseResult;
import com.kimi.myshop.plus.commons.utils.MapperUtils;
import com.kimi.myshop.plus.commons.utils.OkHttpClientUtil;
import com.kimi.myshop.plus.provider.api.UmsAdminService;
import com.kimi.myshop.plus.provider.domain.UmsAdmin;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 登录管理.
 *
 * @param
 * @author chenmc
 * @date 2020/11/22 1:08
 * @return
 */
//@CrossOrigin
@RestController
public class LoginController {

    @Reference(version = "1.0.0")
    private UmsAdminService umsAdminService;


    private static final String URL_OAUTH_TOKEN = "http://localhost:9001/oauth/token";

    @Value("${business.oauth2.grant_type}")
    public String oauthGrantType;

    @Value("${business.oauth2.client_id}")
    public String oauthClientId;

    @Value("${business.oauth2.client_secret}")
    public String oauthClientSecret;

    @Resource(name = "userDetailsServiceBean")
    public UserDetailsService userDetailsService;

    @Resource
    public BCryptPasswordEncoder passwordEncoder;

    @Resource
    public TokenStore tokenStore;

    @Resource
    public ProfileFeign profileFeign;

    @PostMapping(value = "/vue-admin-template/user/login")
    public ResponseResult<Map<String, Object>> login(@RequestBody LoginParam loginParam) {
        // 封装返回的结果集
        Map<String, Object> result = Maps.newHashMap();

        // 验证账号密码
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginParam.getUsername());
        if (userDetails == null || !passwordEncoder.matches(loginParam.getPassword(), userDetails.getPassword())) {
            return new ResponseResult<Map<String, Object>>(ResponseResult.CodeStatus.FAIL, "账号或密码错误", null);
        }

        // 通过 HTTP 客户端请求登录接口
        Map<String, String> params = Maps.newHashMap();
        params.put("username", loginParam.getUsername());
        params.put("password", loginParam.getPassword());
        params.put("grant_type", oauthGrantType);
        params.put("client_id", oauthClientId);
        params.put("client_secret", oauthClientSecret);
        String jsonString = OkHttpClientUtil.sendByPostMap(URL_OAUTH_TOKEN, params);
        try {
            // 解析响应结果封装并返回
            Map<String, Object> jsonMap = MapperUtils.json2map(jsonString);
            String token = String.valueOf(jsonMap.get("access_token"));
            result.put("token", token);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 更新登录时间
        umsAdminService.updateLoginTime(loginParam.getUsername());

        return new ResponseResult<Map<String, Object>>(ResponseResult.CodeStatus.OK, "登录成功", result);
    }

    @GetMapping(value = "/vue-admin-template/user/info")
    public ResponseResult<LoginInfo> info() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String jsonString = profileFeign.info(authentication.getName());
        UmsAdmin umsAdmin = MapperUtils.json2pojoByTree(jsonString, "data", UmsAdmin.class);

        // 返回熔断结果
        if (umsAdmin==null){
            return MapperUtils.json2pojo(jsonString,ResponseResult.class);
        }

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setName(umsAdmin.getUsername());
        loginInfo.setAvatar(umsAdmin.getIcon());
        return new ResponseResult<LoginInfo>(ResponseResult.CodeStatus.OK, "获取用户信息", loginInfo);
    }


    @PostMapping(value = "/vue-admin-template/user/logout")
    public ResponseResult<Void> logout(HttpServletRequest request) {
        String token = request.getParameter("access_token");
        OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token);
        tokenStore.removeAccessToken(oAuth2AccessToken);
        return new ResponseResult<Void>(ResponseResult.CodeStatus.OK, "用户注销", null);
    }


}
