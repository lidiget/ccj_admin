package com.example.demoapply.serviceimp;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demoapply.dao.CommonUserDao;
import com.example.demoapply.dao.OrdersDao;
import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;
import com.example.demoapply.service.CommonUserService;

@Service
public class CommonUserServiceImp implements CommonUserService {

	@Autowired
	CommonUserDao commonuserDao;
	@Autowired
	OrdersDao ordersDao;

	@Override
	public List<PageData> getAllCommonUsersDataListpage(Page page) {
		// TODO Auto-generated method stub
		return commonuserDao.getAllCommonUsersDataListpage(page);
	}

	@Override
	public PageData getOneCommonUserDataByid(PageData pd) {
		// TODO Auto-generated method stub
		return commonuserDao.getOneCommonUserDataByid(pd);
	}

	@Override
	@Transactional
	public int updataCommonuserdataByid(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String type_id = pd.getString("user_id");
		String bills_type = pd.getString("bills_type");
		String user_operation_record_detail = pd.getString("user_operation_record_detail");
		String myid = pd.getString("myid");
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "编辑");
		aa.put("type_id", type_id);
		aa.put("user_operation_record_detail", user_operation_record_detail);
		aa.put("bills_type", bills_type);
		aa.put("user_operation_record_createtime", new Date());
		if(!user_operation_record_detail.equals("")) {
			ordersDao.addUserOperationRecorddata(aa);
		}
		return commonuserDao.updataCommonuserdataByid(pd);
	}

	@Override
	@Transactional
	public int addCommonuserdata(PageData map) {
		// TODO Auto-generated method stub
		commonuserDao.addCommonuserdata(map);
		
		PageData aa = new PageData();
		String myid = String.valueOf(map.get("myid"));
		aa.put("user_id", Integer.valueOf(myid));
		String type_id = String.valueOf(map.get("user_id"));
		String bills_type = String.valueOf(map.get("bills_type"));
		String user_operation_record_detail = String.valueOf(map.get("user_operation_record_detail"));
		aa.put("user_operation_record_type", "新增");
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		aa.put("user_operation_record_detail", user_operation_record_detail);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		return 1;
	}

	@Override
	public List<?> SelectPhone(Map<?, ?> map) {
		// TODO Auto-generated method stub
		return commonuserDao.SelectPhone(map);
	}

	@Override
	public PageData SelectUserData(PageData pd) {
		// TODO Auto-generated method stub
		return commonuserDao.SelectUserData(pd);
	}

	@Override
	@Transactional
	public int deleteOneCommonuserdataByid(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		aa.put("user_id", Integer.valueOf(myid));
		String bills_type = pd.getString("bills_type");
		String username = pd.getString("username");
		aa.put("bills_type", bills_type);
		aa.put("user_operation_record_detail", "删除了用户：" + username + "的个人信息");
		aa.put("user_operation_record_type", "删除");
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		return commonuserDao.deleteOneCommonuserdataByid(pd);
	}

	@Override
	public int UpdatepwdbyCommonuserid(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		aa.put("user_id", Integer.valueOf(myid));
		String type_id = pd.getString("user_id");
		String bills_type = pd.getString("bills_type");
		String username = pd.getString("username");
		String password = pd.getString("password");
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		aa.put("user_operation_record_type", "编辑");
		aa.put("user_operation_record_detail", "修改了用户：" + username + "的密码，新密码为：" + password);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		return commonuserDao.UpdatepwdbyCommonuserid(pd);
	}

	@Override
	public PageData getMemberSettingByid(PageData pd) {
		// TODO Auto-generated method stub
		return commonuserDao.getMemberSettingByid(pd);
	}

	@Override
	@Transactional
	public int updateMemberSettingByid(PageData pd) {
		// TODO Auto-generated method stub
		return commonuserDao.updateMemberSettingByid(pd);
	}

	@Override
	@Transactional
	public int updatesellidbyuserid(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		aa.put("user_id", Integer.valueOf(myid));
		String type_id = pd.getString("user_id");
		String bills_type = pd.getString("bills_type");
		String user_operation_record_detail = pd.getString("user_operation_record_detail");
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		aa.put("user_operation_record_type", "编辑");
		aa.put("user_operation_record_detail", user_operation_record_detail);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		commonuserDao.updatesellidbyuserid(pd);
		return 1;
	}

}
