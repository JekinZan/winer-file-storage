package com.winer.winerfilestorage.clients.aliyun;

import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.model.CannedAccessControlList;
import com.winer.winerfilestorage.base.AbstractStorageKeyClientProperties;
import com.winer.winerfilestorage.properties.WinerStorageProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @program: winer-file-storage-dev
 * @description: 阿里云存储属性
 * @Author Jekin
 * @Date 2021/5/8
 */
@ToString(callSuper = true)
@Getter
@Setter
public class AliyunStorageClientProperties extends AbstractStorageKeyClientProperties {

    private static final long serialVersionUID = 6651972119635445024L;

    /**
     * bean条件属性
     */
    public static final String BEAN_CONDITIONAL_PROPERTY = WinerStorageProperties.PREFIX + ".aliyun.enable";

    /**
     * 通道 Bean 名称
     */
    public static final String CHANNEL_BEAN_NAME = CHANNEL_BEAN_PREFIX + "Aliyun" + CHANNEL_BEAN_SUFFIX;

    /**
     * acl权限
     */
    private String cannedACL = "public-read"; // default、private、public-read、public-read-write

    /**
     * @return
     */
    public CannedAccessControlList toCannedAccessControlList() {
        if (StrUtil.isEmpty(this.getCannedACL())) {
            return CannedAccessControlList.PublicRead;
        }
        try {
            return CannedAccessControlList.parse(this.getCannedACL());
        } catch (Exception e) {
            return CannedAccessControlList.PublicRead;
        }
    }
}
