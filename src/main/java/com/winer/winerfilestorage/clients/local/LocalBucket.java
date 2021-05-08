package com.winer.winerfilestorage.clients.local;
import com.winer.winerfilestorage.base.AbstractLocationBucket;
import lombok.ToString;

/**
 * 本地分区
 *
 */
@ToString(callSuper = true)
public class LocalBucket extends AbstractLocationBucket {

    /**
     *
     */
    private static final long serialVersionUID = 4634807388895689305L;


    public LocalBucket(String name) {
        super(name);
    }
}
