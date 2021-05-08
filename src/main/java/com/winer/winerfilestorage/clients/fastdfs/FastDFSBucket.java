package com.winer.winerfilestorage.clients.fastdfs;
import com.winer.winerfilestorage.base.AbstractBucket;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * FastDFS 分区信息
 *
 * @Description
 */
@ToString(callSuper = true)
@Getter
@Setter
public class FastDFSBucket extends AbstractBucket {
    private static final long serialVersionUID = -1799448666278756043L;

    /**
     * @param name
     */
    public FastDFSBucket(String name) {
        super(name);
    }
}
