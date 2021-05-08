package com.winer.winerfilestorage.clients.minio;
import com.winer.winerfilestorage.base.AbstractStorageKeyClientProperties;
import com.winer.winerfilestorage.properties.WinerStorageProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Minio 存储客户端属性
 * <p>
 * </p>
 *
 * @description TODO
 * @author: 老码农
 * @create: 2020-06-27 00:54
 **/
@ToString
@Getter
@Setter
public class MinioStorageClientProperties extends AbstractStorageKeyClientProperties {

    private static final long serialVersionUID = -6332425646084541191L;

    /**
     * bean条件属性
     */
    public static final String BEAN_CONDITIONAL_PROPERTY = WinerStorageProperties.PREFIX + ".minio.enable";

    /**
     * 通道 Bean 名称
     */
    public static final String CHANNEL_BEAN_NAME = CHANNEL_BEAN_PREFIX + "Minio" + CHANNEL_BEAN_SUFFIX;

    /**
     * Minio 服务器地址
     */
    private String serverUrl = "http://127.0.0.1:9000";

}
