package com.example.demoapply.controller;

import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;
import com.example.demoapply.redis.RedisUtil;
import com.example.demoapply.service.RoleService;
import com.example.demoapply.service.UserService;
import com.example.demoapply.shiro.CreateToken;
import com.example.democommon.annotation.PreventSumbit;
import com.example.democommon.unit.HttpUtils;
import com.example.democommon.unit.Randoms;
import com.example.democommon.unit.TokenUtils;
import com.example.democommon.unitedreturn.HjControllerHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@ComponentScan(basePackages = { "controller" })
@RequestMapping("/api/role")
@Api(value = "APP运行的controller")
public class RoleControlle extends BaseController {
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	RoleService roleService;
	
	// 获取所有角色的信息列表
		@GetMapping("/getAllRoleDataListpage")
		@ApiOperation(value = "查看系统全部的角色信息", notes = "success or error")
		@ResponseBody
		public Map<?, ?> getAllRoleDataListPage(@RequestHeader String Authorization,Page page) {
//	        Map<Object,Object> map = new HashMap<>();
			PageData pd = this.getPageData();
	    	page.setPd(pd);
			logBefore(logger, "数据:" + pd);
	        List<PageData> rolelist = roleService.getAllRoleListPage(page);

			return this.PageDto(200,"查询成功",rolelist,page);
		}
}
