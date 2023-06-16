package com.hnkjxy.data;

/**
 * @version: java version 17
 * @Author: Mr WzzZ
 * @description: 图片验证码实体
 * @date: 2023-05-02 21:43
 */
public class VerifyImgCode {
    private String imageBase64;
    private String cutImageBase64;
    private Integer x;
    private Integer y;
    private String uuid;

    public VerifyImgCode() {
    }

    public VerifyImgCode(String imageBase64, String cutImageBase64, Integer x, Integer y, String uuid) {
        this.imageBase64 = imageBase64;
        this.cutImageBase64 = cutImageBase64;
        this.x = x;
        this.y = y;
        this.uuid = uuid;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String getCutImageBase64() {
        return cutImageBase64;
    }

    public void setCutImageBase64(String cutImageBase64) {
        this.cutImageBase64 = cutImageBase64;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
