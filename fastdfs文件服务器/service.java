package com.lmbx.csp.thirdservice.fdfs.impl;

import com.google.common.collect.Maps;
import com.lmbx.csp.core.exception.ServiceException;
import com.lmbx.csp.thirdservice.fdfs.FileService;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Description:
 * 
 * <pre>
 * </pre>
 * 
 * @author javahuang
 * @since 2017/11/24 上午10:11
 */
@Service
public class FastDFSFileServiceImpl implements FileService {

    private Logger logger = LoggerFactory.getLogger(FastDFSFileServiceImpl.class);

    @Value("${third-service.fastdfs.tracker-servers}")
    String         trackerServers = "host+port";

    @PostConstruct
    public void init() {
        try {
            ClientGlobal.initByTrackers(trackerServers);
            logger.info("系统文件服务初始化成功。");
        } catch (Exception e) {
            logger.error("文件服务器初始化异常", e);
            e.printStackTrace();
        }
    }

    @Override
    public String uploadFile(byte[] bytes, String extName, NameValuePair[] pairs) {
        fileNameValidation(pairs);
        TrackerClient tracker = new TrackerClient();
        try {
            TrackerServer trackerServer = tracker.getConnection();
            StorageClient1 client = new StorageClient1(trackerServer, null);
            String fileId = client.upload_file1(bytes, extName, pairs);
            logger.info("上传文件成功，ID：{}", fileId);
            return fileId;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("上传文件失败");
        }
    }

    private void fileNameValidation(NameValuePair[] pairs) {
        boolean hasNamePair = false;
        if (pairs != null) {
            for (NameValuePair pair : pairs) {
                String name = pair.getName();
                if ("name".equals(name)) {
                    hasNamePair = true;
                    break;
                }
            }
        }
        if (!hasNamePair) {
            throw new ServiceException("上传文件必须添加文件名，即 NameValuePair(\"name\", value)");
        }
    }

    @Override
    public boolean uploadFileWithMetaData() {
        return false;
    }

    @Override
    public byte[] downloadFile(String fileId) {
        TrackerClient tracker = new TrackerClient();
        try {
            TrackerServer trackerServer = tracker.getConnection();
            StorageClient1 client = new StorageClient1(trackerServer, null);
            return client.download_file1(fileId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("下载文件失败");
        }
    }

    @Override
    public Map<String, Object> downloadFileWithMetaData(String fileId) {
        TrackerClient tracker = new TrackerClient();
        try {
            TrackerServer trackerServer = tracker.getConnection();
            StorageClient1 client = new StorageClient1(trackerServer, null);
            byte[] body = client.download_file1(fileId);
            NameValuePair[] metaData = client.get_metadata1(fileId);
            Map<String, Object> result = Maps.newHashMap();
            result.put("meta", metaData);
            result.put("body", body);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("下载文件失败");
        }
    }

    @Override
    public NameValuePair[] getFileMetaData(String fileId) {
        TrackerClient tracker = new TrackerClient();
        try {
            TrackerServer trackerServer = tracker.getConnection();
            StorageClient1 client = new StorageClient1(trackerServer, null);
            return client.get_metadata1(fileId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("上传文件失败");
        }
    }
}
