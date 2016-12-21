package qiniu;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qiniu.utils.Configurations;

import java.io.File;

/**
 * Created by kobe on 2016/12/21.
 */
public class UploadDemo {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //设置好账号的ACCESS_KEY和SECRET_KEY
    String ACCESS_KEY = "";
    String SECRET_KEY = "";
    //要上传的空间
    String bucketname = "";

    //上传到七牛后保存的文件名
    String key = "my-java.png";
    //上传文件的路径
    String FilePath = "E:\\新建文本文档.txt";

    //密钥配置
    Auth auth ;

    @Before
    public void init(){
        ACCESS_KEY = Configurations.getString("period.qiniu.accesskey");
        SECRET_KEY = Configurations.getString("period.qiniu.secretkey");
        bucketname = Configurations.getString("period.qiniu.bucketname");

        auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    }



    @Test
    public void simpleUpload(){

        ///////////////////////指定上传的Zone的信息//////////////////
        //第一种方式: 指定具体的要上传的zone
        //注：该具体指定的方式和以下自动识别的方式选择其一即可
        //要上传的空间(bucket)的存储区域为华东时
        // Zone z = Zone.zone0();
        //要上传的空间(bucket)的存储区域为华北时
        // Zone z = Zone.zone1();
        //要上传的空间(bucket)的存储区域为华南时
        // Zone z = Zone.zone2();

        //第二种方式: 自动识别要上传的空间(bucket)的存储区域是华东、华北、华南。
        Zone z = Zone.autoZone();
        Configuration c = new Configuration(z);

        String upToken  = auth.uploadToken(bucketname);

        //创建上传对象
        UploadManager uploadManager = new UploadManager(c);
        try {
            //调用put方法上传
            Response res = uploadManager.put(FilePath, key, upToken);
            //打印返回的信息
            System.out.println(res.bodyString());
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            System.out.println(r.toString());
            try {
                //响应的文本信息
                System.out.println(r.bodyString());
            } catch (QiniuException e1) {
                //ignore
            }
        }
    }

    @Test
    public void testFile(){
        String filepath = "E:\\新建文本文档.txt";
        File file = new File(filepath);
        System.out.println(file.exists());
    }


}
