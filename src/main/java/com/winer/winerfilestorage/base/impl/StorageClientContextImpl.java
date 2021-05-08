package com.winer.winerfilestorage.base.impl;
import com.winer.winerfilestorage.channel.AbstractChannelContext;
import com.winer.winerfilestorage.base.StorageClient;
import com.winer.winerfilestorage.base.StorageClientContext;

/**
 * 存储客户实现
 * <p>
 * </p>
 *
 * @description TODO
 * @author: 老码农
 * @create: 2020-03-02 23:05
 **/
public class StorageClientContextImpl extends AbstractChannelContext<StorageClient> implements StorageClientContext {
    
    /**
     *
     */
    public StorageClientContextImpl() {
        super(16);
    }
}
