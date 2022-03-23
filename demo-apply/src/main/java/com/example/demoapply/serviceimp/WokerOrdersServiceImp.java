package com.example.demoapply.serviceimp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demoapply.dao.OrdersDao;
import com.example.demoapply.dao.WokerOrdersDao;
import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;
import com.example.demoapply.service.OrdersService;
import com.example.demoapply.service.WorkerOrdersService;

@Service
public class WokerOrdersServiceImp implements WorkerOrdersService {

	@Autowired
	WokerOrdersDao wokerOrdersDao;

	@Override
	public List<PageData> getAllTaskFlowListpage(Page page) {
		// TODO Auto-generated method stub
		return wokerOrdersDao.getAllTaskFlowListpage(page);
	}

	@Override
	public List<PageData> getWorkderOrdersAllDetailsDataById(Page page) {
		// TODO Auto-generated method stub
		return wokerOrdersDao.getWorkderOrdersAllDetailsDataById(page);
	}

	@Override
	@Transactional
	public void changeWorkerOrderStatusById(PageData pd) {
		// TODO Auto-generated method stub
		wokerOrdersDao.changeWorkerOrderStatusById(pd);
	}

	

}
