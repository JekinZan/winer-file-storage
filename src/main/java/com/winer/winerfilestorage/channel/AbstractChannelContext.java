package com.winer.winerfilestorage.channel;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.DisposableBean;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通道上下文抽象
 **/
public abstract class AbstractChannelContext<TChannel extends Channel> implements ChannelContext<TChannel>, DisposableBean {

    private final Map<String, TChannel> channelMap;

    public AbstractChannelContext() {
        this(16);
    }

    /**
     * @param initialCapacity
     */
    public AbstractChannelContext(int initialCapacity) {
        this.channelMap = new ConcurrentHashMap<>(initialCapacity);
    }

    private String getKey(TChannel channel) {
        if (channel == null) {
            throw new RuntimeException("channel 不能为空");
        }
        if (StrUtil.isBlank(channel.getChannelId())) {
            throw new RuntimeException("channelId 不能为 null 或空白值。");
        }
        return channel.getChannelId().toUpperCase();
    }

    private String getKey(String channelId) {
        if (StrUtil.isBlank(channelId)) {
            throw new RuntimeException("channelId 不能为 null 或空白值。");
        }
        return channelId.toUpperCase();
    }

    @Override
    public void register(TChannel channel) {
        this.channelMap.put(getKey(channel), channel);
    }

    @Override
    public TChannel getChannel(String channelId) {
        return this.channelMap.get(this.getKey(channelId));
    }

    @Override
    public boolean exist(String channelId) {
        return this.channelMap.containsKey(this.getKey(channelId));
    }

    @Override
    public boolean removeChannel(String channelId) {
        return this.channelMap.remove(this.getKey(channelId)) != null;
    }

    @Override
    public void clearAll() {
        this.channelMap.clear();
    }

    @Override
    public Set<String> channelIdSet() {
        return this.channelMap.keySet();
    }

    @Override
    public Collection<TChannel> channels() {
        return this.channelMap.values();
    }

    @Override
    public void destroy() throws Exception {
        this.channelMap.clear();
    }
}
