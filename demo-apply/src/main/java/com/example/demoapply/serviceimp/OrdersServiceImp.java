package com.example.demoapply.serviceimp;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.hpsf.Decimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demoapply.dao.OrdersDao;
import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;
import com.example.demoapply.service.OrdersService;

@Service
public class OrdersServiceImp implements OrdersService {

	@Autowired
	OrdersDao ordersDao;

	@Override
	public List<PageData> getAllOrdersListpage(Page page) {
		// TODO Auto-generated method stub
		return ordersDao.getAllOrdersListpage(page);
	}

	@Override
	public List<PageData> getOrdersAllDetailsDataById(Page page) {
		// TODO Auto-generated method stub
		return ordersDao.getOrdersAllDetailsDataById(page);
	}

	@Override
	@Transactional
	public void changeOrderStatusById(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();	
		String myid = pd.getString("myid");
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "编辑");
		String id = pd.getString("id");
		String typedata = pd.getString("typedata");
		if(!pd.getString("history").isEmpty()) {
			aa.put("user_operation_record_detail", pd.getString("history"));
		}else {
			aa.put("user_operation_record_detail", "修改订单号为："+id+"的订单状态，修改为："+typedata);
		}
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		 ordersDao.changeOrderStatusById(pd);
	}

	@Override
	public List<PageData> getAllBadEvaluationListpage(Page page) {
		// TODO Auto-generated method stub
		return ordersDao.getAllBadEvaluationListpage(page);
	}

	@Override
	public List<PageData> getOrdersAllDetailsDataByCommentId(Page page) {
		// TODO Auto-generated method stub
		return ordersDao.getOrdersAllDetailsDataByCommentId(page);
	}

	@Override
	public List<PageData> getAllReturnOrderListpage(Page page) {
		// TODO Auto-generated method stub
		return ordersDao.getAllReturnOrderListpage(page);
	}

	@Override
	@Transactional
	public void changeReturnOrderStatusById(PageData pd) {
		// TODO Auto-generated method stub
		ordersDao.changeReturnOrderStatusById(pd);
		
	}

	@Override
	public List<PageData> getAllUserOrderIds(PageData pd) {
		// TODO Auto-generated method stub
		return ordersDao.getAllUserOrderIds(pd);
	}

	@Override
	public void changeOrderkaipiaoStatusById(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();	
		String myid = pd.getString("myid");
		String jieguo = pd.getString("jieguo");
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "编辑");
		String orderid = pd.getString("id");
		String type_id = pd.getString("id");
		String bills_type = pd.getString("bills_type");
		aa.put("type_id", type_id);
		aa.put("bills_type", bills_type);
		aa.put("user_operation_record_detail", "对订单号："+orderid+"开票申请："+jieguo);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
	
		if(pd.getString("type").equals("3")) {//bohui
			ordersDao.deleteAlljiluByOrderid(pd);
			pd.put("tongguo", 2);
			ordersDao.changeOrderkaipiaoStatusByinvoiceId(pd);
			
		}else {
			pd.put("tongguo",1);
			ordersDao.changeOrderkaipiaoStatusByinvoiceId(pd);
		}
		
		ordersDao.changeOrderkaipiaoStatusById(pd);
	}

	@Override
	@Transactional
	public void addColletionBillsdata(PageData pd) {
		// TODO Auto-generated method stub
		
		
		
		PageData bb=new PageData();
		bb =  ordersDao.seleUseridByOrderid(pd);
		String userid = bb.getString("userid");
		pd.put("userid", userid);
		ordersDao.updateOrdersTopayByOrderid(pd);
		ordersDao.updateJifenByuserid(pd);
		
		PageData aa = new PageData();	
		String myid = pd.getString("myid");
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "新增");
		String orderid = pd.getString("orderid");
		String money = pd.getString("money");
		aa.put("user_operation_record_detail", "对订单号："+orderid+"的用户id为:"+userid+"直接收款，收款金额为："+money);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		
		
		ordersDao.addColletionBillsdata(pd);
		PageData cc=new PageData();
		cc= ordersDao.seleOneByOrderid(pd);
		String ifpayed = cc.getString("payed");
		if(ifpayed.equals("yes")) {
			pd.put("id", pd.getString("orderid"));
			pd.put("type", 2);
			ordersDao.changeOrderStatusById(pd);
		}
	}

	@Override
	public List<PageData> getAllCollectionRecordListpage(Page page) {
		// TODO Auto-generated method stub
		return ordersDao.getAllCollectionRecordListpage(page);
	}

	@Override
	@Transactional
	public void addZheKouBillsdata(PageData pd) {
		// TODO Auto-generated method stub
		PageData nn=new PageData();
		nn =  ordersDao.seleUseridByOrderid(pd);
		String userid = nn.getString("userid");
		pd.put("userid", userid);
		ordersDao.addZheKouBillsdata(pd);
		
		
		PageData aa = new PageData();	
		String myid = pd.getString("myid");
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "新增");
		String orderid = pd.getString("orderid");
		String money = pd.getString("money");
		aa.put("user_operation_record_detail", "对订单号："+orderid+"的用户id为:"+userid+"新增打折申请，打折金额为："+money);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
