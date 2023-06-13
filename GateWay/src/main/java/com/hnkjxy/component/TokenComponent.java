package com.hnkjxy.component;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.hnkjxy.data.PayloadData;
import com.hnkjxy.entity.Menu;
import com.hnkjxy.entity.User;
import com.hnkjxy.utils.JsonUtils;
import com.hnkjxy.utils.JwtUtil;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.shaded.json.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.hnkjxy.constant.JwtConstant.*;
import static com.hnkjxy.constant.RedisConstant.USER_ROLE;

/**
 * @version: java version 17
 * @Author: Mr WzzZ
 * @description: Token组件
 * @date: 2023-04-30 12:40
 */
@Component
@RequiredArgsConstructor
public class TokenComponent {

    @Value("${wzzz.auth.secret}")
    private String secret;

    private final StringRedisTemplate redisTemplate;

    /**
     * 颁发Token
     * @Author Mr WzzZ
     * @Date 2023/5/29
     * @Param Token要存入的信息
     * @return java.lang.String
     */
    public Map<String, String> encode(JSONObject payloadObject) {
        return JwtUtil.encode(JWSAlgorithm.HS256,payloadObject, SecureUtil.md5(secret));
    }

    /**
     * Token解析
     * @Author Mr WzzZ
     * @Date 2023/5/29
     * @Param Token
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    public Map<String,Object> decode(String token) {
        return JwtUtil.decode(token);
    }

    /**
     * 获取Token中的信息
     * @Author Mr WzzZ
     * @Date 2023/5/29
     * @Param Token
     * @return com.nimbusds.jose.shaded.json.JSONObject
     */
    public JSONObject getPayloadObject(String token) {
        Map<String, Object> payloadContent = decode(token);
        if (payloadContent == null) {
            return null;
        }
        PayloadData payloadData = new PayloadData();
        payloadData.setPayloadContent((JSONObject)payloadContent.get("payloadContent"));
        payloadData.setJti((String) payloadContent.get("jti"));
        payloadData.setIat((Long) payloadContent.get("iat"));

        if (payloadData.getPayloadContent() == null || !payloadData.getPayloadContent().containsKey(TOKEN_USER_INFO_USERNAME)){
            return null;
        }

        String redisToken = redisTemplate.opsForValue().get(TOKEN+payloadData.getJti());
        if (StrUtil.isBlank(redisToken) || !redisToken.equals(token)) {
            return null;
        }
        return payloadData.getPayloadContent();
    }

    /**
     * 根据Token获取Authentication对象
     * @Author Mr WzzZ
     * @Date 2023/5/29
     * @Param Token
     * @return org.springframework.security.core.Authentication
     */
    public Authentication getAuthentication(String token) {
        JSONObject userDetails = getPayloadObject(token);
        if (userDetails == null) {
            return null;
        }
        String userName = userDetails.get(TOKEN_USER_INFO_USERNAME).toString();
        List<String> uris = JSONUtil.toList(userDetails.get(TOKEN_USER_INFO).toString(),String.class);
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(uris.toArray(String[]::new));
        List<Menu> menuList = JSONUtil.toList(redisTemplate.opsForValue().get(USER_ROLE + userName), Menu.class);
        User user = new User(userName, "", menuList);
        return new UsernamePasswordAuthenticationToken(user,token,authorities);
    }
}
