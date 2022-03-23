package com.example.demoapply.service;

import java.util.List;

import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;

public interface PublicService {

	List<PageData> selectAll(PageData pd);

	PageData selectOne(PageData pd);
	int update(PageData map);

	List<PageData> selectAllSelllistPage(Page map);

	int addSell(PageData map);

	int changeSell(PageData map);

	int addSlideShow(PageData map);

	void deleteSlideShow(PageData pd);

	void changeRoleBySellid(PageData pd);

}
