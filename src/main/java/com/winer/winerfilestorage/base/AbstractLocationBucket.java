package com.winer.winerfilestorage.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


/**
 * @program: winer-file-storage-dev
 * @description: 抽象位置分区
 * @Author Jekin
 * @Date 2021/4/26
 */
@Getter
@Setter
@ToString(callSuper = true)
public class AbstractLocationBucket extends AbstractBucket{


    private static final long serialVersionUID = 4418466106066585396L;
    /**
     * 位置
     */
    private String location;

    /**
     * 创建时间
     */
    private LocalDateTime creationDate;

    public AbstractLocationBucket(String name) {
        super(name);
    }
}
