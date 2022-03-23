package com.example.demoapply.dao;

import org.apache.ibatis.annotations.Mapper;

import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;

import java.util.List;
import java.util.Map;

@Mapper
public interface PublicDao {
	List<PageData> selectAll(PageData pd);

	PageData selectOne(PageData pd);

	int update(PageData map);

	List<PageData> selectAllSelllistPage(Page map);

	int addSell(PageData map);

	int changeSell(PageData map);

	int addSlideShow(PageData map);

	void deleteSlideShow(PageData pd);

	void changeRoleBySellid(PageData pd);

	void changeallzhuguanidBySellid(PageData pd);
}
