package com.example.demoapply.dao;

import org.apache.ibatis.annotations.Mapper;

import com.example.demoapply.entity.Page;
import com.example.demoapply.entity.PageData;

import java.util.List;
import java.util.Map;

@Mapper
public interface WorkUserDao {

	List<PageData> getAllWorkUsersDataListpage(Page page);

	PageData getOneWorkUserDataByid(PageData pd);

	PageData SelectWorkerData(PageData pd);

	int updataWorkuserdataByid(PageData pd);


	int UpdatepwdbyWokerid(PageData pd);

	int addWorkuserdata(PageData map);

	int deleteoneworkuserdataByid(PageData pd);

	void deleteoneworkuserRelationdataByid(PageData pd);

	List<PageData> getALLgongzhongxinxiListPage(Page page);

	PageData getgongzhongdatabygongzhongid(PageData pd);

	void updatagongzongdataByid(PageData pd);   

}
