package com.winer.winerfilestorage.utils.tuple;

import java.io.Serializable;

/**
 * @program: winer-file-storage-dev
 * @description: 基本元祖
 * @Author Jekin
 * @Date 2021/4/26
 */
public class Tuple<T1> implements Serializable {
    private static final long serialVersionUID = 5494366050647308950L;
    private final T1 item1;

    /**
     * 实例化 Unit 类
     *
     */
    public Tuple(T1 item1) {
        this.item1 = item1;
    }

    /**
     * 获取项目1
     *
     */
    public T1 getItem1() {
        return item1;
    }
}
