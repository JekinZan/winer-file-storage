package com.winer.winerfilestorage.clients.minio;

import com.winer.winerfilestorage.base.AbstractBucket;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Minio 分区
 **/
@ToString(callSuper = true)
@Getter
@Setter
public class MinioBucket extends AbstractBucket {

    private static final long serialVersionUID = -4987878962501829389L;

    /**
     * @param name
     */
    public MinioBucket(String name) {
        super(name);
    }


}
