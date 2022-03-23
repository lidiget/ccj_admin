package utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

import com.example.democommon.unit.UUIDUtils;
import com.obs.services.ObsClient;
import com.obs.services.ObsConfiguration;
import com.obs.services.exception.ObsException;
import com.obs.services.model.ObjectMetadata;
import com.obs.services.model.ProgressListener;
import com.obs.services.model.ProgressStatus;
import com.obs.services.model.PutObjectRequest;




public class UploadFile {
	public static final String ENDPOINT = "obs.cn-east-2.myhwclouds.com"; //华为云ak
	public static final String HUAWEI_AK = "BSFIQ09OUFNADK4FAZGW"; //华为云ak
	public static final String HUAWEI_SK = "YGFAtdS5b2jYz791kgaNg6S2MSu0t98x373gBZiY";//华为云sk
	public static Map<String, Object> uploadImg( String bucketName,String filefolder,
			MultipartFile picture) {
		System.out.println("开始上传。。。");
		System.out.println(picture.getOriginalFilename());
		Map<String, Object> map = new HashMap<String, Object>();
		Encoder encoder = Base64.getEncoder();
		String base64Img = null;
		String objectKey = UUIDUtils.getUUID().substring(0, 30);
		try {
			// 6 上传字段（上传表单元素）
			// 6.1.表单字段名称 fileItem.getFieldName();
			// 6.2.上传文件名
			String fileName = picture.getOriginalFilename().replaceAll("-", "").replaceAll(" ", "");
			URLEncoder.encode("工厂","utf-8");
			objectKey=objectKey+"-"+fileName;
			// * 兼容浏览器， IE ： C:\Users\xxx\Desktop\abc.txt ; 其他浏览器 ： abc.txt
			fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
			System.out.println("上传类表单文件名：" + fileName); // 上传的文件名会中文乱码，
		
			// 6.3.上传内容
			InputStream is = picture.getInputStream(); // 获得输入流，
			InputStream isHuaWei = picture.getInputStream(); // 获得输入流，
			// 6.4.开始上传华为云
			if (uploadToHuaWeiCloud(!bucketName.isEmpty() ? bucketName : "mitoufiles",filefolder, objectKey, isHuaWei)) {

				// 6.5.写入输出流
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				byte[] buffer = new byte[4096];
				int n = 0;
				while (-1 != (n = is.read(buffer))) {
					output.write(buffer, 0, n);
				}
                
				base64Img = encoder.encodeToString(output.toByteArray());

				// https://ais-userinfo-img.obs.cn-east-2.myhwclouds.com/2093f76636d84c878ef30f69de58422a
				// https://ais-userinfo-img.obs.cn-east-2.myhwclouds.com/7c1dde34a4f040f9b7a8d0cd260058d7
				// https://ais-userinfo-img.obs.cn-east-2.myhwclouds.com/ff8443264852427f8d80fb6eae550cf7
				if( filefolder!="") {
					map.put("objectKey", "https://" + bucketName + ".obs.cn-east-2.myhwclouds.com/"+filefolder+"/" + objectKey);
				}else {
					map.put("objectKey", "https://" + bucketName + ".obs.cn-east-2.myhwclouds.com/" + objectKey);
				}
				
				map.put("src", base64Img);

				output.close();
			}

			isHuaWei.close();
			is.close();

		} catch (Exception e) {
			e.printStackTrace();
			
//			throw new RuntimeException(e);

		}
		return map;
	}
	public static Map<String, Object> uploadImg2( String bucketName,String filefolder,
			File picture) {
		System.out.println("开始上传。。。");
		System.out.println(picture.getName());
		Map<String, Object> map = new HashMap<String, Object>();
		Encoder encoder = Base64.getEncoder();
		String base64Img = null;
		String objectKey = UUIDUtils.getUUID().substring(0, 30);
		try {
			// 6 上传字段（上传表单元素）
			// 6.1.表单字段名称 fileItem.getFieldName();
			// 6.2.上传文件名
			String fileName = picture.getName().replaceAll("-", "").replaceAll(" ", "");
			URLEncoder.encode("工厂","utf-8");
			objectKey=objectKey+"-"+fileName;
			// * 兼容浏览器， IE ： C:\Users\xxx\Desktop\abc.txt ; 其他浏览器 ： abc.txt
			fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
			System.out.println("上传类表单文件名：" + fileName); // 上传的文件名会中文乱码，
		
			// 6.3.上传内容
			InputStream is =  new FileInputStream(picture); // 获得输入流，
			InputStream isHuaWei = new FileInputStream(picture); // 获得输入流，
			// 6.4.开始上传华为云
			if (uploadToHuaWeiCloud(!bucketName.isEmpty() ? bucketName : "mitoufiles",filefolder, objectKey, isHuaWei)) {

				// 6.5.写入输出流
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				byte[] buffer = new byte[4096];
				int n = 0;
				while (-1 != (n = is.read(buffer))) {
					output.write(buffer, 0, n);
				}
                
				base64Img = encoder.encodeToString(output.toByteArray());

				// https://ais-userinfo-img.obs.cn-east-2.myhwclouds.com/2093f76636d84c878ef30f69de58422a
				// https://ais-userinfo-img.obs.cn-east-2.myhwclouds.com/7c1dde34a4f040f9b7a8d0cd260058d7
				// https://ais-userinfo-img.obs.cn-east-2.myhwclouds.com/ff8443264852427f8d80fb6eae550cf7
				if( filefolder!="") {
					map.put("objectKey", "https://" + bucketName + ".obs.cn-east-2.myhwclouds.com/"+filefolder+"/" + objectKey);
				}else {
					map.put("objectKey", "https://" + bucketName + ".obs.cn-east-2.myhwclouds.com/" + objectKey);
				}
				
				map.put("src", base64Img);

				output.close();
			}

			isHuaWei.close();
			is.close();

		} catch (Exception e) {
			e.printStackTrace();
			
//			throw new RuntimeException(e);

		}
		return map;
	}
	@SuppressWarnings("deprecation")
	protected static boolean uploadToHuaWeiCloud(String bucketName,String  filefolder, String objectKey, InputStream inputStream) {
		String endPoint = ENDPOINT;
		String ak = HUAWEI_AK;
		String sk = HUAWEI_SK;

		ObsConfiguration config = new ObsConfiguration();
		System.out.println(config);
		config.setEndPoint(endPoint);
		config.setHttpsOnly(true);
		config.setDisableDnsBucket(true);
		config.setSignatString("v4");
		config.setDefaultBucketLocation("CHINA");
		ObsClient obsClient = null;

		try {

			obsClient = new ObsClient(ak, sk, config);
			// create bucket
			if (obsClient.headBucket(bucketName)) {
				System.out.println("Bucket:" + bucketName + " already exists.");
			} else {
				System.out.println("Bucket does not exist, create bucket：" + bucketName + ".");
				obsClient.createBucket(bucketName);
			}
            if(filefolder!="") {
        		obsClient.putObject(bucketName, filefolder+"/", new ByteArrayInputStream(new byte[0]));

    			// 在文件夹下创建对象
    			obsClient.putObject(bucketName, filefolder+"/" + objectKey,inputStream);
            }else {
        		// upload stream
    			obsClient.putObject(bucketName, objectKey, inputStream, null);
            }
//	
			System.out.println("Put object:" + objectKey + " successfully.");

		} catch (ObsException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (obsClient != null) {
				try {
					obsClient.close();
				} catch (IOException e) {
				}
			}
		}

		return true;
	}

	
	
