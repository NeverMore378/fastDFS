package pers.xzj.fastdfs.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.xzj.fastdfs.model.FileSystem;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import java.util.UUID;


@RestController
@RequestMapping("/filesystem")
@CrossOrigin
public class FileServerController {
    @Value("${fastdfs.upload_location}")
    private String upload_location;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    static {
        //加载fastDFS客户端的配置 文件
        try {
            ClientGlobal.initByProperties("config/fastdfs-client.properties");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println("network_timeout=" + ClientGlobal.g_network_timeout + "ms");
//        System.out.println("charset=" + ClientGlobal.g_charset);
    }

    @PostMapping("/upload")
    @ResponseBody
    public FileSystem upload(@RequestParam("file") MultipartFile file) throws IOException {
        //将文件先存储在web服务器上（本机），再调用fastDFS的client将文件上传到 fastDSF服务器
        FileSystem fileSystem = new FileSystem();
        //得到 文件的原始名称
        String originalFilename = file.getOriginalFilename();
        //扩展名
        String extention = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileNameNew = UUID.randomUUID() + extention;
        //定义file，使用file存储上传的文件
        File file1 = new File(upload_location + fileNameNew);
        file.transferTo(file1);
        //获取新上传文件的物理路径
        String newFilePath = file1.getAbsolutePath();
        //创建tracker的客户端
        TrackerClient tracker = new TrackerClient();
        TrackerServer trackerServer = tracker.getConnection();
        try {
            //定义storage的客户端
            StorageClient1 client = new StorageClient1(trackerServer, null);
            //文件元信息
            NameValuePair[] metaList = new NameValuePair[1];
            metaList[0] = new NameValuePair("fileName", originalFilename);
            //执行上传，将上传成功的存放在web服务器（本机）上的文件上传到 fastDFS
            String fileId = client.upload_file1(newFilePath, null, metaList);
//            System.out.println("upload success. file id is: " + fileId);
            log.info("upload success. file id is:{}", fileId);
            fileSystem.setFileId(fileId);
            fileSystem.setFilePath(newFilePath);
            fileSystem.setFileName(originalFilename);
            fileSystem.setFileSize(file.getSize());
            fileSystem.setFileType(extention);
            //通过调用service及dao将文件的路径存储到数据库中
            //...
        } catch (Exception ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
        } finally {
            //关闭trackerServer的连接
            trackerServer.close();
        }
        return fileSystem;
    }


}
