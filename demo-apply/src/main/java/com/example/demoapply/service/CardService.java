package com.example.demoapply.service;

import java.util.List;

import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;

public interface CardService {

	List<PageData> getAllCardListPage(Page page);

	int addCarddata(PageData map);

	void updataCarddataByid(PageData pd);

	void deleteOneCarddataByid(PageData pd);

	List<PageData> getAllCardDistributionListPage(Page page);

	void addCardFenfaData(PageData map);

	List<PageData> getAllUserCardList(PageData pd);

	void updataCardstatusdataByid(PageData pd);

	void updataAllzhuguanid(PageData pd);


	PageData getUseridBySellid(PageData pd);

	



	

}
