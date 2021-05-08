package com.winer.winerfilestorage.clients.local;

import cn.hutool.core.util.StrUtil;
import com.winer.winerfilestorage.properties.AbstractStorageClientProperties;
import com.winer.winerfilestorage.properties.WinerStorageProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 本地存储属性
 *
 * @Description TODO
 * @Author 老码农
 */
@ToString(callSuper = true)
@Getter
@Setter
public class LocalStorageClientProperties extends AbstractStorageClientProperties {
    private static final long serialVersionUID = -8781765375269535591L;

    /**
     * bean条件属性
     */
    public static final String BEAN_CONDITIONAL_PROPERTY = WinerStorageProperties.PREFIX + ".local.enable";

    /**
     * 通道 Bean 名称
     */
    public static final String CHANNEL_BEAN_NAME = CHANNEL_BEAN_PREFIX + "Local" + CHANNEL_BEAN_SUFFIX;

    /**
     * 默认终节点
     */
    public static final String DEFAULT_ENDPOINT = "/";

    /**
     * 默认分区
     */
    public static final String DEFAULT_BUCKET_NAME = "files";

    /**
     * 根文件路径
     */
    private String rootFilePath = "";

    public LocalStorageClientProperties() {
        this.setEndpoint(DEFAULT_ENDPOINT);
        this.setRootFilePath("");
        this.setDefaultBucketName(DEFAULT_BUCKET_NAME);
    }

    /**
     * 初始化属性
     */
    public void initByProperties() {
        if (StrUtil.isBlank(this.getEndpoint())) {
            this.setEndpoint(LocalStorageClientProperties.DEFAULT_ENDPOINT);
        }
        if (StrUtil.isBlank(this.getDefaultBucketName())) {
            this.setDefaultBucketName(LocalStorageClientProperties.DEFAULT_BUCKET_NAME);
        }
        if (StrUtil.isBlank(this.getRootFilePath())) {
            this.setRootFilePath(LocalStorageClient.getDefaultLocalPath());
        }
        if (StrUtil.isBlank(this.getRootFilePath())) {
            throw new RuntimeException("本地文件存储无默认保存路径 rootFilePath。");
        }
    }
}
