package com.winer.winerfilestorage.clients.aliyun;

import com.winer.winerfilestorage.base.AbstractLocationBucket;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @program: winer-file-storage-dev
 * @description: 阿里云分区信息
 * @Author Jekin
 * @Date 2021/4/26
 */
@ToString(callSuper = true)
@Setter
@Getter
public class AliyunBucket extends AbstractLocationBucket {

    private static final long serialVersionUID = 3991689031661897156L;

    private String extranetEndpoint;

    private String intranetEndpoint;

    public AliyunBucket(String name) {
        super(name);
    }
}
