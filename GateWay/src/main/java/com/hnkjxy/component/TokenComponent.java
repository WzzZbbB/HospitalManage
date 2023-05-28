package com.hnkjxy.component;

import cn.hutool.crypto.SecureUtil;
import com.hnkjxy.data.PayloadData;
import com.hnkjxy.entity.User;
import com.hnkjxy.utils.JwtUtil;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.hnkjxy.constant.JwtConstant.TOKEN_USER_INFO;
import static com.hnkjxy.constant.JwtConstant.TOKEN_USER_INFO_USERNAME;
import static com.hnkjxy.service.impl.UserDetailServiceImpl.removePreFix;

/**
 * @version: java version 17
 * @Author: Mr WzzZ
 * @description: Token组件
 * @date: 2023-04-30 12:40
 */
@Component
public class TokenComponent {

    @Value("${wzzz.auth.secret}")
    private String secret;

    @Value("${wzzz.auth.expire}")
    private Integer expire;


    public Integer getExpire() {
        return this.expire;
    }

    public String encode(JSONObject payloadObject) {
        String token = JwtUtil.encode(JWSAlgorithm.HS256,payloadObject, SecureUtil.md5(secret),expire);
        return token;
    }

    public Map<String,Object> decode(String token) {
        Map<String, Object> tokenObject = JwtUtil.decode(token);
        return tokenObject;
    }

    public JSONObject getPayloadObject(String token) {
        Map<String, Object> payloadContent = decode(token);
        if (payloadContent == null) {
            return null;
        }
        PayloadData payloadData = new PayloadData();
        payloadData.setPayloadContent((JSONObject) payloadContent.get("payloadContent"));
        payloadData.setExp((Long) payloadContent.get("exp"));
        if (payloadData == null) {
            return null;
        }
        if (payloadData.getPayloadContent() == null || !payloadData.getPayloadContent().containsKey(TOKEN_USER_INFO)){
            return null;
        }
        if (payloadData.getExp() < System.currentTimeMillis()) {
            return null;
        }
        JSONObject userDetails = (JSONObject) payloadData.getPayloadContent().get(TOKEN_USER_INFO);
        return userDetails;
    }

    public Authentication getAuthentication(String token) {
        JSONObject userDetails = getPayloadObject(token);
        if (userDetails == null) {
            return null;
        }
        List<String> userDetailsRoles = (List<String>) userDetails.get("roles");
        List<String> roles = userDetailsRoles.stream().map(item -> removePreFix(item)).collect(Collectors.toList());
        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(String.join(",",roles.toArray(new String[]{})));
        Set<String> collect = authorities.stream().map(item -> item.getAuthority()).collect(Collectors.toSet());
        User user = new User((String) userDetails.get(TOKEN_USER_INFO_USERNAME), "", collect);
        return new UsernamePasswordAuthenticationToken(user,token,authorities);
    }
}
