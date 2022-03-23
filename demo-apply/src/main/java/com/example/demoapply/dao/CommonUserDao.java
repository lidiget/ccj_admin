package com.example.demoapply.dao;

import org.apache.ibatis.annotations.Mapper;

import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;

import java.util.List;
import java.util.Map;

@Mapper
public interface CommonUserDao {

	List<PageData> getAllCommonUsersDataListpage(Page page);

	PageData getOneCommonUserDataByid(PageData pd);

	int updataCommonuserdataByid(PageData pd);

	int addCommonuserdata(PageData map);

	List<?> SelectPhone(Map<?, ?> map);

	PageData SelectUserData(PageData pd);

	int deleteOneCommonuserdataByid(PageData pd);

	int UpdatepwdbyCommonuserid(PageData pd);

	PageData getMemberSettingByid(PageData pd);

	int updateMemberSettingByid(PageData pd);

	void updatesellidbyuserid(PageData pd);

	
    
	

}
