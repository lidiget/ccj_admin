package com.example.demoapply.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;

@Mapper
public interface TakesDao {

	List<PageData> getAllTakeListpage(Page page);

	void changeTakeStatusById(PageData pd);

	int addNewTaskData(PageData map);

	List<PageData> getAllGongzhongListpage(Page page);

	void addgongzhongdata(PageData map);

	void updategongzhongdatabyid(PageData pd);

	void deletegongzhongdatabyid(PageData pd);

	List<PageData> getAllLeiXingListpage(Page page);

	void addleixingdata(PageData map);

	void updateleixingdatabyid(PageData pd);

	void deleteleixingdatabyid(PageData pd);

	List<PageData> getAllGongzhong1Listpage(Page page);

	List<PageData> getAllgongzhongfenlei(Page page);

	void addgongzhongrelationdata(PageData bb);

	void deletegongzhongrelationdatabyid(PageData pd);

	void addgongzhongleixingdata(PageData map);

	void updategongzhongleixingdatabyid(PageData pd);

	void deleteOnefenleidataByid(PageData pd);

	void addNewTaskRelationData(PageData cc);

	PageData seleUseridByPhone(PageData cc);






}
