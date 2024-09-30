package com.api.reservavuelos.Utils;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Random;


@Component
public class GenerateCodes {
    public int code(){
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return code;
    }
}
