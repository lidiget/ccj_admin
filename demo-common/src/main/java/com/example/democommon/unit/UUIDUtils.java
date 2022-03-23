package com.example.democommon.unit;

import java.util.UUID;

/**
 * 生成token
 */
public class UUIDUtils {

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }

}
