package com.example.demoapply.serviceimp;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demoapply.dao.GoodsDao;
import com.example.demoapply.dao.OrdersDao;
import com.example.demoapply.dao.UnitDao;
import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;
import com.example.demoapply.service.GoodsService;

import utils.FileUtil;
import utils.GaoDeUtil;
import utils.UploadFile;

@Service
public class GoodsServiceImp implements GoodsService {

	@Autowired
	GoodsDao goodsDao;
	@Autowired
	OrdersDao ordersDao;
	@Autowired
	UnitDao unitDao;

	@Override
	public List<PageData> getAllShopGoodsListpage(Page page) {
		// TODO Auto-generated method stub
		return goodsDao.getAllShopGoodsListpage(page);
	}

	@Override
	@Transactional
	public int deleteOneShopGoodDataByid(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		String goodsshop_goods_nameid = pd.getString("goodsshop_goods_nameid");
		String bills_type = pd.getString("bills_type");
		aa.put("bills_type", bills_type);
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "删除");
		aa.put("user_operation_record_detail", "删除材料：" + goodsshop_goods_nameid + "的信息");
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		goodsDao.deleteOneShopGoodDataByid(pd);
		goodsDao.deleteAllNewGoodssize(pd);
		return 1;
	}

	@Override
	@Transactional
	public int updataGoodsdataByid(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		String type_id = pd.getString("shop_goods_id");
		String bills_type = pd.getString("bills_type");
		String user_operation_record_detail = pd.getString("user_operation_record_detail");
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("type_id", type_id);
		aa.put("bills_type", bills_type);
		aa.put("user_operation_record_type", "编辑");
		aa.put("user_operation_record_detail", user_operation_record_detail);
		aa.put("user_operation_record_createtime", new Date());
		if (!user_operation_record_detail.equals("")) {
			ordersDao.addUserOperationRecorddata(aa);
		}
		String shop_goods_priority = pd.getString("shop_goods_priority");
		PageData ll = new PageData();
		ll.put("shop_goods_priority", shop_goods_priority);
		ll.put("shop_goods_ifzhixiao", 0);
		PageData alldata = goodsDao.SelectAllGoodsDataByDaima(ll);
		if (alldata != null && !String.valueOf(alldata.get("shop_goods_id")).equals(type_id)) {
			return 2;
		} else {
			goodsDao.updataGoodsdataByid(pd);
			if (!pd.getString("shop_goods_class").equals("no")) {
				String goodsid = pd.getString("shop_goods_id");
				PageData pc = new PageData();
				pc.put("goodsid", goodsid);
				PageData onedata = goodsDao.SeleteOnedataByrelation(pc);
				if (onedata != null) {
					String threeclassid = pd.getString("threeclassid");
					pd.put("threeclassid", threeclassid);
					pd.put("goodsid", goodsid);
					goodsDao.updataGoodsAndThreeRelationData(pd);
				} else {
					String id = pd.getString("shop_goods_id");
					String threeid = pd.getString("threeclassid");
					PageData pa = new PageData();
					pa.put("shop_goods_id", id);
					pa.put("shop_three_id", threeid);
					pa.put("three_goods_creatime", new Date());
					pa.put("three_goods_status", "1");
					goodsDao.addGoodsAndThreeRealtion(pa);
				}
			} else {
				String goodsid = pd.getString("shop_goods_id");
				pd.put("goodsid", goodsid);
				goodsDao.deleteGoodsAndThreeRelationData(pd);
			}
//			JSONArray goodsmessages = JSONArray.parseArray(pd.getString("AllGoodsSize"));
//			for (int a = 0; a < goodsmessages.size(); a++) {
//				JSONArray onegoodssize = goodsmessages.getJSONArray(a);
//				PageData onedata = new PageData();
//				onedata.put("id", pd.get("id"));
//				onedata.put("shop_specification_name", onegoodssize.getJSONObject(a).getString("guigename"));
//				onedata.put("shop_specification_pcsaleprice", onegoodssize.getJSONObject(a).getString("price"));
//				onedata.put("shop_specification_price", onegoodssize.getJSONObject(a).getString("saleprice"));
//				onedata.put("shop_specification_unit", onegoodssize.getJSONObject(a).getString("unit"));
//				onedata.put("shop_specification_inventory", onegoodssize.getJSONObject(a).getString("goodsnumber"));
//				onedata.put("shop_specification_remind", onegoodssize.getJSONObject(a).getString("jinjienumber"));
//				goodsDao.updateGoodsSizeById(onedata);
//			}
//			;
			return 1;
		}

	}

	@Override
	public PageData getOneShopgooddataByid(PageData pd) {
		// TODO Auto-generated method stub
		return goodsDao.getOneShopgooddataByid(pd);
	}

	@Override
	public List<PageData> getAllGoodsClass(PageData pd) {
		// TODO Auto-generated method stub
		return goodsDao.getAllGoodsClass(pd);
	}

	@Override
	public List<PageData> getOneAndTwoRealtionClass(PageData pd) {
		// TODO Auto-generated method stub
		return goodsDao.getOneAndTwoRealtionClass(pd);
	}

	@Override
	public List<PageData> getAllGoodsTwoClass(PageData pd) {
		// TODO Auto-generated method stub
		return goodsDao.getAllGoodsTwoClass(pd);
	}

	@Override
	public List<PageData> getTwoAndThreeRealtionClass(PageData pd) {
		// TODO Auto-generated method stub
		return goodsDao.getTwoAndThreeRealtionClass(pd);
	}

	@Override
	public List<PageData> getAllGoodsThreeClass(PageData pd) {
		// TODO Auto-generated method stub
		return goodsDao.getAllGoodsThreeClass(pd);
	}

	@Override
	@Transactional
	public int addGoodsClassInOneClassify(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "新增");
		String classname = pd.getString("classname");
		aa.put("user_operation_record_detail", "新增了材料一级分类：" + classname);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		goodsDao.addGoodsClassInOneClassify(pd);
		if (Integer.valueOf(pd.getString("shop_goods_ifzhixiao")) == 1) {
			Date b = new Date();
			PageData oneaagoodsclass = new PageData();
			oneaagoodsclass.put("shop_two_creatime", String.valueOf(b));
			oneaagoodsclass.put("shop_two_name", "其他");
			oneaagoodsclass.put("shop_two_status", "1");
			oneaagoodsclass.put("shop_goods_ifzhixiao", Integer.valueOf(pd.getString("shop_goods_ifzhixiao")));
			goodsDao.addGoodsClassInTwoClassify(oneaagoodsclass);

			PageData oneaarelation = new PageData();
			oneaarelation.put("shop_one_id", Integer.valueOf(String.valueOf(pd.get("shop_one_id"))));
			oneaarelation.put("shop_two_id", Integer.valueOf(String.valueOf(oneaagoodsclass.get("shop_two_id"))));
			oneaarelation.put("one_two_creatime", String.valueOf(b));
			oneaarelation.put("ont_two_status", "1");
			goodsDao.addOneRealtionData(oneaarelation);
		}
		return 1;
	}

	@Override
	@Transactional
	public int addGoodsClassInTwoClassify(PageData pd) {
		// TODO Auto-generated method stub
		Date a = new Date();
		PageData onegoodsclass = new PageData();
		onegoodsclass.put("shop_two_creatime", a);
		onegoodsclass.put("shop_two_name", pd.getString("classname"));
		onegoodsclass.put("shop_two_status", "1");
		onegoodsclass.put("shop_goods_ifzhixiao", Integer.valueOf(pd.getString("shop_goods_ifzhixiao")));
		goodsDao.addGoodsClassInTwoClassify(onegoodsclass);

		PageData onerelation = new PageData();
		onerelation.put("shop_one_id", Integer.valueOf(pd.getString("parentid")));
		onerelation.put("shop_two_id", Integer.valueOf(String.valueOf(onegoodsclass.get("shop_two_id"))));
		onerelation.put("one_two_creatime", String.valueOf(a));
		onerelation.put("ont_two_status", "1");
		goodsDao.addOneRealtionData(onerelation);

		if (Integer.valueOf(pd.getString("shop_goods_ifzhixiao")) == 0) {
			Date b = new Date();
			PageData oneaagoodsclass = new PageData();
			oneaagoodsclass.put("shop_three_creatime", String.valueOf(b));
			oneaagoodsclass.put("shop_three_name", "其他");
			oneaagoodsclass.put("shop_three_status", "1");
			oneaagoodsclass.put("shop_goods_ifzhixiao", Integer.valueOf(pd.getString("shop_goods_ifzhixiao")));
			goodsDao.addGoodsClassInThreeClassify(oneaagoodsclass);

			PageData oneaarelation = new PageData();
			oneaarelation.put("shop_two_id", Integer.valueOf(String.valueOf(onegoodsclass.get("shop_two_id"))));
			oneaarelation.put("shop_three_id", Integer.valueOf(String.valueOf(oneaagoodsclass.get("shop_three_id"))));
			oneaarelation.put("two_three_creatime", String.valueOf(a));
			oneaarelation.put("two_three_status", "1");
			goodsDao.addTwoRealtionData(oneaarelation);
		}
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "新增");
		String classname = pd.getString("classname");
		aa.put("user_operation_record_detail", "新增了材料二级分类：" + classname);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		return 1;
	}

	@Override
	@Transactional
	public int addGoodsClassInThreeClassify(PageData pd) {
		// TODO Auto-generated method stub
		Date a = new Date();
		PageData onegoodsclass = new PageData();
		onegoodsclass.put("shop_three_creatime", String.valueOf(a));
		onegoodsclass.put("shop_three_name", pd.getString("classname"));
		onegoodsclass.put("shop_three_status", "1");
		onegoodsclass.put("shop_goods_ifzhixiao", Integer.valueOf(pd.getString("shop_goods_ifzhixiao")));
		goodsDao.addGoodsClassInThreeClassify(onegoodsclass);
		PageData onerelation = new PageData();
		onerelation.put("shop_two_id", Integer.valueOf(pd.getString("parentid")));
		onerelation.put("shop_three_id", Integer.valueOf(String.valueOf(onegoodsclass.get("shop_three_id"))));
		onerelation.put("two_three_creatime", String.valueOf(a));
		onerelation.put("two_three_status", "1");
		goodsDao.addTwoRealtionData(onerelation);

		PageData aa = new PageData();
		String myid = pd.getString("myid");
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "新增");
		String classname = pd.getString("classname");
		aa.put("user_operation_record_detail", "新增了材料三级分类：" + classname);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		return 1;
	}

	@Override
	@Transactional
	public int updateGoodsClassInOneClassify(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "编辑");
		String classname = pd.getString("classname");
		String oldname = pd.getString("oldname");
		String shop_one_price = pd.getString("shop_one_price");
		String oldprice = pd.getString("oldprice");
		aa.put("user_operation_record_detail",
				"编辑了材料一级分类：" + oldname + ",修改为:" + classname + ",修改运费：" + oldprice + ",修改为:" + shop_one_price);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		return goodsDao.updateGoodsClassInOneClassify(pd);
	}

	@Override
	@Transactional
	public int updateGoodsClassInTwoClassify(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "编辑");
		String classname = pd.getString("classname");
		String oldname = pd.getString("oldname");
		aa.put("user_operation_record_detail", "编辑了材料二级分类：" + oldname + ",修改为:" + classname);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		return goodsDao.updateGoodsClassInTwoClassify(pd);
	}

	@Override
	@Transactional
	public int updateGoodsClassInThreeClassify(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "编辑");
		String classname = pd.getString("classname");
		String oldname = pd.getString("oldname");
		aa.put("user_operation_record_detail", "编辑了材料三级分类：" + oldname + ",修改为:" + classname);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		return goodsDao.updateGoodsClassInThreeClassify(pd);
	}

	@Override
	@Transactional
	public int deleteGoodsClassInOneClassify(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "删除");
		String classname = pd.getString("classname");
		aa.put("user_operation_record_detail", "删除了材料一级分类：" + classname);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		return goodsDao.deleteGoodsClassInOneClassify(pd);
	}

	@Override
	@Transactional
	public int deleteGoodsClassInTwoClassify(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "删除");
		String classname = pd.getString("classname");
		aa.put("user_operation_record_detail", "删除了材料二级分类：" + classname);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		return goodsDao.deleteGoodsClassInTwoClassify(pd);
	}

	@Override
	@Transactional
	public int deleteGoodsClassInThreeClassify(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "删除");
		String classname = pd.getString("classname");
		aa.put("user_operation_record_detail", "删除了材料三级分类：" + classname);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		return goodsDao.deleteGoodsClassInThreeClassify(pd);
	}

	@Override
	public List<PageData> getAllShopgoodSizedataByid(PageData pd) {
		// TODO Auto-generated method stub
		return goodsDao.getAllShopgoodSizedataByid(pd);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@Transactional
	public int addGoodsdata(Map map) {
		// TODO Auto-generated method stub
		map.put("shop_goods_priority", String.valueOf(map.get("shop_goods_priority")));
		map.put("shop_goods_ifzhixiao", 0);
		String shop_goods_priority = String.valueOf(map.get("shop_goods_priority"));
		PageData ll = new PageData();
		ll.put("shop_goods_priority", shop_goods_priority);
		ll.put("shop_goods_ifzhixiao", 0);
		PageData alldata = goodsDao.SelectAllGoodsDataByDaima(ll);
		if (alldata != null) {
			return 2;
		} else {
			ArrayList<PageData> pop = new ArrayList();
			if (map.get("AllGoodsSize") != null) {
				JSONArray goodsmessages = JSONArray.parseArray((String) map.get("AllGoodsSize"));
				for (int a = 0; a < goodsmessages.size(); a++) {
					PageData ww = new PageData();
					ww.put("shop_specification_priority", goodsmessages.getJSONObject(a).getString("guigedaima"));
					ll.put("shop_specification_ifzhixiao", 0);
					PageData cunzai = goodsDao.SelectAllGoodsSizeDataByDaima(ww);
					System.out.println("zzzzzzzzzzzzzzzzzzzzz");
					System.out.println(cunzai);
					if (cunzai != null) {
						pop.add(cunzai);
					}

				}
			}
			if (pop.size() > 0) {
				return 3;
			} else {
				goodsDao.addGoodsdata(map);
				PageData aa = new PageData();
				String myid = String.valueOf(map.get("myid"));
				aa.put("user_id", Integer.valueOf(myid));
				String type_id = String.valueOf(map.get("shop_goods_id"));
				String bills_type = String.valueOf(map.get("bills_type"));
				String user_operation_record_detail = String.valueOf(map.get("user_operation_record_detail"));

				aa.put("user_operation_record_type", "新增");
				aa.put("bills_type", bills_type);
				aa.put("type_id", type_id);
				aa.put("user_operation_record_detail", user_operation_record_detail);
				aa.put("user_operation_record_createtime", new Date());
				ordersDao.addUserOperationRecorddata(aa);

				if (!map.get("shop_goods_class").equals("no")) {
					PageData dd = new PageData();
					dd.put("shop_goods_id", map.get("shop_goods_id"));
					dd.put("three_goods_creatime", new Date());
					dd.put("three_goods_status", "1");
					dd.put("shop_three_id", String.valueOf(map.get("threeclassid")));

					goodsDao.addGoodsAndThreeRealtion(dd);
				}
				if (map.get("AllGoodsSize") != null) {
					JSONArray goodsmessages = JSONArray.parseArray((String) map.get("AllGoodsSize"));
					for (int a = 0; a < goodsmessages.size(); a++) {
						PageData pd = new PageData();
						pd.put("shop_goods_id", map.get("shop_goods_id"));
						pd.put("shop_specification_ifzhixiao", 0);
						pd.put("shop_specification_priority", goodsmessages.getJSONObject(a).getString("guigedaima"));
						pd.put("shop_specification_name", goodsmessages.getJSONObject(a).getString("guigename"));
						pd.put("shop_specification_pcsaleprice", goodsmessages.getJSONObject(a).getString("price"));
						pd.put("shop_specification_price", goodsmessages.getJSONObject(a).getString("saleprice"));
						pd.put("shop_specification_unit", goodsmessages.getJSONObject(a).getString("unit"));
						pd.put("shop_specification_inventory", goodsmessages.getJSONObject(a).getString("goodsnumber"));
						pd.put("shop_specification_remind", goodsmessages.getJSONObject(a).getString("jinjienumber"));
						pd.put("shop_specification_creatime", new Date());
						pd.put("shop_specification_status", "1");
						goodsDao.addGoodsSizeById(pd);
					}
				}

				return 1;
			}
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@Transactional
	public int addZhiXiaoGoodsdata(Map map) {
		// TODO Auto-generated method stub
		map.put("shop_goods_priority", String.valueOf(map.get("shop_goods_priority")));
		map.put("shop_goods_ifzhixiao", 1);
		String shop_goods_priority = String.valueOf(map.get("shop_goods_priority"));
		PageData ll = new PageData();
		ll.put("shop_goods_priority", shop_goods_priority);
		ll.put("shop_goods_ifzhixiao", 1);
		PageData alldata = goodsDao.SelectAllGoodsDataByDaima(ll);
		if (alldata != null) {
			return 2;
		} else {
			ArrayList<PageData> pop = new ArrayList();
			if (map.get("AllGoodsSize") != null) {
				JSONArray goodsmessages = JSONArray.parseArray((String) map.get("AllGoodsSize"));
				for (int a = 0; a < goodsmessages.size(); a++) {
					PageData ww = new PageData();
					ww.put("shop_specification_priority", goodsmessages.getJSONObject(a).getString("guigedaima"));
					ww.put("shop_specification_ifzhixiao", 1);
					PageData cunzai = goodsDao.SelectAllGoodsSizeDataByDaima(ww);
					System.out.println("zzzzzzzzzzzzzzzzzzzzz");
					System.out.println(cunzai);
					if (cunzai != null) {
						pop.add(cunzai);
					}

				}
			}
			System.out.println("ssssssssssssssssasdasdasdasda");
			System.out.print(pop);
			if (pop.size() > 0) {
				return 3;
			} else {
				System.out.println("uuuuuuuuuuuuuuuuuuuu");
				goodsDao.addGoodsdata(map);
				PageData aa = new PageData();
				String myid = String.valueOf(map.get("myid"));
				aa.put("user_id", Integer.valueOf(myid));
				String type_id = String.valueOf(map.get("shop_goods_id"));
				String bills_type = String.valueOf(map.get("bills_type"));
				String user_operation_record_detail = String.valueOf(map.get("user_operation_record_detail"));

				aa.put("user_operation_record_type", "新增");
				aa.put("bills_type", bills_type);
				aa.put("type_id", type_id);
				aa.put("user_operation_record_detail", user_operation_record_detail);
				aa.put("user_operation_record_createtime", new Date());
				ordersDao.addUserOperationRecorddata(aa);

				if (!map.get("shop_goods_class").equals("no")) {
					PageData dd = new PageData();
					dd.put("shop_goods_id", map.get("shop_goods_id"));
					dd.put("three_goods_creatime", new Date());
					dd.put("three_goods_status", "1");
					dd.put("shop_three_id", String.valueOf(map.get("threeclassid")));

					goodsDao.addGoodsAndThreeRealtion(dd);
				}
				if (map.get("AllGoodsSize") != null) {
					JSONArray goodsmessages = JSONArray.parseArray((String) map.get("AllGoodsSize"));
					for (int a = 0; a < goodsmessages.size(); a++) {
						PageData pd = new PageData();
						pd.put("shop_goods_id", map.get("shop_goods_id"));
						pd.put("shop_specification_ifzhixiao", 1);
						pd.put("shop_specification_priority", goodsmessages.getJSONObject(a).getString("guigedaima"));
						pd.put("shop_specification_name", goodsmessages.getJSONObject(a).getString("guigename"));
						pd.put("shop_specification_pcsaleprice", goodsmessages.getJSONObject(a).getString("price"));
						pd.put("shop_specification_price", goodsmessages.getJSONObject(a).getString("saleprice"));
						pd.put("shop_specification_unit", goodsmessages.getJSONObject(a).getString("unit"));
						pd.put("shop_specification_inventory", goodsmessages.getJSONObject(a).getString("goodsnumber"));
						pd.put("shop_specification_remind", goodsmessages.getJSONObject(a).getString("jinjienumber"));
						pd.put("shop_specification_creatime", new Date());
						pd.put("shop_specification_status", "1");
						goodsDao.addGoodsSizeById(pd);
					}
				}

				return 1;
			}
		}
	}

	@Override
	@Transactional
	public int updateNewGoodssize(PageData map) {
		// TODO Auto-generated method stub
		PageData ll = new PageData();
		String shop_specification_priority = map.getString("shop_specification_priority");
		ll.put("shop_specification_priority", shop_specification_priority);
		ll.put("shop_specification_ifzhixiao", 0);
		PageData alldata = goodsDao.SelectAllGoodsSizeDataByDaima(ll);
		if (alldata != null && !String.valueOf(alldata.get("shop_specification_id"))
				.equals(map.getString("shop_specification_id"))) {
			return 2;
		} else {
			PageData aa = new PageData();
			String myid = String.valueOf(map.get("myid"));
			aa.put("user_id", Integer.valueOf(myid));
			String type_id = String.valueOf(map.get("shop_specification_id"));
			String bills_type = String.valueOf(map.get("bills_type"));
			String user_operation_record_detail = String.valueOf(map.get("user_operation_record_detail"));
			aa.put("user_operation_record_type", "编辑");
			aa.put("bills_type", bills_type);
			aa.put("type_id", type_id);
			aa.put("user_operation_record_detail", user_operation_record_detail);
			aa.put("user_operation_record_createtime", new Date());
			if (!user_operation_record_detail.equals("")) {
				ordersDao.addUserOperationRecorddata(aa);
			}
			return goodsDao.updateNewGoodssize(map);
		}

	}

	@Override
	@Transactional
	public int updateNewZhiXiaoGoodssize(PageData map) {
		// TODO Auto-generated method stub
		PageData ll = new PageData();
		String shop_specification_priority = map.getString("shop_specification_priority");
		ll.put("shop_specification_priority", shop_specification_priority);
		ll.put("shop_specification_ifzhixiao", 1);
		PageData alldata = goodsDao.SelectAllGoodsSizeDataByDaima(ll);
		if (alldata != null && !String.valueOf(alldata.get("shop_specification_id"))
				.equals(map.getString("shop_specification_id"))) {
			return 2;
		} else {
			PageData aa = new PageData();
			String myid = String.valueOf(map.get("myid"));
			aa.put("user_id", Integer.valueOf(myid));
			String type_id = String.valueOf(map.get("shop_specification_id"));
			String bills_type = String.valueOf(map.get("bills_type"));
			String user_operation_record_detail = String.valueOf(map.get("user_operation_record_detail"));
			aa.put("user_operation_record_type", "编辑");
			aa.put("bills_type", bills_type);
			aa.put("type_id", type_id);
			aa.put("user_operation_record_detail", user_operation_record_detail);
			aa.put("user_operation_record_createtime", new Date());
			if (!user_operation_record_detail.equals("")) {
				ordersDao.addUserOperationRecorddata(aa);
			}
			return goodsDao.updateNewGoodssize(map);
		}

	}

	@Override
	@Transactional
	public int addNewGoodssize(PageData map) {
		// TODO Auto-generated method stub
		PageData ll = new PageData();
		String shop_specification_priority = String.valueOf(map.get("shop_specification_priority"));
		ll.put("shop_specification_ifzhixiao", 0);
		ll.put("shop_specification_priority", shop_specification_priority);
		PageData alldata = goodsDao.SelectAllGoodsSizeDataByDaima(ll);
		if (alldata != null) {
			return 2;
		} else {
			goodsDao.addNewGoodssize(map);
			PageData aa = new PageData();
			String myid = String.valueOf(map.get("myid"));
			aa.put("user_id", Integer.valueOf(myid));
			String type_id = String.valueOf(map.get("shop_specification_id"));
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

	}

	@Override
	@Transactional
	public int addNewZhiXiaoGoodssize(PageData map) {
		// TODO Auto-generated method stub
		PageData ll = new PageData();
		String shop_specification_priority = String.valueOf(map.get("shop_specification_priority"));
		ll.put("shop_specification_ifzhixiao", 1);
		ll.put("shop_specification_priority", shop_specification_priority);
		PageData alldata = goodsDao.SelectAllGoodsSizeDataByDaima(ll);
		if (alldata != null) {
			return 2;
		} else {
			goodsDao.addNewGoodssize(map);
			PageData aa = new PageData();
			String myid = String.valueOf(map.get("myid"));
			aa.put("user_id", Integer.valueOf(myid));
			String type_id = String.valueOf(map.get("shop_specification_id"));
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

	}

	@Override
	@Transactional
	public int deleteNewGoodssize(PageData pd) {
		// TODO Auto-generated method stub
		PageData aa = new PageData();
		String myid = pd.getString("myid");
		String shop_specification_name = pd.getString("shop_specification_name");
		String bills_type = pd.getString("bills_type");
		aa.put("user_id", Integer.valueOf(myid));
		aa.put("user_operation_record_type", "删除");
		aa.put("bills_type", bills_type);
		aa.put("user_operation_record_detail", "删除了规格：" + shop_specification_name);
		aa.put("user_operation_record_createtime", new Date());
		ordersDao.addUserOperationRecorddata(aa);
		return goodsDao.deleteNewGoodssize(pd);
	}

	@SuppressWarnings({ "rawtypes", "null" })
	@Override
	@Transactional
	public void addAllGoodsdata(Map map) {
		JSONArray cailiaolist = JSONArray.parseArray(String.valueOf(map.get("cailiaolist")));

		for (int a = 0; a < cailiaolist.size(); a++) {
			JSONObject onegooddata = cailiaolist.getJSONObject(a);
			PageData onedata = new PageData();
			String linkadress = onegooddata.getString("linkadress");
			PageData mm = new PageData();
			mm.put("keywords", linkadress);
			List<PageData> allpicture = goodsDao.getalldatabygoodsname(mm);

			if (allpicture.size() == 1) {
				String shouyename = String.valueOf(allpicture.get(0).get("url"));
				onedata.put("shop_goods_picture", shouyename);
				onedata.put("shop_goods_pictures", onegooddata.getString("shop_goods_pictures"));

			} else if (allpicture.size() > 1) {

				String shouyename = String.valueOf(allpicture.get(0).get("url"));
				List<String> arr = new ArrayList<String>();
				String lunboname = "";
				for (int i = 0; i < allpicture.size(); i++) {
					if (i > 0 && i < 5) {
//						lunboname = lunboname+String.valueOf(allpicture.get(i).get("url"))+',';
						arr.add(String.valueOf(allpicture.get(i).get("url")));
					}
				}
				lunboname = String.join(",", arr);
				onedata.put("shop_goods_picture", shouyename);
				onedata.put("shop_goods_pictures", lunboname);

			} else {
				onedata.put("shop_goods_picture", onegooddata.getString("shop_goods_picture"));
				onedata.put("shop_goods_pictures", onegooddata.getString("shop_goods_pictures"));
			}
			onedata.put("shop_goods_ifzhixiao", 0);
			onedata.put("shop_goods_priority", onegooddata.getString("shop_goods_priority"));
			onedata.put("shop_goods_brand", onegooddata.getString("shop_goods_brand"));
			onedata.put("shop_goods_class", onegooddata.getString("shop_goods_class"));
			onedata.put("shop_goods_color", onegooddata.getString("shop_goods_color"));
			onedata.put("shop_goods_manufacturers", onegooddata.getString("shop_goods_manufacturers"));
			onedata.put("shop_goods_discount", onegooddata.getString("shop_goods_discount"));
			onedata.put("shop_goods_explain", onegooddata.getString("shop_goods_explain"));
			onedata.put("shop_goods_freight", onegooddata.getString("shop_goods_freight"));
			onedata.put("shop_goods_manufactureradd", onegooddata.getString("shop_goods_manufactureradd"));
			onedata.put("shop_goods_manufacturers", onegooddata.getString("shop_goods_manufacturers"));
			onedata.put("shop_goods_name", onegooddata.getString("shop_goods_name"));
			onedata.put("shop_goods_origin", onegooddata.getString("shop_goods_origin"));
			onedata.put("shop_goods_profession", onegooddata.getString("shop_goods_profession"));
			onedata.put("shop_goods_unit", onegooddata.getString("shop_goods_unit"));

			PageData ll = new PageData();
			String shop_goods_priority = onegooddata.getString("shop_goods_priority");

			ll.put("shop_goods_priority", shop_goods_priority);
			ll.put("shop_goods_ifzhixiao", 0);
			PageData alldata = goodsDao.SelectAllGoodsDataByDaima(ll);

			if (alldata != null) {// 更新
				String shop_goods_id = String.valueOf(alldata.get("shop_goods_id"));
				onedata.put("shop_goods_id", shop_goods_id);
				goodsDao.updataGoodsdataByid(onedata);
				if (!onegooddata.getString("shop_goods_class").equals("no")) {
					String threeid = onegooddata.getString("threeclassid");
					PageData oo = new PageData();
					oo.put("shop_goods_id", String.valueOf(alldata.get("shop_goods_id")));
					oo.put("shop_three_id", threeid);
					oo.put("three_goods_creatime", new Date());
					oo.put("three_goods_status", "1");
					goodsDao.UpdateGoodsAndThreeRealtion(oo);
				}
			} else {// 新增
				goodsDao.addOneGoodsdata(onedata);
				String newid = String.valueOf(onedata.get("shop_goods_id"));
				if (!onegooddata.getString("shop_goods_class").equals("no")) {
					String threeid = onegooddata.getString("threeclassid");
					PageData bb = new PageData();
					bb.put("shop_goods_id", newid);
					bb.put("shop_three_id", threeid);
					bb.put("three_goods_creatime", new Date());
					bb.put("three_goods_status", "1");
					goodsDao.addGoodsAndThreeRealtion(bb);
				}
			}

			JSONArray cailiaoguigelist = JSONArray.parseArray(onegooddata.getString("guigedetail"));
			if (cailiaoguigelist != null) {
				for (int b = 0; b < cailiaoguigelist.size(); b++) {
					String newid = String.valueOf(onedata.get("shop_goods_id"));
					JSONObject onegoodguige = cailiaoguigelist.getJSONObject(b);
					PageData newguige = new PageData();
					newguige.put("shop_goods_id", newid);
					newguige.put("shop_specification_ifzhixiao", 0);
					newguige.put("shop_goods_name", onegoodguige.getString("shop_goods_name"));
					newguige.put("shop_specification_priority", onegoodguige.getString("shop_specification_priority"));
					newguige.put("shop_specification_inventory",
							onegoodguige.getString("shop_specification_inventory"));
					newguige.put("shop_specification_name", onegoodguige.getString("shop_specification_name"));
					newguige.put("shop_specification_pcsaleprice",
							onegoodguige.getString("shop_specification_pcsaleprice"));
					newguige.put("shop_specification_price", onegoodguige.getString("shop_specification_price"));
					newguige.put("shop_specification_remind", onegoodguige.getString("shop_specification_remind"));
					newguige.put("shop_specification_unit", onegoodguige.getString("shop_specification_unit"));
					goodsDao.addOneNewGoodssize(newguige);
				}

			}
		}
		;

	}

	@SuppressWarnings({ "rawtypes", "null" })
	@Override
	@Transactional
	public void addAllZhiXiaoGoodsdata(Map map) {
		JSONArray cailiaolist = JSONArray.parseArray(String.valueOf(map.get("cailiaolist")));

		for (int a = 0; a < cailiaolist.size(); a++) {
			JSONObject onegooddata = cailiaolist.getJSONObject(a);
			PageData onedata = new PageData();
			String linkadress = onegooddata.getString("linkadress");
			PageData mm = new PageData();
			mm.put("keywords", linkadress);
			List<PageData> allpicture = goodsDao.getalldatabygoodsname(mm);

			if (allpicture.size() == 1) {
				String shouyename = String.valueOf(allpicture.get(0).get("url"));
				onedata.put("shop_goods_picture", shouyename);
				onedata.put("shop_goods_pictures", onegooddata.getString("shop_goods_pictures"));

			} else if (allpicture.size() > 1) {

				String shouyename = String.valueOf(allpicture.get(0).get("url"));
				List<String> arr = new ArrayList<String>();
				String lunboname = "";
				for (int i = 0; i < allpicture.size(); i++) {
					if (i > 0 && i < 5) {
//						lunboname = lunboname+String.valueOf(allpicture.get(i).get("url"))+',';
						arr.add(String.valueOf(allpicture.get(i).get("url")));
					}
				}
				lunboname = String.join(",", arr);
				onedata.put("shop_goods_picture", shouyename);
				onedata.put("shop_goods_pictures", lunboname);

			} else {
				onedata.put("shop_goods_picture", onegooddata.getString("shop_goods_picture"));
				onedata.put("shop_goods_pictures", onegooddata.getString("shop_goods_pictures"));
			}
			onedata.put("shop_goods_ifzhixiao", 1);
			onedata.put("shop_goods_priority", onegooddata.getString("shop_goods_priority"));
			onedata.put("shop_goods_brand", onegooddata.getString("shop_goods_brand"));
			onedata.put("shop_goods_class", onegooddata.getString("shop_goods_class"));
			onedata.put("shop_goods_color", onegooddata.getString("shop_goods_color"));
			onedata.put("shop_goods_manufacturers", onegooddata.getString("shop_goods_manufacturers"));
			onedata.put("shop_goods_discount", onegooddata.getString("shop_goods_discount"));
			onedata.put("shop_goods_explain", onegooddata.getString("shop_goods_explain"));
			onedata.put("shop_goods_freight", onegooddata.getString("shop_goods_freight"));
			onedata.put("shop_goods_manufactureradd", onegooddata.getString("shop_goods_manufactureradd"));
			onedata.put("shop_goods_manufacturers", onegooddata.getString("shop_goods_manufacturers"));
			onedata.put("shop_goods_name", onegooddata.getString("shop_goods_name"));
			onedata.put("shop_goods_origin", onegooddata.getString("shop_goods_origin"));
			onedata.put("shop_goods_profession", onegooddata.getString("shop_goods_profession"));
			onedata.put("shop_goods_unit", onegooddata.getString("shop_goods_unit"));

			PageData ll = new PageData();
			String shop_goods_priority = onegooddata.getString("shop_goods_priority");

			ll.put("shop_goods_priority", shop_goods_priority);
			ll.put("shop_goods_ifzhixiao", 1);
			PageData alldata = goodsDao.SelectAllGoodsDataByDaima(ll);

			if (alldata != null) {// 更新
				String shop_goods_id = String.valueOf(alldata.get("shop_goods_id"));
				onedata.put("shop_goods_id", shop_goods_id);
				System.out.println("dddddddddddddddddddddddddddd");
				System.out.println(onedata);
				goodsDao.updataGoodsdataByid(onedata);
				if (!onegooddata.getString("shop_goods_class").equals("no")) {
					String threeid = onegooddata.getString("threeclassid");
					PageData oo = new PageData();
					oo.put("shop_goods_id", String.valueOf(alldata.get("shop_goods_id")));
					oo.put("shop_three_id", threeid);
					oo.put("three_goods_creatime", new Date());
					oo.put("three_goods_status", "1");
					goodsDao.UpdateGoodsAndThreeRealtion(oo);
				}
			} else {// 新增
				goodsDao.addOneGoodsdata(onedata);
				String newid = String.valueOf(onedata.get("shop_goods_id"));
				if (!onegooddata.getString("shop_goods_class").equals("no")) {
					String threeid = onegooddata.getString("threeclassid");
					PageData bb = new PageData();
					bb.put("shop_goods_id", newid);
					bb.put("shop_three_id", threeid);
					bb.put("three_goods_creatime", new Date());
					bb.put("three_goods_status", "1");
					goodsDao.addGoodsAndThreeRealtion(bb);
				}
			}

			JSONArray cailiaoguigelist = JSONArray.parseArray(onegooddata.getString("guigedetail"));
			if (cailiaoguigelist != null) {
				for (int b = 0; b < cailiaoguigelist.size(); b++) {
					String newid = String.valueOf(onedata.get("shop_goods_id"));
					JSONObject onegoodguige = cailiaoguigelist.getJSONObject(b);
					PageData newguige = new PageData();
					newguige.put("shop_goods_id", newid);
					newguige.put("shop_specification_ifzhixiao", 1);
					newguige.put("shop_goods_name", onegoodguige.getString("shop_goods_name"));
					newguige.put("shop_specification_priority", onegoodguige.getString("shop_specification_priority"));
					newguige.put("shop_specification_inventory",
							onegoodguige.getString("shop_specification_inventory"));
					newguige.put("shop_specification_name", onegoodguige.getString("shop_specification_name"));
					newguige.put("shop_specification_pcsaleprice",
							onegoodguige.getString("shop_specification_pcsaleprice"));
					newguige.put("shop_specification_price", onegoodguige.getString("shop_specification_price"));
					newguige.put("shop_specification_remind", onegoodguige.getString("shop_specification_remind"));
					newguige.put("shop_specification_unit", onegoodguige.getString("shop_specification_unit"));
					goodsDao.addOneNewGoodssize(newguige);
				}

			}
		}
		;
	}

	@Override
	public List<PageData> getAllShopGoodsByid(PageData pd) {
		// TODO Auto-generated method stub
		return goodsDao.getAllShopGoodsByid(pd);
	}

	private static List<File> readFile(String fileDir) {
		List<File> fileList = new ArrayList<File>();
		File file = new File(fileDir);
		File[] files = file.listFiles();
		if (files == null) {
			return null;
		}
		for (File f : files) {
			if (f.isFile()) {
				fileList.add(f);
			} else if (f.isDirectory()) {
				System.out.println(f.getAbsolutePath());
				readFile(f.getAbsolutePath());
			}
		}
		for (File f1 : fileList) {
			System.out.println(f1.getName());
		}
		return fileList;
	}

	public static String listToString1(List list, char separator) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i)).append(separator);
		}
		return list.isEmpty() ? "" : sb.toString().substring(0, sb.toString().length() - 1);
	}

	@Override
	public List<PageData> getAllShopGoodsSIzeListpage(Page page) {
		// TODO Auto-generated method stub
		return goodsDao.getAllShopGoodsSIzeListpage(page);
	}

	@Override
	@Transactional
	public void addPictueData(PageData picturedata) {
		// TODO Auto-generated method stub
		goodsDao.addPictueData(picturedata);
	}

	@Override
	public List<PageData> getAllGoodsDataNeedAdd(PageData pd) {
		// TODO Auto-generated method stub
		return goodsDao.getAllGoodsDataNeedAdd(pd);
	}

	@Override
	public PageData selectOneEmail(PageData pd) {
		// TODO Auto-generated method stub
		return goodsDao.selectOneEmail(pd);
	}

	@Override
	public PageData getOnePictureDatabyName(PageData picturedata) {
		// TODO Auto-generated method stub
		return goodsDao.getOnePictureDatabyName(picturedata);
	}

	@Override
	@Transactional
	public void updatePictueData(PageData picturedata) {
		// TODO Auto-generated method stub
		goodsDao.updatePictueData(picturedata);
	}

	public String getSn(String table, Object companyid, String type) {
		PageData pa = new PageData();
		pa.put("table", table);
		pa.put("type", type);
		pa.put("companyid", companyid);
		pa.put("createtime", new Date());
		Integer i = goodsDao.getSn(pa);
//		System.out.println(i + "ssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
		// 新品
		String num = (i + 1) + "";

		Date date = new Date();

		Calendar c = Calendar.getInstance();

		c.setTime(date);
		String year = c.get(Calendar.YEAR) + "";

		String month = (c.get(Calendar.MONTH) + 1) + "";
		String day = c.get(Calendar.DATE) + "";
		return year + month + day + (num.length() == 1 ? ("0" + num) : num);
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional
	public void addAllSellingdata(Map map) {
		// TODO Auto-generated method stub
		JSONArray changshanglist = JSONArray.parseArray(String.valueOf(map.get("changshanglist")));

		for (int a = 0; a < changshanglist.size(); a++) {
			JSONObject onegooddata = changshanglist.getJSONObject(a);
			PageData onedata = new PageData();
			String linkadress = onegooddata.getString("linkadress");
			PageData mm = new PageData();
			mm.put("keywords", linkadress);
			List<PageData> allpicture = goodsDao.getalldatabygoodsname(mm);
			if (allpicture.size() == 1) {
				String shouyename = String.valueOf(allpicture.get(0).get("url"));
				onedata.put("shop_warehouse_manupicture", shouyename);

			} else if (allpicture.size() > 1) {

				String shouyename = String.valueOf(allpicture.get(0).get("url"));
				onedata.put("shop_warehouse_manupicture", shouyename);
			} else {
				onedata.put("shop_warehouse_manupicture", onegooddata.getString("shop_warehouse_manupicture"));
			}
			onedata.put("shop_warehouse_name", onegooddata.getString("shop_warehouse_name"));
			onedata.put("shop_warehouse_status", 1);
			onedata.put("shop_warehouse_creatime", new Date());
			String chengshi2 = onegooddata.getString("city");
			JSONObject weizhi = GaoDeUtil.getLatAndLng(chengshi2);

			if (weizhi != null) {
				String aa = String.valueOf(weizhi.get("location"));
				if (aa != null) {
					String[] chs = aa.split(",");
					onedata.put("lon", chs[0]);
					onedata.put("lat", chs[1]);
				}
			}
			unitDao.addsellingdata(onedata);

			String newid = String.valueOf(onedata.get("shop_warehouse_id"));

			JSONArray cailiaoguigelist = JSONArray.parseArray(onegooddata.getString("dizhidetail"));
			if (cailiaoguigelist != null) {
				for (int b = 0; b < cailiaoguigelist.size(); b++) {
					JSONObject onegoodguige = cailiaoguigelist.getJSONObject(b);
					PageData newguige = new PageData();
					newguige.put("shop_warehouse_id", newid);
					newguige.put("warehouse_classify_name", onegoodguige.getString("warehouse_classify_name"));
					newguige.put("warehouse_classify_status", 1);
					newguige.put("warehouse_classify_creatime", new Date());

					unitDao.addsellingAddressdata(newguige);
				}

			}
		}
		;
	}

	@Override
	public List<PageData> getAllShopZhiXiaoGoodsListpage(Page page) {
		// TODO Auto-generated method stub
		return goodsDao.getAllShopZhiXiaoGoodsListpage(page);
	}
}
