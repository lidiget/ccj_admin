package utils;


import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 阿里云 OSS文件类
 *
 * @author YuanDuDu
 */
public class AliYunUploadFile {

	static Log log = LogFactory.getLog(AliYunUploadFile.class);
    // endpoint以杭州为例，其它region请按实际情况填写
    private static String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
    // accessKey
    private static String accessKeyId = "LTAI5tBXStgogXEY58UPqUkq";
    private static String accessKeySecret = "n5DMgOISnYOkniZL1c4Yw8pE5ZCV81";
    //空间
    private static String bucketName = "yiranapp";
    //文件存储目录
    private static String filedir = "test";

    private static OSSClient ossClient;

    public AliYunUploadFile() {
        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * 初始化
     */
    public static void init() {
        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * 销毁
     */
    public void destory() {
        ossClient.shutdown();
    }

    /**
     * 上传图片
     *
     * @param url
     */
    public void uploadImg2Oss(String url) {
        File fileOnServer = new File(url);
        FileInputStream fin;
        try {
            fin = new FileInputStream(fileOnServer);
            String[] split = url.split("/");
            this.uploadFile2OSS(fin, split[split.length - 1]);
        } catch (FileNotFoundException e) {
            System.out.println("图片上传失败");
        }
    }


    public static String uploadImg2Oss(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        Random random = new Random();
        String name = random.nextInt(10000) + System.currentTimeMillis() + substring;
        try {
            InputStream inputStream = file.getInputStream();
            uploadFile2OSS(inputStream, name);
            return name;
        } catch (Exception e) {
            System.out.println("图片上传失败");
            return null;
        }
    }

    /**
     * 获得图片路径
     *
     * @param fileUrl
     * @return
     */
    public static String getImgUrl(String fileUrl) {
        if (!StringUtils.isEmpty(fileUrl)) {
            String[] split = fileUrl.split("/");
            return getUrl(filedir + split[split.length - 1]);
        }
        return null;
    }

    /**
     * 上传到OSS服务器  如果同名文件会覆盖服务器上的
     *
     * @param instream 文件流
     * @param fileName 文件名称 包括后缀名
     * @return 出错返回"" ,唯一MD5数字签名
     */
    public static String uploadFile2OSS(InputStream instream, String fileName) {
        String ret = "";
        System.out.println(instream);
        System.out.println(fileName);
        try {
            //创建上传Object的Metadata
            ObjectMetadata objectMetadata = new ObjectMetadata();
            
            objectMetadata.setContentLength(instream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            System.out.println("sssssssssssssssssssssssssss");
            System.out.println(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
            objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
            
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            //上传文件
            PutObjectResult putResult = ossClient.putObject(bucketName, filedir + fileName, instream, objectMetadata);
            ret = putResult.getETag();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * Description: 判断OSS服务文件上传时文件的contentType
     *
     * @param FilenameExtension 文件后缀
     * @return String
     */
    public static String getcontentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        else if (FilenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        else if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
                FilenameExtension.equalsIgnoreCase(".jpg") ||
                FilenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpeg";
        }
        else if (FilenameExtension.equalsIgnoreCase(".html")) {
            return "text/html";
        }
        else if (FilenameExtension.equalsIgnoreCase(".txt")) {
            return "text/plain";
        }
        else if (FilenameExtension.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";
        }
        else if (FilenameExtension.equalsIgnoreCase(".pptx") ||
                FilenameExtension.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        else if (FilenameExtension.equalsIgnoreCase(".docx") ||
                FilenameExtension.equalsIgnoreCase(".doc")) {
            return "application/msword";
        }
        else if (FilenameExtension.equalsIgnoreCase(".xml")) {
            return "text/xml";
        }
        else if(FilenameExtension.equalsIgnoreCase(".mp4")) {
        	return "mp4";
        }else {
        	 return "image/jpeg";
        }
    }

    /**
     * 获得url链接
     *
     * @param key
     * @return
     */
    public static String getUrl(String key) {
        // 设置URL过期时间为10年  3600l* 1000*24*365*10
        Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
        if (url != null) {
        	String Str2 = url.toString();
            String str1=Str2.substring(0, Str2.indexOf("?"));

            return str1;
        }
        return null;
    }
}
