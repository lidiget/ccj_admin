package com.example.demoapply.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;

@Mapper
public interface RecordDao {

	List<PageData> getRnvoiceRecordListpage(Page page);

	List<PageData> getWalletRecordListpage(Page page);

	List<PageData> getAllBrowseDataListpage(Page page);








}
