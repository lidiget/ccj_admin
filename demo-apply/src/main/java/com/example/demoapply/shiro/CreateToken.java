package com.example.demoapply.shiro;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * 用于加密使用
 */

public class CreateToken {
    public static String token(String saltSource,String password){
    	System.out.println("獲取token");
        //md5加密
        String hashAlgorithmName = "MD5";
        //密码
        String credentials = password;
        //盐值
        Object salt = new Md5Hash(saltSource);
        int hashIterations = 1024;
        Object result = new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations);
        return result.toString();
    }
}
