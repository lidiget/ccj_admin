package com.example.demoapply.serviceimp;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demoapply.dao.OrdersDao;
import com.example.demoapply.dao.WorkUserDao;
import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;
import com.example.demoapply.service.WorkUserService;

@Service
public class WorkUserServiceImp implements WorkUserService {

    @Autowired
    WorkUserDao workUserDao;
    @Autowired
	OrdersDao ordersDao;
	@Override
	public List<PageData> getAllWorkUsersDataListpage(Page page) {
		// TODO Auto-generated method stub
		return workUserDao.getAllWorkUsersDataListpage(page);
	}

	@Override
	public PageData getOneWorkUserDataByid(PageData pd) {
		// TODO Auto-generated method stub
		return workUserDao.getOneWorkUserDataByid(pd);
	}

	@Override
	public PageData SelectWorkerData(PageData pd) {
		// TODO Auto-generated method stub
		return workUserDao.SelectWorkerData(pd);
	}

	@Override
	@Transactional
	public int updataWorkuserdataByid(PageData pd) {
		// TODO Auto-generated method stub
		String type_id = pd.getString("worker_id");
		PageData onedata1 = new PageData();
		onedata1.put("worker_id", Integer.valueOf(type_id));
		ordersDao.deleteworktwonamebyworkid(onedata1);
		String list = pd.getString("worker_two_name");
		String[] bb = list.split(",");
		for (int i = 0; i < bb.length; i++) {
			String id = bb[i];
			PageData onedata = new PageData();
			onedata.put("worker_two_id", Integer.valueOf(id));
			onedata.put("worker_id", Integer.valueOf(type_id));
			onedata.put("user_worker_cretime", new Date());
			onedata.put("user_worker_status", 1);
			onedata.put("choosedunit", '天');
			onedata.put("worker_price",999);
			ordersDao.addworktwonamebyworkid(onedata);
		}
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		String bills_type = pd.getString("bills_type");
		String user_operation_record_detail = pd.getString("user_operation_record_detail");
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("type_id", type_id);
		aa.put("bills_type", bills_type);
		aa.put("user_operation_record_type", "编辑");
		aa.put("user_operation_record_detail", user_operation_record_detail);
		aa.put("user_operation_record_createtime", new Date());
		if(!user_operation_record_detail.equals("")) {
			ordersDao.addUserOperationRecorddata(aa);
		}
		return workUserDao.updataWorkuserdataByid(pd);
	}

	@Override
	@Transactional
	public int UpdatepwdbyWokerid(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();	
		String myid = pd.getString("myid");
		String bills_type = pd.getString("bills_type");
		String type_id = pd.getString("worker_id");
		String workername = pd.getString("workername");
		String pwd = pd.getString("password");
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "编辑");
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		aa.put("user_operation_record_detail", "重置工人："+workername+"的个人密码，新密码为："+pwd);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		return workUserDao.UpdatepwdbyWokerid(pd);
	}

	@Override
	@Transactional
	public int addWorkuserdata(PageData map) {
		// TODO Auto-generated method stub
		workUserDao.addWorkuserdata(map);
		String type_id = String.valueOf(map.get("worker_id"));
		String list = String.valueOf(map.get("worker_two_name"));
		String[] bb = list.split(",");
		for (int i = 0; i < bb.length; i++) {
			String id = bb[i];
			PageData onedata = new PageData();
			onedata.put("worker_two_id", Integer.valueOf(id));
			onedata.put("worker_id", Integer.valueOf(type_id));
			onedata.put("user_worker_cretime", new Date());
			onedata.put("user_worker_status", 1);
			onedata.put("choosedunit", '天');
			onedata.put("worker_price",999);
			ordersDao.addworktwonamebyworkid(onedata);
		}
		
		PageData aa = new PageData();
		String myid = String.valueOf(map.get("myid"));
//		String type_id = String.valueOf(map.get("worker_id"));
		String bills_type = String.valueOf(map.get("bills_type"));
		String user_operation_record_detail = String.valueOf(map.get("user_operation_record_detail"));
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		aa.put("user_operation_record_type", "新增");
		aa.put("user_operation_record_detail", user_operation_record_detail);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		return 1;
	}

	@Override
	@Transactional
	public int deleteoneworkuserdataByid(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		String bills_type = pd.getString("bills_type");
		String workername = pd.getString("workername");
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("bills_type", bills_type);
		aa.put("user_operation_record_type", "删除");
		aa.put("user_operation_record_detail", "删除工人："+workername+"的个人信息");
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		workUserDao.deleteoneworkuserRelationdataByid(pd);//删除先将工种和工人关联信息删除
		return workUserDao.deleteoneworkuserdataByid(pd);
	}

	@Override
	public List<PageData> getALLgongzhongxinxiListPage(Page page) {
		// TODO Auto-generated method stub
		return workUserDao.getALLgongzhongxinxiListPage(page);
	}

	@Override
	public PageData getgongzhongdatabygongzhongid(PageData pd) {
		// TODO Auto-generated method stub
		return workUserDao.getgongzhongdatabygongzhongid(pd);
	}

	@Override
	@Transactional
	public void updatagongzongdataByid(PageData pd) {
		// TODO Auto-generated method stub
		workUserDao.updatagongzongdataByid(pd);
	}

  

}
