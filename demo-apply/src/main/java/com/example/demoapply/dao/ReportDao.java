package com.example.demoapply.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;

@Mapper
public interface ReportDao {

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
