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
import com.example.demoapply.dao.TakesDao;
import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;
import com.example.demoapply.service.CardService;
import com.example.demoapply.service.OrdersService;
import com.example.demoapply.service.TakesService;

@Service
public class CardServiceImp implements CardService {

	@Autowired
	CardDao cardDao;
	@Autowired
	OrdersDao ordersDao;

	@Override
	public List<PageData> getAllCardListPage(Page page) {
		// TODO Auto-generated method stub
		return cardDao.getAllCardListPage(page);
	}

	@Override
	@Transactional
	public int addCarddata(PageData map) {
		// TODO Auto-generated method stub
		cardDao.addCarddata(map);
		PageData aa = new PageData();
		String myid = String.valueOf(map.get("myid"));
		String type_id = String.valueOf(map.get("coupon_id"));
		String bills_type = String.valueOf(map.get("bills_type"));
		String user_operation_record_detail = String.valueOf(map.get("user_operation_record_detail"));
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "新增");
		aa.put("user_operation_record_detail", user_operation_record_detail);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		return 1;
	}

	@Override
	@Transactional
	public void updataCarddataByid(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		String type_id = pd.getString("coupon_id");
		String bills_type = pd.getString("bills_type");
		String user_operation_record_detail = pd.getString("user_operation_record_detail");
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "编辑");
		aa.put("user_operation_record_detail", user_operation_record_detail);
		aa.put("user_operation_record_createtime", new Date());
		if (!user_operation_record_detail.equals("")) {
			ordersDao.addUserOperationRecorddata(aa);
		}
		cardDao.updataCarddataByid(pd);
	}

	@Override
	public void deleteOneCarddataByid(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		String type_id = pd.getString("shop_goods_worktype_id");
		String bills_type = pd.getString("bills_type");
		String coupon_type = pd.getString("coupon_type");
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "删除");
		aa.put("user_operation_record_detail", "删除卡券：" + coupon_type);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		cardDao.deleteOneCarddataByid(pd);
	}

	@Override
	public List<PageData> getAllCardDistributionListPage(Page page) {
		// TODO Auto-generated method stub
		return cardDao.getAllCardDistributionListPage(page);
	}

	@Override
	@Transactional
	public void addCardFenfaData(PageData map) {
		// TODO Auto-generated method stub
		String list = map.getString("user_id");
		String[] aa = list.split(",");
		for (int i = 0; i < aa.length; i++) {
			String id = aa[i];
			System.out.println("222222222222222222222222");
			System.out.println(id);
			PageData onedata = new PageData();
			onedata.put("user_id", Integer.valueOf(id));
			onedata.put("coupon_id", Integer.valueOf(map.getString("coupon_id")));
			onedata.put("user_coupon_creatime", new Date());
			onedata.put("user_coupon_status", '0');
			cardDao.addCardFenfaData(onedata);
			PageData mm = new PageData();
			String type_id = String.valueOf(onedata.get("user_coupon_id"));
			String bills_type = map.getString("bills_type");
			String myid = map.getString("myid");
			mm.put("bills_type", bills_type);
			mm.put("type_id", type_id);
			mm.put("user_id", Integer.valueOf(myid));
			String allusername = String.valueOf(map.get("user_id"));
			mm.put("user_operation_record_type", "新增");
			mm.put("user_operation_record_detail", "新增卡券分发信息,分发用户编号"+allusername);
			mm.put("user_operation_record_createtime", new Date());
			ordersDao.addUserOperationRecorddata(mm);
		}
	}

	@Override
	public List<PageData> getAllUserCardList(PageData pd) {
		// TODO Auto-generated method stub
		return cardDao.getAllUserCardList(pd);
	}

	@Override
	@Transactional
	public void updataCardstatusdataByid(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		String coupon_type = pd.getString("coupon_type");
		String changetype = pd.getString("changetype");
		String type_id = pd.getString("coupon_id");
		String bills_type = pd.getString("bills_type");
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		aa.put("user_operation_record_type", "编辑");
		aa.put("user_operation_record_detail", "修改卡券状态：" + coupon_type+changetype);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		cardDao.updataCardstatusdataByid(pd);
	}

	@Override
	public void updataAllzhuguanid(PageData pd) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		String list = pd.getString("allid");
		String[] aa = list.split(",");
		for (int i = 0; i < aa.length; i++) {
			String id = aa[i];
			PageData onedata = new PageData();
			onedata.put("needid", Integer.valueOf(id));
			onedata.put("zhuguanid", Integer.valueOf(pd.getString("zhuguanid")));
			cardDao.updataAllzhuguanid(onedata);
//			cardDao.updataUserzhuguanid(onedata);
		}
		
	}

	@Override
	public PageData getUseridBySellid(PageData pd) {
		// TODO Auto-generated method stub
		return cardDao.getUseridBySellid(pd);
	}


}
