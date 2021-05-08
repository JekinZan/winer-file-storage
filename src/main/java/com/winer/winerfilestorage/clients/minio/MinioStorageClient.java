package com.winer.winerfilestorage.clients.minio;
import cn.hutool.core.util.StrUtil;
import com.winer.winerfilestorage.base.*;
import com.winer.winerfilestorage.utils.tuple.TupleTwo;
import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import io.minio.messages.Item;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Minio 存储客户端
 * <p>
 * </p>
 *
 * @description TODO
 * @author: 老码农
 * @create: 2020-06-27 01:16
 **/
public class MinioStorageClient extends AbstractStorageClient<MinioBucket> {

    /**
     * 存储代码
     */
    public static final String CHANNEL_ID = "Minio";

    /**
     * 存储名称
     */
    public static final String CHANNEL_NAME = "Minio 文件服务器";

    private final String serverUrl;
    private final String accessKey;
    private final String secretKey;

    /**
     * 客户端
     */
    protected final MinioClient minioClient;

    /**
     * MinioStorageClient
     *
     * @param endpoint
     * @param defaultBucketName
     */
    public MinioStorageClient(String endpoint, String defaultBucketName, String serverUrl,
                              String accessKey, String secretKey) {
        super(endpoint, defaultBucketName);
        this.serverUrl = serverUrl;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.minioClient = this.createMinioClient();
    }

    /**
     * MinioStorageClient
     *
     * @param properties 属性
     */
    public MinioStorageClient(MinioStorageClientProperties properties) {
        this(properties.getEndpoint(),
                properties.getDefaultBucketName(),
                properties.getServerUrl(),
                properties.getAccessKey(),
                properties.getSecretKey());
        this.setStorageClientProperties(properties);
    }

    /**
     * 创建客户端
     *
     * @return
     */
    private MinioClient createMinioClient() {
        try {
            return new MinioClient(this.getServerUrl(), this.getAccessKey(), this.getSecretKey());
        } catch (InvalidEndpointException e) {
            throw new RuntimeException("无效的服务器地址:" + this.getServerUrl());
        } catch (InvalidPortException e) {
            throw new RuntimeException("无效的服务器端口:" + this.getServerUrl());
        }
    }

