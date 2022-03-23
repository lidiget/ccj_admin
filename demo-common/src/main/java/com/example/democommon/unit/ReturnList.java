package com.example.democommon.unit;

import com.alibaba.fastjson.JSONObject;
import com.example.democommon.unitedreturn.HjControllerHelper;

import java.util.List;
import java.util.Map;

public class ReturnList {
    //
    public static Map qwe(List<Map<?,?>> list,HjControllerHelper hjControllerHelper){
        if(list.isEmpty()){
            return hjControllerHelper.result(list,true,"查询成功",200);
        }else{
            for(int q =0;q<list.size();q++){
                if(list.get(q).get("report_json")=="{}"){
                    Map<String,String> map3= (Map<String,String>) list.get(q);
                    map3.put("report_json","null");
                }else if(list.get(q).get("report_json")==null){
                    Map<String,String> map3= (Map<String, String>) list.get(q);
                    map3.put("report_json","null");
                }else if(list.get(q).get("report_json").equals(" ")){
                    Map<String,String> map3= (Map<String, String>) list.get(q);
                    map3.put("report_json","null");
                }else{
                    JSONObject jsonObject = JSONObject.parseObject(String.valueOf(list.get(q).get("report_json")));
                    Map<String,JSONObject> map3= (Map<String,JSONObject>)list.get(q);
                    map3.put("report_json",jsonObject);
                }
            }
            return hjControllerHelper.result(list,true,"查询成功",200);
        }
    }
    public static Map abc(List<Map<?,?>> list,HjControllerHelper hjControllerHelper,String a,String abc){
            for(int q =0;q<list.size();q++){
                Map<String,String> map3= (Map<String, String>) list.get(q);
                map3.put("type",a);
            }
            return hjControllerHelper.result(list,true,abc,200);
    }
    public static Map StoreInformation(List<?> list,HjControllerHelper hjControllerHelper){
        for(int i =0;i<list.size();i++){
            Map<String,?> map1= (Map<String, String>) list.get(i);
            if(map1.get("order_info_storeid").equals("暂无")){
                map1.put("order_info_storeid",null);
            }
            if(map1.get("order_info_shopid").equals("暂无")){
                map1.put("order_info_shopid",null);
            }
        }
        return hjControllerHelper.result(list,true,"查询成功",200);
    }
}
