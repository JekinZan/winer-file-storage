package com.winer.winerfilestorage.clients.fastdfs;


import cn.hutool.core.util.StrUtil;
import com.winer.winerfilestorage.base.*;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;

/**
 * FastDFS 存储客户端
 *
 */
public class FastDFSStorageClient extends AbstractStorageClient<FastDFSBucket> {

    /**
     * 存储代码
     */
    public static final String CHANNEL_ID = "fastDFS";

    /**
     * 存储名称
     */
    public static final String CHANNEL_NAME = "FastDFS 文件服务器";

    /**
     * 实例化
     *
     * @param endpoint
     * @param defaultBucketName
     */
    public FastDFSStorageClient(String endpoint, String defaultBucketName) {
        super(endpoint, defaultBucketName);
    }

    /**
     * FastDFSStorageClient
     *
     * @param properties 属性
     */
    public FastDFSStorageClient(FastDFSStorageClientProperties properties) {
        this(properties.getEndpoint(), properties.getDefaultBucketName());
        this.setStorageClientProperties(properties);
    }

    @Override
    public String getChannelId() {
        return CHANNEL_ID;
    }

    @Override
    public String getChannelName() {
        return CHANNEL_NAME;
    }

    @Override
    public boolean existBucket(String bucketName) {
        throw new RuntimeException("不支持的操作");
    }

    @Override
    public FastDFSBucket createBucket(String bucketName) {
        throw new RuntimeException("不支持的操作");
    }

    @Override
    public FastDFSBucket getBucket(String bucketName) {
        return null;
    }

    /**
     * 创建存储客户端
     *
     * @return
     */
    private StorageClient createStorageClient() throws IOException {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer;
        trackerServer = trackerClient.getConnection();
        return new StorageClient(trackerServer, null);
    }

    /**
     * 创建文件
     *
     * @param request 请求
     */
    protected NameValuePair[] createHeaders(FileStorageRequest request) {
        List<NameValuePair> pairs = new ArrayList<>(2);
        String contentType = this.getFileContentType(request.getFileInfo().getExtensionName());
        if (StrUtil.isNotEmpty(contentType)) {
            pairs.add(new NameValuePair("Content-Type", contentType));
        }
        String contentDisposition = this.getContentDisposition(request);
        if (StrUtil.isNotBlank(contentDisposition)) {
            pairs.add(new NameValuePair(CONTENT_DISPOSITION, contentDisposition));
        }
        return pairs.toArray(new NameValuePair[pairs.size()]);
    }

    @Override
    public FileObject saveFile(FileStorageRequest request) throws Exception {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            String bucketName = this.checkBucketName(request.getBucketName());
            StorageClient storageClient = this.createStorageClient();
            this.writeOutputStream(request.getInputStream(), outputStream);
            byte[] bytes = outputStream.toByteArray();
            String[] files = storageClient.upload_file(bucketName, bytes,
                    request.getFileInfo().getExtensionName(), this.createHeaders(request));
            if (files == null || files.length < 2) {
                throw new RuntimeException("上传失败，获取上传文件信息不正确。");
            }
            FileObject fileObject = new FileObject();
            FileInfo fileInfo = new FileInfo(files[1], true, bytes.length);
            fileObject.setAccessUrl(this.getAccessUrl(files[0], fileInfo.getFullPath()));
            fileObject.setUrl(fileInfo.getFullPath());
            fileObject.setFileInfo(fileInfo);
            return fileObject;
        } finally {
            IOUtils.closeQuietly(request.getInputStream());
            IOUtils.closeQuietly(outputStream);
        }
    }

    @Override
    public FileStorageObject getFile(String bucketName, String fullPath) throws IOException, MyException {
        bucketName = this.checkBucketName(bucketName);
        if (StrUtil.isBlank(fullPath)){
            throw new RuntimeException("fullPath 为空");
        }
        StorageClient storageClient = this.createStorageClient();

            byte[] bytes = storageClient.download_file(bucketName, fullPath);
            FileInfo fileInfo = new FileInfo(fullPath, true, bytes.length);
            FileStorageObject storageObject = new FileStorageObject();
            storageObject.setInputStream(new ByteArrayInputStream(bytes));
            storageObject.setAccessUrl(this.getAccessUrl(bucketName, fullPath));
            storageObject.setUrl(fullPath);
            storageObject.setFileInfo(fileInfo);
            return storageObject;
    }

    @Override
    public boolean existFile(String bucketName, String fullPath) throws IOException, MyException {
        bucketName = this.checkBucketName(bucketName);
        if (StrUtil.isBlank(fullPath)){ throw new RuntimeException("fullPath 为空"); }
        StorageClient storageClient = this.createStorageClient();
        FileInfo fileInfo = new FileInfo(fullPath, true);
        return storageClient.query_file_info(bucketName, fileInfo.getFullPath()) != null;
    }

    /**
     * 获取访问Url
     *
     * @param bucketName 分区名称
     * @param fileInfo
     * @return
     */
    private String getAccessUrl(String bucketName, FileInfo fileInfo) {
        return this.getPathAddress(this.getEndpoint(), bucketName, fileInfo.getFullPath());
    }

    @Override
    public String getAccessUrl(String bucketName, String fullPath) {
        bucketName = this.checkBucketName(bucketName);
        if (StrUtil.isBlank(fullPath)){ throw new RuntimeException("fullPath 为空"); }
        FileInfo fileInfo = new FileInfo(fullPath, true);
        return this.getAccessUrl(bucketName, fileInfo);
    }

    @Override
    public void deleteFile(String bucketName, String fullPath) throws IOException, MyException {
        bucketName = this.checkBucketName(bucketName);
        if (StrUtil.isBlank(fullPath)){ throw new RuntimeException("fullPath 为空"); }
        StorageClient storageClient = this.createStorageClient();
        FileInfo fileInfo = new FileInfo(fullPath, true);
        storageClient.delete_file(bucketName, fileInfo.getFullPath());
    }

    @Override
    public List<FileObject> listFileObjects(String bucketName, String prefix) {
        throw new RuntimeException("不支持的操作");
    }

}
