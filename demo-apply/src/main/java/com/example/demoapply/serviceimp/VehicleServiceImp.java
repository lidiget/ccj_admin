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
import com.example.demoapply.dao.VehicleDao;
import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;
import com.example.demoapply.service.CardService;
import com.example.demoapply.service.OrdersService;
import com.example.demoapply.service.TakesService;
import com.example.demoapply.service.VehicleService;

@Service
public class VehicleServiceImp implements VehicleService {

	@Autowired
	VehicleDao vehicleDao;
	@Autowired
	OrdersDao ordersDao;

	@Override
	public List<PageData> getAllCarParametersDataListpage(Page page) {
		// TODO Auto-generated method stub
		return vehicleDao.getAllCarParametersDataListpage(page);
	}

	@Override
	@Transactional
	public int addVechiledata(PageData map) {
		// TODO Auto-generated method stub
		vehicleDao.addVechiledata(map);
		PageData aa = new PageData();
		String myid = String.valueOf(map.get("myid"));
		String type_id = String.valueOf(map.get("vehicle_classify_id"));
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
	public void updataVechiledataByid(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		String type_id = pd.getString("vehicle_classify_id");
		String bills_type = pd.getString("bills_type");
		String user_operation_record_detail = pd.getString("user_operation_record_detail");
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "编辑");
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		aa.put("user_operation_record_detail", user_operation_record_detail);
		aa.put("user_operation_record_createtime", new Date());
		if (!user_operation_record_detail.equals("")) {
			ordersDao.addUserOperationRecorddata(aa);
		}
		vehicleDao.updataVechiledataByid(pd);
	}

	@Override
	@Transactional
	public void deleteonevehicledataByid(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		String type_id = pd.getString("id");
		String bills_type = pd.getString("bills_type");
		String vehicle_classify_name = pd.getString("vehicle_classify_name");
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "删除");
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		aa.put("user_operation_record_detail", "删除车辆类型：" + vehicle_classify_name);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		vehicleDao.deleteonevehicledataByid(pd);
	}

	@Override
	public List<PageData> getAllVehicleOrdersListpage(Page page) {
		// TODO Auto-generated method stub
		return vehicleDao.getAllVehicleOrdersListpage(page);
	}

}
