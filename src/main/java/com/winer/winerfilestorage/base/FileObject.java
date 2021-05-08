package com.winer.winerfilestorage.base;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @program: winer-file-storage-dev
 * @description: 文件保存对象
 * @Author Jekin
 * @Date 2021/4/26
 */
@Getter
@Setter
public class FileObject implements Serializable {
    private static final long serialVersionUID = 3296472238979852249L;
    private FileInfo fileInfo;
    /**
     * 获取url
     */
    private String url;
    /**
     * 获取访问文件夹
     */
    private String accessUrl;

    @Override
    public String toString() {
        return "url = " + this.getUrl() + " accessUrl = " + this.getAccessUrl() + " fileInfo -> " + getFileInfo();
    }
}
