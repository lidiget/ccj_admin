package com.example.demoapply.serviceimp;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demoapply.dao.OrdersDao;
import com.example.demoapply.dao.RoleDao;
import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;
import com.example.demoapply.service.RoleService;

@Service
public class RoleServiceImp implements RoleService {

    @Autowired
    RoleDao roleDao;
    @Autowired
	OrdersDao ordersDao;

	@Override
	public List<PageData> getAllRoleListPage(Page page) {
		// TODO Auto-generated method stub
		return roleDao.getAllRoleListPage(page);
	}

	@Override
	public PageData getOneRoleDataByid(PageData pd) {
		// TODO Auto-generated method stub
		return roleDao.getOneRoleDataByid(pd);
	}

	@Override
	@Transactional
	public int updataRoledataByid(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		String type_id = pd.getString("role_id");
		String bills_type = pd.getString("bills_type");
		String user_operation_record_detail = pd.getString("user_operation_record_detail");
//		String user_operation_record_olddetail = pd.getString("user_operation_record_olddetail");
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "编辑");
		aa.put("type_id", type_id);
		aa.put("user_operation_record_detail", user_operation_record_detail);
//		aa.put("user_operation_record_olddetail", user_operation_record_olddetail);
		aa.put("bills_type", bills_type);
		aa.put("user_operation_record_createtime", new Date());
		if(!user_operation_record_detail.equals("")) {
			ordersDao.addUserOperationRecorddata(aa);
		}
		return roleDao.updataRoledataByid(pd);
	}

	@Override
	@Transactional
	public int InsertRole(PageData map) {
		// TODO Auto-generated method stub
		roleDao.InsertRole(map);
		PageData aa = new PageData();
		String myid = String.valueOf(map.get("myid"));
		aa.put("user_id", Integer.valueOf(myid));
		String type_id = String.valueOf(map.get("role_id"));
		String bills_type = String.valueOf(map.get("bills_type"));
		aa.put("user_operation_record_type", "新增");
		String user_operation_record_detail = String.valueOf(map.get("user_operation_record_detail"));
		aa.put("user_operation_record_detail", user_operation_record_detail);
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		return 1;
	}

	@Override
	@Transactional
	public int deleteOneRoledataByid(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		String name = pd.getString("rolename");
		String bills_type = pd.getString("bills_type");
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("bills_type", bills_type);
		aa.put("user_operation_record_type", "删除");
		aa.put("user_operation_record_detail", "删除了名称为："+name+"的角色信息");
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		return roleDao.deleteOneRoledataByid(pd);
	}

	@Override
	public List<PageData> getAllRoleData(PageData pd) {
		// TODO Auto-generated method stub
		return roleDao.getAllRoleData(pd);
	}

	@Override
	@Transactional
	public int udataRoleRights(Map<?, ?> map) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = String.valueOf(map.get("myid"));
		String name = String.valueOf(map.get("rolename"));
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "编辑");
		aa.put("user_operation_record_detail", "编辑了角色名称为："+name+"的表格信息展示与隐藏");
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		return roleDao.udataRoleRights(map);
	}


	@Override
	@Transactional
	public int udataRoleGongNengRights(Map<?, ?> map) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = String.valueOf(map.get("myid"));
		String name = String.valueOf(map.get("rolename"));
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "编辑");
		aa.put("user_operation_record_detail", "编辑了角色名称为："+name+"的权限范围");
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		return roleDao.udataRoleGongNengRights(map);
	
	}

	@Override
	@Transactional
	public int updataRoledataByid1(PageData pd) {
		// TODO Auto-generated method stub
		return roleDao.updataRoledataByid1(pd);
	}

	@Override
	public List<PageData> getAllRoleListById(PageData aa) {
		// TODO Auto-generated method stub
		return roleDao.getAllRoleListById(aa);
	}

    

}
