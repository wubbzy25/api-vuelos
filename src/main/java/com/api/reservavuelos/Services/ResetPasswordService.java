package com.api.reservavuelos.Services;

import com.api.reservavuelos.Utils.GenerateCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ResetPasswordService {

    private final GenerateCodes generateCodes;
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public ResetPasswordService(GenerateCodes generateCodes, RedisTemplate<String, Object> redisTemplate){
        this.generateCodes = generateCodes;
        this.redisTemplate = redisTemplate;
    }

    public String SetResetCode(String email){
        String code = String.valueOf(generateCodes.code());
        redisTemplate.opsForValue().set(email, code, 15, TimeUnit.MINUTES);
        return code;
    }

    public String getData(String email) {
        return (String) redisTemplate.opsForValue().get(email);
    }

    public void deleteData(String email){
        redisTemplate.delete(email);
    }

    public void setVerifyStatus(String email){
        redisTemplate.opsForValue().set(email, "verified", 5, TimeUnit.MINUTES);
    }
}
