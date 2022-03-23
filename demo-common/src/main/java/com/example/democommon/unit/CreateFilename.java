package com.example.democommon.unit;

public class CreateFilename {
    public static String summary(String brand,String abc){
        String order = "projectp/order/"+brand+"/"+abc;
        return order;
    }

    //创建店铺文件夹
    public static String order(String brand,String areas,String orderid){
        String order = "projectp/order/"+
                brand+"/"+
                areas+"/"+
                orderid;
        return order;
    }
    //创建视频文件夹
    public static String video(String order){
        String video = order+ "/"+"video";
        return video;
    }
    //创建报告的图片文件夹
    public static String picture(String order){
        String picture = order+"/"+"picture";
        return picture;
    }
    //创建全景文件夹
    public static String panoramic(String order){
        String panoramic = order+"/"+"panoramic";
        return panoramic;
    }
    //创建报告文件夹
    public static String report(String order){
        String report = order+"/"+"report";
        //String report = "D:\\test2";
        return report;
    }
    //创建头像文件夹
    public static String user(String user_phone){
        String user = "projectp/user/"+user_phone;
        return user;
    }
    public static String userpin(String user_phone){
        String user = "https://www.kdsji.cn/user/"+user_phone;
        return user;
    }
    public static String pictures(String brand,String areas,String storeid,String piclassify_id,String a){
        String order = "https://www.kdsji.cn/order/"+
                brand+"/"+
                areas+"/"+
                storeid+"/"+"picture"+"/"+piclassify_id+"/"+a;
        return order;
    }
    public static String panoramic(String brand,String areas,String storeid,String order_panoramic_group_id,String a){
        String order = "https://www.kdsji.cn/order/"+
                brand+"/"+
                areas+"/"+
                storeid+"/"+"panoramic"+"/"+order_panoramic_group_id+"/"+a;
        return order;
    }
    public static String videos(String brand,String areas,String storeid,String video_group_id,String a){
        String order = "https://www.kdsji.cn/order/"+
                brand+"/"+
                areas+"/"+
                storeid+"/"+"video"+"/"+video_group_id+"/"+a;
        return order;
    }
    public static String report_picture(Object brand, Object areas, Object storeid,Object reporid){
        String order = "https://www.kdsji.cn/order/"+
                brand+"/"+
                areas+"/"+
                storeid+"/"+"report"+"/"+reporid;
        return order;
    }

}
