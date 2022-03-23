package com.example.demoapply.dao;

import org.apache.ibatis.annotations.Mapper;

import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;

import java.util.List;
import java.util.Map;

@Mapper
public interface RoleDao {

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
