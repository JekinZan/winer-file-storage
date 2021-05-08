package com.winer.winerfilestorage.channel;

/**
 * 通道
 * 提供各种通道管理
 **/
public interface Channel {

    /**
     * 获取通道id
     *
     * @return
     */
    String getChannelId();

    /**
     * 获取通道名称
     *
     * @return
     */
    String getChannelName();
}
