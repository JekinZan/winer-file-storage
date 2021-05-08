package com.winer.winerfilestorage.base;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: winer-file-storage-dev
 * @description: 分区信息抽象
 * @Author Jekin
 * @Date 2021/4/26
 */
@AllArgsConstructor
@Data
public abstract class AbstractBucket implements Serializable {

    private static final long serialVersionUID = 4279191837708642575L;
    private final String name;

    @Override
    public String toString() {
        return "name = " + this.getName();
    }

}
