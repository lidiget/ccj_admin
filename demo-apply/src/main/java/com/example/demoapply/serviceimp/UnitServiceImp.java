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
import com.example.demoapply.dao.UnitDao;
import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;
import com.example.demoapply.service.CardService;
import com.example.demoapply.service.OrdersService;
import com.example.demoapply.service.ReportService;
import com.example.demoapply.service.TakesService;
import com.example.demoapply.service.UnitService;

@Service
public class UnitServiceImp implements UnitService {

	@Autowired
	UnitDao unitDao;
	@Autowired
	OrdersDao ordersDao;

	@Override
	public List<PageData> getAllUnitsDataListpage(Page page) {
		// TODO Auto-generated method stub
		return unitDao.getAllUnitsDataListpage(page);
	}

	@Override
	public List<PageData> getAllSizeUnitDataListPage(Page page) {
		// TODO Auto-generated method stub
		return unitDao.getAllSizeUnitDataListPage(page);
	}

	@Override
	@Transactional
	public void addunitdata(PageData map) {
		// TODO Auto-generated method stub

		unitDao.addunitdata(map);
		PageData aa = new PageData();
		String myid = String.valueOf(map.get("myid"));
		String type_id = String.valueOf(map.get("shop_goods_unit_id"));
		String bills_type = String.valueOf(map.get("bills_type"));
		String shop_goods_unit_name = String.valueOf(map.get("shop_goods_unit_name"));
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "新增");
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		aa.put("user_operation_record_detail", "新增材料单位:" + shop_goods_unit_name);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
	}

	@Override
	@Transactional
	public void updateunitdatabyid(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		String type_id = pd.getString("shop_goods_unit_id");
		String bills_type = pd.getString("bills_type");
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		String shop_goods_unit_name = pd.getString("shop_goods_unit_name");
		String oldname = pd.getString("oldname");
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "编辑");
		aa.put("user_operation_record_detail", "修改前:" + oldname+",修改后：" + shop_goods_unit_name);
		aa.put("user_operation_record_createtime", new Date());
		if(!pd.getString("oldname").equals(pd.getString("shop_goods_unit_name"))) {
			ordersDao.addUserOperationRecorddata(aa);
		}
		unitDao.updateunitdatabyid(pd);
	}

	@Override
	@Transactional
	public void deleteunitdatabyid(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		String type_id = pd.getString("id");
		String bills_type = pd.getString("bills_type");
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		String shop_goods_unit_name = pd.getString("shop_goods_unit_name");
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "删除");
		aa.put("user_operation_record_detail", "删除材料单位：" + shop_goods_unit_name);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		unitDao.deleteunitdatabyid(pd);
	}

	@Override
	@Transactional
	public void addsizeunitdata(PageData map) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		unitDao.addsizeunitdata(map);
		String myid = String.valueOf(map.get("myid"));
		String type_id = String.valueOf(map.get("shop_specification_unit_id"));
		String bills_type = String.valueOf(map.get("bills_type"));
		String shop_specification_unit_name = String.valueOf(map.get("shop_specification_unit_name"));
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "新增");
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		aa.put("user_operation_record_detail", "新增材料规格单位:" + shop_specification_unit_name);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
	}

	@Override
	@Transactional
	public void updatesizeunitdatabyid(PageData pd) {
		// TODO Auto-generated method stub

		PageData aa = new PageData();
		String myid = pd.getString("myid");
		String type_id = pd.getString("shop_specification_unit_id");
		String bills_type = pd.getString("bills_type");
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		String shop_specification_unit_name = pd.getString("shop_specification_unit_name");
		String oldname = pd.getString("oldname");
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "编辑");
		aa.put("user_operation_record_detail", "修改前:" + oldname+",修改后：" + shop_specification_unit_name);
		aa.put("user_operation_record_createtime", new Date());
		if(!pd.getString("oldname").equals(pd.getString("shop_specification_unit_name"))) {
			ordersDao.addUserOperationRecorddata(aa);
		}
		unitDao.updatesizeunitdatabyid(pd);
	}

	@Override
	@Transactional
	public void deletesizeunitdatabyid(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		aa.put("user_id", Integer.valueOf(myid));
		String type_id = pd.getString("shop_specification_unit_id");
		String bills_type = pd.getString("bills_type");
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		String shop_specification_unit_name = pd.getString("shop_specification_unit_name");
		aa.put("user_operation_record_type", "删除");
		aa.put("user_operation_record_detail", "删除材料规格单位" + shop_specification_unit_name);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		unitDao.deletesizeunitdatabyid(pd);
	}

	@Override
	public List<PageData> getAllColorDataListpage(Page page) {
		// TODO Auto-generated method stub
		return unitDao.getAllColorDataListpage(page);
	}

	@Override
	@Transactional
	public void updatecolordatabyid(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		String type_id = pd.getString("shop_goods_color_id");
		String bills_type = pd.getString("bills_type");
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		aa.put("user_id", Integer.valueOf(myid));
		String shop_goods_color_name = pd.getString("shop_goods_color_name");
		String oldname = pd.getString("oldname");
		aa.put("user_operation_record_type", "编辑");
		aa.put("user_operation_record_detail", "修改前:" + oldname+",修改后：" + shop_goods_color_name);
		aa.put("user_operation_record_createtime", new Date());
		if(!pd.getString("oldname").equals(pd.getString("shop_goods_color_name"))) {
			ordersDao.addUserOperationRecorddata(aa);
		}
		unitDao.updatecolordatabyid(pd);
	}

	@Override
	@Transactional
	public void addcolordata(PageData map) {
		// TODO Auto-generated method stub
		unitDao.addcolordata(map);
		PageData aa = new PageData();
		String myid = String.valueOf(map.get("myid"));
		String type_id = String.valueOf(map.get("shop_goods_color_id"));
		String bills_type = String.valueOf(map.get("bills_type"));
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "新增");
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		aa.put("user_id", Integer.valueOf(myid));
		String shop_goods_color_name = String.valueOf(map.get("shop_goods_color_name"));
		aa.put("user_operation_record_type", "新增");
		aa.put("user_operation_record_detail", "新增材料颜色:" + shop_goods_color_name);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);

	}

	@Override
	@Transactional
	public void deletecolordatabyid(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		aa.put("user_id", Integer.valueOf(myid));
		String type_id = pd.getString("shop_specification_unit_id");
		String bills_type = pd.getString("bills_type");
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		String shop_goods_color_name = pd.getString("shop_goods_color_name");
		aa.put("user_operation_record_type", "删除");
		aa.put("user_operation_record_detail", "删除材料颜色：" + shop_goods_color_name);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		unitDao.deletecolordatabyid(pd);
	}

	@Override
	public List<PageData> getAllWorkTypeDataListpage(Page page) {
		// TODO Auto-generated method stub
		return unitDao.getAllWorkTypeDataListpage(page);
	}

	@Override
	@Transactional
	public void addworktypedata(PageData map) {
		// TODO Auto-generated method stub
		unitDao.addworktypedata(map);
		PageData aa = new PageData();
		String myid = String.valueOf(map.get("myid"));
		String type_id = String.valueOf(map.get("shop_goods_worktype_id"));
		String bills_type = String.valueOf(map.get("bills_type"));
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		String shop_goods_worktype_name = String.valueOf(map.get("shop_goods_worktype_name"));
		aa.put("user_operation_record_type", "新增");
		aa.put("user_operation_record_detail", "新增材料工种:" + shop_goods_worktype_name);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);

	}

	@Override
	@Transactional
	public void updateworktypedatabyid(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		String type_id = pd.getString("shop_goods_worktype_id");
		String bills_type = pd.getString("bills_type");
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "编辑");
		String shop_goods_worktype_name = pd.getString("shop_goods_worktype_name");
		String oldname = pd.getString("oldname");
		aa.put("user_operation_record_detail", "修改前:" + oldname+",修改后:" + shop_goods_worktype_name);
		aa.put("user_operation_record_createtime", new Date());
		if(!pd.getString("oldname").equals(pd.getString("shop_goods_worktype_name"))) {
			ordersDao.addUserOperationRecorddata(aa);
		}
		unitDao.updateworktypedatabyid(pd);
	}

	@Override
	@Transactional
	public void deleteworktypedatabyid(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		String type_id = pd.getString("shop_goods_worktype_id");
		String bills_type = pd.getString("bills_type");
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "删除");
		String shop_goods_worktype_name = pd.getString("shop_goods_worktype_name");
		aa.put("user_operation_record_detail", "删除材料工种：" + shop_goods_worktype_name);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		unitDao.deleteworktypedatabyid(pd);
	}

	@Override
	public List<PageData> getAllLogisticssDataListpage(Page page) {
		// TODO Auto-generated method stub
		return unitDao.getAllLogisticssDataListpage(page);
	}

	@Override
	@Transactional
	public void addlogisticsdata(PageData map) {
		// TODO Auto-generated method stub
		unitDao.addlogisticsdata(map);
		PageData aa = new PageData();
		String myid = String.valueOf(map.get("myid"));
		String type_id = String.valueOf(map.get("shop_order_logistics_id"));
		String bills_type = String.valueOf(map.get("bills_type"));
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		String shop_order_logistics_name = String.valueOf(map.get("shop_order_logistics_name"));
		aa.put("user_operation_record_type", "新增");
		aa.put("user_operation_record_detail", "新增物流:" + shop_order_logistics_name);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
	}

	@Override
	@Transactional
	public void updatelogisticsdatabyid(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		String type_id = pd.getString("shop_order_logistics_id");
		String bills_type = pd.getString("bills_type");
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "编辑");
		String user_operation_record_detail = pd.getString("user_operation_record_detail");
		aa.put("user_operation_record_detail", user_operation_record_detail);
		aa.put("user_operation_record_createtime", new Date());
		if(!user_operation_record_detail.equals("")) {
			ordersDao.addUserOperationRecorddata(aa);
		}
		unitDao.updatelogisticsdatabyid(pd);
	}

	@Override
	@Transactional
	public void deletelogisticsdatabyid(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		String type_id = pd.getString("shop_order_logistics_id");
		String bills_type = pd.getString("bills_type");
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "删除");
		String shop_order_logistics_name = pd.getString("shop_order_logistics_name");
		aa.put("user_operation_record_detail", "删除物流信息：" + shop_order_logistics_name);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		unitDao.deletelogisticsdatabyid(pd);
	}

	@Override
	public List<PageData> getAllSellingDataListpage(Page page) {
		// TODO Auto-generated method stub
		return unitDao.getAllSellingDataListpage(page);
	}

	@Override
	@Transactional
	public void addsellingdata(PageData map) {
		// TODO Auto-generated method stub
		unitDao.addsellingdata(map);
		PageData aa = new PageData();
		String myid = String.valueOf(map.get("myid"));
		String type_id = String.valueOf(map.get("shop_warehouse_id"));
		String bills_type = String.valueOf(map.get("bills_type"));
		String shop_warehouse_name = String.valueOf(map.get("shop_warehouse_name"));
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "新增");
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		aa.put("user_operation_record_detail", "新增厂商信息:" + shop_warehouse_name);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
	}

	@Override
	@Transactional
	public void updatesellingdatabyid(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		String type_id = pd.getString("shop_warehouse_id");
		String bills_type = pd.getString("bills_type");
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "编辑");
		String shop_warehouse_name = pd.getString("shop_warehouse_name");
		String oldname = pd.getString("oldname");
		aa.put("user_operation_record_detail", "修改前:" + oldname+",修改后:" + shop_warehouse_name);
		aa.put("user_operation_record_createtime", new Date());
		if(!pd.getString("oldname").equals(pd.getString("shop_warehouse_name"))) {
			ordersDao.addUserOperationRecorddata(aa);
		}
		
		unitDao.updatesellingdatabyid(pd);

	}

	@Override
	@Transactional
	public void deletesellingdatabyid(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		String type_id = pd.getString("shop_warehouse_id");
		String bills_type = pd.getString("bills_type");
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "删除");
		String shop_warehouse_name = pd.getString("shop_warehouse_name");
		aa.put("user_operation_record_detail", "删除厂商信息：" + shop_warehouse_name);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		unitDao.deletesellingdatabyid(pd);
		unitDao.deletesellingaddressdatabywarehouseid(pd);
	}

	@Override
	public List<PageData> getAllSellingAddressListpage(Page page) {
		// TODO Auto-generated method stub
		return unitDao.getAllSellingAddressListpage(page);
	}

	@Override
	@Transactional
	public void addsellingAddressdata(PageData map) {
		// TODO Auto-generated method stub
		unitDao.addsellingAddressdata(map);
		PageData aa = new PageData();
		String myid = String.valueOf(map.get("myid"));
		String type_id = String.valueOf(map.get("warehouse_classify_id"));
		String bills_type = String.valueOf(map.get("bills_type"));
		String warehouse_classify_name = String.valueOf(map.get("warehouse_classify_name"));
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "新增");
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		aa.put("user_operation_record_detail", "新增厂商地址信息:" + warehouse_classify_name);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
	}

	@Override
	@Transactional
	public void updatesellingAddressdatabyid(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		String type_id = pd.getString("warehouse_classify_id");
		String bills_type = pd.getString("bills_type");
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "编辑");
		String warehouse_classify_name = pd.getString("warehouse_classify_name");
		String oldname = pd.getString("oldname");
		aa.put("user_operation_record_detail", "修改前:" + oldname+",修改后:" + warehouse_classify_name);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		if(!pd.getString("oldname").equals(pd.getString("warehouse_classify_name"))) {
			ordersDao.addUserOperationRecorddata(aa);
		}
		unitDao.updatesellingAddressdatabyid(pd);
	}

	@Override
	@Transactional
	public void deletesellingAddressdatabyid(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		String type_id = pd.getString("warehouse_classify_id");
		String bills_type = pd.getString("bills_type");
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "删除");
		String warehouse_classify_name = pd.getString("warehouse_classify_name");
		aa.put("user_operation_record_detail", "删除厂商地址信息：" + warehouse_classify_name);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		unitDao.deletesellingAddressdatabyid(pd);

	}
}
