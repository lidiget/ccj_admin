package com.example.democommon.unit;

import java.util.List;
import java.util.Map;

public class TokenList {
    // 分割区域功能
    public static List<?> Token(String token, List<Map<?,?>> list){
        for(int i = 0; i < list.size(); i++){
            Map<String,String> map3= (Map<String, String>) list.get(i);
            map3.put("user_token",token);
        }
        String b = (String) list.get(0).get("areas_name");
        String[] strArr= b.split("/");//根据分隔符拆分字符串\
        for (int i = 0; i < list.size(); i++) {
            Map<String, String[]> map2 = (Map<String, String[]>) list.get(i);
            map2.put("areas_name",strArr);
        }
        return list;
    }
}
