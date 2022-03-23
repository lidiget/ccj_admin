package com.example.demoapply.service;

import java.util.List;

import com.example.demoapply.entity.PageData;

public interface ReportService {

	List<PageData> getAllSalesRankingAllData(PageData pd);

	List<PageData> getAllBadRankingAllData(PageData pd);

	List<PageData> getAllValidOrderAllData(PageData pd);

	List<PageData> getAllWorkersPraiseListpage(PageData pd);

	List<PageData> getAllSalesGoodsPriceData(PageData pd);

	List<PageData> getAllSalesData(PageData pd);

	List<PageData> getAllSalesPerformanceAllData(PageData pd);

	List<PageData> getAllTrackingCustomersAllData(PageData pd);

	List<PageData> getAllOrderByuseridListpage(PageData pd);

	List<PageData> getAllOrderByGoodsidListpage(PageData pd);


	
}
