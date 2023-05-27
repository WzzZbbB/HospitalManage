package com.hnkjxy.data;

import com.nimbusds.jose.shaded.json.JSONObject;
import lombok.*;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description:
 * @date: 2023-04-30 12:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayloadData {
    public Long getIat() {
        return iat;
    }

    public void setIat(Long iat) {
        this.iat = iat;
    }

    public Long getExp() {
        return exp;
    }

    public void setExp(Long exp) {
        this.exp = exp;
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public JSONObject getPayloadContent() {
        return payloadContent;
    }

    public void setPayloadContent(JSONObject payloadContent) {
        this.payloadContent = payloadContent;
    }

    /**
     * 签发时间
     */
    private Long iat;
    /**
     * 过期时间
     */

    private Long exp;
    /**
     * JWT的ID
     */
    private String jti;
    /**
     * jwt载荷
     */
    private JSONObject payloadContent;
}
