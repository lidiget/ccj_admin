package com.example.demoapply.dao;

import org.apache.ibatis.annotations.Mapper;

import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserDao {
    //查询品牌信息
    List<?> CaiLogin(Map<?, ?> map);
    //查询手机号是否注册
    List<?> SelectPhone(Map<?,?> map);
    //注册
    int InsertUser(Map<?,?> map);
    //用户权限中间表
    int InsertUrole(Map<?,?> map);
    //忘记密码
    int Updatepwd(Map<?,?> map);
    //用户注册钱包
    int InsertWallet(Map<?,?> map);
	Integer UpdateIdentity(PageData pd);
	//获取用户的信息列表
	List<PageData> getAllAdminList(Page pd);
	//通过id获取单个用户的信息
	PageData getOneAdminDataByid(PageData pd);
	//根据id更新用户信息
	Integer updataAdmindataByid(PageData pd);
	int Updatepwdbyuserphone(PageData pd);
	int deleteOneAdmindataByid(PageData pd);
	PageData SelectOnebyAdminId(PageData pd);
	List<PageData> getAllDeptClass(PageData pd);
	int addDeptClassData(Map<?, ?> map);
	void updateDeptClassById(PageData pd);
	void deleteDeptClassById(PageData pd);
	List<PageData> getAllUserOperationRecordListpage(Page page);
	void addSelluserdata(PageData bb);
	void deleteOneSellUserByid(PageData pd);
	void updateSelluserdata(PageData ee);
	PageData getAllSelling(PageData pd);
	List<PageData> getUserDataByOrderid(PageData pd);
	List<PageData> getAllueridBySellzhuguanid(PageData pd);
	List<PageData> getAllUseridBysellid(PageData nn);
	void addgenzongxinxibyuserid(PageData pd);
	List<PageData> getAllTrackingDataByUseridListpage(Page page);
	PageData getTheSellidByUserid(PageData pd);
	void updataAllSelldataByuserid(PageData pd);
	List<PageData> selectuserdatabyphone(PageData pd);
	void addyuyuexinxi(PageData pd);
	List<PageData> getAllNewPictures(PageData pd);
	void updateAllNewPicturesByid(PageData pd);
	

}
