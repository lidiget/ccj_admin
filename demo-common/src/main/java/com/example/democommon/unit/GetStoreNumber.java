package com.example.democommon.unit;

import java.util.Calendar;
import java.util.Map;

public class GetStoreNumber {
    public static String StoreNumber(Map<String,String> map){
        // 获取年份
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        String years = Integer.toString(year);
        years = years.substring(2);
        // 获取月份
        int month = now.get(Calendar.MONTH)+1;
        String months = Integer.toString(month);
        months = "0"+months;
        String moon = map.get("order_info_storeid");
        // 判断这是第几月 如果是同月就继续
        if((moon.substring(7,9)).equals(months)){
            moon = moon.substring(9);
            // 第一位
            int one = Integer.parseInt(moon.substring(0,1));
            //

            int two = Integer.parseInt(moon.substring(1,2));
            int three = Integer.parseInt(moon.substring(2,3));
            if(two == 0){
                three = three+1;
                if(three==10){
                    String ones = moon.substring(0,1);
                    String e = ones +three;
                    return "-"+years+months+e;
                }else{
                    String ones = moon.substring(0,1);
                    String e = ones + two + three;
                    return "-"+years+months+e;
                }
            }else{
                String w = moon.substring(1,3);
                int d = Integer.parseInt(w);
                d = d+1;
                if(d==100){
                    d =one+1;
                    String e = d+"00";
                    return "-"+years+months+e;
                }else{
                    String h = String.valueOf(one);
                    String e = h+d;
                    return "-"+years+months+e;
                }
            }
        }else{
            return "-"+years+months+"001";
        }
    }
}
