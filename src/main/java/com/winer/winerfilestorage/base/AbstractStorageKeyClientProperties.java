package com.winer.winerfilestorage.base;

import com.winer.winerfilestorage.properties.AbstractStorageClientProperties;
import lombok.Data;

/**
 * @program: winer-file-storage-dev
 * @description: 具有访问键的客户端熟悉
 * @Author Jekin
 * @Date 2021/4/26
 */
@Data
public class AbstractStorageKeyClientProperties extends AbstractStorageClientProperties {

    private static final long serialVersionUID = 5072627775913663379L;


    /**
     * 访问键
     */
    private String accessKey = "";

    /**
     * 安全键
     */
    private String secretKey = "";
}