//		ordersDao.updateOrdersDiscountByOrderid(pd);
//		PageData bb=new PageData();
//		bb= ordersDao.seleOneByOrderid(pd);
//		String ifpayed = bb.getString("payed");
//		if(ifpayed.equals("yes")) {
//			pd.put("id", pd.getString("orderid"));
//			pd.put("type", 2);
//			ordersDao.changeOrderStatusById(pd);
//		}
	}

	@Override
	public List<PageData> getAllDiscountRecordListpage(Page page) {
		// TODO Auto-generated method stub
		return ordersDao.getAllDiscountRecordListpage(page);
	}

	@Override
	@Transactional
	public void shenheDiscountSuccess(PageData pd) {
		// TODO Auto-generated method stub
		ordersDao.shenheDiscountSuccess(pd);
		PageData aa = new PageData();	
		String myid = pd.getString("myid");
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "新增");
		String orderid1 = pd.getString("order_info_id");
		String type_id = pd.getString("id");
		String bills_type = pd.getString("bills_type");
		String user_id = pd.getString("user_id");
		String changetype = pd.getString("changetype");
		String money = pd.getString("money");
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		aa.put("user_operation_record_detail", "对订单号："+orderid1+"的折扣审批单："+changetype+"，该用户编号为："+user_id+",折扣金额为："+money);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		PageData uu = ordersDao.getonediscountrecordbyid(pd);
		if(String.valueOf(uu.get("examine_status")).equals("2")) {
			String orderid = String.valueOf(uu.get("order_info_id"));
			pd.put("orderid", orderid);
			ordersDao.updateOrdersDiscountByOrderid(pd);
			PageData bb=new PageData();
			bb= ordersDao.seleOneByOrderid(pd);
			String ifpayed = bb.getString("payed");
			if(ifpayed.equals("yes")) {
				pd.put("id", pd.getString("orderid"));
				pd.put("type", 2);
				ordersDao.changeOrderStatusById(pd);
			}
		}
	
		
	}

	@Override
	public PageData getonediscountrecordbyid(PageData pd) {
		// TODO Auto-generated method stub
		return ordersDao.getonediscountrecordbyid(pd);
	}

	@Override
	@Transactional
	public void deleteshenheDiscountSuccess(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();	
		String myid = pd.getString("myid");
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "删除");
		String orderid = pd.getString("orderid");
		aa.put("user_operation_record_detail", "对订单号："+orderid+"重新进行打折申请");
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		ordersDao.deleteshenheDiscountSuccess(pd);
	}

	@Override
	public PageData getUserDataByOrderid(PageData pd) {
		// TODO Auto-generated method stub
		return ordersDao.getUserDataByOrderid(pd);
	}

	@Override
	public void addUserOperationRecorddata(PageData pd) {
		// TODO Auto-generated method stub
		pd.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(pd);
	}

	@Override
	public List<PageData> getAllKaiPiaoDetailsListpage(PageData pd) {
		// TODO Auto-generated method stub
		return ordersDao.getAllKaiPiaoDetailsListpage(pd);
	}

	@Override
	public List<PageData> getAllWorkOrderListpage(Page page) {
		// TODO Auto-generated method stub
		return ordersDao.getAllWorkOrderListpage(page);
	}

	@Override
	public List<PageData> getWorkOrdersAllDetailsDataById(Page page) {
		// TODO Auto-generated method stub
		return ordersDao.getWorkOrdersAllDetailsDataById(page);
	}

	@Override
	@Transactional
	public void changeWorkOrderStatusById(PageData pd) {
		// TODO Auto-generated method stub
		ordersDao.changeWorkOrderStatusById(pd);
	}

	@Override
	public List<PageData> getWorkOrdersDataByIdListPage(Page page) {
		// TODO Auto-generated method stub
		return ordersDao.getWorkOrdersDataByIdListPage(page);
	}

	@Override
	@Transactional
	public void changeAllOrderStatusByIds(Map<?, ?> map) {
		// TODO Auto-generated method stub
		
		
		JSONArray allids = JSONArray.parseArray(String.valueOf(map.get("ids")));
		for (int a = 0; a < allids.size(); a++) {
			JSONObject onedata = allids.getJSONObject(a);
			PageData aa = new PageData();	
			String myid = String.valueOf(map.get("myid"));
			aa.put("user_id", Integer.valueOf(myid));
			aa.put("user_operation_record_type", "编辑");
			String typedata = String.valueOf(map.get("typedata"));
		    aa.put("user_operation_record_detail", "修改订单号为："+onedata.getString("id")+"的订单状态，修改为："+typedata);
			aa.put("user_operation_record_createtime", new Date());
			ordersDao.addUserOperationRecorddata(aa);
			
			
			
			PageData bb = new PageData();
			bb.put("type", '3');
			bb.put("wuliugongsi", String.valueOf(map.get("wuliugongsi")));
			if(!String.valueOf(map.get("files")).isEmpty()) {
				bb.put("files", String.valueOf(map.get("files")));
			}
			bb.put("dingdan", String.valueOf(map.get("dingdan")));
			bb.put("remark", String.valueOf(map.get("remark")));
			bb.put("id", onedata.getString("id"));
			bb.put("yunfei", onedata.getString("yunfei"));
			
			ordersDao.changeOrderStatusById(bb);
		}
	}

}
