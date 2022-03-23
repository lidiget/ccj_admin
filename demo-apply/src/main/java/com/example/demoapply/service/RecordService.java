package com.example.demoapply.service;

import java.util.List;

import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;

public interface RecordService {

	List<PageData> getRnvoiceRecordListpage(Page page);

	List<PageData> getWalletRecordListpage(Page page);

	List<PageData> getAllBrowseDataListpage(Page page);


	

	

}
