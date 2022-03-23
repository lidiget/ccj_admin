package com.example.demoapply.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;

@Mapper
public interface WokerOrdersDao {

	List<PageData> getAllTaskFlowListpage(Page page);

	List<PageData> getWorkderOrdersAllDetailsDataById(Page page);

	void changeWorkerOrderStatusById(PageData pd);






}
