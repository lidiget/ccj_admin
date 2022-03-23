/**
 * 
 */
package com.example.demoapply.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demoapply.entity.PageData;
import com.example.demoapply.service.GoodsService;

import utils.AliYunUploadFile;
import utils.UploadFile;

/**
 * @author 46794
 *
 */
@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/pub")
public class UploadFileController extends BaseController {

	@Autowired
	GoodsService goodsService;

	/**
	 * 上传图片(单个文件，文件name 为picture)
	 * 
	 * @param bucketName
	 * @param request
	 * @return
	 */
	@RequestMapping("/uploadImg/{bucketName}")
	@ResponseBody
	public Map<String, Object> uploadImg(@PathVariable("bucketName") String bucketName, String filefolder,
			@RequestParam(name = "picture", required = false) MultipartFile picture) {

		Map<String, Object> map = UploadFile.uploadImg(bucketName, "", picture);

		return ObjectDto(0, "上传成功", map);
	}

	/**
	 * 上传图片(多个文件，文件name 为picture)
	 * 
	 * @param bucketName
	 * @param request
	 * @return
	 */
	@RequestMapping("/uploadImgs/{bucketName}")
	@ResponseBody
	public Map<String, Object> uploadImgs(@PathVariable("bucketName") String bucketName, String filefolder,
			@RequestParam(name = "picture", required = false) List<MultipartFile> pictures) {
//		if (session.getAttribute("user") != null) {
//			System.out.println((((PageData) session.getAttribute("user")).getString("code")));
//			filefolder = ((PageData) session.getAttribute("user")).getString("code");
//		}
		List<Object> imgs = new ArrayList<Object>();
		for (MultipartFile picture : pictures) {
			Map<String, Object> map = UploadFile.uploadImg(bucketName, filefolder.isEmpty() ? "" : filefolder, picture);
			imgs.add(map.get("objectKey"));
		}

		return ObjectDto(0, "上传成功", imgs);
	}

	@RequestMapping("/aliuploadImg2")
	@ResponseBody
	public Map<String, Object> aliuploadImg2(@RequestParam(name = "picture", required = false) MultipartFile picture) {
		AliYunUploadFile.init();// 初始化
		String map = AliYunUploadFile.uploadImg2Oss(picture);

		System.out.println(picture);
		PageData picturedata = new PageData();
		String name = picture.getOriginalFilename();
		int unixSep = name.lastIndexOf('/');
		int winSep = name.lastIndexOf('\\');
		int pos = (winSep > unixSep ? winSep : unixSep);
		if (pos != -1) {
			name = name.substring(pos + 1);
		}
		String url = AliYunUploadFile.getImgUrl(map);
		picturedata.put("name", name);
		picturedata.put("url", url);
		PageData onedata = goodsService.getOnePictureDatabyName(picturedata);
		if (onedata != null) {
			goodsService.updatePictueData(picturedata);
		} else {
			goodsService.addPictueData(picturedata);
		}

		return ObjectDto(0, "上传成功", map);
	}

	@RequestMapping("/aliuploadImg3")
	@ResponseBody
	public Map<String, Object> aliuploadImg3(@RequestParam(name = "picture", required = false) MultipartFile picture) {

		AliYunUploadFile.init();// 初始化
		String map = AliYunUploadFile.uploadImg2Oss(picture);

		System.out.println(picture);
		PageData data = new PageData();

		String url = AliYunUploadFile.getImgUrl(map);

		data.put("objectKey", url);
		return ObjectDto(0, "上传成功", data);
	}

	@ResponseBody
	public Map<String, Object> aliupuploadImgs(
			@RequestParam(name = "picture", required = false) List<MultipartFile> pictures) {
		AliYunUploadFile.init();// 初始化

		List<Object> imgs = new ArrayList<Object>();
		for (MultipartFile picture : pictures) {
//			Map<String, Object> map = UploadFile.uploadImg(bucketName, filefolder.isEmpty() ? "" : filefolder,
//					picture);
			String map = AliYunUploadFile.uploadImg2Oss(picture);
			String url = AliYunUploadFile.getImgUrl(map);
				PageData picturedata = new PageData();
				String name = picture.getOriginalFilename();
				int unixSep = name.lastIndexOf('/');
				int winSep = name.lastIndexOf('\\');
				int pos = (winSep > unixSep ? winSep : unixSep);
				if (pos != -1) {
					name = name.substring(pos + 1);
				}
				picturedata.put("name", name);
				picturedata.put("url", url);
				PageData onedata = goodsService.getOnePictureDatabyName(picturedata);
				if (onedata != null) {
					goodsService.updatePictueData(picturedata);
				} else {
					goodsService.addPictueData(picturedata);
				}
			imgs.add(url);
		}

		return ObjectDto(0, "上传成功", imgs);
	}

}
