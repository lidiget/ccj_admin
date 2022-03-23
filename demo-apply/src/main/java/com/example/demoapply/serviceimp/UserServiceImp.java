package com.example.demoapply.serviceimp;

import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demoapply.dao.OrdersDao;
import com.example.demoapply.dao.UserDao;
import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;
import com.example.demoapply.service.UserService;

@Service
public class UserServiceImp implements UserService {

	@Autowired
	UserDao userDao;
	@Autowired
	OrdersDao ordersDao;

	@Override
	public List<?> CaiLogin(Map<?, ?> map) {
		return userDao.CaiLogin(map);
	}

	@Override
	public List<?> SelectPhone(Map<?, ?> map) {
		return userDao.SelectPhone(map);
	}

	@Override
	@Transactional
	public int InsertUser(Map<?, ?> map) {
		
		userDao.InsertUser(map);
		//如果是新增用户那么就还需要新增app端用户信息
		
		
		
		
		if(String.valueOf(map.get("sell_leve")).equals("1")) {//如果是销售人员新增销售人员信息
			PageData bb = new PageData();
			bb.put("name", String.valueOf(map.get("nick_name")));
			bb.put("role", 0);
			bb.put("user_id", String.valueOf(map.get("user_id")));
			userDao.addSelluserdata(bb);
		}
		PageData aa = new PageData();
		String myid = String.valueOf(map.get("myid"));
		String type_id = String.valueOf(map.get("user_id"));
		String bills_type = String.valueOf(map.get("bills_type"));
		String user_operation_record_detail = String.valueOf(map.get("user_operation_record_detail"));
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "新增");
		aa.put("user_operation_record_detail", user_operation_record_detail);
		aa.put("bills_type", bills_type);
		aa.put("type_id", type_id);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		return 1;
	}

	@Override
	@Transactional
	public int InsertUrole(Map<?, ?> map) {
		return userDao.InsertUrole(map);
	}

	@Override
	@Transactional
	public int Updatepwd(Map<?, ?> map) {
		return userDao.Updatepwd(map);
	}

	@Override
	@Transactional
	public int InsertWallet(Map<?, ?> map) {
		return userDao.InsertWallet(map);
	}

	@Override
	@Transactional
	public Integer UpdateIdentity(PageData pd) {
		// TODO Auto-generated method stub
		return userDao.UpdateIdentity(pd);
	}

	@Override
	public List<PageData> getAllAdminListPage(Page pd) {
		// TODO Auto-generated method stub
		return userDao.getAllAdminList(pd);
	}

	@Override
	public PageData getOneAdminDataByid(PageData pd) {
		// TODO Auto-generated method stub
		return userDao.getOneAdminDataByid(pd);
	}

	@Override
	@Transactional
	public Integer updataAdmindataByid(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		String bills_type = pd.getString("bills_type");
		aa.put("user_id", Integer.valueOf(myid));
		String type_id = pd.getString("user_id");
		String user_operation_record_detail = pd.getString("user_operation_record_detail");
//		String user_operation_record_olddetail = pd.getString("user_operation_record_olddetail");
		aa.put("user_operation_record_type", "编辑");
		aa.put("type_id", type_id);
		aa.put("user_operation_record_detail", user_operation_record_detail);
//		aa.put("user_operation_record_olddetail", user_operation_record_olddetail);
		aa.put("bills_type", bills_type);
		aa.put("user_operation_record_createtime", new Date());
		if(!user_operation_record_detail.equals("")) {
			ordersDao.addUserOperationRecorddata(aa);
		}
		if(pd.getString("sell_leve").equals("3")) {//如果为普通用户状态为3那么就查找有没有当前userid的销售有的话就删除
			PageData cc = new PageData();
			cc.put("userid", pd.getString("user_id"));
			userDao.deleteOneSellUserByid(cc);
		}else if(pd.getString("sell_leve").equals("1")) {
			PageData allselling = userDao.getAllSelling(pd);
			if(allselling==null) {//如果等于空那么新增销售信息
				PageData dd = new PageData();
				dd.put("name", pd.getString("nick_name"));
				dd.put("role", 0);
				dd.put("user_id", pd.getString("user_id"));
				userDao.addSelluserdata(dd);
			}else{//如果不为空就更新销售名称
				PageData ee = new PageData();
				System.out.println("1111111111111111111111111111");
				System.out.println(pd.getString("nick_name"));
				ee.put("nick_name", pd.getString("nick_name"));
				ee.put("user_id", pd.getString("user_id"));
				userDao.updateSelluserdata(ee);
			}
		}else if(pd.getString("sell_leve").equals("2")) {//销售主管
			PageData allselling = userDao.getAllSelling(pd);
			if(allselling==null) {//如果等于空那么新增销售主管信息
				PageData dd = new PageData();
				dd.put("name", pd.getString("nick_name"));
				dd.put("role", 1);
				dd.put("user_id", pd.getString("user_id"));
				userDao.addSelluserdata(dd);
			}else{//如果不为空就更新销售名称
				PageData ee = new PageData();
				ee.put("nick_name", pd.getString("nick_name"));
				ee.put("user_id", pd.getString("user_id"));
				userDao.updateSelluserdata(ee);
			}
		}
		return userDao.updataAdmindataByid(pd);
	}

	@Override
	@Transactional
	public int Updatepwdbyuserphone(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		String bills_type = pd.getString("bills_type");
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "编辑");
		String newpassword = pd.getString("newpassword");
		String type_id = pd.getString("user_id");
		aa.put("type_id", type_id);
		aa.put("bills_type", bills_type);
		aa.put("user_operation_record_detail", "更新用户密码，重置后新密码为:"+newpassword);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		return userDao.Updatepwdbyuserphone(pd);
	}

	@Override
	@Transactional
	public int deleteOneAdmindataByid(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "删除");
		String username = pd.getString("username");
		String phonenumber = pd.getString("phonenumber");
		aa.put("user_operation_record_detail", "删除用户:"+username+"的信息,该用户的联系方式为:"+phonenumber);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		userDao.deleteOneAdmindataByid(pd);
		userDao.deleteOneSellUserByid(pd);
		return 1;
	}

	@Override
	public PageData SelectOnebyAdminId(PageData pd) {
		// TODO Auto-generated method stub
		return userDao.SelectOnebyAdminId(pd);
	}

	@Override
	public List<PageData> getAllDeptClass(PageData pd) {
		// TODO Auto-generated method stub
		return userDao.getAllDeptClass(pd);
	}

	@Override
	@Transactional
	public int addDeptClassData(Map<?, ?> map) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = String.valueOf(map.get("myid"));
		String classname = String.valueOf(map.get("classname"));
	
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "新增");
		aa.put("user_operation_record_detail", "新增部门名称:"+classname);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		return userDao.addDeptClassData(map);
	}

	@Override
	@Transactional
	public void updateDeptClassById(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		aa.put("user_id", Integer.valueOf(myid));
		String oldclassname =pd.getString("oldclassname");
		String classname =pd.getString("classname");
		aa.put("user_operation_record_type", "编辑");
		aa.put("user_operation_record_detail", "编辑部门信息,原部门名称:"+oldclassname+"修改为:"+classname);
		aa.put("user_operation_record_createtime", new Date());
		if(!oldclassname.equals(classname)) {
			ordersDao.addUserOperationRecorddata(aa);
		}
		userDao.updateDeptClassById(pd);
	}

	@Override
	@Transactional
	public void deleteDeptClassById(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		String classname =pd.getString("classname");
		String type =pd.getString("type");
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "删除");
		aa.put("user_operation_record_detail", "删除"+type+"部门信息:"+classname);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		userDao.deleteDeptClassById(pd);
	}

	@Override
	public List<PageData> getAllUserOperationRecordListpage(Page page) {
		// TODO Auto-generated method stub
		return userDao.getAllUserOperationRecordListpage(page);
	}

	@Override
	public List<PageData> getUserDataByOrderid(PageData pd) {
		// TODO Auto-generated method stub
		return userDao.getUserDataByOrderid(pd);
	}

	@Override
	public List<PageData> getAllueridBySellzhuguanid(PageData pd) {
		// TODO Auto-generated method stub
		return userDao.getAllueridBySellzhuguanid(pd);
	}

	@Override
	public List<PageData> getAllUseridBysellid(PageData nn) {
		// TODO Auto-generated method stub
		return userDao.getAllUseridBysellid(nn);
	}


	@Override
	@Transactional
	public void addgenzongxinxibyuserid(PageData pd) {
		// TODO Auto-generated method stub
		userDao.addgenzongxinxibyuserid(pd);
	}

	@Override
	public List<PageData> getAllTrackingDataByUseridListpage(Page page) {
		// TODO Auto-generated method stub
		return userDao.getAllTrackingDataByUseridListpage(page);
	}

	@Override
	public List<PageData> selectAllbangdinguserData(PageData pd) {
		// TODO Auto-generated method stub
		PageData bb = userDao.getTheSellidByUserid(pd);
		PageData aa = new PageData();
		aa.put("id", String.valueOf(bb.get("id")));
		return userDao.getAllUseridBysellid(aa);
	}

	@Override
	@Transactional
	public void updataAllSelldataByuserid(PageData pd) {
		// TODO Auto-generated method stub
		PageData bb = userDao.getTheSellidByUserid(pd);
		PageData aa = new PageData();
		aa.put("oldid", String.valueOf(bb.get("id")));
		aa.put("newsellid", pd.getString("newsellid"));
		userDao.updataAllSelldataByuserid(aa);
	}
	
	
	public static void main(String[] args) throws Exception{  
        URL serverUrl = new URL("http:47.103.34.184:8084/api/USER/houRegisterUser");  
        HttpURLConnection conn = (HttpURLConnection) serverUrl.openConnection();  
        conn.setRequestMethod("POST");  
        conn.setRequestProperty("Content-type", "application/json");  
        //必须设置false，否则会自动redirect到重定向后的地址  
        conn.setInstanceFollowRedirects(false);  
        conn.connect();  
        String result = getReturn(conn);  
    }  
  
    /*请求url获取返回的内容*/  
    public static String getReturn(HttpURLConnection connection) throws IOException{  
        StringBuffer buffer = new StringBuffer();  
        //将返回的输入流转换成字符串  
        try(InputStream inputStream = connection.getInputStream();  
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);  
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);){  
            String str = null;  
            while ((str = bufferedReader.readLine()) != null) {  
                buffer.append(str);  
            }  
            String result = buffer.toString();  
            return result;  
        }  
}

	@Override
	public List<PageData> selectuserdatabyphone(PageData pd) {
		// TODO Auto-generated method stub
		return userDao.selectuserdatabyphone(pd);
	}

	@Override
	@Transactional
	public void addyuyuexinxi(PageData pd) {
		// TODO Auto-generated method stub
		userDao.addyuyuexinxi(pd);
		
	}

	@Override
	public List<PageData> getAllNewPictures(PageData pd) {
		// TODO Auto-generated method stub
		return userDao.getAllNewPictures(pd);
	}

	@Override
	@Transactional
	public void updateAllNewPicturesByid(PageData pd) {
		// TODO Auto-generated method stub
		userDao.updateAllNewPicturesByid(pd);
	}
}
