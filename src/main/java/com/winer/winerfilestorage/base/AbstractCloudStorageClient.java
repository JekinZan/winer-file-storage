package com.winer.winerfilestorage.base;

import com.winer.winerfilestorage.properties.AbstractStorageClientProperties;
import com.winer.winerfilestorage.utils.StringUtils;
import lombok.Getter;
import sun.swing.StringUIClientPropertyKey;

/**
 * @program: winer-file-storage-dev
 * @description: 云存储客户端
 * @Author Jekin
 * @Date 2021/4/26
 */
@Getter
public abstract class AbstractCloudStorageClient<TBucket extends AbstractBucket> extends AbstractStorageClient<TBucket>{
    private final String accessKey;
    private final String secretKey;
    private final String httpProtocol;
    private final String httpDomainName;

    /**
     * AbstractCloudStorageClient
     *
     * @param endpoint
     * @param defaultBucketName
     * @param accessKey
     * @param secretKey
     */
    public AbstractCloudStorageClient(String endpoint, String defaultBucketName,
                                      String accessKey, String secretKey) {
        super(StringUtils.removeALLWhitespace(endpoint), defaultBucketName);
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        int index = this.getEndpoint().indexOf("//");
        if (index > 2) {
            this.httpProtocol = this.getEndpoint().substring(0, index - 1);
            this.httpDomainName = this.getEndpoint().substring(index + 2);
        } else {
            this.httpProtocol = "http";
            this.httpDomainName = this.getEndpoint();
        }
    }

    /**
     * MinioStorageClient
     *
     * @param properties 属性
     */
    public AbstractCloudStorageClient(AbstractStorageKeyClientProperties properties) {
        this(properties.getEndpoint(),
                properties.getDefaultBucketName(),
                properties.getAccessKey(),
                properties.getSecretKey());
        this.setStorageClientProperties(properties);
    }


    /**
     * 获取访问Url
     *
     * @param bucketName 分区名称
     * @param fileInfo
     * @return
     */
    protected String getAccessUrl(String bucketName, FileInfo fileInfo) {
        String domainName = this.getHttpProtocol() + "://" + bucketName + "." + this.getHttpDomainName();
        return this.getPathAddress(domainName, fileInfo.getFullPath());
    }

    @Override
    public String getAccessUrl(String bucketName, String fullPath) {
        bucketName = this.checkBucketName(bucketName);
        if (StringUtils.isNullOrBlank(fullPath)) {
            throw new RuntimeException("完整路径为空.");
        }
        FileInfo fileInfo = new FileInfo(fullPath, true);
        return this.getAccessUrl(this.checkBucketName(bucketName), fileInfo);
    }
}
