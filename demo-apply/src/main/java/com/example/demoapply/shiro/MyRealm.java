package com.example.demoapply.shiro;

import com.example.demoapply.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义的认证空间     只做判断密码,不做页面验证
 */

@Component
public class MyRealm extends AuthorizingRealm {
    @Autowired
    UserService hjService;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection){
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        String principal = (String)principalCollection.getPrimaryPrincipal();
        LinkedHashMap map = new LinkedHashMap();
        map.put("user_phone", principal);
        List lblogin = hjService.CaiLogin(map);
        Map m = (Map)lblogin.get(0);
//        if(m.get("user_status").equals(1)){
//            info.addRole("yh");
//        }
//        info.addRole("yh");
        return info;
    }
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)throws AuthenticationException{
        LinkedHashMap<String, Object> map = new LinkedHashMap();
        map.put("user_phone", token.getPrincipal());
        List login = hjService.CaiLogin(map);
        if (login == null) {
            return null;
        }
        Map m = (Map)login.get(0);
        String source = m.get("user_saltvalue").toString();
        String pwd = m.get("user_encryption").toString();
        String relname = getName();
        Md5Hash bs = new Md5Hash(source);
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(token.getPrincipal(), pwd, bs, relname);
        HashMap map1 = new HashMap();
        map1.put("user_phone", token.getPrincipal());
        if (hjService.CaiLogin(map1).size() == 1) {
            return info;
        }
        return null;
    }
    @PostConstruct
    public void setCredentialsMatcher(){
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("MD5");
        matcher.setHashIterations(1024);
        this.setCredentialsMatcher(matcher);
    }
}
