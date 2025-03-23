package org.example.socialbe.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.socialbe.util.AES;
import org.example.socialbe.util.JsonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CipherService {
    @Value("${aes.key}")
    private String secretKey;

    @Value("${aes.iv}")
    private String secretIV;

    @Resource
    private JsonUtil jsonUtil;

    public String encrypt(Object obj) {
        String json = jsonUtil.toJson(obj);
        return AES.encrypt(json, secretKey, secretIV);
    }

    public <T> T decrypt(String token, Class<T> returnType) {
        String json = AES.decrypt(token, secretKey, secretIV);
        return jsonUtil.fromJson(json, returnType);
    }

    @PostConstruct
    public void postConstruct() {
        log.info("aes.key={}...", secretKey.substring(0, 5));
        log.info("aes.iv={}...", secretIV.substring(0, 5));
    }
}
