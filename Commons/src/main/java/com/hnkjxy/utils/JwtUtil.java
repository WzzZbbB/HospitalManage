package com.hnkjxy.utils;

import cn.hutool.json.JSONUtil;
import com.hnkjxy.data.PayloadData;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.shaded.json.JSONObject;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * @version: java version 1.8
 * @Author: Mr WzzZ
 * @description:
 * @date: 2023-04-29 22:24
 */
public final class JwtUtil {
    public static String encode(JWSAlgorithm algorithm, JSONObject payLoadObject, String secret,Integer expire) {
        //1. 头部
        JWSHeader jwsHeader =
                        //加密算法
                new JWSHeader.Builder(algorithm)
                        //静态常量
                        .type(JOSEObjectType.JWT)
                        .build();

        //2.载荷
        //可以传String，Json，或Map
        PayloadData payloadData = getPayloadData(payLoadObject, expire);
        Payload payload = new Payload(JsonUtils.deserializer(payloadData));


        try {
            //3.签名器
            MACSigner jwsSigner = new MACSigner(secret);
            // 4.JWSObject 是有状态的：未签名、已签名和签名 在执行sign方法之后，JWSObject 对象就变成了已签名状态。
            JWSObject jwsObject = new JWSObject(jwsHeader, payload);
            //进行签名
            jwsObject.sign(jwsSigner);
            return jwsObject.serialize();
        } catch (JOSEException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 解密
     * @author Mr WzzZ
     * @date 2023/4/29
     * @param encode
     * @return 解密后的对象
     */
    public static Map<String,Object> decode(String encode) {
        try {
            JWSObject parse = JWSObject.parse(encode);
            Payload payload = parse.getPayload();
            if (payload != null) {
                return payload.toJSONObject();
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private static PayloadData getPayloadData(JSONObject payloadContent,Integer expire) {
        Date now = new Date();
        Date exp = DateUtil.addDate(now, Calendar.SECOND,expire);
        PayloadData payloadData = new PayloadData();
        payloadData.setIat(now.getTime());
        payloadData.setExp(exp.getTime());
        payloadData.setPayloadContent(payloadContent);
        payloadData.setJti(UUID.randomUUID().toString());
        return payloadData;
    }
}
