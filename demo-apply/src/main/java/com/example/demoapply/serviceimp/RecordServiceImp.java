package com.example.demoapply.serviceimp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demoapply.dao.OrdersDao;
import com.example.demoapply.dao.RecordDao;
import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;
import com.example.demoapply.service.OrdersService;
import com.example.demoapply.service.RecordService;

@Service
public class RecordServiceImp implements RecordService {

	@Autowired
	RecordDao recordDao;

	@Override
	public List<PageData> getRnvoiceRecordListpage(Page page) {
		// TODO Auto-generated method stub
		return recordDao.getRnvoiceRecordListpage(page);
	}

	@Override
	public List<PageData> getWalletRecordListpage(Page page) {
		// TODO Auto-generated method stub
		return recordDao.getWalletRecordListpage(page);
	}

	@Override
	public List<PageData> getAllBrowseDataListpage(Page page) {
		// TODO Auto-generated method stub
		return recordDao.getAllBrowseDataListpage(page);
	}



	

}
