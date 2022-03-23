package com.example.demoapply.service;

import java.util.List;

import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;

public interface TakesService {

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

	void addgongzhongleixingdata(PageData map);

	void updategongzhongleixingdatabyid(PageData pd);

	void deleteOnefenleidataByid(PageData pd);



	

}
