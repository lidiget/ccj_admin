package com.example.democommon.unit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class GaoDeUtil {
    //	根据地质生成腾讯地图的经纬度坐标
	public static JSONObject getLatAndLng(String address) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("address", address.trim());
		params.put("key", "a238cd4a5c630c6c494b638905cf3151");
		try {
			String result = HttpClientHelper.doGet("https://restapi.amap.com/v3/geocode/geo", params);
			System.out.println(result);
			JSONObject jsonObject = JSONObject.parseObject(result);
			if (jsonObject.getInteger("status") ==1) {
//				try {
//					if(jsonObject.getJSONArray("geocodes").size()==0) {
//					
//					}
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
//				
				return jsonObject.getJSONArray("geocodes").getJSONObject(0);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();

		}
		return null;

	}
	
	public static JSONObject getDistanceByAddress(String address1,String address2) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("origins", "116.481028,39.989643");
		params.put("destination", "114.465302,40.004717");
		params.put("type", "0");		
		params.put("key", "a238cd4a5c630c6c494b638905cf3151");
		try {
			String result = HttpClientHelper.doGet("https://restapi.amap.com/v3/distance", params);
			JSONObject jsonObject = JSONObject.parseObject(result);
			System.out.println(jsonObject);
			if (jsonObject.getInteger("status") == 1) {
				return jsonObject.getJSONArray("results").getJSONObject(0);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
}
