package com.example.demoapply.serviceimp;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demoapply.dao.CardDao;
import com.example.demoapply.dao.OrdersDao;
import com.example.demoapply.dao.PublicDao;
import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;
import com.example.demoapply.service.PublicService;

@Service
public class PublicServiceImp implements PublicService {

	@Autowired
	PublicDao publicDao;
	@Autowired
	OrdersDao ordersDao;
	@Autowired
	CardDao cardDao;

	@Override
	public List<PageData> selectAll(PageData pd) {
		// TODO Auto-generated method stub
		return publicDao.selectAll(pd);
	}

	@Override
	public int update(PageData map) {
		// TODO Auto-generated method stub
		return publicDao.update(map);
	}

	@Override
	public PageData selectOne(PageData pd) {
		// TODO Auto-generated method stub
		return publicDao.selectOne(pd);
	}

	@Override
	public List<PageData> selectAllSelllistPage(Page map) {
		// TODO Auto-generated method stub
		return publicDao.selectAllSelllistPage(map);
	}

	@Override
	@Transactional
	public int addSell(PageData map) {
		// TODO Auto-generated method stub
		publicDao.addSell(map);
		PageData aa = new PageData();
		String myid = String.valueOf(map.get("myid"));
		aa.put("user_id", Integer.valueOf(myid));
		String type_id = String.valueOf(map.get("id"));
		String bills_type = String.valueOf(map.get("bills_type"));
		String user_operation_record_detail = String.valueOf(map.get("user_operation_record_detail"));
		aa.put("user_operation_record_type", "新增");
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		aa.put("user_operation_record_detail", user_operation_record_detail);
		aa.put("user_operation_record_createtime", new Date());
		if(!user_operation_record_detail.equals("")) {
			ordersDao.addUserOperationRecorddata(aa);
		}
		return 1;
	}

	@Override
	@Transactional
	public int changeSell(PageData map) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = String.valueOf(map.get("myid"));
		aa.put("user_id", Integer.valueOf(myid));
		String type_id = String.valueOf(map.get("id"));
		String bills_type = String.valueOf(map.get("bills_type"));
		String user_operation_record_detail = String.valueOf(map.get("user_operation_record_detail"));
		aa.put("user_operation_record_type", "编辑");
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		aa.put("user_operation_record_detail", user_operation_record_detail);
		aa.put("user_operation_record_createtime", new Date());
		if (!user_operation_record_detail.equals("")) {
			ordersDao.addUserOperationRecorddata(aa);
		}
		return publicDao.changeSell(map);
	}

	@Override
	@Transactional
	public int addSlideShow(PageData map) {
		// TODO Auto-generated method stub
		return publicDao.addSlideShow(map);
	}

	@Override
	@Transactional
	public void deleteSlideShow(PageData pd) {
		// TODO Auto-generated method stub
		publicDao.deleteSlideShow(pd);
	}

	@Override
	@Transactional
	public void changeRoleBySellid(PageData pd) {
		// TODO Auto-generated method stub
		publicDao.changeallzhuguanidBySellid(pd);
		publicDao.changeRoleBySellid(pd);
		if(!pd.getString("user_id").equals("")) {
			pd.put("sell_leve", Integer.valueOf(pd.getString("sell_leve")));
			cardDao.updataUserzhuguanid(pd);
		}
	}

}
