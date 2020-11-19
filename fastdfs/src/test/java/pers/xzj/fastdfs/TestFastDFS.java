package pers.xzj.fastdfs;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 在此文件中通过fastDSF的client代码访问tracker和storage
 * 通过client的api代码方便 访问 tracker和storage，它们中间走的socket协议
 */
public class TestFastDFS {
    //测试文件上传
    @Test
    public void testUpload(){
        //通过fastDSF的client代码访问tracker和storage
        try {
            //加载fastDFS客户端的配置 文件
            ClientGlobal.initByProperties("config/fastdfs-client.properties");
            System.out.println("network_timeout=" + ClientGlobal.g_network_timeout + "ms");
            System.out.println("charset=" + ClientGlobal.g_charset);

            //创建tracker的客户端
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = null;
            //定义storage的客户端
            StorageClient1 client = new StorageClient1(trackerServer, storageServer);
            //文件元信息
            NameValuePair[] metaList = new NameValuePair[1];
            metaList[0] = new NameValuePair("fileName", "test.txt");
            //执行上传
            String fileId = client.upload_file1("C:\\Users\\Administrator\\Desktop\\test.txt", "txt", metaList);
            System.out.println("upload success. file id is: " + fileId);
            //关闭trackerServer的连接
            trackerServer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    //查询 文件上传
    @Test
    public void testQuery(){
        try {
            //加载fastDFS客户端的配置 文件
            ClientGlobal.initByProperties("config/fastdfs-client.properties");
            System.out.println("network_timeout=" + ClientGlobal.g_network_timeout + "ms");
            System.out.println("charset=" + ClientGlobal.g_charset);

            //创建tracker的客户端
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = null;
            //定义storage的客户端
            StorageClient1 client = new StorageClient1(trackerServer, storageServer);
            FileInfo group1 = client.query_file_info("group1", "M00/00/00/wKjrgF-yBMiAALHkAAAABCd_gfM562.txt");
            FileInfo fileInfo = client.query_file_info1("group1/M00/00/00/wKjrgF-yBMiAALHkAAAABCd_gfM562.txt");
            System.out.println(group1);
            System.out.println(fileInfo);
            //查询文件元信息
            NameValuePair[] metadata1 = client.get_metadata1("group1/M00/00/00/wKjrgF-yBMiAALHkAAAABCd_gfM562.txt");
            System.out.println(metadata1);
            //关闭trackerServer的连接
            trackerServer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //下载 文件上传
    @Test
    public void testDownload(){
        try {
            //加载fastDFS客户端的配置 文件
            ClientGlobal.initByProperties("config/fastdfs-client.properties");
            System.out.println("network_timeout=" + ClientGlobal.g_network_timeout + "ms");
            System.out.println("charset=" + ClientGlobal.g_charset);

            //创建tracker的客户端
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = null;
            //定义storage的客户端
            StorageClient1 client = new StorageClient1(trackerServer, storageServer);
            byte[] bytes = client.download_file1("group1/M00/00/00/wKjrgF-yBMiAALHkAAAABCd_gfM562.txt");
            File file = new File("d:/a.txt");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
            fileOutputStream.close();
            //关闭trackerServer的连接
            trackerServer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
