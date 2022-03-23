package com.example.demoapply.service;

import java.util.List;
import java.util.Map;

import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;

public interface RoleService {
	//查询全部权限信息
	List<PageData> getAllRoleListPage(Page page);

	PageData getOneRoleDataByid(PageData pd);

	int updataRoledataByid(PageData pd);

	int InsertRole(PageData map);

	int deleteOneRoledataByid(PageData pd);

	List<PageData> getAllRoleData(PageData pd);

	int udataRoleRights(Map<?, ?> map);

	int udataRoleGongNengRights(Map<?, ?> map);

	int updataRoledataByid1(PageData pd);

	List<PageData> getAllRoleListById(PageData aa);
  
   
	
}
