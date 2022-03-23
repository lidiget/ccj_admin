package com.example.demoapply.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;

@Mapper
public interface VehicleDao {

	List<PageData> getAllCarParametersDataListpage(Page page);

	int addVechiledata(PageData map);

	void updataVechiledataByid(PageData pd);

	void deleteonevehicledataByid(PageData pd);

	List<PageData> getAllVehicleOrdersListpage(Page page);









}
