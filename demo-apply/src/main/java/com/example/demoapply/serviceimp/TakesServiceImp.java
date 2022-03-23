package com.example.demoapply.serviceimp;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demoapply.dao.OrdersDao;
import com.example.demoapply.dao.TakesDao;
import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;
import com.example.demoapply.service.OrdersService;
import com.example.demoapply.service.TakesService;

@Service
public class TakesServiceImp implements TakesService {

	@Autowired
	TakesDao takesDao;
	@Autowired
	OrdersDao ordersDao;

	@Override
	public List<PageData> getAllTakeListpage(Page page) {
		// TODO Auto-generated method stub
		return takesDao.getAllTakeListpage(page);
	}

	@Override
	@Transactional
	public void changeTakeStatusById(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		String changetype = pd.getString("changetype");
		String bills_type = pd.getString("bills_type");
		String type_id = pd.getString("task_info_id");
		aa.put("type_id", type_id);
		aa.put("bills_type", bills_type);
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "编辑");
		aa.put("user_operation_record_detail", "编辑任务状态为：" + changetype);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		takesDao.changeTakeStatusById(pd);
	}

	@Override
	@Transactional
	public int addNewTaskData(PageData map) {
		// TODO Auto-generated method stub
		takesDao.addNewTaskData(map);
		PageData cc = new PageData();
		cc.put("user_id", String.valueOf(map.get("myid")));
		PageData ss = takesDao.seleUseridByPhone(cc);
		if(ss!=null) {
			PageData dd = new PageData();
			dd.put("user_id", String.valueOf(ss.get("user_id")));
			dd.put("task_info_id", String.valueOf(map.get("task_info_id")));
			takesDao.addNewTaskRelationData(dd);
		}
		PageData aa = new PageData();
		String myid = String.valueOf(map.get("myid"));
		String type_id = String.valueOf(map.get("task_info_id"));
		String bills_type = String.valueOf(map.get("bills_type"));
		String worker_two_name = String.valueOf(map.get("worker_two_name"));
		String task_info_type = String.valueOf(map.get("task_info_type"));
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("type_id", type_id);
		aa.put("bills_type", bills_type);
		aa.put("user_operation_record_type", "新增");
		aa.put("user_operation_record_detail", "新增任务信息，工种为：" + worker_two_name + "单据类型为：" + task_info_type);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		return 1;
	}

	@Override
	public List<PageData> getAllGongzhongListpage(Page page) {
		// TODO Auto-generated method stub
		return takesDao.getAllGongzhongListpage(page);
	}

	@Override
	@Transactional
	public void addgongzhongdata(PageData map) {
		// TODO Auto-generated method stub
		map.put("worker_two_status", 1);
		takesDao.addgongzhongdata(map);
		PageData bb = new PageData();
		bb.put("worker_one_id", String.valueOf(map.get("fenleiid")));
		bb.put("worker_two_id", String.valueOf(map.get("task_worktype_id")));
		bb.put("worker_one_two_creatime", new Date());
		bb.put("worker_one_two_status", 1);
		takesDao.addgongzhongrelationdata(bb);
		PageData aa = new PageData();
		String myid = String.valueOf(map.get("myid"));
		String type_id = String.valueOf(map.get("task_worktype_id"));
		String bills_type = String.valueOf(map.get("bills_type"));
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		String task_worktype_name = String.valueOf(map.get("worker_two_name"));
		aa.put("user_operation_record_type", "新增");
		aa.put("user_operation_record_detail", "新增工种:" + task_worktype_name);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
	}

	@Override
	@Transactional
	public void updategongzhongdatabyid(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		String type_id = pd.getString("worker_two_id");
		String bills_type = pd.getString("bills_type");
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "编辑");
		String worker_two_name = pd.getString("worker_two_name");
		String oldname = pd.getString("oldname");
		aa.put("user_operation_record_detail", "修改前:" + oldname + ",修改后:" + worker_two_name);
		aa.put("user_operation_record_createtime", new Date());
		if (!oldname.equals(worker_two_name)) {
			ordersDao.addUserOperationRecorddata(aa);
		}
		takesDao.deletegongzhongrelationdatabyid(pd);

		takesDao.updategongzhongdatabyid(pd);
		PageData bb = new PageData();
		bb.put("worker_one_id", pd.getString("fenleiid"));
		bb.put("worker_two_id", pd.getString("worker_two_id"));
		bb.put("worker_one_two_creatime", new Date());
		bb.put("worker_one_two_status", 1);
		takesDao.addgongzhongrelationdata(bb);
	}

	@Override
	@Transactional
	public void deletegongzhongdatabyid(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		aa.put("user_id", Integer.valueOf(myid));
		String type_id = pd.getString("worker_two_id");
		String bills_type = pd.getString("bills_type");
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		String task_worktype_name = pd.getString("worker_two_name");
		aa.put("user_operation_record_type", "删除");
		aa.put("user_operation_record_detail", "删除工种：" + task_worktype_name);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		takesDao.deletegongzhongrelationdatabyid(pd);
		takesDao.deletegongzhongdatabyid(pd);

	}

	@Override
	public List<PageData> getAllLeiXingListpage(Page page) {
		// TODO Auto-generated method stub
		return takesDao.getAllLeiXingListpage(page);
	}

	@Override
	@Transactional
	public void addleixingdata(PageData map) {
		// TODO Auto-generated method stub
		takesDao.addleixingdata(map);
		PageData aa = new PageData();
		String myid = String.valueOf(map.get("myid"));
		String type_id = String.valueOf(map.get("task_type_id"));
		String bills_type = String.valueOf(map.get("bills_type"));
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		String task_type_name = String.valueOf(map.get("task_type_name"));
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "新增");
		aa.put("user_operation_record_detail", "新增类型" + task_type_name);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
	}

	@Override
	@Transactional
	public void updateleixingdatabyid(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		String type_id = pd.getString("task_type_id");
		String bills_type = pd.getString("bills_type");
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "编辑");
		String task_type_name = pd.getString("task_type_name");
		String oldname = pd.getString("oldname");
		aa.put("user_operation_record_olddetail", "修改前:" + oldname);
		aa.put("user_operation_record_detail", "修改后:" + task_type_name);
		aa.put("user_operation_record_createtime", new Date());
		if (!oldname.equals(task_type_name)) {
			ordersDao.addUserOperationRecorddata(aa);
		}

		takesDao.updateleixingdatabyid(pd);
	}

	@Override
	@Transactional
	public void deleteleixingdatabyid(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		String type_id = pd.getString("task_worktype_id");
		String bills_type = pd.getString("bills_type");
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		String task_type_name = pd.getString("task_type_name");
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "删除");
		aa.put("user_operation_record_detail", "删除任务类型：" + task_type_name);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		takesDao.deleteleixingdatabyid(pd);
	}

	@Override
	public List<PageData> getAllGongzhong1Listpage(Page page) {
		// TODO Auto-generated method stub
		return takesDao.getAllGongzhong1Listpage(page);
	}

	@Override
	public List<PageData> getAllgongzhongfenlei(Page page) {
		// TODO Auto-generated method stub
		return takesDao.getAllgongzhongfenlei(page);
	}

	@Override
	@Transactional
	public void addgongzhongleixingdata(PageData map) {
		// TODO Auto-generated method stub
		map.put("worker_two_status", 1);
		takesDao.addgongzhongleixingdata(map);
		PageData aa = new PageData();
		String myid = String.valueOf(map.get("myid"));
		String type_id = String.valueOf(map.get("worker_one_id"));
		String bills_type = String.valueOf(map.get("bills_type"));
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		String name = String.valueOf(map.get("worker_one_name"));
		aa.put("user_operation_record_type", "新增");
		aa.put("user_operation_record_detail", "新增工种类型:" + name);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);

	}

	@Override
	@Transactional
	public void updategongzhongleixingdatabyid(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		String type_id = pd.getString("task_type_id");
		String bills_type = pd.getString("bills_type");
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "编辑");
		String task_type_name = pd.getString("worker_one_name");
		String oldname = pd.getString("oldname");
		aa.put("user_operation_record_olddetail", "修改前:" + oldname);
		aa.put("user_operation_record_detail", "修改后:" + task_type_name);
		aa.put("user_operation_record_createtime", new Date());
		if (!oldname.equals(task_type_name)) {
			ordersDao.addUserOperationRecorddata(aa);
		}

		takesDao.updategongzhongleixingdatabyid(pd);
		
	}

	@Override
	@Transactional
	public void deleteOnefenleidataByid(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		String type_id = pd.getString("task_worktype_id");
		String bills_type = pd.getString("bills_type");
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		String task_type_name = pd.getString("name");
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "删除");
		aa.put("user_operation_record_detail", "删除工种分类：" + task_type_name);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		takesDao.deleteOnefenleidataByid(pd);
		
	}

}
