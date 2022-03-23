package com.example.demoapply.service;

import java.util.List;

import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;

public interface WorkerOrdersService {

	List<PageData> getAllTaskFlowListpage(Page page);

	List<PageData> getWorkderOrdersAllDetailsDataById(Page page);

	void changeWorkerOrderStatusById(PageData pd);

	

}
