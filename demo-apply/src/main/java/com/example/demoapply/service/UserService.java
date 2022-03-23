package com.example.demoapply.service;

import java.util.List;
import java.util.Map;

import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;

public interface UserService {
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
	List<PageData> getAllAdminListPage(Page page);
	PageData getOneAdminDataByid(PageData pd);
	Integer updataAdmindataByid(PageData pd);
	int Updatepwdbyuserphone(PageData pd);
	int deleteOneAdmindataByid(PageData pd);
	PageData SelectOnebyAdminId(PageData pd);
	List<PageData> getAllDeptClass(PageData pd);
	int addDeptClassData(Map<?, ?> map);
	void updateDeptClassById(PageData pd);
	void deleteDeptClassById(PageData pd);
	List<PageData> getAllUserOperationRecordListpage(Page page);
	List<PageData> getUserDataByOrderid(PageData pd);
	List<PageData> getAllueridBySellzhuguanid(PageData pd);
	List<PageData> getAllUseridBysellid(PageData nn);
	void addgenzongxinxibyuserid(PageData pd);
	List<PageData> getAllTrackingDataByUseridListpage(Page page);
	List<PageData> selectAllbangdinguserData(PageData pd);
	void updataAllSelldataByuserid(PageData pd);
	List<PageData> selectuserdatabyphone(PageData pd);
	void addyuyuexinxi(PageData pd);
	List<PageData> getAllNewPictures(PageData pd);
	void updateAllNewPicturesByid(PageData pd);
	
}
