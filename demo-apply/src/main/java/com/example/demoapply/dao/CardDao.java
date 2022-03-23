package com.example.demoapply.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;

@Mapper
public interface CardDao {

	List<PageData> getAllCardListPage(Page page);

	int addCarddata(PageData map);

	void updataCarddataByid(PageData pd);

	void deleteOneCarddataByid(PageData pd);

	List<PageData> getAllCardDistributionListPage(Page page);

	void addCardFenfaData(PageData onedata);

	List<PageData> getAllUserCardList(PageData pd);

	void updataCardstatusdataByid(PageData pd);

	void updataAllzhuguanid(PageData onedata);

	void updataUserzhuguanid(PageData onedata);

	PageData getUseridBySellid(PageData pd);







}