	public static Map<String, Object> uploadImgAndJingDu( String bucketName,String filefolder,
			MultipartFile picture,HttpSession session) {
		System.out.println("开始上传。。。");
		System.out.println(picture.getOriginalFilename());
		Map<String, Object> map = new HashMap<String, Object>();
		Encoder encoder = Base64.getEncoder();
		String base64Img = null;
		String objectKey = UUID.randomUUID().toString().replaceAll("-", "");
		try {
			// 6 上传字段（上传表单元素）
			// 6.1.表单字段名称 fileItem.getFieldName();
			// 6.2.上传文件名
			String fileName = picture.getOriginalFilename();
			// * 兼容浏览器， IE ： C:\Users\xxx\Desktop\abc.txt ; 其他浏览器 ： abc.txt
			fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
			System.out.println("上传类表单文件名：" + fileName); // 上传的文件名会中文乱码，
			// 6.3.上传内容
			InputStream is = picture.getInputStream(); // 获得输入流，
			InputStream isHuaWei = picture.getInputStream(); // 获得输入流，
			Long size = picture.getSize();//获得文件大小；
			// 6.4.开始上传华为云
			if (uploadToHuaWeiCloudAndJingdu(!bucketName.isEmpty() ? bucketName : "mitoufiles",filefolder, objectKey, isHuaWei,size,session)) {

				// 6.5.写入输出流
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				byte[] buffer = new byte[4096];
				int n = 0;
				while (-1 != (n = is.read(buffer))) {
					output.write(buffer, 0, n);
				}

				base64Img = encoder.encodeToString(output.toByteArray());

				// https://ais-userinfo-img.obs.cn-east-2.myhwclouds.com/2093f76636d84c878ef30f69de58422a
				// https://ais-userinfo-img.obs.cn-east-2.myhwclouds.com/7c1dde34a4f040f9b7a8d0cd260058d7
				// https://ais-userinfo-img.obs.cn-east-2.myhwclouds.com/ff8443264852427f8d80fb6eae550cf7
				if( filefolder!="") {
					map.put("objectKey", "https://" + bucketName + ".obs.cn-east-2.myhwclouds.com/"+filefolder+"/" + objectKey);
				}else {
					map.put("objectKey", "https://" + bucketName + ".obs.cn-east-2.myhwclouds.com/" + objectKey);
				}
				
				map.put("src", base64Img);

				output.close();
			}

			isHuaWei.close();
			is.close();

		} catch (Exception e) {
			e.printStackTrace();
			
//			throw new RuntimeException(e);

		}
		return map;
	}
	
	
	@SuppressWarnings("deprecation")
	protected static boolean uploadToHuaWeiCloudAndJingdu(String bucketName,String  filefolder, String objectKey, InputStream inputStream,Long size,HttpSession session) {
		String endPoint = ENDPOINT;
		String ak = HUAWEI_AK;
		String sk = HUAWEI_SK;

		ObsConfiguration config = new ObsConfiguration();
		config.setEndPoint(endPoint);
		config.setHttpsOnly(true);
		config.setDisableDnsBucket(true);
		config.setSignatString("v4");
		config.setDefaultBucketLocation("CHINA");
		ObsClient obsClient = null;
        
        		
		try {

			obsClient = new ObsClient(ak, sk, config);

			// create bucket
			if (obsClient.headBucket(bucketName)) {
				System.out.println("Bucket:" + bucketName + " already exists.");
			} else {
				System.out.println("Bucket does not exist, create bucket：" + bucketName + ".");
				obsClient.createBucket(bucketName);
			}
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(size);
			System.out.println(size);
			
            if(filefolder!="") {
            	
        		obsClient.putObject(bucketName, filefolder+"/", new ByteArrayInputStream(new byte[0]));

    			// 在文件夹下创建对象
        		PutObjectRequest request = new PutObjectRequest(bucketName, filefolder+"/" + objectKey);
        		request.setInput(inputStream);
        		request.setMetadata(metadata);
        		request.setProgressListener(new ProgressListener() {
        		       
        		       @Override
        		       public void progressChanged(ProgressStatus status) {
        		              // 获取上传平均速率
        		              System.out.println("AverageSpeed:" + status.getAverageSpeed());
        		              // 获取上传进度百分比
        		              System.out.println("TransferPercentage:" + status.getTransferPercentage());
        		            
        		              session.setAttribute("jingdus", status.getTransferPercentage());
        		             
        		       }

					
					
        		});
        		// 每上传1MB数据反馈上传进度
        		request.setProgressInterval(1024 * 1024L);
        		obsClient.putObject(request);
            }else {
        		// upload stream
            	// 在文件夹下创建对象
        		PutObjectRequest request = new PutObjectRequest(bucketName,objectKey);
        		request.setInput(inputStream);
        		request.setMetadata(metadata);
        		request.setProgressListener(new ProgressListener() {
        		       
        		       @Override
        		       public void progressChanged(ProgressStatus status) {
        		              // 获取上传平均速率
        		              System.out.println("AverageSpeed:" + status.getAverageSpeed());
        		              // 获取上传进度百分比
        		              System.out.println("TransferPercentage:" + status.getTransferPercentage());
        		            
        		              session.setAttribute("jingdus", status.getTransferPercentage());
        		             
        		       }

					
					
        		});
        		// 每上传1MB数据反馈上传进度
        		request.setProgressInterval(1024 * 1024L);
        		obsClient.putObject(request);
    			obsClient.putObject(bucketName, objectKey, inputStream, null);
            }
//	
			System.out.println("Put object:" + objectKey + " successfully.");

		} catch (ObsException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (obsClient != null) {
				try {
					obsClient.close();
				} catch (IOException e) {
				}
			}
		}

		return true;
	}




}
