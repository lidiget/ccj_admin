package com.example.demoapply.serviceimp;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demoapply.dao.CardDao;
import com.example.demoapply.dao.OrdersDao;
import com.example.demoapply.dao.ReportDao;
import com.example.demoapply.dao.TakesDao;
import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;
import com.example.demoapply.service.CardService;
import com.example.demoapply.service.OrdersService;
import com.example.demoapply.service.ReportService;
import com.example.demoapply.service.TakesService;

@Service
public class ReportServiceImp implements ReportService {

	@Autowired
	ReportDao reportDao;

	@Override
	public List<PageData> getAllSalesRankingAllData(PageData pd) {
		// TODO Auto-generated method stub
		return reportDao.getAllSalesRankingAllData(pd);
	}

	@Override
	public List<PageData> getAllBadRankingAllData(PageData pd) {
		// TODO Auto-generated method stub
		return reportDao.getAllBadRankingAllData(pd);
	}

	@Override
	public List<PageData> getAllValidOrderAllData(PageData pd) {
		// TODO Auto-generated method stub
		return reportDao.getAllValidOrderAllData(pd);
	}

	@Override
	public List<PageData> getAllWorkersPraiseListpage(PageData pd) {
		// TODO Auto-generated method stub
		return reportDao.getAllWorkersPraiseListpage(pd);
	}

	@Override
	public List<PageData> getAllSalesGoodsPriceData(PageData pd) {
		// TODO Auto-generated method stub
		return reportDao.getAllSalesGoodsPriceData(pd);
	}

	@Override
	public List<PageData> getAllSalesData(PageData pd) {
		// TODO Auto-generated method stub
		return reportDao.getAllSalesData(pd);
	}

	@Override
	public List<PageData> getAllSalesPerformanceAllData(PageData pd) {
		// TODO Auto-generated method stub
		return reportDao.getAllSalesPerformanceAllData(pd);
	}

	@Override
	public List<PageData> getAllTrackingCustomersAllData(PageData pd) {
		// TODO Auto-generated method stub
		return reportDao.getAllTrackingCustomersAllData(pd);
	}

	@Override
	public List<PageData> getAllOrderByuseridListpage(PageData pd) {
		// TODO Auto-generated method stub
		return reportDao.getAllOrderByuseridListpage(pd);
	}

	@Override
	public List<PageData> getAllOrderByGoodsidListpage(PageData pd) {
		// TODO Auto-generated method stub
		return reportDao.getAllOrderByGoodsidListpage(pd);
	}

	

}
