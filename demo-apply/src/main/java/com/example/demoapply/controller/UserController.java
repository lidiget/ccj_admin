package com.example.demoapply.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;
import com.example.demoapply.redis.RedisUtil;
import com.example.demoapply.service.CardService;
import com.example.demoapply.service.CommonUserService;
import com.example.demoapply.service.GoodsService;
import com.example.demoapply.service.OrdersService;
import com.example.demoapply.service.RecordService;
import com.example.demoapply.service.ReportService;
import com.example.demoapply.service.RoleService;
import com.example.demoapply.service.TakesService;
import com.example.demoapply.service.UnitService;
import com.example.demoapply.service.UserService;
import com.example.demoapply.service.VehicleService;
import com.example.demoapply.service.WorkUserService;
import com.example.demoapply.service.WorkerOrdersService;
import com.example.demoapply.shiro.CreateToken;
import com.example.democommon.annotation.PreventSumbit;
import com.example.democommon.unit.HttpUtils;
import com.example.democommon.unit.Randoms;
import com.example.democommon.unit.TokenUtils;
import com.example.democommon.unitedreturn.HjControllerHelper;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.MediaProcessTaskInput;
import com.tencentcloudapi.vod.v20180717.models.ProcessMediaRequest;
import com.tencentcloudapi.vod.v20180717.models.ProcessMediaResponse;
import com.tencentcloudapi.vod.v20180717.models.SearchMediaRequest;
import com.tencentcloudapi.vod.v20180717.models.SearchMediaResponse;
import com.tencentcloudapi.vod.v20180717.models.TranscodeTaskInput;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import utils.EmailSendUtil;
import utils.GaoDeUtil;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@ComponentScan(basePackages = { "controller" })
@RequestMapping("/api/user")
@Api(value = "APP运行的controller")
public class UserController extends BaseController {
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	UserService userService;
	@Autowired
	RoleService roleService;
	@Autowired
	CommonUserService commonuserService;
	@Autowired
	GoodsService goodsService;
	@Autowired
	WorkUserService workUserService;
	@Autowired
	OrdersService ordersService;
	@Autowired
	WorkerOrdersService workerOrdersService;
	@Autowired
	TakesService takesService;
	@Autowired
	RecordService recordService;
	@Autowired
	CardService cardService;
	@Autowired
	VehicleService vehicleService;
	@Autowired
	ReportService reportService;
	@Autowired
	UnitService unitService;

	// 统一返回json
	HjControllerHelper hjControllerHelper = new HjControllerHelper();

	List<Map<?, ?>> list = new ArrayList<>();

	@ApiOperation("登录:根据手机号user_phone和密码user_password去进行登录," + "生成一个token，前端进行保存,每次请求带上")
	@PostMapping("/login")
	@ResponseBody
	@PreventSumbit
	public Map<?, ?> login(@RequestBody Map<?, ?> map) {
		System.out.println(map.get("user_phone") + "  " + map.get("user_password"));
		String user_phone = String.valueOf(map.get("user_phone"));
		String user_password = String.valueOf(map.get("user_password"));
		if (userService.SelectPhone(map) == null || userService.SelectPhone(map).isEmpty()) {
			return hjControllerHelper.result(list, false, "手机号未注册", 406);
		} else {
			PageData aa = new PageData();
			aa.put("user_phone", user_phone);
			aa.put("user_password", user_password);
			PageData user = userService.getOneAdminDataByid(aa);
			Subject subject = SecurityUtils.getSubject();
			subject.isAuthenticated();
			if (map.get("user_phone") != null) {
				UsernamePasswordToken token = new UsernamePasswordToken(user_phone, user_password);
				try {
					subject.login(token);
//                    boolean hasRole = subject.hasRole("yh");
//                    if (hasRole == true) {
//                        //生成token保存
//                        String Retoken = TokenUtils.encoded(user_phone);
//                        redisUtil.del(user_phone);
//                        redisUtil.set(user_phone+"token",Retoken);
//                        Map tokenmap = new HashMap<>();
//                        tokenmap.put("token", Retoken);
//                        list.clear();
//                        list.add(tokenmap);
//                        return hjControllerHelper.result(list, true, "登录成功", 200);
//                    }
					String Retoken = TokenUtils.encoded(user_phone);
					redisUtil.del(user_phone);
					redisUtil.set(user_phone + "token", Retoken);
					PageData pd = new PageData();
					pd.put("roleid", String.valueOf(user.get("role_id")));
					PageData roledata = roleService.getOneRoleDataByid(pd);
					user.put("Retoken", Retoken);
					user.put("rights", roledata.get("rights"));
					user.put("cailiaorights", roledata.getString("cailiaorights"));
					user.put("rengongrights", roledata.getString("rengongrights"));
					user.put("yonghurights", roledata.getString("yonghurights"));
					return hjControllerHelper.result(user, true, "登录成功", 200);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return hjControllerHelper.result(list, false, "密码错误", 403);
		}
	}

	// 注册
	@GetMapping("/getUserInfo")
	@ResponseBody
	// @MyLog(value = "注册记录")
	public Map<?, ?> getUserInfo(@RequestHeader String Authorization) {
		String user_phone = TokenUtils.decoded(Authorization);
		System.out.println(user_phone);
		return hjControllerHelper.result(list, true, "查询個人信息成功", 400);
	}

	// 注册
	@PostMapping("/registerUser")
	@ApiOperation(value = " 注册:传验证码code,手机号user_phone,密码user_password,再次确认密码password,邀请码user_invite,"
			+ "注册完生成一个token返回,前端进行保存", notes = "返回token")
	@ResponseBody
	// @MyLog(value = "注册记录")
	public Map<?, ?> insertuser(@RequestBody Map<Object, Object> map) {
		List<Map<?, ?>> list = new ArrayList<>();
		String code;
		try {
			code = redisUtil.get(map.get("user_phone") + "code").toString();
		} catch (Exception e) {
			return hjControllerHelper.result(list, false, "请获取真实的验证码", 403);
		}
		if (map.get("code").toString().equals(code) && code.toString() != null) {
			if (userService.SelectPhone(map) == null || userService.SelectPhone(map).isEmpty()) {
				if (map.get("user_password").equals(map.get("password"))) {
					String saltSource = RandomStringUtils.random(6, "123456789abcdefghijklmnopqrstuvwxyz");
					map.put("user_saltvalue", saltSource);
					map.put("user_encryption", CreateToken.token(saltSource, map.get("password").toString()));
					map.put("user_name", map.get("user_phone"));
					int a = userService.InsertUser(map);
					if (a == 1) {
						// 生成token保存
						String Retoken = TokenUtils.encoded(map.get("user_phone").toString());
						redisUtil.del(map.get("user_phone").toString());
						redisUtil.set(map.get("user_phone").toString() + "token", Retoken);
						userService.InsertUrole(map);
						Map<String, String> truemap = new HashMap<>();
						truemap.put("token", Retoken);
						userService.InsertWallet(map);
						list.clear();
						list.add(truemap);
						return hjControllerHelper.result(list, true, "账号注册成功", 200);
					}
					return hjControllerHelper.result(list, false, "账号注册失败", 403);
				}
				return hjControllerHelper.result(list, false, "两次密码不正确,请重新输入", 406);
			}
			return hjControllerHelper.result(list, false, "账号已注册", 406);
		}
		return hjControllerHelper.result(list, false, "验证码已失效或验证码不一致", 400);
	}

	// 忘记密码
	@PostMapping("/updatePwd")
	@ResponseBody
	@ApiOperation(value = "修改密码:传验证码code,手机号user_phone,密码user_password," + "可与内部的修改密码用同一个方法" + ",已测试")
	public Map<?, ?> updatepwd(@RequestBody Map<String, String> map) {
		if (userService.SelectPhone(map) == null || userService.SelectPhone(map).isEmpty()) {
			return hjControllerHelper.result(list, false, "手机号未注册", 406);
		} else {
			String code;
			try {
				code = redisUtil.get(map.get("user_phone") + "code").toString();
			} catch (Exception e) {
				return hjControllerHelper.result(list, false, "请获取真实的验证码", 403);
			}
			if (map.get("code").equals(code)) {
				// 加密进行密码修改
				String saltSource = RandomStringUtils.random(6, "123456789abcdefghijklmnopqrstuvwxyz");
				map.put("user_phone", map.get("user_phone"));
				map.put("user_saltvalue", saltSource);
				map.put("user_encryption", CreateToken.token(saltSource, map.get("user_password")));
				int a = userService.Updatepwd(map);
				if (a == 1) {
					return hjControllerHelper.result(list, true, "修改密码成功", 200);
				}
				return hjControllerHelper.result(list, false, "修改密码失败", 403);
			}
			return hjControllerHelper.result(list, false, "验证码已失效或验证码不一致", 403);
		}
	}

	// 获取验证码
	@RequestMapping("/registercode")
	@ApiOperation(value = "获取验证码:通过user_phone获取验证码" + ",已测试", notes = "success")
	@ResponseBody
	@PreventSumbit
	public Map<?, ?> send(@RequestBody Map<String, String> map) {
		String host = "http://dingxintz.market.alicloudapi.com";
		String path = "/dx/notifySms";
		String method = "POST";
		String appcode = "c114071e58a24ec2991bd0f097ecee3e";
		Map<String, String> headers = new HashMap<>();
		// 最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
		headers.put("Authorization", "APPCODE " + appcode);
		String code = Randoms.getcode();
		System.out.println(code);
		Map<String, String> querys = new HashMap<>();
		querys.put("mobile", map.get("user_phone"));
		querys.put("param", "code:" + code);
		querys.put("tpl_id", "TP2103316");
		Map<String, String> bodys = new HashMap<>();
		try {
			// 存入缓存并设置时间
			redisUtil.set(map.get("user_phone") + "code", code, 300);
			HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
			Map<String, Boolean> map1 = new HashMap<>();
			map1.put("success", true);
			list.add(map1);
			return hjControllerHelper.result(list, true, "发送成功", 200);
		} catch (Exception e) {

		}
		Map<String, Boolean> map1 = new HashMap<>();
		map1.put("error", true);
		return hjControllerHelper.result(list, true, "发送失败", 403);
	}

	// 获取验证码
	@PostMapping("/sendLogin")
	@ApiOperation(value = "验证码登录:通过user_phone获取验证码" + ",已测试", notes = "success")
	@ResponseBody
	public Map<?, ?> sendLogin(@RequestBody Map<String, String> map) {
		System.out.println(map);
		String code = redisUtil.get(map.get("user_phone") + "code").toString();
		;
		if (code != null && code.equals(map.get("code"))) {
			if (userService.SelectPhone(map) == null || userService.SelectPhone(map).isEmpty()) {
				return hjControllerHelper.result(list, false, "手机号未注册", 406);
			} else {
				// 生成token保存
				String Retoken = TokenUtils.encoded(map.get("user_phone"));
				redisUtil.del(map.get("user_phone"));
				redisUtil.set(map.get("user_phone") + "token", Retoken);
				Map tokenmap = new HashMap<>();
				tokenmap.put("token", Retoken);
				list.clear();
				list.add(tokenmap);
				return hjControllerHelper.result(Retoken, true, "登录成功", 200);
			}
		}
		list.clear();
		return hjControllerHelper.result(list, false, "验证码已失效或验证码不一致", 401);
	}

	// 退出登录
	@GetMapping("/loginQuit")
	@ApiOperation(value = "退出登录:通过手机号user_phone,user_token", notes = "success or error")
	@ResponseBody
	public Map<?, ?> logindel(@RequestHeader String Authorization) {
		Map<Object, Object> map = new HashMap<>();
		String user_phone = TokenUtils.decoded(Authorization);
		map.put("user_phone", user_phone);
		// 删除缓存
		redisUtil.del(map.get("user_phone") + "token");
		Map<String, Boolean> map1 = new HashMap<>();
		map1.put("success", true);
		list.add(map1);
		return hjControllerHelper.result(list, true, "登录成功", 200);
	}

	// 获取所有用户的信息列表
	@GetMapping("/getAllAdminListPage")
	@ApiOperation(value = "查看系统全部的用户信息", notes = "success or error")
	@ResponseBody
	public Map<?, ?> getAllAdminListPage(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
//		System.out.println("11111111111111111111111111111111111");
//		System.out.println(page.getPageSize());//10
//		System.out.println(page.getPageNo());//0
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> userlist = userService.getAllAdminListPage(page);

		return this.PageDto(200, "查询成功", userlist, page);
	}

	// 根据id查找用户信息
	@GetMapping("/getOneAdminDataByid")
	@ApiOperation(value = "查看系统用户信息根据id查询", notes = "success or error")
	@ResponseBody
	public Map<?, ?> getOneAdminDataByid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		PageData userdata = userService.getOneAdminDataByid(pd);

		return this.ObjectDto(200, "查询成功", userdata);
	}

	// 根据id更新用户信息
	@GetMapping("/updataAdmindataByid")
	@ApiOperation(value = "根据userid更新用户信息", notes = "success or error")
	@ResponseBody
	public Map<?, ?> updataAdmindataByid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		if (pd.getString("updatepassword") != null) {
			System.out.println("撒大苏打撒对等的的");
			String saltSource = RandomStringUtils.random(6, "123456789abcdefghijklmnopqrstuvwxyz");
			pd.put("phonenumber", pd.get("phonenumber"));
			pd.put("user_saltvalue", saltSource);
			pd.put("user_encryption", CreateToken.token(saltSource, pd.get("newpassword").toString()));
			pd.put("password", pd.get("newpassword").toString());
			int a = userService.Updatepwdbyuserphone(pd);
			return this.ObjectDto(200, "更新成功", a);
		} else {
			PageData oneddata = userService.SelectOnebyAdminId(pd);
			if (oneddata != null) {
				if (oneddata.get("user_id").toString().equals(pd.getString("user_id"))
						&& oneddata.getString("phonenumber").equals(pd.getString("phonenumber"))) {
					int a = userService.updataAdmindataByid(pd);
					return this.ObjectDto(200, "更新成功", a);
				} else {

					return this.ObjectDto(201, "更新失败", 1);
				}
			} else {
				int a = userService.updataAdmindataByid(pd);
				return this.ObjectDto(200, "更新成功", a);
			}

		}

	}

