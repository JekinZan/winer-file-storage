package com.winer.winerfilestorage.properties;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * @program: winer-file-storage
 * @description: 存储属性
 * @Author Jekin
 * @Date 2021/4/25
 */
@Data
@ToString(callSuper = true)
@ConfigurationProperties(prefix = WinerStorageProperties.PREFIX)
public class WinerStorageProperties implements Serializable {

    private static final long serialVersionUID = -292579288216952713L;

    /**
     * 属性前缀
     */
    public final static String PREFIX = "winer.storage.client";

    /**
     * 阿里云属性
     */
    private AliyunStorageClientProperties aliyun = new AliyunStorageClientProperties();


    /**
     * 本地磁盘属性
     */
    private LocalStorageClientProperties local = new LocalStorageClientProperties();

    /**
     * FastDFS 属性
     */
    private FastDFSStorageClientProperties fastDFS = new FastDFSStorageClientProperties();

    /**
     * Minio 属性
     */
    private MinioStorageClientProperties minio = new MinioStorageClientProperties();

    /**
     *
     */
    public WinerStorageProperties() {

    }
}
