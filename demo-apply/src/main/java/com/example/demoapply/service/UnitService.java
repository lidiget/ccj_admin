package com.example.demoapply.service;

import java.util.List;

import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;

public interface UnitService {

	List<PageData> getAllUnitsDataListpage(Page page);

	List<PageData> getAllSizeUnitDataListPage(Page page);

	void addunitdata(PageData map);

	void updateunitdatabyid(PageData pd);

	void deleteunitdatabyid(PageData pd);

	void addsizeunitdata(PageData map);

	void updatesizeunitdatabyid(PageData pd);

	void deletesizeunitdatabyid(PageData pd);

	List<PageData> getAllColorDataListpage(Page page);

	void updatecolordatabyid(PageData pd);

	void addcolordata(PageData map);

	void deletecolordatabyid(PageData pd);

	List<PageData> getAllWorkTypeDataListpage(Page page);

	void addworktypedata(PageData map);

	void updateworktypedatabyid(PageData pd);

	void deleteworktypedatabyid(PageData pd);

	List<PageData> getAllLogisticssDataListpage(Page page);

	void addlogisticsdata(PageData map);

	void updatelogisticsdatabyid(PageData pd);

	void deletelogisticsdatabyid(PageData pd);

	List<PageData> getAllSellingDataListpage(Page page);

	void addsellingdata(PageData map);

	void updatesellingdatabyid(PageData pd);

	void deletesellingdatabyid(PageData pd);

	List<PageData> getAllSellingAddressListpage(Page page);

	void addsellingAddressdata(PageData map);

	void updatesellingAddressdatabyid(PageData pd);

	void deletesellingAddressdatabyid(PageData pd);

	
	

}