    @Override
    public boolean existBucket(String bucketName) {
        if (StrUtil.isBlank(bucketName)){ throw new RuntimeException("bucketName 不能为空"); }
        try {
            return this.minioClient.bucketExists(bucketName);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public MinioBucket createBucket(String bucketName) {
        try {
            this.minioClient.makeBucket(bucketName);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return new MinioBucket(bucketName);
    }

    @Override
    public MinioBucket getBucket(String bucketName) {
        if (this.existBucket(bucketName)) {
            return new MinioBucket(bucketName);
        }
        return null;
    }

    /**
     * 设置文件选项
     *
     * @param options 选项
     * @param request 请求
     */
    protected void setHeaders(PutObjectOptions options, FileStorageRequest request) {
        String contentType = this.getFileContentType(request.getFileInfo().getExtensionName());
        if (StrUtil.isNotBlank(contentType)) {
            options.setContentType(contentType);
        }
        String contentDisposition = this.getContentDisposition(request);
        if (StrUtil.isNotBlank(contentDisposition)) {
            Map<String, String> headers = new HashMap<>(1);
            headers.put(CONTENT_DISPOSITION, contentDisposition);
            options.setHeaders(headers);
        }
    }

    @Override
    public FileObject saveFile(FileStorageRequest request) throws Exception {

        String bucketName = this.checkBucketName(request.getBucketName());
        if (!this.existBucket(bucketName)) {
            this.createBucket(bucketName);
        }
        InputStream inputStream = null;
        try {
            TupleTwo<Long, InputStream> two = this.readRequestStream(request);
            long size = two.getItem1();
            inputStream = two.getItem2();
            PutObjectOptions options = new PutObjectOptions(size, -1L);
            this.setHeaders(options, request);
            this.minioClient.putObject(bucketName, request.getFileInfo().getFullPath(), inputStream, options);
            //ObjectStat stat = this.minioClient.statObject(bucketName, request.getFileInfo().getFullPath());
            FileObject fileObject = new FileObject();
            fileObject.setFileInfo(request.getFileInfo());
            fileObject.getFileInfo().setLength(size);
            fileObject.setUrl(request.getFileInfo().getFullPath());
            //fileObject.setAccessUrl(this.minioClient.getObjectUrl(bucketName, request.getFileInfo().getFullPath()));
            fileObject.setAccessUrl(this.getAccessUrl(bucketName, request.getFileInfo().getFullPath()));
            return fileObject;
        } finally {
            IOUtils.closeQuietly(request.getInputStream());
            IOUtils.closeQuietly(inputStream);
        }
    }

    @Override
    public FileStorageObject getFile(String bucketName, String fullPath) {
        bucketName = this.checkBucketName(bucketName);
        if (StrUtil.isBlank(fullPath)){ throw new RuntimeException("fullPath 不能为空"); }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = null;
        try {
            inputStream = this.minioClient.getObject(bucketName, fullPath);
            long size = this.writeOutputStream(inputStream, outputStream);
            FileInfo fileInfo = new FileInfo(fullPath, true, outputStream.size());
            FileStorageObject storageObject = new FileStorageObject();
            storageObject.setInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
            storageObject.setAccessUrl(this.getAccessUrl(bucketName, fullPath));
            storageObject.setUrl(fullPath);
            storageObject.setFileInfo(fileInfo);
            return storageObject;
        } catch (ErrorResponseException e) {
            if (e.errorResponse().errorCode().equals(ErrorCode.NO_SUCH_OBJECT)) {
                return null;
            }
            throw new RuntimeException(e.getMessage(), e);
        } catch (Exception e) {
            throw  new RuntimeException(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(inputStream);
        }

    }

    @Override
    public boolean existFile(String bucketName, String fullPath) {
        bucketName = this.checkBucketName(bucketName);
        if (StrUtil.isBlank(fullPath)){ throw new RuntimeException("fullPath 不能为空"); }
        try {
            ObjectStat stat = this.minioClient.statObject(bucketName, fullPath);
            return stat != null;
        } catch (ErrorResponseException e) {
            if (e.errorResponse().errorCode().equals(ErrorCode.NO_SUCH_OBJECT)) {
                return false;
            }
            throw new RuntimeException(e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
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
        if (StrUtil.isBlank(fullPath)){ throw new RuntimeException("fullPath 不能为空"); }
        FileInfo fileInfo = new FileInfo(fullPath, true);
        return this.getAccessUrl(bucketName, fileInfo);
    }

    @Override
    public void deleteFile(String bucketName, String fullPath) {
        bucketName = this.checkBucketName(bucketName);
        if (StrUtil.isBlank(fullPath)){ throw new RuntimeException("fullPath 不能为空"); }
        try {
            FileInfo fileInfo = new FileInfo(fullPath, true);
            this.minioClient.removeObject(bucketName, fileInfo.getFullPath());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public List<FileObject> listFileObjects(String bucketName, String prefix) {
        bucketName = this.checkBucketName(bucketName);
        try {
            Iterable<Result<Item>> items = this.minioClient.listObjects(bucketName, prefix);
            List<FileObject> fileObjects = new ArrayList<>(16);
            for (Result<Item> item : items) {
                Item i = item.get();
                FileObject fileObject = new FileObject();
                FileInfo fileInfo = new FileInfo(i.objectName(), !i.isDir());
                fileInfo.setLength(i.size());
                fileObject.setFileInfo(fileInfo);
                fileObject.setUrl(i.objectName());
                fileObject.setAccessUrl(this.getAccessUrl(bucketName, i.objectName()));
                fileObjects.add(fileObject);
            }
            return fileObjects;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public String getChannelId() {
        return CHANNEL_ID;
    }

    @Override
    public String getChannelName() {
        return CHANNEL_NAME;
    }

    /**
     * 获取服务器址
     *
     * @return
     */
    public String getServerUrl() {
        return this.serverUrl;
    }

    /**
     * 获取访问Key
     *
     * @return
     */
    public String getAccessKey() {
        return this.accessKey;
    }

    /**
     * 获取访问Secret
     *
     * @return
     */
    public String getSecretKey() {
        return this.secretKey;
    }

    @Override
    public String toString() {
        return "启用 FileStorage " + this.getChannelName()
                + " 默认分区:" + this.getDefaultBucketName()
                + " 访问根路径:" + this.getEndpoint()
                + " 服务器地址:" + this.getServerUrl();
    }
}
