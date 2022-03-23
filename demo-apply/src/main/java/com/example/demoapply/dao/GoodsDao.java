package com.example.demoapply.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;


import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;

@Mapper
public interface GoodsDao {

	int getAllShopGoodsListpage(PageData pd);

	List<PageData> getAllShopGoodsListpage(Page page);

	int deleteOneShopGoodDataByid(PageData pd);

	int updataGoodsdataByid(PageData pd);

	int addGoodsdata(Map<?, ?> map);

	PageData getOneShopgooddataByid(PageData pd);

	List<PageData> getAllGoodsClass(PageData pd);

	List<PageData> getOneAndTwoRealtionClass(PageData pd);

	List<PageData> getAllGoodsTwoClass(PageData pd);

	List<PageData> getTwoAndThreeRealtionClass(PageData pd);

	List<PageData> getAllGoodsThreeClass(PageData pd);

	int addGoodsClassInOneClassify(PageData pd);

	void addGoodsClassInTwoClassify(PageData onegoodsclass);

	void addOneRealtionData(PageData onerelation);

	void addGoodsClassInThreeClassify(PageData onegoodsclass);

	void addTwoRealtionData(PageData onerelation);

	int updateGoodsClassInOneClassify(PageData pd);

	int updateGoodsClassInTwoClassify(PageData pd);

	int updateGoodsClassInThreeClassify(PageData pd);

	int deleteGoodsClassInOneClassify(PageData pd);

	int deleteGoodsClassInTwoClassify(PageData pd);

	int deleteGoodsClassInThreeClassify(PageData pd);

	void addGoodsSizeById(PageData pd);

	void updateGoodsSizeById(PageData onedata);

	List<PageData> getAllShopgoodSizedataByid(PageData pd);

	void addGoodsAndThreeRealtion(PageData dd);

	int updateNewGoodssize(PageData map);

	int addNewGoodssize(PageData map);

	int deleteNewGoodssize(PageData pd);

	void updataGoodsAndThreeRelationData(PageData pd);

	void deleteGoodsAndThreeRelationData(PageData pd);

	PageData SeleteOnedataByrelation(PageData pc);

	List<PageData> getAllShopGoodsByid(PageData pd);

	void addOneGoodsdata(PageData pd);

	void addOneNewGoodssize(PageData newguige);

	List<PageData> getAllShopGoodsSIzeListpage(Page page);

	void addPictueData(PageData picturedata);

	List<PageData> getalldatabygoodsname(PageData mm);

	List<PageData> getAllGoodsDataNeedAdd(PageData pd);

	PageData selectOneEmail(PageData pd);

	PageData getOnePictureDatabyName(PageData picturedata);

	void updatePictueData(PageData picturedata);

	Integer getSn(PageData pa);

	void deleteAllNewGoodssize(PageData pd);

	List<PageData> getAllShopZhiXiaoGoodsListpage(Page page);

	PageData SelectAllGoodsSizeDataByDaima(PageData ww);

	void UpdateGoodsAndThreeRealtion(PageData mm);

	PageData SelectAllGoodsDataByDaima(PageData ll);


}
