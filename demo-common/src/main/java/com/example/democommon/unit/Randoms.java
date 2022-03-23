package com.example.democommon.unit;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 五位数随机验证码类
 */
public class Randoms {
    //随机验证码
    public static String getcode() {
        java.util.Random rand = new java.util.Random();
        String result = "";
        for(int i = 0; i < 5; ++i) {
            int a = Math.abs(rand.nextInt() % 9);
            result = result + a;
        }
        return result;
    }
    public static String getRandom() {
        java.util.Random rand = new java.util.Random();
        String result = "";
        for(int i = 0; i < 4; ++i) {
            int a = Math.abs(rand.nextInt() % 9);
            result = result + a;
        }
        return result;
    }
    public static String GetOrderId(){
        String code =  Randoms.getRandom();
        String Date = new SimpleDateFormat("yyyyMMdd").format(new Date()).toString();
        Date +=code;
        return Date;
    }
}