	// 根据id删除用户信息
	@GetMapping("/deleteOneAdmindataByid")
	@ApiOperation(value = "根据userid更新用户信息", notes = "success or error")
	@ResponseBody
	public Map<?, ?> deleteOneAdmindataByid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		int a = userService.deleteOneAdmindataByid(pd);
		return this.ObjectDto(200, "删除成功", a);
	}

	// 直接新增用户信息
	@GetMapping("/addAdmindata")
	@ApiOperation(value = "直接新增用户信息", notes = "success or error")
	@ResponseBody
	public Map<?, ?> addAdmindata() {
		PageData map = this.getPageData();
		if (userService.SelectPhone(map) == null || userService.SelectPhone(map).isEmpty()) {
			String saltSource = RandomStringUtils.random(6, "123456789abcdefghijklmnopqrstuvwxyz");
			map.put("user_saltvalue", saltSource);
			map.put("password", "111111");
			map.put("user_encryption", CreateToken.token(saltSource, map.get("password").toString()));
			map.put("user_name", map.get("user_phone"));
			map.put("createtime", new Date());
			map.put("status", "0");
			int a = userService.InsertUser(map);
			if (a == 1) {
				// 生成token保存
				String Retoken = TokenUtils.encoded(map.get("user_phone").toString());
				redisUtil.del(map.get("user_phone").toString());
				redisUtil.set(map.get("user_phone").toString() + "token", Retoken);
				userService.InsertUrole(map);
				Map<String, String> truemap = new HashMap<>();
				truemap.put("token", Retoken);
				userService.InsertWallet(map);
				list.clear();
				list.add(truemap);
				return hjControllerHelper.result(list, true, "新增信息成功", 200);
			} else {
				return hjControllerHelper.result(list, false, "新增信息失败", 406);
			}
		} else {
			return hjControllerHelper.result(list, false, "该手机号已经被注册", 407);
		}
	}

	// 获取所有角色的信息列表
	@GetMapping("/getAllRoleDataListpage")
	@ApiOperation(value = "查看系统全部的角色信息", notes = "success or error")
	@ResponseBody
	public Map<?, ?> getAllRoleDataListPage(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> rolelist = roleService.getAllRoleListPage(page);

		return this.PageDto(200, "查询成功", rolelist, page);
	}

	// 获取所有角色的信息
	@GetMapping("/getAllRoleData")
	@ApiOperation(value = "查看系统全部的角色信息", notes = "success or error")
	@ResponseBody
	public Map<?, ?> getAllRoleData(@RequestHeader String Authorization) {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		List<PageData> rolelist = roleService.getAllRoleData(pd);

		return this.ObjectDto(200, "查询成功", rolelist);
	}

	// 根据id查找对应角色权限信息
	@GetMapping("/getOneRoleDataByid")
	@ApiOperation(value = "查看系统角色信息根据id查询", notes = "success or error")
	@ResponseBody
	public Map<?, ?> getOneRoleDataByid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		PageData roledata = roleService.getOneRoleDataByid(pd);

		return this.ObjectDto(200, "查询成功", roledata);
	}

	// 根据id更新角色信息
	@GetMapping("/updataRoledataByid")
	@ApiOperation(value = "根据roleid更新角色信息", notes = "success or error")
	@ResponseBody
	public Map<?, ?> updataRoledataByid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		pd.put("update_time", new Date());
		int a = roleService.updataRoledataByid1(pd);
		return this.ObjectDto(200, "更新成功", a);

	}

	// 根据id更新角色权限信息
	@PostMapping("/udataRoleRights")
	@ResponseBody
	public Map<?, ?> udataRoleRights(@RequestBody Map<?, ?> map) {
		PageData pd = this.getPageData();

		int a = roleService.udataRoleRights(map);
		return this.ObjectDto(200, "更新成功", a);
	}

	// 根据id更新角色功能权限信息
	@PostMapping("/udataRoleGongNengRights")
	@ResponseBody
	public Map<?, ?> udataRoleGongNengRights(@RequestBody Map<?, ?> map) {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		int a = roleService.udataRoleGongNengRights(map);
		return this.ObjectDto(200, "更新成功", a);
	}

	// 直接新增角色信息
	@GetMapping("/addRoledata")
	@ApiOperation(value = "直接新增角色信息", notes = "success or error")
	@ResponseBody
	public Map<?, ?> addRoledata() {
		PageData map = this.getPageData();
		map.put("create_time", new Date());
		map.put("role_sort", "1");
//		map.put("danjurights",
//				"{\"genzong\": 1, \"caozuojilu\": 1, \"fafangjilu\": 1, \"jiaoyijilu\": 1, \"guigedanwei\": 1, \"jifenshezhi\": 1, \"kaipiaojilu\": 1, \"liulanshuju\": 1, \"yanseguanli\": 1, \"yejibaobiao\": 1, \"pinglunshuju\": 1, \"putongyonghu\": 1, \"renwubiaodan\": 1, \"renwuliushui\": 1, \"shoukuanjilu\": 1, \"yonghuguanli\": 1, \"zhekoushenhe\": 1, \"cailiaodanwei\": 1,\"changshangguanli\": 1, \"cailiaoguanli\": 1, \"canshuliebiao\": 1, \"gongrenyonghu\": 1, \"huiyuanyonghu\": 1, \"kaipiaoshenhe\": 1, \"kaquanliebiao\": 1, \"yinglipaihang\": 1, \"chapingpaihang\": 1, \"cheliangcanshu\": 1, \"dingdanbaobiao\": 1, \"dingdanliebiao\": 1, \"guanggaoshezhi\": 1, \"haopingpaihang\": 1, \"quanxianguanli\": 1, \"cheliangdingdan\": 1, \"gongzhongguanli\": 1, \"shoufeibiaozhun\": 1, \"tuikuanshenqing\": 1, \"xiaoshoubiaodan\": 1,\"xiaoshoufenpei\": 1, \"xiaoshoupaihang\": 1, \"zhekouzongshenhe\": 1, \"rengongdingdan\": 1, \"wuliuxinxi\": 1}");
		map.put("rights",
				"[{\"name\":\"xitong\",\"xitong\":1,\"check\":false,\"children\":[{\"name\":\"yonghuguanli\",\"yonghuguanli\":1,\"children\":[{\"add\":1,\"edit\":1,\"delete\":1}]},{\"name\":\"quanxianguanli\",\"quanxianguanli\":1,\"children\":[{\"add\":1,\"edit\":1,\"delete\":1}]}]},{\"name\":\"yonghu\",\"yonghu\":1,\"check\":false,\"children\":[{\"name\":\"putongyonghu\",\"putongyonghu\":1,\"children\":[{\"add\":1,\"edit\":1,\"delete\":1,\"fenxiao\":1,\"upload\":1,\"import\":1,\"bangding\":1}]},{\"name\":\"gongrenyonghu\",\"gongrenyonghu\":1,\"children\":[{\"add\":1,\"edit\":1,\"delete\":1}]},{\"name\":\"huiyuanyonghu\",\"huiyuanyonghu\":1,\"children\":[{\"edit\":1,\"delete\":1,\"bangding\":1}]},{\"name\":\"jifenshezhi\",\"jifenshezhi\":1}]},{\"name\":\"yewu\",\"yewu\":1,\"check\":false,\"children\":[{\"name\":\"dingdanliebiao\",\"dingdanliebiao\":1,\"children\":[{\"edit\":1,\"import\":1}]},{\"name\":\"rengongdingdan\",\"rengongdingdan\":1,\"children\":[{\"edit\":1,\"import\":1}]},{\"name\":\"wuliuxinxi\",\"wuliuxinxi\":1,\"children\":[{\"add\":1,\"edit\":1,\"delete\":1}]}]},{\"name\":\"shuju\",\"shuju\":1,\"check\":false,\"children\":[{\"name\":\"liulanshuju\",\"liulanshuju\":1},{\"name\":\"pinglunshuju\",\"pinglunshuju\":1},{\"name\":\"renwuliushui\",\"renwuliushui\":1},{\"name\":\"kaipiaojilu\",\"kaipiaojilu\":1}]},{\"name\":\"cailiao\",\"cailiao\":1,\"check\":false,\"children\":[{\"name\":\"cailiaoguanli\",\"cailiaoguanli\":1,\"children\":[{\"add\":1,\"edit\":1,\"delete\":1,\"upload\":1,\"import\":1}]},{\"name\":\"zhixiaocailiaoguanli\",\"zhixiaocailiaoguanli\":1,\"children\":[{\"add\":1,\"edit\":1,\"delete\":1,\"upload\":1,\"import\":1}]},{\"name\":\"changshangxinxi\",\"changshangxinxi\":1,\"children\":[{\"add\":1,\"edit\":1,\"delete\":1,\"upload\":1}]},{\"name\":\"cailiaodanwei\",\"cailiaodanwei\":1,\"children\":[{\"add\":1,\"edit\":1,\"delete\":1}]},{\"name\":\"guigedanwei\",\"guigedanwei\":1,\"children\":[{\"add\":1,\"edit\":1,\"delete\":1}]},{\"name\":\"yanseguanli\",\"yanseguanli\":1,\"children\":[{\"add\":1,\"edit\":1,\"delete\":1}]},{\"name\":\"gongzhongguanli\",\"gongzhongguanli\":1,\"children\":[{\"add\":1,\"edit\":1,\"delete\":1}]}]},{\"name\":\"xiaoshou\",\"xiaoshou\":1,\"check\":false,\"children\":[{\"name\":\"xiaoshoubiaodan\",\"xiaoshoubiaodan\":1,\"children\":[{\"add\":1,\"edit\":1}]},{\"name\":\"xiaoshoufenpei\",\"xiaoshoufenpei\":1,\"children\":[{\"add\":1,\"edit\":1}]},{\"name\":\"genzong\",\"genzong\":1,\"children\":[{\"import\":1}]}]},{\"name\":\"renwu\",\"renwu\":1,\"check\":false,\"children\":[{\"name\":\"renwubiaodan\",\"renwubiaodan\":1,\"children\":[{\"add\":1,\"gongzhong\":1,\"renwulx\":1}]}]},{\"name\":\"kaquan\",\"kaquan\":1,\"check\":false,\"children\":[{\"name\":\"kaquanliebiao\",\"kaquanliebiao\":1,\"children\":[{\"add\":1,\"edit\":1,\"delete\":1}]},{\"name\":\"fafangjilu\",\"fafangjilu\":1,\"children\":[{\"fenfa\":1}]}]},{\"name\":\"canshu\",\"canshu\":1,\"check\":false,\"children\":[{\"name\":\"canshuliebiao\",\"canshuliebiao\":1},{\"name\":\"guanggaoshezhi\",\"guanggaoshezhi\":1,\"children\":[{\"add\":1,\"edit\":1,\"delete\":1}]}]},{\"name\":\"cheliang\",\"cheliang\":1,\"check\":false,\"children\":[{\"name\":\"cheliangcanshu\",\"cheliangcanshu\":1,\"children\":[{\"add\":1,\"edit\":1,\"delete\":1}]},{\"name\":\"shoufeibiaozhun\",\"shoufeibiaozhun\":1},{\"name\":\"cheliangdingdan\",\"cheliangdingdan\":1,\"children\":[{\"import\":1}]}]},{\"name\":\"caiwu\",\"caiwu\":1,\"check\":false,\"children\":[{\"name\":\"jiaoyijilu\",\"jiaoyijilu\":1},{\"name\":\"tuikuanshenqing\",\"tuikuanshenqing\":1,\"children\":[{\"tuikuan\":1,\"tuihuo\":1}]},{\"name\":\"shoukuanjilu\",\"shoukuanjilu\":1},{\"name\":\"kaipiaoshenhe\",\"kaipiaoshenhe\":1,\"children\":[{\"edit\":1}]},{\"name\":\"zhekoushenhe\",\"zhekoushenhe\":1,\"children\":[{\"edit\":1}]}]},{\"name\":\"zongjinban\",\"zongjinban\":1,\"check\":false,\"children\":[{\"name\":\"zhekouzongshenhe\",\"zhekouzongshenhe\":1,\"children\":[{\"edit\":1}]},{\"name\":\"caozuojilu\",\"caozuojilu\":1}]},{\"name\":\"baobiao\",\"baobiao\":1,\"check\":false,\"children\":[{\"name\":\"dingdanbaobiao\",\"dingdanbaobiao\":1,\"children\":[{\"import\":1}]},{\"name\":\"xiaoshoupaihang\",\"xiaoshoupaihang\":1,\"children\":[{\"import\":1}]},{\"name\":\"chapingpaihang\",\"chapingpaihang\":1,\"children\":[{\"import\":1}]},{\"name\":\"haopingpaihang\",\"haopingpaihang\":1,\"children\":[{\"import\":1}]},{\"name\":\"yinglipaihang\",\"yinglipaihang\":1,\"children\":[{\"import\":1}]},{\"name\":\"yejibaobiao\",\"yejibaobiao\":1,\"children\":[{\"import\":1}]}]}]");
		map.put("cailiaorights",
				"[{\"name\": \"order_info_id\", \"show\": 1}, {\"name\": \"order_info_ids\", \"show\": 1}, {\"name\": \"harvest_name\", \"show\": 1}, {\"name\": \"harvest_phone\", \"show\": 1}, {\"name\": \"lirun\", \"show\": 1}, {\"name\": \"order_info_altogether\", \"show\": 1}, {\"name\": \"order_info_moneys\", \"show\": 1}, {\"name\": \"discount\", \"show\": 1}, {\"name\": \"order_info_status\", \"show\": 1}, {\"name\": \"order_info_voicsta\", \"show\": 1}, {\"name\": \"order_info_creatime\", \"show\": 1}, {\"name\": \"harvest_add\", \"show\": 1}, {\"name\": \"harvest_address\", \"show\": 1}, {\"name\": \"coupon_moeny\", \"show\": 1}, {\"name\": \"order_info_payment\", \"show\": 1}, {\"name\": \"order_info_floor\", \"show\": 1}, {\"name\": \"order_info_distance\", \"show\": 1}, {\"name\": \"order_info_elevator\", \"show\": 1}, {\"name\": \"order_info_house\", \"show\": 1}, {\"name\": \"order_info_word\", \"show\": 1}, {\"name\": \"order_info_deliverycretime\", \"show\": 1}]");
		map.put("rengongrights",
				"[{\"name\": \"worker_info_id\", \"show\": 1}, {\"name\": \"worker_name\", \"show\": 1}, {\"name\": \"worker_info_amount\", \"show\": 1}, {\"name\": \"worker_headline\", \"show\": 1}, {\"name\": \"worker_price\", \"show\": 1}, {\"name\": \"worker_info_sta\", \"show\": 1}, {\"name\": \"worker_info_dianhua\", \"show\": 1}, {\"name\": \"worker_info_issuetime\", \"show\": 1}, {\"name\": \"worker_info_creatime\", \"show\": 1}, {\"name\": \"harvest_add\", \"show\": 1}, {\"name\": \"harvest_address\", \"show\": 1}, {\"name\": \"coupon_moeny\", \"show\": 1}, {\"name\": \"order_info_payment\", \"show\": 1}, {\"name\": \"worker_info_word\", \"show\": 1}, {\"name\": \"worker_info_receiving\", \"show\": 1}, {\"name\": \"worker_info_accomplish\", \"show\": 1}, {\"name\": \"worker_info_driver\", \"show\": 1}]");
		map.put("yonghurights",
				"[{\"name\": \"user_id\", \"show\": 1}, {\"name\": \"user_name\", \"show\": 1}, {\"name\": \"user_realname\", \"show\": 1}, {\"name\": \"user_address\", \"show\": 1}, {\"name\": \"user_identity\", \"show\": 1}, {\"name\": \"user_wechat_id\", \"show\": 1}, {\"name\": \"user_wechat_name\", \"show\": 1}, {\"name\": \"user_authentication\", \"show\": 1}, {\"name\": \"user_status\", \"show\": 1}, {\"name\": \"user_creatime\", \"show\": 1}]");
		int a = roleService.InsertRole(map);
		if (a == 1) {
			return hjControllerHelper.result(list, true, "新增信息成功", 200);
		} else {
			return hjControllerHelper.result(list, false, "新增信息失败", 406);
		}
	}

	// 根据id删除角色信息
	@GetMapping("/deleteOneRoledataByid")
	@ApiOperation(value = "根据userid更新用户信息", notes = "success or error")
	@ResponseBody
	public Map<?, ?> deleteOneRoledataByid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		PageData aa = new PageData();
		aa.put("roleid", pd.getString("roleid"));
		List<PageData> rolelist = roleService.getAllRoleListById(aa);
		if (rolelist == null) {
			int a = roleService.deleteOneRoledataByid(pd);
			if (a == 1) {
				return this.ObjectDto(200, "删除成功", a);
			} else {
				return this.ObjectDto(401, "删除失败", a);
			}
		} else {
			return this.ObjectDto(201, "删除失败", 1);
		}

	}

	// 获取所有用户的信息列表
	@GetMapping("/getAllCommonUsersDataListpage")
	@ApiOperation(value = "查看系统全部的用户信息", notes = "success or error")
	@ResponseBody
	public Map<?, ?> getAllCommonUsersDataListpage(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> commonuserlist = commonuserService.getAllCommonUsersDataListpage(page);
		return this.PageDto(200, "查询成功", commonuserlist, page);
	}

	// 根据id查找对应用户权限信息
	@GetMapping("/getOneCommonUserDataByid")
	@ApiOperation(value = "查看系统用户信息根据id查询", notes = "success or error")
	@ResponseBody
	public Map<?, ?> getOneCommonUserDataByid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		PageData commonuserdata = commonuserService.getOneCommonUserDataByid(pd);

		return this.ObjectDto(200, "查询成功", commonuserdata);
	}

	// 根据id更新绑定销售信息
	@GetMapping("/updatesellidbyuserid")
	@ResponseBody
	public Map<?, ?> updatesellidbyuserid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		int a = commonuserService.updatesellidbyuserid(pd);
		return this.ObjectDto(200, "更新成功", a);
	}

	// 根据id更新用户信息
	@GetMapping("/updataCommonuserdataByid")
	@ApiOperation(value = "根据roleid更新角色信息", notes = "success or error")
	@ResponseBody
	public Map<?, ?> updataCommonuserdataByid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		if (pd.getString("updatepassword") != null) {
			String saltSource = RandomStringUtils.random(6, "123456789abcdefghijklmnopqrstuvwxyz");
			pd.put("user_saltvalue", saltSource);
			pd.put("user_encryption", CreateToken.token(saltSource, pd.getString("password")));
			int a = commonuserService.UpdatepwdbyCommonuserid(pd);
			if (a == 1) {
				return hjControllerHelper.result(list, true, "更新信息成功", 200);
			} else {
				return hjControllerHelper.result(list, false, "更新信息失败", 406);
			}
		} else {
			PageData userdata = commonuserService.SelectUserData(pd);
			if (userdata != null) {
				if (userdata.get("user_id").toString().equals(pd.getString("user_id"))
						&& userdata.getString("user_name").equals(pd.getString("user_name"))) {
					int a = commonuserService.updataCommonuserdataByid(pd);
					return this.ObjectDto(200, "更新成功", a);
				} else {
					int a = 1;
					return this.ObjectDto(201, "更新失败,已经被注册", a);
				}
			} else {
				int a = commonuserService.updataCommonuserdataByid(pd);
				return this.ObjectDto(200, "更新成功", a);
			}
		}
	}

	// 直接新增用户信息
	@GetMapping("/addCommonuserdata")
	@ApiOperation(value = "直接新增用户信息", notes = "success or error")
	@ResponseBody
	public Map<?, ?> addCommonuserdata() {
		PageData map = this.getPageData();
		String saltSource = RandomStringUtils.random(6, "123456789abcdefghijklmnopqrstuvwxyz");
		map.put("user_saltvalue", saltSource);
		map.put("user_encryption", CreateToken.token(saltSource, "111111"));
		map.put("user_creatime", new Date());
		if (map.getString("vip") != null) {
			map.put("user_integral", map.getString("number"));
		} else {
			map.put("user_integral", 0);
		}
		if (commonuserService.SelectPhone(map) == null || commonuserService.SelectPhone(map).isEmpty()) {
			int a = commonuserService.addCommonuserdata(map);
			if (a == 1) {
				return hjControllerHelper.result(list, true, "新增信息成功", 200);
			} else {
				return hjControllerHelper.result(list, false, "新增信息失败", 406);
			}
		} else {
			return hjControllerHelper.result(list, false, "该手机号已经被注册", 407);
		}

	}

	// 根据id删除用户信息
	@GetMapping("/deleteOneCommonuserdataByid")
	@ApiOperation(value = "根据userid删除用户信息", notes = "success or error")
	@ResponseBody
	public Map<?, ?> deleteOneCommonuserdataByid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		int a = commonuserService.deleteOneCommonuserdataByid(pd);
		if (a == 1) {
			return this.ObjectDto(200, "删除成功", a);
		} else {
			return this.ObjectDto(406, "删除失败", a);
		}

	}

	// 获取所有工人用户的信息列表
	@GetMapping("/getAllWorkUsersDataListpage")
	@ApiOperation(value = "查看系统全部的用户信息", notes = "success or error")
	@ResponseBody
	public Map<?, ?> getAllWorkUsersDataListpage(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> workuserlist = workUserService.getAllWorkUsersDataListpage(page);
		return this.PageDto(200, "查询成功", workuserlist, page);
	}

	// 查询全部工种的信息详情列表
	@GetMapping("/getALLgongzhongxinxiListPage")
	@ApiOperation(value = "查询全部工种的信息详情列表", notes = "success or error")
	@ResponseBody
	public Map<?, ?> getALLgongzhongxinxiListPage(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> ordersdetaillist = workUserService.getALLgongzhongxinxiListPage(page);
		return this.PageDto(200, "查询成功", ordersdetaillist, page);
	}

	// 单个工种详细信息byid
	@GetMapping("/getgongzhongdatabygongzhongid")
	@ApiOperation(value = "查看对应工人信息根据id查询", notes = "success or error")
	@ResponseBody
	public Map<?, ?> getgongzhongdatabygongzhongid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		PageData workuserdata = workUserService.getgongzhongdatabygongzhongid(pd);

		return this.ObjectDto(200, "查询成功", workuserdata);
	}

	// 根据id查找对应工人用户权限信息
	@GetMapping("/getOneWorkUserDataByid")
	@ApiOperation(value = "查看对应工人信息根据id查询", notes = "success or error")
	@ResponseBody
	public Map<?, ?> getOneWorkUserDataByid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		PageData workuserdata = workUserService.getOneWorkUserDataByid(pd);

		return this.ObjectDto(200, "查询成功", workuserdata);
	}

	// 根据id更新工种信息
	@GetMapping("/updatagongzongdataByid")
	@ApiOperation(value = "根据work_id更新工人信息", notes = "success or error")
	@ResponseBody
	public Map<?, ?> updatagongzongdataByid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);

		workUserService.updatagongzongdataByid(pd);
		return this.ObjectDto(200, "更新成功", 1);

	}

	// 根据id更新工人信息
	@GetMapping("/updataWorkuserdataByid")
	@ApiOperation(value = "根据work_id更新工人信息", notes = "success or error")
	@ResponseBody
	public Map<?, ?> updataWorkuserdataByid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		if (pd.getString("updatepassword") != null) {
			String saltSource = RandomStringUtils.random(6, "123456789abcdefghijklmnopqrstuvwxyz");
			pd.put("user_saltvalue", saltSource);
			pd.put("user_encryption", CreateToken.token(saltSource, pd.getString("password")));
			int a = workUserService.UpdatepwdbyWokerid(pd);
			if (a == 1) {
				return hjControllerHelper.result(list, true, "更新信息成功", 200);
			} else {
				return hjControllerHelper.result(list, false, "更新信息失败", 406);
			}
		} else {
			PageData workerdata = workUserService.SelectWorkerData(pd);
			if (workerdata != null) {
				if (workerdata.get("worker_id").toString().equals(pd.getString("worker_id"))
						&& workerdata.getString("user_phone").equals(pd.getString("user_phone"))) {
					int a = workUserService.updataWorkuserdataByid(pd);
					return this.ObjectDto(200, "更新成功", a);
				} else {
					int a = 1;
					return this.ObjectDto(201, "更新失败,已经被注册", a);
				}
			} else {
				int a = workUserService.updataWorkuserdataByid(pd);
				return this.ObjectDto(200, "更新成功", a);
			}

		}
	}

	// 根据id删除工人用户信息
	@GetMapping("/deleteoneworkuserdataByid")
	@ResponseBody
	public Map<?, ?> deleteoneworkuserdataByid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		int a = workUserService.deleteoneworkuserdataByid(pd);
		if (a == 1) {
			return this.ObjectDto(200, "删除成功", a);
		} else {
			return this.ObjectDto(406, "删除失败", a);
		}

	}

	// 直接新增工人信息
	@GetMapping("/addWorkuserdata")
	@ApiOperation(value = "直接新增工人信息", notes = "success or error")
	@ResponseBody
	public Map<?, ?> addWorkuserdata() {
		PageData map = this.getPageData();
		String saltSource = RandomStringUtils.random(6, "123456789abcdefghijklmnopqrstuvwxyz");
		map.put("user_saltvalue", saltSource);
		map.put("user_encryption", CreateToken.token(saltSource, "111111"));
		map.put("worker_cretime", new Date());
		PageData workerdata = workUserService.SelectWorkerData(map);
		if (workerdata == null || workerdata.isEmpty()) {
			int a = workUserService.addWorkuserdata(map);
			if (a == 1) {
				return hjControllerHelper.result(list, true, "新增信息成功", 200);
			} else {
				return hjControllerHelper.result(list, false, "新增信息失败", 406);
			}
		} else {
			return hjControllerHelper.result(list, false, "该手机号已经被注册", 407);
		}

	}

	// 根据id查找会员积分设置权限信息
	@GetMapping("/getMemberSettingByid")
	@ApiOperation(value = "查看会员积分信息根据id查询", notes = "success or error")
	@ResponseBody
	public Map<?, ?> getMemberSettingByid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		pd.put("id", 1);
		PageData membersetting = commonuserService.getMemberSettingByid(pd);

		return this.ObjectDto(200, "查询成功", membersetting);
	}

	// 根据id更新会员积分门槛信息
	@GetMapping("/updateMemberSettingByid")
	@ApiOperation(value = "根据id更新会员积分信息", notes = "success or error")
	@ResponseBody
	public Map<?, ?> updateMemberSettingByid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		pd.put("id", 1);
		int a = commonuserService.updateMemberSettingByid(pd);
		if (a == 1) {
			return hjControllerHelper.result(list, true, "更新信息成功", 200);
		} else {
			return hjControllerHelper.result(list, false, "更新信息失败", 406);
		}
	}

	// 查询全部商品材料列表
	@GetMapping("/getAllShopGoodsListpage")
	@ApiOperation(value = "获取全部的商品信息", notes = "success or error")
	@ResponseBody
	public Map<?, ?> getAllShopGoodsListpage(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> goodslist = goodsService.getAllShopGoodsListpage(page);
		return this.PageDto(200, "查询成功", goodslist, page);
	}

	// 查询全部商品材料列表
	@GetMapping("/getAllShopZhiXiaoGoodsListpage")
	@ApiOperation(value = "获取全部的商品信息", notes = "success or error")
	@ResponseBody
	public Map<?, ?> getAllShopZhiXiaoGoodsListpage(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> goodslist = goodsService.getAllShopZhiXiaoGoodsListpage(page);
		return this.PageDto(200, "查询成功", goodslist, page);
	}

	// 查询全部cailiao规格列表
	@GetMapping("/getAllShopGoodsSIzeListpage")
	@ResponseBody
	public Map<?, ?> getAllShopGoodsSIzeListpage(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> goodslist = goodsService.getAllShopGoodsSIzeListpage(page);
		return this.PageDto(200, "查询成功", goodslist, page);
	}

	// 根据id删除商品信息
	@GetMapping("/deleteOneShopGoodDataByid")
	@ApiOperation(value = "根据userid删除商品信息", notes = "success or error")
	@ResponseBody
	public Map<?, ?> deleteOneShopGoodDataByid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		int a = goodsService.deleteOneShopGoodDataByid(pd);
		if (a == 1) {
			return this.ObjectDto(200, "删除成功", a);
		} else {
			return this.ObjectDto(406, "删除失败", a);
		}

	}

	// 根据id删除商品规格信息
	@GetMapping("/deleteNewGoodssize")
	@ApiOperation(value = "根据userid删除商品信息", notes = "success or error")
	@ResponseBody
	public Map<?, ?> deleteNewGoodssize() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		int a = goodsService.deleteNewGoodssize(pd);
		if (a == 1) {
			return this.ObjectDto(200, "删除成功", a);
		} else {
			return this.ObjectDto(406, "删除失败", a);
		}

	}

	// 根据id更新材料信息
	@GetMapping("/updataGoodsdataByid")
	@ApiOperation(value = "根据id更新材料信息", notes = "success or error")
	@ResponseBody
	public Map<?, ?> updataGoodsdataByid() {
		PageData pd = this.getPageData();

		pd.put("shop_goods_ifzhixiao", 0);
		logBefore(logger, "数据:" + pd);
		int a = goodsService.updataGoodsdataByid(pd);
		if (a == 1) {
			return hjControllerHelper.result(list, true, "更新信息成功", 200);
		} else {
			return hjControllerHelper.result(list, false, "更新信息失败", 406);
		}
	}

	// 根据id更新材料信息
	@GetMapping("/updataZhiXiaoGoodsdataByid")
	@ApiOperation(value = "根据id更新材料信息", notes = "success or error")
	@ResponseBody
	public Map<?, ?> updataZhiXiaoGoodsdataByid() {
		PageData pd = this.getPageData();

		pd.put("shop_goods_ifzhixiao", 1);
		logBefore(logger, "数据:" + pd);
		int a = goodsService.updataGoodsdataByid(pd);
		if (a == 1) {
			return hjControllerHelper.result(list, true, "更新信息成功", 200);
		} else {
			return hjControllerHelper.result(list, false, "更新信息失败", 406);
		}
	}

	// 直接新增材料信息
	@PostMapping("/addGoodsdata")
	@ApiOperation(value = "直接新增材料信息", notes = "success or error")
	@ResponseBody
	public Map<?, ?> addGoodsdata(@RequestBody Map<?, ?> map) {
		System.out.println("撒大苏打实打实打算");
		System.out.println(map);

		int a = goodsService.addGoodsdata(map);
		if (a == 1) {
			return hjControllerHelper.result(list, true, "新增信息成功", 200);
		} else if (a == 2) {
			return hjControllerHelper.result(list, false, "新增信息失败", 406);
		} else {
			return hjControllerHelper.result(list, false, "新增信息失败", 407);
		}

	}

	// 直接新增材料信息
	@PostMapping("/addZhiXiaoGoodsdata")
	@ApiOperation(value = "直接新增材料信息", notes = "success or error")
	@ResponseBody
	public Map<?, ?> addZhiXiaoGoodsdata(@RequestBody Map<?, ?> map) {
		System.out.println("撒大苏打实打实打算");
		System.out.println(map);

		int a = goodsService.addZhiXiaoGoodsdata(map);
		if (a == 1) {
			return hjControllerHelper.result(list, true, "新增信息成功", 200);
		} else {
			return hjControllerHelper.result(list, false, "新增信息失败", 406);
		}

	}

	// 导入材料信息材料信息
	@PostMapping("/addAllGoodsdata")
	@ApiOperation(value = "批量导入材料信息", notes = "success or error")
	@ResponseBody
	public Map<?, ?> addAllGoodsdata(@RequestBody Map<?, ?> map) {
		PageData pd = this.getPageData();

		System.out.println("xxxxxxxxxxxxxxxxxxx");
		System.out.println(pd);
		System.out.println(map.get("cailiaolist"));
		goodsService.addAllGoodsdata(map);
		return hjControllerHelper.result(list, true, "新增信息成功", 200);

	}

	// 导入材料信息材料信息
	@PostMapping("/addAllZhiXiaoGoodsdata")
	@ApiOperation(value = "批量导入材料信息", notes = "success or error")
	@ResponseBody
	public Map<?, ?> addAllZhiXiaoGoodsdata(@RequestBody Map<?, ?> map) {
		PageData pd = this.getPageData();

		System.out.println("xxxxxxxxxxxxxxxxxxx");
		System.out.println(pd);
		System.out.println(map.get("cailiaolist"));
		goodsService.addAllZhiXiaoGoodsdata(map);
		return hjControllerHelper.result(list, true, "新增信息成功", 200);

	}

	// 导入材料信息厂商信息
	@PostMapping("/addAllSellingdata")
	@ApiOperation(value = "批量导入厂商信息", notes = "success or error")
	@ResponseBody
	public Map<?, ?> addAllSellingdata(@RequestBody Map<?, ?> map) {
		PageData pd = this.getPageData();

		System.out.println("xxxxxxxxxxxxxxxxxxx");
		System.out.println(pd);
		System.out.println(map.get("changshanglist"));
		goodsService.addAllSellingdata(map);
		return hjControllerHelper.result(list, true, "新增信息成功", 200);

	}

	// 查找全部的商品类型
	@GetMapping("/getAllGoodsClass")
	@ApiOperation(value = "查找全部的商品类型", notes = "success or error")
	@ResponseBody
	public Map<?, ?> getAllGoodsClass() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		List<PageData> goodsoneclass = goodsService.getAllGoodsClass(pd);
		List<PageData> goodstwoclass = goodsService.getAllGoodsTwoClass(pd);
		List<PageData> goodsthreeclass = goodsService.getAllGoodsThreeClass(pd);
		PageData alldata = new PageData();
		alldata.put("oneclass", goodsoneclass);
		alldata.put("twoclass", goodstwoclass);
		alldata.put("threeclass", goodsthreeclass);
		return this.ObjectDto(200, "查询成功", alldata);
	}

	// 根据classtype新增类别
	@GetMapping("/addGoodsClassByClassType")
	@ApiOperation(value = "根据classtype新增类别", notes = "success or error")
	@ResponseBody
	public Map<?, ?> addGoodsClassByClassType() {
		PageData pd = this.getPageData();
		if (pd.getString("classtype").equals("0")) {
			pd.put("shop_one_creatime", new Date());
			pd.put("shop_one_status", "1");
			pd.put("shop_one_name", pd.getString("classname"));
			int a = goodsService.addGoodsClassInOneClassify(pd);
			return hjControllerHelper.result(a, true, "新增信息成功", 200);
		} else if (pd.getString("classtype").equals("1")) {

			int a = goodsService.addGoodsClassInTwoClassify(pd);
			return hjControllerHelper.result(a, true, "新增信息成功", 200);

		} else if (pd.getString("classtype").equals("2")) {
			int a = goodsService.addGoodsClassInThreeClassify(pd);
			return hjControllerHelper.result(a, true, "新增信息成功", 200);
		} else {

			return hjControllerHelper.result(1, true, "新增信息成功", 201);
		}

	}

	// 根据classtype更新类别
	@GetMapping("/updateGoodsClassByClassType")
	@ApiOperation(value = "根据classtype更新类别", notes = "success or error")
	@ResponseBody
	public Map<?, ?> updateGoodsClassByClassType() {
		PageData pd = this.getPageData();
		if (pd.getString("classtype").equals("1")) {
			int a = goodsService.updateGoodsClassInOneClassify(pd);
			return hjControllerHelper.result(a, true, "更新信息成功", 200);
		} else if (pd.getString("classtype").equals("2")) {
			int a = goodsService.updateGoodsClassInTwoClassify(pd);
			return hjControllerHelper.result(a, true, "新增信息成功", 200);

		} else if (pd.getString("classtype").equals("3")) {
			int a = goodsService.updateGoodsClassInThreeClassify(pd);
			return hjControllerHelper.result(a, true, "新增信息成功", 200);
		} else {
			return hjControllerHelper.result(1, true, "新增信息成功", 201);
		}

	}

	// 根据classtype删除类别
	@GetMapping("/deleteGoodsClassById")
	@ApiOperation(value = "根据classtype删除类别", notes = "success or error")
	@ResponseBody
	public Map<?, ?> deleteGoodsClassById() {
		PageData pd = this.getPageData();
		if (pd.getString("classtype").equals("1")) {
			int a = goodsService.deleteGoodsClassInOneClassify(pd);
			return hjControllerHelper.result(a, true, "更新信息成功", 200);
		} else if (pd.getString("classtype").equals("2")) {
			int a = goodsService.deleteGoodsClassInTwoClassify(pd);
			return hjControllerHelper.result(a, true, "新增信息成功", 200);

		} else if (pd.getString("classtype").equals("3")) {
			int a = goodsService.deleteGoodsClassInThreeClassify(pd);
			return hjControllerHelper.result(a, true, "新增信息成功", 200);
		} else {
			return hjControllerHelper.result(1, true, "新增信息成功", 201);
		}

	}

	// 根据id查找对应材料信息
	@GetMapping("/getOneShopgooddataByid")
	@ApiOperation(value = "查看对应材料信息根据id查询", notes = "success or error")
	@ResponseBody
	public Map<?, ?> getOneShopgooddataByid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		List<PageData> onegoodssizedata = goodsService.getAllShopgoodSizedataByid(pd);
		PageData onegoodsdata = goodsService.getOneShopgooddataByid(pd);
		PageData aa = this.getPageData();
		aa.put("onegoodsdata", onegoodsdata);
		aa.put("onegoodssizedata", onegoodssizedata);
		return this.ObjectDto(200, "查询成功", aa);
	}

	// 查询全部订单的列表
	@GetMapping("/getAllOrderListpage")
	@ApiOperation(value = "获取全部的订单列表信息", notes = "success or error")
	@ResponseBody
	public Map<?, ?> getAllOrderListpage(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> orderslist = ordersService.getAllOrdersListpage(page);
		return this.PageDto(200, "查询成功", orderslist, page);
	}

	// 查询全部订单的列表
	@GetMapping("/getAllWorkOrderListpage")
	@ApiOperation(value = "获取全部工人的订单列表信息", notes = "success or error")
	@ResponseBody
	public Map<?, ?> getAllWorkOrderListpage(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> workorderslist = ordersService.getAllWorkOrderListpage(page);
		return this.PageDto(200, "查询成功", workorderslist, page);
	}

	// 查询全部订单的详情列表
	@GetMapping("/getOrdersAllDetailsDataById")
	@ApiOperation(value = "获取全部的订单列表信息", notes = "success or error")
	@ResponseBody
	public Map<?, ?> getOrdersAllDetailsDataById(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> ordersdetaillist = ordersService.getOrdersAllDetailsDataById(page);
		return this.PageDto(200, "查询成功", ordersdetaillist, page);
	}

	// 查询全部工人订单的详情列表
	@GetMapping("/getWorkOrdersAllDetailsDataById")
	@ApiOperation(value = "获取全部的工人订单列表信息", notes = "success or error")
	@ResponseBody
	public Map<?, ?> getWorkOrdersAllDetailsDataById(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> ordersdetaillist = ordersService.getWorkOrdersAllDetailsDataById(page);
		return this.PageDto(200, "查询成功", ordersdetaillist, page);
	}

	// 查询全部工人订单的详情列表
	@GetMapping("/getWorkOrdersDataByIdListPage")
	@ApiOperation(value = "获取全部的工人订单列表信息", notes = "success or error")
	@ResponseBody
	public Map<?, ?> getWorkOrdersDataByIdListPage(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> ordersdetaillist = ordersService.getWorkOrdersDataByIdListPage(page);
		return this.PageDto(200, "查询成功", ordersdetaillist, page);
	}

	// 改变订单状态
	@GetMapping("/changeOrderStatusById")
	@ApiOperation(value = "改变订单状态", notes = "success or error")
	@ResponseBody
	public Map<?, ?> changeOrderStatusById() {
		PageData pd = this.getPageData();
		ordersService.changeOrderStatusById(pd);
		return this.ObjectDto(200, "更新成功", 1);
	}

	// 出库全部选中的订单
	@PostMapping("/changeAllOrderStatusByIds")
	@ApiOperation(value = "改变订单状态", notes = "success or error")
	@ResponseBody
	public Map<?, ?> changeAllOrderStatusByIds(@RequestBody Map<?, ?> map) {
		PageData pd = this.getPageData();
		ordersService.changeAllOrderStatusByIds(map);
		return this.ObjectDto(200, "更新成功", 1);
	}

	// 改变订单状态
	@GetMapping("/changeWorkOrderStatusById")
	@ApiOperation(value = "改变订单状态", notes = "success or error")
	@ResponseBody
	public Map<?, ?> changeWorkOrderStatusById() {
		PageData pd = this.getPageData();
		ordersService.changeWorkOrderStatusById(pd);
		return this.ObjectDto(200, "更新成功", 1);
	}

	// 改变订单状态
	@GetMapping("/changeOrderkaipiaoStatusById")
	@ApiOperation(value = "改变订单状态", notes = "success or error")
	@ResponseBody
	public Map<?, ?> changeOrderkaipiaoStatusById() {
		PageData pd = this.getPageData();
		ordersService.changeOrderkaipiaoStatusById(pd);
		return this.ObjectDto(200, "更新成功", 1);

	}

	// 新增收款单
	@GetMapping("/addColletionBillsdata")
	@ResponseBody
	public Map<?, ?> addColletionBillsdata() {
		PageData pd = this.getPageData();
		pd.put("createtime", new Date());
		ordersService.addColletionBillsdata(pd);
		return this.ObjectDto(200, "更新成功", 1);
	}

	// 新增折扣单
	@GetMapping("/addZheKouBillsdata")
	@ResponseBody
	public Map<?, ?> addZheKouBillsdata() {
		PageData pd = this.getPageData();
		pd.put("examine_status", '0');
		pd.put("createtime", new Date());
		ordersService.addZheKouBillsdata(pd);
		return this.ObjectDto(200, "更新成功", 1);

	}

	// 查询全部任务的列表
	@GetMapping("/getAllTakeListpage")
	@ApiOperation(value = "获取全部的任务列表信息", notes = "success or error")
	@ResponseBody
	public Map<?, ?> getAllTakeListpage(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> takeslist = takesService.getAllTakeListpage(page);
		return this.PageDto(200, "查询成功", takeslist, page);
	}

	// 改变项目的状态（已接单未接单和完成切换）
	@GetMapping("/changeTakeStatusById")
	@ApiOperation(value = "改变项目的状态（已接单未接单和完成切换）", notes = "success or error")
	@ResponseBody
	public Map<?, ?> changeTakeStatusById() {
		PageData pd = this.getPageData();
		takesService.changeTakeStatusById(pd);
		return this.ObjectDto(200, "更新成功", 1);

	}

	// 直接新增任务信息
	@GetMapping("/addNewTaskData")
	@ApiOperation(value = "直接新增任务信息", notes = "success or error")
	@ResponseBody
	public Map<?, ?> addNewTaskData() {
		PageData map = this.getPageData();
		map.put("task_info_status", "1");
		map.put("task_info_creatime", new Date());
		int a = takesService.addNewTaskData(map);
		if (a == 1) {
			return hjControllerHelper.result(list, true, "新增信息成功", 200);
		} else {
			return hjControllerHelper.result(list, false, "新增信息失败", 406);
		}

	}

	// 直接修改任务信息
	@GetMapping("/changeSlideShow")
	@ResponseBody
	public Map<?, ?> changeSlideShow() {
		PageData map = this.getPageData();
		int a = publicService.update(map);
		if (a == 1) {
			return hjControllerHelper.result(list, true, "更新成功", 200);
		} else {
			return hjControllerHelper.result(list, false, "更新失败", 406);
		}

	}

	@GetMapping("/addNewGoodssize")
	@ResponseBody
	public Map<?, ?> addNewGoodssize() {
		PageData map = this.getPageData();
		int a = goodsService.addNewGoodssize(map);
		if (a == 1) {
			return hjControllerHelper.result(list, true, "新增信息成功", 200);
		} else {
			return hjControllerHelper.result(list, false, "新增信息失败", 406);
		}

	}

	@GetMapping("/addNewZhiXiaoGoodssize")
	@ResponseBody
	public Map<?, ?> addNewZhiXiaoGoodssize() {
		PageData map = this.getPageData();
		int a = goodsService.addNewZhiXiaoGoodssize(map);
		if (a == 1) {
			return hjControllerHelper.result(list, true, "新增信息成功", 200);
		} else {
			return hjControllerHelper.result(list, false, "新增信息失败", 406);
		}

	}

	@GetMapping("/addSlideShow")
	@ResponseBody
	public Map<?, ?> addSlideShow() {
		PageData map = this.getPageData();
		int a = publicService.addSlideShow(map);
		if (a == 1) {
			return hjControllerHelper.result(list, true, "新增信息成功", 200);
		} else {
			return hjControllerHelper.result(list, false, "新增信息失败", 406);
		}

	}

	// 直接修改材料规格
	@GetMapping("/updateNewGoodssize")
	@ResponseBody
	public Map<?, ?> updateNewGoodssize() {
		PageData map = this.getPageData();
		System.out.println("撒大苏打实打实打算");
		System.out.println(map);
		int a = goodsService.updateNewGoodssize(map);
		if (a == 1) {
			return hjControllerHelper.result(list, true, "更新成功", 200);
		} else {
			return hjControllerHelper.result(list, false, "更新失败", 406);
		}

	}

	// 直接修改材料规格
	@GetMapping("/updateNewZhiXiaoGoodssize")
	@ResponseBody
	public Map<?, ?> updateNewZhiXiaoGoodssize() {
		PageData map = this.getPageData();
		System.out.println("撒大苏打实打实打算");
		System.out.println(map);
		int a = goodsService.updateNewZhiXiaoGoodssize(map);
		if (a == 1) {
			return hjControllerHelper.result(list, true, "更新成功", 200);
		} else {
			return hjControllerHelper.result(list, false, "更新失败", 406);
		}

	}

	@GetMapping("/selectOne")
	@ResponseBody
	public Map<?, ?> selectOne() {
		PageData map = this.getPageData();
		PageData a = publicService.selectOne(map);
		return hjControllerHelper.result(a, true, "成功", 200);
	}

	@GetMapping("/selectAll")
	@ResponseBody
	public Map<?, ?> selectAll() {
		PageData map = this.getPageData();
		List<PageData> a = publicService.selectAll(map);
		return hjControllerHelper.result(a, true, "成功", 200);
	}

	@GetMapping("/selectAllSell")
	@ResponseBody
	public Map<?, ?> selectAllSell(Page page) {
		PageData map = this.getPageData();
		page.setPd(map);
		List<PageData> a = publicService.selectAllSelllistPage(page);
		return hjControllerHelper.result(a, true, "成功", 200);
	}

	@GetMapping("/addSell")
	@ResponseBody
	public Map<?, ?> addSell() {
		PageData map = this.getPageData();
		int a = publicService.addSell(map);
		return hjControllerHelper.result(a, true, "成功", 200);
	}

	@GetMapping("/changeSell")
	@ResponseBody
	public Map<?, ?> changeSell() {
		PageData map = this.getPageData();
		int a = publicService.changeSell(map);
		return hjControllerHelper.result(a, true, "成功", 200);
	}

	// 获取所有差评的信息列表
	@GetMapping("/getAllBadEvaluationListpage")
	@ResponseBody
	public Map<?, ?> getAllBadEvaluationListpage(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> BadEvaluationList = ordersService.getAllBadEvaluationListpage(page);

		return this.PageDto(200, "查询成功", BadEvaluationList, page);
	}

	// 查询全部订单的详情列表通过评论id
	@GetMapping("/getOrdersAllDetailsDataByCommentId")
	@ResponseBody
	public Map<?, ?> getOrdersAllDetailsDataByCommentId(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> ordersdetaillist = ordersService.getOrdersAllDetailsDataByCommentId(page);
		return this.PageDto(200, "查询成功", ordersdetaillist, page);
	}

	// 获取所有订单流水列表
	@GetMapping("/getAllTaskFlowListpage")
	@ResponseBody
	public Map<?, ?> getAllTaskFlowListpage(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> TaskFlowListpage = workerOrdersService.getAllTaskFlowListpage(page);

		return this.PageDto(200, "查询成功", TaskFlowListpage, page);
	}

	// 查询全部订单的详情列表
	@GetMapping("/getWorkderOrdersAllDetailsDataById")
	@ResponseBody
	public Map<?, ?> getWorkderOrdersAllDetailsDataById(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> workerordersdetaillist = workerOrdersService.getWorkderOrdersAllDetailsDataById(page);
		return this.PageDto(200, "查询成功", workerordersdetaillist, page);
	}

	// 改变订单状态
	@GetMapping("/changeWorkerOrderStatusById")
	@ResponseBody
	public Map<?, ?> changeWorkerOrderStatusById() {
		PageData pd = this.getPageData();
		workerOrdersService.changeWorkerOrderStatusById(pd);
		return this.ObjectDto(200, "更新成功", 1);

	}

	// 获取所有订单开票记录列表
	@GetMapping("/getRnvoiceRecordListpage")
	@ResponseBody
	public Map<?, ?> getRnvoiceRecordListpage(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> RecordListpage = recordService.getRnvoiceRecordListpage(page);

		return this.PageDto(200, "查询成功", RecordListpage, page);
	}

	// 获取所有消费记录列表
	@GetMapping("/getWalletRecordListpage")
	@ResponseBody
	public Map<?, ?> getWalletRecordListpage(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> WalletRecordListpage = recordService.getWalletRecordListpage(page);

		return this.PageDto(200, "查询成功", WalletRecordListpage, page);
	}

	// 获取所有退货记录列表
	@GetMapping("/getAllReturnOrderListpage")
	@ResponseBody
	public Map<?, ?> getAllReturnOrderListpage(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> ReturnOrderListpage = ordersService.getAllReturnOrderListpage(page);

		return this.PageDto(200, "查询成功", ReturnOrderListpage, page);
	}

	// 改变退货订单状态
	@GetMapping("/changeReturnOrderStatusById")
	@ResponseBody
	public Map<?, ?> changeReturnOrderStatusById() {
		PageData pd = this.getPageData();
		ordersService.changeReturnOrderStatusById(pd);
		return this.ObjectDto(200, "更新成功", 1);
	}

	// 查找全部的部门类型
	@GetMapping("/getAllDeptClass")
	@ResponseBody
	public Map<?, ?> getAllDeptClass() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		List<PageData> alldept = userService.getAllDeptClass(pd);

		return this.ObjectDto(200, "查询成功", alldept);
	}

	// 直接新增部门信息
	@GetMapping("/addDeptClassData")
	@ResponseBody
	public Map<?, ?> addDeptClassData() {
		PageData map = this.getPageData();
		map.put("create_time", new Date());
		map.put("dept_name", map.getString("classname"));
		map.put("parent_id", Integer.valueOf(map.getString("parentid")));
		int a = userService.addDeptClassData(map);
		if (a == 1) {
			return hjControllerHelper.result(list, true, "新增信息成功", 200);
		} else {
			return hjControllerHelper.result(list, false, "新增信息失败", 406);
		}

	}

	// 修改部门名称
	@GetMapping("/updateDeptClassById")
	@ResponseBody
	public Map<?, ?> updateDeptClassById() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		userService.updateDeptClassById(pd);
		return hjControllerHelper.result(list, true, "新增信息成功", 200);
	}

	// 删除部门名称
	@GetMapping("/deleteDeptClassById")
	@ResponseBody
	public Map<?, ?> deleteDeptClassById() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		userService.deleteDeptClassById(pd);
		return hjControllerHelper.result(list, true, "删除信息成功", 200);
	}

	// 删除部门名称
	@GetMapping("/deleteSlideShow")
	@ResponseBody
	public Map<?, ?> deleteSlideShow() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		publicService.deleteSlideShow(pd);
		return hjControllerHelper.result(list, true, "删除信息成功", 200);
	}

	// 查找全部的用户订单id
	@GetMapping("/getAllUserOrderIds")
	@ResponseBody
	public Map<?, ?> getAllUserOrderIds() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		List<PageData> UserOrder = ordersService.getAllUserOrderIds(pd);

		return this.ObjectDto(200, "查询成功", UserOrder);
	}

	// 查询全部卡券的列表
	@GetMapping("/getAllCardListPage")
	@ResponseBody
	public Map<?, ?> getAllCardListPage(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> cardslist = cardService.getAllCardListPage(page);
		return this.PageDto(200, "查询成功", cardslist, page);
	}

	// 直接新增卡券信息
	@GetMapping("/addCarddata")
	@ResponseBody
	public Map<?, ?> addCarddata() {
		PageData map = this.getPageData();
		logBefore(logger, "数据:" + map);
		map.put("coupon_creatime", new Date());
		map.put("coupon_status", 0);
		int a = cardService.addCarddata(map);
		if (a == 1) {
			return hjControllerHelper.result(list, true, "新增信息成功", 200);
		} else {
			return hjControllerHelper.result(list, false, "新增信息失败", 406);
		}

	}

	// 修改卡卷信息
	@GetMapping("/updataCarddataByid")
	@ResponseBody
	public Map<?, ?> updataCarddataByid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		cardService.updataCarddataByid(pd);
		return hjControllerHelper.result(list, true, "修改卡卷信息成功", 200);
	}

	// 修改卡卷状态信息
	@GetMapping("/updataCardstatusdataByid")
	@ResponseBody
	public Map<?, ?> updataCardstatusdataByid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		cardService.updataCardstatusdataByid(pd);
		return hjControllerHelper.result(list, true, "修改卡卷信息成功", 200);
	}

	// 删除卡卷信息
	@GetMapping("/deleteOneCarddataByid")
	@ResponseBody
	public Map<?, ?> deleteOneCarddataByid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		List<PageData> cardlist = cardService.getAllUserCardList(pd);
		if (cardlist.size() > 0) {
			return hjControllerHelper.result(list, true, "删除信息失败", 201);
		} else {
			cardService.deleteOneCarddataByid(pd);
			return hjControllerHelper.result(list, true, "删除信息成功", 200);
		}

	}

	// 查询全部卡券发放记录的列表
	@GetMapping("/getAllCardDistributionListPage")
	@ResponseBody
	public Map<?, ?> getAllCardDistributionListPage(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> cardslist = cardService.getAllCardDistributionListPage(page);
		return this.PageDto(200, "查询成功", cardslist, page);
	}

	// 新增卡券分发信息
	@GetMapping("/addCardFenfaData")
	@ResponseBody
	public Map<?, ?> addCardFenfaData() {
		PageData map = this.getPageData();
		logBefore(logger, "数据:" + map);
		cardService.addCardFenfaData(map);
		return hjControllerHelper.result(list, true, "新增信息成功", 200);
	}

	// 绑定销售主管信息
	@GetMapping("/updataAllzhuguanid")
	@ResponseBody
	public Map<?, ?> updataAllzhuguanid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		cardService.updataAllzhuguanid(pd);
		return hjControllerHelper.result(list, true, "新增信息成功", 200);
	}

	// 查询全部车辆类型列表
	@GetMapping("/getAllCarParametersDataListpage")
	@ResponseBody
	public Map<?, ?> getAllCarParametersDataListpage(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> vehiclelist = vehicleService.getAllCarParametersDataListpage(page);
		return this.PageDto(200, "查询成功", vehiclelist, page);
	}

	// 直接新增车辆信息
	@GetMapping("/addVechiledata")
	@ResponseBody
	public Map<?, ?> addVechiledata() {
		PageData map = this.getPageData();
		logBefore(logger, "数据:" + map);
		map.put("vehicle_classify_creatime", new Date());
		int a = vehicleService.addVechiledata(map);
		if (a == 1) {
			return hjControllerHelper.result(list, true, "新增信息成功", 200);
		} else {
			return hjControllerHelper.result(list, false, "新增信息失败", 406);
		}

	}

	// 修改卡卷信息
	@GetMapping("/updataVechiledataByid")
	@ResponseBody
	public Map<?, ?> updataVechiledataByid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		vehicleService.updataVechiledataByid(pd);
		return hjControllerHelper.result(list, true, "修改车辆信息成功", 200);
	}

	// 删除车辆信息
	@GetMapping("/deleteonevehicledataByid")
	@ResponseBody
	public Map<?, ?> deleteonevehicledataByid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		vehicleService.deleteonevehicledataByid(pd);
		return hjControllerHelper.result(list, true, "删除信息成功", 200);
	}

	// 获取所有打车订单记录列表
	@GetMapping("/getAllVehicleOrdersListpage")
	@ResponseBody
	public Map<?, ?> getAllVehicleOrdersListpage(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> VehicleOrdersList = vehicleService.getAllVehicleOrdersListpage(page);
		return this.PageDto(200, "查询成功", VehicleOrdersList, page);
	}

	// 获取所有浏览数据列表
	@GetMapping("/getAllBrowseDataListpage")
	@ResponseBody
	public Map<?, ?> getAllBrowseDataListpage(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> BrowseDataList = recordService.getAllBrowseDataListpage(page);
		return this.PageDto(200, "查询成功", BrowseDataList, page);
	}

	// 获取销售排行
	@GetMapping("/getAllSalesRankingAllData")
	@ResponseBody
	public Map<?, ?> getAllSalesRankingAllData() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		List<PageData> SalesRanking = reportService.getAllSalesRankingAllData(pd);
		return this.ObjectDto(200, "查询成功", SalesRanking);
	}

	// 获取差评排行
	@GetMapping("/getAllBadRankingAllData")
	@ResponseBody
	public Map<?, ?> getAllBadRankingAllData() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		List<PageData> BadRanking = reportService.getAllBadRankingAllData(pd);
		return this.ObjectDto(200, "查询成功", BadRanking);
	}

	// 获取有效订单
	@GetMapping("/getAllValidOrderAllData")
	@ResponseBody
	public Map<?, ?> getAllValidOrderAllData() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		List<PageData> ValidOrder = reportService.getAllValidOrderAllData(pd);
		return this.ObjectDto(200, "查询成功", ValidOrder);
	}

	// 获取工人好评排行
	@GetMapping("/getAllWorkersPraiseListpage")
	@ResponseBody
	public Map<?, ?> getAllWorkersPraiseListpage() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		List<PageData> WorkersPraise = reportService.getAllWorkersPraiseListpage(pd);
		return this.ObjectDto(200, "查询成功", WorkersPraise);
	}

	// 开票记录
	@GetMapping("/getAllKaiPiaoDetailsListpage")
	@ResponseBody
	public Map<?, ?> getAllKaiPiaoDetailsListpage() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		List<PageData> KaiPiaoDetail = ordersService.getAllKaiPiaoDetailsListpage(pd);
		return this.ObjectDto(200, "查询成功", KaiPiaoDetail);
	}

	// 获取商品盈利排行
	@GetMapping("/getAllSalesGoodsPriceData")
	@ResponseBody
	public Map<?, ?> getAllSalesGoodsPriceData() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		List<PageData> GoodsPrice = reportService.getAllSalesGoodsPriceData(pd);
		return this.ObjectDto(200, "查询成功", GoodsPrice);
	}

	// 获取全部销售人员
	@GetMapping("/getAllSalesData")
	@ResponseBody
	public Map<?, ?> getAllSalesData() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		List<PageData> Sales = reportService.getAllSalesData(pd);
		return this.ObjectDto(200, "查询成功", Sales);
	}

	// 获取销售人员销售额度排行
	@GetMapping("/getAllSalesPerformanceAllData")
	@ResponseBody
	public Map<?, ?> getAllSalesPerformanceAllData() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		List<PageData> SalesPerformance = reportService.getAllSalesPerformanceAllData(pd);
		return this.ObjectDto(200, "查询成功", SalesPerformance);
	}

	// 根据销售人员获取全部客户
	@GetMapping("/getAllTrackingCustomersAllData")
	@ResponseBody
	public Map<?, ?> getAllTrackingCustomersAllData() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		List<PageData> TrackingCustomers = reportService.getAllTrackingCustomersAllData(pd);
		return this.ObjectDto(200, "查询成功", TrackingCustomers);
	}

	// 根据用户id获取全部订单
	@GetMapping("/getAllOrderByuseridListpage")
	@ResponseBody
	public Map<?, ?> getAllOrderByuseridListpage() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		List<PageData> AllOrderByuseridListpage = reportService.getAllOrderByuseridListpage(pd);
		return this.ObjectDto(200, "查询成功", AllOrderByuseridListpage);
	}

	// 根据商品id获取全部订单
	@GetMapping("/getAllOrderByGoodsidListpage")
	@ResponseBody
	public Map<?, ?> getAllOrderByGoodsidListpage() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		List<PageData> AllOrderByuseridListpage = reportService.getAllOrderByGoodsidListpage(pd);
		return this.ObjectDto(200, "查询成功", AllOrderByuseridListpage);
	}

	// 获取所有材料单位信息列表
	@GetMapping("/getAllUnitsDataListpage")
	@ResponseBody
	public Map<?, ?> getAllUnitsDataListpage(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> UnitsData = unitService.getAllUnitsDataListpage(page);
		return this.PageDto(200, "查询成功", UnitsData, page);
	}

	// 获取所有厂商直销信息列表
	@GetMapping("/getAllSellingDataListpage")
	@ResponseBody
	public Map<?, ?> getAllSellingDataListpage(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> UnitsData = unitService.getAllSellingDataListpage(page);
		return this.PageDto(200, "查询成功", UnitsData, page);
	}

	// 获取所有物流信息列表
	@GetMapping("/getAllLogisticssDataListpage")
	@ResponseBody
	public Map<?, ?> getAllLogisticssDataListpage(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> Logistics = unitService.getAllLogisticssDataListpage(page);
		return this.PageDto(200, "查询成功", Logistics, page);
	}

	// 获取所有材料规格单位信息列表
	@GetMapping("/getAllSizeUnitDataListPage")
	@ResponseBody
	public Map<?, ?> getAllSizeUnitDataListPage(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> UnitsData = unitService.getAllSizeUnitDataListPage(page);
		return this.PageDto(200, "查询成功", UnitsData, page);
	}

	// 获取所有物流信息列表
	@GetMapping("/getAllLogisticsDataListpage")
	@ResponseBody
	public Map<?, ?> getAllLogisticsDataListpage(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> UnitsData = unitService.getAllLogisticssDataListpage(page);
		return this.PageDto(200, "查询成功", UnitsData, page);
	}

	// 直接新增材料单位信息
	@GetMapping("/addunitdata")
	@ResponseBody
	public Map<?, ?> addunitdata() {
		PageData map = this.getPageData();
		logBefore(logger, "数据:" + map);
		map.put("shop_goods_unit_creatime", new Date());
		unitService.addunitdata(map);
		return hjControllerHelper.result(list, true, "新增信息成功", 200);

	}

	// 直接新增厂商信息
	@GetMapping("/addsellingdata")
	@ResponseBody
	public Map<?, ?> addsellingdata() {
		PageData map = this.getPageData();
		logBefore(logger, "数据:" + map);
		map.put("shop_warehouse_creatime", new Date());
		map.put("shop_warehouse_status", 1);
		String chengshi2 = String.valueOf(map.get("city"));
		JSONObject weizhi = GaoDeUtil.getLatAndLng(chengshi2);

		if (weizhi != null) {
			String aa = String.valueOf(weizhi.get("location"));
			if (aa != null) {
				String[] chs = aa.split(",");
				map.put("lon", chs[0]);
				map.put("lat", chs[1]);
			}
		}
		unitService.addsellingdata(map);
		return hjControllerHelper.result(list, true, "新增信息成功", 200);

	}

	// 直接新增材料单位信息
	@GetMapping("/addlogisticsdata")
	@ResponseBody
	public Map<?, ?> addlogisticsdata() {
		PageData map = this.getPageData();
		logBefore(logger, "数据:" + map);
		map.put("shop_order_logistics_createtime", new Date());
		unitService.addlogisticsdata(map);
		return hjControllerHelper.result(list, true, "新增信息成功", 200);

	}

	// 修改厂商信息
	@GetMapping("/updatesellingdatabyid")
	@ResponseBody
	public Map<?, ?> updatesellingdatabyid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		String chengshi2 = pd.getString("city");
		JSONObject weizhi = GaoDeUtil.getLatAndLng(chengshi2);
		if (weizhi != null) {
			String aa = String.valueOf(weizhi.get("location"));
			if (aa != null) {
				String[] chs = aa.split(",");
				pd.put("lon", chs[0]);
				pd.put("lat", chs[1]);
			}
		}
		unitService.updatesellingdatabyid(pd);
		return hjControllerHelper.result(list, true, "修改信息成功", 200);
	}

	// 修改单位信息
	@GetMapping("/updateunitdatabyid")
	@ResponseBody
	public Map<?, ?> updateunitdatabyid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		unitService.updateunitdatabyid(pd);
		return hjControllerHelper.result(list, true, "修改信息成功", 200);
	}

	// 修改物流信息
	@GetMapping("/updatelogisticsdatabyid")
	@ResponseBody
	public Map<?, ?> updatelogisticsdatabyid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		unitService.updatelogisticsdatabyid(pd);
		return hjControllerHelper.result(list, true, "修改信息成功", 200);
	}

	// 删除厂商信息
	@GetMapping("/deletesellingdatabyid")
	@ResponseBody
	public Map<?, ?> deletesellingdatabyid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);

		unitService.deletesellingdatabyid(pd);
		return hjControllerHelper.result(list, true, "删除信息成功", 200);

	}

	// 删除物流信息
	@GetMapping("/deletelogisticsdatabyid")
	@ResponseBody
	public Map<?, ?> deletelogisticsdatabyid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);

		unitService.deletelogisticsdatabyid(pd);
		return hjControllerHelper.result(list, true, "删除信息成功", 200);

	}

	// 删除单位信息
	@GetMapping("/deleteunitdatabyid")
	@ResponseBody
	public Map<?, ?> deleteunitdatabyid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		List<PageData> aa = goodsService.getAllShopGoodsByid(pd);
		if (aa.size() == 0) {
			unitService.deleteunitdatabyid(pd);
			return hjControllerHelper.result(list, true, "删除信息成功", 200);
		} else {
			return hjControllerHelper.result(list, true, "删除信息成功", 201);
		}
	}

	// 直接新增材料规格单位信息
	@GetMapping("/addsizeunitdata")
	@ResponseBody
	public Map<?, ?> addsizeunitdata() {
		PageData map = this.getPageData();
		logBefore(logger, "数据:" + map);
		map.put("shop_specification_unit_creatime", new Date());
		unitService.addsizeunitdata(map);
		return hjControllerHelper.result(list, true, "新增信息成功", 200);

	}

	// 直接新增厂商地址信息
	@GetMapping("/addsellingAddressdata")
	@ResponseBody
	public Map<?, ?> addsellingAddressdata() {
		PageData map = this.getPageData();
		logBefore(logger, "数据:" + map);
		map.put("warehouse_classify_creatime", new Date());
		map.put("warehouse_classify_status", 1);
		unitService.addsellingAddressdata(map);
		return hjControllerHelper.result(list, true, "新增信息成功", 200);

	}

	// 修改规格厂商地址信息
	@GetMapping("/updatesellingAddressdatabyid")
	@ResponseBody
	public Map<?, ?> updatesellingAddressdatabyid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		unitService.updatesellingAddressdatabyid(pd);
		return hjControllerHelper.result(list, true, "修改信息成功", 200);
	}

	// 修改规格单位信息
	@GetMapping("/updatesizeunitdatabyid")
	@ResponseBody
	public Map<?, ?> updatesizeunitdatabyid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		unitService.updatesizeunitdatabyid(pd);
		return hjControllerHelper.result(list, true, "修改信息成功", 200);
	}

	// 删除厂商地址信息
	@GetMapping("/deletesellingAddressdatabyid")
	@ResponseBody
	public Map<?, ?> deletesellingAddressdatabyid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		unitService.deletesellingAddressdatabyid(pd);
		return hjControllerHelper.result(list, true, "删除信息成功", 200);
	}

	// 删除规格单位信息
	@GetMapping("/deletesizeunitdatabyid")
	@ResponseBody
	public Map<?, ?> deletesizeunitdatabyid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		unitService.deletesizeunitdatabyid(pd);
		return hjControllerHelper.result(list, true, "删除信息成功", 200);
	}

	// 获取所有厂商地址信息列表
	@GetMapping("/getAllSellingAddressListpage")
	@ResponseBody
	public Map<?, ?> getAllSellingAddressListpage(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> ColorDataList = unitService.getAllSellingAddressListpage(page);
		return this.PageDto(200, "查询成功", ColorDataList, page);
	}

	// 获取所有材料工种信息列表
	@GetMapping("/getAllWorkTypeDataListpage")
	@ResponseBody
	public Map<?, ?> getAllWorkTypeDataListpage(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> ColorDataList = unitService.getAllWorkTypeDataListpage(page);
		return this.PageDto(200, "查询成功", ColorDataList, page);
	}

	// 获取所有材料颜色信息列表
	@GetMapping("/getAllColorDataListpage")
	@ResponseBody
	public Map<?, ?> getAllColorDataListpage(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> ColorDataList = unitService.getAllColorDataListpage(page);
		return this.PageDto(200, "查询成功", ColorDataList, page);
	}

	// 直接新增材料工种单位信息
	@GetMapping("/addworktypedata")
	@ResponseBody
	public Map<?, ?> addworktypedata() {
		PageData map = this.getPageData();
		logBefore(logger, "数据:" + map);
		map.put("shop_goods_worktype_creatime", new Date());
		unitService.addworktypedata(map);
		return hjControllerHelper.result(list, true, "新增信息成功", 200);

	}

	// 修改材料工种信息
	@GetMapping("/updateworktypedatabyid")
	@ResponseBody
	public Map<?, ?> updateworktypedatabyid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		unitService.updateworktypedatabyid(pd);
		return hjControllerHelper.result(list, true, "修改信息成功", 200);
	}

	// 删除材料工种信息
	@GetMapping("/deleteworktypedatabyid")
	@ResponseBody
	public Map<?, ?> deleteworktypedatabyid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		List<PageData> aa = goodsService.getAllShopGoodsByid(pd);
		if (aa.size() == 0) {
			unitService.deleteworktypedatabyid(pd);
			return hjControllerHelper.result(list, true, "删除信息成功", 200);
		} else {
			return hjControllerHelper.result(list, true, "删除信息失败", 201);
		}
	}

	// 修改颜色信息
	@GetMapping("/updatecolordatabyid")
	@ResponseBody
	public Map<?, ?> updatecolordatabyid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		unitService.updatecolordatabyid(pd);
		return hjControllerHelper.result(list, true, "修改信息成功", 200);
	}

	// 直接新增材料颜色单位信息
	@GetMapping("/addcolordata")
	@ResponseBody
	public Map<?, ?> addcolordata() {
		PageData map = this.getPageData();
		logBefore(logger, "数据:" + map);
		map.put("shop_goods_color_creatime", new Date());
		unitService.addcolordata(map);
		return hjControllerHelper.result(list, true, "新增信息成功", 200);

	}

	// 删除颜色信息
	@GetMapping("/deletecolordatabyid")
	@ResponseBody
	public Map<?, ?> deletecolordatabyid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		List<PageData> aa = goodsService.getAllShopGoodsByid(pd);
		if (aa.size() == 0) {
			unitService.deletecolordatabyid(pd);
			return hjControllerHelper.result(list, true, "删除信息成功", 200);
		} else {
			return hjControllerHelper.result(list, true, "删除信息失败", 201);
		}

	}

	// 获取所有工种信息列表
	@GetMapping("/getAllGongzhongListpage")
	@ResponseBody
	public Map<?, ?> getAllGongzhongListpage(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> GongzhongLis = takesService.getAllGongzhongListpage(page);
		return this.PageDto(200, "查询成功", GongzhongLis, page);
	}

	// 获取所有工种信息列表
	@GetMapping("/getAllGongzhong1Listpage")
	@ResponseBody
	public Map<?, ?> getAllGongzhong1Listpage(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> GongzhongLis = takesService.getAllGongzhong1Listpage(page);
		return this.PageDto(200, "查询成功", GongzhongLis, page);
	}

	// 获取所有工种类型信息列表
	@GetMapping("/getAllgongzhongfenlei")
	@ResponseBody
	public Map<?, ?> getAllgongzhongfenlei(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> GongzhongLis = takesService.getAllgongzhongfenlei(page);
		return this.PageDto(200, "查询成功", GongzhongLis, page);
	}

	// 获取所有任务类型信息列表
	@GetMapping("/getAllLeiXingListpage")
	@ResponseBody
	public Map<?, ?> getAllLeiXingListpage(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> LeiXingList = takesService.getAllLeiXingListpage(page);
		return this.PageDto(200, "查询成功", LeiXingList, page);
	}

	// 直接新增工种信息
	@GetMapping("/addgongzhongdata")
	@ResponseBody
	public Map<?, ?> addgongzhongdata() {
		PageData map = this.getPageData();
		logBefore(logger, "数据:" + map);
		map.put("worker_two_creatime", new Date());
		takesService.addgongzhongdata(map);
		return hjControllerHelper.result(list, true, "新增信息成功", 200);

	}

	// 直接新增工种分类信息
	@GetMapping("/addgongzhongleixingdata")
	@ResponseBody
	public Map<?, ?> addgongzhongleixingdata() {
		PageData map = this.getPageData();
		logBefore(logger, "数据:" + map);
		map.put("worker_one_creatime", new Date());
		map.put("worker_one_status", 1);
		takesService.addgongzhongleixingdata(map);
		return hjControllerHelper.result(list, true, "新增信息成功", 200);

	}

	// 修改工种类型信息
	@GetMapping("/updategongzhongleixingdatabyid")
	@ResponseBody
	public Map<?, ?> updategongzhongleixingdatabyid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		takesService.updategongzhongleixingdatabyid(pd);
		return hjControllerHelper.result(list, true, "修改信息成功", 200);
	}

	// 删除工种类型信息
	@GetMapping("/deleteOnefenleidataByid")
	@ResponseBody
	public Map<?, ?> deleteOnefenleidataByid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		takesService.deleteOnefenleidataByid(pd);
		return hjControllerHelper.result(list, true, "删除信息成功", 200);
	}

	// 直接新增任务类型信息
	@GetMapping("/addleixingdata")
	@ResponseBody
	public Map<?, ?> addleixingdata() {
		PageData map = this.getPageData();
		logBefore(logger, "数据:" + map);
		map.put("task_type_createtime", new Date());
		takesService.addleixingdata(map);
		return hjControllerHelper.result(list, true, "新增信息成功", 200);

	}

	// 修改工种信息
	@GetMapping("/updategongzhongdatabyid")
	@ResponseBody
	public Map<?, ?> updategongzhongdatabyid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		takesService.updategongzhongdatabyid(pd);
		return hjControllerHelper.result(list, true, "修改信息成功", 200);
	}

	// 修改任务类型信息
	@GetMapping("/updateleixingdatabyid")
	@ResponseBody
	public Map<?, ?> updateleixingdatabyid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		takesService.updateleixingdatabyid(pd);
		return hjControllerHelper.result(list, true, "修改信息成功", 200);
	}

	// 删除规格单位信息
	@GetMapping("/deletegongzhongdatabyid")
	@ResponseBody
	public Map<?, ?> deletegongzhongdatabyid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		takesService.deletegongzhongdatabyid(pd);
		return hjControllerHelper.result(list, true, "删除信息成功", 200);
	}

	// 删除任务类型信息
	@GetMapping("/deleteleixingdatabyid")
	@ResponseBody
	public Map<?, ?> deleteleixingdatabyid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		takesService.deleteleixingdatabyid(pd);
		return hjControllerHelper.result(list, true, "删除信息成功", 200);
	}

	// 获取所有订单收款信息列表
	@GetMapping("/getAllCollectionRecordListpage")
	@ResponseBody
	public Map<?, ?> getAllCollectionRecordListpage(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> CollectionRecordList = ordersService.getAllCollectionRecordListpage(page);
		return this.PageDto(200, "查询成功", CollectionRecordList, page);
	}

	// 获取所有订单折扣信息列表
	@GetMapping("/getAllDiscountRecordListpage")
	@ResponseBody
	public Map<?, ?> getAllDiscountRecordListpage(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> DiscountRecord = ordersService.getAllDiscountRecordListpage(page);
		return this.PageDto(200, "查询成功", DiscountRecord, page);
	}

	// 审核折扣通过
	@GetMapping("/shenheDiscountSuccess")
	@ResponseBody
	public Map<?, ?> shenheDiscountSuccess() {
		PageData pd = this.getPageData();
		ordersService.shenheDiscountSuccess(pd);
		return this.ObjectDto(200, "更新成功", 1);

	}

	@GetMapping("/getUseridBySellid")
	@ResponseBody
	public Map<?, ?> getUseridBySellid() {
		PageData pd = this.getPageData();
		PageData onedata = cardService.getUseridBySellid(pd);
		return this.ObjectDto(200, "查询成功", onedata);

	}

	// 根据id查找对应审核
	@GetMapping("/getonediscountrecordbyid")
	@ResponseBody
	public Map<?, ?> getonediscountrecordbyid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		PageData onedata = ordersService.getonediscountrecordbyid(pd);
		return this.ObjectDto(200, "查询成功", onedata);
	}

	// 删除审核单据
	@GetMapping("/deleteshenheDiscountSuccess")
	@ResponseBody
	public Map<?, ?> deleteshenheDiscountSuccess() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		ordersService.deleteshenheDiscountSuccess(pd);
		return hjControllerHelper.result(list, true, "删除信息成功", 200);
	}

	// 根据orderid查出来userdata
	@GetMapping("/getUserDataByOrderid")
	@ResponseBody
	public Map<?, ?> getUserDataByOrderid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		PageData userdata = ordersService.getUserDataByOrderid(pd);
		return this.ObjectDto(200, "查询成功", userdata);
	}

	// 新增用户操作记录
	@GetMapping("/addUserOperationRecorddata")
	@ResponseBody
	public Map<?, ?> addUserOperationRecorddata() {
		PageData pd = this.getPageData();
		ordersService.addUserOperationRecorddata(pd);
		return this.ObjectDto(200, "更新成功", 1);
	}

	// 获取所有用户操作信息列表
	@GetMapping("/getAllUserOperationRecordListpage")
	@ResponseBody
	public Map<?, ?> getAllUserOperationRecordListpage(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> AllUserOperation = userService.getAllUserOperationRecordListpage(page);
		return this.PageDto(200, "查询成功", AllUserOperation, page);
	}

	// 审核折扣通过
	@GetMapping("/changeRoleBySellid")
	@ResponseBody
	public Map<?, ?> changeRoleBySellid() {
		PageData pd = this.getPageData();
		publicService.changeRoleBySellid(pd);
		return this.ObjectDto(200, "更新成功", 1);

	}

	// 根据userid查出来关联得orderid
	@GetMapping("/getAllueridBySellid")
	@ResponseBody
	public Map<?, ?> getAllueridBySellid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		List<PageData> userdata = userService.getUserDataByOrderid(pd);
		return this.ObjectDto(200, "查询成功", userdata);
	}

	// 根据抓管id查出来关联得orderid
	@GetMapping("/getAllueridBySellzhuguanid")
	@ResponseBody
	public Map<?, ?> getAllueridBySellzhuguanid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		List<PageData> userdata = userService.getAllueridBySellzhuguanid(pd);
		if (userdata == null) {
			return this.ObjectDto(200, "查询成功", userdata);
		} else {
			List<PageData> aa = new ArrayList<PageData>();
			for (int i = 0; i < userdata.size(); i++) {
				System.out.println(userdata.get(i));
				PageData nn = this.getPageData();
				nn.put("sellid", String.valueOf(userdata.get(i)));
				List<PageData> bb = userService.getAllUseridBysellid(nn);
				if (bb != null) {
					for (int m = 0; m < bb.size(); m++) {
						aa.add(bb.get(i));
					}
				}
			}
			return this.ObjectDto(200, "查询成功", aa);
		}

	}

	// 修改跟踪信息
	@GetMapping("/addgenzongxinxibyuserid")
	@ResponseBody
	public Map<?, ?> updatagenzongxinxibyuserid() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		pd.put("user_track_createtime", new Date());
		userService.addgenzongxinxibyuserid(pd);
		return hjControllerHelper.result(list, true, "修改信息成功", 200);
	}

	// 获取跟踪信息列表
	@GetMapping("/getAllTrackingDataByUseridListpage")
	@ResponseBody
	public Map<?, ?> getAllTrackingDataByUserid(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		logBefore(logger, "数据:" + pd);
		List<PageData> AllUserOperation = userService.getAllTrackingDataByUseridListpage(page);
		return this.PageDto(200, "查询成功", AllUserOperation, page);
	}

	// 根据userid查出全部绑定该userid的用户
	@GetMapping("/selectAllbangdinguserData")
	@ResponseBody
	public Map<?, ?> selectAllbangdinguserData() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		List<PageData> userdata = userService.selectAllbangdinguserData(pd);
		return this.ObjectDto(200, "查询成功", userdata);
	}

	// 先找到对应的sellid再更换绑定sellid
	@GetMapping("/updataAllSelldataByuserid")
	@ResponseBody
	public Map<?, ?> updataAllSelldataByuserid() {
		PageData pd = this.getPageData();
		userService.updataAllSelldataByuserid(pd);
		return this.ObjectDto(200, "更新成功", 1);

	}

	// 新增用户预约信息
	@GetMapping("/addyuyuexinxi")
	@ResponseBody
	public Map<?, ?> addyuyuexinxi() {
		PageData pd = this.getPageData();
		List<PageData> sameuserdata = userService.selectuserdatabyphone(pd);
		if (sameuserdata != null && !sameuserdata.isEmpty()) {
			return this.ObjectDto(201, "该手机号已经预约啦", 1);
		} else {
			userService.addyuyuexinxi(pd);
			return this.ObjectDto(200, "预约成功", 1);
		}

	}

	// 获取全部图片列表
	@GetMapping("/getAllNewPictures")
	@ResponseBody
	public Map<?, ?> getAllNewPictures() {
		PageData pd = this.getPageData();
		logBefore(logger, "数据:" + pd);
		List<PageData> alldata = userService.getAllNewPictures(pd);
		return this.ObjectDto(200, "查询成功", alldata);
	}

	// 更新图片信息根据id
	@GetMapping("/updateAllNewPicturesByid")
	@ResponseBody
	public Map<?, ?> updateAllNewPicturesByid() {
		PageData pd = this.getPageData();
		userService.updateAllNewPicturesByid(pd);
		return this.ObjectDto(200, "更新成功", 1);
	}

	// 获取云点播录像信息列表
	@GetMapping("/getAllLuXiangList")
	@ResponseBody
	public Map<?, ?> getAllLuXiangList(Page page) {
		PageData pd = this.getPageData();

		String[] args = null;
//		String id = pd.getString("deviceid");

		String aa = getALLData(args, pd);

		return this.ObjectDto(200, "查询成功", aa);
	}

	// 获取云点播录像转码
	@GetMapping("/zhuanmaByFileId")
	@ResponseBody
	public Map<?, ?> zhuanmaByFileId(Page page) {
		PageData pd = this.getPageData();

		String[] args = null;
		String id = pd.getString("FileId");

		String aa = jinxingzhuanma(args, id);// 获取转码任务id之后读取转码成功后的任务信息

		return this.ObjectDto(200, "查询成功", aa);
	}

	// 获取云点播任务详情
	@GetMapping("/huoqurenwuxiangqing")
	@ResponseBody
	public Map<?, ?> huoqurenwuxiangqing(@RequestHeader String Authorization, Page page) {
		PageData pd = this.getPageData();

		String[] args = null;
		String id = pd.getString("taskid");
		String aa = getTaskDetail(args, id);
		return this.ObjectDto(200, "查询成功", aa);
	}

	// 获取全部云点播数据
