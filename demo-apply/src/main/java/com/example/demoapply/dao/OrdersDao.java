package com.example.demoapply.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;

@Mapper
public interface OrdersDao {

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

	PageData seleUseridByOrderid(PageData pd);

	void updateOrdersTopayByOrderid(PageData pd);

	PageData seleOneByOrderid(PageData pd);

	List<PageData> getAllCollectionRecordListpage(Page page);

	void updateOrdersDiscountByOrderid(PageData pd);

	void addZheKouBillsdata(PageData pd);

	List<PageData> getAllDiscountRecordListpage(Page page);

	void shenheDiscountSuccess(PageData pd);

	PageData getonediscountrecordbyid(PageData pd);

	void deleteshenheDiscountSuccess(PageData pd);

	PageData getUserDataByOrderid(PageData pd);

	void updateJifenByuserid(PageData pd);

	void addUserOperationRecorddata(PageData pd);

	void deleteAlljiluByOrderid(PageData pd);

	List<PageData> getAllKaiPiaoDetailsListpage(PageData pd);

	List<PageData> getAllWorkOrderListpage(Page page);

	List<PageData> getWorkOrdersAllDetailsDataById(Page page);

	void changeWorkOrderStatusById(PageData pd);

	List<PageData> getWorkOrdersDataByIdListPage(Page page);

	void addworktwonamebyworkid(PageData onedata);

	void deleteworktwonamebyworkid(PageData onedata1);

	void changeOrderkaipiaoStatusByinvoiceId(PageData pd);





}
