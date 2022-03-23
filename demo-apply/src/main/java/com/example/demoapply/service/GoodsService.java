package com.example.demoapply.service;

import java.util.List;
import java.util.Map;

import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;

public interface GoodsService {

	List<PageData> getAllShopGoodsListpage(Page page);

	int deleteOneShopGoodDataByid(PageData pd);

	int updataGoodsdataByid(PageData pd);

	int addGoodsdata(Map<?, ?> map);

	PageData getOneShopgooddataByid(PageData pd);

	List<PageData> getAllGoodsClass(PageData pd);

	void addAllGoodsdata(Map<?, ?> map);

	List<PageData> getOneAndTwoRealtionClass(PageData pd);

	List<PageData> getAllGoodsTwoClass(PageData pd);

	List<PageData> getTwoAndThreeRealtionClass(PageData pd);

	List<PageData> getAllGoodsThreeClass(PageData pd);

	int addGoodsClassInOneClassify(PageData pd);

	int addGoodsClassInTwoClassify(PageData pd);

	int addGoodsClassInThreeClassify(PageData pd);

	int updateGoodsClassInOneClassify(PageData pd);

	int updateGoodsClassInTwoClassify(PageData pd);

	int updateGoodsClassInThreeClassify(PageData pd);

	int deleteGoodsClassInOneClassify(PageData pd);

	int deleteGoodsClassInTwoClassify(PageData pd);

	int deleteGoodsClassInThreeClassify(PageData pd);

	List<PageData> getAllShopgoodSizedataByid(PageData pd);

	int updateNewGoodssize(PageData map);

	int addNewGoodssize(PageData map);

	int deleteNewGoodssize(PageData pd);

	List<PageData> getAllShopGoodsByid(PageData pd);

	List<PageData> getAllShopGoodsSIzeListpage(Page page);

	void addPictueData(PageData picturedata);

	List<PageData> getAllGoodsDataNeedAdd(PageData pd);

	PageData selectOneEmail(PageData pd);

	PageData getOnePictureDatabyName(PageData picturedata);

	void updatePictueData(PageData picturedata);

	void addAllSellingdata(Map<?, ?> map);

	List<PageData> getAllShopZhiXiaoGoodsListpage(Page page);

	void addAllZhiXiaoGoodsdata(Map<?, ?> map);

	int addZhiXiaoGoodsdata(Map<?, ?> map);

	int addNewZhiXiaoGoodssize(PageData map);

	int updateNewZhiXiaoGoodssize(PageData map);

}
