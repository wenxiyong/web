package com.period.web.utils;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * Created by kobe on 2016/12/21.
 */
public class PicUploadUtil {
    private static Logger logger = LoggerFactory.getLogger(PicUploadUtil.class);

    //设置好账号的ACCESS_KEY和SECRET_KEY
    private static String ACCESS_KEY;
    private static String SECRET_KEY;
    //要上传的空间
    private static Zone z;
    private static Configuration c;
    private static String upToken;
    private static String bucketname;
    private static UploadManager uploadManager;
    //密钥配置
    private static Auth auth ;

    static {
        ACCESS_KEY = Configurations.getString("period.qiniu.accesskey");
        SECRET_KEY = Configurations.getString("period.qiniu.secretkey");
        bucketname = Configurations.getString("period.qiniu.bucketname");

        auth = Auth.create(ACCESS_KEY, SECRET_KEY);

        z = Zone.autoZone();
        c = new Configuration(z);

        upToken = auth.uploadToken(bucketname);

        //创建上传对象
        uploadManager = new UploadManager(c);
    }

    public static String simpleUpload(String filepath){

        ///////////////////////指定上传的Zone的信息//////////////////
        //第一种方式: 指定具体的要上传的zone
        //注：该具体指定的方式和以下自动识别的方式选择其一即可
        //要上传的空间(bucket)的存储区域为华东时
        // Zone z = Zone.zone0();
        //要上传的空间(bucket)的存储区域为华北时
        // Zone z = Zone.zone1();
        //要上传的空间(bucket)的存储区域为华南时
        // Zone z = Zone.zone2();
        if(StringUtils.isEmpty(filepath)) return null;

        String fileName = filepath.substring(filepath.lastIndexOf("/") + 1 ,filepath.length());

        //第二种方式: 自动识别要上传的空间(bucket)的存储区域是华东、华北、华南。
        try {
            //调用put方法上传
            Response res = uploadManager.put(filepath, fileName, upToken);
            //打印返回的信息
            logger.info("上传至7牛成功，返回信息={}",res.bodyString());
        } catch (QiniuException e) {
            fileName = "";
            Response r = e.response;
            // 请求失败时打印的异常的信息
            logger.error("上传至7牛失败，异常信息={}",r.toString());
            try {
                //响应的文本信息
                logger.error("上传至7牛失败，响应文本={}",r.bodyString());
            } catch (QiniuException e1) {
                //ignore
            }
        }
        return fileName;
    }
}