//	public static void getALLData(String[] args) {
//		try {
//			Credential cred = new Credential("AKIDb091dQAEFA3uUnigCsVt3xay5yhCLRFn",
//					"HJ8xd4HFD1bkfWEiwZBcrthEU0GGBpHW");
//			CvmClient client = new CvmClient(cred, "ap-shanghai");
//
//			DescribeInstancesRequest req = new DescribeInstancesRequest();
//			DescribeInstancesResponse resp = client.DescribeInstances(req);
//			System.out.println("11111111111111111111111111111111111111111111111111111111111");
//			System.out.println(DescribeInstancesResponse.toJsonString(resp));
//		} catch (TencentCloudSDKException e) {
//			System.out.println("22222222222222222222222222222222222222222222222222222222222");
//			System.out.println(e.toString());
//		}
//	}
	public static String getTaskDetail(String[] args, String id) {
		try {
			// 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
			// 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
			Credential cred = new Credential("AKIDb091dQAEFA3uUnigCsVt3xay5yhCLRFn",
					"HJ8xd4HFD1bkfWEiwZBcrthEU0GGBpHW");
			// 实例化一个http选项，可选的，没有特殊需求可以跳过
			HttpProfile httpProfile = new HttpProfile();
			httpProfile.setEndpoint("vod.tencentcloudapi.com");
			// 实例化一个client选项，可选的，没有特殊需求可以跳过
			ClientProfile clientProfile = new ClientProfile();
			clientProfile.setHttpProfile(httpProfile);
			// 实例化要请求产品的client对象,clientProfile是可选的
			VodClient client = new VodClient(cred, "", clientProfile);
			// 实例化一个请求对象,每个接口都会对应一个request对象
			DescribeTaskDetailRequest req = new DescribeTaskDetailRequest();
			req.setTaskId(id);
			// 返回的resp是一个DescribeTaskDetailResponse的实例，与请求对象对应
			DescribeTaskDetailResponse resp = client.DescribeTaskDetail(req);
			// 输出json格式的字符串回包
			System.out.println(DescribeTaskDetailResponse.toJsonString(resp));
			return DescribeTaskDetailResponse.toJsonString(resp);
		} catch (TencentCloudSDKException e) {
			System.out.println(e.toString());
			return e.toString();
		}
	}

	public static String jinxingzhuanma(String[] args, String id) {
		try {
			// 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
			// 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
			Credential cred = new Credential("AKIDb091dQAEFA3uUnigCsVt3xay5yhCLRFn",
					"HJ8xd4HFD1bkfWEiwZBcrthEU0GGBpHW");
			// 实例化一个http选项，可选的，没有特殊需求可以跳过
			HttpProfile httpProfile = new HttpProfile();
			httpProfile.setEndpoint("vod.tencentcloudapi.com");
			// 实例化一个client选项，可选的，没有特殊需求可以跳过
			ClientProfile clientProfile = new ClientProfile();
			clientProfile.setHttpProfile(httpProfile);
			// 实例化要请求产品的client对象,clientProfile是可选的
			VodClient client = new VodClient(cred, "", clientProfile);
			// 实例化一个请求对象,每个接口都会对应一个request对象
			ProcessMediaRequest req = new ProcessMediaRequest();
			req.setFileId(id);
			MediaProcessTaskInput mediaProcessTaskInput1 = new MediaProcessTaskInput();

			TranscodeTaskInput[] transcodeTaskInputs1 = new TranscodeTaskInput[1];
			TranscodeTaskInput transcodeTaskInput1 = new TranscodeTaskInput();
			transcodeTaskInput1.setDefinition(1202989L);
			transcodeTaskInputs1[0] = transcodeTaskInput1;

			mediaProcessTaskInput1.setTranscodeTaskSet(transcodeTaskInputs1);

			req.setMediaProcessTask(mediaProcessTaskInput1);

			// 返回的resp是一个ProcessMediaResponse的实例，与请求对象对应
			ProcessMediaResponse resp = client.ProcessMedia(req);
			// 输出json格式的字符串回包
			System.out.println(ProcessMediaResponse.toJsonString(resp));
			String TaskId = resp.getTaskId();
			return TaskId;
		} catch (TencentCloudSDKException e) {
			System.out.println(e.toString());
			return e.toString();
		}
	}

	public static String getALLData(String[] args, PageData pd) {
		try {
			// 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
			// 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
			Credential cred = new Credential("AKIDb091dQAEFA3uUnigCsVt3xay5yhCLRFn",
					"HJ8xd4HFD1bkfWEiwZBcrthEU0GGBpHW");
			// 实例化一个http选项，可选的，没有特殊需求可以跳过
			HttpProfile httpProfile = new HttpProfile();
			httpProfile.setEndpoint("vod.tencentcloudapi.com");
			// 实例化一个client选项，可选的，没有特殊需求可以跳过
			ClientProfile clientProfile = new ClientProfile();
			clientProfile.setHttpProfile(httpProfile);
			// 实例化要请求产品的client对象,clientProfile是可选的
			VodClient client = new VodClient(cred, "", clientProfile);
			// 实例化一个请求对象,每个接口都会对应一个request对象
			SearchMediaRequest req = new SearchMediaRequest();

			// 返回的resp是一个SearchMediaResponse的实例，与请求对象对应
			String id = pd.getString("deviceid");
			String[] streamIds = { id, id + "1000", "100" + id, id + "100000" };
			System.out.println("sssssssssssssssssssssssss");
			System.out.println(id);
			System.out.println(id + "1000");
			System.out.println("100" + id);
			System.out.println(id + "100000");
			if (!pd.getString("starttime").isEmpty()) {
				TimeRange timeRange1 = new TimeRange();
				timeRange1.setAfter(pd.getString("starttime") + "T16:00:00Z");
				timeRange1.setBefore(pd.getString("endtime") + "T16:00:00Z");
				req.setCreateTime(timeRange1);
			}
			req.setStreamIds(streamIds);
			SearchMediaResponse resp = client.SearchMedia(req);
			// 输出json格式的字符串回包
			System.out.println(SearchMediaResponse.toJsonString(resp));
			return SearchMediaResponse.toJsonString(resp);
		} catch (TencentCloudSDKException e) {
			System.out.println(e.toString());
			return e.toString();
		}
	}

	// 获取所有低于预警线的材料列表
	@GetMapping("/getAllGoodsDataNeedAdd")
	@ResponseBody
	public void getAllGoodsDataNeedAdd() {
		for (int a = 0; a < 10000; a++) {
			try {
				PageData pd = this.getPageData();
				logBefore(logger, "数据:" + pd);
				String content = "";
				List<PageData> AllGoodsDataNeedAdd = goodsService.getAllGoodsDataNeedAdd(pd);
				System.out.println("111111111111111111111");
				System.out.println(AllGoodsDataNeedAdd.size());
				if (AllGoodsDataNeedAdd.size() > 0) {
					for (int i = 0; i < AllGoodsDataNeedAdd.size(); i++) {
						String goodsid = "";
						goodsid = String.valueOf(AllGoodsDataNeedAdd.get(i).get("shop_goods_id")) != null
								? String.valueOf(AllGoodsDataNeedAdd.get(i).get("shop_goods_id"))
								: "";
						String sizeid = "";
						sizeid = String.valueOf(AllGoodsDataNeedAdd.get(i).get("shop_specification_id")) != null
								? String.valueOf(AllGoodsDataNeedAdd.get(i).get("shop_specification_id"))
								: "";
						String shop_goods_name = "";
						shop_goods_name = String.valueOf(AllGoodsDataNeedAdd.get(i).get("shop_goods_name")) != null
								? String.valueOf(AllGoodsDataNeedAdd.get(i).get("shop_goods_name"))
								: "";
						String shop_specification_inventory = "";
						shop_specification_inventory = String
								.valueOf(AllGoodsDataNeedAdd.get(i).get("shop_specification_inventory")) != null
										? String.valueOf(AllGoodsDataNeedAdd.get(i).get("shop_specification_inventory"))
										: "";
						String shop_specification_remind = "";
						shop_specification_remind = String
								.valueOf(AllGoodsDataNeedAdd.get(i).get("shop_specification_remind")) != null
										? String.valueOf(AllGoodsDataNeedAdd.get(i).get("shop_specification_remind"))
										: "";
						content = content + "材料编号：" + goodsid + ",材料规格编号：" + sizeid + ",材料名称" + shop_goods_name
								+ ",当前库存数量" + shop_specification_inventory + ",预警值：" + shop_specification_remind;
					}
					if (!content.equals("")) {
						PageData getEmail = goodsService.selectOneEmail(pd);
						String email = getEmail.getString("email");
						EmailSendUtil.sendEmail(email, "材佳佳预警提示", content);
					}
				}
				Thread.sleep(7200000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
