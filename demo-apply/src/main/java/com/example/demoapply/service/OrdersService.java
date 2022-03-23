package com.example.demoapply.service;

import java.util.List;
import java.util.Map;

import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;

public interface OrdersService {

	List<PageData> getAllOrdersListpage(Page page);

	List<PageData> getOrdersAllDetailsDataById(Page page);

	void changeOrderStatusById(PageData pd);

	List<PageData> getAllBadEvaluationListpage(Page page);

	List<PageData> getOrdersAllDetailsDataByCommentId(Page page);

	List<PageData> getAllReturnOrderListpage(Page page);

	void changeReturnOrderStatusById(PageData pd);

	List<PageData> getAllUserOrderIds(PageData pd);

	void changeOrderkaipiaoStatusById(PageData pd);

	void addColletionBillsdata(PageData pd);

	List<PageData> getAllCollectionRecordListpage(Page page);

	void addZheKouBillsdata(PageData pd);

	List<PageData> getAllDiscountRecordListpage(Page page);

	void shenheDiscountSuccess(PageData pd);

	PageData getonediscountrecordbyid(PageData pd);

	void deleteshenheDiscountSuccess(PageData pd);

	PageData getUserDataByOrderid(PageData pd);

	void addUserOperationRecorddata(PageData pd);

	List<PageData> getAllKaiPiaoDetailsListpage(PageData pd);

	List<PageData> getAllWorkOrderListpage(Page page);

	List<PageData> getWorkOrdersAllDetailsDataById(Page page);

	void changeWorkOrderStatusById(PageData pd);

	List<PageData> getWorkOrdersDataByIdListPage(Page page);

	void changeAllOrderStatusByIds(Map<?, ?> map);

	

}
