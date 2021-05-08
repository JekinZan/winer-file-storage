package com.winer.winerfilestorage.properties;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: winer-file-storage
 * @description: 存储客户端抽象属性
 * @Author Jekin
 * @Date 2021/4/25
 */
@Data
public class AbstractStorageClientProperties implements Serializable {

    private static final long serialVersionUID = -7541539316033567660L;

    /**
     * 通道 Bean 前缀
     */
    public static final String CHANNEL_BEAN_PREFIX = "winer";
    /**
     * 通道 Bean 后缀
     */
    public static final String CHANNEL_BEAN_SUFFIX = "StorageClient";

    /**
     * 是否启用
     */
    private boolean enable = false;
    /**
     * 终节点
     */
    private String endpoint;
    /**
     * 默认分区
     */
    private String defaultBucketName;
    /**
     * 读数据块大小
     */
    private int readBlockSize = 2048;
    /**
     * 写数据块大小
     */
    private int writeBlockSize = 2048;
}
