package com.example.demoapply.service;

import java.util.List;

import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;

public interface VehicleService {

	List<PageData> getAllCarParametersDataListpage(Page page);

	int addVechiledata(PageData map);

	void updataVechiledataByid(PageData pd);

	void deleteonevehicledataByid(PageData pd);

	List<PageData> getAllVehicleOrdersListpage(Page page);



}
