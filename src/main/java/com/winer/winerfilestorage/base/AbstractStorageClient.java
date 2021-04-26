package com.winer.winerfilestorage.base;

import com.winer.winerfilestorage.properties.AbstractStorageClientProperties;
import com.winer.winerfilestorage.utils.StringUtils;
import com.winer.winerfilestorage.utils.tuple.TupleTwo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.winer.winerfilestorage.base.FileObject;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: winer-file-storage-dev
 * @description: 存储客户端抽象
 * @Author Jekin
 * @Date 2021/4/26
 */
@Slf4j
public abstract class AbstractStorageClient<TBucket extends AbstractBucket> implements StorageClient{
    private static final Map<String, TupleTwo<String, Integer>> EXTENSION_CONTENT_TYPE_MAP = new HashMap<>(16);
    /**
     * URL 分隔符
     */
    public static final char URL_SEPARATOR = '/';
    private final String endpoint;
    private final String defaultBucketName;
    private int writeBlockSize = 2048;
    private int readBlockSize = 4096;


    static {
        EXTENSION_CONTENT_TYPE_MAP.put("jpg", new TupleTwo<>("image/jpg", 1));
        EXTENSION_CONTENT_TYPE_MAP.put("png", new TupleTwo<>("image/png", 1));
        EXTENSION_CONTENT_TYPE_MAP.put("bmp", new TupleTwo<>("image/bmp", 1));
        EXTENSION_CONTENT_TYPE_MAP.put("tif", new TupleTwo<>("image/tif", 1));
        EXTENSION_CONTENT_TYPE_MAP.put("gif", new TupleTwo<>("image/gif", 1));
        EXTENSION_CONTENT_TYPE_MAP.put("jpeg", new TupleTwo<>("image/jpeg", 1));
        EXTENSION_CONTENT_TYPE_MAP.put("tiff", new TupleTwo<>("image/tiff", 1));
        EXTENSION_CONTENT_TYPE_MAP.put("dib", new TupleTwo<>("image/dib", 1));
        EXTENSION_CONTENT_TYPE_MAP.put("html", new TupleTwo<>("text/html", 2));
        EXTENSION_CONTENT_TYPE_MAP.put("xml", new TupleTwo<>("text/xml", 2));
        EXTENSION_CONTENT_TYPE_MAP.put("plain", new TupleTwo<>("text/plain", 2));
        EXTENSION_CONTENT_TYPE_MAP.put("json", new TupleTwo<>("application/json", 3));
        EXTENSION_CONTENT_TYPE_MAP.put("pdf", new TupleTwo<>("application/pdf", 4));
    }

    @Override
    public abstract TBucket createBucket(String bucketName);

    @Override
    public abstract TBucket getBucket(String bucketName);

    /**
     * 日志
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 实例化
     *
     * @param properties 属性
     */
    public AbstractStorageClient(AbstractStorageClientProperties properties) {
        this(properties.getEndpoint(), properties.getDefaultBucketName());
    }
    /**
     * AbstractStorageClient
     *
     * @param endpoint          终节点
     * @param defaultBucketName 默认分区
     */
    public AbstractStorageClient(String endpoint, String defaultBucketName) {
        if (StringUtils.isNullOrBlank(endpoint)) {
            throw new RuntimeException("终节点(url根路径)不能为空。");
        }
        this.endpoint = StringUtils.removeStart(StringUtils.removeEnd(endpoint, URL_SEPARATOR), URL_SEPARATOR)
                .toLowerCase();
        if (defaultBucketName == null) {
            defaultBucketName = "";
        } else {
            defaultBucketName = this.checkBucketName(defaultBucketName);
        }
        this.defaultBucketName = defaultBucketName.trim().toLowerCase();
    }

    @Override
    public final FileObject saveFile(String bucketName, String fullPath, InputStream input) throws Exception {
        FileStorageRequest request = new FileStorageRequest(bucketName, fullPath, input);
        return this.saveFile(request);
    }

    /**
     * 设置客户端属性
     *
     * @param properties 属性
     */
    protected void setStorageClientProperties(AbstractStorageClientProperties properties) {
        this.setReadBlockSize(properties.getReadBlockSize());
        this.setWriteBlockSize(properties.getWriteBlockSize());
    }
    /**
     * 检查分区名称
     *
     * @param bucketName 分区名称
     * @return
     */
    protected String checkBucketName(String bucketName) {
        if (StringUtils.isNotNullOrBlank(bucketName)) {
            return bucketName;
        }
        if (StringUtils.isNullOrBlank(this.getDefaultBucketName())) {
            throw new RuntimeException("默认分区名为空.");
        }
        return this.getDefaultBucketName();
    }
}
