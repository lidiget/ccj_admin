package com.example.demoapply.service;

import java.util.List;

import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;

public interface WorkUserService {

	List<PageData> getAllWorkUsersDataListpage(Page page);

	PageData getOneWorkUserDataByid(PageData pd);

	PageData SelectWorkerData(PageData pd);

	int updataWorkuserdataByid(PageData pd);


	int UpdatepwdbyWokerid(PageData pd);

	int addWorkuserdata(PageData map);

	int deleteoneworkuserdataByid(PageData pd);

	List<PageData> getALLgongzhongxinxiListPage(Page page);

	PageData getgongzhongdatabygongzhongid(PageData pd);

	void updatagongzongdataByid(PageData pd);

}
